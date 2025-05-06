package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterView() {
        // Sử dụng GridBagLayout để bố trí các thành phần
        setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        // Thêm tiêu đề
        JLabel titleLabel = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
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

        // Confirm password label và field
        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        add(confirmPasswordLabel, gridBagConstraints);

        confirmPasswordField = new JPasswordField(20);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        add(confirmPasswordField, gridBagConstraints);

        // Email label và field
        JLabel emailLabel = new JLabel("Email:");
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        add(emailLabel, gridBagConstraints);

        emailField = new JTextField(20);
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        add(emailField, gridBagConstraints);

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // Register button
        registerButton = new JButton("Đăng ký");
        buttonPanel.add(registerButton);

        // Back button
        backButton = new JButton("Quay lại đăng nhập");
        buttonPanel.add(backButton);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
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

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public String getEmail() {
        return emailField.getText();
    }

    // Đặt action listener cho các nút
    public void setRegisterButtonListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    // Reset form
    public void resetForm() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        emailField.setText("");
        usernameField.requestFocus();
    }
}