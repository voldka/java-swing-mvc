package View;

import Controller.*;
import Model.Account;
import Model.Order;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // Views
    private LoginView loginView;
    private RegisterView registerView;
    private DashboardView dashboardView;
    private ProductView productView;
    private CategoryView categoryView;
    private HistoryView historyView;
    private OrderView orderView;
    private OrderDetailView orderDetailView;
    private UserDetails userDetailsView;

    // Controllers
    private LoginController loginController;
    private RegisterController registerController;
    private DashboardController dashboardController;
    private ProductController productController;
    private CategoryController categoryController;
    private HistoryController historyController;
    private OrderController orderController;
    private OrderDetailController orderDetailController;
    private UserController userController;

    // Current logged in account
    private Account currentAccount;

    // Card names for card layout
    private static final String LOGIN_CARD = "login";
    private static final String REGISTER_CARD = "register";
    private static final String DASHBOARD_CARD = "dashboard";
    private static final String PRODUCT_CARD = "product";
    private static final String CATEGORY_CARD = "category";
    private static final String HISTORY_CARD = "history";
    private static final String ORDER_CARD = "order";
    private static final String ORDER_DETAIL_CARD = "orderDetail";
    private static final String USER_CARD = "user";

    public MainFrame() {
        setTitle("Hệ thống Quản lý Kho");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set application icon if available
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/appicon.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Could not load application icon.");
        }

        // Initialize card layout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initialize views
        initViews();

        // Initialize controllers
        initControllers();

        // Add card panels
        cardPanel.add(loginView, LOGIN_CARD);
        cardPanel.add(registerView, REGISTER_CARD);
        cardPanel.add(dashboardView, DASHBOARD_CARD);
        cardPanel.add(productView, PRODUCT_CARD);
        cardPanel.add(categoryView, CATEGORY_CARD);
        cardPanel.add(historyView, HISTORY_CARD);
        cardPanel.add(orderView, ORDER_CARD);
        cardPanel.add(orderDetailView, ORDER_DETAIL_CARD);
        cardPanel.add(userDetailsView, USER_CARD);

        // Add card panel to frame
        getContentPane().add(cardPanel);

        // Start with login screen
        cardLayout.show(cardPanel, LOGIN_CARD);
    }

    // Initialize all views
    private void initViews() {
        loginView = new LoginView();
        registerView = new RegisterView();
        dashboardView = new DashboardView();
        productView = new ProductView();
        categoryView = new CategoryView();
        historyView = new HistoryView();
        orderView = new OrderView();
        orderDetailView = new OrderDetailView();
        userDetailsView = new UserDetails();
    }

    // Initialize all controllers
    private void initControllers() {
        // Login controller
        loginController = new LoginController(loginView);
        loginController.setLoginListener(account -> {
            currentAccount = account;
            showDashboard();
        });
        loginController.setShowRegisterListener(() -> {
            showRegister();
        });

        // Register controller
        registerController = new RegisterController(registerView);
        registerController.setBackToLoginListener(() -> {
            showLogin();
        });

        // Dashboard controller will be initialized when user logs in

        // Product controller
        productController = new ProductController(productView);
        productController.setBackListener(() -> {
            showDashboard();
            // Update dashboard info when going back
            if (dashboardController != null) {
                dashboardController.updateDashboardInfo();
            }
        });

        // Category controller
        categoryController = new CategoryController(categoryView);
        categoryController.setBackListener(() -> {
            showDashboard();
            // Update dashboard info when going back
            if (dashboardController != null) {
                dashboardController.updateDashboardInfo();
            }
        });

        // History controller
        historyController = new HistoryController(historyView);
        historyController.setBackListener(() -> {
            showDashboard();
            // Update dashboard info when going back
            if (dashboardController != null) {
                dashboardController.updateDashboardInfo();
            }
        });

        // Order controller
        orderController = new OrderController(orderView, orderDetailView);
        orderController.setBackListener(() -> {
            showDashboard();
            // Update dashboard info when going back
            if (dashboardController != null) {
                dashboardController.updateDashboardInfo();
            }
        });
        orderController.setViewOrderDetailListener(order -> {
            showOrderDetail(order);
        });

        // Order Detail controller
        orderDetailController = new OrderDetailController(orderDetailView);
        orderDetailController.setBackToOrdersListener(() -> {
            showOrderManagement();
        });

        // User controller
        Form form = new Form();
        userController = new UserController(form, userDetailsView);
        userController.setBackListener(() -> {
            showDashboard();
        });
    }

    // Show register screen
    private void showRegister() {
        cardLayout.show(cardPanel, REGISTER_CARD);
    }

    // Show login screen
    private void showLogin() {
        // Reset login form
        loginView.resetForm();
        cardLayout.show(cardPanel, LOGIN_CARD);
    }

    // Show dashboard after login
    private void showDashboard() {
        if (dashboardController == null) {
            dashboardController = new DashboardController(dashboardView, currentAccount);

            // Set dashboard listeners
            dashboardController.setProductManagementListener(() -> {
                showProductManagement();
            });

            dashboardController.setCategoryManagementListener(() -> {
                showCategoryManagement();
            });

            dashboardController.setUserManagementListener(() -> {
                showUserManagement();
            });

            dashboardController.setHistoryListener(() -> {
                showHistoryView();
            });

            dashboardController.setOrderManagementListener(() -> {
                showOrderManagement();
            });

            dashboardController.setLogoutListener(() -> {
                logout();
            });
        } else {
            // Refresh dashboard info
            dashboardController.updateDashboardInfo();
        }

        cardLayout.show(cardPanel, DASHBOARD_CARD);
    }

    // Show product management screen
    private void showProductManagement() {
        // Reload products and categories
        productController.loadProducts();
        cardLayout.show(cardPanel, PRODUCT_CARD);
    }

    // Show category management screen
    private void showCategoryManagement() {
        // Reload categories
        categoryController.loadCategories();
        cardLayout.show(cardPanel, CATEGORY_CARD);
    }

    // Show user management screen
    private void showUserManagement() {
        // Reload users using the controller method
        userController.loadUsers();
        cardLayout.show(cardPanel, USER_CARD);
    }

    // Show history view
    private void showHistoryView() {
        // Load history data
        historyController.loadHistory();
        cardLayout.show(cardPanel, HISTORY_CARD);
    }

    // Show order management screen
    private void showOrderManagement() {
        // Load orders
        orderController.loadAllOrders();
        cardLayout.show(cardPanel, ORDER_CARD);
    }

    // Show order detail screen
    private void showOrderDetail(Order order) {
        // Load order details
        orderDetailController.loadOrder(order);
        cardLayout.show(cardPanel, ORDER_DETAIL_CARD);
    }

    // Logout and return to login screen
    private void logout() {
        // Reset current user and clear forms
        currentAccount = null;
        dashboardController = null;

        // Reset forms
        loginView.resetForm();
        productView.clearForm();
        categoryView.clearForm();

        // Show login screen
        cardLayout.show(cardPanel, LOGIN_CARD);
    }
}
