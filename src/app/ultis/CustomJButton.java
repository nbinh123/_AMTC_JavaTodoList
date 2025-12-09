package app.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class CustomJButton extends JButton {

    // ==== DEFAULTS ====
    public static final int DEFAULT_RADIUS = 12;
    public static final int DEFAULT_PADDING = 8;

    public static final Color DEFAULT_BG = new Color(50, 50, 50);
    public static final Color DEFAULT_TEXT = Color.WHITE;

    // === real constructor ===
    public CustomJButton(
        String text,
        int radius,
        Color bgColor,
        Color textColor,
        int padding
    ) {
        super(text);

        setForeground(textColor);
        setBackground(bgColor);

        setFocusPainted(false);
        setBorderPainted(false);

        // padding
        setBorder(BorderFactory.createEmptyBorder(padding, padding * 2, padding, padding * 2));

        setContentAreaFilled(false);
        setOpaque(false);

        // pointer cursor
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(bgColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(bgColor);
            }
        });
    }

    // === overload with DEFAULTS ===
    public CustomJButton(String text) {
        this(text, DEFAULT_RADIUS, DEFAULT_BG, DEFAULT_TEXT, DEFAULT_PADDING);
    }

    public CustomJButton(String text, int radius) {
        this(text, radius, DEFAULT_BG, DEFAULT_TEXT, DEFAULT_PADDING);
    }

    // === override paint ===
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), DEFAULT_RADIUS, DEFAULT_RADIUS);

        super.paintComponent(g);
        g2.dispose();
    }
}
