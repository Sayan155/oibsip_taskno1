package ATM_Interface;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Map;
// lastly we have the main class of this project
public class Main {
    public static void main(String[] args) {
        try {
            // Initialize DatabaseManager
            DatabaseManager.connect();

            // Check if the user "new_user" already exists, this was done at the testing phase
            String testUsername = "new_user"; // thisis just a test
            if (!DatabaseManager.doesUserExist(testUsername)) {
                // Register a new user if not already registered
                System.out.println("User does not exist. Registering new user...");
                //DatabaseManager.insertUser(testUsername, "password123"); this is the hardcoded user
            } else {
                System.out.println("User already exists. Skipping registration.");
            }

            // Retrieve accounts from the database
            Map<String, Account> accounts = DatabaseManager.retrieveAccounts();

            // Print retrieved accounts for debugging
            System.out.println("Retrieved Accounts:");
            for (Map.Entry<String, Account> entry : accounts.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue().getBalance());
            } // these will be printed in our terminal

            // Close the database connection
            DatabaseManager.disconnect();
//            closing the connection is very important

            // Launch the ATM GUI
            SwingUtilities.invokeLater(() -> new ATMGUI(accounts));
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception
        }
    }
}

// we currently on't have any accounts, lets sing up one
// similarly in our database we will have them
// lets transfer to someone who doesn't exist in the database
// as i said, the main application won't be closed even when the main menu is closed

// this is obi taskno 1