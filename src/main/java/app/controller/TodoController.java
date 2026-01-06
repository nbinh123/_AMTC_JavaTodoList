package app.controller;

import java.time.LocalDate;

import app.service.TodoService;
import app.session.UserSession;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class TodoController {

    private final TodoService service = new TodoService();

    public TodoController(TextField input, Button addBtn, DatePicker datePicker) {

        addBtn.setOnAction(e -> {
            String text = input.getText().trim();
            if (text.isEmpty()) return;

            var user = UserSession.getInstance().getUser();
            if (user == null) return;

            LocalDate date = datePicker.getValue();
            if (date == null) date = LocalDate.now();

            service.addTask(text, date);
            input.clear();
        });
    }
}
