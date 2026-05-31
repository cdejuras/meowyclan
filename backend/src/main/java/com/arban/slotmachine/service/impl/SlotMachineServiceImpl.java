package com.arban.slotmachine.service.impl;

import com.arban.slotmachine.dto.SpinRequestDTO;
import com.arban.slotmachine.dto.SpinResponseDTO;
import com.arban.slotmachine.entity.GameSession;
import com.arban.slotmachine.enums.Symbol;
import com.arban.slotmachine.exception.InsufficientCoinsException;
import com.arban.slotmachine.repository.GameSessionRepository;
import com.arban.slotmachine.service.SlotMachineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Core slot machine business logic.
 *
 * Spin Logic:
 * - Each spin costs 10 coins
 * - Symbols are picked via weighted random (rarer = less likely)
 * - 3 matching symbols = win (payout = bet × symbol.payout)
 * - 2 matching symbols = partial win (payout = bet × 0.5)
 * - New players start with 100 coins
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SlotMachineServiceImpl implements SlotMachineService {

    private static final int SPIN_COST    = 10;
    private static final int STARTING_COINS = 100;
    private final Random random = new Random();

    private final GameSessionRepository gameSessionRepository;

    @Override
    public SpinResponseDTO spin(SpinRequestDTO request) {
        final String playerName = request.getPlayerName();
        final GameSession session = getOrCreateSession(playerName);

        validateCoins(session);

        // Deduct spin cost
        session.setCoins(session.getCoins() - SPIN_COST);
        session.setTotalSpins(session.getTotalSpins() + 1);

        // Spin the reels
        final List<Symbol> reels = spinReels();
        final int coinsWon = calculatePayout(reels, SPIN_COST);
        final boolean isWin = coinsWon > 0;

        if (isWin) {
            session.setCoins(session.getCoins() + coinsWon);
            session.setTotalWins(session.getTotalWins() + 1);
            session.setTotalWinnings(session.getTotalWinnings() + coinsWon);
        }

        gameSessionRepository.save(session);
        log.info("Player '{}' spun: {} | won: {} coins", playerName, reels, coinsWon);

        return buildResponse(session, reels, isWin, coinsWon);
    }

    @Override
    public GameSession getOrCreateSession(String playerName) {
        return gameSessionRepository.findByPlayerName(playerName)
                .orElseGet(() -> createNewSession(playerName));
    }

    @Override
    public List<GameSession> getLeaderboard() {
        return gameSessionRepository.findTop10ByOrderByTotalWinningsDesc();
    }

    @Override
    public GameSession resetSession(String playerName) {
        final GameSession session = getOrCreateSession(playerName);
        session.setCoins(STARTING_COINS);
        session.setTotalSpins(0);
        session.setTotalWins(0);
        session.setTotalWinnings(0);
        return gameSessionRepository.save(session);
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    private void validateCoins(GameSession session) {
        if (session.getCoins() < SPIN_COST) {
            throw new InsufficientCoinsException(session.getPlayerName(), session.getCoins());
        }
    }

    private List<Symbol> spinReels() {
        final List<Symbol> reels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            reels.add(pickWeightedSymbol());
        }
        return reels;
    }

    /**
     * Weighted random symbol picker.
     * Higher weight = more likely to appear.
     */
    private Symbol pickWeightedSymbol() {
        int roll = random.nextInt(Symbol.TOTAL_WEIGHT);
        for (Symbol symbol : Symbol.values()) {
            roll -= symbol.getWeight();
            if (roll < 0) return symbol;
        }
        return Symbol.CHERRY; // fallback (should never reach)
    }

    /**
     * Payout rules:
     * - 3 matching symbols → bet × symbol.payout
     * - 2 matching symbols → bet × 0.5 (half back)
     * - No match → 0
     */
    private int calculatePayout(List<Symbol> reels, int bet) {
        final Symbol first = reels.get(0);
        final Symbol second = reels.get(1);
        final Symbol third = reels.get(2);

        if (first == second && second == third) {
            return bet * first.getPayout();
        }
        if (first == second || second == third || first == third) {
            return bet / 2;
        }
        return 0;
    }

    private GameSession createNewSession(String playerName) {
        final GameSession session = GameSession.builder()
                .playerName(playerName)
                .coins(STARTING_COINS)
                .totalSpins(0)
                .totalWins(0)
                .totalWinnings(0)
                .build();
        return gameSessionRepository.save(session);
    }

    private SpinResponseDTO buildResponse(GameSession session, List<Symbol> reels,
                                          boolean isWin, int coinsWon) {
        return SpinResponseDTO.builder()
                .playerName(session.getPlayerName())
                .reels(reels.stream().map(Symbol::getEmoji).toList())
                .isWin(isWin)
                .coinsWon(coinsWon)
                .remainingCoins(session.getCoins())
                .totalSpins(session.getTotalSpins())
                .totalWins(session.getTotalWins())
                .message(buildMessage(reels, isWin, coinsWon))
                .build();
    }

    private String buildMessage(List<Symbol> reels, boolean isWin, int coinsWon) {
        if (!isWin) return "No match. Try again!";
        final Symbol first = reels.get(0);
        final boolean isJackpot = reels.stream().allMatch(s -> s == first);
        if (isJackpot && first == Symbol.DIAMOND) return "💎 JACKPOT! Incredible!";
        if (isJackpot) return "🎉 Three of a kind! You won " + coinsWon + " coins!";
        return "✨ Two of a kind! You got " + coinsWon + " coins back!";
    }
}
