package Controller;

import Model.Account;
import View.Form;
import View.UserDetails;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List; // Import List

public class UserController {
    // database file - giữ lại cho tương thích với code cũ
    private String databaseFile = "src\\data\\database.txt";
    private Form form;
    private UserDetails userDetails;

    public interface BackListener {
        void onBack();
    }

    private BackListener backListener;

    public void setBackListener(BackListener listener) {
        this.backListener = listener;
    }

    // Phương thức để gọi sự kiện quay lại
    private void fireBackEvent() {
        if (backListener != null) {
            backListener.onBack();
        }
    }

    public UserController(Form form, UserDetails userDetails) {
        this.form = form;
        this.userDetails = userDetails;

        // Tạo bảng Account nếu chưa tồn tại
        Account.createTableIfNotExists();

        // submit user
        this.form.submitUsers(e -> {
            String username = this.form.getUsername().trim();
            String email = this.form.getEmail().trim();
            String password = this.form.getPassword().trim();

            // simple validations
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this.form, "Username Required.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this.form, "Password Required.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } else if (email.isEmpty()) {
                JOptionPane.showMessageDialog(this.form, "Email Required.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo và lưu account trực tiếp
            Account newAccount = new Account(username, password, email);
            newAccount.saveAccount();
            this.form.reset(true);
        });

        // load accounts
        this.form.viewUsers(e -> {
            this.userDetails.getUsers(Account.loadAccounts());
        });
    }

    // Method to load users and update the view
    public void loadUsers() {
        List<Account> accounts = Account.loadAccounts();
        this.userDetails.getUsers(accounts); // Update the UserDetails view
    }

    /**
     * Get list of all users/accounts from the database
     * 
     * @return List of Account objects
     */
    public List<Account> getUsers() {
        return Account.loadAccounts();
    }

    // Method to search users by a keyword (username or email)
    public List<Account> searchUsers(String keyword) {
        List<Account> allAccounts = getUsers();
        List<Account> filteredAccounts = new ArrayList<>();

        for (Account account : allAccounts) {
            if (account.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                    account.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                filteredAccounts.add(account);
            }
        }

        return filteredAccounts;
    }
}
