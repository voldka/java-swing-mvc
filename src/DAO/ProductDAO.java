package DAO;

import Model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Thêm sản phẩm mới
    public boolean addProduct(Product product) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Product (Name, Price, Stock, Unit, Category) VALUES (?, ?, ?, ?, ?)")) {

            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setInt(4, product.getUnit());
            pstmt.setString(5, product.getCategory());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách tất cả sản phẩm
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setPrice(rs.getInt("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setUnit(rs.getInt("Unit"));
                product.setCategory(rs.getString("Category"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    // Lấy danh sách sản phẩm theo danh mục
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Product WHERE Category = ?")) {

            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setPrice(rs.getInt("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setUnit(rs.getInt("Unit"));
                product.setCategory(rs.getString("Category"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy danh sách sản phẩm theo danh mục: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }

    // Cập nhật thông tin sản phẩm
    public boolean updateProduct(Product product) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE Product SET Name = ?, Price = ?, Stock = ?, Unit = ?, Category = ? WHERE Id = ?")) {

            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getPrice());
            pstmt.setInt(3, product.getStock());
            pstmt.setInt(4, product.getUnit());
            pstmt.setString(5, product.getCategory());
            pstmt.setInt(6, product.getId());

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Xóa sản phẩm
    public boolean deleteProduct(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Product WHERE Id = ?")) {

            pstmt.setInt(1, id);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi xóa sản phẩm: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Product WHERE Id = ?")) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setPrice(rs.getInt("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setUnit(rs.getInt("Unit"));
                product.setCategory(rs.getString("Category"));
                return product;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy thông tin sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // Kiểm tra xem sản phẩm đã tồn tại hay chưa
    public boolean isProductExists(String name) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Product WHERE Name = ?")) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật số lượng hàng trong kho
    public boolean updateStock(int productId, int additionalStock) {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE Product SET Stock = Stock + ? WHERE Id = ?")) {

            pstmt.setInt(1, additionalStock);
            pstmt.setInt(2, productId);

            int rows = pstmt.executeUpdate();

            // Thêm vào bảng History với thời gian hiện tại để tạo bản ghi duy nhất
            if (rows > 0) {
                try (PreparedStatement historyStmt = conn.prepareStatement(
                        "INSERT INTO History (ProductID, [Added Stocks], Date) VALUES (?, ?, GETDATE())")) {
                    historyStmt.setInt(1, productId);
                    historyStmt.setInt(2, additionalStock);
                    historyStmt.executeUpdate();
                } catch (SQLException historyEx) {
                    // Xử lý trường hợp không thêm được vào bảng History
                    // Vì việc cập nhật Stock đã thành công, chỉ log lỗi và không trả về false
                    System.out.println("Lỗi khi thêm vào lịch sử nhập kho: " + historyEx.getMessage());
                }
            }

            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi cập nhật số lượng sản phẩm trong kho: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tổng số lượng sản phẩm trong kho
    public int getTotalProductsCount() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Product")) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy tổng số sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    // Lấy tổng giá trị kho hàng
    public long getTotalInventoryValue() {
        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT SUM(Price * Stock) AS TotalValue FROM Product")) {

            if (rs.next()) {
                return rs.getLong("TotalValue");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy tổng giá trị kho hàng: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    // Tìm kiếm sản phẩm theo tên
    public List<Product> searchProductsByName(String keyword) {
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Product WHERE Name LIKE ?")) {

            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("Id"));
                product.setName(rs.getString("Name"));
                product.setPrice(rs.getInt("Price"));
                product.setStock(rs.getInt("Stock"));
                product.setUnit(rs.getInt("Unit"));
                product.setCategory(rs.getString("Category"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.out.println("Lỗi tìm kiếm sản phẩm: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
}