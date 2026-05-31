package com.slotmachine.payment;

import org.springframework.stereotype.Component;

// Inheritance: implements PaymentProcessor interface
// Polymorphism: SlotService uses PaymentProcessor type, so this can be swapped easily
@Component
public class CashPaymentProcessor implements PaymentProcessor {

    @Override
    public double deposit(double currentBalance, double amount) {
        if (amount < MIN_DEPOSIT) {
            throw new IllegalArgumentException(
                "Minimum deposit is PHP " + MIN_DEPOSIT);
        }
        if (amount > MAX_DEPOSIT) {
            throw new IllegalArgumentException(
                "Maximum deposit is PHP " + MAX_DEPOSIT);
        }
        return Math.round((currentBalance + amount) * 100.0) / 100.0;
    }

    @Override
    public double withdraw(double currentBalance, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount > currentBalance) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        return Math.round((currentBalance - amount) * 100.0) / 100.0;
    }
}
