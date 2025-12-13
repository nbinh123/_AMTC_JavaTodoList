package app.ulti;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class CustomJPanel extends JPanel {

    public static final int DEFAULT_RADIUS = 8;
    public static final int DEFAULT_PADDING = 12;
    public static final int DEFAULT_MARGIN = 8;

    public static final Color  DEFAULT_BG = new Color(45, 45, 45);

    private int radius;
    private Color bg;

    public CustomJPanel(
            int radius,
            Color bgColor,
            int padding,
            int margin
    ) {
        this.radius = radius;
        this.bg = bgColor;

        setOpaque(false);

        // padding
        setBorder(
            BorderFactory.createEmptyBorder(
                padding,
                padding,
                padding,
                padding
            )
        );

        // margin external (wrap)
        setLayout(new BorderLayout());
    }

    // === overload default ===
    public CustomJPanel() {
        this(DEFAULT_RADIUS, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_MARGIN);
    }

    public CustomJPanel(int radius) {
        this(radius, DEFAULT_BG, DEFAULT_PADDING, DEFAULT_MARGIN);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g.create();
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}
