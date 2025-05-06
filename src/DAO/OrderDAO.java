package DAO;

import Model.Order;
import Model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private final Connection connection;

    public OrderDAO() {
        this.connection = DatabaseConnection.getConnection();
    }

    // Add a new order
    public int addOrder(Order order) {
        String sql = "INSERT INTO [Order] (CustomerName, OrderDate, Status, TotalAmount, Note) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, order.getCustomerName());
            pstmt.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
            pstmt.setString(3, order.getStatus());
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setString(5, order.getNote());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding order: " + e.getMessage());
            e.printStackTrace();
        }

        return -1; // Failed to add order
    }

    // Update an existing order
    public boolean updateOrder(Order order) {
        String sql = "UPDATE [Order] SET CustomerName = ?, OrderDate = ?, Status = ?, TotalAmount = ?, Note = ? WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, order.getCustomerName());
            pstmt.setTimestamp(2, new Timestamp(order.getOrderDate().getTime()));
            pstmt.setString(3, order.getStatus());
            pstmt.setDouble(4, order.getTotalAmount());
            pstmt.setString(5, order.getNote());
            pstmt.setInt(6, order.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Delete an order
    public boolean deleteOrder(int orderId) {
        // First delete associated transactions
        String deleteTrans = "DELETE FROM [Transaction] WHERE OrderID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteTrans)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting transactions for order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        // Then delete the order
        String deleteOrder = "DELETE FROM [Order] WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(deleteOrder)) {
            pstmt.setInt(1, orderId);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get all orders
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order] ORDER BY OrderDate DESC";

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    // Get order by ID
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM [Order] WHERE ID = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting order by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null; // Order not found
    }

    // Helper method to map ResultSet to Order object
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("ID"));
        order.setCustomerName(rs.getString("CustomerName"));
        order.setOrderDate(rs.getTimestamp("OrderDate"));
        order.setStatus(rs.getString("Status"));
        order.setTotalAmount(rs.getDouble("TotalAmount"));
        order.setNote(rs.getString("Note"));
        return order;
    }

    // Search orders by customer name
    public List<Order> searchOrdersByCustomer(String customerName) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE CustomerName LIKE ? ORDER BY OrderDate DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + customerName + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error searching orders: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }

    // Get orders by status
    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM [Order] WHERE Status = ? ORDER BY OrderDate DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Error getting orders by status: " + e.getMessage());
            e.printStackTrace();
        }

        return orders;
    }
}