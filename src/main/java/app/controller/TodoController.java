package app.controller;

import javax.swing.*;

import app.service.TodoService;

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
