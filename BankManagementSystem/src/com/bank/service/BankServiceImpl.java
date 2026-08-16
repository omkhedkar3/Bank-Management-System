package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BankServiceImpl implements BankService {
    private static final String DATA_FILE = "bank_data.dat";
    private Map<String, Account> accounts;

    public BankServiceImpl() {
        this.accounts = loadAccountsFromFile();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Account> loadAccountsFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, Account>) ois.readObject();
        } catch (Exception e) {
            System.err.println("Could not load existing account data: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private synchronized void saveAccountsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.err.println("Failed to persist account data: " + e.getMessage());
        }
    }

    @Override
    public synchronized Account createAccount(String name, String pin, double initialDeposit) throws InvalidAmountException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder name cannot be empty.");
        }
        if (pin == null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be exactly 4 digits.");
        }
        if (initialDeposit < 0) {
            throw new InvalidAmountException("Initial deposit cannot be negative.");
        }

        String accNum;
        Random random = new Random();
        do {
            accNum = String.valueOf(100000 + random.nextInt(900000));
        } while (accounts.containsKey(accNum));

        Account account = new Account(accNum, name.trim(), pin, initialDeposit);
        accounts.put(accNum, account);
        saveAccountsToFile();
        return account;
    }

    @Override
    public Account authenticate(String accountNumber, String pin) throws AccountNotFoundException {
        Account acc = accounts.get(accountNumber);
        if (acc == null || !acc.validatePin(pin)) {
            throw new AccountNotFoundException("Invalid Account Number or PIN.");
        }
        return acc;
    }

    @Override
    public synchronized void deposit(String accountNumber, double amount) throws AccountNotFoundException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }

        double newBalance = acc.getBalance() + amount;
        acc.setBalance(newBalance);
        acc.addTransaction(new Transaction("DEPOSIT", amount, newBalance));
        saveAccountsToFile();
    }

    @Override
    public synchronized void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        if (acc.getBalance() < amount) {
            throw new InsufficientFundsException("Insufficient funds. Available: $" + String.format("%.2f", acc.getBalance()));
        }

        double newBalance = acc.getBalance() - amount;
        acc.setBalance(newBalance);
        acc.addTransaction(new Transaction("WITHDRAWAL", amount, newBalance));
        saveAccountsToFile();
    }

    @Override
    public synchronized void transfer(String fromAccNum, String toAccNum, double amount) throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException {
        if (fromAccNum.equals(toAccNum)) {
            throw new IllegalArgumentException("Cannot transfer to the same account.");
        }
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than zero.");
        }

        Account sender = accounts.get(fromAccNum);
        Account recipient = accounts.get(toAccNum);

        if (sender == null) throw new AccountNotFoundException("Sender account not found.");
        if (recipient == null) throw new AccountNotFoundException("Recipient account " + toAccNum + " not found.");
        if (sender.getBalance() < amount) throw new InsufficientFundsException("Insufficient funds for transfer.");

        double senderBal = sender.getBalance() - amount;
        double recipientBal = recipient.getBalance() + amount;

        sender.setBalance(senderBal);
        sender.addTransaction(new Transaction("TRANSFER OUT -> " + toAccNum, amount, senderBal));

        recipient.setBalance(recipientBal);
        recipient.addTransaction(new Transaction("TRANSFER IN <- " + fromAccNum, amount, recipientBal));

        saveAccountsToFile();
    }

    @Override
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account acc = accounts.get(accountNumber);
        if (acc == null) {
            throw new AccountNotFoundException("Account not found: " + accountNumber);
        }
        return acc;
    }
}