package ATM_Interface;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
// this is the database class, which will have the most complex logic of this entire project, I have written the comments and tried to make it look easy
public class DatabaseManager { // we have the jdbc connection details
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sample_database"; // loading my database into the memory
    private static final String USERNAME = "root";
    private static final String PASSWORD = "MySqlR00tPwd!"; // mysql pass

    public static Connection connect() throws SQLException { // returns a Connection object
//        throws exception if a database access error occurs
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void insertUser(String username, String password) { //getting the user details
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Both username and password are required for signup"); // same as before, checking if any of the username or password field is empty or not
            return;
        }

        try (Connection connection = connect(); // connection throws an exception, handling that
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            String hashedPassword = hashPassword(password); // hashing the password before storing it in the database.

//             set parameters in the prepared statement
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);

            int rowsAffected = preparedStatement.executeUpdate(); // execute sql query

            if (rowsAffected > 0) { // checking if the user was registered successfully
                System.out.println("User registered successfully");
            } else {
                System.out.println("Failed to register user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAccountBalance(String username, double newBalance) { // method to update the account balance
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET balance = ? WHERE username = ?")) {
            // setting parameters in the prepared statement
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, username);
// all of these changes are getting saved in our database
            int rowsAffected = preparedStatement.executeUpdate(); // execute sql query

            if (rowsAffected > 0) {
                System.out.println("Account balance updated successfully");
            } else {
                System.out.println("Failed to update account balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// this method checks if an user exists or not
    public static boolean doesUserExist(String username) {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); //checking if the result has any entries, indicating that the user's existence in the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Map<String, Account> retrieveAccounts() { // retrieving the accounts from the database
        Map<String, Account> accounts = new HashMap<>();

        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT username, balance FROM users");
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                double balance = resultSet.getDouble("balance");

                Account account = new Account(username, "", balance);
                accounts.put(username, account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }
    public static void registerUser(String username, String password) { // similarly with registering the user
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

            // Hash the password before storing it in the database
            String hashedPassword = hashPassword(password);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User registered successfully");
            } else {
                System.out.println("Failed to register user");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

// the username & the password entered during the login
//    returning if the login successful or not
    public static boolean isValidLogin(String username, String password) {
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?")) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    String enteredPassword = hashPassword(password);
                    return storedPassword.equals(enteredPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static String hashPassword(String password) {
        return password;
    }

    public static void disconnect() {
        // Connection will be closed automatically by try-with-resources
    }
    public static void updateAccount(String username, double newBalance) { //method to update the username and the new balance in the database
        try (Connection connection = connect();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET balance = ? WHERE username = ?")) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, username);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Account balance for " + username + " updated successfully");
            } else {
                System.out.println("Failed to update account balance for " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Account retrieveAccount(String username) {
        // Fetch account details from the database based on the username
        Map<String, Account> accounts = retrieveAccounts();
        return accounts.get(username);
    }



    public static void updateAccounts(Map<String, Account> accounts) {
        System.out.println("Updated Accounts:");
        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getBalance());
        }
    }
}

