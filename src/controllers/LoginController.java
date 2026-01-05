package controllers;

import java.util.regex.Pattern;
import models.UserManager;
import views.LoginView;
import views.TaskView; 

public class LoginController {
    private UserManager userManager;
    private LoginView view;

    public LoginController(UserManager userManager, LoginView view) {
        this.userManager = userManager;
        this.view = view;

        this.view.addLoginListener(e -> xyLyDangNhap());
        this.view.addRegisterListener(e -> xyLyDangKy());
        this.view.addSwitchToRegister(e -> { view.clearFields(); view.showRegisterScreen(); });
        this.view.addSwitchToLogin(e -> view.showLoginScreen());
    }

    public void showWindow() { view.setVisible(true); }

    private void xyLyDangNhap() {
        String u = view.getLoginUser();
        String p = view.getLoginPass();
        if (userManager.checkLogin(u, p)) {
            view.showMessage("Đăng nhập thành công!");
            view.setVisible(false);
            new TaskController(new TaskView()).runTaskProgram();
        } else {
            view.showMessage("Sai tài khoản hoặc mật khẩu!");
        }
    }

    private void xyLyDangKy() {
        String u = view.getRegUser();
        String e = view.getRegEmail();
        String p = view.getRegPass();
        String c = view.getRegConfirm();

        if (u.isEmpty() || e.isEmpty() || p.isEmpty()) {
            view.showMessage("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!isValidEmail(e)) {
            view.showMessage("Email không hợp lệ! (Ví dụ: ten@gmail.com)");
            return;
        }

        if (!p.equals(c)) {
            view.showMessage("Mật khẩu xác nhận không trùng khớp!");
            return;
        }

        boolean success = userManager.register(u, e, p);
        if (success) {
            view.showMessage("Đăng ký thành công! Hãy đăng nhập.");
            view.clearFields();
            view.showLoginScreen();
        } else {
            view.showMessage("Tên đăng nhập hoặc Email đã tồn tại!");
        }
    }

    private boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(regex, email);
    }
}