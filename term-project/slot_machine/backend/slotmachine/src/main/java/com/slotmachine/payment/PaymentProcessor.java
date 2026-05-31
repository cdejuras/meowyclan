package com.slotmachine.payment;

// Abstraction: defines payment behavior as an interface
// Other classes can implement this (e.g., CashPaymentProcessor, OnlinePaymentProcessor)
public interface PaymentProcessor {

    double MIN_DEPOSIT = 100.0;    // PHP 100
    double MAX_DEPOSIT = 10000.0;  // PHP 10,000

    /**
     * Deposit money into the player's balance.
     * @param currentBalance the player's current balance
     * @param amount the amount to deposit
     * @return the new balance after deposit
     * @throws IllegalArgumentException if amount is out of range
     */
    double deposit(double currentBalance, double amount);

    /**
     * Withdraw money from the player's balance.
     * @param currentBalance the player's current balance
     * @param amount the amount to withdraw
     * @return the new balance after withdrawal
     * @throws IllegalArgumentException if amount is invalid or exceeds balance
     */
    double withdraw(double currentBalance, double amount);
}
