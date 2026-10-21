
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class FinoraRegistration extends JFrame {

    private final Color darkGreen = new Color(0, 91, 78);
    private final Color mint = new Color(226, 247, 240);
    private final Color textColor = new Color(65, 91, 105);

    private JTextField nameField, emailField, phoneField;
    private JPasswordField passwordField, confirmPasswordField;

    public FinoraRegistration() {
        setTitle("Finora | Create Account");
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 25, 0));
        content.setBackground(mint);
        content.setBorder(new EmptyBorder(35, 35, 35, 35));

        content.add(createWelcomePanel());
        content.add(createFormPanel());

        mainPanel.add(content, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 25, 15, 25));

        JLabel logo = new JLabel("🍃 Finora");
        logo.setFont(new Font("Serif", Font.BOLD, 30));
        logo.setForeground(darkGreen);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setBackground(Color.WHITE);

        JLabel loginText = new JLabel("Already have an account?");
        loginText.setForeground(textColor);

        JButton loginButton = new JButton("Login");
        styleOutlineButton(loginButton);
        loginButton.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this, "Connect this button to your login page."));

        right.add(loginText);
        right.add(loginButton);

        header.add(logo, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(mint);
        panel.setBorder(new EmptyBorder(25, 15, 20, 15));

        JLabel heading = new JLabel(
                "<html>Take Control<br>of Your Finances</html>");
        heading.setFont(new Font("Serif", Font.BOLD, 35));
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Track your expenses, set your goals,<br>"
                + "and build a better financial future with Finora.</html>");
        description.setFont(new Font("SansSerif", Font.PLAIN, 16));
        description.setForeground(textColor);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(20));
        panel.add(description);
        panel.add(Box.createVerticalStrut(35));

        addFeature(panel, "📊", "Track Expenses",
                "Know where your money goes");
        addFeature(panel, "🎯", "Set Goals",
                "Achieve your dreams step by step");
        addFeature(panel, "🛡", "Stay Secure",
                "Your data, always protected");
        addFeature(panel, "🍃", "Build Better Habits",
                "Small steps, big results");

        return panel;
    }

    private void addFeature(JPanel panel, String icon,
                            String title, String subtitle) {
        JPanel feature = new JPanel(new BorderLayout(12, 0));
        feature.setBackground(mint);
        feature.setAlignmentX(Component.LEFT_ALIGNMENT);
        feature.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65));
        feature.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 25));
        iconLabel.setPreferredSize(new Dimension(48, 45));

        JPanel labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        labels.setBackground(mint);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(darkGreen);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subLabel.setForeground(textColor);

        labels.add(titleLabel);
        labels.add(Box.createVerticalStrut(5));
        labels.add(subLabel);

        feature.add(iconLabel, BorderLayout.WEST);
        feature.add(labels, BorderLayout.CENTER);

        panel.add(feature);
        panel.add(Box.createVerticalStrut(8));
    }

    private JPanel createLabeledField(String labelText, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 63));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Times New Roman", Font.BOLD, 16));
        label.setForeground(darkGreen);
        label.setPreferredSize(new Dimension(145, 45));

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(213, 236, 230), 1),
                new EmptyBorder(25, 35, 25, 35)));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Your Account");
        heading.setFont(new Font("Serif", Font.BOLD, 29));
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Start your journey towards better financial health");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(heading);
        form.add(Box.createVerticalStrut(8));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(25));

        nameField = createTextField("Full Name");
        emailField = createTextField("Email Address");
        phoneField = createTextField("Phone Number");
        passwordField = createPasswordField("Password");
        confirmPasswordField = createPasswordField("Confirm Password");

        form.add(createLabeledField("Username", nameField));
        form.add(Box.createVerticalStrut(12));

        form.add(createLabeledField("Email Address", emailField));
        form.add(Box.createVerticalStrut(12));

        form.add(createLabeledField("Phone Number", phoneField));
        form.add(Box.createVerticalStrut(12));

        form.add(createLabeledField("Password", passwordField));
        form.add(Box.createVerticalStrut(12));

        form.add(createLabeledField("Confirm Password", confirmPasswordField));
        form.add(Box.createVerticalStrut(22));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(darkGreen);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.addActionListener(e -> registerUser());

        form.add(registerButton);

        card.add(form);
        return card;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(textColor);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        field.setPreferredSize(new Dimension(600, 63));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(195, 222, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));
        field.setToolTipText(placeholder);
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        field.setPreferredSize(new Dimension(600, 63));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(195, 222, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));
        field.setToolTipText(placeholder);
        return field;
    }

    private void styleOutlineButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(darkGreen);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 155, 145), 1),
                new EmptyBorder(8, 16, 8, 16)));
    }

    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        char[] password = passwordField.getPassword();
        char[] confirm = confirmPasswordField.getPassword();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()
                || password.length == 0 || confirm.length == 0) {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');

            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields.",
                    "Missing Details", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');

            JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');

            JOptionPane.showMessageDialog(this,
                    "Enter a valid 10-digit phone number.");
            return;
        }

        if (password.length < 8) {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');

            JOptionPane.showMessageDialog(this,
                    "Password must contain at least 8 characters.");
            return;
        }

        if (!Arrays.equals(password, confirm)) {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');

            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.",
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String passwordHash;

        try {
            passwordHash = hashPassword(password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not secure the password.",
                    "Security Error", JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirm, '\0');
        }

        String sql = "INSERT INTO users "
                + "(full_name, email, phone, password_hash) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed. "
                        + "Please check DBConnection.java.",
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, passwordHash);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Registration successful! Welcome to Finora.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);

                    nameField.setText("");
                    emailField.setText("");
                    phoneField.setText("");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this,
                        "This email is already registered.",
                        "Duplicate Email", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Please check the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private String hashPassword(char[] password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(password, salt, 210000, 256);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            byte[] hash = factory.generateSecret(spec).getEncoded();

            return Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);
        } finally {
            spec.clearPassword();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Use the default Swing appearance.
            }
            new FinoraRegistration().setVisible(true);
        });
    }
}
