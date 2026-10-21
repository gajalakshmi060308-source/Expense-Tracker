
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.io.File;

public class FinoraLogin extends JFrame {

    private final Color darkGreen = new Color(0, 91, 78);
    private final Color mint = new Color(226, 247, 240);
    private final Color textColor = new Color(65, 91, 105);

    private JTextField emailField;
    private JPasswordField passwordField;

    public FinoraLogin() {
        setTitle("Finora | Login");
        setSize(1150, 750);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(1, 2, 25, 0));
        content.setBackground(mint);
        content.setBorder(new EmptyBorder(35, 35, 35, 35));

        content.add(createWelcomePanel());
        content.add(createLoginPanel());

        mainPanel.add(content, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(12, 25, 12, 25));

        JLabel logo;

        File logoFile = new File("images/logo.png.jpeg");

        if (logoFile.exists()) {
            ImageIcon original = new ImageIcon(logoFile.getPath());
            Image scaled = original.getImage()
                    .getScaledInstance(55, 55, Image.SCALE_SMOOTH);
            logo = new JLabel("Finora", new ImageIcon(scaled),
                    SwingConstants.LEFT);
            logo.setIconTextGap(10);
        } else {
            logo = new JLabel("🍃 Finora");
        }

        logo.setFont(new Font("Times New Roman", Font.BOLD, 32));
        logo.setForeground(darkGreen);

        header.add(logo, BorderLayout.WEST);

        return header;
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(mint);
        panel.setBorder(new EmptyBorder(35, 15, 20, 15));

        JLabel heading = new JLabel(
                "<html>Welcome<br>Back!</html>");
        heading.setFont(new Font("Times New Roman", Font.BOLD, 43));
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Your journey towards better financial<br>"
                + "health continues here.</html>");
        description.setFont(new Font("Times New Roman", Font.PLAIN, 21));
        description.setForeground(textColor);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(20));
        panel.add(description);
        panel.add(Box.createVerticalStrut(45));

        addFeature(panel, "📊", "Track Your Expenses",
                "Keep your spending under control");

        addFeature(panel, "🎯", "Achieve Your Goals",
                "Stay focused on your financial plans");

        addFeature(panel, "🛡", "Secure Access",
                "Your account details stay protected");

        addFeature(panel, "🍃", "Build Better Habits",
                "Make every financial decision count");

        return panel;
    }

    private void addFeature(JPanel panel, String icon,
                            String title, String subtitle) {

        JPanel feature = new JPanel(new BorderLayout(12, 0));
        feature.setBackground(mint);
        feature.setAlignmentX(Component.LEFT_ALIGNMENT);
        feature.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        feature.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 27));
        iconLabel.setPreferredSize(new Dimension(48, 48));

        JPanel labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        labels.setBackground(mint);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 19));
        titleLabel.setForeground(darkGreen);

        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        subLabel.setForeground(textColor);

        labels.add(titleLabel);
        labels.add(Box.createVerticalStrut(5));
        labels.add(subLabel);

        feature.add(iconLabel, BorderLayout.WEST);
        feature.add(labels, BorderLayout.CENTER);

        panel.add(feature);
        panel.add(Box.createVerticalStrut(12));
    }

    private JPanel createLoginPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(213, 236, 230), 1),
                new EmptyBorder(30, 25, 30, 25)));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Login to Finora");
        heading.setFont(new Font("Times New Roman", Font.BOLD, 34));
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Manage your finances with confidence");
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(heading);
        form.add(Box.createVerticalStrut(12));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(40));

        emailField = createTextField();
        passwordField = createPasswordField();

        form.add(createLabeledField("Email Address", emailField));
        form.add(Box.createVerticalStrut(22));

        form.add(createLabeledField("Password", passwordField));
        form.add(Box.createVerticalStrut(15));

        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setFont(
                new Font("Times New Roman", Font.PLAIN, 16));
        forgotPassword.setForeground(darkGreen);
        forgotPassword.setCursor(
                new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.setAlignmentX(Component.RIGHT_ALIGNMENT);

        forgotPassword.addMouseListener(
                new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                JOptionPane.showMessageDialog(
                        FinoraLogin.this,
                        "Password reset can be added separately.",
                        "Forgot Password",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        form.add(forgotPassword);
        form.add(Box.createVerticalStrut(30));

        JButton loginButton = new JButton("Login");
        stylePrimaryButton(loginButton);
        loginButton.addActionListener(e -> loginUser());

        form.add(loginButton);
        form.add(Box.createVerticalStrut(25));

        JLabel accountLabel = new JLabel("Don't have an account?");
        accountLabel.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        accountLabel.setForeground(textColor);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(accountLabel);
        form.add(Box.createVerticalStrut(12));

        JButton registerButton = new JButton("Create Account");
        styleOutlineButton(registerButton);

        registerButton.addActionListener(e -> {
            new FinoraRegistration().setVisible(true);
            dispose();
        });

        form.add(registerButton);

        card.add(form);

        return card;
    }

    private JPanel createLabeledField(String labelText,
                                      JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        panel.setPreferredSize(new Dimension(500, 55));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Times New Roman", Font.BOLD, 17));
        label.setForeground(darkGreen);
        label.setPreferredSize(new Dimension(125, 45));

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        field.setForeground(textColor);
        field.setPreferredSize(new Dimension(300, 50));
        field.setMinimumSize(new Dimension(150, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(195, 222, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));

        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        field.setForeground(textColor);
        field.setPreferredSize(new Dimension(300, 50));
        field.setMinimumSize(new Dimension(150, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(195, 222, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));

        return field;
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(darkGreen);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        button.setPreferredSize(new Dimension(400, 52));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void styleOutlineButton(JButton button) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 18));
        button.setForeground(darkGreen);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(0, 155, 145), 1),
                new EmptyBorder(10, 20, 10, 20)));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setPreferredSize(new Dimension(400, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void loginUser() {
        String email = emailField.getText().trim();
        char[] password = passwordField.getPassword();

        if (email.isEmpty() || password.length == 0) {
            Arrays.fill(password, '\0');
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter your email address and password.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String storedHash = null;

        String sql = "SELECT password_hash FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Database connection failed. "
                        + "Please check DBConnection.java.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        storedHash = rs.getString("password_hash");
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not access the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        boolean valid = false;

        if (storedHash != null) {
            valid = verifyPassword(password, storedHash);
        }

        Arrays.fill(password, '\0');

        if (valid) {
            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Welcome back to Finora.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Open your expense tracker dashboard here later.
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email address or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean verifyPassword(char[] password, String storedHash) {
        PBEKeySpec spec = null;

        try {
            String[] parts = storedHash.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] expectedHash = Base64.getDecoder().decode(parts[1]);

            spec = new PBEKeySpec(password, salt, 210000, 256);

            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            byte[] actualHash =
                    factory.generateSecret(spec).getEncoded();

            return java.security.MessageDigest.isEqual(
                    expectedHash, actualHash);

        } catch (Exception e) {
            return false;
        } finally {
            if (spec != null) {
                spec.clearPassword();
            }
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

            new FinoraLogin().setVisible(true);
        });
    }
}
