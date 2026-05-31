package com.slotmachine.request;

// Represents what the client sends when making a spin request
public class SlotRequest {

    private double betAmount;

    public SlotRequest() {}

    public SlotRequest(double betAmount) {
        this.betAmount = betAmount;
    }

    public double getBetAmount() { return betAmount; }
    public void setBetAmount(double betAmount) { this.betAmount = betAmount; }
}
