package app.controller;

import app.service.TodoService;
import javax.swing.*;

public class TodoController {

    private TodoService service;

    public TodoController(JTextField txt, JButton btn) {
        service = new TodoService();

        btn.addActionListener(e -> {
            service.addTask(txt.getText());
            service.save();
            txt.setText("");
        });
    }
}
