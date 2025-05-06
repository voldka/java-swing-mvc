package View;

import Model.History;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class HistoryView extends JPanel {
    // Components for search
    private JTextField searchField;
    private JButton searchButton;

    // Buttons
    private JButton backButton;
    private JButton refreshButton;

    // Table
    private JTable historyTable;
    private DefaultTableModel tableModel;

    // Date formatter
    private SimpleDateFormat dateFormat;

    public HistoryView() {
        // Initialize
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        // Setup layout
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with title and back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("LỊCH SỬ NHẬP KHO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        searchField = new JTextField(20);
        searchButton = new JButton("Tìm kiếm");
        refreshButton = new JButton("Làm mới");

        searchPanel.add(new JLabel("Tên sản phẩm:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);

        add(searchPanel, BorderLayout.SOUTH);

        // Table panel
        String[] columns = { "ID", "Mã SP", "Tên sản phẩm", "Số lượng nhập", "Ngày nhập" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        historyTable = new JTable(tableModel);

        // Set column widths
        historyTable.getColumnModel().getColumn(0).setPreferredWidth(30);
        historyTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        historyTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        historyTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        historyTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Table will fill the viewport height
        JScrollPane scrollPane = new JScrollPane(historyTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách lịch sử nhập kho"));

        add(scrollPane, BorderLayout.CENTER);
    }

    // Load history to table
    public void loadHistory(List<History> historyList) {
        tableModel.setRowCount(0);

        for (History history : historyList) {
            Object[] row = new Object[] {
                    history.getId(),
                    history.getProductId(),
                    history.getProductName(),
                    history.getAddedStocks(),
                    dateFormat.format(history.getDate())
            };
            tableModel.addRow(row);
        }
    }

    // Get search keyword
    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    // Add action listeners for buttons
    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public void setRefreshButtonListener(ActionListener listener) {
        refreshButton.addActionListener(listener);
    }
}