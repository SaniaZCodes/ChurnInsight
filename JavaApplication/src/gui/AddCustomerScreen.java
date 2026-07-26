/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gui;

import database.CustomerDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddCustomerScreen extends JFrame 
{

    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color SOFT_PURPLE = new Color(124, 58, 237);
    private final Color BASIC_COLOR = new Color(37, 99, 235);
    private final Color STANDARD_COLOR = new Color(22, 163, 74);
    private final Color PREMIUM_COLOR = new Color(168, 85, 247);
    private final Color CARD_BG = new Color(255, 255, 255, 230);
    private final Color SELECTED_GLOW = new Color(124, 58, 237, 80);

    private JLabel nextIdLabel;
    private JTextField phoneField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<String> seniorCitizenCombo;
    private JComboBox<String> partnerCombo;
    private JComboBox<String> dependentsCombo;
    private JComboBox<Integer> tenureCombo;
    private JComboBox<String> phoneServiceCombo;
    private JComboBox<String> multipleLinesCombo;
    private JComboBox<String> paymentMethodCombo;
    private JPanel detailPanel;
    private JLabel totalLabel;
    private JPanel[] packageCards = new JPanel[3];
    private int selectedPackageIdx = 0;

    private MainFrame dashboard;
    private CustomerDAO dao;
    private int nextId;

    private static final String[][] PACKAGES = {
        {"Basic - Rs. 2,000/mo", "DSL", "No", "No", "No", "No", "No", "No", "Month-to-month", "Yes", "2000"},
        {"Standard - Rs. 5,000/mo", "Fiber optic", "Yes", "Yes", "No", "Yes", "Yes", "No", "One year", "Yes", "5000"},
        {"Premium - Rs. 8,000/mo", "Fiber optic", "Yes", "Yes", "Yes", "Yes", "Yes", "Yes", "Two year", "Yes", "8000"}
    };

    private static final String[] FEATURE_LABELS = {
        "Internet", "Online Security", "Online Backup", "Device Protection",
        "Tech Support", "Streaming TV", "Streaming Movies", "Contract", "Paperless Billing"
    };

    private static final String[] PKG_SHORT_NAMES = {"Basic", "Standard", "Premium"};
    private static final String[] PKG_PRICES = {"Rs. 2,000/mo", "Rs. 5,000/mo", "Rs. 8,000/mo"};

    public AddCustomerScreen(MainFrame dashboard, CustomerDAO dao) 
    {
        try
        {
            this.dashboard = dashboard;
            this.dao = dao;
            this.nextId = dao.getNextCustomerId();

setTitle("ChurnInsight - Add New Customer");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(1300, 800);
            setLocationRelativeTo(dashboard);
            setResizable(false);

            buildUI();
            setVisible(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                dashboard,
                "Error: Could not open Add Customer form.\n" +
                "Database connection may be unavailable.\n\n" +
                "Details: " + ex.getMessage(),
                "Error Opening Form",
                JOptionPane.ERROR_MESSAGE
            );
            dispose();
        }
    }

    private void buildUI() 
    {
        setLayout(new BorderLayout(0, 5));

        // === HEADER ===
        JPanel headerPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, DARK_NAVY, getWidth(), 0, SOFT_PURPLE);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(10, getHeight() - 1, getWidth() - 10, getHeight() - 1);
                g2d.dispose();
            }
        };
        headerPanel.setPreferredSize(new Dimension(1, 55));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        
        JLabel titleLabel = new JLabel("✚ Add New Customer");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        
        nextIdLabel = new JLabel("🆔 Next ID: " + nextId);
        nextIdLabel.setForeground(new Color(200, 255, 200));
        nextIdLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(nextIdLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // === MAIN CONTENT (no scroll) ===
        JPanel mainPanel = new JPanel(new BorderLayout(0, 8))
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

        // === TOP: Three Package Cards ===
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        Color[] cardColors = {BASIC_COLOR, STANDARD_COLOR, PREMIUM_COLOR};
        for (int i = 0; i < 3; i++) {
            packageCards[i] = createPackageCard(i, cardColors[i]);
            cardsPanel.add(packageCards[i]);
            final int idx = i;
            packageCards[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectPackage(idx);
                }
            });
        }
        mainPanel.add(cardsPanel, BorderLayout.NORTH);

        // === CENTER SPLIT: Left Form + Right Detail ===
        JPanel centerSplit = new JPanel(new GridLayout(1, 2, 15, 0));
        centerSplit.setOpaque(false);
        centerSplit.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));

        // --- LEFT: Customer Info Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
            "📋 Customer Information",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), DARK_NAVY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 8, 3, 8);
        gbc.weightx = 1.0;

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        String[] yesNo = {"No", "Yes"};
        String[] paymentMethods = {"Electronic check", "Mailed check", "Bank transfer", "Credit card"};

        int row = 0;

        // Phone
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel phoneLbl = new JLabel("📞 Phone:");
        phoneLbl.setFont(labelFont);
        formPanel.add(phoneLbl, gbc);
        
        phoneField = new JTextField(20);
        phoneField.setFont(fieldFont);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(phoneField, gbc);

        // Gender
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel genderLbl = new JLabel("👤 Gender:");
        genderLbl.setFont(labelFont);
        formPanel.add(genderLbl, gbc);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setOpaque(false);
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        maleRadio.setFont(fieldFont); femaleRadio.setFont(fieldFont);
        maleRadio.setOpaque(false); femaleRadio.setOpaque(false);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio); genderGroup.add(femaleRadio);
        maleRadio.setSelected(true);
        genderPanel.add(maleRadio); genderPanel.add(femaleRadio);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(genderPanel, gbc);

        // Senior Citizen
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel scLbl = new JLabel("👴 Senior Citizen:");
        scLbl.setFont(labelFont);
        formPanel.add(scLbl, gbc);
        seniorCitizenCombo = new JComboBox<>(yesNo);
        seniorCitizenCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(seniorCitizenCombo, gbc);

        // Partner
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel partnerLbl = new JLabel("💑 Partner:");
        partnerLbl.setFont(labelFont);
        formPanel.add(partnerLbl, gbc);
        partnerCombo = new JComboBox<>(yesNo);
        partnerCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(partnerCombo, gbc);

        // Dependents
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel depLbl = new JLabel("👶 Dependents:");
        depLbl.setFont(labelFont);
        formPanel.add(depLbl, gbc);
        dependentsCombo = new JComboBox<>(yesNo);
        dependentsCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(dependentsCombo, gbc);

        // Tenure
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel tenLbl = new JLabel("📅 Tenure (months):");
        tenLbl.setFont(labelFont);
        formPanel.add(tenLbl, gbc);
        tenureCombo = new JComboBox<>();
        for (int i = 1; i <= 120; i++) tenureCombo.addItem(i);
        tenureCombo.setFont(fieldFont);
        tenureCombo.addActionListener(e -> updatePackageDetails());
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(tenureCombo, gbc);

        // Phone Service
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel psLbl = new JLabel("📞 Phone Service:");
        psLbl.setFont(labelFont);
        formPanel.add(psLbl, gbc);
        phoneServiceCombo = new JComboBox<>(yesNo);
        phoneServiceCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(phoneServiceCombo, gbc);

        // Multiple Lines
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel mlLbl = new JLabel("🔗 Multiple Lines:");
        mlLbl.setFont(labelFont);
        formPanel.add(mlLbl, gbc);
        multipleLinesCombo = new JComboBox<>(new String[]{"No", "Yes", "No phone service"});
        multipleLinesCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(multipleLinesCombo, gbc);

        // Payment Method
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.0;
        JLabel pmLbl = new JLabel("💳 Payment Method:");
        pmLbl.setFont(labelFont);
        formPanel.add(pmLbl, gbc);
        paymentMethodCombo = new JComboBox<>(paymentMethods);
        paymentMethodCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.weightx = 1.0;
        formPanel.add(paymentMethodCombo, gbc);

        // Fill space
        gbc.gridy = ++row; gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);

        centerSplit.add(formPanel);

        // --- RIGHT: Package Details ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
            "📦 Package Details",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14), DARK_NAVY
        ));

        detailPanel = new JPanel(new GridBagLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                g2d.setColor(new Color(180, 200, 220, 120));
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, w - 1, h - 1, 15, 15);
                g2d.dispose();
            }
        };
        detailPanel.setOpaque(false);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        rightPanel.add(detailPanel, BorderLayout.CENTER);

        centerSplit.add(rightPanel);
        mainPanel.add(centerSplit, BorderLayout.CENTER);

        // === BOTTOM: Total + Buttons ===
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(3, 20, 10, 20));

        totalLabel = new JLabel("Total Charges: Rs. 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(DARK_NAVY);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);
        JButton addBtn = createStyledButton("✚ Add Customer", SOFT_PURPLE, 160, 40);
        JButton backBtn = createStyledButton("◀ Back To Dashboard", DARK_NAVY, 210, 40);
        btnPanel.add(addBtn);
        btnPanel.add(backBtn);

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Initialize
        selectPackage(0);
        addBtn.addActionListener(e -> addCustomer());
        backBtn.addActionListener(e -> dispose());
    }

    private JPanel createPackageCard(int index, Color accentColor)
    {
        JPanel card = new JPanel(new BorderLayout())
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(2, 2, w - 4, h - 4, 18, 18);
                g2d.setColor(accentColor);
                g2d.fillRoundRect(2, 2, w - 4, 6, 6, 6);
                g2d.fillRect(2, 5, w - 4, 4);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(200, 210, 230));
                g2d.drawRoundRect(2, 2, w - 4, h - 4, 18, 18);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 10, 12));
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.fill = GridBagConstraints.HORIZONTAL;
        cgbc.insets = new Insets(2, 0, 2, 0);
        cgbc.gridx = 0;

        JLabel nameLbl = new JLabel(PKG_SHORT_NAMES[index], SwingConstants.CENTER);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nameLbl.setForeground(accentColor);
        cgbc.gridy = 0;
        content.add(nameLbl, cgbc);

        JLabel priceLbl = new JLabel(PKG_PRICES[index], SwingConstants.CENTER);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLbl.setForeground(DARK_NAVY);
        cgbc.gridy = 1;
        content.add(priceLbl, cgbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 225, 240));
        cgbc.gridy = 2;
        cgbc.insets = new Insets(5, 15, 5, 15);
        content.add(sep, cgbc);
        cgbc.insets = new Insets(1, 0, 1, 0);

        String[] pkg = PACKAGES[index];
        int inclCount = 0;
        for (int i = 2; i <= 7; i++) {
            if ("Yes".equals(pkg[i])) inclCount++;
        }
        JLabel featLbl = new JLabel(inclCount + " premium features", SwingConstants.CENTER);
        featLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        featLbl.setForeground(new Color(100, 110, 130));
        cgbc.gridy = 3;
        content.add(featLbl, cgbc);

        JLabel internetLbl = new JLabel("📶 " + pkg[1], SwingConstants.CENTER);
        internetLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        internetLbl.setForeground(new Color(80, 90, 110));
        cgbc.gridy = 4;
        content.add(internetLbl, cgbc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void selectPackage(int idx)
    {
        selectedPackageIdx = idx;
        Color[] colors = {BASIC_COLOR, STANDARD_COLOR, PREMIUM_COLOR};

        for (int i = 0; i < packageCards.length; i++)
        {
            final int cardIdx = i;
            final Color accent = colors[i];
            JPanel card = packageCards[i];

            JPanel newCard = new JPanel(new BorderLayout())
            {
                @Override
                protected void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    int w = getWidth(), h = getHeight();

                    g2d.setColor(CARD_BG);
                    g2d.fillRoundRect(2, 2, w - 4, h - 4, 18, 18);

                    if (cardIdx == selectedPackageIdx)
                    {
                        g2d.setColor(SELECTED_GLOW);
                        g2d.setStroke(new BasicStroke(6));
                        g2d.drawRoundRect(1, 1, w - 2, h - 2, 18, 18);
                        g2d.setColor(accent);
                        g2d.fillRoundRect(2, 2, w - 4, 8, 8, 8);
                        g2d.fillRect(2, 7, w - 4, 4);
                        g2d.setStroke(new BasicStroke(2.5f));
                        g2d.setColor(accent);
                        g2d.drawRoundRect(2, 2, w - 4, h - 4, 18, 18);
                    }
                    else
                    {
                        g2d.setColor(accent);
                        g2d.fillRoundRect(2, 2, w - 4, 6, 6, 6);
                        g2d.fillRect(2, 5, w - 4, 4);
                        g2d.setStroke(new BasicStroke(1.5f));
                        g2d.setColor(new Color(200, 210, 230));
                        g2d.drawRoundRect(2, 2, w - 4, h - 4, 18, 18);
                    }
                    g2d.dispose();
                }
            };
            newCard.setOpaque(false);
            newCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel content = new JPanel(new GridBagLayout());
            content.setOpaque(false);
            content.setBorder(BorderFactory.createEmptyBorder(12, 12, 10, 12));
            GridBagConstraints cgbc = new GridBagConstraints();
            cgbc.fill = GridBagConstraints.HORIZONTAL;
            cgbc.insets = new Insets(2, 0, 2, 0);
            cgbc.gridx = 0;

            JLabel nameLbl = new JLabel(PKG_SHORT_NAMES[cardIdx], SwingConstants.CENTER);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
            nameLbl.setForeground(accent);
            cgbc.gridy = 0;
            content.add(nameLbl, cgbc);

            JLabel priceLbl = new JLabel(PKG_PRICES[cardIdx], SwingConstants.CENTER);
            priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
            priceLbl.setForeground(DARK_NAVY);
            cgbc.gridy = 1;
            content.add(priceLbl, cgbc);

            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(220, 225, 240));
            cgbc.gridy = 2;
            cgbc.insets = new Insets(5, 15, 5, 15);
            content.add(sep, cgbc);
            cgbc.insets = new Insets(1, 0, 1, 0);

            String[] pkg = PACKAGES[cardIdx];
            int inclCount = 0;
            for (int j = 2; j <= 7; j++) {
                if ("Yes".equals(pkg[j])) inclCount++;
            }
            JLabel featLbl = new JLabel(inclCount + " premium features", SwingConstants.CENTER);
            featLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            featLbl.setForeground(new Color(100, 110, 130));
            cgbc.gridy = 3;
            content.add(featLbl, cgbc);

            JLabel internetLbl = new JLabel("📶 " + pkg[1], SwingConstants.CENTER);
            internetLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            internetLbl.setForeground(new Color(80, 90, 110));
            cgbc.gridy = 4;
            content.add(internetLbl, cgbc);

            newCard.add(content, BorderLayout.CENTER);

            // Replace the card in the parent
            Container parent = card.getParent();
            if (parent != null)
            {
                parent.remove(card);
                parent.add(newCard, cardIdx);
                final int fIdx = cardIdx;
                newCard.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectPackage(fIdx);
                    }
                });
                packageCards[cardIdx] = newCard;
            }
        }

        parentRevalidate(packageCards[0]);
        updatePackageDetails();
    }

    private void parentRevalidate(JPanel child)
    {
        Container p = child.getParent();
        while (p != null) {
            p.revalidate();
            p.repaint();
            p = p.getParent();
        }
    }

    private void updatePackageDetails()
    {
        int idx = selectedPackageIdx;
        if (idx < 0) idx = 0;
        String[] pkg = PACKAGES[idx];
        double monthly = Double.parseDouble(pkg[10]);
        int tenureVal = (Integer) tenureCombo.getSelectedItem();

        detailPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 8, 3, 8);
        gbc.gridx = 0;

        Color pkgColor = idx == 0 ? BASIC_COLOR : (idx == 1 ? STANDARD_COLOR : PREMIUM_COLOR);

        // Package name + badge
        JLabel pkgNameLbl = new JLabel("📦 " + PKG_SHORT_NAMES[idx] + " Plan");
        pkgNameLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pkgNameLbl.setForeground(pkgColor);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 8, 2, 8);
        detailPanel.add(pkgNameLbl, gbc);

        // Monthly price
        JLabel priceLbl = new JLabel(PKG_PRICES[idx]);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        priceLbl.setForeground(pkgColor);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 8, 6, 8);
        detailPanel.add(priceLbl, gbc);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(200, 210, 230));
        gbc.gridy = 2;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        detailPanel.add(sep, gbc);
        gbc.insets = new Insets(3, 8, 3, 8);

        // Feature list with clean label : value format
        String[] values = {pkg[1], pkg[2], pkg[3], pkg[4], pkg[5], pkg[6], pkg[7], pkg[8], pkg[9]};
        Font detailFont = new Font("Segoe UI", Font.PLAIN, 13);

        for (int i = 0; i < FEATURE_LABELS.length; i++)
        {
            gbc.gridy = i + 3;
            boolean included = "Yes".equals(values[i]);
            String value = values[i];

            JPanel featureRow = new JPanel(new BorderLayout(8, 0));
            featureRow.setOpaque(false);

            JLabel featLbl = new JLabel(FEATURE_LABELS[i]);
            featLbl.setFont(detailFont);
            featLbl.setForeground(DARK_NAVY);

            JLabel valLbl = new JLabel(value, SwingConstants.RIGHT);
            valLbl.setFont(new Font("Segoe UI", included ? Font.BOLD : Font.PLAIN, 13));
            valLbl.setForeground(included ? new Color(22, 163, 74) : new Color(180, 50, 50));

            featureRow.add(featLbl, BorderLayout.CENTER);
            featureRow.add(valLbl, BorderLayout.EAST);

            detailPanel.add(featureRow, gbc);
        }

        // Update total
        double totalCharges = monthly * tenureVal;
        totalLabel.setText("Total Charges: Rs. " + String.format("%,.0f", totalCharges));

        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private JButton createStyledButton(String text, Color bgColor, int w, int h) {
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
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addCustomer() 
    {
        try 
        {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) 
            {
                JOptionPane.showMessageDialog(this, "Please enter a phone number.", "Missing Information", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idx = selectedPackageIdx;
            if (idx < 0) idx = 0;
            String[] pkg = PACKAGES[idx];

            String gender = maleRadio.isSelected() ? "Male" : "Female";
            int seniorCitizen = "Yes".equals(seniorCitizenCombo.getSelectedItem()) ? 1 : 0;
            String partner = (String) partnerCombo.getSelectedItem();
            String dependents = (String) dependentsCombo.getSelectedItem();
            int tenure = (Integer) tenureCombo.getSelectedItem();
            String phoneService = (String) phoneServiceCombo.getSelectedItem();
            String multipleLines = (String) multipleLinesCombo.getSelectedItem();
            String internet = pkg[1];
            String onlineSecurity = pkg[2];
            String onlineBackup = pkg[3];
            String deviceProtection = pkg[4];
            String techSupport = pkg[5];
            String streamingTv = pkg[6];
            String streamingMovies = pkg[7];
            String contract = pkg[8];
            String paperlessBilling = pkg[9];
            String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
            double monthly = Double.parseDouble(pkg[10]);
            String offerStatus = "Not Offered";

            dao.insertCustomer(
                phone, gender, seniorCitizen, partner, dependents,
                tenure, phoneService, multipleLines, internet,
                onlineSecurity, onlineBackup, deviceProtection, techSupport,
                streamingTv, streamingMovies, contract, paperlessBilling,
                paymentMethod, monthly, offerStatus
            );
            
            JOptionPane.showMessageDialog(this, "✅ Customer Added Successfully! (ID: " + nextId + ")");
            dashboard.refreshData();
            dispose();
        } 
        catch (NumberFormatException ex) 
        {
            JOptionPane.showMessageDialog(this, "Please enter valid values.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}

