package Model;

import DAO.UserDAO;
import java.io.*;
import java.util.ArrayList;

public class Database {

    private ArrayList<Account> accountArrayList;
    private UserDAO userDAO;

    public Database() {
        accountArrayList = new ArrayList<>();
        userDAO = new UserDAO();
        // Create the Account table if it doesn't exist
        userDAO.createTableIfNotExists();
    }

    // adds account to our collection
    public void addAccount(Account account) {
        accountArrayList.add(account);
    }

    // saves account to database
    public void saveAccount(File file) {
        try {
            // Using SQL Server now, but still maintaining File parameter for compatibility
            Account account;

            int i = 0;
            while (i < accountArrayList.size()) {
                account = accountArrayList.get(i);
                // Add account to SQL Server database
                userDAO.addAccount(account);
                i++;
            }

            // Clear the array list after saving to avoid duplicates on next save
            accountArrayList.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // reads accounts from database
    public Object[] loadAccounts(File file) {
        // Using SQL Server now, but still maintaining File parameter for compatibility
        try {
            // Get accounts from SQL Server database
            return userDAO.loadAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
