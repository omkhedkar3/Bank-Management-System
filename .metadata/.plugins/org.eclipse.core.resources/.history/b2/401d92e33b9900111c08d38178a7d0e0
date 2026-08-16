package com.bank;

import com.bank.service.BankService;
import com.bank.service.BankServiceImpl;
import com.bank.ui.LoginFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BankService bankService = new BankServiceImpl();
            new LoginFrame(bankService).setVisible(true);
        });
    }
}