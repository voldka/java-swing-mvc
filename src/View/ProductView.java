package View;

import Model.Category;
import Model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductView extends JPanel {
    // Components for the form
    private JTextField idField;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField stockField;
    // Đơn vị đã được ẩn, nhưng vẫn giữ biến để tương thích
    private JTextField unitField;
    private JComboBox<Category> categoryComboBox;

    // Components for search
    private JTextField searchField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;
    private JButton searchButton;
    private JButton importButton;

    // Table
    private JTable productTable;
    private DefaultTableModel tableModel;

    // Format currency
    private NumberFormat currencyFormatter;

    public ProductView() {
        // Initialize
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // Setup layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("QUẢN LÝ SẢN PHẨM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ID field (disabled, used for updates)
        JLabel idLabel = new JLabel("Mã sản phẩm:");
        idField = new JTextField(10);
        idField.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        // Name field
        JLabel nameLabel = new JLabel("Tên sản phẩm:");
        nameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        // Price field
        JLabel priceLabel = new JLabel("Giá (VND):");
        priceField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        // Stock field
        JLabel stockLabel = new JLabel("Tồn kho:");
        stockField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(stockLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(stockField, gbc);

        // Unit field - Đã ẩn, không thêm vào giao diện
        unitField = new JTextField(10);
        unitField.setText("1"); // Đặt giá trị mặc định là 1

        // Category combo box
        JLabel categoryLabel = new JLabel("Loại sản phẩm:");
        categoryComboBox = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 4; // Dòng thứ 4 thay vì thứ 5 vì đã bỏ trường đơn vị
        formPanel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(categoryComboBox, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Thêm");
        updateButton = new JButton("Cập nhật");
        deleteButton = new JButton("Xóa");
        clearButton = new JButton("Xóa form");
        importButton = new JButton("Nhập kho");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(importButton);

        gbc.gridx = 0;
        gbc.gridy = 5; // Dòng thứ 5 thay vì thứ 6 vì đã bỏ trường đơn vị
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sản phẩm"));

        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");

        searchPanel.add(new JLabel("Tên sản phẩm:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        gbc.gridx = 0;
        gbc.gridy = 6; // Dòng thứ 6 thay vì thứ 7 vì đã bỏ trường đơn vị
        gbc.gridwidth = 2;
        formPanel.add(searchPanel, gbc);

        // Add form panel to main panel
        add(formPanel, BorderLayout.WEST);

        // Table panel
        String[] columns = { "ID", "Tên sản phẩm", "Giá", "Tồn kho", "Loại sản phẩm" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);

        // Set column widths
        productTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Add click listener to table
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displaySelectedProduct(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        add(scrollPane, BorderLayout.CENTER);
    }

    // Display selected product on form
    private void displaySelectedProduct(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        nameField.setText(tableModel.getValueAt(row, 1).toString());

        // Convert price format back to number
        String priceStr = tableModel.getValueAt(row, 2).toString();
        priceStr = priceStr.replaceAll("[^0-9]", "");
        priceField.setText(priceStr);

        stockField.setText(tableModel.getValueAt(row, 3).toString());
        unitField.setText("1"); // Mặc định là 1

        String categoryName = tableModel.getValueAt(row, 4).toString();
        for (int i = 0; i < categoryComboBox.getItemCount(); i++) {
            Category category = categoryComboBox.getItemAt(i);
            if (category.getCategoryItem().equals(categoryName)) {
                categoryComboBox.setSelectedIndex(i);
                break;
            }
        }
    }

    // Load categories to combo box
    public void loadCategories(List<Category> categories) {
        categoryComboBox.removeAllItems();
        for (Category category : categories) {
            categoryComboBox.addItem(category);
        }
    }

    // Load products to table
    public void loadProducts(List<Product> products) {
        tableModel.setRowCount(0);
        for (Product product : products) {
            Object[] row = new Object[] {
                    product.getId(),
                    product.getName(),
                    currencyFormatter.format(product.getPrice()),
                    product.getStock(),
                    product.getCategory()
            };
            tableModel.addRow(row);
        }
    }

    // Get product from form
    public Product getProductFromForm() {
        Product product = new Product();

        if (!idField.getText().isEmpty()) {
            product.setId(Integer.parseInt(idField.getText()));
        }

        product.setName(nameField.getText());
        product.setPrice(Integer.parseInt(priceField.getText()));
        product.setStock(Integer.parseInt(stockField.getText()));
        // Đơn vị luôn là 1
        product.setUnit(1);

        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
        product.setCategory(selectedCategory.getCategoryItem());

        return product;
    }

    // Get import quantity for stock update
    public int getImportQuantity() {
        String input = JOptionPane.showInputDialog(this, "Nhập số lượng cần nhập kho:", "Nhập kho",
                JOptionPane.QUESTION_MESSAGE);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Clear form
    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        stockField.setText("");
        unitField.setText("1"); // Luôn đặt giá trị mặc định là 1
        categoryComboBox.setSelectedIndex(0);
        searchField.setText("");
    }

    // Get search keyword
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    // Get selected product ID
    public int getSelectedProductId() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            return Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        }
        return -1;
    }

    // Add action listeners for buttons
    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void setImportButtonListener(ActionListener listener) {
        importButton.addActionListener(listener);
    }
}