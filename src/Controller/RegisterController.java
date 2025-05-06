package Controller;

import DAO.AccountDAO;
import Model.Account;
import View.RegisterView;

import javax.swing.*;
import java.util.regex.Pattern;

public class RegisterController {
    private AccountDAO accountDAO;
    private RegisterView registerView;

    public RegisterController(RegisterView registerView) {
        this.accountDAO = new AccountDAO();
        this.registerView = registerView;

        // Thiết lập action listener cho nút đăng ký
        this.registerView.setRegisterButtonListener(e -> register());

        // Thiết lập action listener cho nút quay lại
        this.registerView.setBackButtonListener(e -> fireBackToLoginEvent());
    }

    // Phương thức đăng ký
    private void register() {
        // Lấy thông tin đăng ký từ view
        String username = registerView.getUsername();
        String password = registerView.getPassword();
        String confirmPassword = registerView.getConfirmPassword();
        String email = registerView.getEmail();

        // Kiểm tra thông tin nhập vào
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(registerView, "Vui lòng nhập đầy đủ thông tin đăng ký!",
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra mật khẩu nhập lại
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(registerView, "Mật khẩu nhập lại không khớp!",
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(registerView, "Email không hợp lệ!",
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra username đã tồn tại chưa
        if (accountDAO.isUsernameExists(username)) {
            JOptionPane.showMessageDialog(registerView, "Tên đăng nhập đã tồn tại!",
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra email đã tồn tại chưa
        if (accountDAO.isEmailExists(email)) {
            JOptionPane.showMessageDialog(registerView, "Email đã được sử dụng!",
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo tài khoản mới
        Account newAccount = new Account(username, password, email);
        boolean success = accountDAO.addAccount(newAccount);

        if (success) {
            JOptionPane.showMessageDialog(registerView, "Đăng ký tài khoản thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            registerView.resetForm();

            // Quay lại màn hình đăng nhập
            fireBackToLoginEvent();
        } else {
            JOptionPane.showMessageDialog(registerView, "Có lỗi xảy ra khi đăng ký tài khoản!",
                    "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Kiểm tra email hợp lệ
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    // Interface cho event listener quay lại đăng nhập
    public interface BackToLoginListener {
        void onBackToLogin();
    }

    private BackToLoginListener backToLoginListener;

    // Thiết lập listener
    public void setBackToLoginListener(BackToLoginListener listener) {
        this.backToLoginListener = listener;
    }

    // Gửi thông báo quay lại đăng nhập
    private void fireBackToLoginEvent() {
        if (backToLoginListener != null) {
            backToLoginListener.onBackToLogin();
        }
    }
}