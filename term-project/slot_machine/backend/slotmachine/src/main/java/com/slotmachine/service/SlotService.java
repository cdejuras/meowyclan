package com.slotmachine.service;

import com.slotmachine.dto.SlotDto;
import com.slotmachine.entity.Slot;
import com.slotmachine.mapper.SlotMapper;
import com.slotmachine.payment.PaymentProcessor;
import com.slotmachine.repository.SlotRepository;
import com.slotmachine.request.SlotRequest;
import com.slotmachine.responses.SlotResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SlotService {

    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;
    private final PaymentProcessor paymentProcessor;
    private final SlotMachine slotMachine;

    public SlotService(SlotRepository slotRepository,
                       SlotMapper slotMapper,
                       PaymentProcessor paymentProcessor) {
        this.slotRepository = slotRepository;
        this.slotMapper = slotMapper;
        this.paymentProcessor = paymentProcessor;
        this.slotMachine = new SlotMachine();
    }

    public SlotDto createSession(String playerName, double initialDeposit) {
        double balance = paymentProcessor.deposit(0, initialDeposit);
        Slot slot = new Slot(playerName, balance);
        slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    public Optional<SlotDto> getSession(Long id) {
        return slotRepository.findById(id).map(slotMapper::toDto);
    }

    public List<SlotDto> getAllSessions() {
        return slotRepository.findAllOrderedByWins()
                .stream()
                .map(slotMapper::toDto)
                .toList();
    }

    public SlotResponse spin(Long sessionId, SlotRequest request) {
        Slot slot = slotRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        double bet = request.getBetAmount();

        if (bet <= 0) throw new IllegalArgumentException("Bet must be positive");
        if (bet > slot.getBalance()) throw new IllegalArgumentException("Insufficient balance");

        slot.setBalance(Math.round((slot.getBalance() - bet) * 100.0) / 100.0);

        List<String> reels = slotMachine.spin();
        String winType = slotMachine.evaluateResult(reels);
        boolean isWin = !winType.equals("NO_WIN");
        double payout = slotMachine.calculatePayout(bet, winType);

        slot.setBalance(Math.round((slot.getBalance() + payout) * 100.0) / 100.0);
        slot.recordSpin(isWin);
        slotRepository.save(slot);

        SlotResponse response = new SlotResponse();
        response.setSymbols(reels);
        response.setWin(isWin);
        response.setWinType(winType);
        response.setPayout(payout);
        response.setNewBalance(slot.getBalance());
        response.setSpinCount(slot.getSpinCount());
        response.setWinCount(slot.getWinCount());
        response.setWinRate(slot.getWinRate());
        response.setMessage(buildMessage(winType, payout));

        return response;
    }

    public SlotDto deposit(Long sessionId, double amount) {
        Slot slot = slotRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        double newBalance = paymentProcessor.deposit(slot.getBalance(), amount);
        slot.setBalance(newBalance);
        slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    public SlotDto withdraw(Long sessionId, double amount) {
        Slot slot = slotRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        double newBalance = paymentProcessor.withdraw(slot.getBalance(), amount);
        slot.setBalance(newBalance);
        slotRepository.save(slot);
        return slotMapper.toDto(slot);
    }

    public void deleteSession(Long sessionId) {
        slotRepository.deleteById(sessionId);
    }

    private String buildMessage(String winType, double payout) {
        return switch (winType) {
            case "JACKPOT"   -> "JACKPOT! You won PHP " + payout + "!";
            case "TWO_MATCH" -> "Two of a kind! You won PHP " + payout + "!";
            default          -> "No match. Try again!";
        };
    }
}