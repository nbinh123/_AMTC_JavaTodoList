package app.view;

import app.model.User;
import app.service.TodoService;
import app.session.UserSession;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AuthPanel extends StackPane {

    private final Stage stage;
    private VBox loginBox;
    private VBox registerBox;
    private boolean isLoginMode = true;

    public AuthPanel(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: linear-gradient(135deg, #667eea 0%, #764ba2 100%);");

        // ===== CONTAINER CHÍNH =====
        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(450);
        container.setStyle(
            "-fx-background-color: rgba(255, 255, 255, 0.95); " +
            "-fx-background-radius: 25; " +
            "-fx-padding: 50 40; " +
            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 30, 0, 0, 10);"
        );

        // ===== LOGO & TITLE =====
        Text logo = new Text("✓");
        logo.setStyle(
            "-fx-font-size: 60px; " +
            "-fx-fill: linear-gradient(to right, #667eea, #764ba2); " +
            "-fx-font-weight: bold;"
        );

        Text appName = new Text("Todo App");
        appName.setStyle(
            "-fx-font-size: 32px; " +
            "-fx-fill: #2C3E50; " +
            "-fx-font-weight: bold;"
        );

        Text subtitle = new Text("Quản lý công việc hiệu quả");
        subtitle.setStyle(
            "-fx-font-size: 14px; " +
            "-fx-fill: #7F8C8D; " +
            "-fx-font-style: italic;"
        );

        VBox header = new VBox(10, logo, appName, subtitle);
        header.setAlignment(Pos.CENTER);

        // ===== LOGIN FORM =====
        loginBox = createLoginForm();
        
        // ===== REGISTER FORM =====
        registerBox = createRegisterForm();
        registerBox.setVisible(false);
        registerBox.setManaged(false);

        // ===== STACK CẢ 2 FORM =====
        StackPane formStack = new StackPane(loginBox, registerBox);

        container.getChildren().addAll(header, formStack);

        // ===== THÊM VÀO STAGE =====
        StackPane root = new StackPane(container);
        root.setPadding(new Insets(40));
        getChildren().add(root);

        // ===== ANIMATION KHI MỞ =====
        ScaleTransition st = new ScaleTransition(Duration.millis(400), container);
        st.setFromX(0.8);
        st.setFromY(0.8);
        st.setToX(1.0);
        st.setToY(1.0);
        
        FadeTransition ft = new FadeTransition(Duration.millis(400), container);
        ft.setFromValue(0);
        ft.setToValue(1);
        
        ParallelTransition pt = new ParallelTransition(st, ft);
        pt.play();
    }

    private VBox createLoginForm() {
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);

        // ===== USERNAME =====
        Label userLabel = new Label("Tên đăng nhập");
        userLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        
        TextField username = new TextField();
        username.setPromptText("Nhập tên đăng nhập");
        styleTextField(username);

        VBox userBox = new VBox(8, userLabel, username);

        // ===== PASSWORD =====
        Label passLabel = new Label("Mật khẩu");
        passLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        
        PasswordField password = new PasswordField();
        password.setPromptText("Nhập mật khẩu");
        styleTextField(password);

        VBox passBox = new VBox(8, passLabel, password);

        // ===== REMEMBER & FORGOT =====
        CheckBox remember = new CheckBox("Ghi nhớ đăng nhập");
        remember.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 12px;");

        Hyperlink forgot = new Hyperlink("Quên mật khẩu?");
        forgot.setStyle(
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 12px; " +
            "-fx-border-width: 0;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox options = new HBox(remember, spacer, forgot);
        options.setAlignment(Pos.CENTER_LEFT);

        // ===== LOGIN BUTTON =====
        Button loginBtn = new Button("Đăng nhập");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(loginBtn, true);

        loginBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();

            if (user.isEmpty() || pass.isEmpty()) {
                showError("Vui lòng điền đầy đủ thông tin!");
                return;
            }

            User authenticatedUser = authenticate(user, pass);

            if (authenticatedUser != null) {
                UserSession.getInstance().login(authenticatedUser);

                // Chuyển sang TodoPanel
                Scene todoScene = new Scene(new TodoPanel(), 1000, 500);
                todoScene.getStylesheets().add(
                    getClass().getResource("/material.css").toExternalForm()
                );

                stage.setScene(todoScene);
                stage.setTitle("Material Todo App");
            } else {
                showError("Sai tên đăng nhập hoặc mật khẩu!");
            }
        });

        // ===== SWITCH TO REGISTER =====
        HBox switchBox = createSwitchBox(
            "Chưa có tài khoản?",
            "Đăng ký ngay",
            () -> switchToRegister()
        );

        form.getChildren().addAll(userBox, passBox, options, loginBtn, switchBox);
        return form;
    }

    private VBox createRegisterForm() {
        VBox form = new VBox(20);
        form.setAlignment(Pos.CENTER);

        // ===== USERNAME =====
        Label userLabel = new Label("Tên đăng nhập");
        userLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        
        TextField username = new TextField();
        username.setPromptText("Chọn tên đăng nhập");
        styleTextField(username);

        VBox userBox = new VBox(8, userLabel, username);

        // ===== EMAIL =====
        Label emailLabel = new Label("Email");
        emailLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        
        TextField email = new TextField();
        email.setPromptText("Nhập email của bạn");
        styleTextField(email);

        VBox emailBox = new VBox(8, emailLabel, email);

        // ===== PASSWORD =====
        Label passLabel = new Label("Mật khẩu");
        passLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        
        PasswordField password = new PasswordField();
        password.setPromptText("Tạo mật khẩu");
        styleTextField(password);

        VBox passBox = new VBox(8, passLabel, password);

        // ===== CONFIRM PASSWORD =====
        Label confirmLabel = new Label("Xác nhận mật khẩu");
        confirmLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #2C3E50; -fx-font-weight: bold;");
        
        PasswordField confirmPass = new PasswordField();
        confirmPass.setPromptText("Nhập lại mật khẩu");
        styleTextField(confirmPass);

        VBox confirmBox = new VBox(8, confirmLabel, confirmPass);

        // ===== REGISTER BUTTON =====
        Button registerBtn = new Button("Tạo tài khoản");
        registerBtn.setMaxWidth(Double.MAX_VALUE);
        styleButton(registerBtn, true);

        registerBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String mail = email.getText().trim();
            String pass = password.getText().trim();
            String confirm = confirmPass.getText().trim();

            if (user.isEmpty() || mail.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
                showError("Vui lòng điền đầy đủ thông tin!");
                return;
            }

            if (!pass.equals(confirm)) {
                showError("Mật khẩu xác nhận không khớp!");
                return;
            }

            if (pass.length() < 6) {
                showError("Mật khẩu phải có ít nhất 6 ký tự!");
                return;
            }

            boolean success = register(user, mail, pass);

            if (success) {
                showSuccess("Đăng ký thành công! Vui lòng đăng nhập.");
                switchToLogin();
            } else {
                showError("Tên đăng nhập đã tồn tại!");
            }
        });

        // ===== SWITCH TO LOGIN =====
        HBox switchBox = createSwitchBox(
            "Đã có tài khoản?",
            "Đăng nhập",
            () -> switchToLogin()
        );

        form.getChildren().addAll(userBox, emailBox, passBox, confirmBox, registerBtn, switchBox);
        return form;
    }

    private HBox createSwitchBox(String text, String linkText, Runnable action) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #7F8C8D; -fx-font-size: 13px;");

        Hyperlink link = new Hyperlink(linkText);
        link.setStyle(
            "-fx-text-fill: #667eea; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-border-width: 0; " +
            "-fx-padding: 0 0 0 5;"
        );

        link.setOnAction(e -> action.run());

        HBox box = new HBox(5, label, link);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private void switchToRegister() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), loginBox);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            loginBox.setVisible(false);
            loginBox.setManaged(false);
            
            registerBox.setVisible(true);
            registerBox.setManaged(true);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), registerBox);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
        
        isLoginMode = false;
    }

    private void switchToLogin() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), registerBox);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            registerBox.setVisible(false);
            registerBox.setManaged(false);
            
            loginBox.setVisible(true);
            loginBox.setManaged(true);
            
            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), loginBox);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
        
        isLoginMode = true;
    }

    private void styleTextField(Control field) {
        field.setStyle(
            "-fx-background-color: #F8F9FA; " +
            "-fx-border-color: #E0E0E0; " +
            "-fx-border-width: 2; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-padding: 12 15; " +
            "-fx-font-size: 14px; " +
            "-fx-text-fill: #2C3E50; " +
            "-fx-prompt-text-fill: #95A5A6;"
        );

        field.focusedProperty().addListener((obs, old, focused) -> {
            if (focused) {
                field.setStyle(
                    "-fx-background-color: #FFFFFF; " +
                    "-fx-border-color: #667eea; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-padding: 12 15; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #2C3E50; " +
                    "-fx-prompt-text-fill: #95A5A6; " +
                    "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.3), 10, 0, 0, 0);"
                );
            } else {
                field.setStyle(
                    "-fx-background-color: #F8F9FA; " +
                    "-fx-border-color: #E0E0E0; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-padding: 12 15; " +
                    "-fx-font-size: 14px; " +
                    "-fx-text-fill: #2C3E50; " +
                    "-fx-prompt-text-fill: #95A5A6;"
                );
            }
        });
    }

    private void styleButton(Button btn, boolean isPrimary) {
        if (isPrimary) {
            btn.setStyle(
                "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 15px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-padding: 14 0; " +
                "-fx-cursor: hand; " +
                "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 15, 0, 0, 5);"
            );

            btn.setOnMouseEntered(e -> {
                btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #7b91ff, #8a5bb8); " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 15px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-radius: 10; " +
                    "-fx-padding: 14 0; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.6), 20, 0, 0, 7);"
                );
                
                ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
                st.setToX(1.02);
                st.setToY(1.02);
                st.play();
            });

            btn.setOnMouseExited(e -> {
                btn.setStyle(
                    "-fx-background-color: linear-gradient(to right, #667eea, #764ba2); " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 15px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-background-radius: 10; " +
                    "-fx-padding: 14 0; " +
                    "-fx-cursor: hand; " +
                    "-fx-effect: dropshadow(gaussian, rgba(102, 126, 234, 0.4), 15, 0, 0, 5);"
                );
                
                ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
                st.setToX(1.0);
                st.setToY(1.0);
                st.play();
            });
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private User authenticate(String username, String password) {
        TodoService service = new TodoService();
        return service.authenticate(username, password); 
    }


    private boolean register(String username, String email, String password) {
        // ✅ Gọi TodoService để đăng ký vào MongoDB
        TodoService service = new TodoService();
        return service.registerUser(username, email, password);
    }
}