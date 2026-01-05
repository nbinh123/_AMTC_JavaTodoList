package app.utils;

import javafx.scene.control.CheckBox;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class MaterialCheckBox extends CheckBox {

    public MaterialCheckBox(String text) {
        super("  " + text);

        // Style cơ bản
        setStyle(
            "-fx-font-size: 18px; " +
            "-fx-text-fill: white;"
        );

        setEffect(new DropShadow(2, Color.rgb(0,0,0,0.25)));
        
        // ✅ Thêm CSS inline cho tick mark
        String css = 
            ".check-box .box { " +
            "   -fx-background-color: #2A2A2A; " +
            "   -fx-border-color: #444444; " +
            "   -fx-border-radius: 4px; " +
            "   -fx-background-radius: 4px; " +
            "} " +
            ".check-box:selected .mark { " +
            "   -fx-background-color: #ffffffff; " +
            "} " +
            ".check-box:selected .box { " +
            "   -fx-background-color: #667eea; " +
            "   -fx-border-color: #667eea; " +
            "}";
        
        getStylesheets().add("data:text/css," + css);
    }
}