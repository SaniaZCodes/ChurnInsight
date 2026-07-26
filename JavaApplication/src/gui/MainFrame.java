/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import model.Customer;
import database.CustomerDAO;
import datastructure.*;
import ml.FlaskConnector;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame 
{

    private CustomerDAO dao = new CustomerDAO();
    private LinkedList linkedList = new LinkedList();
    private BST bst = new BST();
    private Customer[] customerArray;
    private Stack actionHistory = new Stack();
    private Queue outreachQueue = new Queue();
    private JLabel totalValueLabel = new JLabel("0");
    private JLabel highRiskValueLabel = new JLabel("0");
    private JLabel offeredValueLabel = new JLabel("0");

    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color ROYAL_BLUE = new Color(37, 99, 235);
    private final Color SOFT_PURPLE = new Color(124, 58, 237);
    private final Color GLASS_WHITE = new Color(255, 255, 255, 200);
    private final Color GLASS_BORDER = new Color(255, 255, 255, 180);

    public MainFrame() 
    {
        setTitle("ChurnInsight - Dashboard");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1200, 750, 30, 30));

        loadData();
        buildDashboard();

        setVisible(true);
    }

    private void loadData() 
    {
        for (Customer c : dao.getAllCustomers()) 
        {
            linkedList.add(c);
        }
        customerArray = linkedList.toArray();
        bst.buildFromArray(customerArray);
    }

    private void reloadData() 
    {
        linkedList = new LinkedList();
        for (Customer c : dao.getAllCustomers())
        {
            linkedList.add(c);
        }
        customerArray = linkedList.toArray();
        bst = new BST();
        bst.buildFromArray(customerArray);
    }

    private void buildDashboard()
    {
        // Main glass gradient background panel
        JPanel glassBg = new JPanel(new BorderLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Dark gradient background
                GradientPaint gradient = new GradientPaint(0, 0, DARK_NAVY, getWidth(), getHeight(), SOFT_PURPLE);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Decorative blurred circles
                g2d.setColor(new Color(37, 99, 235, 80));
                g2d.fillOval(-100, -80, 400, 400);
                g2d.setColor(new Color(168, 85, 247, 60));
                g2d.fillOval(getWidth() - 280, getHeight() - 280, 380, 380);
                g2d.setColor(new Color(59, 130, 246, 40));
                g2d.fillOval(getWidth() / 2 - 100, 50, 250, 250);
                g2d.dispose();
            }
        };
        setContentPane(glassBg);

        // ---- Glass Header ----
        JPanel glassHeader = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
                // Bottom border line
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(20, getHeight() - 1, getWidth() - 20, getHeight() - 1);
                g2d.dispose();
            }
        };
        glassHeader.setOpaque(false);
        glassHeader.setBorder(new EmptyBorder(20, 40, 15, 40));
        glassHeader.setPreferredSize(new Dimension(1200, 130));

        // Title area
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);

        JLabel titleLabel = new JLabel("✦ ChurnInsight");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 34));

        JLabel subtitleLabel = new JLabel("AI-Powered Customer Churn Prediction & Retention System");
        subtitleLabel.setForeground(new Color(200, 210, 240));
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Stats summary on header
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 35, 8));
        statsPanel.setOpaque(false);

        int total = customerArray.length;
        int highRisk = 0;
        for (Customer c : customerArray) 
        {
            if (c.getChurnProbability() > 0.5) highRisk++;
        }
        int offered = 0;
        for (Customer c : customerArray) 
        {
            if ("Offered".equals(c.getOfferStatus())) offered++;
        }

        statsPanel.add(createStatCard("\uD83D\uDCCA", "Total Customers", totalValueLabel, new Color(37, 99, 235)));
        statsPanel.add(createStatCard("\u26A1", "High-Risk Customers", highRiskValueLabel, new Color(220, 38, 38)));
        statsPanel.add(createStatCard("\u2709\uFE0F", "Offers Sent", offeredValueLabel, new Color(22, 163, 74)));
        refreshDashboardStats();

        glassHeader.add(titlePanel, BorderLayout.WEST);
        glassHeader.add(statsPanel, BorderLayout.EAST);

        glassBg.add(glassHeader, BorderLayout.NORTH);

        // ---- Center Glass Card with Buttons ----
        JPanel glassCenter = new JPanel(new GridBagLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                // Glass card
                g2d.setColor(new Color(255, 255, 255, 45));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.drawRoundRect(50, 20, getWidth() - 100, getHeight() - 40, 30, 30);
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.fillRoundRect(50, 20, getWidth() - 100, getHeight() - 40, 30, 30);
                g2d.dispose();
            }
        };
        glassCenter.setOpaque(false);
        glassCenter.setBorder(new EmptyBorder(30, 60, 30, 60));

        JPanel buttonGrid = new JPanel(new GridLayout(4, 2, 25, 25));
        buttonGrid.setOpaque(false);

        // Modern styled buttons with glass effect
        JButton searchBtn = createGlassButton("\uD83D\uDD0D  Search Customer", ROYAL_BLUE);
        JButton showAllBtn = createGlassButton("\uD83D\uDCCB  Show All Customers", ROYAL_BLUE);
        JButton highRiskBtn = createGlassButton("\u26A1  View High-Risk Customers", ROYAL_BLUE);
        JButton allocateBtn = createGlassButton("\uD83D\uDCB0  Allocate Retention Budget", ROYAL_BLUE);
        JButton addBtn = createGlassButton("\u2795  Add New Customer", SOFT_PURPLE);
        JButton updateBtn = createGlassButton("\u270F\uFE0F  Update Customer", SOFT_PURPLE);
        JButton deleteBtn = createGlassButton("\uD83D\uDDD1\uFE0F  Delete Customer", SOFT_PURPLE);
        JButton exitBtn = createGlassButton("\uD83D\uDEAA  Exit Program", new Color(220, 38, 38));

        // Button actions
        searchBtn.addActionListener(e -> new SearchScreen(this, bst, customerArray, dao));
        showAllBtn.addActionListener(e -> new AllCustomersScreen(this, customerArray, dao));
        highRiskBtn.addActionListener(e -> new HighRiskScreen(this, customerArray, dao));
        allocateBtn.addActionListener(e -> new AllocateBudgetScreen(this, customerArray, actionHistory, outreachQueue, dao));
        addBtn.addActionListener(e -> new AddCustomerScreen(this, dao));
        updateBtn.addActionListener(e -> new UpdateCustomerScreen(this, dao));
        deleteBtn.addActionListener(e -> new DeleteCustomerScreen(this, dao));

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

        buttonGrid.add(searchBtn);
        buttonGrid.add(showAllBtn);
        buttonGrid.add(highRiskBtn);
        buttonGrid.add(allocateBtn);
        buttonGrid.add(addBtn);
        buttonGrid.add(updateBtn);
        buttonGrid.add(deleteBtn);
        buttonGrid.add(exitBtn);

        glassCenter.add(buttonGrid);
        glassBg.add(glassCenter, BorderLayout.CENTER);

        // ---- Footer ----
        JLabel footerLabel = new JLabel("Machine Learning  •  Database  •  Data Structures", SwingConstants.CENTER);
        footerLabel.setForeground(new Color(180, 190, 220, 180));
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        glassBg.add(footerLabel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String icon, String label, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();

                // Card shadow
                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillRoundRect(3, 4, w - 6, h - 3, 18, 18);

                // Glass card background (more opaque for stronger appearance)
                g2d.setColor(new Color(255, 255, 255, 55));
                g2d.fillRoundRect(0, 0, w, h, 18, 18);

                // Top glass highlight
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fillRoundRect(5, 2, w - 10, h / 3, 14, 14);

                // Left accent border (thicker)
                g2d.setColor(accentColor);
                g2d.setStroke(new BasicStroke(4));
                g2d.drawLine(0, 8, 0, h - 8);

                // Bottom subtle glow line
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(1, 1, w - 3, h - 3, 18, 18);

                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(175, 80));
        card.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblLabel.setForeground(new Color(180, 195, 230));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(valueLabel, BorderLayout.NORTH);
        textPanel.add(lblLabel, BorderLayout.SOUTH);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void refreshDashboardStats() 
    {
        int total = customerArray.length;
        int highRisk = 0;
        for (Customer c : customerArray) 
        {
            if (c.getChurnProbability() > 0.5) highRisk++;
        }
        int offered = 0;
        for (Customer c : customerArray) 
        {
            if ("Offered".equals(c.getOfferStatus())) offered++;
        }
        totalValueLabel.setText(String.format("%,d", total));
        highRiskValueLabel.setText(String.format("%,d", highRisk));
        offeredValueLabel.setText(String.format("%,d", offered));
    }

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

                int w = getWidth();
                int h = getHeight();

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
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(260, 65));

        return btn;
    }

    // Methods accessible by feature screens
    public void refreshData() 
    {
        reloadData();
        refreshDashboardStats();
    }

    public Customer[] getCustomerArray() 
    {
        return customerArray;
    }

    public BST getBST() 
    {
        return bst;
    }

    public CustomerDAO getDAO() 
    {
        return dao;
    }
}