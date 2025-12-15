package app.view.FX;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TodoApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Test JSON (giống code cũ của bạn)
        JSONObject obj = new JSONObject();
        obj.put("hello", "world");
        System.out.println(obj);

        primaryStage.setTitle("Todo List App");

        // Main layout
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");
        root.setPadding(new Insets(20));

        // Header
        Label title = new Label("📝 My Todo List");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        VBox header = new VBox(title);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        // Input area
        TextField taskInput = new TextField();
        taskInput.setPromptText("Enter a new task...");
        taskInput.setPrefHeight(45);
        taskInput.setStyle(
            "-fx-background-color: #2a2a2a; " +
            "-fx-text-fill: white; " +
            "-fx-prompt-text-fill: #888; " +
            "-fx-border-color: #444; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px; " +
            "-fx-padding: 10px; " +
            "-fx-font-size: 14px;"
        );

        Button addButton = new Button("Add Task");
        addButton.setPrefHeight(45);
        addButton.setPrefWidth(120);
        addButton.setStyle(
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand;"
        );
        
        // Hover effect
        addButton.setOnMouseEntered(e -> 
            addButton.setStyle(
                "-fx-background-color: #45a049; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );
        addButton.setOnMouseExited(e -> 
            addButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5px; " +
                "-fx-cursor: hand;"
            )
        );

        HBox inputBox = new HBox(10, taskInput, addButton);
        HBox.setHgrow(taskInput, Priority.ALWAYS);

        // Task list with CheckBoxes
        ListView<HBox> taskListView = new ListView<>();
        taskListView.setStyle(
            "-fx-background-color: #2a2a2a; " +
            "-fx-border-color: #444; " +
            "-fx-border-radius: 5px; " +
            "-fx-background-radius: 5px;"
        );
        taskListView.setPlaceholder(new Label("No tasks yet. Add one above!") {{
            setStyle("-fx-text-fill: #888;");
        }});

        // Add task action
        addButton.setOnAction(e -> addTask(taskInput, taskListView));
        taskInput.setOnAction(e -> addButton.fire());

        // Delete button
        Button deleteButton = new Button("🗑️ Delete Selected");
        deleteButton.setStyle(
            "-fx-background-color: #f44336; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand;"
        );
        deleteButton.setOnAction(e -> {
            int selectedIndex = taskListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                taskListView.getItems().remove(selectedIndex);
            }
        });

        Button clearAllButton = new Button("Clear All");
        clearAllButton.setStyle(
            "-fx-background-color: #555; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-background-radius: 5px; " +
            "-fx-cursor: hand;"
        );
        clearAllButton.setOnAction(e -> taskListView.getItems().clear());

        HBox bottomBox = new HBox(10, deleteButton, clearAllButton);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));

        // Combine everything
        VBox center = new VBox(15, inputBox, taskListView, bottomBox);
        VBox.setVgrow(taskListView, Priority.ALWAYS);

        root.setTop(header);
        root.setCenter(center);

        // Scene
        Scene scene = new Scene(root, 650, 550);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void addTask(TextField taskInput, ListView<HBox> taskListView) {
        String task = taskInput.getText().trim();
        if (!task.isEmpty()) {
            CheckBox checkBox = new CheckBox(task);
            checkBox.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
            
            // Strike-through khi checked
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    checkBox.setStyle("-fx-text-fill: #888; -fx-font-size: 14px;");
                    checkBox.setText("✓ " + task);
                } else {
                    checkBox.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                    checkBox.setText(task);
                }
            });
            
            // Delete button for each task
            Button deleteBtn = new Button("×");
            deleteBtn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #f44336; " +
                "-fx-font-size: 20px; " +
                "-fx-font-weight: bold; " +
                "-fx-cursor: hand;"
            );
            deleteBtn.setOnAction(e -> taskListView.getItems().remove(
                taskListView.getItems().stream()
                    .filter(hbox -> hbox.getChildren().contains(deleteBtn))
                    .findFirst()
                    .orElse(null)
            ));
            
            HBox taskBox = new HBox(10, checkBox, deleteBtn);
            taskBox.setAlignment(Pos.CENTER_LEFT);
            taskBox.setStyle("-fx-padding: 5px; -fx-background-color: #333; -fx-background-radius: 3px;");
            HBox.setHgrow(checkBox, Priority.ALWAYS);
            
            taskListView.getItems().add(taskBox);
            taskInput.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}