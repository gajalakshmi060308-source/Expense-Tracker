
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NavigationPanel extends JPanel {

    private final Color darkGreen = new Color(0, 119, 94);
    private final Color mint = new Color(229, 248, 241);
    private final Color hoverGreen = new Color(211, 243, 232);
    private final Color popupBorder = new Color(170, 210, 195);

    private final String fullName;
    private final String email;
    private final Runnable onLogout;

    private JFrame ownerFrame;
    private JPanel drawerPanel;
    private boolean drawerOpen = false;
    private Timer drawerTimer;

    private final int drawerWidth = 250;

    public NavigationPanel(
            String fullName, String email, Runnable onLogout) {

        this.fullName = fullName;
        this.email = email;
        this.onLogout = onLogout;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
setBorder(new EmptyBorder(5, 22, 5, 25));
setPreferredSize(new Dimension(100, 90));

        // Hamburger button
        JButton menuButton = new JButton("\u2630");
        menuButton.setFont(new Font("SansSerif", Font.PLAIN, 25));
        menuButton.setForeground(new Color(45, 55, 55));
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);
        menuButton.setMargin(new Insets(0, 0, 0, 0));
        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Finora logo - larger and centered vertically
        // Finora logo
JLabel logoImage = new JLabel();

ImageIcon originalLogo =
        new ImageIcon("images/logo.png.jpeg");

if (originalLogo.getIconWidth() > 0) {

    int logoWidth = 220;
    int logoHeight = 70;

    Image scaledImage = originalLogo.getImage()
            .getScaledInstance(
                    logoWidth, logoHeight, Image.SCALE_SMOOTH);

    logoImage.setIcon(new ImageIcon(scaledImage));

} else {
    logoImage.setText("Finora");
    logoImage.setFont(
            new Font("Times New Roman", Font.BOLD, 34));
    logoImage.setForeground(darkGreen);
}

logoImage.setPreferredSize(new Dimension(220, 70));
logoImage.setHorizontalAlignment(SwingConstants.LEFT);
logoImage.setVerticalAlignment(SwingConstants.CENTER);

        // Left side: hamburger and logo
        // Hamburger and logo alignment
JPanel left = new JPanel(
        new FlowLayout(FlowLayout.LEFT, 12, 0));

left.setBackground(Color.WHITE);
left.setOpaque(true);
left.setAlignmentY(Component.CENTER_ALIGNMENT);

menuButton.setPreferredSize(new Dimension(35, 46));

left.add(menuButton);
left.add(logoImage);

        // Custom white dropdown arrow
        Icon arrowIcon = new Icon() {

            @Override
            public int getIconWidth() {
                return 12;
            }

            @Override
            public int getIconHeight() {
                return 8;
            }

            @Override
            public void paintIcon(
                    Component component,
                    Graphics graphics,
                    int x,
                    int y) {

                Graphics2D g2 = (Graphics2D) graphics.create();

                g2.setColor(Color.WHITE);
                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int[] xs = {x, x + 12, x + 6};
                int[] ys = {y, y, y + 7};

                g2.fillPolygon(xs, ys, 3);
                g2.dispose();
            }
        };

        // Profile button with a user symbol
JButton profileButton =
        new JButton(fullName, arrowIcon);

        profileButton.setFont(
                new Font("Times New Roman", Font.BOLD, 18));
        profileButton.setForeground(Color.WHITE);
        profileButton.setBackground(darkGreen);
        profileButton.setHorizontalAlignment(SwingConstants.CENTER);
        profileButton.setHorizontalTextPosition(SwingConstants.LEFT);
        profileButton.setVerticalAlignment(SwingConstants.CENTER);
        profileButton.setIconTextGap(12);
        profileButton.setMargin(new Insets(0, 12, 0, 12));
        profileButton.setFocusPainted(false);
        profileButton.setBorderPainted(false);
        profileButton.setContentAreaFilled(true);
        profileButton.setOpaque(true);
        profileButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        // Adjust width to fit the profile name and icons
        FontMetrics metrics =
                profileButton.getFontMetrics(profileButton.getFont());

        int profileWidth = metrics.stringWidth(
                "\u263A  " + fullName)
                + arrowIcon.getIconWidth()
                + profileButton.getIconTextGap()
                + 32;

        // Profile button size
profileButton.setPreferredSize(
        new Dimension(Math.max(175, profileWidth), 46));

// Position the profile button at the right
JPanel right = new JPanel(
        new FlowLayout(FlowLayout.RIGHT, 0, 0));

right.setBackground(Color.WHITE);
right.setOpaque(true);
right.add(profileButton);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);

        // Create sidebar
        createDrawer();

        menuButton.addActionListener(e -> toggleDrawer());

        // Profile popup menu
        
        // Profile popup menu
        JPopupMenu userMenu = new JPopupMenu();
        userMenu.setBackground(Color.WHITE);
        userMenu.setLightWeightPopupEnabled(false);

        userMenu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(popupBorder, 1),
                BorderFactory.createEmptyBorder(3, 3, 3, 3)));

        // My Profile item with icon
        JMenuItem profileItem =
                createProfileMenuItem(" My Profile");

        profileItem.setIcon(new MenuIcon(true));


        profileItem.addActionListener(e ->
                openNewPage(
                        new ProfileFrame(fullName, email, onLogout)));

        // Logout item with icon
        JMenuItem logoutItem =
                createProfileMenuItem(" Logout");
logoutItem.setIcon(new MenuIcon(false));

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

        // Style the separator
        for (Component component : userMenu.getComponents()) {
            if (component instanceof JPopupMenu.Separator) {
                component.setBackground(popupBorder);
                component.setPreferredSize(new Dimension(180, 1));
            }
        }

        // Show popup below the profile button, aligned right
        profileButton.addActionListener(e -> {

            int popupX = Math.max(
                    0,
                    profileButton.getWidth()
                            - userMenu.getPreferredSize().width);

            userMenu.show(
                    profileButton,
                    popupX,
                    profileButton.getHeight());
        });
    }

    
private static class MenuIcon implements Icon {

    private final boolean profile;

    MenuIcon(boolean profile) {
        this.profile = profile;
    }

    @Override
    public int getIconWidth() {
        return 20;
    }

    @Override
    public int getIconHeight() {
        return 20;
    }

    @Override
    public void paintIcon(
            Component c, Graphics g, int x, int y) {

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(0, 119, 94));
        g2.setStroke(new BasicStroke(1.8f));

        if (profile) {
            // Head
            g2.drawOval(x + 7, y + 2, 7, 7);

            // Shoulders/body
            g2.drawArc(x + 3, y + 8, 15, 12, 0, 180);
        } else {
            // Logout door
            g2.drawRect(x + 2, y + 2, 9, 15);

            // Arrow pointing out
            g2.drawLine(x + 7, y + 10, x + 18, y + 10);
            g2.drawLine(x + 14, y + 6, x + 18, y + 10);
            g2.drawLine(x + 14, y + 14, x + 18, y + 10);
        }

        g2.dispose();
    }
}

    // Style profile popup menu items
    private JMenuItem createProfileMenuItem(String text) {

        JMenuItem item = new JMenuItem(text);

        item.setFont(
                new Font("Segoe UI Symbol", Font.PLAIN, 17));
        item.setForeground(darkGreen);
        item.setBackground(Color.WHITE);
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(
                10, 14, 10, 20));
        item.setPreferredSize(new Dimension(180, 40));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(hoverGreen);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(Color.WHITE);
            }
        });

        return item;
    }

    // Build the sidebar
    private void createDrawer() {

        drawerPanel = new JPanel(new BorderLayout());
        drawerPanel.setBackground(Color.WHITE);
        drawerPanel.setBorder(
                BorderFactory.createMatteBorder(
                        0, 0, 0, 1, new Color(195, 229, 218)));

        // Sidebar heading
        JPanel drawerHeader = new JPanel(new BorderLayout());
        drawerHeader.setBackground(mint);
        drawerHeader.setBorder(
                new EmptyBorder(20, 18, 20, 14));

        JLabel title = new JLabel("Finora Menu");
        title.setFont(new Font("Serif", Font.BOLD, 23));
        title.setForeground(darkGreen);

        JButton closeButton = new JButton("\u2715");
        closeButton.setFont(
                new Font("SansSerif", Font.PLAIN, 18));
        closeButton.setForeground(darkGreen);
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        closeButton.addActionListener(e -> toggleDrawer());

        drawerHeader.add(title, BorderLayout.WEST);
        drawerHeader.add(closeButton, BorderLayout.EAST);

        // Sidebar menu items
        JPanel items = new JPanel();
        items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));
        items.setBackground(Color.WHITE);
        items.setBorder(new EmptyBorder(18, 10, 18, 10));

        addDrawerItem(items, "\u2302   Dashboard", "dashboard");
        addDrawerItem(items, "\uFF0B   Add Income", "income");
        addDrawerItem(items, "\u2212   Add Expense", "expense");
        addDrawerItem(items, "\uD83D\uDCB0   Budget Planning", "budget");
        addDrawerItem(items, "\u25A5   Reports", "reports");
        addDrawerItem(items, "\u25F7   Transaction History", "history");

        drawerPanel.add(drawerHeader, BorderLayout.NORTH);
        drawerPanel.add(items, BorderLayout.CENTER);

        // Start hidden
        drawerPanel.setPreferredSize(new Dimension(0, 100));
    }

    // Show or hide sidebar with sliding-width animation
    private void toggleDrawer() {

        Window window = SwingUtilities.getWindowAncestor(this);

        if (!(window instanceof JFrame)) {
            return;
        }

        ownerFrame = (JFrame) window;

        if (drawerTimer != null && drawerTimer.isRunning()) {
            drawerTimer.stop();
        }

        Container content = ownerFrame.getContentPane();

        if (!drawerOpen) {

            drawerOpen = true;

            drawerPanel.setPreferredSize(
                    new Dimension(0, content.getHeight()));

            content.add(drawerPanel, BorderLayout.WEST);
            content.revalidate();
            content.repaint();

            drawerTimer = new Timer(8, null);

            drawerTimer.addActionListener(e -> {

                int width = drawerPanel.getPreferredSize().width;
                int nextWidth = Math.min(width + 25, drawerWidth);

                drawerPanel.setPreferredSize(
                        new Dimension(nextWidth, content.getHeight()));

                content.revalidate();
                content.repaint();

                if (nextWidth >= drawerWidth) {
                    drawerTimer.stop();
                }
            });

            drawerTimer.start();

        } else {

            drawerOpen = false;

            drawerTimer = new Timer(8, null);

            drawerTimer.addActionListener(e -> {

                int width = drawerPanel.getPreferredSize().width;
                int nextWidth = Math.max(width - 25, 0);

                drawerPanel.setPreferredSize(
                        new Dimension(nextWidth, content.getHeight()));

                content.revalidate();
                content.repaint();

                if (nextWidth <= 0) {
                    drawerTimer.stop();
                    content.remove(drawerPanel);
                    content.revalidate();
                    content.repaint();
                }
            });

            drawerTimer.start();
        }
    }

    // Create each sidebar button
    private void addDrawerItem(
            JPanel panel, String title, String page) {

        JButton item = new JButton(title);

        item.setFont(new Font("SansSerif", Font.PLAIN, 16));
        item.setForeground(new Color(35, 75, 65));
        item.setBackground(Color.WHITE);
        item.setHorizontalAlignment(SwingConstants.LEFT);
        item.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 48));
        item.setPreferredSize(new Dimension(230, 48));
        item.setBorder(new EmptyBorder(10, 14, 10, 10));
        item.setFocusPainted(false);
        item.setBorderPainted(false);
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        item.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(hoverGreen);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(Color.WHITE);
            }
        });

        item.addActionListener(e -> {

            closeDrawerImmediately();

            switch (page) {

                case "dashboard":
                    openNewPage(
                            new DashboardFrame(fullName, email));
                    break;

                case "income":
                    openNewPage(
                            new AddIncomeFrame(fullName, email));
                    break;

                case "expense":
                    openNewPage(
                            new AddExpenseFrame(fullName, email));
                    break;

                case "budget":
                    openNewPage(
                            new BudgetPlanningFrame(fullName, email));
                    break;

                case "reports":
                    JOptionPane.showMessageDialog(
                            this,
                            "The Reports page will be added next.");
                    break;

                case "history":
                    JOptionPane.showMessageDialog(
                            this,
                            "The Transaction History page will be added next.");
                    break;
            }
        });

        panel.add(item);
        panel.add(Box.createVerticalStrut(5));
    }

    private void closeDrawerImmediately() {

        if (drawerTimer != null && drawerTimer.isRunning()) {
            drawerTimer.stop();
        }

        drawerOpen = false;

        if (ownerFrame != null) {
            Container content = ownerFrame.getContentPane();
            content.remove(drawerPanel);
            content.revalidate();
            content.repaint();
        }
    }

    private void openNewPage(JFrame frame) {

        Window current = SwingUtilities.getWindowAncestor(this);

        if (current != null) {
            current.dispose();
        }

        frame.setVisible(true);
    }
}
