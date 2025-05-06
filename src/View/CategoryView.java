package View;

import Model.Category;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CategoryView extends JPanel {
    // Components for the form
    private JTextField idField;
    private JTextField categoryNameField;

    // Buttons
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;

    // Table
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    public CategoryView() {
        // Setup layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("QUẢN LÝ LOẠI SẢN PHẨM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin loại sản phẩm"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // ID field (disabled, used for updates)
        JLabel idLabel = new JLabel("Mã loại:");
        idField = new JTextField(10);
        idField.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(idLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        // Category name field
        JLabel nameLabel = new JLabel("Tên loại sản phẩm:");
        categoryNameField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(categoryNameField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("Thêm");
        updateButton = new JButton("Cập nhật");
        deleteButton = new JButton("Xóa");
        clearButton = new JButton("Xóa form");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // Add form panel to main panel
        add(formPanel, BorderLayout.WEST);

        // Table panel
        String[] columns = { "ID", "Tên loại sản phẩm" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        categoryTable = new JTable(tableModel);

        // Add click listener to table
        categoryTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = categoryTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displaySelectedCategory(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách loại sản phẩm"));

        add(scrollPane, BorderLayout.CENTER);
    }

    // Display selected category on form
    private void displaySelectedCategory(int row) {
        idField.setText(tableModel.getValueAt(row, 0).toString());
        categoryNameField.setText(tableModel.getValueAt(row, 1).toString());
    }

    // Load categories to table
    public void loadCategories(List<Category> categories) {
        tableModel.setRowCount(0);
        for (Category category : categories) {
            Object[] row = new Object[] {
                    category.getId(),
                    category.getCategoryItem()
            };
            tableModel.addRow(row);
        }
    }

    // Get category from form
    public Category getCategoryFromForm() {
        Category category = new Category();

        if (!idField.getText().isEmpty()) {
            category.setId(Integer.parseInt(idField.getText()));
        }

        category.setCategoryItem(categoryNameField.getText());

        return category;
    }

    // Clear form
    public void clearForm() {
        idField.setText("");
        categoryNameField.setText("");
    }

    // Get selected category ID
    public int getSelectedCategoryId() {
        int selectedRow = categoryTable.getSelectedRow();
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
}