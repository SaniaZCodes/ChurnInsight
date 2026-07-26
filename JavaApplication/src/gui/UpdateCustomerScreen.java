/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package gui;

import database.CustomerDAO;
import model.Customer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UpdateCustomerScreen extends JFrame 
{

    private final Color DARK_NAVY = new Color(15, 23, 42);
    private final Color SOFT_PURPLE = new Color(124, 58, 237);
    private final Color BASIC_COLOR = new Color(37, 99, 235);
    private final Color STANDARD_COLOR = new Color(22, 163, 74);
    private final Color PREMIUM_COLOR = new Color(168, 85, 247);
    private final Color CARD_BG = new Color(255, 255, 255, 230);
    private final Color SELECTED_GLOW = new Color(124, 58, 237, 80);

    private JTextField idField;
    private JTextField phoneField;
    private JRadioButton maleRadio, femaleRadio;
    private JComboBox<Integer> tenureCombo;
    private JPanel detailPanel;
    private JLabel totalLabel;
    private JPanel[] packageCards = new JPanel[3];
    private int selectedPackageIdx = 0;
    private boolean customerLoaded = false;
    private int loadedCustomerId = -1;

    private MainFrame dashboard;
    private CustomerDAO dao;

    // Package definitions
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

    /**
     * Maps a customer's internet+contract+monthly to a package index.
     */
    private static int detectPackageIndex(String internet, String contract, double monthly)
    {
        for (int i = 0; i < PACKAGES.length; i++)
        {
            String[] pkg = PACKAGES[i];
            boolean internetMatch = pkg[1].equalsIgnoreCase(internet) || 
                                    (internet.contains("No") && pkg[1].contains("No"));
            boolean contractMatch = pkg[8].equalsIgnoreCase(contract.trim());
            double pkgMonthly = Double.parseDouble(pkg[10]);
            boolean monthlyMatch = Math.abs(pkgMonthly - monthly) < 10;

            if (internetMatch && contractMatch && monthlyMatch) {
                return i;
            }
        }
        // Fallback: try matching by monthly charges only
        for (int i = 0; i < PACKAGES.length; i++)
        {
            double pkgMonthly = Double.parseDouble(PACKAGES[i][10]);
            if (Math.abs(pkgMonthly - monthly) < 10) return i;
        }
        return 0; // default Basic
    }

    public UpdateCustomerScreen(MainFrame dashboard, CustomerDAO dao)
    {
        this.dashboard = dashboard;
        this.dao = dao;

setTitle("ChurnInsight - Update Customer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1300, 800);
        setLocationRelativeTo(dashboard);
        setResizable(false);

        buildUI();
        setVisible(true);
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());

        // === Header ===
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
        headerPanel.setPreferredSize(new Dimension(1, 60));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 25));
        
        JLabel titleLabel = new JLabel("✏ Update Customer");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(new Color(200, 255, 200));
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);

        // === Main Content ===
        JPanel mainPanel = new JPanel(new BorderLayout())
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

        // === TOP: Customer ID + Load Button ===
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(12, 15, 5, 15));

        JLabel idLabel = new JLabel("🆔 Customer ID:");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        idLabel.setForeground(DARK_NAVY);

        idField = new JTextField(10);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 200, 220), 1),
            BorderFactory.createEmptyBorder(7, 12, 7, 12)));

        JButton loadBtn = createStyledButton("🔍 Load Customer", SOFT_PURPLE, 160, 38);

        topBar.add(idLabel);
        topBar.add(idField);
        topBar.add(loadBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // === CENTER SPLIT: Left Form + Right Package/Cards ===
        JPanel centerSplit = new JPanel(new GridLayout(1, 2, 15, 0));
        centerSplit.setOpaque(false);
        centerSplit.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));

        // --- Left: Customer Info Form ---
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
        gbc.insets = new Insets(4, 8, 4, 8);

        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        String[] yesNo = {"No", "Yes"};

        int row = 0;

        // Phone
        gbc.gridx = 0; gbc.gridy = row;
        JLabel phoneLbl = new JLabel("📞 Phone:");
        phoneLbl.setFont(labelFont);
        formPanel.add(phoneLbl, gbc);
        
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        phoneField.setForeground(Color.GRAY);
        phoneField.setText("Load a customer first");
        phoneField.setEnabled(false);
        phoneField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230), 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        // Gender
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel genderLbl = new JLabel("👤 Gender:");
        genderLbl.setFont(labelFont);
        formPanel.add(genderLbl, gbc);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setOpaque(false);
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        maleRadio.setFont(fieldFont);
        femaleRadio.setFont(fieldFont);
        maleRadio.setOpaque(false);
        femaleRadio.setOpaque(false);
        maleRadio.setEnabled(false);
        femaleRadio.setEnabled(false);
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        maleRadio.setSelected(true);
        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        gbc.gridx = 1;
        formPanel.add(genderPanel, gbc);

        // Tenure
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel tenLbl = new JLabel("📅 Tenure (months):");
        tenLbl.setFont(labelFont);
        formPanel.add(tenLbl, gbc);
        tenureCombo = new JComboBox<>();
        for (int i = 1; i <= 120; i++) tenureCombo.addItem(i);
        tenureCombo.setFont(fieldFont);
        tenureCombo.setEnabled(false);
        tenureCombo.addActionListener(e -> { if (customerLoaded) updatePackageDetails(); });
        gbc.gridx = 1;
        formPanel.add(tenureCombo, gbc);

        row++;
        gbc.gridy = row;

        // --- Right: Package Section ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);

        // Package Cards on top of right panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 8, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 5, 0));

        Color[] cardColors = {BASIC_COLOR, STANDARD_COLOR, PREMIUM_COLOR};
        for (int i = 0; i < 3; i++) {
            packageCards[i] = createPackageCard(i, cardColors[i]);
            cardsPanel.add(packageCards[i]);
            final int idx = i;
            packageCards[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (customerLoaded) selectPackage(idx);
                    else JOptionPane.showMessageDialog(UpdateCustomerScreen.this,
                        "Please load a customer first.", "No Customer Loaded", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }

        rightPanel.add(cardsPanel, BorderLayout.NORTH);

        // Package Details below cards
        JPanel detailWrapper = new JPanel(new BorderLayout());
        detailWrapper.setOpaque(false);
        detailWrapper.setBorder(BorderFactory.createTitledBorder(
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
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Show placeholder
        JLabel placeholderLbl = new JLabel("Load a customer to see details", SwingConstants.CENTER);
        placeholderLbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        placeholderLbl.setForeground(Color.GRAY);
        detailPanel.add(placeholderLbl);

        JScrollPane detailScroll = new JScrollPane(detailPanel);
        detailScroll.setBorder(null);
        detailScroll.setOpaque(false);
        detailScroll.getViewport().setOpaque(false);

        detailWrapper.add(detailScroll, BorderLayout.CENTER);
        rightPanel.add(detailWrapper, BorderLayout.CENTER);

        centerSplit.add(formPanel);
        centerSplit.add(rightPanel);

        mainPanel.add(centerSplit, BorderLayout.CENTER);

        // === Bottom ===
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 12, 15));

        totalLabel = new JLabel("Total Charges: Rs. 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(DARK_NAVY);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnPanel.setOpaque(false);

        JButton updateBtn = createStyledButton("✏ Update Customer", SOFT_PURPLE, 190, 42);
        JButton backBtn = createStyledButton("◀ Back To Dashboard", DARK_NAVY, 210, 42);

        btnPanel.add(updateBtn);
        btnPanel.add(backBtn);

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Actions
        loadBtn.addActionListener(e -> loadCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        backBtn.addActionListener(e -> dispose());
    }

    /**
     * Loads customer from database and populates fields.
     */
    private void loadCustomer()
    {
        try
        {
            int id = Integer.parseInt(idField.getText().trim());
            ArrayList<Customer> all = dao.getAllCustomers();
            Customer found = null;
            for (Customer c : all)
            {
                if (c.getCustomerId() == id)
                {
                    found = c;
                    break;
                }
            }

            if (found == null)
            {
                JOptionPane.showMessageDialog(this,
                    "Customer ID not found.",
                    "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loadedCustomerId = id;
            customerLoaded = true;

            // Populate phone number from database
            String phone = found.getPhoneNumber();
            if (phone != null && !phone.isEmpty()) {
                phoneField.setText(phone);
            } else {
                phoneField.setText("N/A");
            }
            phoneField.setEnabled(false);
            phoneField.setForeground(Color.BLACK);
            phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            // Populate gender
            if ("Male".equalsIgnoreCase(found.getGender()))
                maleRadio.setSelected(true);
            else
                femaleRadio.setSelected(true);
            maleRadio.setEnabled(true);
            femaleRadio.setEnabled(true);

            // Populate tenure
            tenureCombo.setSelectedItem(found.getTenure());
            tenureCombo.setEnabled(true);

            // Detect current package
            int pkgIdx = detectPackageIndex(
                found.getInternetService(),
                found.getContractType(),
                found.getMonthlyCharges()
            );

            // Select the package card
            selectPackage(pkgIdx);

            detailPanel.removeAll();

            JOptionPane.showMessageDialog(this,
                "✅ Customer loaded successfully!\n\n" +
                "Customer ID: " + id + "\n" +
                "Gender: " + found.getGender() + "\n" +
                "Tenure: " + found.getTenure() + " months\n" +
                "Current Package: " + PKG_SHORT_NAMES[pkgIdx],
                "Customer Loaded", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this,
                "Please enter a valid Customer ID.",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a clickable package card.
     */
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
        card.setPreferredSize(new Dimension(180, 150));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.fill = GridBagConstraints.HORIZONTAL;
        cgbc.insets = new Insets(2, 0, 2, 0);
        cgbc.gridx = 0;

        JLabel nameLbl = new JLabel(PKG_SHORT_NAMES[index], SwingConstants.CENTER);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setForeground(accentColor);
        cgbc.gridy = 0;
        content.add(nameLbl, cgbc);

        JLabel priceLbl = new JLabel(PKG_PRICES[index], SwingConstants.CENTER);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLbl.setForeground(DARK_NAVY);
        cgbc.gridy = 1;
        content.add(priceLbl, cgbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 225, 240));
        cgbc.gridy = 2;
        cgbc.insets = new Insets(4, 15, 4, 15);
        content.add(sep, cgbc);
        cgbc.insets = new Insets(1, 0, 1, 0);

        String[] pkg = PACKAGES[index];
        int inclCount = 0;
        for (int i = 2; i <= 7; i++) {
            if ("Yes".equals(pkg[i])) inclCount++;
        }
        JLabel featLbl = new JLabel(inclCount + " premium features", SwingConstants.CENTER);
        featLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        featLbl.setForeground(new Color(100, 110, 130));
        cgbc.gridy = 3;
        content.add(featLbl, cgbc);

        JLabel internetLbl = new JLabel("📶 " + pkg[1], SwingConstants.CENTER);
        internetLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        internetLbl.setForeground(new Color(80, 90, 110));
        cgbc.gridy = 4;
        content.add(internetLbl, cgbc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /**
     * Highlights the selected card and updates package details.
     */
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
            newCard.setPreferredSize(new Dimension(180, 150));
            newCard.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JPanel content = new JPanel(new GridBagLayout());
            content.setOpaque(false);
            content.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
            GridBagConstraints cgbc = new GridBagConstraints();
            cgbc.fill = GridBagConstraints.HORIZONTAL;
            cgbc.insets = new Insets(2, 0, 2, 0);
            cgbc.gridx = 0;

            JLabel nameLbl = new JLabel(PKG_SHORT_NAMES[cardIdx], SwingConstants.CENTER);
            nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            nameLbl.setForeground(accent);
            cgbc.gridy = 0;
            content.add(nameLbl, cgbc);

            JLabel priceLbl = new JLabel(PKG_PRICES[cardIdx], SwingConstants.CENTER);
            priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            priceLbl.setForeground(DARK_NAVY);
            cgbc.gridy = 1;
            content.add(priceLbl, cgbc);

            JSeparator sep = new JSeparator();
            sep.setForeground(new Color(220, 225, 240));
            cgbc.gridy = 2;
            cgbc.insets = new Insets(4, 15, 4, 15);
            content.add(sep, cgbc);
            cgbc.insets = new Insets(1, 0, 1, 0);

            String[] pkg = PACKAGES[cardIdx];
            int inclCount = 0;
            for (int j = 2; j <= 7; j++) {
                if ("Yes".equals(pkg[j])) inclCount++;
            }
            JLabel featLbl = new JLabel(inclCount + " premium features", SwingConstants.CENTER);
            featLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            featLbl.setForeground(new Color(100, 110, 130));
            cgbc.gridy = 3;
            content.add(featLbl, cgbc);

            JLabel internetLbl = new JLabel("📶 " + pkg[1], SwingConstants.CENTER);
            internetLbl.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            internetLbl.setForeground(new Color(80, 90, 110));
            cgbc.gridy = 4;
            content.add(internetLbl, cgbc);

            newCard.add(content, BorderLayout.CENTER);

            Container parent = card.getParent();
            if (parent != null)
            {
                parent.remove(card);
                parent.add(newCard, cardIdx);
                final int fIdx = cardIdx;
                newCard.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (customerLoaded) selectPackage(fIdx);
                        else JOptionPane.showMessageDialog(UpdateCustomerScreen.this,
                            "Please load a customer first.", "No Customer Loaded", JOptionPane.INFORMATION_MESSAGE);
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

    /**
     * Updates the package detail panel with feature list.
     */
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

        JLabel pkgNameLbl = new JLabel("📦 " + PKG_SHORT_NAMES[idx] + " Plan");
        pkgNameLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        pkgNameLbl.setForeground(pkgColor);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 8, 2, 8);
        detailPanel.add(pkgNameLbl, gbc);

        JLabel priceLbl = new JLabel(PKG_PRICES[idx]);
        priceLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        priceLbl.setForeground(pkgColor);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 8, 5, 8);
        detailPanel.add(priceLbl, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(200, 210, 230));
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        detailPanel.add(sep, gbc);
        gbc.insets = new Insets(3, 8, 3, 8);

        String[] values = {pkg[1], pkg[2], pkg[3], pkg[4], pkg[5], pkg[6], pkg[7], pkg[8], pkg[9]};
        Font detailFont = new Font("Segoe UI", Font.PLAIN, 12);

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
            valLbl.setFont(new Font("Segoe UI", included ? Font.BOLD : Font.PLAIN, 12));
            valLbl.setForeground(included ? new Color(22, 163, 74) : new Color(180, 50, 50));

            featureRow.add(featLbl, BorderLayout.CENTER);
            featureRow.add(valLbl, BorderLayout.EAST);

            detailPanel.add(featureRow, gbc);
        }

        double totalCharges = monthly * tenureVal;
        totalLabel.setText("Total Charges: Rs. " + String.format("%,.0f", totalCharges));

        detailPanel.revalidate();
        detailPanel.repaint();
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

    /**
     * Updates customer with current field values and package mapping.
     */
    private void updateCustomer()
    {
        if (!customerLoaded)
        {
            JOptionPane.showMessageDialog(this,
                "Please load a customer first.",
                "No Customer Loaded", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            String gender = maleRadio.isSelected() ? "Male" : "Female";
            int tenure = (Integer) tenureCombo.getSelectedItem();

            int idx = selectedPackageIdx;
            if (idx < 0) idx = 0;
            String[] pkg = PACKAGES[idx];

            String contract = pkg[8];
            String internet = pkg[1];
            double monthly = Double.parseDouble(pkg[10]);

            dao.updateCustomer(loadedCustomerId, gender, tenure, contract, internet, monthly);

            JOptionPane.showMessageDialog(this, "✅ Customer Updated Successfully!");
            dashboard.refreshData();
            dispose();
        }
        catch (NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(this,
                "Please enter valid values.",
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}

