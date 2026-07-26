/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gui;

import model.Customer;
import database.CustomerDAO;
import datastructure.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class AllocateBudgetScreen extends JFrame 
{

    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color ROYAL_BLUE = new Color(37, 99, 235);
    private final Color GREEN = new Color(22, 163, 74);
    private final Color ORANGE = new Color(249, 115, 22);

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField budgetField;
    private JButton processBtn;
    private JButton undoBtn;
    private JLabel statusLabel;

    private MainFrame dashboard;
    private Customer[] customerArray;
    private Stack actionHistory;
    private Queue outreachQueue;
    private CustomerDAO dao;
    private Customer[] allocatedCustomers;

    public AllocateBudgetScreen(MainFrame dashboard, Customer[] customerArray,
                                 Stack actionHistory, Queue outreachQueue, CustomerDAO dao) 
    {
        this.dashboard = dashboard;
        this.customerArray = customerArray;
        this.actionHistory = actionHistory;
        this.outreachQueue = outreachQueue;
        this.dao = dao;

        setTitle("ChurnInsight - Allocate Retention Budget");
        setSize(1100, 700);
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
                GradientPaint gp = new GradientPaint(0, 0, DARK_NAVY, getWidth(), 0, new Color(22, 163, 74));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(1100, 55));
        headerPanel.setLayout(new GridBagLayout());
        JLabel titleLabel = new JLabel("💰 Allocate Retention Budget");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Control Panel with glass background
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12)) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 255, 240), 0, getHeight(), new Color(220, 240, 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        controlPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(180, 210, 180)));

        JLabel budgetLabel = new JLabel("💰 Budget (PKR):");
        budgetLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        budgetField = new JTextField(10);
        budgetField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        budgetField.setPreferredSize(new Dimension(150, 36));
        budgetField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 180), 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)));

        JButton allocateBtn = createStyledButton("📊 Allocate", ROYAL_BLUE, 130, 36);

        processBtn = createStyledButton("📞 Contact Next Customer", GREEN, 230, 36);
        processBtn.setVisible(false);
        undoBtn = createStyledButton("↩ Cancel Last Offer", ORANGE, 200, 36);
        undoBtn.setVisible(false);

        controlPanel.add(budgetLabel);
        controlPanel.add(budgetField);
        controlPanel.add(allocateBtn);
        controlPanel.add(processBtn);
        controlPanel.add(undoBtn);

        // Status
        statusLabel = new JLabel("💡 Enter a budget and click Allocate to begin.");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(new Color(245, 255, 245));
        statusPanel.add(statusLabel, BorderLayout.CENTER);

        JPanel northComposite = new JPanel(new BorderLayout());
        northComposite.add(controlPanel, BorderLayout.CENTER);
        northComposite.add(statusPanel, BorderLayout.SOUTH);
        add(northComposite, BorderLayout.NORTH);

        // Table Container with glass gradient
        JPanel tableContainer = new JPanel(new BorderLayout()) 
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(248, 255, 248), 0, getHeight(), new Color(235, 250, 235));
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

        // Context menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem similarItem = new JMenuItem("Show Similar Customers");
        popupMenu.add(similarItem);
        table.setComponentPopupMenu(popupMenu);
        similarItem.addActionListener(e -> showSimilarCustomers());

        // Bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8)) 
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(240, 255, 240), 0, getHeight(), new Color(220, 240, 220));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(180, 210, 180)));

        JButton backBtn = createStyledButton("◀ Back To Dashboard", DARK_NAVY, 210, 38);
        backBtn.addActionListener(e -> dispose());
        bottomPanel.add(backBtn);

        add(tableContainer, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        allocateBtn.addActionListener(e -> allocateBudget());
        processBtn.addActionListener(e -> processQueue());
        undoBtn.addActionListener(e -> undoAction());
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

    private void allocateBudget() 
    {
        try {
            double budget = Double.parseDouble(budgetField.getText().trim());
            Customer[] sortedArray = customerArray.clone();
            MergeSort.sort(sortedArray);
            ArrayList<Customer> selected = GreedyAllocator.allocate(sortedArray, budget);

            outreachQueue = new Queue();
            outreachQueue.enqueueAll(selected);
            allocatedCustomers = selected.toArray(new Customer[0]);

            processBtn.setVisible(true);
            undoBtn.setVisible(true);

            refreshTable(allocatedCustomers);
            statusLabel.setText("✅ Budget Allocated | Selected Customers: " + selected.size());
        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Please enter a valid budget amount.");
        }
    }

    private void processQueue() 
    {
        if (outreachQueue.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Queue is empty. Allocate a budget first.");
            return;
        }
        Customer next = outreachQueue.dequeue();
        String previousStatus = next.getOfferStatus();
        next.setOfferStatus("Offered");
        dao.updateOfferStatus(next.getCustomerId(), "Offered");

        actionHistory.push(actionHistory.new Action(next.getCustomerId(), previousStatus, "Offered"));

        JOptionPane.showMessageDialog(this,
            "📞 Contacted: Customer ID " + next.getCustomerId() +
            "\nRisk: " + String.format("%.2f", next.getChurnProbability()) +
            "\nMarked as Offered.\n\nRemaining in queue: " + outreachQueue.getSize());

        dashboard.refreshData();
        refreshTable(allocatedCustomers);
    }

    private void undoAction()
    {
        Stack.Action lastAction = actionHistory.pop();
        if (lastAction == null) 
        {
            JOptionPane.showMessageDialog(this, "No actions to undo.");
            return;
        }
        for (Customer c : customerArray) 
        {
            if (c.getCustomerId() == lastAction.customerId) 
            {
                c.setOfferStatus(lastAction.previousStatus);
                dao.updateOfferStatus(c.getCustomerId(), lastAction.previousStatus);
                break;
            }
        }
        dashboard.refreshData();
        refreshTable(allocatedCustomers);
        statusLabel.setText("↩ Undone: Customer " + lastAction.customerId + " reverted to " + lastAction.previousStatus);
    }

    private void showSimilarCustomers() 
    {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) 
        {
            JOptionPane.showMessageDialog(this, "Please select a customer row first.");
            return;
        }
        int customerId = (int) tableModel.getValueAt(selectedRow, 0);

        Graph graph = new Graph();
        graph.buildGraph(customerArray);
        java.util.List<Customer> similar = graph.findSimilarCustomers(customerId, 1);

        if (similar.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "No similar customers found.");
            return;
        }
        refreshTable(similar.toArray(new Customer[0]));
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
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 255, 245));
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
        table.setGridColor(new Color(200, 220, 200));
    }
}

