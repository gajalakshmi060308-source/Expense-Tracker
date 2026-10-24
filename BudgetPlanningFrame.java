
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BudgetPlanningFrame extends JFrame {

    private final String fullName;
    private final String email;

    private final Color mint = new Color(235, 247, 239);
    private final Color darkGreen = new Color(30, 91, 65);
    private final Color lightGreen = new Color(218, 239, 224);
    private final Color textColor = new Color(45, 65, 52);
    private final Font normalFont =
            new Font("Times New Roman", Font.PLAIN, 17);
    private final Font boldFont =
            new Font("Times New Roman", Font.BOLD, 18);

    private final String[] defaultCategories = {
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

    private JTextField monthField;
    private JTable budgetTable;
    private DefaultTableModel model;

    private JLabel totalBudgetLabel;
    private JLabel totalSpentLabel;
    private JLabel remainingLabel;

    public BudgetPlanningFrame(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;

        setTitle("Finora | Budget Planning");
        setSize(1450, 900);
        setMinimumSize(new Dimension(1050, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createDatabaseTable();

        NavigationPanel navigation = new NavigationPanel(
            fullName,
            email,
            () -> {
                dispose();
                new FinoraLogin().setVisible(true);
            }
        );

        add(navigation, BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadBudgetData();
    }

    // Create the existing budget table if it does not exist
    private void createDatabaseTable() {
        String sql =
            "CREATE TABLE IF NOT EXISTS budgets (" +
            "budget_id INT PRIMARY KEY AUTO_INCREMENT, " +
            "user_id INT NOT NULL, " +
            "budget_month DATE NOT NULL, " +
            "category VARCHAR(60) NOT NULL, " +
            "budget_amount DECIMAL(12,2) NOT NULL, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "UNIQUE KEY unique_user_month_category " +
            "(user_id, budget_month, category), " +
            "FOREIGN KEY (user_id) REFERENCES users(user_id))";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            st.executeUpdate(sql);

        } catch (SQLException ex) {
            showError("Unable to create budget table:\n"
                    + ex.getMessage());
        }
    }

    // Main page layout
    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(mint);
        main.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(
            headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("Budget Planning");
        heading.setFont(new Font(
            "Times New Roman", Font.BOLD, 38));
        heading.setForeground(darkGreen);

        JLabel subtitle = new JLabel(
            "Plan your monthly budget and track your spending."
        );
        subtitle.setFont(normalFont);
        subtitle.setForeground(textColor);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(6));
        headingPanel.add(subtitle);

        JPanel monthPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 10, 5));
        monthPanel.setOpaque(false);

        JLabel monthLabel = new JLabel("Budget Month (yyyy-MM):");
        monthLabel.setFont(boldFont);

        monthField = new JTextField(
            YearMonth.now().toString(), 8);
        monthField.setFont(normalFont);

        JButton viewButton = new JButton("Load Month");
        styleButton(viewButton, lightGreen, darkGreen);
        viewButton.addActionListener(e -> loadBudgetData());

        monthPanel.add(monthLabel);
        monthPanel.add(monthField);
        monthPanel.add(viewButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(headingPanel, BorderLayout.NORTH);
        topPanel.add(monthPanel, BorderLayout.SOUTH);

        main.add(topPanel, BorderLayout.NORTH);

        // Budget table
        model = new DefaultTableModel(
            new Object[]{
                "Category",
                "Budget (₹)",
                "Spent (₹)",
                "Remaining (₹)",
                "Status"
            }, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column >= 1 && column <= 3) {
                    return BigDecimal.class;
                }
                return String.class;
            }
        };

        budgetTable = new JTable(model);
        budgetTable.setFont(normalFont);
        budgetTable.setRowHeight(36);
        budgetTable.setGridColor(new Color(218, 232, 221));
        budgetTable.setSelectionBackground(lightGreen);
        budgetTable.setSelectionForeground(darkGreen);

// Darker table heading
JTableHeader header = budgetTable.getTableHeader();

header.setBackground(new Color(0, 85, 67));
header.setForeground(Color.WHITE);
header.setFont(new Font("Times New Roman", Font.BOLD, 17));
header.setPreferredSize(new Dimension(0, 42));
header.setReorderingAllowed(false);

        budgetTable.getColumnModel().getColumn(0)
            .setPreferredWidth(220);
        budgetTable.getColumnModel().getColumn(1)
            .setPreferredWidth(140);
        budgetTable.getColumnModel().getColumn(2)
            .setPreferredWidth(140);
        budgetTable.getColumnModel().getColumn(3)
            .setPreferredWidth(160);
        budgetTable.getColumnModel().getColumn(4)
            .setPreferredWidth(150);

        JScrollPane tableScroll = new JScrollPane(budgetTable);
        tableScroll.setBorder(new LineBorder(
            new Color(218, 232, 221), 1, true));
        tableScroll.getViewport().setBackground(Color.WHITE);

        JPanel tablePanel = new JPanel(new BorderLayout(0, 12));
        tablePanel.setOpaque(false);

        JPanel tableHeading = new JPanel(
            new BorderLayout());
        tableHeading.setOpaque(false);

        JLabel tableTitle = new JLabel("Your Category Budgets");
        tableTitle.setFont(
            new Font("Times New Roman", Font.BOLD, 25));
        tableTitle.setForeground(darkGreen);

       JButton addCategoryButton = new JButton("+ Add Category");

addCategoryButton.setFont(
    new Font("Times New Roman", Font.BOLD, 18)
);
addCategoryButton.setForeground(Color.WHITE);
addCategoryButton.setBackground(darkGreen);
addCategoryButton.setFocusPainted(false);
addCategoryButton.setOpaque(true);
addCategoryButton.setBorder(
    new CompoundBorder(
        new LineBorder(new Color(155, 204, 173), 1, true),
        new EmptyBorder(10, 20, 10, 20)
    )
);
addCategoryButton.setCursor(
    new Cursor(Cursor.HAND_CURSOR)
);

addCategoryButton.addActionListener(e -> addNewCategory());

        tableHeading.add(tableTitle, BorderLayout.WEST);
        tableHeading.add(addCategoryButton, BorderLayout.EAST);

        tablePanel.add(tableHeading, BorderLayout.NORTH);
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        main.add(tablePanel, BorderLayout.CENTER);

        // Summary and action buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 15));
        bottomPanel.setOpaque(false);

        JPanel summary = new JPanel(new GridLayout(1, 3, 12, 0));
        summary.setBackground(Color.WHITE);
        summary.setBorder(new CompoundBorder(
            new LineBorder(new Color(218, 232, 221), 1, true),
            new EmptyBorder(18, 15, 18, 15)
        ));

        totalBudgetLabel = createSummaryLabel(
            "Total Budget", "₹0.00");
        totalSpentLabel = createSummaryLabel(
            "Total Spent", "₹0.00");
        remainingLabel = createSummaryLabel(
            "Remaining", "₹0.00");

        summary.add(totalBudgetLabel);
        summary.add(totalSpentLabel);
        summary.add(remainingLabel);

        JPanel buttons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 5));
        buttons.setOpaque(false);

        JButton clearButton = new JButton("Clear Form");
        styleButton(clearButton, lightGreen, darkGreen);
        clearButton.addActionListener(e -> clearForm());

        JButton saveButton = new JButton("Save Budget");
        styleButton(saveButton, darkGreen, Color.WHITE);
        saveButton.addActionListener(e -> saveBudget());

        buttons.add(clearButton);
        buttons.add(saveButton);

        bottomPanel.add(summary, BorderLayout.CENTER);
        bottomPanel.add(buttons, BorderLayout.SOUTH);

        main.add(bottomPanel, BorderLayout.SOUTH);

        return main;
    }

    private JLabel createSummaryLabel(
            String title, String initialValue) {

        JLabel label = new JLabel(
            "<html><center>" + title + "<br><br>" +
            "<b>" + initialValue + "</b></center></html>",
            SwingConstants.CENTER);

        label.setFont(
            new Font("Times New Roman", Font.PLAIN, 19));
        label.setForeground(darkGreen);

        return label;
    }

    private void styleButton(
            JButton button,
            Color background,
            Color foreground) {

        button.setFont(boldFont);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Add a custom category

private void addNewCategory() {
    JDialog dialog = new JDialog(this, "Add New Category", true);
    dialog.setSize(430, 260);
    dialog.setResizable(false);
    dialog.setLocationRelativeTo(this);
    dialog.setUndecorated(true);

    Color darkGreen = new Color(30, 95, 65);
    Color mintGreen = new Color(232, 245, 236);
    Color borderGreen = new Color(170, 210, 185);

    JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
    mainPanel.setBackground(Color.WHITE);
    mainPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(borderGreen, 2),
        BorderFactory.createEmptyBorder(0, 22, 20, 22)
    ));

    // Header
    JPanel header = new JPanel(new BorderLayout());
    header.setBackground(darkGreen);
    header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 12));

    JLabel title = new JLabel("Add New Category");
    title.setFont(new Font("Times New Roman", Font.BOLD, 21));
    title.setForeground(Color.WHITE);

    JButton closeButton = new JButton("×");
    closeButton.setFont(new Font("Arial", Font.PLAIN, 24));
    closeButton.setForeground(Color.WHITE);
    closeButton.setBackground(darkGreen);
    closeButton.setBorderPainted(false);
    closeButton.setFocusPainted(false);
    closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    closeButton.addActionListener(e -> dialog.dispose());

    header.add(title, BorderLayout.CENTER);
    header.add(closeButton, BorderLayout.EAST);

    // Form
    JPanel formPanel = new JPanel();
    formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

    JLabel prompt = new JLabel("Enter your budget category name");
    prompt.setFont(new Font("Times New Roman", Font.BOLD, 17));
    prompt.setForeground(darkGreen);
    prompt.setAlignmentX(Component.LEFT_ALIGNMENT);

    JTextField categoryField = new JTextField();
    categoryField.setFont(new Font("Times New Roman", Font.PLAIN, 17));
    categoryField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
    categoryField.setPreferredSize(new Dimension(350, 38));
    categoryField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(borderGreen, 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    ));
    categoryField.setAlignmentX(Component.LEFT_ALIGNMENT);

    formPanel.add(prompt);
    formPanel.add(Box.createVerticalStrut(12));
    formPanel.add(categoryField);

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    buttonPanel.setBackground(Color.WHITE);

    JButton cancelButton = new JButton("Cancel");
    JButton addButton = new JButton("Add Category");

    for (JButton button : new JButton[]{cancelButton, addButton}) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
    }

    cancelButton.setForeground(darkGreen);
    cancelButton.setBackground(mintGreen);
    cancelButton.setOpaque(true);
    cancelButton.setBorderPainted(false);
    addButton.setForeground(Color.WHITE);
    addButton.setBackground(darkGreen);
    addButton.setOpaque(true);
    addButton.setBorderPainted(false);

    cancelButton.addActionListener(e -> dialog.dispose());

    addButton.addActionListener(e -> {
        String category = categoryField.getText().trim();

        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(
                dialog, "Please enter a category name.",
                "Category Required", JOptionPane.WARNING_MESSAGE
            );
            categoryField.requestFocusInWindow();
            return;
        }

        if (category.length() > 60) {
            JOptionPane.showMessageDialog(
                dialog, "Category name cannot exceed 60 characters.",
                "Invalid Category", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (category.equalsIgnoreCase("Overall")) {
            JOptionPane.showMessageDialog(
                dialog, "'Overall' is a reserved category name.",
                "Invalid Category", JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            String existing = model.getValueAt(i, 0).toString();

            if (existing.equalsIgnoreCase(category)) {
                JOptionPane.showMessageDialog(
                    dialog, "This category already exists.",
                    "Duplicate Category", JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        model.addRow(new Object[]{
            category,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            "On Track"
        });

        dialog.dispose();
    });

    buttonPanel.add(cancelButton);
    buttonPanel.add(addButton);

    mainPanel.add(header, BorderLayout.NORTH);
    mainPanel.add(formPanel, BorderLayout.CENTER);
    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    dialog.setContentPane(mainPanel);

    categoryField.addActionListener(e -> addButton.doClick());

    dialog.setVisible(true);
    categoryField.requestFocusInWindow();
}


    // Load categories, budgets, and actual spending
    private void loadBudgetData() {
        if (model == null) {
            return;
        }

        YearMonth selectedMonth;

        try {
            selectedMonth = YearMonth.parse(
                monthField.getText().trim());
        } catch (DateTimeParseException ex) {
            showError("Enter the month in yyyy-MM format.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            int userId = getUserId(con);

            if (userId == -1) {
                showError("User not found. Please log in again.");
                return;
            }

            model.setRowCount(0);

            // Store category budgets for the selected month
            Map<String, BigDecimal> budgets =
                new LinkedHashMap<>();

            String budgetSql =
                "SELECT category, budget_amount " +
                "FROM budgets WHERE user_id = ? " +
                "AND budget_month = ? AND category <> 'Overall' " +
                "ORDER BY budget_id";

            try (PreparedStatement ps =
                    con.prepareStatement(budgetSql)) {

                ps.setInt(1, userId);
                ps.setDate(2, Date.valueOf(
                    selectedMonth.atDay(1)));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        budgets.put(
                            rs.getString("category"),
                            rs.getBigDecimal("budget_amount")
                        );
                    }
                }
            }

            // Add standard categories first
            for (String category : defaultCategories) {
                budgets.putIfAbsent(category, BigDecimal.ZERO);
            }

            // Calculate expenses by category for this month
            Map<String, BigDecimal> spending =
                new LinkedHashMap<>();

            String expenseSql =
                "SELECT e.category, COALESCE(SUM(e.amount), 0) AS spent " +
                "FROM expenses e " +
                "JOIN users u ON e.user_id = u.user_id " +
                "WHERE u.user_id = ? " +
                "AND e.expense_date >= ? " +
                "AND e.expense_date < ? " +
                "GROUP BY e.category";

            try (PreparedStatement ps =
                    con.prepareStatement(expenseSql)) {

                ps.setInt(1, userId);
                ps.setDate(2, Date.valueOf(
                    selectedMonth.atDay(1)));
                ps.setDate(3, Date.valueOf(
                    selectedMonth.plusMonths(1).atDay(1)));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        spending.put(
                            rs.getString("category"),
                            rs.getBigDecimal("spent")
                        );
                    }
                }
            }

            // Include any custom categories already used in expenses
            for (String category : spending.keySet()) {
                if (!category.equalsIgnoreCase("Overall")) {
                    budgets.putIfAbsent(category, BigDecimal.ZERO);
                }
            }

            for (Map.Entry<String, BigDecimal> entry
                    : budgets.entrySet()) {

                String category = entry.getKey();
                BigDecimal budget = entry.getValue();
                BigDecimal spent = spending.getOrDefault(
                    category, BigDecimal.ZERO);

                BigDecimal remaining = budget.subtract(spent);

                String status;

                if (spent.compareTo(budget) > 0) {
                    status = "Over Budget";
                } else {
                    status = "On Track";
                }

                model.addRow(new Object[]{
                    category, budget, spent, remaining, status
                });
            }

            updateSummary();

        } catch (SQLException ex) {
            showError("Unable to load budget data:\n"
                    + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Get the logged-in user's database ID
    private int getUserId(Connection con) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE email = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }

        return -1;
    }

    // Save category budgets and overall monthly budget
    private void saveBudget() {
        if (budgetTable.isEditing()) {
            budgetTable.getCellEditor().stopCellEditing();
        }

        YearMonth selectedMonth;

        try {
            selectedMonth = YearMonth.parse(
                monthField.getText().trim());
        } catch (DateTimeParseException ex) {
            showError("Enter the month in yyyy-MM format.");
            return;
        }

        Map<String, BigDecimal> values =
            new LinkedHashMap<>();

        BigDecimal totalBudget = BigDecimal.ZERO;

        for (int i = 0; i < model.getRowCount(); i++) {
            String category =
                model.getValueAt(i, 0).toString();

            if (category.equalsIgnoreCase("Overall")) {
                showError("'Overall' is reserved.");
                return;
            }

            BigDecimal amount;

            try {
                Object value = model.getValueAt(i, 1);

                amount = value instanceof BigDecimal
                    ? (BigDecimal) value
                    : new BigDecimal(value.toString().trim());

                if (amount.compareTo(BigDecimal.ZERO) < 0) {
                    showError(
                        "Budget cannot be negative: " + category);
                    return;
                }

            } catch (Exception ex) {
                showError(
                    "Enter a valid budget amount for " + category);
                return;
            }

            values.put(category, amount);
            totalBudget = totalBudget.add(amount);
        }

        String sql =
            "INSERT INTO budgets " +
            "(user_id, budget_month, category, budget_amount) " +
            "VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "budget_amount = VALUES(budget_amount)";

        try (Connection con = DBConnection.getConnection()) {
            int userId = getUserId(con);

            if (userId == -1) {
                showError("User not found. Please log in again.");
                return;
            }

            con.setAutoCommit(false);

            try (PreparedStatement ps =
                    con.prepareStatement(sql)) {

                Date monthDate = Date.valueOf(
                    selectedMonth.atDay(1));

                // Save every category budget
                for (Map.Entry<String, BigDecimal> entry
                        : values.entrySet()) {

                    ps.setInt(1, userId);
                    ps.setDate(2, monthDate);
                    ps.setString(3, entry.getKey());
                    ps.setBigDecimal(4, entry.getValue());
                    ps.addBatch();
                }

                // Preserve the Overall row for compatibility
                ps.setInt(1, userId);
                ps.setDate(2, monthDate);
                ps.setString(3, "Overall");
                ps.setBigDecimal(4, totalBudget);
                ps.addBatch();

                ps.executeBatch();
                con.commit();

            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }

            JOptionPane.showMessageDialog(
                this,
                "Budget saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            loadBudgetData();

        } catch (SQLException ex) {
            showError("Unable to save budget:\n"
                    + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Update the summary totals
    private void updateSummary() {
        if (model == null) {
            return;
        }

        BigDecimal totalBudget = BigDecimal.ZERO;
        BigDecimal totalSpent = BigDecimal.ZERO;

        for (int i = 0; i < model.getRowCount(); i++) {
            totalBudget = totalBudget.add(
                (BigDecimal) model.getValueAt(i, 1));

            totalSpent = totalSpent.add(
                (BigDecimal) model.getValueAt(i, 2));
        }

        BigDecimal remaining = totalBudget.subtract(totalSpent);

        totalBudgetLabel.setText(
            "<html><center>Total Budget<br><br><b>" +
            formatMoney(totalBudget) + "</b></center></html>");

        totalSpentLabel.setText(
            "<html><center>Total Spent<br><br><b>" +
            formatMoney(totalSpent) + "</b></center></html>");

        remainingLabel.setText(
            "<html><center>Remaining<br><br><b>" +
            formatMoney(remaining) + "</b></center></html>");

        remainingLabel.setForeground(
            remaining.compareTo(BigDecimal.ZERO) < 0
                ? Color.RED : darkGreen);
    }

    private String formatMoney(BigDecimal amount) {
        return "₹" + amount.setScale(
            2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    // Clear the category budgets for editing
    private void clearForm() {
        if (budgetTable.isEditing()) {
            budgetTable.getCellEditor().stopCellEditing();
        }

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(BigDecimal.ZERO, i, 1);
        }

        updateSummary();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Finora",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
