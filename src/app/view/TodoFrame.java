package app.view;

import javax.swing.*;

public class TodoFrame extends JFrame {

    public TodoFrame() {
        setTitle("Todo App");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(new TodoPanel());
         // padding 
        // getRootPane().setBorder(
        //     BorderFactory.createEmptyBorder(0, 0, 0, 0)
        // );

        setVisible(true);
    }
}
