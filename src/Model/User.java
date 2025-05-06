package Model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DAO.DatabaseConnection;

public class User {
    private int uid;
    private String username;
    private String password;
    private String email;

    public User() {
        // empty constructor
    }

    public User(int uid, String username, String password, String email) {
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Constructor không có uid (dùng khi thêm mới)
    public User(String username, String password, String email) {
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

    // Save account to database
    public void saveUser() {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Account (Username, Password, Email) VALUES (?, ?, ?)")) {

            pstmt.setString(1, this.username);
            pstmt.setString(2, this.password);
            pstmt.setString(3, this.email);
            pstmt.executeUpdate();

            System.out.println("Account added to database: " + this.username + " " + this.email);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load all accounts from database
    public static Object[] loadUsers() {
        List<String> userStrings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT Username, Email FROM Account")) {

            while (rs.next()) {
                String userString = rs.getString("Username") + ", " + rs.getString("Email");
                userStrings.add(userString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userStrings.toArray();
    }

    // Get all accounts from database as User objects
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Account")) {

            while (rs.next()) {
                User user = new User();
                user.setUid(rs.getInt("Uid"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password"));
                user.setEmail(rs.getString("Email"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public String toString() {
        return "User [uid=" + uid + ", username=" + username + ", email=" + email + "]";
    }
}
