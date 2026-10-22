import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final Color darkGreen = new Color(0, 91, 78);
    private final Color mint = new Color(226, 247, 240);

    public DashboardFrame(String fullName, String email) {
        setTitle("Finora | Dashboard");
        setSize(1150, 750);
        setMinimumSize(new Dimension(950, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Runnable logout = () -> {
            dispose();
            new FinoraHero().setVisible(true);
        };

        add(new NavigationPanel(fullName, email, logout),
                BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 25));
        content.setBackground(mint);
        content.setBorder(new EmptyBorder(30, 35, 35, 35));

        JLabel welcome = new JLabel(
                "Welcome back, " + fullName + "!");
        welcome.setFont(new Font("Serif", Font.BOLD, 32));
        welcome.setForeground(darkGreen);

        JLabel subtitle = new JLabel(
                "Here is an overview of your financial activity.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setBackground(mint);
        heading.add(welcome);
        heading.add(Box.createVerticalStrut(8));
        heading.add(subtitle);

        JPanel cards = new JPanel(new GridLayout(1, 3, 18, 0));
        cards.setBackground(mint);
        cards.add(createCard("Total Income", "₹ 0.00"));
        cards.add(createCard("Total Expenses", "₹ 0.00"));
        cards.add(createCard("Balance", "₹ 0.00"));

        JPanel center = new JPanel(new BorderLayout(0, 25));
        center.setBackground(mint);
        center.add(heading, BorderLayout.NORTH);
        center.add(cards, BorderLayout.CENTER);

        JButton addIncome = new JButton("+ Add Income");
        addIncome.setFont(new Font("SansSerif", Font.BOLD, 16));
        addIncome.setBackground(darkGreen);
        addIncome.setForeground(Color.WHITE);
        addIncome.setFocusPainted(false);

        addIncome.addActionListener(e ->
                new AddIncomeFrame(fullName, email).setVisible(true));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBackground(mint);
        actions.add(addIncome);

        content.add(center, BorderLayout.NORTH);
        content.add(actions, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createCard(String title, String amount) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(213, 236, 230)),
                new EmptyBorder(22, 22, 22, 22)));

        JLabel label = new JLabel(title);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));

        JLabel value = new JLabel(amount);
        value.setForeground(darkGreen);
        value.setFont(new Font("SansSerif", Font.BOLD, 25));

        card.add(label);
        card.add(Box.createVerticalStrut(18));
        card.add(value);

        return card;
    }
}