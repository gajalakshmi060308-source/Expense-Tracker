
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class FinoraHero extends JFrame {

    // Theme colors
    private final Color darkGreen = new Color(0, 91, 72);
    private final Color buttonGreen = new Color(0, 115, 88);
    private final Color mintGreen = new Color(211, 245, 232);
    private final Color darkText = new Color(0, 48, 43);

    public FinoraHero() {
        setTitle("Finora | Personal Finance Tracker");
        setSize(1366, 760);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 0));

        // Main background
        getContentPane().setBackground(Color.WHITE);

        // Add navigation bar
        add(createNavigationBar(), BorderLayout.NORTH);

        // Main center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(
                new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(
                new EmptyBorder(10, 18, 8, 18));

        // Add Hero Section
        JPanel heroPanel = createHeroSection();
        heroPanel.setPreferredSize(new Dimension(1300, 480));
        heroPanel.setMinimumSize(new Dimension(950, 450));
        heroPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 480));
        heroPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(heroPanel);

        // Space between Hero and Key Features
        centerPanel.add(Box.createVerticalStrut(20));

        // Add Key Features Section
        JPanel featuresPanel = createKeyFeaturesSection();
        featuresPanel.setPreferredSize(new Dimension(1300, 360));
        featuresPanel.setMinimumSize(new Dimension(950, 360));
        featuresPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 360));
        featuresPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(featuresPanel);

        // Space between Key Features and Why Choose Finora
        centerPanel.add(Box.createVerticalStrut(20));

        // Add Why Choose Finora and How It Works Section
        JPanel whyAndHowPanel = createWhyAndHowSection();
        whyAndHowPanel.setPreferredSize(new Dimension(1300, 360));
        whyAndHowPanel.setMinimumSize(new Dimension(950, 360));
        whyAndHowPanel.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 360));
        whyAndHowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(whyAndHowPanel);

        // Add scrolling to view all sections
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);

        add(scrollPane, BorderLayout.CENTER);
    }

    // ---------------- NAVIGATION BAR ----------------

    private JPanel createNavigationBar() {

        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(Color.WHITE);
        navbar.setPreferredSize(new Dimension(100, 82));
        navbar.setBorder(new EmptyBorder(5, 22, 5, 25));

        // Logo image
        ImageIcon logoIcon = loadImage("images/logo.png.jpeg");

        JLabel logoLabel;

        if (logoIcon != null) {
            Image logoImage = logoIcon.getImage().getScaledInstance(
                    200, 120, Image.SCALE_SMOOTH);

            logoLabel = new JLabel(new ImageIcon(logoImage));
        } else {
            logoLabel = new JLabel("Finora");
            logoLabel.setFont(
                    new Font("Times New Roman", Font.BOLD, 30));
            logoLabel.setForeground(darkGreen);

            System.out.println(
                    "Logo not found: images/logo.png.jpeg");
        }

        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        navbar.add(logoLabel, BorderLayout.WEST);

        // Login and Register buttons
        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 14, 14));

        buttonPanel.setBackground(Color.WHITE);

        JButton loginButton = createButton("Login", false);
        JButton registerButton = createButton("Register", true);

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        navbar.add(buttonPanel, BorderLayout.EAST);

        // Button actions
        // CHANGED: Open the login page
        loginButton.addActionListener(e -> {
            new FinoraLogin().setVisible(true);
        });

        // Register action unchanged
        registerButton.addActionListener(e -> {
            new FinoraRegistration().setVisible(true);
        });

        return navbar;
    }

    // ---------------- BUTTON DESIGN ----------------

    private JButton createButton(String text, boolean filled) {

        JButton button = new JButton(text);

        button.setFont(new Font("Times New Roman", Font.BOLD, 17));
        button.setPreferredSize(new Dimension(132, 40));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (filled) {
            button.setBackground(buttonGreen);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createLineBorder(
                    buttonGreen, 1));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(darkGreen);
            button.setBorder(BorderFactory.createLineBorder(
                    buttonGreen, 2));
        }

        return button;
    }

    // ---------------- HERO SECTION ----------------

    private JPanel createHeroSection() {

        JPanel heroPanel = new JPanel(new GridBagLayout());
        heroPanel.setBackground(mintGreen);
        heroPanel.setBorder(new EmptyBorder(18, 28, 18, 28));

        GridBagConstraints gbc = new GridBagConstraints();

        // LEFT SIDE: INTRODUCTION
        JPanel textPanel = createIntroduction();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.47;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(60, 5, 0, 10);

        heroPanel.add(textPanel, gbc);

        // RIGHT SIDE: HERO IMAGE
        JPanel imagePanel = createHeroImagePanel();

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.53;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 60, 80);

        heroPanel.add(imagePanel, gbc);

        return heroPanel;
    }

    // ---------------- INTRODUCTION CONTENT ----------------

    private JPanel createIntroduction() {

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(mintGreen);

        // Main heading
        JLabel heading1 = new JLabel("Take Control of Your");
        heading1.setFont(
                new Font("Times New Roman", Font.BOLD, 39));
        heading1.setForeground(darkText);
        heading1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading2 = new JLabel("Financial Future");
        heading2.setFont(
                new Font("Times New Roman", Font.BOLD, 39));
        heading2.setForeground(buttonGreen);
        heading2.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description
        JTextArea description = new JTextArea(
                "Track your income, manage your expenses, " +
                "set budgets and achieve your savings goals " +
                "— all in one place."
        );

        description.setFont(
                new Font("Times New Roman", Font.BOLD, 19));
        description.setForeground(darkText);
        description.setBackground(mintGreen);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setFocusable(false);
        description.setOpaque(false);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setMaximumSize(new Dimension(490, 100));

        // Login and Register buttons
        JPanel actionPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 0));

        actionPanel.setBackground(mintGreen);
        actionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginButton = createButton("Login", true);
        JButton registerButton = createButton("Register", false);

        actionPanel.add(loginButton);
        actionPanel.add(registerButton);

        // CHANGED: Open the login page
        loginButton.addActionListener(e -> {
            new FinoraLogin().setVisible(true);
        });

        // Register action unchanged
        registerButton.addActionListener(e -> {
            new FinoraRegistration().setVisible(true);
        });

        textPanel.add(heading1);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(heading2);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(description);
        textPanel.add(Box.createVerticalStrut(25));
        textPanel.add(actionPanel);

        return textPanel;
    }

    // ---------------- HERO IMAGE ----------------

    private JPanel createHeroImagePanel() {

        JPanel imagePanel = new JPanel(new GridBagLayout());
        imagePanel.setBackground(mintGreen);

        // Remove near-black background from the illustration
        BufferedImage heroImage =
                loadImageWithoutBlackBackground("images/hero.png.jpeg");

        if (heroImage != null) {

            Image scaledImage = heroImage.getScaledInstance(
                    600, 450, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
            imageLabel.setOpaque(false);

            imagePanel.add(imageLabel);

        } else {

            JLabel errorLabel = new JLabel(
                    "<html><center>Hero image not found.<br>" +
                    "Check images/hero.png.jpeg</center></html>");

            errorLabel.setFont(
                    new Font("Times New Roman", Font.PLAIN, 17));
            errorLabel.setForeground(darkText);

            imagePanel.add(errorLabel);

            System.out.println(
                    "Hero image not found: images/hero.png.jpeg");
        }

        return imagePanel;
    }

    // ---------------- LOAD NORMAL IMAGE ----------------

    private ImageIcon loadImage(String path) {

        File file = new File(path);

        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }

        return null;
    }

    // ---------------- REMOVE BLACK BACKGROUND ----------------

    private BufferedImage loadImageWithoutBlackBackground(
            String path) {

        try {

            File file = new File(path);

            if (!file.exists()) {
                return null;
            }

            BufferedImage original = ImageIO.read(file);

            if (original == null) {
                return null;
            }

            BufferedImage transparentImage = new BufferedImage(
                    original.getWidth(),
                    original.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
            );

            for (int y = 0; y < original.getHeight(); y++) {

                for (int x = 0; x < original.getWidth(); x++) {

                    int pixel = original.getRGB(x, y);

                    int alpha = (pixel >> 24) & 0xff;
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;

                    // Make only very dark pixels transparent
                    if (red < 18 && green < 18 && blue < 18) {
                        alpha = 0;
                    }

                    int newPixel =
                            (alpha << 24) |
                            (red << 16) |
                            (green << 8) |
                            blue;

                    transparentImage.setRGB(x, y, newPixel);
                }
            }

            return transparentImage;

        } catch (Exception e) {

            System.out.println(
                    "Error loading hero image: " + e.getMessage());

            return null;
        }
    }

    // ---------------- KEY FEATURES SECTION ----------------

    private JPanel createKeyFeaturesSection() {

        JPanel featuresPanel = new JPanel(
                new BorderLayout(0, 16));

        featuresPanel.setBackground(Color.WHITE);
        featuresPanel.setBorder(
                new EmptyBorder(12, 2, 20, 2));

        // Heading and subtitle
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(
                headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Key Features");
        heading.setFont(
                new Font("Times New Roman", Font.BOLD, 30));
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Everything you need to manage your finances, all in one place."
        );

        subtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        subtitle.setForeground(darkText);
        subtitle.setBorder(
                new EmptyBorder(6, 0, 0, 0));
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headingPanel.add(heading);
        headingPanel.add(subtitle);

        featuresPanel.add(headingPanel, BorderLayout.NORTH);

        // Three cards in each row
        JPanel cardsPanel = new JPanel(
                new GridLayout(2, 3, 18, 18));

        cardsPanel.setBackground(Color.WHITE);

        cardsPanel.add(createFeatureCard(
                "↕",
                "Income & Expense Tracking",
                "Add and manage your income and expenses easily."
        ));

        cardsPanel.add(createFeatureCard(
                "₹",
                "Budget Management",
                "Set category-wise budgets and track your spending."
        ));

        cardsPanel.add(createFeatureCard(
                "★",
                "Savings Goals",
                "Create savings goals and monitor your progress."
        ));

        cardsPanel.add(createFeatureCard(
                "▤",
                "Reports & Analysis",
                "Understand your finances with detailed reports and charts."
        ));

        cardsPanel.add(createFeatureCard(
                "↻",
                "Transaction History",
                "View, search and filter all your transactions."
        ));

        cardsPanel.add(createFeatureCard(
                "⚙",
                "Profile Settings",
                "Update your profile, change your password and more."
        ));

        featuresPanel.add(cardsPanel, BorderLayout.CENTER);

        return featuresPanel;
    }

    // ---------------- INDIVIDUAL FEATURE CARD ----------------

    private JPanel createFeatureCard(
            String symbol,
            String title,
            String description) {

        JPanel card = new JPanel(
                new BorderLayout(16, 0));

        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 230, 216), 1),
                new EmptyBorder(18, 16, 18, 16)
        ));

        // Icon
        JLabel icon = new JLabel(
                symbol, SwingConstants.CENTER);

        icon.setFont(
                new Font("Segoe UI Symbol", Font.PLAIN, 32));
        icon.setForeground(buttonGreen);
        icon.setOpaque(true);
        icon.setBackground(mintGreen);
        icon.setPreferredSize(new Dimension(62, 62));

        JPanel iconPanel = new JPanel(
                new GridBagLayout());

        iconPanel.setBackground(Color.WHITE);
        iconPanel.add(icon);

        // Title and description
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(
                textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 19));
        titleLabel.setForeground(darkText);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea descriptionLabel = new JTextArea(description);
        descriptionLabel.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        descriptionLabel.setForeground(darkText);
        descriptionLabel.setBackground(Color.WHITE);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setEditable(false);
        descriptionLabel.setFocusable(false);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setBorder(
                new EmptyBorder(9, 0, 0, 0));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        textPanel.add(titleLabel);
        textPanel.add(descriptionLabel);

        card.add(iconPanel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    // ---------------- WHY CHOOSE FINORA + HOW IT WORKS ----------------

    private JPanel createWhyAndHowSection() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(5, 0, 10, 0));

        // ========== WHY CHOOSE FINORA ==========

        JPanel whyPanel = new JPanel(new BorderLayout(0, 18));
        whyPanel.setBackground(Color.WHITE);
        whyPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 230, 216), 1),
                new EmptyBorder(18, 15, 18, 15)));

        JPanel whyHeader = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0));
        whyHeader.setBackground(Color.WHITE);
        whyHeader.add(new DrawnIcon("star", 38, buttonGreen));

        JLabel whyTitle = new JLabel("Why Choose Finora?");
        whyTitle.setFont(new Font("Times New Roman", Font.BOLD, 30));
        whyTitle.setForeground(darkGreen);
        whyHeader.add(whyTitle);

        JLabel whySubtitle = new JLabel(
                "Your personal finance partner for a smarter tomorrow.",
                SwingConstants.CENTER);
        whySubtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        whySubtitle.setForeground(darkText);

        JPanel whySubtitlePanel = new JPanel(new BorderLayout());
        whySubtitlePanel.setBackground(Color.WHITE);
        whySubtitlePanel.add(whySubtitle, BorderLayout.CENTER);

        // White benefits area with dashed separators
        JPanel benefitsPanel = new JPanel(new GridLayout(1, 7, 0, 0));
        benefitsPanel.setBackground(Color.WHITE);
        benefitsPanel.setBorder(new EmptyBorder(14, 2, 10, 2));
        benefitsPanel.setPreferredSize(new Dimension(0, 175));

        benefitsPanel.add(createBenefit("shield", "Secure &", "Private"));
        benefitsPanel.add(createDashedSeparator());
        benefitsPanel.add(createBenefit("clock", "Simple &", "Easy to Use"));
        benefitsPanel.add(createDashedSeparator());
        benefitsPanel.add(createBenefit("calendar", "Stay", "Organized"));
        benefitsPanel.add(createDashedSeparator());
        benefitsPanel.add(createBenefit("chart", "Build", "Better Habits"));

        JPanel whyBody = new JPanel(new BorderLayout(0, 16));
        whyBody.setBackground(Color.WHITE);
        whyBody.add(whySubtitlePanel, BorderLayout.NORTH);
        whyBody.add(benefitsPanel, BorderLayout.CENTER);

        whyPanel.add(whyHeader, BorderLayout.NORTH);
        whyPanel.add(whyBody, BorderLayout.CENTER);

        // ========== HOW IT WORKS ==========

        JPanel howPanel = new JPanel(new BorderLayout(0, 18));
        howPanel.setBackground(Color.WHITE);
        howPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(190, 230, 216), 1),
                new EmptyBorder(18, 15, 18, 15)));

        JPanel howHeader = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0));
        howHeader.setBackground(Color.WHITE);
        howHeader.add(new DrawnIcon("bulb", 38, buttonGreen));

        JLabel howTitle = new JLabel("How It Works");
        howTitle.setFont(new Font("Times New Roman", Font.BOLD, 30));
        howTitle.setForeground(darkGreen);
        howHeader.add(howTitle);

        JLabel howSubtitle = new JLabel(
                "Get started in just a few simple steps.",
                SwingConstants.CENTER);
        howSubtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        howSubtitle.setForeground(darkText);

        JPanel howSubtitlePanel = new JPanel(new BorderLayout());
        howSubtitlePanel.setBackground(Color.WHITE);
        howSubtitlePanel.add(howSubtitle, BorderLayout.CENTER);

        JPanel stepsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        stepsPanel.setBackground(Color.WHITE);
        stepsPanel.setBorder(new EmptyBorder(5, 2, 2, 2));

        stepsPanel.add(createStep(
                "user", "1", "Create Your Account",
                "Sign up and set up your profile."));
        stepsPanel.add(createStep(
                "wallet", "2", "Track Your Transactions",
                "Add income and expenses, set budgets."));
        stepsPanel.add(createStep(
                "target", "3", "Achieve Your Goals",
                "Monitor progress and build your savings."));

        JPanel howBody = new JPanel(new BorderLayout(0, 15));
        howBody.setBackground(Color.WHITE);
        howBody.add(howSubtitlePanel, BorderLayout.NORTH);
        howBody.add(stepsPanel, BorderLayout.CENTER);

        howPanel.add(howHeader, BorderLayout.NORTH);
        howPanel.add(howBody, BorderLayout.CENTER);

        mainPanel.add(whyPanel);
        mainPanel.add(howPanel);
        return mainPanel;
    }

    // ---------------- DASHED SEPARATOR ----------------

    private JPanel createDashedSeparator() {
        JPanel separator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(150, 195, 178));
                g2.setStroke(new BasicStroke(
                        1.4f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_ROUND, 1f,
                        new float[]{3f, 5f}, 0f));
                int x = getWidth() / 2;
                g2.drawLine(x, 12, x, getHeight() - 12);
                g2.dispose();
            }
        };
        separator.setBackground(Color.WHITE);
        separator.setOpaque(true);
        return separator;
    }

    // ---------------- WHY CHOOSE FINORA BENEFIT CARD ----------------

    private JPanel createBenefit(
            String iconType, String line1, String line2) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        DrawnIcon icon = new DrawnIcon(iconType, 58, buttonGreen);
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(
                "<html><div style='text-align:center'>"
                + line1 + "<br>" + line2 + "</div></html>",
                SwingConstants.CENTER);
        label.setFont(new Font("Times New Roman", Font.BOLD, 17));
        label.setForeground(darkText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(Box.createVerticalGlue());
        panel.add(icon);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    // ---------------- HOW IT WORKS STEP CARD ----------------

    private JPanel createStep(
            String iconType,
            String number,
            String title,
            String description) {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Icon
        DrawnIcon icon = new DrawnIcon(iconType, 58, buttonGreen);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(icon, gbc);

        // Number badge
        JLabel numberLabel = new JLabel(number, SwingConstants.CENTER);
        numberLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 16));
        numberLabel.setForeground(Color.WHITE);
        numberLabel.setOpaque(true);
        numberLabel.setBackground(darkGreen);
        numberLabel.setPreferredSize(new Dimension(32, 26));
        numberLabel.setMinimumSize(new Dimension(32, 26));
        numberLabel.setMaximumSize(new Dimension(32, 26));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 9, 0);
        panel.add(numberLabel, gbc);

        // Explicit line breaks keep headings fully visible
        String wrappedTitle;

        if ("Create Your Account".equals(title)) {
            wrappedTitle = "Create Your<br>Account";
        } else if ("Track Your Transactions".equals(title)) {
            wrappedTitle = "Track Your<br>Transactions";
        } else if ("Achieve Your Goals".equals(title)) {
            wrappedTitle = "Achieve Your<br>Goals";
        } else {
            wrappedTitle = title;
        }

        JLabel titleLabel = new JLabel(
                "<html><div style='text-align:center'>"
                + wrappedTitle + "</div></html>",
                SwingConstants.CENTER);
        titleLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 20));
        titleLabel.setForeground(darkGreen);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(180, 52));

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 6, 2);
        panel.add(titleLabel, gbc);

        // Descriptions wrap to avoid clipping
        String wrappedDescription;

        if ("Sign up and set up your profile.".equals(description)) {
            wrappedDescription = "Sign up and set up<br>your profile.";
        } else if ("Add income and expenses, set budgets."
                .equals(description)) {
            wrappedDescription = "Add income and expenses,<br>set budgets.";
        } else if ("Monitor progress and build your savings."
                .equals(description)) {
            wrappedDescription = "Monitor progress and<br>build your savings.";
        } else {
            wrappedDescription = description;
        }

        JLabel descriptionLabel = new JLabel(
                "<html><div style='text-align:center'>"
                + wrappedDescription + "</div></html>",
                SwingConstants.CENTER);
        descriptionLabel.setFont(
                new Font("Times New Roman", Font.PLAIN, 16));
        descriptionLabel.setForeground(darkText);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setPreferredSize(new Dimension(180, 48));

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 2, 0, 2);
        panel.add(descriptionLabel, gbc);

        return panel;
    }

    // ---------------- CUSTOM DRAWN ICONS ----------------

    private class DrawnIcon extends JPanel {

        private final String type;
        private final Color iconColor;

        DrawnIcon(String type, int size, Color iconColor) {
            this.type = type;
            this.iconColor = iconColor;

            setPreferredSize(new Dimension(size, size));
            setMinimumSize(new Dimension(size, size));
            setMaximumSize(new Dimension(size, size));
            setOpaque(true);
            setBackground(mintGreen);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(new Color(177, 239, 208));
            g2.fillOval(2, 2, w - 4, h - 4);

            g2.setColor(iconColor);
            g2.setStroke(new BasicStroke(
                    2.5f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));

            int cx = w / 2;
            int cy = h / 2;

            switch (type) {
                case "star":
                    Polygon star = new Polygon();
                    for (int i = 0; i < 10; i++) {
                        double angle = Math.PI * i / 5 - Math.PI / 2;
                        int radius = (i % 2 == 0) ? 14 : 7;
                        star.addPoint(
                                cx + (int)(radius * Math.cos(angle)),
                                cy + (int)(radius * Math.sin(angle)));
                    }
                    g2.fillPolygon(star);
                    break;

                case "shield":
                    Polygon shield = new Polygon(
                            new int[]{cx, cx + 13, cx + 11, cx,
                                      cx - 11, cx - 13},
                            new int[]{cy - 17, cy - 11, cy + 5,
                                      cy + 17, cy + 5, cy - 11},
                            6);
                    g2.drawPolygon(shield);
                    g2.drawLine(cx - 7, cy, cx - 1, cy + 6);
                    g2.drawLine(cx - 1, cy + 6, cx + 8, cy - 6);
                    break;

                case "clock":
                    g2.drawOval(cx - 14, cy - 14, 28, 28);
                    g2.drawLine(cx, cy, cx, cy - 9);
                    g2.drawLine(cx, cy, cx + 8, cy + 5);
                    break;

                case "calendar":
                    g2.drawRoundRect(
                            cx - 14, cy - 12, 28, 26, 4, 4);
                    g2.drawLine(cx - 14, cy - 4, cx + 14, cy - 4);
                    g2.drawLine(cx - 7, cy - 17, cx - 7, cy - 8);
                    g2.drawLine(cx + 7, cy - 17, cx + 7, cy - 8);

                    for (int row = 0; row < 2; row++) {
                        for (int col = 0; col < 3; col++) {
                            g2.fillOval(
                                    cx - 8 + col * 8,
                                    cy + row * 7, 3, 3);
                        }
                    }
                    break;

                case "chart":
                    g2.drawLine(cx - 15, cy + 14, cx + 15, cy + 14);
                    g2.fillRoundRect(cx - 12, cy + 1, 6, 13, 2, 2);
                    g2.fillRoundRect(cx - 3, cy - 8, 6, 22, 2, 2);
                    g2.fillRoundRect(cx + 6, cy - 16, 6, 30, 2, 2);
                    break;

                case "bulb":
                    g2.drawOval(cx - 10, cy - 15, 20, 21);
                    g2.drawLine(cx - 6, cy + 6, cx - 4, cy + 12);
                    g2.drawLine(cx + 6, cy + 6, cx + 4, cy + 12);
                    g2.drawLine(cx - 4, cy + 12, cx + 4, cy + 12);
                    g2.drawLine(cx - 3, cy + 16, cx + 3, cy + 16);
                    break;

                case "user":
                    g2.fillOval(cx - 7, cy - 16, 14, 14);
                    g2.fillRoundRect(
                            cx - 13, cy + 1, 26, 17, 8, 8);
                    break;

                case "wallet":
                    g2.fillRoundRect(
                            cx - 15, cy - 10, 30, 24, 5, 5);
                    g2.setColor(mintGreen);
                    g2.fillRoundRect(
                            cx + 1, cy - 4, 14, 12, 3, 3);
                    g2.setColor(iconColor);
                    g2.drawRoundRect(
                            cx + 1, cy - 4, 14, 12, 3, 3);
                    g2.fillOval(cx + 5, cy, 3, 3);
                    break;

                case "target":
                    g2.drawOval(cx - 15, cy - 15, 30, 30);
                    g2.drawOval(cx - 9, cy - 9, 18, 18);
                    g2.drawOval(cx - 3, cy - 3, 6, 6);
                    g2.drawLine(cx + 1, cy - 1, cx + 16, cy - 16);
                    g2.drawLine(cx + 9, cy - 16, cx + 16, cy - 16);
                    g2.drawLine(cx + 16, cy - 16, cx + 16, cy - 9);
                    break;
            }

            g2.dispose();
        }
    }

    // ---------------- MAIN METHOD ----------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FinoraHero app = new FinoraHero();
            app.setVisible(true);
        });
    }
}
