import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NavigationPanel extends JPanel {

    private final Color darkGreen = new Color(0, 91, 78);
    private final String fullName;
    private final String email;
    private final Runnable onLogout;

    public NavigationPanel(
            String fullName, String email, Runnable onLogout) {

        this.fullName = fullName;
        this.email = email;
        this.onLogout = onLogout;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(12, 22, 12, 22));

        JButton menuButton = new JButton("☰");
        menuButton.setFont(new Font("SansSerif", Font.PLAIN, 25));
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);

        JLabel logo = new JLabel("🍃 Finora");
        logo.setFont(new Font("Serif", Font.BOLD, 29));
        logo.setForeground(darkGreen);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setBackground(Color.WHITE);
        left.add(menuButton);
        left.add(logo);

        JButton profileButton = new JButton(fullName + "  ▾");
        profileButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        profileButton.setForeground(darkGreen);
        profileButton.setBackground(Color.WHITE);
        profileButton.setFocusPainted(false);

        add(left, BorderLayout.WEST);
        add(profileButton, BorderLayout.EAST);

        JPopupMenu navMenu = new JPopupMenu();
        addPageItem(navMenu, "Dashboard", "dashboard");
        addPageItem(navMenu, "Add Income", "income");
        addPageItem(navMenu, "Add Expense", "expense");
        addPageItem(navMenu, "Reports", "reports");
        addPageItem(navMenu, "Transaction History", "history");

        menuButton.addActionListener(e ->
                navMenu.show(menuButton, 0, menuButton.getHeight()));

        JPopupMenu userMenu = new JPopupMenu();

        JMenuItem profileItem = new JMenuItem("My Profile");
        profileItem.addActionListener(e -> {
            openNewPage(new ProfileFrame(fullName, email, onLogout));
        });

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                onLogout.run();
            }
        });

        userMenu.add(profileItem);
        userMenu.addSeparator();
        userMenu.add(logoutItem);

        profileButton.addActionListener(e ->
                userMenu.show(profileButton, 0, profileButton.getHeight()));
    }

    private void addPageItem(JPopupMenu menu, String title, String page) {
        JMenuItem item = new JMenuItem(title);

        item.addActionListener(e -> {
            switch (page) {
                case "dashboard":
                    openNewPage(new DashboardFrame(fullName, email));
                    break;

                case "income":
                    openNewPage(new AddIncomeFrame(fullName, email));
                    break;

                case "expense":
                    JOptionPane.showMessageDialog(this,
                            "The Add Expense page will be added next.");
                    break;

                case "reports":
                    JOptionPane.showMessageDialog(this,
                            "The Reports page will be added next.");
                    break;

                case "history":
                    JOptionPane.showMessageDialog(this,
                            "The Transaction History page will be added next.");
                    break;
            }
        });

        menu.add(item);
    }

    private void openNewPage(JFrame frame) {
        Window current = SwingUtilities.getWindowAncestor(this);
        if (current != null) {
            current.dispose();
        }
        frame.setVisible(true);
    }
}