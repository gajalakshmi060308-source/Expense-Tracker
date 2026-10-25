
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
import java.io.File;

public class FinoraLogin extends JFrame {

    // Theme colors
    private final Color darkGreen = new Color(0, 91, 78);
    private final Color mint = new Color(226, 247, 240);
    private final Color textColor = new Color(65, 91, 105);

    private JTextField emailField;
    private JPasswordField passwordField;

    public FinoraLogin() {

        setTitle("Finora | Login");
        setSize(1180, 760);
        setMinimumSize(new Dimension(980, 650));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(246, 251, 248));

        page.add(createHeader(), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(new Color(246, 251, 248));
        body.setBorder(new EmptyBorder(20, 35, 35, 35));

        JPanel layout = new JPanel(new GridLayout(1, 2, 0, 0));
        layout.setPreferredSize(new Dimension(1000, 570));
        layout.setBackground(Color.WHITE);

        layout.setBorder(
                BorderFactory.createLineBorder(
                        new Color(218, 235, 228), 1));

        layout.add(createBrandPanel());
        layout.add(createLoginPanel());

        body.add(layout);

        page.add(body, BorderLayout.CENTER);

        setContentPane(page);
    }

    // Header containing only the logo image
    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(10, 28, 10, 28));
        header.setPreferredSize(new Dimension(100, 140));

        File logoFile = new File("images/logo.png.jpeg");

        if (logoFile.exists()) {

            ImageIcon original =
                    new ImageIcon(logoFile.getPath());

            Image scaled = original.getImage()
                    .getScaledInstance(
                            200, 120, Image.SCALE_SMOOTH);

            header.add(
                    new JLabel(new ImageIcon(scaled)),
                    BorderLayout.WEST);

        } else {

            JLabel fallback = new JLabel("Finora");

            fallback.setFont(
                    new Font("Times New Roman", Font.BOLD, 28));

            fallback.setForeground(darkGreen);

            header.add(fallback, BorderLayout.WEST);
        }

        return header;
    }

    // Left-side welcome panel
    private JPanel createBrandPanel() {

        JPanel panel = new JPanel();

        panel.setLayout(
                new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(new Color(218, 239, 224));

        panel.setBorder(
                new EmptyBorder(55, 45, 45, 35));

        JLabel eyebrow =
                new JLabel("YOUR MONEY. YOUR FUTURE.");

        eyebrow.setFont(
                new Font("Times New Roman", Font.BOLD, 15));

        eyebrow.setForeground(
                new Color(30, 91, 65));

        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title = new JLabel(
                "<html>Make your<br>money matter.</html>");

        title.setFont(
                new Font("Times New Roman", Font.BOLD, 42));

        title.setForeground(new Color(30, 91, 65));

        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Build healthier financial habits with<br>"
                + "simple tools that keep you in control.</html>");

        description.setFont(
                new Font("Times New Roman", Font.PLAIN, 20));

        description.setForeground(
                new Color(45, 65, 52));

        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(eyebrow);
        panel.add(Box.createVerticalStrut(22));
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(description);
        panel.add(Box.createVerticalStrut(38));

        panel.add(createBrandFeature(
                "01",
                "Track spending",
                "See where your money goes."));

        panel.add(Box.createVerticalStrut(18));

        panel.add(createBrandFeature(
                "02",
                "Plan your budget",
                "Keep expenses within your limits."));

        panel.add(Box.createVerticalStrut(18));

        panel.add(createBrandFeature(
                "03",
                "Reach your goals",
                "Make progress one step at a time."));

        panel.add(Box.createVerticalGlue());

        JLabel footer = new JLabel(
                "A clearer view of your finances starts here.");

        footer.setFont(
                new Font("Times New Roman", Font.ITALIC, 15));

        footer.setForeground(
                new Color(30, 91, 65));

        footer.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(footer);

        return panel;
    }

    // Feature rows on the welcome panel
    private JPanel createBrandFeature(
            String number,
            String title,
            String subtitle) {

        JPanel row = new JPanel(
                new BorderLayout(14, 0));

        row.setOpaque(false);

        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        row.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 55));

        JLabel numberLabel =
                new JLabel(number, SwingConstants.CENTER);

        numberLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 16));

        numberLabel.setForeground(new Color(30, 91, 65));

        numberLabel.setPreferredSize(
                new Dimension(42, 42));

        numberLabel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(30, 91, 65), 1));

        JPanel words = new JPanel();

        words.setOpaque(false);

        words.setLayout(
                new BoxLayout(words, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);

        titleLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 18));

        titleLabel.setForeground(new Color(30, 91, 65));

        JLabel subLabel = new JLabel(subtitle);

        subLabel.setFont(
                new Font("Times New Roman", Font.PLAIN, 15));

        subLabel.setForeground(
                new Color(45, 65, 52));

        words.add(titleLabel);
        words.add(Box.createVerticalStrut(4));
        words.add(subLabel);

        row.add(numberLabel, BorderLayout.WEST);
        row.add(words, BorderLayout.CENTER);

        return row;
    }

    // Right-side login form
    private JPanel createLoginPanel() {

        JPanel panel = new JPanel(
                new GridBagLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                new EmptyBorder(35, 48, 35, 48));

        JPanel form = new JPanel();

        form.setLayout(
                new BoxLayout(form, BoxLayout.Y_AXIS));

        form.setBackground(Color.WHITE);

        form.setPreferredSize(
                new Dimension(390, 460));

        JLabel smallTitle =
                new JLabel("WELCOME BACK");

        smallTitle.setFont(
                new Font("Times New Roman", Font.BOLD, 14));

        smallTitle.setForeground(
                new Color(0, 145, 112));

        smallTitle.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        JLabel heading =
                new JLabel("Sign in to Finora");

        heading.setFont(
                new Font("Times New Roman", Font.BOLD, 34));

        heading.setForeground(darkGreen);

        heading.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Enter your details to continue.");

        subtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));

        subtitle.setForeground(textColor);

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        form.add(smallTitle);
        form.add(Box.createVerticalStrut(10));
        form.add(heading);
        form.add(Box.createVerticalStrut(10));
        form.add(subtitle);
        form.add(Box.createVerticalStrut(35));

        emailField = createTextField();
        passwordField = createPasswordField();

        form.add(createStackedField(
                "Email Address", emailField));

        form.add(Box.createVerticalStrut(20));

        form.add(createStackedField(
                "Password", passwordField));

        form.add(Box.createVerticalStrut(28));

        JButton loginButton =
                new JButton("Login to your account");

        stylePrimaryButton(loginButton);

        loginButton.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        loginButton.addActionListener(
                e -> loginUser());

        form.add(loginButton);

        form.add(Box.createVerticalStrut(32));

        JLabel securityNote = new JLabel(
                "Your financial information stays protected.");

        securityNote.setFont(
                new Font("Times New Roman", Font.ITALIC, 14));

        securityNote.setForeground(
                new Color(120, 145, 137));

        securityNote.setAlignmentX(
                Component.CENTER_ALIGNMENT);

        form.add(Box.createVerticalStrut(32));
        form.add(securityNote);

        panel.add(form);

        return panel;
    }

    // Field label above the input box
    private JPanel createStackedField(
            String labelText,
            JComponent field) {

        JPanel wrapper = new JPanel();

        wrapper.setLayout(
                new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        wrapper.setBackground(Color.WHITE);

        wrapper.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        wrapper.setMaximumSize(
                new Dimension(390, 82));

        JLabel label = new JLabel(labelText);

        label.setFont(
                new Font("Times New Roman", Font.BOLD, 17));

        label.setForeground(darkGreen);

        label.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        field.setAlignmentX(
                Component.LEFT_ALIGNMENT);

        field.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48));

        wrapper.add(label);

        wrapper.add(Box.createVerticalStrut(8));

        wrapper.add(field);

        return wrapper;
    }

    // Email field
    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setFont(
                new Font("Times New Roman", Font.PLAIN, 18));

        field.setForeground(textColor);

        field.setPreferredSize(
                new Dimension(390, 48));

        field.setMinimumSize(
                new Dimension(250, 48));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(195, 222, 220), 1),
                        new EmptyBorder(8, 12, 8, 12)));

        return field;
    }

    // Password field
    private JPasswordField createPasswordField() {

        JPasswordField field = new JPasswordField();

        field.setFont(
                new Font("Times New Roman", Font.PLAIN, 18));

        field.setForeground(textColor);

        field.setPreferredSize(
                new Dimension(390, 48));

        field.setMinimumSize(
                new Dimension(250, 48));

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(195, 222, 220), 1),
                        new EmptyBorder(8, 12, 8, 12)));

        return field;
    }

    // Login button styling
    private void stylePrimaryButton(JButton button) {

        button.setFont(
                new Font("Times New Roman", Font.BOLD, 19));

        button.setForeground(Color.WHITE);

        button.setBackground(darkGreen);

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        button.setPreferredSize(
                new Dimension(390, 50));

        button.setMaximumSize(
                new Dimension(390, 50));
    }

    // Authenticate user using the existing database
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
        String fullName = null;

        String sql =
                "SELECT full_name, password_hash "
                + "FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {

            if (con == null) {

                Arrays.fill(password, '\0');

                JOptionPane.showMessageDialog(
                        this,
                        "Database connection failed. "
                        + "Please check DBConnection.java.",
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);

                return;
            }

            try (PreparedStatement ps =
                         con.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        fullName = rs.getString("full_name");

                        storedHash =
                                rs.getString("password_hash");
                    }
                }
            }

        } catch (SQLException e) {

            Arrays.fill(password, '\0');

            JOptionPane.showMessageDialog(
                    this,
                    "Could not access the database.",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);

            e.printStackTrace();

            return;
        }

        boolean valid = false;

        if (storedHash != null) {
            valid = verifyPassword(password, storedHash);
        }

        Arrays.fill(password, '\0');

        if (valid && fullName != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful! Welcome back to Finora.");

            dispose();

            final String loggedInName = fullName;

            SwingUtilities.invokeLater(() -> {
                new DashboardFrame(
                        loggedInName, email).setVisible(true);
            });

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid email address or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Verify the entered password against its stored hash
    private boolean verifyPassword(
            char[] password,
            String storedHash) {

        PBEKeySpec spec = null;

        try {

            String[] parts = storedHash.split(":");

            if (parts.length != 2) {
                return false;
            }

            byte[] salt =
                    Base64.getDecoder().decode(parts[0]);

            byte[] expectedHash =
                    Base64.getDecoder().decode(parts[1]);

            spec = new PBEKeySpec(
                    password, salt, 210000, 256);

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

    // Application entry point
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
