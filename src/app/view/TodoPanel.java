package app.view;

import app.service.TodoService;
import app.model.TodoItem;

import javax.swing.*;
import java.awt.*;

public class TodoPanel extends JPanel {

    private final TodoService service = new TodoService();
    private final JPanel listPanel = new JPanel();

    public TodoPanel() {

        setLayout(new BorderLayout());

        /// ===== First Row =====
        /// 
        JPanel firstRow = new JPanel(new BorderLayout());
        JLabel heading = new JLabel("Add Task for good day: ");
        firstRow.add(heading, BorderLayout.CENTER);

        /// ===== Input Row =====
        JPanel inputRow = new JPanel(new BorderLayout());
        JTextField input = new JTextField();
        JButton addBtn = new JButton("Them");
        // JButton addBtn2 = new JButton("Add");

        input.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        inputRow.add(input, BorderLayout.CENTER);
        inputRow.add(addBtn, BorderLayout.EAST);
        // inputRow.add(addBtn2, BorderLayout.WEST);

        /// ⭐ đặt inputRow lên NORTH
        // add(firstRow, BorderLayout.NORTH);
        add(inputRow, BorderLayout.NORTH);

        /// ===== List panel =====
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        JScrollPane scroll = new JScrollPane(listPanel);
        add(scroll, BorderLayout.CENTER);

        /// ===== Event add =====
        addBtn.addActionListener(e -> {
            service.addTask(input.getText());
            input.setText("");
            renderList();
        });

        renderList();
    }

    /// ===== Render list =====
    private void renderList() {
        listPanel.removeAll();

        for (TodoItem item : service.getAll()) {
            listPanel.add(createRow(item));
        }

        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createRow(TodoItem item) {
        JPanel row = new JPanel(new BorderLayout());
        JCheckBox cb = new JCheckBox(item.getTitle(), item.isCompleted());
        JButton delete = new JButton("X");

        row.add(cb, BorderLayout.CENTER);
        row.add(delete, BorderLayout.EAST);

        cb.addActionListener(e -> service.toggleCompleted(item));
        delete.addActionListener(e -> {
            service.removeTask(item);
            renderList();
        });

        return row;
    }
}
