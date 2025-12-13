package app;

import java.awt.Color;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.json.JSONObject;

import app.view.TodoFrame;

public class Main {
    public static void main(String[] args) {

        JSONObject obj = new JSONObject();
        obj.put("hello", "world");
        System.out.println(obj);


        // chạy UI đúng thread
        SwingUtilities.invokeLater(() -> {
            setupDarkTheme();

            // mở cửa sổ chính
            new TodoFrame().setVisible(true);
        });
    }

    private static void setupDarkTheme() {

        UIManager.put("Panel.background", new Color(25,25,25));

        UIManager.put("Button.background", new Color(50,50,50));
        UIManager.put("Button.foreground", Color.WHITE);

        UIManager.put("Label.foreground", Color.WHITE);

        UIManager.put("TextField.background", new Color(40,40,40));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.caretForeground", Color.WHITE);

        UIManager.put("CheckBox.background", new Color(25,25,25));
        UIManager.put("CheckBox.foreground", Color.WHITE);

        UIManager.put("List.background", new Color(30,30,30));
        UIManager.put("List.foreground", Color.WHITE);

        UIManager.put("ScrollPane.background", new Color(30,30,30));
    }
}
