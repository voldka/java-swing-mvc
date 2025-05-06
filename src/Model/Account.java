package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DAO.DatabaseConnection;

// Renamed class from User to Account
public class Account {
    private int uid;
    private String username;
    private String password;
    private String email;

    public Account() {
        // empty constructor
    }

    public Account(int uid, String username, String password, String email) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Constructor không có uid (dùng khi thêm mới)
    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters
    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    // Setters
    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Database operations

    // Create the Account table if it doesn't exist
    public static void createTableIfNotExists() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            // Check if the table exists in the correct schema (dbo is default for SQL
            // Server)
            String checkTableSQL = "IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_SCHEMA = 'dbo' AND TABLE_NAME = 'Account') " +
                    "CREATE TABLE Account (" +
                    "Uid INT IDENTITY(1,1) PRIMARY KEY, " +
                    "Username VARCHAR(100) NOT NULL UNIQUE, " + // Added NOT NULL and UNIQUE
                    "Password VARCHAR(100) NOT NULL, " + // Added NOT NULL
                    "Email VARCHAR(100) NOT NULL UNIQUE)"; // Added NOT NULL and UNIQUE

            stmt.execute(checkTableSQL);
            System.out.println("Account table checked/created successfully.");
        } catch (SQLException e) {
            System.err.println("Error checking/creating Account table: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Save account to database - Renamed from saveUser
    public void saveAccount() {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Account (Username, Password, Email) VALUES (?, ?, ?)")) {

            pstmt.setString(1, this.username);
            pstmt.setString(2, this.password);
            pstmt.setString(3, this.email);
            pstmt.executeUpdate();

            System.out.println("Account added to database: " + this.username + " " + this.email);
        } catch (SQLException e) {
            // Handle potential unique constraint violation
            if (e.getMessage().contains("UNIQUE KEY constraint")) {
                System.err.println("Error saving account: Username or Email already exists.");
                // Optionally, re-throw a custom exception or return a specific value
            } else {
                System.err.println("Error saving account: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // Load all accounts from database - Renamed from loadUsers, returns
    // List<Account>
    public static List<Account> loadAccounts() {
        List<Account> accounts = new ArrayList<>(); // Changed to List<Account>

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                // Select all columns to build Account objects
                ResultSet rs = stmt.executeQuery("SELECT Uid, Username, Password, Email FROM Account")) {

            while (rs.next()) {
                // Create Account object from ResultSet
                Account account = new Account(
                        rs.getInt("Uid"),
                        rs.getString("Username"),
                        rs.getString("Password"), // Be cautious about loading passwords unless necessary
                        rs.getString("Email"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
            e.printStackTrace();
        }

        return accounts; // Return the list of Account objects
    }

    // Get all accounts from database as User objects - Renamed from getAllUsers
    public static List<Account> getAllAccounts() { // Renamed method
        return loadAccounts(); // Delegate to the primary loading method
    }

    @Override
    public String toString() {
        // Updated class name in toString
        return "Account [uid=" + uid + ", username=" + username + ", email=" + email + "]";
    }
}