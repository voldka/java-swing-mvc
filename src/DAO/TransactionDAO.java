package DAO;

import Model.Product;
import Model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private final Connection connection;

    public TransactionDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Add a new transaction
    public int addTransaction(Transaction transaction) {
        String sql = "INSERT INTO [Transaction] (OrderID, ProductID, Quantity, Price, Total, TransactionDate) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, transaction.getOrderId());
            pstmt.setInt(2, transaction.getProductId());
            pstmt.setInt(3, transaction.getQuantity());
            pstmt.setDouble(4, transaction.getPrice());
            pstmt.setDouble(5, transaction.getTotal());
            pstmt.setTimestamp(6, new Timestamp(transaction.getTransactionDate().getTime()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

            // Update product stock after transaction
            updateProductStock(transaction.getProductId(), transaction.getQuantity());

        } catch (SQLException e) {
            System.out.println("Error adding transaction: " + e.getMessage());
            e.printStackTrace();
        }

        return -1; // Failed to add transaction
    }

    // Update product stock after transaction
    private void updateProductStock(int productId, int quantity) {
        String sql = "UPDATE Product SET StockQuantity = StockQuantity - ? WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating product stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update an existing transaction
    public boolean updateTransaction(Transaction transaction) {
        // First get the old transaction to calculate stock difference
        Transaction oldTransaction = getTransactionById(transaction.getId());
        if (oldTransaction == null) {
            return false;
        }

        int stockDifference = oldTransaction.getQuantity() - transaction.getQuantity();

        String sql = "UPDATE [Transaction] SET OrderID = ?, ProductID = ?, Quantity = ?, Price = ?, " +
                "Total = ?, TransactionDate = ? WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getOrderId());
            pstmt.setInt(2, transaction.getProductId());
            pstmt.setInt(3, transaction.getQuantity());
            pstmt.setDouble(4, transaction.getPrice());
            pstmt.setDouble(5, transaction.getTotal());
            pstmt.setTimestamp(6, new Timestamp(transaction.getTransactionDate().getTime()));
            pstmt.setInt(7, transaction.getId());

            int affectedRows = pstmt.executeUpdate();

            // Update product stock based on the difference
            if (stockDifference != 0) {
                adjustProductStock(transaction.getProductId(), stockDifference);
            }

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Adjust product stock when updating transaction
    private void adjustProductStock(int productId, int difference) {
        String sql = "UPDATE Product SET StockQuantity = StockQuantity + ? WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, difference);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adjusting product stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete a transaction
    public boolean deleteTransaction(int transactionId) {
        // First get the transaction to restore stock
        Transaction transaction = getTransactionById(transactionId);
        if (transaction == null) {
            return false;
        }

        String sql = "DELETE FROM [Transaction] WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);

            int affectedRows = pstmt.executeUpdate();

            // Restore product stock
            restoreProductStock(transaction.getProductId(), transaction.getQuantity());

            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Restore product stock after deleting transaction
    private void restoreProductStock(int productId, int quantity) {
        String sql = "UPDATE Product SET StockQuantity = StockQuantity + ? WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error restoring product stock: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get all transactions for an order
    public List<Transaction> getTransactionsByOrderId(int orderId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, p.Name AS ProductName FROM [Transaction] t " +
                "JOIN Product p ON t.ProductID = p.ID " +
                "WHERE t.OrderID = ? ORDER BY t.TransactionDate DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaction transaction = mapResultSetToTransaction(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            System.out.println("Error getting transactions for order: " + e.getMessage());
            e.printStackTrace();
        }

        return transactions;
    }

    // Get transaction by ID
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT t.*, p.Name AS ProductName FROM [Transaction] t " +
                "JOIN Product p ON t.ProductID = p.ID " +
                "WHERE t.ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToTransaction(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting transaction by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Transaction not found
    }

    // Helper method to map ResultSet to Transaction object
    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("ID"));
        transaction.setOrderId(rs.getInt("OrderID"));
        transaction.setProductId(rs.getInt("ProductID"));
        transaction.setProductName(rs.getString("ProductName"));
        transaction.setQuantity(rs.getInt("Quantity"));
        transaction.setPrice(rs.getDouble("Price"));
        transaction.setTotal(rs.getDouble("Total"));
        transaction.setTransactionDate(rs.getTimestamp("TransactionDate"));
        return transaction;
    }

    // Calculate total amount for an order
    public double calculateOrderTotal(int orderId) {
        String sql = "SELECT SUM(Total) AS TotalAmount FROM [Transaction] WHERE OrderID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("TotalAmount");
            }
        } catch (SQLException e) {
            System.out.println("Error calculating order total: " + e.getMessage());
            e.printStackTrace();
        }

        return 0.0;
    }
}