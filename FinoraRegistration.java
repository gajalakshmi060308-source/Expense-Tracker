
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.security.SecureRandom;
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(mint);

        mainPanel.add(createHeader(), BorderLayout.NORTH);

        // Keep only the registration form centered on the page.
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(mint);
        content.setBorder(new EmptyBorder(20, 30, 30, 30));

        JPanel formCard = createFormPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        content.add(formCard, gbc);
        mainPanel.add(content, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    // Logo on the left, without extra header details.
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(100, 95));
        header.setBorder(new EmptyBorder(5, 25, 5, 25));

        JLabel logo;
        java.io.File logoFile =
                new java.io.File("images/logo.png.jpeg");

        if (logoFile.exists()) {
            ImageIcon original = new ImageIcon(logoFile.getPath());
            Image scaled = original.getImage()
                    .getScaledInstance(200, 120, Image.SCALE_SMOOTH);

            logo = new JLabel(new ImageIcon(scaled));
        } else {
            logo = new JLabel("🍃 Finora");
            logo.setFont(
                    new Font("Times New Roman", Font.BOLD, 30));
            logo.setForeground(darkGreen);
        }

        header.add(logo, BorderLayout.WEST);
        return header;
    }

    private JPanel createLabeledField(
            String labelText, JComponent field) {

        JPanel panel = new JPanel(new BorderLayout(12, 0));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(520, 48));
        panel.setMaximumSize(new Dimension(520, 48));

        JLabel label = new JLabel(labelText);
        label.setFont(
                new Font("Times New Roman", Font.BOLD, 16));
        label.setForeground(darkGreen);
        label.setPreferredSize(new Dimension(150, 40));

        panel.add(label, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createFormPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(650, 540));

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(213, 236, 230), 1),
                new EmptyBorder(25, 35, 25, 35)));

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);

        JLabel heading = new JLabel("Create Your Account");
        heading.setFont(
                new Font("Times New Roman", Font.BOLD, 32));
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        form.add(heading);
        form.add(Box.createVerticalStrut(25));

        nameField = createTextField("Full Name");
        emailField = createTextField("Email Address");
        phoneField = createTextField("Phone Number");
        passwordField = createPasswordField("Password");
        confirmPasswordField =
                createPasswordField("Confirm Password");

        form.add(createLabeledField("Username", nameField));
        form.add(Box.createVerticalStrut(15));

        form.add(createLabeledField("Email Address", emailField));
        form.add(Box.createVerticalStrut(15));

        form.add(createLabeledField("Phone Number", phoneField));
        form.add(Box.createVerticalStrut(15));

        form.add(createLabeledField("Password", passwordField));
        form.add(Box.createVerticalStrut(15));

        form.add(createLabeledField(
                "Confirm Password", confirmPasswordField));
        form.add(Box.createVerticalStrut(25));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(
                new Font("Times New Roman", Font.BOLD, 19));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(darkGreen);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));
        registerButton.setPreferredSize(new Dimension(520, 48));
        registerButton.setMaximumSize(new Dimension(520, 48));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerButton.addActionListener(e -> registerUser());

        form.add(registerButton);

        card.add(form);
        return card;
    }

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        field.setForeground(textColor);
        field.setPreferredSize(new Dimension(350, 42));
        field.setMinimumSize(new Dimension(200, 42));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(195, 222, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));

        field.setToolTipText(placeholder);
        return field;
    }

    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField();
        field.setFont(
                new Font("Times New Roman", Font.PLAIN, 17));
        field.setPreferredSize(new Dimension(350, 42));
        field.setMinimumSize(new Dimension(200, 42));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(195, 222, 220), 1),
                new EmptyBorder(8, 12, 8, 12)));

        field.setToolTipText(placeholder);
        return field;
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
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

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
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String passwordHash;

        try {
            passwordHash = hashPassword(password);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Could not secure the password.",
                    "Security Error",
                    JOptionPane.ERROR_MESSAGE);
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
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE);
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
                            "Registration successful! "
                            + "Please log in to Finora.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Close the registration page and open the login page.
                    dispose();
                    FinoraLogin loginPage = new FinoraLogin();
                    loginPage.setVisible(true);
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this,
                        "This email is already registered.",
                        "Duplicate Email",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registration failed. Please check the database.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private String hashPassword(char[] password) throws Exception {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        PBEKeySpec spec =
                new PBEKeySpec(password, salt, 210000, 256);

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(
                            "PBKDF2WithHmacSHA256");

            byte[] hash =
                    factory.generateSecret(spec).getEncoded();

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
