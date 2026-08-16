package com.bank.ui;

import com.bank.model.Account;
import com.bank.service.BankService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateAccountDialog extends JDialog {
    public CreateAccountDialog(Frame owner, BankService bankService) {
        super(owner, "Open a New Account", true);
        setSize(400, 360);
        setLocationRelativeTo(owner);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(248, 250, 252));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Account Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(15, 23, 42));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField nameField = new JTextField();
        JPasswordField pinField = new JPasswordField();
        JTextField depositField = new JTextField();

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(createInputBlock("Full Legal Name", nameField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createInputBlock("4-Digit Security PIN", pinField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(createInputBlock("Initial Deposit ($)", depositField));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton createBtn = new JButton("Create Account");
        createBtn.setMaximumSize(new Dimension(340, 40));
        createBtn.setPreferredSize(new Dimension(340, 40));
        createBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        createBtn.setBackground(new Color(37, 99, 235));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.setBorderPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        createBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String pin = new String(pinField.getPassword());
                double deposit = Double.parseDouble(depositField.getText().trim());
                Account acc = bankService.createAccount(name, pin, deposit);
                
                JOptionPane.showMessageDialog(this, 
                    "Account Created Successfully!\n\nYour Account Number: " + acc.getAccountNumber() + 
                    "\nPlease store this number securely to log in.", 
                    "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric value for the deposit.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(createBtn);
        add(mainPanel);
    }

    private JPanel createInputBlock(String title, JTextField field) {
        JPanel block = new JPanel(new BorderLayout(5, 5));
        block.setOpaque(false);
        block.setMaximumSize(new Dimension(340, 55));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(71, 85, 105));

        field.setPreferredSize(new Dimension(320, 32));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));

        block.add(label, BorderLayout.NORTH);
        block.add(field, BorderLayout.CENTER);
        return block;
    }
}