
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardFrame extends JFrame {

    private final String fullName;
    private final String email;

    private final Color darkGreen = new Color(0, 91, 72);
    private final Color buttonGreen = new Color(0, 119, 94);
    private final Color mint = new Color(229, 248, 241);
    private final Color pageBackground = new Color(246, 250, 247);
    private final Color textColor = new Color(45, 65, 52);
    private final Color mutedText = new Color(115, 135, 123);
    private final Color borderColor = new Color(220, 234, 224);
    private final Color incomeColor = new Color(45, 155, 112);
    private final Color expenseColor = new Color(231, 125, 104);

    // Existing fonts
    private final Font normalFont = new Font("Times New Roman", Font.PLAIN, 17);
    private final Font boldFont = new Font("Times New Roman", Font.BOLD, 18);
    private final Font sectionTitleFont = new Font("Times New Roman", Font.BOLD, 25);
    private final Font pageTitleFont = new Font("Times New Roman", Font.BOLD, 38);
    private final Font smallFont = new Font("Times New Roman", Font.PLAIN, 14);

    private JLabel incomeValue;
    private JLabel expenseValue;
    private JLabel budgetValue;
    private JLabel savingsValue;
    private JLabel budgetSubtitle;
    private JLabel goalsSubtitle;

    private BarChartPanel barChart;
    private DonutChartPanel donutChart;
    private DefaultTableModel transactionModel;
    private JPanel goalsList;

    private double monthlyIncome;
    private double monthlyExpenses;
    private double monthlyBudget;
    private double totalSavings;

    private final Map<String, Double> expenseBreakdown = new LinkedHashMap<>();
    private final List<GoalData> savingsGoals = new ArrayList<>();

    // Data for the Income vs Expenses chart, grouped by month.
    private final Map<YearMonth, Double> chartIncomeByMonth = new LinkedHashMap<>();
    private final Map<YearMonth, Double> chartExpensesByMonth = new LinkedHashMap<>();

    private final Color[] chartColors = {
        new Color(45, 155, 112),
        new Color(231, 125, 104),
        new Color(94, 139, 219),
        new Color(225, 178, 76),
        new Color(157, 111, 201),
        new Color(76, 171, 180),
        new Color(197, 113, 145)
    };

    public DashboardFrame(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;

        setTitle("Finora | Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1366, 850);
        setMinimumSize(new Dimension(950, 650));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(pageBackground);

        NavigationPanel navigation = new NavigationPanel(
            fullName,
            email,
            () -> {
                dispose();
                new FinoraHero().setVisible(true);
            }
        );
        add(navigation, BorderLayout.NORTH);

        JPanel content = buildDashboardContent();

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setBackground(pageBackground);
        scrollPane.getViewport().setBackground(pageBackground);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        add(scrollPane, BorderLayout.CENTER);

        loadDashboardData();
        refreshDashboard();
    }

    private JPanel buildDashboardContent() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(pageBackground);
        content.setBorder(new EmptyBorder(24, 30, 30, 30));

        // Page heading; no refresh button.
        JPanel headingPanel = new JPanel(new BorderLayout(10, 8));
        headingPanel.setOpaque(false);
        headingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        headingPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        JPanel headingTextPanel = new JPanel();
        headingTextPanel.setLayout(new BoxLayout(headingTextPanel, BoxLayout.Y_AXIS));
        headingTextPanel.setOpaque(false);

        JLabel heading = new JLabel("Dashboard");
        heading.setFont(pageTitleFont);
        heading.setForeground(darkGreen);

        JLabel welcome = new JLabel(
            "Welcome back, " + fullName + "! Here's your financial overview."
        );
        welcome.setFont(normalFont);
        welcome.setForeground(mutedText);

        headingTextPanel.add(heading);
        headingTextPanel.add(Box.createVerticalStrut(6));
        headingTextPanel.add(welcome);

        headingPanel.add(headingTextPanel, BorderLayout.WEST);

        content.add(headingPanel);
        content.add(Box.createVerticalStrut(22));

        // Summary cards
        JPanel summaryCards = new JPanel(new GridLayout(1, 4, 16, 0));
        summaryCards.setOpaque(false);
        summaryCards.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 135));

        incomeValue = new JLabel("₹0.00");
        expenseValue = new JLabel("₹0.00");
        budgetValue = new JLabel("N/A");
        savingsValue = new JLabel("₹0.00");
        budgetSubtitle = new JLabel("No budget set for this month");
        goalsSubtitle = new JLabel("0 savings goal(s)");

        summaryCards.add(createSummaryCard(
            "TOTAL INCOME", incomeValue,
            "Income recorded this month", incomeColor, "↗"
        ));

        summaryCards.add(createSummaryCard(
            "TOTAL EXPENSES", expenseValue,
            "Expenses recorded this month", expenseColor, "↘"
        ));

        summaryCards.add(createSummaryCard(
            "BUDGET USED", budgetValue,
            budgetSubtitle, new Color(94, 139, 219), "%"
        ));

        summaryCards.add(createSummaryCard(
            "TOTAL SAVINGS", savingsValue,
            goalsSubtitle, new Color(157, 111, 201), "★"
        ));

        content.add(summaryCards);
        content.add(Box.createVerticalStrut(22));

        // Charts side by side: existing layout is preserved.
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        barChart = new BarChartPanel();
        donutChart = new DonutChartPanel();

        chartsPanel.add(createPanelCard(
            "Income vs Expenses", "January - December", barChart
        ));

        chartsPanel.add(createPanelCard(
            "Expense Breakdown", "Expenses by category", donutChart
        ));

        content.add(chartsPanel);
        content.add(Box.createVerticalStrut(22));

        // Recent Transactions and Savings Goals
        JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 18, 0));
        lowerPanel.setOpaque(false);
        lowerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lowerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 370));
        lowerPanel.setPreferredSize(new Dimension(1200, 370));

        lowerPanel.add(buildTransactionsPanel());
        lowerPanel.add(buildSavingsPanel());

        content.add(lowerPanel);

        return content;
    }

    private JPanel createSummaryCard(
            String title, JLabel valueLabel, String subtitle,
            Color accent, String symbol) {

        JLabel subtitleLabel = new JLabel(subtitle);

        return createSummaryCard(
            title, valueLabel, subtitleLabel, accent, symbol
        );
    }

    private JPanel createSummaryCard(
            String title, JLabel valueLabel, JLabel subtitleLabel,
            Color accent, String symbol) {

        JPanel card = new JPanel(new BorderLayout(8, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(16, 17, 15, 17)
        ));

        JPanel top = new JPanel(new BorderLayout(6, 0));
        top.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
        titleLabel.setForeground(mutedText);

        JLabel symbolLabel = new JLabel(symbol, SwingConstants.CENTER);
        symbolLabel.setFont(new Font("Times New Roman", Font.BOLD, 17));
        symbolLabel.setForeground(accent);
        symbolLabel.setOpaque(true);
        symbolLabel.setBackground(new Color(
            Math.min(255, accent.getRed() + 100),
            Math.min(255, accent.getGreen() + 80),
            Math.min(255, accent.getBlue() + 80)
        ));
        symbolLabel.setPreferredSize(new Dimension(34, 34));

        top.add(titleLabel, BorderLayout.WEST);
        top.add(symbolLabel, BorderLayout.EAST);

        valueLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));
        valueLabel.setForeground(darkGreen);

        subtitleLabel.setFont(new Font("Times New Roman", Font.PLAIN, 14));
        subtitleLabel.setForeground(mutedText);

        card.add(top, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subtitleLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createPanelCard(
            String title, String subtitle, JComponent component) {

        JPanel card = new JPanel(new BorderLayout(10, 12));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(17, 18, 15, 18)
        ));

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(sectionTitleFont);
        titleLabel.setForeground(darkGreen);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(smallFont);
        subtitleLabel.setForeground(mutedText);

        headingPanel.add(titleLabel);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(subtitleLabel);

        component.setOpaque(false);

        card.add(headingPanel, BorderLayout.NORTH);
        card.add(component, BorderLayout.CENTER);

        return card;
    }

    private JPanel buildTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(17, 18, 17, 18)
        ));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Recent Transactions");
        title.setFont(sectionTitleFont);
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel("Your latest income and expenses");
        subtitle.setFont(smallFont);
        subtitle.setForeground(mutedText);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton viewAllButton = new JButton("View All");
        styleSecondaryButton(viewAllButton);

        viewAllButton.addActionListener(e -> {
            dispose();
            new TransactionHistoryFrame(fullName, email).setVisible(true);
        });

        header.add(titlePanel, BorderLayout.WEST);
        header.add(viewAllButton, BorderLayout.EAST);

        String[] columns = {
            "Date", "Type", "Category / Source", "Description", "Amount"
        };

        transactionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(transactionModel);
        styleTable(table);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(new LineBorder(borderColor, 1));
        tableScroll.getViewport().setBackground(Color.WHITE);

        panel.add(header, BorderLayout.NORTH);
        panel.add(tableScroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildSavingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(borderColor, 1),
            new EmptyBorder(17, 18, 17, 18)
        ));

        JPanel header = new JPanel(new BorderLayout(8, 8));
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Savings Goals");
        title.setFont(sectionTitleFont);
        title.setForeground(darkGreen);

        JLabel subtitle = new JLabel("Track your financial goals");
        subtitle.setFont(smallFont);
        subtitle.setForeground(mutedText);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        JButton viewAllButton = new JButton("View All");
        styleSecondaryButton(viewAllButton);

        viewAllButton.addActionListener(e -> {
            dispose();
            new SavingsGoalsFrame(fullName, email).setVisible(true);
        });

        header.add(titlePanel, BorderLayout.WEST);
        header.add(viewAllButton, BorderLayout.EAST);

        goalsList = new JPanel();
        goalsList.setLayout(new BoxLayout(goalsList, BoxLayout.Y_AXIS));
        goalsList.setBackground(Color.WHITE);

        JScrollPane goalsScroll = new JScrollPane(goalsList);
        goalsScroll.setBorder(null);
        goalsScroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        goalsScroll.getVerticalScrollBar().setUnitIncrement(14);
        goalsScroll.getViewport().setBackground(Color.WHITE);

        panel.add(header, BorderLayout.NORTH);
        panel.add(goalsScroll, BorderLayout.CENTER);

        return panel;
    }

    private void loadDashboardData() {
        monthlyIncome = 0;
        monthlyExpenses = 0;
        monthlyBudget = 0;
        totalSavings = 0;

        expenseBreakdown.clear();
        savingsGoals.clear();
        chartIncomeByMonth.clear();
        chartExpensesByMonth.clear();

        YearMonth currentMonth = YearMonth.now();

        LocalDate start = currentMonth.atDay(1);
        LocalDate nextMonth = currentMonth.plusMonths(1).atDay(1);

        // CHANGED: The chart starts in January of the current calendar year.
        YearMonth firstChartMonth = YearMonth.of(currentMonth.getYear(), 1);
        LocalDate chartStart = firstChartMonth.atDay(1);

        // End date for the chart is January 1 of the following year.
        LocalDate nextYear = currentMonth.plusYears(1).withMonth(1).atDay(1);

        // Initialise all 12 months to zero so months without transactions
        // are still displayed in the chart.
        for (int i = 0; i < 12; i++) {
            YearMonth month = firstChartMonth.plusMonths(i);
            chartIncomeByMonth.put(month, 0.0);
            chartExpensesByMonth.put(month, 0.0);
        }

        try (Connection con = DBConnection.getConnection()) {

            // Existing current-month income query
            String incomeSql =
                "SELECT COALESCE(SUM(amount), 0) FROM incomes " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND income_date >= ? AND income_date < ?";

            try (PreparedStatement ps = con.prepareStatement(incomeSql)) {
                ps.setString(1, email);
                ps.setDate(2, Date.valueOf(start));
                ps.setDate(3, Date.valueOf(nextMonth));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        monthlyIncome = rs.getDouble(1);
                    }
                }
            }

            // Existing current-month expenses query
            String expenseSql =
                "SELECT COALESCE(SUM(amount), 0) FROM expenses " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND expense_date >= ? AND expense_date < ?";

            try (PreparedStatement ps = con.prepareStatement(expenseSql)) {
                ps.setString(1, email);
                ps.setDate(2, Date.valueOf(start));
                ps.setDate(3, Date.valueOf(nextMonth));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        monthlyExpenses = rs.getDouble(1);
                    }
                }
            }

            // Monthly income totals for January through December.
            String monthlyIncomeSql =
                "SELECT YEAR(income_date) AS transaction_year, " +
                "MONTH(income_date) AS transaction_month, " +
                "COALESCE(SUM(amount), 0) AS total " +
                "FROM incomes " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND income_date >= ? AND income_date < ? " +
                "GROUP BY YEAR(income_date), MONTH(income_date) " +
                "ORDER BY transaction_year, transaction_month";

            try (PreparedStatement ps = con.prepareStatement(monthlyIncomeSql)) {
                ps.setString(1, email);
                ps.setDate(2, Date.valueOf(chartStart));
                ps.setDate(3, Date.valueOf(nextYear));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        YearMonth month = YearMonth.of(
                            rs.getInt("transaction_year"),
                            rs.getInt("transaction_month")
                        );

                        chartIncomeByMonth.put(month, rs.getDouble("total"));
                    }
                }
            }

            // Monthly expense totals for January through December.
            String monthlyExpenseSql =
                "SELECT YEAR(expense_date) AS transaction_year, " +
                "MONTH(expense_date) AS transaction_month, " +
                "COALESCE(SUM(amount), 0) AS total " +
                "FROM expenses " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND expense_date >= ? AND expense_date < ? " +
                "GROUP BY YEAR(expense_date), MONTH(expense_date) " +
                "ORDER BY transaction_year, transaction_month";

            try (PreparedStatement ps = con.prepareStatement(monthlyExpenseSql)) {
                ps.setString(1, email);
                ps.setDate(2, Date.valueOf(chartStart));
                ps.setDate(3, Date.valueOf(nextYear));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        YearMonth month = YearMonth.of(
                            rs.getInt("transaction_year"),
                            rs.getInt("transaction_month")
                        );

                        chartExpensesByMonth.put(month, rs.getDouble("total"));
                    }
                }
            }

            // Existing budget query
            String budgetSql =
                "SELECT COALESCE(SUM(budget_amount), 0) FROM budgets " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND budget_month = ?";

            try (PreparedStatement ps = con.prepareStatement(budgetSql)) {
                ps.setString(1, email);
                ps.setDate(2, Date.valueOf(start));

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        monthlyBudget = rs.getDouble(1);
                    }
                }
            }

            // Existing expense breakdown query
            String breakdownSql =
                "SELECT category, SUM(amount) AS total FROM expenses " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "AND expense_date >= ? AND expense_date < ? " +
                "GROUP BY category ORDER BY total DESC";

            try (PreparedStatement ps = con.prepareStatement(breakdownSql)) {
                ps.setString(1, email);
                ps.setDate(2, Date.valueOf(start));
                ps.setDate(3, Date.valueOf(nextMonth));

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        expenseBreakdown.put(
                            rs.getString("category"),
                            rs.getDouble("total")
                        );
                    }
                }
            }

            // Existing savings goals query
            String goalsSql =
                "SELECT goal_name, target_amount, saved_amount, target_date " +
                "FROM savings_goals " +
                "WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
                "ORDER BY created_at DESC";

            try (PreparedStatement ps = con.prepareStatement(goalsSql)) {
                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GoalData goal = new GoalData(
                            rs.getString("goal_name"),
                            rs.getDouble("target_amount"),
                            rs.getDouble("saved_amount"),
                            rs.getDate("target_date")
                        );

                        savingsGoals.add(goal);
                        totalSavings += goal.saved;
                    }
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load dashboard data.\n" +
                "Please check your database connection and tables.\n\nDetails: " +
                ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void refreshDashboard() {
        incomeValue.setText(formatMoney(monthlyIncome));
        expenseValue.setText(formatMoney(monthlyExpenses));
        savingsValue.setText(formatMoney(totalSavings));

        if (monthlyBudget > 0) {
            double percent = monthlyExpenses / monthlyBudget * 100.0;

            budgetValue.setText(
                String.format(Locale.US, "%.1f%%", percent)
            );

            budgetSubtitle.setText(
                formatMoney(monthlyExpenses) + " of " +
                formatMoney(monthlyBudget)
            );
        } else {
            budgetValue.setText("N/A");
            budgetSubtitle.setText("No budget set for this month");
        }

        goalsSubtitle.setText(savingsGoals.size() + " savings goal(s)");

        barChart.repaint();
        donutChart.repaint();

        loadRecentTransactions();
        loadSavingsGoals();
    }

    private void loadRecentTransactions() {
        transactionModel.setRowCount(0);

        String sql =
            "SELECT transaction_date, transaction_type, item_name, description, amount " +
            "FROM (" +
            " SELECT income_date AS transaction_date, 'Income' AS transaction_type, " +
            " source AS item_name, description, amount " +
            " FROM incomes WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
            " UNION ALL " +
            " SELECT expense_date AS transaction_date, 'Expense' AS transaction_type, " +
            " category AS item_name, description, amount " +
            " FROM expenses WHERE user_id = (SELECT user_id FROM users WHERE email = ?) " +
            ") AS transactions ORDER BY transaction_date DESC LIMIT 8";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date date = rs.getDate("transaction_date");

                    String dateText = date == null ? "-" :
                        date.toLocalDate().format(
                            DateTimeFormatter.ofPattern("dd MMM yyyy")
                        );

                    transactionModel.addRow(new Object[]{
                        dateText,
                        rs.getString("transaction_type"),
                        rs.getString("item_name"),
                        rs.getString("description") == null
                            ? "-" : rs.getString("description"),
                        formatMoney(rs.getDouble("amount"))
                    });
                }
            }

        } catch (SQLException ex) {
            showTableError("recent transactions", ex);
        }
    }

    private void loadSavingsGoals() {
        goalsList.removeAll();

        if (savingsGoals.isEmpty()) {
            JLabel emptyLabel = new JLabel(
                "No savings goals yet. Add one from Savings Goals."
            );

            emptyLabel.setFont(normalFont);
            emptyLabel.setForeground(mutedText);
            emptyLabel.setBorder(new EmptyBorder(15, 2, 15, 2));

            goalsList.add(emptyLabel);
        } else {
            int count = Math.min(savingsGoals.size(), 5);

            for (int i = 0; i < count; i++) {
                goalsList.add(createGoalRow(savingsGoals.get(i)));
                goalsList.add(Box.createVerticalStrut(12));
            }
        }

        goalsList.revalidate();
        goalsList.repaint();
    }

    private JPanel createGoalRow(GoalData goal) {
        JPanel row = new JPanel(new BorderLayout(12, 7));
        row.setBackground(Color.WHITE);
        row.setBorder(new EmptyBorder(5, 2, 5, 2));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        JPanel details = new JPanel(new BorderLayout(10, 0));
        details.setOpaque(false);

        JLabel name = new JLabel(goal.name);
        name.setFont(boldFont);
        name.setForeground(textColor);

        JLabel saved = new JLabel(
            formatMoney(goal.saved) + " saved of " + formatMoney(goal.target)
        );
        saved.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        saved.setForeground(mutedText);

        details.add(name, BorderLayout.WEST);
        details.add(saved, BorderLayout.EAST);

        JProgressBar progress = new JProgressBar(0, 100);

        int percent = goal.target <= 0 ? 0 :
            (int) Math.round(goal.saved / goal.target * 100.0);

        percent = Math.max(0, Math.min(100, percent));

        progress.setValue(percent);
        progress.setStringPainted(true);
        progress.setString(percent + "%");
        progress.setFont(new Font("Times New Roman", Font.BOLD, 14));
        progress.setForeground(buttonGreen);
        progress.setBackground(mint);
        progress.setBorderPainted(false);
        progress.setPreferredSize(new Dimension(100, 22));

        row.add(details, BorderLayout.NORTH);
        row.add(progress, BorderLayout.CENTER);

        return row;
    }

    private void styleTable(JTable table) {
        table.setFont(normalFont);
        table.setForeground(textColor);
        table.setBackground(Color.WHITE);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(borderColor);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setSelectionBackground(mint);
        table.setSelectionForeground(darkGreen);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = table.getTableHeader();
        header.setFont(boldFont);
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(0, 85, 67));
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);
    }

    private void stylePrimaryButton(JButton button) {
        button.setFont(boldFont);
        button.setForeground(Color.WHITE);
        button.setBackground(buttonGreen);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
    }

    private void styleSecondaryButton(JButton button) {
        button.setFont(new Font("Times New Roman", Font.BOLD, 16));
        button.setForeground(darkGreen);
        button.setBackground(mint);
        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(new CompoundBorder(
            new LineBorder(new Color(190, 220, 201), 1, true),
            new EmptyBorder(8, 14, 8, 14)
        ));

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private String formatMoney(double amount) {
        return String.format(Locale.US, "₹%,.2f", amount);
    }

    private void showTableError(String tableName, SQLException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Could not load " + tableName + ".\nDetails: " + ex.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static class GoalData {
        String name;
        double target;
        double saved;
        Date targetDate;

        GoalData(String name, double target, double saved, Date targetDate) {
            this.name = name;
            this.target = target;
            this.saved = saved;
            this.targetDate = targetDate;
        }
    }

    // Monthly income and expense bars from January through December.
    private class BarChartPanel extends JPanel {

        BarChartPanel() {
            setPreferredSize(new Dimension(350, 220));
            setMinimumSize(new Dimension(250, 180));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            int left = 48;
            int right = 10;
            int top = 32;
            int bottom = 43;

            int chartWidth = width - left - right;
            int chartHeight = height - top - bottom;

            if (chartWidth <= 0 || chartHeight <= 0) {
                g.dispose();
                return;
            }

            // Find the maximum value across all 12 months.
            double max = 0;

            for (double value : chartIncomeByMonth.values()) {
                max = Math.max(max, value);
            }

            for (double value : chartExpensesByMonth.values()) {
                max = Math.max(max, value);
            }

            if (max <= 0) {
                max = 100;
            }

            max = max * 1.15;

            // Draw legend.
            g.setFont(new Font("Times New Roman", Font.PLAIN, 12));

            int legendY = 12;

            g.setColor(incomeColor);
            g.fillRect(left + 4, legendY - 7, 9, 9);

            g.setColor(textColor);
            g.drawString("Income", left + 18, legendY + 1);

            g.setColor(expenseColor);
            g.fillRect(left + 85, legendY - 7, 9, 9);

            g.setColor(textColor);
            g.drawString("Expenses", left + 99, legendY + 1);

            // Draw horizontal grid lines and amount labels.
            for (int i = 0; i <= 4; i++) {
                int y = top + chartHeight - (i * chartHeight / 4);
                double value = max * i / 4.0;

                g.setColor(new Color(230, 238, 232));
                g.drawLine(left, y, left + chartWidth, y);

                g.setColor(mutedText);
                g.drawString(shortMoney(value), 2, y + 4);
            }

            // CHANGED: Always start the chart at January of the current year.
            YearMonth firstMonth = YearMonth.of(YearMonth.now().getYear(), 1);

            int monthCount = 12;
            double slotWidth = (double) chartWidth / monthCount;
            int barWidth = Math.max(
                3,
                Math.min(12, (int) (slotWidth / 3.0))
            );

            g.setFont(new Font("Times New Roman", Font.PLAIN, 11));

            for (int i = 0; i < monthCount; i++) {
                YearMonth month = firstMonth.plusMonths(i);

                double income = chartIncomeByMonth.getOrDefault(month, 0.0);
                double expenses = chartExpensesByMonth.getOrDefault(month, 0.0);

                int centerX = left + (int) ((i + 0.5) * slotWidth);

                int incomeHeight = (int) Math.round(
                    income / max * chartHeight
                );

                int expenseHeight = (int) Math.round(
                    expenses / max * chartHeight
                );

                int incomeX = centerX - barWidth;
                int expenseX = centerX;

                int incomeY = top + chartHeight - incomeHeight;
                int expenseY = top + chartHeight - expenseHeight;

                // Income bar
                g.setColor(incomeColor);
                g.fillRoundRect(
                    incomeX,
                    incomeY,
                    barWidth,
                    Math.max(0, incomeHeight),
                    5,
                    5
                );

                // Expenses bar
                g.setColor(expenseColor);
                g.fillRoundRect(
                    expenseX,
                    expenseY,
                    barWidth,
                    Math.max(0, expenseHeight),
                    5,
                    5
                );

                // Month label
                String monthLabel = month.format(
                    DateTimeFormatter.ofPattern("MMM", Locale.ENGLISH)
                );

                FontMetrics metrics = g.getFontMetrics();

                int labelX = centerX -
                    metrics.stringWidth(monthLabel) / 2;

                g.setColor(textColor);
                g.drawString(
                    monthLabel,
                    labelX,
                    top + chartHeight + 18
                );
            }

            g.dispose();
        }

        private String shortMoney(double value) {
            if (value >= 10000000) {
                return String.format(Locale.US, "%.1fCr", value / 10000000);
            }

            if (value >= 100000) {
                return String.format(Locale.US, "%.1fL", value / 100000);
            }

            if (value >= 1000) {
                return String.format(Locale.US, "%.1fK", value / 1000);
            }

            return String.format(Locale.US, "%.0f", value);
        }
    }

    // Existing expense breakdown donut chart remains unchanged.
    private class DonutChartPanel extends JPanel {

        DonutChartPanel() {
            setPreferredSize(new Dimension(350, 220));
            setMinimumSize(new Dimension(250, 180));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int width = getWidth();
            int height = getHeight();

            int diameter = Math.max(80, Math.min(145, height - 25));
            int x = 12;
            int y = Math.max(5, (height - diameter) / 2);

            double total = 0;

            for (double amount : expenseBreakdown.values()) {
                total += amount;
            }

            if (total <= 0) {
                g.setColor(mint);
                g.fillOval(x, y, diameter, diameter);

                g.setColor(darkGreen);
                g.setFont(new Font("Times New Roman", Font.BOLD, 14));

                drawCentered(
                    g,
                    "No expenses",
                    x + diameter / 2,
                    y + diameter / 2 + 5
                );

                g.dispose();
                return;
            }

            int startAngle = 90;
            int colorIndex = 0;

            for (double amount : expenseBreakdown.values()) {
                int arcAngle = (int) Math.round(amount / total * 360.0);

                g.setColor(chartColors[colorIndex % chartColors.length]);

                g.fill(new Arc2D.Double(
                    x, y, diameter, diameter,
                    startAngle, -arcAngle, Arc2D.PIE
                ));

                startAngle -= arcAngle;
                colorIndex++;
            }

            int inner = diameter / 2;
            int innerX = x + (diameter - inner) / 2;
            int innerY = y + (diameter - inner) / 2;

            g.setColor(Color.WHITE);
            g.fillOval(innerX, innerY, inner, inner);

            g.setColor(darkGreen);
            g.setFont(new Font("Times New Roman", Font.BOLD, 14));

            drawCentered(
                g,
                "Total",
                x + diameter / 2,
                y + diameter / 2 - 2
            );

            g.setFont(new Font("Times New Roman", Font.PLAIN, 12));

            drawCentered(
                g,
                compactMoney(total),
                x + diameter / 2,
                y + diameter / 2 + 15
            );

            int legendX = x + diameter + 18;
            int legendY = 22;
            int legendWidth = width - legendX - 5;

            g.setFont(new Font("Times New Roman", Font.PLAIN, 13));

            FontMetrics metrics = g.getFontMetrics();
            colorIndex = 0;

            for (Map.Entry<String, Double> entry : expenseBreakdown.entrySet()) {
                if (legendY > height - 12) {
                    g.setColor(mutedText);
                    g.drawString("...", legendX, legendY);
                    break;
                }

                g.setColor(chartColors[colorIndex % chartColors.length]);
                g.fillRoundRect(legendX, legendY - 9, 10, 10, 3, 3);

                g.setColor(textColor);

                String category = entry.getKey() == null
                    ? "Other" : entry.getKey();

                int maxTextWidth = Math.max(20, legendWidth - 20);

                while (
                    metrics.stringWidth(category) > maxTextWidth &&
                    category.length() > 4
                ) {
                    category = category.substring(
                        0, category.length() - 4
                    ) + "...";
                }

                g.drawString(category, legendX + 16, legendY);

                legendY += 24;
                colorIndex++;
            }

            g.dispose();
        }

        private void drawCentered(Graphics2D g, String text, int x, int y) {
            FontMetrics metrics = g.getFontMetrics();

            g.drawString(
                text,
                x - metrics.stringWidth(text) / 2,
                y
            );
        }

        private String compactMoney(double value) {
            if (value >= 100000) {
                return String.format(Locale.US, "₹%.1fL", value / 100000);
            }

            if (value >= 1000) {
                return String.format(Locale.US, "₹%.1fK", value / 1000);
            }

            return String.format(Locale.US, "₹%.0f", value);
        }
    }
}
