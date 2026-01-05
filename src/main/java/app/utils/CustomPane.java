package app.utils;

import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class CustomPane extends StackPane {

    // ==== DEFAULTS ====
    public static final double DEFAULT_RADIUS = 8;
    public static final double DEFAULT_PADDING = 12;
    public static final double DEFAULT_MARGIN = 8;

    public static final Color DEFAULT_BG = Color.rgb(45, 45, 45);

    private double radius;
    private Color bg;

    public CustomPane(
            double radius,
            Color bgColor,
            double padding,
            double margin
    ) {
        this.radius = radius;
        this.bg = bgColor;

        // padding (internal)
        setPadding(new Insets(padding));

        // background + radius
        applyStyle();

        // margin (external) → set khi add vào parent
        StackPane.setMargin(this, new Insets(margin));
    }

    // ==== overload default ====
    public CustomPane() {
        this(DEFAULT_RADIUS, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_MARGIN);
    }

    public CustomPane(double radius) {
        this(radius, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_MARGIN);
    }

    // ==== apply style ====
    private void applyStyle() {
        setStyle(String.format(
                "-fx-background-color: %s;" +
                "-fx-background-radius: %.1f;",
                toCss(bg),
                radius
        ));
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
