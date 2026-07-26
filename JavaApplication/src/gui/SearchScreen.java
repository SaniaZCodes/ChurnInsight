/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gui;

import model.Customer;
import database.CustomerDAO;
import datastructure.BST;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SearchScreen extends JFrame 
{

    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color ROYAL_BLUE = new Color(37, 99, 235);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private MainFrame dashboard;
    private BST bst;
    private Customer[] customerArray;
    private CustomerDAO dao;

    public SearchScreen(MainFrame dashboard, BST bst, Customer[] customerArray, CustomerDAO dao) 
    {
        this.dashboard = dashboard;
        this.bst = bst;
        this.customerArray = customerArray;
        this.dao = dao;

        setTitle("ChurnInsight - Search Customer");
        setSize(1000, 600);
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
                GradientPaint gp = new GradientPaint(0, 0, DARK_NAVY, getWidth(), 0, ROYAL_BLUE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(1000, 55));
        headerPanel.setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel("🔍 Search Customer");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Search Panel with glass background
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12)) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 244, 255), 0, getHeight(), new Color(220, 230, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        searchBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 210, 230)));

        JLabel searchLabel = new JLabel("🆔 Customer ID:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        searchField = new JTextField(10);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(150, 36));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        JButton searchBtn = createStyledButton("🔍 Search", ROYAL_BLUE, 130, 36);
        searchBar.add(searchLabel);
        searchBar.add(searchField);
        searchBar.add(searchBtn);

        add(searchBar, BorderLayout.NORTH);

        // Table with glass background
        JPanel tableContainer = new JPanel(new BorderLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(248, 250, 255), 0, getHeight(), new Color(235, 240, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };

        String[] columns = {"ID", "Gender", "Tenure", "Contract", "Internet", "Monthly Charges (PKR)", "Churn Risk", "Status"};
        tableModel = new DefaultTableModel(columns, 0) 
        {
            public boolean isCellEditable(int row, int col) 
            { 
                return false; 
            }
        };
        table = new JTable(tableModel);
        styleTable();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8))
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 244, 255), 0, getHeight(), new Color(220, 230, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 210, 230)));

        JButton backBtn = createStyledButton("◀ Back To Dashboard", DARK_NAVY, 210, 38);
        backBtn.addActionListener(e -> dispose());
        bottomPanel.add(backBtn);

        add(tableContainer, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Search action
        searchBtn.addActionListener(e -> searchCustomer());
        searchField.addActionListener(e -> searchCustomer());
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
                g2d.fillRoundRect(0, 0, bw, bh, 18, 18);
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.fillRoundRect(4, 2, bw - 8, bh / 3, 12, 12);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int tx = (bw - fm.stringWidth(getText())) / 2;
                int ty = (bh + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), tx, ty);
                g2d.dispose();
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        return btn;
    }

    private void searchCustomer()
    {
        try
        {
            int id = Integer.parseInt(searchField.getText().trim());
            Customer found = bst.search(id);
            if (found != null) 
            {
                refreshTable(new Customer[]{found});
            } 
            else 
            {
                JOptionPane.showMessageDialog(this, "Customer ID " + id + " not found.");
                refreshTable(new Customer[0]);
            }
        } 
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric ID.");
        }
    }

    private void refreshTable(Customer[] data)
    {
        tableModel.setRowCount(0);
        for (Customer c : data) 
        {
            tableModel.addRow(new Object[]
            {
                c.getCustomerId(), c.getGender(), c.getTenure(), c.getContractType(),
                c.getInternetService(), String.format("%.0f", c.getMonthlyCharges() * 280),
                String.format("%.2f", c.getChurnProbability()), c.getOfferStatus()
            });
        }
    }

    private void styleTable()
    {
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() 
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) 
            {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) 
                {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 248, 255));
                }
                return c;
            }
        });
        table.getTableHeader().setBackground(DARK_NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
    }
}