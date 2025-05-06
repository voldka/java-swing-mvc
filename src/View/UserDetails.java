package View;

import Model.Account;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserDetails extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

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
}
