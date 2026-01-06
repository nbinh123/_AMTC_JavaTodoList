package app;

import app.session.UserSession;
import app.view.AuthPanel;  
import app.view.TodoPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Scene scene;

        if (UserSession.getInstance().isLoggedIn()) {
            scene = new Scene(new TodoPanel(), 1000, 500);
            stage.setTitle("Material Todo App");
        } else {

            scene = new Scene(new AuthPanel(stage), 500, 700);
            stage.setTitle("Todo App - Đăng nhập");  
        }

        scene.getStylesheets().add(
            getClass().getResource("/material.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.centerOnScreen();  
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}