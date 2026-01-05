package app;

import app.session.UserSession;
import app.view.LoginPanel;
import app.view.TodoPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Scene scene;

        // ✅ CHECK LOGIN NGAY TỪ ĐẦU
        if (UserSession.getInstance().isLoggedIn()) {
            scene = new Scene(new TodoPanel(), 1000, 500);
            stage.setTitle("Material Todo App");
        } else {
            scene = new Scene(new LoginPanel(stage), 400, 300);
            stage.setTitle("Login");
        }

        scene.getStylesheets().add(
            getClass().getResource("/material.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

