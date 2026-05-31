package com.arban.slotmachine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a player attempts to spin without enough coins.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientCoinsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InsufficientCoinsException(String playerName, int coins) {
        super(String.format("Player '%s' has insufficient coins (%d). Minimum required: 10.", playerName, coins));
    }
}
