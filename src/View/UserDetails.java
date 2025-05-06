package View;

import Model.Account;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionListener;

public class UserDetails extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JButton backButton;

    public UserDetails() {
        setLayout(new BorderLayout());

        // Create table model with column names
        String[] columns = { "User ID", "Username", "Email" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };

        // Create JTable with the model
        userTable = new JTable(tableModel);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.setFillsViewportHeight(true);

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("QUẢN LÝ NGƯỜI DUNG", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        backButton = new JButton("Quay lại");
        backButton.setFont(new Font("Arial", Font.BOLD, 12));
        headerPanel.add(backButton, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Add table to a scroll pane
        scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Update the table with a list of accounts
     * 
     * @param accounts List of Account objects to display
     */
    public void getUsers(List<Account> accounts) {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Add each account to the table
        for (Account account : accounts) {
            Object[] row = {
                    account.getUid(),
                    account.getUsername(),
                    account.getEmail()
            };
            tableModel.addRow(row);
        }

        // Refresh the table
        userTable.revalidate();
        userTable.repaint();
    }

    /**
     * Sets the action listener for the back button
     * 
     * @param listener ActionListener to handle back button clicks
     */
    public void setBackButtonListener(ActionListener listener) {
        // Remove any existing listeners first
        for (ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }

        // Add the new listener
        backButton.addActionListener(listener);
    }
}
