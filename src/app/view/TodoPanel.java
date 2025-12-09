package app.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import app.model.TodoItem;
import app.service.TodoService;
import app.ultis.CustomJButton;

public class TodoPanel extends JPanel {

    private final TodoService service = new TodoService();
    private final JPanel listPanel = new JPanel();

    public TodoPanel() {

        setLayout(new BorderLayout());

        /// ===== First Row =====
        ///
        /// ===== TOP container =====
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        /// first row
        JLabel heading = new JLabel("Thêm công việc cho hôm nay nào: ");
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        heading.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0));
        

        /// input row
        JPanel inputRow = new JPanel(new BorderLayout());
        inputRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField input = new JTextField();
        JButton addBtn = new JButton("Thêm");
        // addBtn.setFocusPainted(false);
        input.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        inputRow.add(input, BorderLayout.CENTER);
        inputRow.add(addBtn, BorderLayout.EAST);

        inputRow.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        /// put BOTH into top
        top.add(heading);
        top.add(Box.createVerticalStrut(5)); // margin
        top.add(inputRow);

        add(top, BorderLayout.NORTH);

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
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); // height 40px
        
        JCheckBox cb = new JCheckBox(item.getTitle(), item.isCompleted());
        JButton delete = new JButton("X");

        row.add(cb, BorderLayout.CENTER);
        row.add(delete, BorderLayout.EAST);
        row.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        cb.addActionListener(e -> service.toggleCompleted(item));
        delete.addActionListener(e -> {
            service.removeTask(item);
            renderList();
        });

        return row;
    }
}
