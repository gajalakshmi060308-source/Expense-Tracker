
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ProfileFrame extends JFrame {

    private final String email;
    private final Runnable onLogout;

    private final Color darkGreen = new Color(0, 91, 78);
    private final Color green = new Color(0, 119, 94);
    private final Color mint = new Color(235, 249, 244);
    private final Color lightGreen = new Color(220, 243, 234);
    private final Color textColor = new Color(48, 65, 59);
    private final Color muted = new Color(112, 130, 122);
    private final Color borderColor = new Color(218, 234, 226);

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JLabel namePreview;
    private AvatarPanel avatar;
    private JButton saveButton;

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

        JPanel background = new JPanel(new BorderLayout(0, 22));
        background.setBackground(mint);
        background.setBorder(new EmptyBorder(28, 38, 30, 38));

        // Page heading
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);

        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Times New Roman", Font.BOLD, 34));
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel(
                "Manage your personal information and account details.");
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        subtitle.setForeground(muted);

        heading.add(title);
        heading.add(Box.createVerticalStrut(5));
        heading.add(subtitle);

        background.add(heading, BorderLayout.NORTH);

        // Main white card
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(borderColor, 1, true),
                new EmptyBorder(25, 25, 25, 25)));

        // Left profile summary
        JPanel summary = new JPanel();
        summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
        summary.setBackground(Color.WHITE);
        summary.setPreferredSize(new Dimension(265, 400));
        summary.setBorder(new EmptyBorder(12, 5, 12, 25));

        avatar = new AvatarPanel(getInitials(fullName));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);

        namePreview = new JLabel(fullName);
        namePreview.setFont(new Font("Times New Roman", Font.BOLD, 24));
        namePreview.setForeground(darkGreen);
        namePreview.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel accountLabel = new JLabel("FINORA ACCOUNT");
        accountLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 13));
        accountLabel.setForeground(muted);
        accountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel activeLabel = new JLabel("  ●  Account Active  ");
        activeLabel.setFont(
                new Font("Times New Roman", Font.BOLD, 14));
        activeLabel.setForeground(darkGreen);
        activeLabel.setBackground(lightGreen);
        activeLabel.setOpaque(true);
        activeLabel.setBorder(new EmptyBorder(8, 10, 8, 10));
        activeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel description = new JLabel(
                "<html><div style='text-align:center;'>"
                + "Your personal space to manage<br>"
                + "your Finora account details."
                + "</div></html>");
        description.setFont(
                new Font("Times New Roman", Font.PLAIN, 16));
        description.setForeground(muted);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        summary.add(Box.createVerticalStrut(10));
        summary.add(avatar);
        summary.add(Box.createVerticalStrut(20));
        summary.add(namePreview);
        summary.add(Box.createVerticalStrut(8));
        summary.add(accountLabel);
        summary.add(Box.createVerticalStrut(20));
        summary.add(activeLabel);
        summary.add(Box.createVerticalStrut(25));
        summary.add(description);

        // Right editable form
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(
                        0, 1, 0, 0, borderColor),
                new EmptyBorder(12, 30, 5, 10)));

        JLabel formTitle = new JLabel("Personal Information");
        formTitle.setFont(
                new Font("Times New Roman", Font.BOLD, 27));
        formTitle.setForeground(darkGreen);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formSubtitle = new JLabel(
                "Edit your details and save your changes.");
        formSubtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 16));
        formSubtitle.setForeground(muted);
        formSubtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        nameField = createField();
        emailField = createField();
        phoneField = createField();

        emailField.setEditable(false);
        emailField.setBackground(new Color(243, 247, 245));
        emailField.setForeground(muted);

        form.add(formTitle);
        form.add(Box.createVerticalStrut(7));
        form.add(formSubtitle);
        form.add(Box.createVerticalStrut(24));

        form.add(createFieldGroup("Full Name", nameField));
        form.add(Box.createVerticalStrut(14));

        form.add(createFieldGroup("Email Address", emailField));
        form.add(Box.createVerticalStrut(14));

        form.add(createFieldGroup("Phone Number", phoneField));
        form.add(Box.createVerticalStrut(8));

        JLabel hint = new JLabel(
                "Enter a valid 10-digit phone number.");
        hint.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        hint.setForeground(muted);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);

        form.add(Box.createVerticalStrut(25));

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttons.setOpaque(false);
        buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton backButton = createButton(
                "Back to Dashboard", Color.WHITE, darkGreen);

        saveButton = createButton(
                "Save Changes", darkGreen, Color.WHITE);

        buttons.add(backButton);
        buttons.add(saveButton);
        form.add(buttons);

        // Card layout: summary on left, form on right
        card.add(summary, BorderLayout.WEST);
        card.add(form, BorderLayout.CENTER);

        background.add(card, BorderLayout.CENTER);
        add(background, BorderLayout.CENTER);

        // Update avatar and name preview while typing
        nameField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    private void refresh() {
                        String name = nameField.getText().trim();
                        namePreview.setText(
                                name.isEmpty() ? "Your Name" : name);
                        avatar.setInitials(getInitials(name));
                    }

                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e) {
                        refresh();
                    }

                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e) {
                        refresh();
                    }

                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e) {
                        refresh();
                    }
                });

        saveButton.addActionListener(e -> updateProfile());

        backButton.addActionListener(e -> {
            dispose();
            new DashboardFrame(nameField.getText().trim(), email)
                    .setVisible(true);
        });

        loadProfile();
    }

    private JPanel createFieldGroup(
            String labelText, JTextField field) {

        JPanel group = new JPanel();
        group.setLayout(new BoxLayout(group, BoxLayout.Y_AXIS));
        group.setBackground(Color.WHITE);
        group.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Times New Roman", Font.BOLD, 17));
        label.setForeground(textColor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        group.add(label);
        group.add(Box.createVerticalStrut(7));
        group.add(field);

        return group;
    }

    private JTextField createField() {
        JTextField field = new JTextField();

        field.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        field.setForeground(textColor);
        field.setCaretColor(darkGreen);
        field.setBackground(Color.WHITE);
        field.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 1),
                new EmptyBorder(9, 12, 9, 12)));

        return field;
    }

    private JButton createButton(
            String text, Color background, Color foreground) {

        JButton button = new JButton(text);
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 17, 12, 17));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(
                        background == darkGreen ? green : lightGreen);
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(background);
            }
        });

        return button;
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "F";
        }

        String[] parts = name.trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return (parts[0].substring(0, 1)
                + parts[parts.length - 1].substring(0, 1))
                .toUpperCase();
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

                        String phone = rs.getString("phone");
                        phoneField.setText(phone == null ? "" : phone);
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Profile not found.");
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

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your full name.");
            nameField.requestFocusInWindow();
            return;
        }

        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this,
                    "Enter a valid 10-digit phone number.");
            phoneField.requestFocusInWindow();
            return;
        }

        String sql =
                "UPDATE users SET full_name = ?, phone = ? WHERE email = ?";

        saveButton.setEnabled(false);

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                showDatabaseError();
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Profile updated successfully!",
                            "Finora",
                            JOptionPane.INFORMATION_MESSAGE);

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
        } finally {
            saveButton.setEnabled(true);
        }
    }

    private void showDatabaseError() {
        JOptionPane.showMessageDialog(this,
                "Database operation failed. Check the terminal.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // Circular profile avatar with initials
    private static class AvatarPanel extends JPanel {

        private String initials;

        AvatarPanel(String initials) {
            this.initials = initials;
            setOpaque(false);
            setPreferredSize(new Dimension(112, 112));
            setMinimumSize(new Dimension(112, 112));
            setMaximumSize(new Dimension(112, 112));
        }

        void setInitials(String initials) {
            this.initials = initials;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(220, 243, 234));
            g2.fillOval(1, 1, 110, 110);

            g2.setColor(new Color(0, 91, 78));
            g2.fillOval(7, 7, 98, 98);

            g2.setFont(new Font("Times New Roman", Font.BOLD, 35));
            g2.setColor(Color.WHITE);

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initials)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2
                    + fm.getAscent();

            g2.drawString(initials, x, y);
            g2.dispose();
        }
    }
}
