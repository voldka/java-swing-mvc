package DAO;

import Model.Account;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // Xác thực người dùng (đăng nhập)
    public Account authenticate(String username, String password) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT * FROM Account WHERE Username = ? AND Password = ?")) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Account account = new Account();
                account.setUid(rs.getInt("Uid"));
                account.setUsername(rs.getString("Username"));
                account.setPassword(rs.getString("Password"));
                account.setEmail(rs.getString("Email"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi xác thực: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Trả về null nếu không tìm thấy người dùng
    }

    // Thêm tài khoản mới
    public boolean addAccount(Account account) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Account (Username, Password, Email) VALUES (?, ?, ?)")) {

            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.setString(3, account.getEmail());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm tài khoản: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả tài khoản
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
            System.out.println("Lỗi lấy danh sách tài khoản: " + e.getMessage());
            e.printStackTrace();
        }

        return accounts;
    }

    // Cập nhật thông tin tài khoản
    public boolean updateAccount(Account account) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE Account SET Username = ?, Password = ?, Email = ? WHERE Uid = ?")) {

            pstmt.setString(1, account.getUsername());
            pstmt.setString(2, account.getPassword());
            pstmt.setString(3, account.getEmail());
            pstmt.setInt(4, account.getUid());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật tài khoản: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa tài khoản
    public boolean deleteAccount(int uid) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Account WHERE Uid = ?")) {

            pstmt.setInt(1, uid);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa tài khoản: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra xem tên người dùng đã tồn tại hay chưa
    public boolean isUsernameExists(String username) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Account WHERE Username = ?")) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra tên người dùng: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Kiểm tra xem email đã tồn tại hay chưa
    public boolean isEmailExists(String email) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Account WHERE Email = ?")) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra email: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}