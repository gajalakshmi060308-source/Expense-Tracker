
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SavingsGoalsFrame extends JFrame {

    private final String fullName;
    private final String email;

    private final Color mint = new Color(235, 247, 239);
    private final Color darkGreen = new Color(30, 91, 65);
    private final Color textColor = new Color(45, 65, 52);
    private final Color borderColor = new Color(218, 232, 221);

    private JTextField goalNameField;
    private JTextField targetAmountField;
    private JTextField savedAmountField;
    private JTextField targetDateField;

    private JTable goalsTable;
    private DefaultTableModel tableModel;

    private JLabel totalGoalsLabel;
    private JLabel totalSavedLabel;

    public SavingsGoalsFrame(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;

        setTitle("Finora | Savings Goals");
        setSize(1450, 900);
        setMinimumSize(new Dimension(1050, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Runnable logoutAction = () -> {
            dispose();
            new FinoraLogin().setVisible(true);
        };

        NavigationPanel navigation =
                new NavigationPanel(fullName, email, logoutAction);

        add(navigation, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 22));
        content.setBackground(mint);
        content.setBorder(new EmptyBorder(35, 45, 35, 45));

        // Page heading
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(
                new BoxLayout(headingPanel, BoxLayout.Y_AXIS)
        );
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("Savings Goals");
        heading.setFont(
                new Font("Times New Roman", Font.BOLD, 40)
        );
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "Plan your savings and track your financial goals."
        );
        subtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 19)
        );
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(10));
        headingPanel.add(subtitle);

        // Goal entry form
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(borderColor, 1),
                        new EmptyBorder(22, 25, 22, 25)
                )
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        goalNameField = new JTextField(18);
        targetAmountField = new JTextField(18);
        savedAmountField = new JTextField("0.00", 18);
        targetDateField = new JTextField(18);

        addFormField(formCard, gbc, 0, 0,
                "Goal Name", goalNameField);
        addFormField(formCard, gbc, 1, 0,
                "Target Amount (₹)", targetAmountField);
        addFormField(formCard, gbc, 0, 1,
                "Already Saved (₹)", savedAmountField);
        addFormField(formCard, gbc, 1, 1,
                "Target Date (YYYY-MM-DD)", targetDateField);

        JButton addButton = new JButton("Add Savings Goal");
        styleButton(addButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;

        formCard.add(addButton, gbc);
        addButton.addActionListener(e -> addSavingsGoal());

        // Summary cards
        JPanel summaryPanel = new JPanel(
                new GridLayout(1, 2, 20, 0)
        );
        summaryPanel.setOpaque(false);

        totalGoalsLabel = new JLabel("Savings Goals: 0");
        totalSavedLabel = new JLabel("Total Saved: ₹0.00");

        styleSummaryLabel(totalGoalsLabel);
        styleSummaryLabel(totalSavedLabel);

        summaryPanel.add(totalGoalsLabel);
        summaryPanel.add(totalSavedLabel);

        // Goals table
        String[] columns = {
                "Goal Name",
                "Target Amount (₹)",
                "Saved Amount (₹)",
                "Remaining (₹)",
                "Target Date",
                "Progress"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        goalsTable = new JTable(tableModel);
        goalsTable.setFont(
                new Font("Times New Roman", Font.PLAIN, 17)
        );
        goalsTable.setRowHeight(38);
        goalsTable.setBackground(Color.WHITE);
        goalsTable.setForeground(textColor);
        goalsTable.setGridColor(borderColor);
        goalsTable.setSelectionBackground(
                new Color(211, 245, 232)
        );
        goalsTable.setSelectionForeground(darkGreen);
        goalsTable.setShowVerticalLines(false);
        goalsTable.setFillsViewportHeight(true);
        goalsTable.setAutoCreateRowSorter(true);

        goalsTable.getTableHeader().setFont(
                new Font("Times New Roman", Font.BOLD, 16)
        );
        goalsTable.getTableHeader().setBackground(darkGreen);
        goalsTable.getTableHeader().setForeground(Color.WHITE);
        goalsTable.getTableHeader().setPreferredSize(
                new Dimension(100, 45)
        );
        goalsTable.getTableHeader().setReorderingAllowed(false);

        goalsTable.getColumnModel().getColumn(0).setPreferredWidth(190);
        goalsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        goalsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        goalsTable.getColumnModel().getColumn(3).setPreferredWidth(150);
        goalsTable.getColumnModel().getColumn(4).setPreferredWidth(140);
        goalsTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane tableScrollPane = new JScrollPane(goalsTable);
        tableScrollPane.setBorder(new LineBorder(borderColor, 1));
        tableScrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new LineBorder(borderColor, 1));
        tableCard.add(tableScrollPane, BorderLayout.CENTER);

        // Update Savings button
        JButton updateButton = new JButton("Update Savings");
        styleButton(updateButton);
        updateButton.addActionListener(e -> updateSavings());

        // Refresh button
        JButton refreshButton = new JButton("Refresh Goals");
        styleButton(refreshButton);
        refreshButton.addActionListener(e -> loadSavingsGoals());

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 12, 5)
        );
        bottomPanel.setOpaque(false);
        bottomPanel.add(updateButton);
        bottomPanel.add(refreshButton);

        // Arrange page content
        JPanel topContent = new JPanel(new BorderLayout(0, 20));
        topContent.setOpaque(false);
        topContent.add(headingPanel, BorderLayout.NORTH);

        JPanel formAndSummary = new JPanel(
                new BorderLayout(0, 18)
        );
        formAndSummary.setOpaque(false);
        formAndSummary.add(formCard, BorderLayout.NORTH);
        formAndSummary.add(summaryPanel, BorderLayout.CENTER);

        topContent.add(formAndSummary, BorderLayout.CENTER);

        JPanel centerContent = new JPanel(new BorderLayout(0, 15));
        centerContent.setOpaque(false);
        centerContent.add(topContent, BorderLayout.NORTH);
        centerContent.add(tableCard, BorderLayout.CENTER);
        centerContent.add(bottomPanel, BorderLayout.SOUTH);

        content.add(centerContent, BorderLayout.CENTER);

        // Make the complete page scrollable
        JScrollPane mainScrollPane = new JScrollPane(content);
        mainScrollPane.setBorder(null);
        mainScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        mainScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(mainScrollPane, BorderLayout.CENTER);

        loadSavingsGoals();
    }

    private void addFormField(
            JPanel panel,
            GridBagConstraints gbc,
            int column,
            int row,
            String labelText,
            JTextField field
    ) {
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 7));
        fieldPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Times New Roman", Font.BOLD, 17));
        label.setForeground(darkGreen);

        field.setFont(new Font("Times New Roman", Font.PLAIN, 17));
        field.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(borderColor, 1),
                        new EmptyBorder(9, 10, 9, 10)
                )
        );

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(field, BorderLayout.CENTER);

        gbc.gridx = column;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 1;

        panel.add(fieldPanel, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 17));
        button.setBackground(darkGreen);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleSummaryLabel(JLabel label) {
        label.setFont(new Font("Times New Roman", Font.BOLD, 21));
        label.setForeground(darkGreen);
        label.setBackground(Color.WHITE);
        label.setOpaque(true);
        label.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(borderColor, 1),
                        new EmptyBorder(18, 20, 18, 20)
                )
        );
    }

    // Add a new savings goal
    private void addSavingsGoal() {
        String goalName = goalNameField.getText().trim();
        String targetText = targetAmountField.getText().trim();
        String savedText = savedAmountField.getText().trim();
        String dateText = targetDateField.getText().trim();

        if (goalName.isEmpty() || targetText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the goal name and target amount.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        BigDecimal targetAmount;
        BigDecimal savedAmount;

        try {
            targetAmount = new BigDecimal(targetText);
            savedAmount = new BigDecimal(
                    savedText.isEmpty() ? "0" : savedText
            );

            if (targetAmount.compareTo(BigDecimal.ZERO) <= 0
                    || savedAmount.compareTo(BigDecimal.ZERO) < 0
                    || savedAmount.compareTo(targetAmount) > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Target must be positive, and saved amount must be between 0 and the target.",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Enter valid numeric amounts.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        java.sql.Date targetDate = null;

        if (!dateText.isEmpty()) {
            try {
                targetDate = java.sql.Date.valueOf(dateText);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter the date in YYYY-MM-DD format.",
                        "Invalid Date",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        String sql =
                "INSERT INTO savings_goals " +
                "(user_id, goal_name, target_amount, saved_amount, target_date) " +
                "SELECT user_id, ?, ?, ?, ? FROM users WHERE email = ?";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(
                        this, "Could not connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, goalName);
                ps.setBigDecimal(2, targetAmount);
                ps.setBigDecimal(3, savedAmount);
                ps.setDate(4, targetDate);
                ps.setString(5, email);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this, "Savings goal added successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE
                    );

                    goalNameField.setText("");
                    targetAmountField.setText("");
                    savedAmountField.setText("0.00");
                    targetDateField.setText("");

                    loadSavingsGoals();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "User account was not found. Please log in again.",
                            "Error", JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to save savings goal.\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    // Add a contribution to the selected goal
    private void updateSavings() {
        int selectedRow = goalsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a savings goal from the table first.",
                    "Select Goal",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Convert the selected view row to its actual model row,
        // because the table supports sorting.
        int modelRow = goalsTable.convertRowIndexToModel(selectedRow);

        String goalName = tableModel.getValueAt(modelRow, 0).toString();
        BigDecimal targetAmount =
                new BigDecimal(tableModel.getValueAt(modelRow, 1).toString());
        BigDecimal currentSaved =
                new BigDecimal(tableModel.getValueAt(modelRow, 2).toString());

        String input = JOptionPane.showInputDialog(
                this,
                "Goal: " + goalName
                        + "\nCurrently saved: ₹" + currentSaved
                        + "\nTarget amount: ₹" + targetAmount
                        + "\n\nEnter the additional amount you saved (₹):",
                "Update Savings",
                JOptionPane.PLAIN_MESSAGE
        );

        if (input == null) {
            return;
        }

        BigDecimal contribution;

        try {
            contribution = new BigDecimal(input.trim());

            if (contribution.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Enter an amount greater than zero.",
                        "Invalid Amount",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (currentSaved.add(contribution).compareTo(targetAmount) > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "This contribution exceeds the target amount.\n"
                                + "Remaining amount: ₹"
                                + targetAmount.subtract(currentSaved),
                        "Target Exceeded",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid numeric amount.",
                    "Invalid Amount",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql =
                "UPDATE savings_goals " +
                "SET saved_amount = saved_amount + ? " +
                "WHERE goal_name = ? " +
                "AND user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND saved_amount + ? <= target_amount";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(
                        this, "Could not connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setBigDecimal(1, contribution);
                ps.setString(2, goalName);
                ps.setString(3, email);
                ps.setBigDecimal(4, contribution);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Savings updated successfully!\n"
                                    + "Added: ₹" + contribution
                                    + "\nNew total: ₹"
                                    + currentSaved.add(contribution),
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    loadSavingsGoals();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "The goal could not be updated. It may have changed, "
                                    + "or the contribution exceeds the target.",
                            "Update Failed",
                            JOptionPane.WARNING_MESSAGE
                    );
                    loadSavingsGoals();
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to update savings.\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    // Load the logged-in user's savings goals
    private void loadSavingsGoals() {
        tableModel.setRowCount(0);

        String sql =
                "SELECT goal_name, target_amount, saved_amount, target_date " +
                "FROM savings_goals " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "ORDER BY created_at DESC";

        BigDecimal totalSaved = BigDecimal.ZERO;
        int count = 0;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                JOptionPane.showMessageDialog(
                        this, "Could not connect to the database.",
                        "Database Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("goal_name");
                        BigDecimal target = rs.getBigDecimal("target_amount");
                        BigDecimal saved = rs.getBigDecimal("saved_amount");
                        java.sql.Date date = rs.getDate("target_date");

                        if (target == null) {
                            target = BigDecimal.ZERO;
                        }
                        if (saved == null) {
                            saved = BigDecimal.ZERO;
                        }

                        BigDecimal remaining = target.subtract(saved);
                        BigDecimal percentage = BigDecimal.ZERO;

                        if (target.compareTo(BigDecimal.ZERO) > 0) {
                            percentage = saved
                                    .multiply(new BigDecimal("100"))
                                    .divide(target, 2, RoundingMode.HALF_UP);
                        }

                        tableModel.addRow(new Object[]{
                                name,
                                target.setScale(2, RoundingMode.HALF_UP)
                                        .toPlainString(),
                                saved.setScale(2, RoundingMode.HALF_UP)
                                        .toPlainString(),
                                remaining.setScale(2, RoundingMode.HALF_UP)
                                        .toPlainString(),
                                date == null ? "-" : date.toString(),
                                percentage.toPlainString() + "%"
                        });

                        totalSaved = totalSaved.add(saved);
                        count++;
                    }
                }
            }

            totalGoalsLabel.setText("Savings Goals: " + count);
            totalSavedLabel.setText(
                    "Total Saved: ₹" +
                    totalSaved.setScale(2, RoundingMode.HALF_UP)
                            .toPlainString()
            );

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load savings goals.\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
