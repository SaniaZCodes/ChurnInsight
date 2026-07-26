/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gui;

import database.CustomerDAO;

import javax.swing.*;
import java.awt.*;

public class DeleteCustomerScreen extends JFrame 
{

    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color RED = new Color(220, 38, 38);

    private JTextField idField;
    private MainFrame dashboard;
    private CustomerDAO dao;

    public DeleteCustomerScreen(MainFrame dashboard, CustomerDAO dao) 
    {
        this.dashboard = dashboard;
        this.dao = dao;

        setTitle("ChurnInsight - Delete Customer");
        setSize(480, 280);
        setLocationRelativeTo(dashboard);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        buildUI();
        setVisible(true);
    }

    private void buildUI() 
    {
        setLayout(new BorderLayout());

        // Glass gradient header
        JPanel headerPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, DARK_NAVY, getWidth(), 0, RED.darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(480, 55));
        headerPanel.setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel("🗑 Delete Customer");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Glass gradient center
        JPanel glassBg = new JPanel(new GridBagLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 240, 240), 0, getHeight(), new Color(255, 220, 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        glassBg.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel idLabel = new JLabel("🆔 Enter Customer ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));

        idField = new JTextField(10);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        idField.setPreferredSize(new Dimension(150, 38));
        idField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 180, 180), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        gbc.gridx = 0; gbc.gridy = 0;
        glassBg.add(idLabel, gbc);
        gbc.gridx = 1;
        glassBg.add(idField, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setOpaque(false);

        JButton deleteBtn = createStyledButton("🗑 Delete Customer", RED, 180, 42);
        JButton backBtn = createStyledButton("◀ Back To Dashboard", DARK_NAVY, 210, 42);

        btnPanel.add(deleteBtn);
        btnPanel.add(backBtn);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 8, 5, 8);
        glassBg.add(btnPanel, gbc);

        add(glassBg, BorderLayout.CENTER);

        deleteBtn.addActionListener(e -> deleteCustomer());
        backBtn.addActionListener(e -> dispose());
    }

    private JButton createStyledButton(String text, Color bgColor, int w, int h)
    {
        JButton btn = new JButton(text) 
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int bw = getWidth(), bh = getHeight();
                if (getModel().isPressed()) g2d.setColor(bgColor.darker());
                else if (getModel().isRollover()) g2d.setColor(bgColor.brighter());
                else g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, bw, bh, 20, 20);
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillRoundRect(5, 2, bw - 10, bh / 3, 15, 15);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int tx = (bw - fm.stringWidth(getText())) / 2;
                int ty = (bh + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), tx, ty);
                g2d.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private void deleteCustomer() 
    {
        String idText = idField.getText().trim();
        if (idText.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a Customer ID.", "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try 
        {
            int id = Integer.parseInt(idText);
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete Customer ID " + id + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION)
            {
                dao.deleteCustomer(id);
                JOptionPane.showMessageDialog(this, "✅ Customer Deleted Successfully!");
                dashboard.refreshData();
                dispose();
            }
        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}