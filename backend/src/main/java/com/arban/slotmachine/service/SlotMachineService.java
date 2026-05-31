package com.arban.slotmachine.service;

import com.arban.slotmachine.dto.SpinRequestDTO;
import com.arban.slotmachine.dto.SpinResponseDTO;
import com.arban.slotmachine.entity.GameSession;

import java.util.List;

/**
 * Service contract for slot machine operations.
 * Programming to interface — not implementation.
 */
public interface SlotMachineService {
    SpinResponseDTO spin(SpinRequestDTO request);
    GameSession getOrCreateSession(String playerName);
    List<GameSession> getLeaderboard();
    GameSession resetSession(String playerName);
}
