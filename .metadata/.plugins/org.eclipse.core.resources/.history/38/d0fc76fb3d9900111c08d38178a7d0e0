package com.bank.service;

import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InsufficientFundsException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;

public interface BankService {
    Account createAccount(String name, String pin, double initialDeposit) throws InvalidAmountException;
    Account authenticate(String accountNumber, String pin) throws AccountNotFoundException;
    void deposit(String accountNumber, double amount) throws AccountNotFoundException, InvalidAmountException;
    void withdraw(String accountNumber, double amount) throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException;
    void transfer(String fromAccNum, String toAccNum, double amount) throws AccountNotFoundException, InsufficientFundsException, InvalidAmountException;
    Account getAccount(String accountNumber) throws AccountNotFoundException;
}