package com.bank.ui;

import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private final BankService bankService;
    private final Account account;
    private final JLabel balanceAmountLabel;
    private final DefaultTableModel tableModel;

    public DashboardFrame(BankService bankService, Account account) {
        this.bankService = bankService;
        this.account = account;

        setTitle("Kinetrexa Global Bank - Customer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout(15, 15));
        rootPanel.setBackground(new Color(241, 245, 249));
        rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.setOpaque(false);

        JLabel userTitle = new JLabel("Welcome back, " + account.getHolderName());
        userTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        userTitle.setForeground(new Color(15, 23, 42));

        JButton logoutBtn = new JButton("Sign Out");
        styleSecondaryButton(logoutBtn);
        logoutBtn.addActionListener(e -> {
            new LoginFrame(bankService).setVisible(true);
            dispose();
        });

        navPanel.add(userTitle, BorderLayout.WEST);
        navPanel.add(logoutBtn, BorderLayout.EAST);

        JPanel cardPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(800, 110));

        JPanel balanceCard = new JPanel(new BorderLayout());
        balanceCard.setBackground(new Color(30, 41, 59));
        balanceCard.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel balTitle = new JLabel("TOTAL BALANCE");
        balTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        balTitle.setForeground(new Color(148, 163, 184));

        balanceAmountLabel = new JLabel();
        balanceAmountLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        balanceAmountLabel.setForeground(new Color(56, 189, 248));

        balanceCard.add(balTitle, BorderLayout.NORTH);
        balanceCard.add(balanceAmountLabel, BorderLayout.CENTER);

        JPanel infoCard = new JPanel(new GridLayout(2, 1));
        infoCard.setBackground(Color.WHITE);
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel accInfoTitle = new JLabel("ACCOUNT NUMBER");
        accInfoTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        accInfoTitle.setForeground(new Color(100, 116, 139));

        JLabel accInfoValue = new JLabel("•••• •••• " + account.getAccountNumber());
        accInfoValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        accInfoValue.setForeground(new Color(30, 41, 59));

        infoCard.add(accInfoTitle);
        infoCard.add(accInfoValue);

        cardPanel.add(balanceCard);
        cardPanel.add(infoCard);

        JPanel topContainer = new JPanel(new BorderLayout(0, 15));
        topContainer.setOpaque(false);
        topContainer.add(navPanel, BorderLayout.NORTH);
        topContainer.add(cardPanel, BorderLayout.CENTER);
        rootPanel.add(topContainer, BorderLayout.NORTH);

        String[] columns = {"Date & Time", "Activity", "Amount", "Balance After"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.setSelectionBackground(new Color(224, 242, 254));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Recent Transactions"));
        scrollPane.getViewport().setBackground(Color.WHITE);
        rootPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        actionPanel.setOpaque(false);

        JButton depositBtn = createActionButton("Deposit Funds", new Color(16, 185, 129));
        JButton withdrawBtn = createActionButton("Withdraw Funds", new Color(239, 68, 68));
        JButton transferBtn = createActionButton("Transfer Funds", new Color(59, 130, 246));

        depositBtn.addActionListener(e -> promptDeposit());
        withdrawBtn.addActionListener(e -> promptWithdraw());
        transferBtn.addActionListener(e -> promptTransfer());

        actionPanel.add(depositBtn);
        actionPanel.add(withdrawBtn);
        actionPanel.add(transferBtn);
        rootPanel.add(actionPanel, BorderLayout.SOUTH);

        add(rootPanel);
        refreshData();
    }

    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(new Color(226, 232, 240));
        btn.setForeground(new Color(30, 41, 59));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void refreshData() {
        balanceAmountLabel.setText(String.format("$%.2f", account.getBalance()));
        tableModel.setRowCount(0);
        for (Transaction t : account.getTransactions()) {
            tableModel.addRow(new Object[]{
                t.getTimestamp(),
                t.getType(),
                String.format("$%.2f", t.getAmount()),
                String.format("$%.2f", t.getRemainingBalance())
            });
        }
    }

    private void promptDeposit() {
        String input = JOptionPane.showInputDialog(this, "Enter deposit amount ($):", "Quick Deposit", JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amt = Double.parseDouble(input.trim());
                bankService.deposit(account.getAccountNumber(), amt);
                refreshData();
                JOptionPane.showMessageDialog(this, "Successfully deposited $" + String.format("%.2f", amt), "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Deposit Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void promptWithdraw() {
        String input = JOptionPane.showInputDialog(this, "Enter withdrawal amount ($):", "Quick Withdraw", JOptionPane.PLAIN_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                double amt = Double.parseDouble(input.trim());
                bankService.withdraw(account.getAccountNumber(), amt);
                refreshData();
                JOptionPane.showMessageDialog(this, "Successfully withdrew $" + String.format("%.2f", amt), "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Withdrawal Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void promptTransfer() {
        JPanel transferPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        JTextField recipientField = new JTextField();
        JTextField amountField = new JTextField();

        transferPanel.add(new JLabel("Recipient Account Number:"));
        transferPanel.add(recipientField);
        transferPanel.add(new JLabel("Transfer Amount ($):"));
        transferPanel.add(amountField);

        int result = JOptionPane.showConfirmDialog(this, transferPanel, "Transfer Money", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String targetAcc = recipientField.getText().trim();
                double amt = Double.parseDouble(amountField.getText().trim());
                bankService.transfer(account.getAccountNumber(), targetAcc, amt);
                refreshData();
                JOptionPane.showMessageDialog(this, "Transferred $" + String.format("%.2f", amt) + " to Account " + targetAcc, "Transfer Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Transfer Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}