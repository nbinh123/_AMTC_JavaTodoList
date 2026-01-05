package app.utils;

import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CustomButton extends Button {

    // ==== DEFAULTS ====
    public static final double DEFAULT_RADIUS = 8;
    public static final double DEFAULT_PADDING = 10;

    public static final Color DEFAULT_BG = Color.web("#3f51b5");
    public static final Color DEFAULT_TEXT = Color.WHITE;

    private final Color baseBg;
    private final double radius;
    private final double padding;

    public CustomButton(
            String text,
            double radius,
            Color bgColor,
            Color textColor,
            double padding
    ) {
        super(text);

        this.baseBg = bgColor;
        this.radius = radius;
        this.padding = padding;

        setTextFill(textColor);
        setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        setCursor(Cursor.HAND);
        setFocusTraversable(false);

        applyBaseStyle();
        applyMaterialEffect();
        registerStates();
    }

    // ==== Overloads ====
    public CustomButton(String text) {
        this(text, DEFAULT_RADIUS, DEFAULT_BG, DEFAULT_TEXT, DEFAULT_PADDING);
    }

    public CustomButton(String text, Color bg) {
        this(text, DEFAULT_RADIUS, bg, DEFAULT_TEXT, DEFAULT_PADDING);
    }

    // ==== Styling ====
    private void applyBaseStyle() {
        setStyle(
            "-fx-background-color: " + toCss(baseBg) + ";" +
            "-fx-background-radius: " + radius + ";" +
            "-fx-padding: " + padding + " " + (padding * 2) + ";" +
            "-fx-text-fill: " + toCss(DEFAULT_TEXT) + ";"
        );
    }

    private void applyMaterialEffect() {
        DropShadow shadow = new DropShadow();
        shadow.setRadius(8);
        shadow.setOffsetY(2);
        shadow.setColor(Color.rgb(0, 0, 0, 0.35));
        setEffect(shadow);
    }

    // ==== States (hover / pressed / disabled) ====
    private void registerStates() {

        setOnMouseEntered(e ->
            setStyle(buildStyle(baseBg.brighter()))
        );

        setOnMouseExited(e ->
            setStyle(buildStyle(baseBg))
        );

        setOnMousePressed(e ->
            setStyle(buildStyle(baseBg.darker()))
        );

        disabledProperty().addListener((obs, o, disabled) -> {
            if (disabled) {
                setStyle(buildStyle(Color.GRAY));
                setOpacity(0.6);
            } else {
                setOpacity(1);
                setStyle(buildStyle(baseBg));
            }
        });
    }

    private String buildStyle(Color bg) {
        return
            "-fx-background-color: " + toCss(bg) + ";" +
            "-fx-background-radius: " + radius + ";" +
            "-fx-padding: " + padding + " " + (padding * 2) + ";" +
            "-fx-text-fill: " + toCss(DEFAULT_TEXT) + ";";
    }

    // ==== Utils ====
    private String toCss(Color c) {
        return String.format(
            "rgba(%d,%d,%d,%.2f)",
            (int) (c.getRed() * 255),
            (int) (c.getGreen() * 255),
            (int) (c.getBlue() * 255),
            c.getOpacity()
        );
    }
}
