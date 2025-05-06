package DAO;

import Model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Thêm loại sản phẩm mới
    public boolean addCategory(Category category) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Category (CategoryItem) VALUES (?)")) {

            pstmt.setString(1, category.getCategoryItem());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả loại sản phẩm
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Category")) {

            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("Id"));
                category.setCategoryItem(rs.getString("CategoryItem"));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return categories;
    }

    // Cập nhật thông tin loại sản phẩm
    public boolean updateCategory(Category category) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE Category SET CategoryItem = ? WHERE Id = ?")) {

            pstmt.setString(1, category.getCategoryItem());
            pstmt.setInt(2, category.getId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa loại sản phẩm
    public boolean deleteCategory(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Category WHERE Id = ?")) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra xem loại sản phẩm đã tồn tại hay chưa
    public boolean isCategoryExists(String categoryItem) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn
                        .prepareStatement("SELECT COUNT(*) FROM Category WHERE CategoryItem = ?")) {

            pstmt.setString(1, categoryItem);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Lấy category theo ID
    public Category getCategoryById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Category WHERE Id = ?")) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("Id"));
                category.setCategoryItem(rs.getString("CategoryItem"));
                return category;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy thông tin loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Lấy category theo tên
    public Category getCategoryByName(String categoryItem) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Category WHERE CategoryItem = ?")) {

            pstmt.setString(1, categoryItem);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("Id"));
                category.setCategoryItem(rs.getString("CategoryItem"));
                return category;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy thông tin loại sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}