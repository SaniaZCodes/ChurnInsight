/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame 
{
    // Project colors
    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color ROYAL_BLUE = new Color(37, 99, 235);
    private final Color SOFT_PURPLE = new Color(124, 58, 237);
    private final Color LIGHT_TEXT = new Color(100, 116, 139);

    public LoginFrame()
    {
        setTitle("ChurnInsight - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // ========== LAYERED PANE ==========
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);

        // Layer 0: Gradient background with static decorative circles
        JPanel backgroundPanel = new JPanel(null)
        {
            @Override
            protected void paintComponent(Graphics graphics) 
            {
                super.paintComponent(graphics);
                Graphics2D g2d = (Graphics2D) graphics.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dark navy to purple gradient background
                GradientPaint gradient = new GradientPaint(0, 0, DARK_NAVY, getWidth(), getHeight(), SOFT_PURPLE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative large static circles
                g2d.setColor(new Color(37, 99, 235, 70));
                g2d.fillOval(-100, -100, 350, 350);

                g2d.setColor(new Color(168, 85, 247, 55));
                g2d.fillOval(getWidth() - 270, getHeight() - 270, 380, 380);

                g2d.dispose();
            }
        };
        backgroundPanel.setBounds(0, 0, 1100, 650);
        layeredPane.add(backgroundPanel, Integer.valueOf(0));

        // ========== PREMIUM FROSTED WHITE CARD (centered, clean, no animation) ==========
        int cardW = 520, cardH = 370;
        int baseX = (1100 - cardW) / 2;
        int baseY = (650 - cardH) / 2;

        JPanel glassCard = new JPanel(new GridBagLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int w = getWidth(), h = getHeight();

                // Soft elegant shadow for premium depth
                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillRoundRect(4, 6, w - 8, h - 5, 38, 38);

                // Clean white frosted background (very light transparency)
                g2d.setColor(new Color(255, 255, 255, 230));
                g2d.fillRoundRect(0, 0, w, h, 38, 38);

                // Subtle thin border
                g2d.setColor(new Color(255, 255, 255, 220));
                g2d.setStroke(new BasicStroke(1.2f));
                g2d.drawRoundRect(0, 0, w - 1, h - 1, 38, 38);

                g2d.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setBounds(baseX, baseY, cardW, cardH);
        glassCard.setBorder(BorderFactory.createEmptyBorder(28, 40, 24, 40));
        layeredPane.add(glassCard, Integer.valueOf(1));

        // ========== CARD CONTENT ==========
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // ---- "ChurnInsight" - Dark navy, bold, clean ----
        JLabel titleLabel = new JLabel("ChurnInsight", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        titleLabel.setForeground(DARK_NAVY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        gbc.fill = GridBagConstraints.NONE;
        glassCard.add(titleLabel, gbc);

        // ---- Subtitle - dark gray, readable ----
        JLabel subtitleLabel = new JLabel(
            "<html><div style='text-align:center;'>" +
            "<span style='font-size:14px; color:#475569;'>" +
            "AI-Powered Customer Churn<br>Prediction &amp; Retention System</span></div></html>",
            SwingConstants.CENTER
        );
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(71, 85, 105));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.fill = GridBagConstraints.NONE;
        glassCard.add(subtitleLabel, gbc);

        // ---- "Predict. | Retain. | Grow." - Dark purple, bold ----
        JLabel taglineLabel = new JLabel("Predict. | Retain. | Grow.", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        taglineLabel.setForeground(SOFT_PURPLE);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.NONE;
        glassCard.add(taglineLabel, gbc);

        // ---- LOGIN Button ----
        JButton loginBtn = createGlassButton("  LOGIN  ", 
            new Color(37, 99, 235));
        loginBtn.setPreferredSize(new Dimension(240, 44));
        gbc.gridy = 4;
        gbc.insets = new Insets(2, 0, 3, 0);
        gbc.fill = GridBagConstraints.NONE;
        glassCard.add(loginBtn, gbc);

        // ---- EXIT Button ----
        JButton exitBtn = createGlassButton("  EXIT  ", 
            new Color(220, 38, 38));
        exitBtn.setPreferredSize(new Dimension(240, 44));
        gbc.gridy = 5;
        gbc.insets = new Insets(3, 0, 4, 0);
        glassCard.add(exitBtn, gbc);

        // ========== BUTTON ACTIONS ==========
        loginBtn.addActionListener(e -> showLoginDialog());
        exitBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit ChurnInsight?",
                "Exit Program",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    // ============================================================
    //  GLASS BUTTON (matches MainFrame dashboard button style)
    // ============================================================
    private JButton createGlassButton(String text, Color baseColor)
    {
        JButton btn = new JButton(text)
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int w = getWidth(), h = getHeight();

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillRoundRect(3, 5, w - 6, h - 5, 18, 18);

                // Main glass background
                if (getModel().isPressed())
                {
                    g2d.setColor(baseColor.darker());
                }
                else if (getModel().isRollover())
                {
                    g2d.setColor(baseColor.brighter());
                }
                else
                {
                    g2d.setColor(baseColor);
                }

                // Semi-transparent overlay for glass feel
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
                g2d.fillRoundRect(0, 0, w, h, 18, 18);

                // Glass highlight at top
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(3, 2, w - 6, h / 3, 15, 15);

                // Border glow
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(1.2f));
                g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);

                // Text
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textX = (w - fm.stringWidth(getText())) / 2;
                int textY = (h + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), textX, textY);

                g2d.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(240, 44));
        return btn;
    }

    // ============================================================
    //  LOGIN DIALOG
    // ============================================================
    private void showLoginDialog()
    {
        JDialog loginDialog = new JDialog(this, "Login", true);
        loginDialog.setSize(400, 280);
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setResizable(false);
        loginDialog.setUndecorated(true);
        loginDialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(100, 116, 139, 100), 1));

        // Main panel with gradient
        JPanel dialogPanel = new JPanel(new GridBagLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, Color.WHITE, 0, getHeight(), new Color(240, 244, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));
        loginDialog.setContentPane(dialogPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);

        // Title
        JLabel dialogTitle = new JLabel("Enter Credentials", SwingConstants.CENTER);
        dialogTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        dialogTitle.setForeground(DARK_NAVY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 8, 12, 8);
        dialogPanel.add(dialogTitle, gbc);

        // Username
        JLabel userLbl = new JLabel("Username");
        userLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userLbl.setForeground(DARK_NAVY);
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 8, 2, 8);
        dialogPanel.add(userLbl, gbc);

        JTextField userField = new JTextField(15);
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        gbc.gridy = 2;
        gbc.insets = new Insets(2, 8, 6, 8);
        dialogPanel.add(userField, gbc);

        // Password
        JLabel passLbl = new JLabel("Password");
        passLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        passLbl.setForeground(DARK_NAVY);
        gbc.gridy = 3;
        gbc.insets = new Insets(4, 8, 2, 8);
        dialogPanel.add(passLbl, gbc);

        JPasswordField passField = new JPasswordField(15);
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        gbc.gridy = 4;
        gbc.insets = new Insets(2, 8, 8, 8);
        dialogPanel.add(passField, gbc);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBackground(ROYAL_BLUE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setPreferredSize(new Dimension(120, 38));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(100, 110, 130));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setPreferredSize(new Dimension(120, 38));

        cancelBtn.addActionListener(ev -> loginDialog.dispose());

        btnPanel.add(loginBtn);
        btnPanel.add(cancelBtn);

        gbc.gridy = 5;
        gbc.insets = new Insets(10, 8, 0, 8);
        dialogPanel.add(btnPanel, gbc);

        // Login action
        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty())
            {
                JOptionPane.showMessageDialog(
                        loginDialog,
                        "Please enter both username and password.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (username.equals("admin") && password.equals("admin123"))
            {
                loginDialog.dispose();
                dispose();
                new MainFrame();
            }
            else
            {
                JOptionPane.showMessageDialog(
                        loginDialog,
                        "Invalid username or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                passField.setText("");
                passField.requestFocus();
            }
        });

        // Press Enter in password field triggers login
        loginDialog.getRootPane().setDefaultButton(loginBtn);

        loginDialog.setVisible(true);
    }

    // ============================================================
    //  MAIN
    // ============================================================
    public static void main(String[] args) 
    {
        try 
        {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
            {
                if ("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } 
        catch (Exception ignored) { }

        SwingUtilities.invokeLater(LoginFrame::new);
    }
}

