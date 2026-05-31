package com.slotmachine.entity;

import jakarta.persistence.*;

// Encapsulation: private fields with public getters/setters
@Entity
@Table(name = "slot_sessions")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String playerName;

    @Column(nullable = false)
    private double balance;

    @Column(nullable = false)
    private int spinCount;

    @Column(nullable = false)
    private int winCount;

    // Constructors
    public Slot() {}

    public Slot(String playerName, double initialBalance) {
        this.playerName = playerName;
        this.balance = initialBalance;
        this.spinCount = 0;
        this.winCount = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }

    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public int getSpinCount() { return spinCount; }
    public void setSpinCount(int spinCount) { this.spinCount = spinCount; }

    public int getWinCount() { return winCount; }
    public void setWinCount(int winCount) { this.winCount = winCount; }

    // Business logic: increment spin and win count
    public void recordSpin(boolean won) {
        this.spinCount++;
        if (won) this.winCount++;
    }

    public double getWinRate() {
        if (spinCount == 0) return 0.0;
        return (double) winCount / spinCount * 100;
    }
}
