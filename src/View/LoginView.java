package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView() {
        // Sử dụng GridBagLayout để bố trí các thành phần
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // Thêm tiêu đề
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP HỆ THỐNG QUẢN LÝ KHO", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Thêm các thành phần vào panel
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        add(titleLabel, gridBagConstraints);

        // Username label và field
        JLabel usernameLabel = new JLabel("Tên đăng nhập:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 1;
        add(usernameLabel, gridBagConstraints);

        usernameField = new JTextField(20);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(usernameField, gridBagConstraints);

        // Password label và field
        JLabel passwordLabel = new JLabel("Mật khẩu:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        add(passwordLabel, gridBagConstraints);

        passwordField = new JPasswordField(20);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(passwordField, gridBagConstraints);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Login button
        loginButton = new JButton("Đăng nhập");
        buttonPanel.add(loginButton);

        // Register button
        registerButton = new JButton("Đăng ký tài khoản mới");
        buttonPanel.add(registerButton);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new Insets(15, 5, 5, 5);
        add(buttonPanel, gridBagConstraints);
    }

    // Getter methods
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    // Đặt action listener cho nút đăng nhập
    public void setLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    // Đặt action listener cho nút đăng ký
    public void setRegisterButtonListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // Reset form
    public void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocus();
    }
}