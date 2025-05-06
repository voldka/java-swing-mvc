package Controller;

import DAO.AccountDAO;
import Model.Account;
import View.LoginView;

import javax.swing.*;

public class LoginController {
    private AccountDAO accountDAO;
    private LoginView loginView;
    private Account loggedInAccount;

    public LoginController(LoginView loginView) {
        this.accountDAO = new AccountDAO();
        this.loginView = loginView;

        // Thiết lập action listener cho nút đăng nhập
        this.loginView.setLoginButtonListener(e -> login());

        // Thiết lập action listener cho nút đăng ký
        this.loginView.setRegisterButtonListener(e -> fireShowRegisterEvent());
    }

    // Phương thức đăng nhập
    private void login() {
        // Lấy thông tin đăng nhập từ view
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        // Kiểm tra thông tin nhập vào
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginView, "Vui lòng nhập đầy đủ thông tin đăng nhập!",
                    "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Xác thực đăng nhập
        loggedInAccount = accountDAO.authenticate(username, password);

        if (loggedInAccount != null) {
            JOptionPane.showMessageDialog(loginView, "Đăng nhập thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            // Thông báo sự kiện đăng nhập thành công cho MainFrame để chuyển hướng
            fireLoginSuccessEvent();
        } else {
            JOptionPane.showMessageDialog(loginView, "Tên đăng nhập hoặc mật khẩu không đúng!",
                    "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            loginView.resetForm();
        }
    }

    // Interface cho event listener đăng nhập thành công
    public interface LoginListener {
        void onLoginSuccess(Account account);
    }

    private LoginListener loginListener;

    // Thiết lập listener
    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    // Gửi thông báo đăng nhập thành công
    private void fireLoginSuccessEvent() {
        if (loginListener != null && loggedInAccount != null) {
            loginListener.onLoginSuccess(loggedInAccount);
        }
    }

    // Interface cho event listener hiển thị màn hình đăng ký
    public interface ShowRegisterListener {
        void onShowRegister();
    }

    private ShowRegisterListener showRegisterListener;

    // Thiết lập listener cho hiển thị màn hình đăng ký
    public void setShowRegisterListener(ShowRegisterListener listener) {
        this.showRegisterListener = listener;
    }

    // Gửi thông báo hiển thị màn hình đăng ký
    private void fireShowRegisterEvent() {
        if (showRegisterListener != null) {
            showRegisterListener.onShowRegister();
        }
    }

    // Lấy thông tin người dùng đã đăng nhập
    public Account getLoggedInAccount() {
        return loggedInAccount;
    }
}