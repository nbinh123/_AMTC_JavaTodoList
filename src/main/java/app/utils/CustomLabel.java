package app.utils;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class CustomLabel extends Label {

    // ==== DEFAULTS ====
    public static final double DEFAULT_PADDING = 4;
    public static final double DEFAULT_RADIUS = 0;
    public static final Color DEFAULT_TEXT = Color.WHITE;
    public static final Color DEFAULT_BG = null; // null = transparent

    private Color bg;
    private double radius;
    private double padding;

    public CustomLabel(
            String text,
            double fontSize,
            Color textColor,
            Color bgColor,
            double padding,
            double radius
    ) {
        super(text);

        this.bg = bgColor;
        this.radius = radius;
        this.padding = padding;

        // text
        setTextFill(textColor);
        setFont(Font.font("Segoe UI", fontSize));

        // cursor pointer
        setCursor(Cursor.HAND);

        applyStyle();
    }

    // ==== overloads ====
    public CustomLabel(String text) {
        this(text, 14, DEFAULT_TEXT, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_RADIUS);
    }

    public CustomLabel(String text, double fontSize) {
        this(text, fontSize, DEFAULT_TEXT, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_RADIUS);
    }

    // ==== apply style ====
    private void applyStyle() {
        StringBuilder style = new StringBuilder();

        // padding
        style.append(String.format(
                "-fx-padding: %.1f;", padding
        ));

        // background
        if (bg != null) {
            style.append(String.format(
                    "-fx-background-color: %s;" +
                    "-fx-background-radius: %.1f;",
                    toCss(bg),
                    radius
            ));
        } else {
            style.append("-fx-background-color: transparent;");
        }

        setStyle(style.toString());
    }

    private String toCss(Color c) {
        return String.format("rgba(%d,%d,%d,%.2f)",
                (int) (c.getRed() * 255),
                (int) (c.getGreen() * 255),
                (int) (c.getBlue() * 255),
                c.getOpacity()
        );
    }
}
