package View;

import Model.Product;
import Model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderDetailView extends JPanel {
    // Components for the form
    private JTextField orderIdField;
    private JTextField customerNameField;
    private JComboBox<Product> productComboBox;
    private JTextField quantityField;
    private JTextField priceField;
    private JTextField amountField;

    // Buttons
    private JButton addItemButton;
    private JButton removeItemButton;
    private JButton saveOrderButton;
    private JButton backToOrdersButton;

    // Table
    private JTable orderItemsTable;
    private DefaultTableModel tableModel;

    // Format currency
    private NumberFormat currencyFormatter;

    // Order label for display
    private JLabel orderInfoLabel;
    private int currentOrderId;

    public OrderDetailView() {
        // Initialize
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currentOrderId = -1;

        // Setup layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        orderInfoLabel = new JLabel("CHI TIẾT ĐƠN HÀNG", SwingConstants.CENTER);
        orderInfoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(orderInfoLabel, BorderLayout.CENTER);

        backToOrdersButton = new JButton("Quay lại danh sách đơn hàng");
        backToOrdersButton.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(backToOrdersButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Form panel for adding items to order
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thêm sản phẩm vào đơn hàng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Order ID field (hidden)
        orderIdField = new JTextField(10);
        orderIdField.setVisible(false);

        // Customer name field (readonly)
        JLabel customerNameLabel = new JLabel("Khách hàng:");
        customerNameField = new JTextField(20);
        customerNameField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(customerNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(customerNameField, gbc);

        // Product combo box
        JLabel productLabel = new JLabel("Sản phẩm:");
        productComboBox = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(productLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(productComboBox, gbc);

        // Quantity field
        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(quantityLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(quantityField, gbc);

        // Price field
        JLabel priceLabel = new JLabel("Giá (VND):");
        priceField = new JTextField(15);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(priceLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(priceField, gbc);

        // Amount field (calculated)
        JLabel amountLabel = new JLabel("Thành tiền:");
        amountField = new JTextField(15);
        amountField.setEditable(false);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(amountLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(amountField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addItemButton = new JButton("Thêm sản phẩm");
        removeItemButton = new JButton("Xóa sản phẩm");
        saveOrderButton = new JButton("Lưu đơn hàng");

        buttonPanel.add(addItemButton);
        buttonPanel.add(removeItemButton);
        buttonPanel.add(saveOrderButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // Table panel
        String[] columns = { "ID", "Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderItemsTable = new JTable(tableModel);

        // Set column widths
        orderItemsTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        orderItemsTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        orderItemsTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        orderItemsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        orderItemsTable.getColumnModel().getColumn(4).setPreferredWidth(100);

        orderItemsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = orderItemsTable.getSelectedRow();
                if (selectedRow >= 0) {
                    // Enable remove button when row is selected
                    removeItemButton.setEnabled(true);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(orderItemsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết đơn hàng"));

        add(scrollPane, BorderLayout.CENTER);

        // Initialize button states
        removeItemButton.setEnabled(false);
    }

    // Set current order info
    public void setOrderInfo(int orderId, String customerName) {
        this.currentOrderId = orderId;
        this.orderIdField.setText(String.valueOf(orderId));
        this.customerNameField.setText(customerName);
        this.orderInfoLabel.setText("CHI TIẾT ĐƠN HÀNG #" + orderId);
    }

    // Get current order ID
    public int getCurrentOrderId() {
        return this.currentOrderId;
    }

    // Load order items
    public void loadOrderItems(List<Transaction> transactions) {
        tableModel.setRowCount(0);
        double total = 0;

        for (Transaction transaction : transactions) {
            Object[] row = new Object[] {
                    transaction.getId(),
                    transaction.getProductName(),
                    transaction.getQuantity(),
                    currencyFormatter.format(transaction.getPrice()),
                    currencyFormatter.format(transaction.getAmount())
            };
            tableModel.addRow(row);
            total += transaction.getAmount();
        }

        // Update the order total at the bottom of the table
        JLabel totalLabel = new JLabel("Tổng cộng: " + currencyFormatter.format(total));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        // add total label to bottom of the panel
        add(totalLabel, BorderLayout.SOUTH);
    }

    // Load products to combo box
    public void loadProducts(List<Product> products) {
        productComboBox.removeAllItems();
        for (Product product : products) {
            productComboBox.addItem(product);
        }

        // Add product selection listener to auto-fill price
        productComboBox.addActionListener(e -> {
            if (productComboBox.getSelectedItem() != null) {
                Product selectedProduct = (Product) productComboBox.getSelectedItem();
                priceField.setText(String.valueOf(selectedProduct.getPrice()));
                calculateAmount();
            }
        });

        // Add document listener to quantity field to auto-calculate amount
        quantityField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmount();
            }
        });

        // Add document listener to price field to auto-calculate amount
        priceField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calculateAmount();
            }
        });
    }

    // Calculate amount based on quantity and price
    private void calculateAmount() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            double amount = quantity * price;
            amountField.setText(currencyFormatter.format(amount));
        } catch (NumberFormatException e) {
            amountField.setText("");
        }
    }

    // Get selected item ID for removal
    public int getSelectedItemId() {
        int selectedRow = orderItemsTable.getSelectedRow();
        if (selectedRow >= 0) {
            return Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        }
        return -1;
    }

    // Get transaction from form
    public Transaction getTransactionFromForm() {
        Transaction transaction = new Transaction();

        // Set order ID
        transaction.setOrderId(currentOrderId);

        // Set product ID and price from selected product
        if (productComboBox.getSelectedItem() != null) {
            Product selectedProduct = (Product) productComboBox.getSelectedItem();
            transaction.setProductId(selectedProduct.getId());
            transaction.setProductName(selectedProduct.getName());
        }

        // Set quantity
        try {
            transaction.setQuantity(Integer.parseInt(quantityField.getText()));
        } catch (NumberFormatException e) {
            return null; // Return null if quantity is not valid
        }

        // Set price
        try {
            transaction.setPrice(Double.parseDouble(priceField.getText()));
        } catch (NumberFormatException e) {
            return null; // Return null if price is not valid
        }

        return transaction;
    }

    // Clear form
    public void clearItemForm() {
        if (productComboBox.getItemCount() > 0) {
            productComboBox.setSelectedIndex(0);
        }
        quantityField.setText("");
        priceField.setText("");
        amountField.setText("");
        removeItemButton.setEnabled(false);
    }

    // Set action listeners
    public void setAddItemButtonListener(ActionListener listener) {
        addItemButton.addActionListener(listener);
    }

    public void setRemoveItemButtonListener(ActionListener listener) {
        removeItemButton.addActionListener(listener);
    }

    public void setSaveOrderButtonListener(ActionListener listener) {
        saveOrderButton.addActionListener(listener);
    }

    public void setBackButtonListener(ActionListener listener) {
        backToOrdersButton.addActionListener(listener);
    }
}