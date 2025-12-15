package app;

import app.view.TodoPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

    TodoPanel root = new TodoPanel();
    Scene scene = new Scene(root, 1000, 500);

    scene.getStylesheets().add(
        getClass().getResource("/material.css").toExternalForm()
    );

    stage.setTitle("Material Todo App");
    stage.setScene(scene);
    stage.show();
}

    public static void main(String[] args) {
        launch(args);
    }
}
