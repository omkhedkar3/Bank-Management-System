# Bank Management System

A desktop banking application developed in Java using the Eclipse IDE, featuring an interactive graphical user interface (GUI) built with Java Swing and local file-based data persistence.

---

## 📌 Features

- **Account Management**: Create new bank accounts with automatically generated 6-digit account numbers and secure 4-digit PIN authentication.
- **Deposit & Withdrawal**: Real-time balance updates with strict validation rules (no negative or zero amounts, balance overdraft checks).
- **Fund Transfer**: Instant account-to-account money transfers with validation of recipient account existence and transaction rollback safety.
- **Transaction History**: Formatted, timestamped transaction ledger tracking operations (`DEPOSIT`, `WITHDRAWAL`, `TRANSFER IN`, `TRANSFER OUT`) and remaining balances.
- **Data Persistence**: Automatic serialization to a local file (`bank_data.dat`), retaining user accounts and transaction histories across application restarts.
- **Interactive UI**: Responsive Java Swing interface with styled cards, dashboard summaries, custom color palettes, and dialog validations.
- **Robust Exception Handling**: Custom domain-level exceptions (`InsufficientFundsException`, `AccountNotFoundException`, `InvalidAmountException`).

---
🛠️ Tech Stack & Prerequisites
Language: Java (JDK 17 or higher recommended)
Development Environment: Eclipse IDE for Java Developers
GUI Toolkit: Java Swing / AWT
Persistence: Java Object Serialization (java.io.Serializable)

🚀 How to Run in Eclipse
Open Eclipse IDE.
Go to File -> Open Projects from File System... (or File -> Import -> Existing Projects into Workspace).
Select the BankManagementSystem directory and click Finish.
In the Package Explorer, expand src/com/bank and locate Main.java.
Right-click Main.java and select Run As -> Java Application.

📖 Usage Guide
Create an Account:
Click Open New Account on the login screen.
Enter your full name, a 4-digit PIN, and an optional initial deposit.
Note down the generated 6-digit Account Number.
Login:
Enter your Account Number and PIN to access the dashboard.
Perform Operations:
Use the Deposit, Withdraw, and Transfer buttons at the bottom to perform transactions.
Watch the real-time balance card and the transaction table update automatically.
Sign Out:

Click Sign Out to return to the login screen. All records are automatically saved to bank_data.dat.

## 🏗️ Project Architecture

The project adheres to a modular, layered architecture:

```text
BankManagementSystem/
├── src/
│   └── com/
│       └── bank/
│           ├── exception/      # Custom domain exceptions
│           │   ├── AccountNotFoundException.java
│           │   ├── InsufficientFundsException.java
│           │   └── InvalidAmountException.java
│           ├── model/          # Core entity definitions (Serializable)
│           │   ├── Account.java
│           │   └── Transaction.java
│           ├── service/        # Business logic interface & implementation
│           │   ├── BankService.java
│           │   └── BankServiceImpl.java
│           ├── ui/             # Java Swing GUI components
│           │   ├── LoginFrame.java
│           │   ├── DashboardFrame.java
│           │   └── CreateAccountDialog.java
│           └── Main.java       # Application entry point
├── bank_data.dat               # Auto-generated binary storage file
└── README.md

