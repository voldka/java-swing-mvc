package DAO;

import Model.History;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistoryDAO {

    // Lấy danh sách lịch sử nhập kho với tên sản phẩm
    public List<History> getAllHistory() {
        List<History> historyList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT h.Id, h.ProductID, p.Name as ProductName, h.[Added Stocks], h.Date " +
                                "FROM History h " +
                                "JOIN Product p ON h.ProductID = p.Id " +
                                "ORDER BY h.Date DESC")) {

            while (rs.next()) {
                History history = new History();
                history.setId(rs.getInt("Id"));
                history.setProductId(rs.getInt("ProductID"));
                history.setProductName(rs.getString("ProductName"));
                history.setAddedStocks(rs.getInt("Added Stocks"));
                history.setDate(rs.getTimestamp("Date"));
                historyList.add(history);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách lịch sử nhập kho: " + e.getMessage());
            e.printStackTrace();
        }

        return historyList;
    }

    // Lấy lịch sử nhập kho theo sản phẩm
    public List<History> getHistoryByProductId(int productId) {
        List<History> historyList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT h.Id, h.ProductID, p.Name as ProductName, h.[Added Stocks], h.Date " +
                                "FROM History h " +
                                "JOIN Product p ON h.ProductID = p.Id " +
                                "WHERE h.ProductID = ? " +
                                "ORDER BY h.Date DESC")) {

            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                History history = new History();
                history.setId(rs.getInt("Id"));
                history.setProductId(rs.getInt("ProductID"));
                history.setProductName(rs.getString("ProductName"));
                history.setAddedStocks(rs.getInt("Added Stocks"));
                history.setDate(rs.getTimestamp("Date"));
                historyList.add(history);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy lịch sử nhập kho theo sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return historyList;
    }

    // Thêm bản ghi lịch sử nhập kho mới
    public boolean addHistory(History history) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO History (ProductID, [Added Stocks], Date) VALUES (?, ?, ?)")) {

            pstmt.setInt(1, history.getProductId());
            pstmt.setInt(2, history.getAddedStocks());

            // Nếu ngày không được cung cấp, sử dụng ngày hiện tại
            if (history.getDate() == null) {
                pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            } else {
                pstmt.setTimestamp(3, new Timestamp(history.getDate().getTime()));
            }

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm lịch sử nhập kho: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa bản ghi lịch sử nhập kho
    public boolean deleteHistory(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM History WHERE Id = ?")) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa lịch sử nhập kho: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa toàn bộ lịch sử nhập kho của một sản phẩm
    public boolean deleteHistoryByProductId(int productId) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM History WHERE ProductID = ?")) {

            pstmt.setInt(1, productId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa lịch sử nhập kho theo sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Tìm kiếm lịch sử nhập kho theo tên sản phẩm
    public List<History> searchHistoryByProductName(String keyword) {
        List<History> historyList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT h.Id, h.ProductID, p.Name as ProductName, h.[Added Stocks], h.Date " +
                                "FROM History h " +
                                "JOIN Product p ON h.ProductID = p.Id " +
                                "WHERE p.Name LIKE ? " +
                                "ORDER BY h.Date DESC")) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                History history = new History();
                history.setId(rs.getInt("Id"));
                history.setProductId(rs.getInt("ProductID"));
                history.setProductName(rs.getString("ProductName"));
                history.setAddedStocks(rs.getInt("Added Stocks"));
                history.setDate(rs.getTimestamp("Date"));
                historyList.add(history);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm lịch sử nhập kho: " + e.getMessage());
            e.printStackTrace();
        }

        return historyList;
    }
}