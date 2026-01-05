package app.view;

import app.session.UserSession;
import app.model.User;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPanel extends VBox {

    private final Stage stage;

    public LoginPanel(Stage stage) {
        this.stage = stage;
        buildUI();
    }

    private void buildUI() {
        TextField username = new TextField();
        PasswordField password = new PasswordField();
        Button loginBtn = new Button("Login");

        loginBtn.setOnAction(e -> {
            User user = authenticate(username.getText(), password.getText());

            if (user != null) {
                UserSession.getInstance().login(user);

                // 🔁 CHUYỂN SANG TODO PANEL
                Scene todoScene = new Scene(new TodoPanel(), 1000, 500);
                todoScene.getStylesheets().add(
                    getClass().getResource("/material.css").toExternalForm()
                );

                stage.setScene(todoScene);
                stage.setTitle("Material Todo App");
            } else {
                new Alert(Alert.AlertType.ERROR, "Sai tài khoản hoặc mật khẩu").show();
            }
        });

        getChildren().addAll(username, password, loginBtn);
    }

    private User authenticate(String u, String p) {
        // TODO: gọi UserService / MongoDB
        return null;
    }
}
