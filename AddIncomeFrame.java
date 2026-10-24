
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.math.BigDecimal;


public class AddIncomeFrame extends JFrame {

    private final String fullName;
    private final String email;

    private final Color green = new Color(0, 91, 78);
    private final Color mint = new Color(231, 249, 243);
    private final Color paleMint = new Color(210, 244, 232);
    private final Color muted = new Color(91, 119, 132);
    private final Color fieldBorder = new Color(200, 224, 222);

    private JComboBox<String> sourceBox;
    private JTextField amountField;
    private JTextField dateField;
    private JTextArea descriptionArea;

   
public AddIncomeFrame(String fullName, String email) {
    this.fullName = fullName;
    this.email = email;

    setTitle("Finora | Add Income");
    setSize(1450, 900);
    setMinimumSize(new Dimension(1050, 720));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Main content: left introduction and right form
    JPanel content = new JPanel(new GridBagLayout());
    content.setBackground(mint);
    content.setBorder(new EmptyBorder(28, 42, 28, 42));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weighty = 1;
    gbc.insets = new Insets(0, 14, 0, 14);

    JPanel left = createLeftPanel();
    gbc.gridx = 0;
    gbc.weightx = 0.88;
    content.add(left, gbc);

    JPanel form = createFormPanel();
    gbc.gridx = 1;
    gbc.weightx = 1.12;
    content.add(form, gbc);

    // Navbar with logo, hamburger sidebar and profile dropdown
    Runnable logoutAction = () -> {
        dispose();
        new FinoraLogin().setVisible(true);
    };

    NavigationPanel navigation =
            new NavigationPanel(fullName, email, logoutAction);

    add(navigation, BorderLayout.NORTH);
    add(content, BorderLayout.CENTER);
}

    // ---------------- LEFT PANEL ----------------

private JPanel createLeftPanel() {
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(new EmptyBorder(18, 5, 10, 12));

    JLabel title = new JLabel("Add Income");
    title.setFont(new Font("Serif", Font.BOLD, 49));
    title.setForeground(green);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);

    JLabel subtitle1 = new JLabel("Record money received into");
    JLabel subtitle2 = new JLabel("your account.");

    subtitle1.setFont(new Font("Serif", Font.PLAIN, 25));
    subtitle2.setFont(new Font("Serif", Font.PLAIN, 25));
    subtitle1.setForeground(muted);
    subtitle2.setForeground(muted);

    subtitle1.setAlignmentX(Component.LEFT_ALIGNMENT);
    subtitle2.setAlignmentX(Component.LEFT_ALIGNMENT);

    panel.add(title);
    panel.add(Box.createVerticalStrut(10));
    panel.add(subtitle1);
    panel.add(subtitle2);
    panel.add(Box.createVerticalStrut(28));

    addBenefit(panel, "Track Your Income",
            "Keep a record of all your earnings.", "\u20B9");

    addBenefit(panel, "Plan Better",
            "Build a stronger financial future.", "\u25A0");

    addBenefit(panel, "Stay Organized",
            "Manage your money with ease.", "\u2713");

    addBenefit(panel, "Reach Your Goals",
            "Small steps lead to big dreams.", "\u2665");

    panel.add(Box.createVerticalGlue());

    WalletIllustration wallet = new WalletIllustration();
    wallet.setAlignmentX(Component.LEFT_ALIGNMENT);
    wallet.setPreferredSize(new Dimension(350, 180));
    wallet.setMaximumSize(new Dimension(420, 190));
    panel.add(wallet);

    return panel;
}


    private void addBenefit(
            JPanel parent, String heading,
            String description, String symbol) {

        JPanel row = new JPanel(new BorderLayout(17, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(520, 70));
        row.setPreferredSize(new Dimension(520, 70));

        JLabel icon = new JLabel(symbol, SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.BOLD, 25));
        icon.setForeground(green);
        icon.setOpaque(true);
        icon.setBackground(paleMint);
        icon.setPreferredSize(new Dimension(58, 58));

        JPanel words = new JPanel();
        words.setOpaque(false);
        words.setLayout(new BoxLayout(words, BoxLayout.Y_AXIS));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("Serif", Font.BOLD, 20));
        headingLabel.setForeground(green);

        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        descriptionLabel.setForeground(muted);

        words.add(Box.createVerticalStrut(3));
        words.add(headingLabel);
        words.add(Box.createVerticalStrut(5));
        words.add(descriptionLabel);

        row.add(icon, BorderLayout.WEST);
        row.add(words, BorderLayout.CENTER);

        parent.add(row);
        parent.add(Box.createVerticalStrut(12));
    }

    // ---------------- WALLET ILLUSTRATION ----------------

    private class WalletIllustration extends JPanel {

        WalletIllustration() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Soft background circles
            g.setColor(new Color(211, 245, 233));
            g.fillOval(25, 35, 210, 125);
            g.fillOval(85, 20, 170, 145);

            // Small decorative leaves
            g.setColor(new Color(100, 205, 170));
            g.fillOval(35, 120, 17, 8);
            g.fillOval(245, 105, 20, 9);
            g.fillOval(220, 135, 13, 7);

            // Plant stem
            g.setColor(new Color(0, 125, 93));
            g.setStroke(new BasicStroke(4));
            g.drawLine(145, 104, 145, 40);

            // Leaves
            g.setColor(new Color(42, 171, 132));
            g.fillOval(117, 44, 27, 13);
            g.fillOval(145, 59, 29, 14);
            g.fillOval(128, 25, 18, 29);

            // Coins
            g.setColor(new Color(229, 181, 62));
            g.fillRoundRect(62, 110, 64, 15, 7, 7);
            g.fillRoundRect(66, 99, 58, 15, 7, 7);
            g.fillRoundRect(70, 88, 50, 15, 7, 7);

            g.setColor(new Color(248, 213, 111));
            g.fillOval(70, 85, 50, 12);
            g.fillOval(66, 96, 58, 12);
            g.fillOval(62, 107, 64, 12);

            // Wallet body
            g.setColor(new Color(0, 110, 88));
            g.fillRoundRect(105, 95, 135, 68, 16, 16);

            g.setColor(new Color(28, 160, 125));
            g.fillRoundRect(112, 101, 118, 56, 12, 12);

            // Wallet flap
            g.setColor(new Color(0, 91, 78));
            g.fillRoundRect(188, 115, 60, 33, 9, 9);

            g.setColor(new Color(37, 179, 140));
            g.fillOval(213, 126, 10, 10);

            // Ground
            g.setColor(new Color(195, 239, 224));
            g.fillOval(28, 158, 250, 10);

            g.dispose();
        }
    }

    // ---------------- FORM PANEL ----------------

    private JPanel createFormPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(28, 35, 25, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 5, 0);

        int row = 0;

        JLabel title = new JLabel("Add Income");
        title.setFont(new Font("Serif", Font.BOLD, 40));
        title.setForeground(green);
        addRow(card, title, gbc, row++);

        JLabel subtitle = new JLabel(
                "Fill in the details below to record your income.");
        subtitle.setFont(new Font("Serif", Font.PLAIN, 17));
        subtitle.setForeground(muted);
        addRow(card, subtitle, gbc, row++);

        gbc.insets = new Insets(19, 0, 7, 0);
        addRow(card, makeLabel("Income Source"), gbc, row++);

        gbc.insets = new Insets(0, 0, 7, 0);

        sourceBox = new JComboBox<>(new String[]{
                "Salary", "Freelance", "Business",
                "Investment", "Gift", "Other"
        });
        styleComboBox(sourceBox);
        addRow(card, sourceBox, gbc, row++);

        gbc.insets = new Insets(13, 0, 7, 0);
        addRow(card, makeLabel("Amount (\u20B9)"), gbc, row++);

        gbc.insets = new Insets(0, 0, 7, 0);

        amountField = new JTextField();
        styleTextField(amountField, "Enter amount");
        addRow(card, amountField, gbc, row++);

        gbc.insets = new Insets(13, 0, 7, 0);
        addRow(card, makeLabel("Date"), gbc, row++);

        gbc.insets = new Insets(0, 0, 7, 0);

        dateField = new JTextField(LocalDate.now().toString());
        styleTextField(dateField, "YYYY-MM-DD");
        addRow(card, dateField, gbc, row++);

        gbc.insets = new Insets(13, 0, 7, 0);
        addRow(card, makeLabel("Description (Optional)"), gbc, row++);

        gbc.insets = new Insets(0, 0, 7, 0);

        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setFont(new Font("Serif", Font.PLAIN, 17));
        descriptionArea.setForeground(green);
        descriptionArea.setBackground(Color.WHITE);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(
                new EmptyBorder(12, 13, 12, 13));

        JScrollPane scroll = new JScrollPane(descriptionArea);
        scroll.setBorder(new RoundedBorder(fieldBorder, 10));
        scroll.setPreferredSize(new Dimension(400, 105));

        addRow(card, scroll, gbc, row++);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 8));
        buttons.setBackground(Color.WHITE);

        JButton cancelButton = new JButton("Cancel");
        JButton saveButton = new JButton("Save Income");

        styleButton(cancelButton, false);
        styleButton(saveButton, true);

        buttons.add(cancelButton);
        buttons.add(saveButton);

        gbc.insets = new Insets(13, 0, 0, 0);
        addRow(card, buttons, gbc, row);

        saveButton.addActionListener(e -> saveIncome());

        cancelButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(fullName, email).setVisible(true);
        });

        return card;
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, 17));
        label.setForeground(green);
        return label;
    }

    private void addRow(
            JPanel panel, Component component,
            GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        panel.add(component, gbc);
    }

    // ---------------- CUSTOM STYLING ----------------

    private void styleTextField(
            JTextField field, String tooltip) {

        field.setFont(new Font("Serif", Font.PLAIN, 17));
        field.setForeground(green);
        field.setBackground(Color.WHITE);
        field.setCaretColor(green);
        field.setToolTipText(tooltip);
        field.setBorder(new RoundedBorder(fieldBorder, 10));
        field.setPreferredSize(new Dimension(400, 49));
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setFont(new Font("Serif", Font.PLAIN, 17));
        box.setForeground(green);
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(400, 49));
        box.setBorder(new RoundedBorder(fieldBorder, 10));
    }

    private void styleButton(JButton button, boolean primary) {
        button.setFont(new Font("Serif", Font.BOLD, 17));
        button.setFocusPainted(false);
        button.setCursor(
                Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(
                new Dimension(primary ? 190 : 135, 48));

        if (primary) {
            button.setBackground(green);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
            button.setBorderPainted(false);
            button.setBorder(
                    new EmptyBorder(8, 16, 8, 16));
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(green);
            button.setBorder(new RoundedBorder(green, 10));
        }
    }

    private static class RoundedBorder extends AbstractBorder {

        private final Color color;
        private final int radius;

        RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(
                Component c, Graphics g,
                int x, int y, int width, int height) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(
                    x, y, width - 1, height - 1,
                    radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(10, 12, 10, 12);
        }
    }

    // ---------------- SAVE TO MYSQL ----------------

    private void saveIncome() {

        String source = (String) sourceBox.getSelectedItem();
        String amountText = amountField.getText().trim();
        String dateText = dateField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (amountText.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter the amount and date.");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountText);

            if (amount.compareTo(BigDecimal.ZERO) <= 0
                    || amount.scale() > 2) {
                JOptionPane.showMessageDialog(this,
                        "Enter a positive amount with up to 2 decimal places.");
                return;
            }

            LocalDate date = LocalDate.parse(dateText);

            String sql = """
                INSERT INTO incomes
                    (user_id, source, amount, income_date, description)
                SELECT user_id, ?, ?, ?, ?
                FROM users
                WHERE email = ?
                """;

            try (Connection con = DBConnection.getConnection()) {
                if (con == null) {
                    showDatabaseError();
                    return;
                }

                try (PreparedStatement ps =
                             con.prepareStatement(sql)) {

                    ps.setString(1, source);
                    ps.setBigDecimal(2, amount);
                    ps.setDate(3, Date.valueOf(date));
                    ps.setString(4, description);
                    ps.setString(5, email);

                    int rows = ps.executeUpdate();

                    if (rows == 1) {
                        JOptionPane.showMessageDialog(this,
                                "Income saved successfully!");

                        amountField.setText("");
                        dateField.setText(
                                LocalDate.now().toString());
                        descriptionArea.setText("");

                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Could not find your user account.");
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid amount.");

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Enter the date in YYYY-MM-DD format.");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Could not save income. Check the database error.");
        }
    }

    private void showDatabaseError() {
        JOptionPane.showMessageDialog(this,
                "Database connection failed. Check DBConnection.java.");
    }
}
