import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class ProfileFrame extends JFrame {

    private final String email;
    private final Runnable onLogout;

    private final Color darkGreen = new Color(0, 91, 78);
    private final Color mint = new Color(226, 247, 240);

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;

    public ProfileFrame(
            String fullName, String email, Runnable onLogout) {

        this.email = email;
        this.onLogout = onLogout;

        setTitle("Finora | My Profile");
        setSize(1150, 750);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new NavigationPanel(fullName, email, onLogout),
                BorderLayout.NORTH);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(mint);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(darkGreen);

        nameField = new JTextField(24);
        emailField = new JTextField(24);
        phoneField = new JTextField(24);

        emailField.setEditable(false);

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridwidth = 1;
        addField(card, gbc, row++, "Full Name", nameField);
        addField(card, gbc, row++, "Email Address", emailField);
        addField(card, gbc, row++, "Phone Number", phoneField);

        JButton saveButton = new JButton("Save Changes");
        saveButton.setBackground(darkGreen);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        JButton backButton = new JButton("Back to Dashboard");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBackground(Color.WHITE);
        buttons.add(backButton);
        buttons.add(saveButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        card.add(buttons, gbc);

        background.add(card);
        add(background, BorderLayout.CENTER);

        saveButton.addActionListener(e -> updateProfile());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(nameField.getText().trim(), email)
                    .setVisible(true);
        });

        loadProfile();
    }

    private void addField(
            JPanel panel, GridBagConstraints gbc,
            int row, String label, JTextField field) {

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadProfile() {
        String sql =
                "SELECT full_name, email, phone FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                showDatabaseError();
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        nameField.setText(rs.getString("full_name"));
                        emailField.setText(rs.getString("email"));
                        phoneField.setText(rs.getString("phone"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showDatabaseError();
        }
    }

    private void updateProfile() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || !phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Enter a name and a valid 10-digit phone number.");
            return;
        }

        String sql =
                "UPDATE users SET full_name = ?, phone = ? WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                showDatabaseError();
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);

                if (ps.executeUpdate() == 1) {
                    JOptionPane.showMessageDialog(this,
                            "Profile updated successfully!");

                    dispose();
                    new ProfileFrame(name, email, onLogout).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Profile could not be updated.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showDatabaseError();
        }
    }

    private void showDatabaseError() {
        JOptionPane.showMessageDialog(this,
                "Database operation failed. Check the terminal for details.");
    }
}