package View;

import Model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderView extends JPanel {
    // Components for the form
    private JTextField idField;
    private JTextField customerNameField;
    private JTextField orderDateField;
    private JComboBox<String> statusComboBox;
    private JTextField totalAmountField;
    private JTextArea noteArea;

    // Components for search
    private JTextField searchField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;
    private JButton searchButton;
    private JButton viewDetailButton;
    private JButton createOrderButton;

    // Table
    private JTable orderTable;
    private DefaultTableModel tableModel;

    // Format currency and date
    private NumberFormat currencyFormatter;
    private SimpleDateFormat dateFormat;

    public OrderView() {
        // Initialize formatters
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Setup layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("QUẢN LÝ ĐƠN HÀNG XUẤT KHO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đơn hàng"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ID field (disabled, used for updates)
        JLabel idLabel = new JLabel("Mã đơn hàng:");
        idField = new JTextField(10);
        idField.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        // Customer name field
        JLabel customerNameLabel = new JLabel("Tên khách hàng:");
        customerNameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(customerNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(customerNameField, gbc);

        // Status combo box
        JLabel statusLabel = new JLabel("Trạng thái:");
        String[] statuses = { "Pending", "Completed", "Canceled" };
        statusComboBox = new JComboBox<>(statuses);
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(statusComboBox, gbc);

        // Total amount field (read-only)
        JLabel totalAmountLabel = new JLabel("Tổng tiền:");
        totalAmountField = new JTextField(15);
        totalAmountField.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(totalAmountLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(totalAmountField, gbc);

        // Note area
        JLabel noteLabel = new JLabel("Ghi chú:");
        noteArea = new JTextArea(4, 20);
        noteArea.setLineWrap(true);
        noteArea.setWrapStyleWord(true);
        JScrollPane noteScrollPane = new JScrollPane(noteArea);
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(noteLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(noteScrollPane, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Thêm");
        updateButton = new JButton("Cập nhật");
        deleteButton = new JButton("Xóa");
        clearButton = new JButton("Xóa form");
        viewDetailButton = new JButton("Xem chi tiết");
        createOrderButton = new JButton("Tạo đơn xuất kho mới");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(viewDetailButton);
        buttonPanel.add(createOrderButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm đơn hàng"));

        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");

        searchPanel.add(new JLabel("Tên khách hàng:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(searchPanel, gbc);

        // Add form panel to main panel
        add(formPanel, BorderLayout.WEST);

        // Table panel
        String[] columns = { "ID", "Khách hàng", "Ngày đặt hàng", "Trạng thái", "Tổng tiền", "Ghi chú" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);

        // Set column widths
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(200);

        // Add click listener to table
        orderTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = orderTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displaySelectedOrder(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách đơn hàng xuất kho"));

        add(scrollPane, BorderLayout.CENTER);
    }

    // Display selected order on form
    private void displaySelectedOrder(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        customerNameField.setText(tableModel.getValueAt(row, 1).toString());

        String status = tableModel.getValueAt(row, 3).toString();
        for (int i = 0; i < statusComboBox.getItemCount(); i++) {
            if (statusComboBox.getItemAt(i).equals(status)) {
                statusComboBox.setSelectedIndex(i);
                break;
            }
        }

        String totalAmountStr = tableModel.getValueAt(row, 4).toString();
        totalAmountField.setText(totalAmountStr);

        noteArea.setText((String) tableModel.getValueAt(row, 5));
    }

    // Load orders to table
    public void loadOrders(List<Order> orders) {
        tableModel.setRowCount(0);

        for (Order order : orders) {
            Object[] row = new Object[] {
                    order.getId(),
                    order.getCustomerName(),
                    dateFormat.format(order.getOrderDate()),
                    order.getStatus(),
                    currencyFormatter.format(order.getTotalAmount()),
                    order.getNote()
            };
            tableModel.addRow(row);
        }
    }

    // Get order from form
    public Order getOrderFromForm() {
        Order order = new Order();

        if (!idField.getText().isEmpty()) {
            order.setId(Integer.parseInt(idField.getText()));
        }

        order.setCustomerName(customerNameField.getText());
        order.setOrderDate(new Date()); // Use current date
        order.setStatus((String) statusComboBox.getSelectedItem());

        // Total amount will be calculated and set by OrderController

        order.setNote(noteArea.getText());

        return order;
    }

    // Clear form
    public void clearForm() {
        idField.setText("");
        customerNameField.setText("");
        statusComboBox.setSelectedIndex(0);
        totalAmountField.setText("");
        noteArea.setText("");
        searchField.setText("");
    }

    // Get search keyword
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    // Get selected order ID
    public int getSelectedOrderId() {
        int selectedRow = orderTable.getSelectedRow();
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

    public void setViewDetailButtonListener(ActionListener listener) {
        viewDetailButton.addActionListener(listener);
    }

    public void setCreateOrderButtonListener(ActionListener listener) {
        createOrderButton.addActionListener(listener);
    }
}