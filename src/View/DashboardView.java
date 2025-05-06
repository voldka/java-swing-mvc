package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardView extends JPanel {
    private JLabel productCountLabel;
    private JLabel inventoryValueLabel;
    private JLabel totalSalesLabel;

    private JButton productButton;
    private JButton categoryButton;
    private JButton userButton;
    private JButton historyButton;
    private JButton orderButton;
    private JButton logoutButton;

    private NumberFormat currencyFormatter;

    public DashboardView() {
        // Khởi tạo formatter để định dạng tiền tệ
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        setLayout(new BorderLayout());

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ KHO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Logout button
        logoutButton = new JButton("Đăng xuất");
        headerPanel.add(logoutButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Main panel with buttons
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Product button
        productButton = createMenuButton("Quản lý sản phẩm", "Quản lý thông tin sản phẩm trong kho");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(productButton, gbc);

        // Category button
        categoryButton = createMenuButton("Quản lý danh mục", "Quản lý các danh mục sản phẩm");
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(categoryButton, gbc);

        // User button
        userButton = createMenuButton("Quản lý người dùng", "Quản lý thông tin người dùng");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(userButton, gbc);

        // History button
        historyButton = createMenuButton("Lịch sử nhập kho", "Xem lịch sử nhập hàng vào kho");
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(historyButton, gbc);

        // Order button
        orderButton = createMenuButton("Quản lý xuất kho", "Quản lý các đơn hàng xuất kho");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(orderButton, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panel hiển thị thông tin
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Thêm các thẻ thông tin với viền và màu sắc
        productCountLabel = createInfoCard("Tổng số sản phẩm", "0", new Color(52, 152, 219));
        inventoryValueLabel = createInfoCard("Tổng giá trị kho hàng", "0 đ", new Color(46, 204, 113));
        totalSalesLabel = createInfoCard("Tổng doanh thu", "0 đ", new Color(231, 76, 60));

        infoPanel.add(productCountLabel);
        infoPanel.add(inventoryValueLabel);
        infoPanel.add(totalSalesLabel);

        add(infoPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(200, 100));
        return button;
    }

    // Tạo thẻ thông tin với màu nền
    private JLabel createInfoCard(String title, String value, Color backgroundColor) {
        JLabel label = new JLabel("<html><div style='text-align:center;'><b>" + title
                + "</b><br><br><span style='font-size:20pt;'>" + value + "</span></div></html>", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(backgroundColor);
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return label;
    }

    // Cập nhật thông tin hiển thị
    public void updateProductCount(int count) {
        productCountLabel.setText(
                "<html><div style='text-align:center;'><b>Tổng số sản phẩm</b><br><br><span style='font-size:20pt;'>"
                        + count + "</span></div></html>");
    }

    public void updateInventoryValue(double value) {
        inventoryValueLabel.setText(
                "<html><div style='text-align:center;'><b>Tổng giá trị kho hàng</b><br><br><span style='font-size:20pt;'>"
                        + currencyFormatter.format(value) + "</span></div></html>");
    }

    public void updateTotalSales(double value) {
        totalSalesLabel.setText(
                "<html><div style='text-align:center;'><b>Tổng doanh thu</b><br><br><span style='font-size:20pt;'>"
                        + currencyFormatter.format(value) + "</span></div></html>");
    }

    // Các phương thức thêm action listener cho các nút
    public void setProductButtonListener(ActionListener listener) {
        productButton.addActionListener(listener);
    }

    public void setCategoryButtonListener(ActionListener listener) {
        categoryButton.addActionListener(listener);
    }

    public void setUserButtonListener(ActionListener listener) {
        userButton.addActionListener(listener);
    }

    public void setHistoryButtonListener(ActionListener listener) {
        historyButton.addActionListener(listener);
    }

    public void setOrderButtonListener(ActionListener listener) {
        orderButton.addActionListener(listener);
    }

    public void setLogoutButtonListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }
}