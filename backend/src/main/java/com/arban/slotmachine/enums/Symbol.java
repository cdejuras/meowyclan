package com.arban.slotmachine.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Symbol enum representing each slot reel symbol.
 * Each symbol has a weight (probability) and payout multiplier.
 * Lower weight = rarer = higher payout.
 */
@Getter
@RequiredArgsConstructor
public enum Symbol {

    CHERRY  ("🍒", 30, 2),
    GRAPE   ("🍇", 25, 3),
    LEMON   ("🍋", 20, 4),
    STAR    ("⭐", 12, 8),
    CLOVER  ("🍀", 8,  10),
    SEVEN   ("7️⃣", 4,  20),
    DIAMOND ("💎", 1,  50);

    private final String emoji;
    private final int weight;       // probability weight
    private final int payout;       // multiplier on match

    public static final int TOTAL_WEIGHT =
            java.util.Arrays.stream(values())
                    .mapToInt(Symbol::getWeight)
                    .sum();
}
