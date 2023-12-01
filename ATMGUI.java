// Class representing the graphical user interface for the ATM application
package ATM_Interface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;

public class ATMGUI { // Attributes: currentAccount: the logged-in account, accounts: a map of all accounts
    private Account currentAccount;
    private Map<String, Account> accounts;

    public ATMGUI(Map<String, Account> accounts) {  // Constructor initializes the GUI with the provided map of accounts.
        this.accounts = accounts;
        createAndShowGUI();
    }
    // Method to create and display the main GUI window.
    private void createAndShowGUI() {
        // Create JFrame and components
        JFrame frame = new JFrame("ATM Application");
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Signup");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        // Set layout
        frame.setLayout(new GridLayout(3, 2)); // using a gridlayout for the main screen
        frame.add(new JLabel("Username:"));
        frame.add(usernameField);
        frame.add(new JLabel("Password:"));
        frame.add(passwordField);
        frame.add(loginButton);
        frame.add(signupButton);

        // Set action listeners
        loginButton.addActionListener(e -> handleLogin(usernameField.getText(), passwordField.getPassword()));
        signupButton.addActionListener(e -> handleSignup(usernameField.getText(), passwordField.getPassword()));

        // Set frame properties
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to handle user login
    private void handleLogin(String username, char[] passwordChars) {
        String password = new String(passwordChars);

        // Call a method to check login credentials
        boolean loginSuccessful = DatabaseManager.isValidLogin(username, password);

        if (loginSuccessful) { // Method to authenticate user during login
            JOptionPane.showMessageDialog(null, "Login Successful");
            // Set the current account
            this.currentAccount = DatabaseManager.retrieveAccounts().get(username);
            // Launch the main menu screen
            new MainMenuScreen(this.currentAccount);

            // Close the current login screen if needed
            // dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Login");
        }

        // Clear the password field after login attempt
        Arrays.fill(passwordChars, ' '); // Clear the password array (empty string)
    }



    // Method to handle user signup
    private void handleSignup(String username, char[] passwordChars) {
        String password = new String(passwordChars);

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both username and password are required for signup"); // if any of the fields, the username or the password field is empty then the login fails and shows this message
            return;
        }

        // Call a method to register a new user
        DatabaseManager.registerUser(username, password);

        // Clear the password field after signup attempt
        Arrays.fill(passwordChars, ' '); // Clear the password array
    }

    // Method to handle money transfer between accounts
    private void handleTransfer() {
        // Get the currently logged-in user
        Account currentAccount = this.currentAccount;

        if (currentAccount != null) {
            // Prompt the user for the recipient's username
            String recipientUsername = JOptionPane.showInputDialog(null, "Enter recipient's username:");

            if (recipientUsername != null && !recipientUsername.isEmpty()) {
                // Get the recipient account from the database using the appropriate method
                Account recipientAccount = DatabaseManager.retrieveAccount(recipientUsername);

                if (recipientAccount != null) {
                    // Prompt the user for the transfer amount
                    try {
                        double transferAmount = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter transfer amount:"));

                        // Perform the money transfer
                        boolean transferSuccessful = currentAccount.transfer(recipientAccount, transferAmount);

                        if (transferSuccessful) { // checking to see if the transfer was successful or not
                            JOptionPane.showMessageDialog(null, "Transfer successful!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Transfer failed. Check your balance or recipient account.");
                        }

                        // Print updated balances
                        System.out.println("Your Balance: $" + currentAccount.getBalance());
                        System.out.println("Recipient Balance: $" + recipientAccount.getBalance());
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Invalid transfer amount. Please enter a valid number.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Recipient account not found."); // if the name that i provide during transfer doesn't exist in the database
                }
            } else {
                JOptionPane.showMessageDialog(null, "Recipient's username cannot be empty.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error: Unable to retrieve the currently logged-in user.");
        }
    }

// creating a instance of the gui and the gui related tasks are handled appropriately on event dispatch thread
    public static void main(String[] args) {
        // For testing the ATMGUI
        SwingUtilities.invokeLater(() -> new ATMGUI(null));
    }
}

