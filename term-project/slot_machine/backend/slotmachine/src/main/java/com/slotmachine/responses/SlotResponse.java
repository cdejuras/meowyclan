package com.slotmachine.responses;

import java.util.List;

// Represents the result of a spin sent back to the client
public class SlotResponse {

    private List<String> symbols;   // The 3 reels result
    private boolean win;
    private String winType;         // "JACKPOT", "TWO_MATCH", "NO_WIN"
    private double payout;
    private double newBalance;
    private int spinCount;
    private int winCount;
    private double winRate;
    private String message;

    public SlotResponse() {}

    // Getters and Setters
    public List<String> getSymbols() { return symbols; }
    public void setSymbols(List<String> symbols) { this.symbols = symbols; }

    public boolean isWin() { return win; }
    public void setWin(boolean win) { this.win = win; }

    public String getWinType() { return winType; }
    public void setWinType(String winType) { this.winType = winType; }

    public double getPayout() { return payout; }
    public void setPayout(double payout) { this.payout = payout; }

    public double getNewBalance() { return newBalance; }
    public void setNewBalance(double newBalance) { this.newBalance = newBalance; }

    public int getSpinCount() { return spinCount; }
    public void setSpinCount(int spinCount) { this.spinCount = spinCount; }

    public int getWinCount() { return winCount; }
    public void setWinCount(int winCount) { this.winCount = winCount; }

    public double getWinRate() { return winRate; }
    public void setWinRate(double winRate) { this.winRate = winRate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
