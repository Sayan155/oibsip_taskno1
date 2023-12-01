package ATM_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuScreen extends JFrame {
    private Account userAccount;

    public MainMenuScreen(Account userAccount) {
        this.userAccount = userAccount;
        createAndShowGUI();
    }
// same as before, but here we create main menu screen which will hold the withdraw, transfer, deposit blocks
    private void createAndShowGUI() {
        setTitle("Main Menu");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JButton viewBalanceButton = new JButton("View Balance");
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton transferButton = new JButton("Transfer");
        JButton quitButton = new JButton("Quit");

        panel.add(viewBalanceButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(transferButton);
        panel.add(quitButton);

        viewBalanceButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "Your Balance: $" + userAccount.getBalance())); // shows us the balance in a new box

        depositButton.addActionListener(e -> handleDeposit());

        withdrawButton.addActionListener(e -> handleWithdraw());

        transferButton.addActionListener(e -> handleTransfer());

        quitButton.addActionListener(e -> handleQuit());



        add(panel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleDeposit() { // method to handle deposit
        String depositAmount = JOptionPane.showInputDialog("Enter deposit amount:");
        try {
            double amount = Double.parseDouble(depositAmount);
            userAccount.deposit(amount);
            JOptionPane.showMessageDialog(null, "Deposit Successful. New Balance: $" + userAccount.getBalance());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void handleWithdraw() { // similarly to handle withdraw
        String withdrawAmount = JOptionPane.showInputDialog("Enter withdrawal amount:");
        try {
            double amount = Double.parseDouble(withdrawAmount);
            if (userAccount.withdraw(amount)) {
                JOptionPane.showMessageDialog(null, "Withdrawal Successful. New Balance: $" + userAccount.getBalance());
            } else {
                JOptionPane.showMessageDialog(null, "Insufficient funds or invalid amount.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
        }
    }

    private void handleTransfer() {
        // we have a way to get the currently logged-in user (userAccount)
        Account currentAccount = userAccount;

        // we have a method to prompt the user for the recipient's username
        String recipientUsername = JOptionPane.showInputDialog("Enter recipient's username:"); // to whom we want tot transfer

        // we have a method to get the recipient account from the database
        Account recipientAccount = DatabaseManager.retrieveAccount(recipientUsername);

        if (recipientAccount != null) {
            // we have a method to prompt the user for the transfer amount
            String transferAmountString = JOptionPane.showInputDialog("Enter transfer amount:"); // how much we want to send
            try {
                double transferAmount = Double.parseDouble(transferAmountString);
                boolean transferSuccessful = currentAccount.transfer(recipientAccount, transferAmount);

                if (transferSuccessful) { // checking if the transfer was successful or not
                    JOptionPane.showMessageDialog(null, "Transfer successful!");
                    // Print updated balances if needed
                    System.out.println("Your Balance: $" + currentAccount.getBalance());
                    System.out.println("Recipient Balance: $" + recipientAccount.getBalance()); // this is optional
                } else {
                    JOptionPane.showMessageDialog(null, "Transfer failed. Check your balance or recipient account.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Recipient account not found.");
        }
    }


    private void handleQuit() { // to exit the main menu screen and NOT exit the whole application
        int choice = JOptionPane.showConfirmDialog(
                null, "Are you sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            // Perform any necessary cleanup or logging
            System.out.println("Quitting the application...");

            // Close the current main menu screen
            dispose();
        }
    }
}
