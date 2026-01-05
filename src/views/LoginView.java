package views;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginView extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;

    // --- 1. CẤU HÌNH MÀU SẮC ---
    private final Color COLOR_BG_MAIN = Color.WHITE;
    private final Color COLOR_CONTAINER = new Color(248, 250, 252); // Xám cực nhạt (trẻ trung)
    private final Color COLOR_INPUT_BG = new Color(235, 237, 240);  // Nền ô nhập liệu
    private final Color COLOR_TEXT_PRIMARY = new Color(45, 52, 54);
    private final Color COLOR_ACCENT_1 = new Color(108, 92, 231);   // Tím
    private final Color COLOR_ACCENT_2 = new Color(162, 155, 254);  // Tím nhạt

    // --- COMPONENTS ---
    private JTextField txtLoginUser = createRoundedTextField();
    private JPasswordField txtLoginPass = createRoundedPasswordField();
    private JButton btnLogin = createGradientButton("SIGN IN");
    private JButton btnGoToRegister = createLinkButton("Don't have an account? Sign Up");

    private JTextField txtRegUser = createRoundedTextField();
    private JTextField txtRegEmail = createRoundedTextField();
    private JPasswordField txtRegPass = createRoundedPasswordField();
    private JPasswordField txtRegConfirm = createRoundedPasswordField();
    private JButton btnRegister = createGradientButton("SIGN UP");
    private JButton btnGoToLogin = createLinkButton("Already have an account? Sign In");

    public LoginView() {
        this.setTitle("Modern Clean App");
        this.setSize(950, 700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // Nền chính
        JPanel mainBackground = new JPanel();
        mainBackground.setBackground(COLOR_BG_MAIN);
        mainBackground.setLayout(new GridBagLayout());

        // Khung chứa form (Container)
        JPanel containerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(COLOR_CONTAINER);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40); // Bo góc mềm mại hơn
                g2d.dispose();
            }
        };
        containerPanel.setOpaque(false);
        containerPanel.setLayout(new BorderLayout());
        // Tăng chiều rộng khung chứa lên 450 để ô nhập liệu dài ra thoải mái
        containerPanel.setPreferredSize(new Dimension(450, 600)); 
        containerPanel.setBorder(new EmptyBorder(40, 50, 40, 50)); // Padding rộng rãi

        // CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        cardPanel.add(createLoginPanel(), "LOGIN_CARD");
        cardPanel.add(createRegisterPanel(), "REGISTER_CARD");

        containerPanel.add(cardPanel, BorderLayout.CENTER);
        mainBackground.add(containerPanel);

        this.add(mainBackground);
    }

    // --- PANEL ĐĂNG NHẬP ---
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(COLOR_TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Sign in to continue");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(Color.GRAY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Thêm khoảng cách
        panel.add(Box.createVerticalGlue()); // Đẩy nội dung vào giữa
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(50));

        addInputGroup(panel, "Username", txtLoginUser);
        addInputGroup(panel, "Password", txtLoginPass);
        
        panel.add(Box.createVerticalStrut(40));
        
        // Nút Sign In full chiều ngang
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnLogin);
        
        panel.add(Box.createVerticalStrut(20));
        
        // Nút Link ở dưới cùng
        btnGoToRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnGoToRegister);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // --- PANEL ĐĂNG KÝ ---
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitle.setForeground(COLOR_TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(30));

        addInputGroup(panel, "Username", txtRegUser);
        addInputGroup(panel, "Email", txtRegEmail);
        addInputGroup(panel, "Password", txtRegPass);
        addInputGroup(panel, "Confirm Password", txtRegConfirm);

        panel.add(Box.createVerticalStrut(30));
        
        // Nút Sign Up full chiều ngang
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        btnRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnRegister);
        
        panel.add(Box.createVerticalStrut(20));
        
        // Nút quay lại Login (Đồng bộ vị trí với trang Login)
        btnGoToLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(btnGoToLogin);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // --- HELPER METHODS ---

    private void addInputGroup(JPanel p, String labelText, JComponent field) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // BÍ KÍP KÉO DÀI Ô INPUT: 
        // 1. Set AlignmentX là LEFT_ALIGNMENT cho field
        // 2. Set MaxSize chiều ngang là MAX_VALUE cho cả panel chứa nó
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, BoxLayout.Y_AXIS));
        temp.setOpaque(false);
        temp.setAlignmentX(Component.CENTER_ALIGNMENT); // Panel con căn giữa panel cha
        // Quan trọng: Ép panel con giãn hết chiều ngang
        temp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
        
        temp.add(lbl);
        temp.add(Box.createVerticalStrut(8));
        temp.add(field);
        
        p.add(temp);
        p.add(Box.createVerticalStrut(15));
    }

    private JTextField createRoundedTextField() {
        JTextField txt = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        txt.setOpaque(false);
        txt.setBorder(new EmptyBorder(10, 15, 10, 15));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(COLOR_TEXT_PRIMARY);
        // Quan trọng: Ép chiều ngang tối đa
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45)); 
        return txt;
    }

    private JPasswordField createRoundedPasswordField() {
        JPasswordField txt = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COLOR_INPUT_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        txt.setOpaque(false);
        txt.setBorder(new EmptyBorder(10, 15, 10, 15));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setForeground(COLOR_TEXT_PRIMARY);
        // Quan trọng: Ép chiều ngang tối đa
        txt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return txt;
    }

    private JButton createGradientButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, COLOR_ACCENT_1, getWidth(), 0, COLOR_ACCENT_2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Bo góc nút ít hơn xíu cho cứng cáp
                
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createLinkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(COLOR_ACCENT_1);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setText("<html><u>" + text + "</u></html>"); }
            public void mouseExited(MouseEvent e) { btn.setText(text); }
        });
        return btn;
    }

    public void showRegisterScreen() { cardLayout.show(cardPanel, "REGISTER_CARD"); }
    public void showLoginScreen() { cardLayout.show(cardPanel, "LOGIN_CARD"); }
    public String getLoginUser() { return txtLoginUser.getText(); }
    public String getLoginPass() { return new String(txtLoginPass.getPassword()); }
    public String getRegUser() { return txtRegUser.getText(); }
    public String getRegEmail() { return txtRegEmail.getText(); }
    public String getRegPass() { return new String(txtRegPass.getPassword()); }
    public String getRegConfirm() { return new String(txtRegConfirm.getPassword()); }
    public void addLoginListener(ActionListener al) { btnLogin.addActionListener(al); }
    public void addRegisterListener(ActionListener al) { btnRegister.addActionListener(al); }
    public void addSwitchToRegister(ActionListener al) { btnGoToRegister.addActionListener(al); }
    public void addSwitchToLogin(ActionListener al) { btnGoToLogin.addActionListener(al); }
    public void showMessage(String msg) { JOptionPane.showMessageDialog(this, msg); }
    public void clearFields() {
        txtRegUser.setText(""); txtRegEmail.setText(""); txtRegPass.setText(""); txtRegConfirm.setText("");
        txtLoginUser.setText(""); txtLoginPass.setText("");
    }
}