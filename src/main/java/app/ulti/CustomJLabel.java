package app.ulti;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class CustomJLabel extends JLabel {

    public static final int DEFAULT_PADDING = 4;
    public static final int DEFAULT_RADIUS = 0;
    public static final Color DEFAULT_TEXT = Color.WHITE;
    public static final Color DEFAULT_BG = null; // null = transparent

    private Color bg = DEFAULT_BG;
    private int radius = DEFAULT_RADIUS;

    public CustomJLabel(
            String text,
            int fontSize,
            Color textColor,
            Color bgColor,
            int padding,
            int radius
    ) {
        super(text);

        // text color
        setForeground(textColor);

        // font
        setFont(new Font("Segoe UI", Font.PLAIN, fontSize));

        // bg
        this.bg = bgColor;
        this.radius = radius;
        setOpaque(bgColor != null);

        // padding
        setBorder(BorderFactory.createEmptyBorder(
                padding, padding, padding, padding));

        // cursor pointer if clickable.
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // overload default
    public CustomJLabel(String text) {
        this(text, 14, DEFAULT_TEXT, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_RADIUS);
    }

    public CustomJLabel(String text, int fontSize) {
        this(text, fontSize, DEFAULT_TEXT, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_RADIUS);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bg != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
