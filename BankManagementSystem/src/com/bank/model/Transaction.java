package com.bank.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type;
    private final double amount;
    private final double remainingBalance;
    private final String timestamp;

    public Transaction(String type, double amount, double remainingBalance) {
        this.type = type;
        this.amount = amount;
        this.remainingBalance = remainingBalance;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public double getRemainingBalance() { return remainingBalance; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %-15s Amount: $%-8.2f Balance: $%.2f", timestamp, type, amount, remainingBalance);
    }
}