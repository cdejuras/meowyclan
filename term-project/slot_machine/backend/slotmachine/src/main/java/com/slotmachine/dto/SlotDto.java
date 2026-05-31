package com.slotmachine.dto;

// Data Transfer Object - separates internal entity from what we expose to frontend
public class SlotDto {

    private Long id;
    private String playerName;
    private double balance;
    private int spinCount;
    private int winCount;
    private double winRate;

    public SlotDto() {}

    public SlotDto(Long id, String playerName, double balance,
                   int spinCount, int winCount, double winRate) {
        this.id = id;
        this.playerName = playerName;
        this.balance = balance;
        this.spinCount = spinCount;
        this.winCount = winCount;
        this.winRate = winRate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public int getSpinCount() { return spinCount; }
    public void setSpinCount(int spinCount) { this.spinCount = spinCount; }

    public int getWinCount() { return winCount; }
    public void setWinCount(int winCount) { this.winCount = winCount; }

    public double getWinRate() { return winRate; }
    public void setWinRate(double winRate) { this.winRate = winRate; }
}
