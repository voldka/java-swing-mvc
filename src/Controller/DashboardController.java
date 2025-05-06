package Controller;

import DAO.ProductDAO;
import Model.Account;
import View.DashboardView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import DAO.DatabaseConnection;

public class DashboardController {
    private ProductDAO productDAO;
    private DashboardView dashboardView;
    private Account loggedInAccount;

    public DashboardController(DashboardView dashboardView, Account loggedInAccount) {
        this.productDAO = new ProductDAO();
        this.dashboardView = dashboardView;
        this.loggedInAccount = loggedInAccount;

        // Cập nhật thông tin trên dashboard
        updateDashboardInfo();

        // Thiết lập các event listener cho các nút
        setupEventListeners();
    }

    // Cập nhật thông tin hiển thị trên dashboard
    public void updateDashboardInfo() {
        // Cập nhật số lượng sản phẩm
        int productCount = productDAO.getTotalProductsCount();
        dashboardView.updateProductCount(productCount);

        // Cập nhật tổng giá trị kho hàng
        long inventoryValue = productDAO.getTotalInventoryValue();
        dashboardView.updateInventoryValue(inventoryValue);

        // Cập nhật tổng doanh thu từ các đơn đã xuất
        long totalSales = getTotalSalesAmount();
        dashboardView.updateTotalSales(totalSales);
    }

    // Lấy tổng giá trị doanh thu từ bảng Transaction
    private long getTotalSalesAmount() {
        long total = 0;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT SUM(CAST(Total AS DECIMAL(18,2))) AS TotalSales FROM [Transaction]")) {
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                total = (long) rs.getDouble("TotalSales");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi lấy tổng doanh thu: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }

    // Thiết lập các event listener
    private void setupEventListeners() {
        // Listener cho nút quản lý sản phẩm
        dashboardView.setProductButtonListener(e -> {
            if (productListener != null) {
                productListener.onProductManagementRequested();
            }
        });

        // Listener cho nút quản lý danh mục
        dashboardView.setCategoryButtonListener(e -> {
            if (categoryListener != null) {
                categoryListener.onCategoryManagementRequested();
            }
        });

        // Listener cho nút quản lý người dùng
        dashboardView.setUserButtonListener(e -> {
            if (userListener != null) {
                userListener.onUserManagementRequested();
            }
        });

        // Listener cho nút lịch sử nhập kho
        dashboardView.setHistoryButtonListener(e -> {
            if (historyListener != null) {
                historyListener.onHistoryRequested();
            }
        });

        // Listener cho nút quản lý xuất kho
        dashboardView.setOrderButtonListener(e -> {
            if (orderListener != null) {
                orderListener.onOrderManagementRequested();
            }
        });

        // Listener cho nút đăng xuất
        dashboardView.setLogoutButtonListener(e -> {
            if (logoutListener != null) {
                logoutListener.onLogoutRequested();
            }
        });
    }

    // Interface và listener cho chuyển hướng đến quản lý sản phẩm
    public interface ProductManagementListener {
        void onProductManagementRequested();
    }

    private ProductManagementListener productListener;

    public void setProductManagementListener(ProductManagementListener listener) {
        this.productListener = listener;
    }

    // Interface và listener cho chuyển hướng đến quản lý danh mục
    public interface CategoryManagementListener {
        void onCategoryManagementRequested();
    }

    private CategoryManagementListener categoryListener;

    public void setCategoryManagementListener(CategoryManagementListener listener) {
        this.categoryListener = listener;
    }

    // Interface và listener cho chuyển hướng đến quản lý người dùng
    public interface UserManagementListener {
        void onUserManagementRequested();
    }

    private UserManagementListener userListener;

    public void setUserManagementListener(UserManagementListener listener) {
        this.userListener = listener;
    }

    // Interface và listener cho chuyển hướng đến xem lịch sử nhập kho
    public interface HistoryListener {
        void onHistoryRequested();
    }

    private HistoryListener historyListener;

    public void setHistoryListener(HistoryListener listener) {
        this.historyListener = listener;
    }

    // Interface và listener cho chuyển hướng đến quản lý xuất kho
    public interface OrderManagementListener {
        void onOrderManagementRequested();
    }

    private OrderManagementListener orderListener;

    public void setOrderManagementListener(OrderManagementListener listener) {
        this.orderListener = listener;
    }

    // Interface và listener cho đăng xuất
    public interface LogoutListener {
        void onLogoutRequested();
    }

    private LogoutListener logoutListener;

    public void setLogoutListener(LogoutListener listener) {
        this.logoutListener = listener;
    }

    // Getter cho thông tin tài khoản đăng nhập
    public Account getLoggedInAccount() {
        return loggedInAccount;
    }
}