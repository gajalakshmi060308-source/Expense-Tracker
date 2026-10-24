
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddExpenseFrame extends JFrame {

    private final String fullName;
    private final String email;

    private final Color mint = new Color(235, 247, 239);
    private final Color darkGreen = new Color(30, 91, 65);
    private final Color lightGreen = new Color(218, 239, 224);
    private final Color textColor = new Color(45, 65, 52);

    private final String[] categories = {
        "Food & Groceries",
        "Transportation",
        "Education",
        "Bills & Housing",
        "Health",
        "Shopping",
        "Entertainment",
        "Personal Care",
        "Savings",
        "Other"
    };

    private JTextField amountField;
    private JTextField dateField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> paymentBox;
    private JTextArea descriptionArea;

    public AddExpenseFrame(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;

        setTitle("Finora | Add Expense");
        setSize(1450, 900);
        setMinimumSize(new Dimension(1050, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

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

        Runnable logoutAction = () -> {
            dispose();
            new FinoraLogin().setVisible(true);
        };

        NavigationPanel navigation =
            new NavigationPanel(fullName, email, logoutAction);

        add(navigation, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }

    // Left introduction panel
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(30, 10, 30, 20));

        JLabel title = new JLabel("Add Expense");
        title.setFont(new Font("Times New Roman", Font.BOLD, 44));
        title.setForeground(darkGreen);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "<html>Track your spending and<br>" +
            "manage your money wisely.</html>"
        );
        subtitle.setFont(new Font("Times New Roman", Font.PLAIN, 21));
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(new EmptyBorder(15, 0, 25, 0));

        JLabel illustration = new JLabel("💸");
illustration.setFont(
    new Font("Segoe UI Emoji", Font.PLAIN, 95)
);
illustration.setAlignmentX(Component.LEFT_ALIGNMENT);
illustration.setBorder(
    new EmptyBorder(10, 0, 25, 0)
);

        JLabel benefitTitle = new JLabel("Spend smarter every day");
        benefitTitle.setFont(new Font("Times New Roman", Font.BOLD, 29));
        benefitTitle.setForeground(darkGreen);
        benefitTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(title);
        panel.add(subtitle);
        panel.add(illustration);
        panel.add(benefitTitle);
        panel.add(Box.createVerticalStrut(18));

        addBenefit(panel, "Record your daily expenses");
        addBenefit(panel, "Understand your spending habits");
        addBenefit(panel, "Stay within your budget");

        return panel;
    }

    private void addBenefit(JPanel panel, String text) {
        JLabel label = new JLabel("✓  " + text);
        label.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        label.setForeground(textColor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(8, 0, 8, 0));
        panel.add(label);
    }

    // Expense form
    private JPanel createFormPanel() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(218, 232, 221), 1, true),
            new EmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 0, 6, 0);

        JLabel heading = new JLabel("Expense Details");
        heading.setFont(new Font("Times New Roman", Font.BOLD, 32));
        heading.setForeground(darkGreen);

        gbc.gridy = 0;
        card.add(heading, gbc);

        JLabel note = new JLabel("Enter the details of your spending.");
        note.setFont(new Font("Times New Roman", Font.PLAIN, 18));
        note.setForeground(Color.GRAY);

        gbc.gridy = 1;
        card.add(note, gbc);

        categoryBox = new JComboBox<>(categories);
        amountField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString());

        paymentBox = new JComboBox<>(new String[]{
            "Cash",
            "Debit Card",
            "Credit Card",
            "UPI",
            "Net Banking",
            "Other"
        });

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setFont(
            new Font("Times New Roman", Font.PLAIN, 18)
        );

        int row = 2;

        row = addField(card, gbc, row,
                       "Expense Category", categoryBox);

        row = addField(card, gbc, row,
                       "Amount (₹)", amountField);

        row = addField(card, gbc, row,
                       "Expense Date (yyyy-mm-dd)", dateField);

        row = addField(card, gbc, row,
                       "Payment Method", paymentBox);

        row = addField(card, gbc, row,
                       "Description (optional)",
                       new JScrollPane(descriptionArea));

        // Buttons aligned at the bottom-right corner
JButton saveButton = new JButton("Save Expense");
styleButton(saveButton, darkGreen, Color.WHITE);
saveButton.addActionListener(e -> saveExpense());

JButton clearButton = new JButton("Clear Form");
styleButton(clearButton, lightGreen, darkGreen);
clearButton.addActionListener(e -> clearForm());

JPanel buttonPanel = new JPanel(
    new FlowLayout(FlowLayout.RIGHT, 12, 0)
);
buttonPanel.setBackground(Color.WHITE);

buttonPanel.add(clearButton);
buttonPanel.add(saveButton);

gbc.gridy = row;
gbc.gridx = 0;
gbc.gridwidth = 1;
gbc.fill = GridBagConstraints.HORIZONTAL;
gbc.anchor = GridBagConstraints.EAST;
gbc.insets = new Insets(18, 0, 0, 0);

card.add(buttonPanel, gbc);
        return card;
    }

    // Adds a label and its field in separate, consecutive rows
    private int addField(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JComponent field) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Times New Roman", Font.BOLD, 18));
        label.setForeground(textColor);

        gbc.gridy = row++;
        gbc.insets = new Insets(9, 0, 3, 0);
        panel.add(label, gbc);

        field.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(300, 40));

            ((JTextField) field).setBorder(new CompoundBorder(
                new LineBorder(new Color(205, 220, 209), 1, true),
                new EmptyBorder(5, 10, 5, 10)
            ));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(300, 40));
        } else if (field instanceof JScrollPane) {
            field.setPreferredSize(new Dimension(300, 85));
        }

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 5, 0);
        panel.add(field, gbc);

        return row;
    }

    // Common button styling
    private void styleButton(
            JButton button,
            Color background,
            Color foreground) {

        button.setFont(new Font("Times New Roman", Font.BOLD, 19));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 15, 12, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }

    // Save expense to MySQL
    private void saveExpense() {
        String category = (String) categoryBox.getSelectedItem();
        String amountText = amountField.getText().trim();
        String dateText = dateField.getText().trim();
        String paymentMethod = (String) paymentBox.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (amountText.isEmpty() || dateText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter the amount and date.",
                "Missing Details",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        BigDecimal amount;
        LocalDate expenseDate;

        try {
            amount = new BigDecimal(amountText);

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Amount must be greater than zero."
                );
                return;
            }

            expenseDate = LocalDate.parse(dateText);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid amount."
            );
            return;
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Enter the date in yyyy-mm-dd format."
            );
            return;
        }

        String sql =
            "INSERT INTO expenses " +
            "(user_id, category, amount, expense_date, " +
            "payment_method, description) " +
            "SELECT user_id, ?, ?, ?, ?, ? " +
            "FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category);
            ps.setBigDecimal(2, amount);
            ps.setDate(3, Date.valueOf(expenseDate));
            ps.setString(4, paymentMethod);
            ps.setString(
                5, description.isEmpty() ? null : description
            );
            ps.setString(6, email);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Expense saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
                clearForm();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "User not found. Please log in again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Unable to save expense:\n" + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    // Clear all fields
    private void clearForm() {
        categoryBox.setSelectedIndex(0);
        amountField.setText("");
        dateField.setText(LocalDate.now().toString());
        paymentBox.setSelectedIndex(0);
        descriptionArea.setText("");
    }
}
