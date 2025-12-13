package app.ulti;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

public class CustomJCheckBox extends JCheckBox {

    public static final int DEFAULT_RADIUS = 4;
    public static final int DEFAULT_PADDING = 4;

    private int radius;
    private boolean uppercase;
    private Color hoverColor;
    private Color normalText;

    private boolean hoverEnabled;
    private boolean shadow;

    public CustomJCheckBox(
            String text,
            int radius,
            int fontSize,
            Color textColor,
            boolean bold,
            boolean italic,
            boolean uppercase,
            boolean hover,
            Color hoverColor,
            boolean shadow
    ) {
        super(text);

        this.radius = radius;
        this.uppercase = uppercase;
        this.hoverColor = hoverColor;
        this.normalText = textColor;
        this.hoverEnabled = hover;
        this.shadow = shadow;

        // remove ugly focus border
        setFocusPainted(false);

        // padding
        setBorder(BorderFactory.createEmptyBorder(
                DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING, DEFAULT_PADDING));

        // text
        setForeground(textColor);
        if (uppercase) setText(text.toUpperCase());

        // font style
        int style = Font.PLAIN;
        if (bold) style |= Font.BOLD;
        if (italic) style |= Font.ITALIC;

        setFont(new Font("Segoe UI", style, fontSize));

        // smooth rollover
        if (hover) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setForeground(hoverColor != null ? hoverColor : textColor.brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setForeground(normalText);
                }
            });
        }
    }

    // default
    public CustomJCheckBox(String text) {
        this(
                text,
                DEFAULT_RADIUS,
                14,
                Color.WHITE,
                false,
                false,
                false,
                false,
                null,
                false
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isSelected()) return;

        if (shadow) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 50));
            g2.fillRoundRect(1, getHeight() / 2 - 6, 12, 12, radius, radius);
            g2.dispose();
        }
    }
}
