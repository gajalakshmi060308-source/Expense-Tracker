
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionHistoryFrame extends JFrame {

    private final String fullName;
    private final String email;

    private final Color mint = new Color(235, 247, 239);
    private final Color darkGreen = new Color(30, 91, 65);
    private final Color textColor = new Color(45, 65, 52);
    private final Color borderColor = new Color(218, 232, 221);

    private JTable transactionTable;
    private DefaultTableModel tableModel;

    private JLabel totalLabel;
    private JLabel countLabel;

    public TransactionHistoryFrame(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;

        setTitle("Finora | Transaction History");
        setSize(1450, 900);
        setMinimumSize(new Dimension(1050, 720));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Navigation bar
        Runnable logoutAction = () -> {
            dispose();
            new FinoraLogin().setVisible(true);
        };

        NavigationPanel navigation =
                new NavigationPanel(fullName, email, logoutAction);

        add(navigation, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new BorderLayout(0, 22));
        content.setBackground(mint);
        content.setBorder(new EmptyBorder(35, 45, 35, 45));

        // Page heading
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(
                new BoxLayout(headingPanel, BoxLayout.Y_AXIS)
        );
        headingPanel.setOpaque(false);

        JLabel heading = new JLabel("Transaction History");
        heading.setFont(
                new Font("Times New Roman", Font.BOLD, 40)
        );
        heading.setForeground(darkGreen);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
                "View all your recorded expenses in one place."
        );
        subtitle.setFont(
                new Font("Times New Roman", Font.PLAIN, 19)
        );
        subtitle.setForeground(textColor);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(10));
        headingPanel.add(subtitle);

        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setPreferredSize(new Dimension(100, 100));

        totalLabel = new JLabel("Total Expenses: ₹0.00");
        countLabel = new JLabel("Transactions: 0");

        styleSummaryLabel(totalLabel);
        styleSummaryLabel(countLabel);

        summaryPanel.add(totalLabel);
        summaryPanel.add(countLabel);

        // Table columns
        String[] columns = {
                "Date",
                "Category",
                "Amount (₹)",
                "Payment Method",
                "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        transactionTable = new JTable(tableModel);
        transactionTable.setFont(
                new Font("Times New Roman", Font.PLAIN, 17)
        );
        transactionTable.setRowHeight(38);
        transactionTable.setBackground(Color.WHITE);
        transactionTable.setForeground(textColor);
        transactionTable.setGridColor(borderColor);
        transactionTable.setSelectionBackground(
                new Color(211, 245, 232)
        );
        transactionTable.setSelectionForeground(darkGreen);
        transactionTable.setShowVerticalLines(false);
        transactionTable.setFillsViewportHeight(true);
        transactionTable.setAutoCreateRowSorter(true);

        transactionTable.getTableHeader().setFont(
                new Font("Times New Roman", Font.BOLD, 17)
        );
        transactionTable.getTableHeader().setBackground(darkGreen);
        transactionTable.getTableHeader().setForeground(Color.WHITE);
        transactionTable.getTableHeader().setPreferredSize(
                new Dimension(100, 45)
        );
        transactionTable.getTableHeader().setReorderingAllowed(false);

        // Set reasonable column widths
        transactionTable.getColumnModel()
                .getColumn(0).setPreferredWidth(130);
        transactionTable.getColumnModel()
                .getColumn(1).setPreferredWidth(200);
        transactionTable.getColumnModel()
                .getColumn(2).setPreferredWidth(150);
        transactionTable.getColumnModel()
                .getColumn(3).setPreferredWidth(160);
        transactionTable.getColumnModel()
                .getColumn(4).setPreferredWidth(350);

        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBorder(
                new LineBorder(borderColor, 1)
        );
        scrollPane.getViewport().setBackground(Color.WHITE);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(
                new LineBorder(borderColor, 1)
        );
        tableCard.add(scrollPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh History");
        refreshButton.setFont(
                new Font("Times New Roman", Font.BOLD, 17)
        );
        refreshButton.setBackground(darkGreen);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(
                new EmptyBorder(12, 20, 12, 20)
        );
        refreshButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        refreshButton.addActionListener(e -> loadTransactions());

        JPanel bottomPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );
        bottomPanel.setOpaque(false);
        bottomPanel.add(refreshButton);

        JPanel topContent = new JPanel(new BorderLayout(0, 20));
        topContent.setOpaque(false);
        topContent.add(headingPanel, BorderLayout.NORTH);
        topContent.add(summaryPanel, BorderLayout.CENTER);

        JPanel centerContent = new JPanel(new BorderLayout(0, 15));
        centerContent.setOpaque(false);
        centerContent.add(topContent, BorderLayout.NORTH);
        centerContent.add(tableCard, BorderLayout.CENTER);
        centerContent.add(bottomPanel, BorderLayout.SOUTH);

        content.add(centerContent, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        // Load saved expenses when the page opens
        loadTransactions();
    }

    private void styleSummaryLabel(JLabel label) {
        label.setFont(
                new Font("Times New Roman", Font.BOLD, 22)
        );
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

    // Retrieve only the logged-in user's expenses
    private void loadTransactions() {
        tableModel.setRowCount(0);

        String sql =
                "SELECT e.expense_date, e.category, e.amount, " +
                "e.payment_method, e.description " +
                "FROM expenses e " +
                "JOIN users u ON e.user_id = u.user_id " +
                "WHERE u.email = ? " +
                "ORDER BY e.expense_date DESC";

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        int count = 0;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    java.sql.Date date =
                            rs.getDate("expense_date");

                    String category =
                            rs.getString("category");

                    java.math.BigDecimal amount =
                            rs.getBigDecimal("amount");

                    String paymentMethod =
                            rs.getString("payment_method");

                    String description =
                            rs.getString("description");

                    if (amount == null) {
                        amount = java.math.BigDecimal.ZERO;
                    }

                    tableModel.addRow(new Object[]{
                            date,
                            category,
                            amount.toPlainString(),
                            paymentMethod,
                            description == null || description.isBlank()
                                    ? "-"
                                    : description
                    });

                    total = total.add(amount);
                    count++;
                }
            }

            totalLabel.setText(
                    "Total Expenses: ₹" +
                    total.setScale(
                            2,
                            java.math.RoundingMode.HALF_UP
                    ).toPlainString()
            );

            countLabel.setText("Transactions: " + count);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load transaction history.\n" +
                    ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

            ex.printStackTrace();
        }
    }
}
