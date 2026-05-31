package com.slotmachine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlotMachine {

    private static final String[] WEIGHTED_POOL = {
            "Cherry", "Cherry", "Cherry", "Cherry", "Cherry",
            "Lemon", "Lemon", "Lemon", "Lemon",
            "Orange", "Orange", "Orange",
            "Grape", "Grape", "Grape",
            "Star", "Star",
            "Bell", "Bell",
            "Diamond",
            "Seven"
    };

    private final Random random = new Random();
    private List<String> customSymbols = null;

    public void setCustomSymbols(List<String> symbols) {
        this.customSymbols = symbols;
    }

    public List<String> spin() {
        String[] pool = (customSymbols != null && customSymbols.size() >= 3)
                ? customSymbols.toArray(new String[0])
                : WEIGHTED_POOL;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            result.add(pool[random.nextInt(pool.length)]);
        }
        return result;
    }

    public String evaluateResult(List<String> reels) {
        String a = reels.get(0);
        String b = reels.get(1);
        String c = reels.get(2);
        if (a.equals(b) && b.equals(c)) return "JACKPOT";
        else if (a.equals(b) || b.equals(c) || a.equals(c)) return "TWO_MATCH";
        return "NO_WIN";
    }

    public double calculatePayout(double bet, String winType) {
        return switch (winType) {
            case "JACKPOT"   -> Math.round(bet * 5.0 * 100.0) / 100.0;
            case "TWO_MATCH" -> Math.round(bet * 1.5 * 100.0) / 100.0;
            default          -> 0.0;
        };
    }
}