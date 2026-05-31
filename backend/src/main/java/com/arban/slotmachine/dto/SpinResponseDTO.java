package com.arban.slotmachine.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * DTO for spin result response.
 * Decouples entity from API layer.
 */
@Data
@Builder
public class SpinResponseDTO {
    private String playerName;
    private List<String> reels;       // 3 emoji symbols
    private boolean isWin;
    private int coinsWon;
    private int remainingCoins;
    private int totalSpins;
    private int totalWins;
    private String message;
}
