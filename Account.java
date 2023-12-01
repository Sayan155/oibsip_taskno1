//This class represents a bank account with basic functionalities

package ATM_Interface;
import java.util.HashMap;
import java.util.Map;

public class Account { // account attributes: the account holder name, pin etc.
    private String accountHolderName;
    private String pin; //
    private double balance;
    private StringBuilder transactionHistory;

     // Constructor initializes account attributes and sets initial transaction history.
    public Account(String accountHolderName, String pin, double balance) { // method to initialize the account
        this.accountHolderName = accountHolderName;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new StringBuilder("Initial balance: $" + balance + "\n");
    }

    // Getter methods for account information.
    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public String[] getTransactionHistory() { // Method to get transaction history as an array of strings.
        return transactionHistory.toString().split("\n");
    }

    public boolean withdraw(double amount) { // Method to withdraw money from the account.
        if (isValidAmount(amount) && amount <= balance) {
            balance -= amount;
            recordTransaction("Withdrawal: -$" + amount);
            return true;
        }
        return false;
    }

    public void deposit(double amount) { // method to deposit the money
        if (isValidAmount(amount)) {
            balance += amount;
            recordTransaction("Deposit: +$" + amount);
        }
    }

    public boolean transfer(Account recipient, double amount) { // for transfering
        if (recipient != null && isValidAmount(amount) && amount <= balance) {
            balance -= amount;
            recipient.deposit(amount);
            recordTransaction("Transfer to " + recipient.getAccountHolderName() + ": -$" + amount);
            recipient.recordTransaction("Transfer from " + getAccountHolderName() + ": +$" + amount);
            return true;
        }
        return false;
    }

    private boolean isValidAmount(double amount) {
        return amount > 0;
    } // method to check if the amount is valid or not

    private void recordTransaction(String transaction) { // Helper method to record a transaction in the transaction history.
        transactionHistory.append(transaction).append("\n");
    }

    // To retrieve the account holder's transactions from the database
    public static Account retrieveAccount(String username) { // this retrieves the account info from the database
        Map<String, Account> accounts = DatabaseManager.retrieveAccounts();
        return accounts.get(username);
    }

    // To update the account details in the database
    public static void updateAccount(Map<String, Account> accounts, String username, double newBalance) {
        System.out.println("Updated Accounts:");
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getBalance());

            // Update the account details in the database
            DatabaseManager.updateAccount(entry.getKey(), entry.getValue().getBalance()); // i haven't shown this yet, i have deleted the previous entries form my databse, the next time i run the app, i will input 2 user account (which will help me in transfer the amounts between them)
        }
    }
}

