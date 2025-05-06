package DAO;

import Model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Create the accounts table if it doesn't exist
    public void createTableIfNotExists() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = "IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Account') " +
                    "CREATE TABLE Account (" +
                    "Uid INT IDENTITY(1,1) PRIMARY KEY, " +
                    "Username VARCHAR(100), " +
                    "Password VARCHAR(100), " +
                    "Email VARCHAR(100))";

            stmt.execute(sql);
            System.out.println("Account table created or already exists");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new account to the database
    public void addAccount(Account account) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Account (Username, Password, Email) VALUES (?, ?, ?)")) {

            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.setString(3, account.getEmail());
            pstmt.executeUpdate();

            System.out.println("Account added to database: " + account.getUsername() + " " + account.getEmail());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all accounts from the database
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Account")) {

            while (rs.next()) {
                Account account = new Account();
                account.setUid(rs.getInt("Uid"));
                account.setUsername(rs.getString("Username"));
                account.setPassword(rs.getString("Password"));
                account.setEmail(rs.getString("Email"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    // Get all accounts as an array of strings for display purposes
    public Object[] loadAccounts() {
        List<String> accountStrings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT Username, Email FROM Account")) {

            while (rs.next()) {
                String accountString = rs.getString("Username") + ", " + rs.getString("Email");
                accountStrings.add(accountString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountStrings.toArray();
    }
}