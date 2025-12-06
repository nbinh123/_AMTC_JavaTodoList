import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Todo List App - Swing");
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel label = new JLabel("Hello from Swing!", JLabel.CENTER);

            frame.add(label);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
