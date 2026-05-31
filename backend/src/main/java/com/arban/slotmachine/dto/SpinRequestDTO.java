package com.arban.slotmachine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for incoming spin requests.
 * Validates player name before processing.
 */
@Data
public class SpinRequestDTO {

    @NotBlank(message = "Player name is required")
    @Size(min = 2, max = 30, message = "Player name must be between 2 and 30 characters")
    private String playerName;
}
