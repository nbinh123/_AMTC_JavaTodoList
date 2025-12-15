package app.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TodoFrame extends Application {

    @Override
    public void start(Stage stage) {

        TodoPanel root = new TodoPanel();

        Scene scene = new Scene(root, 1000, 500);

        stage.setTitle("Todo App");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
