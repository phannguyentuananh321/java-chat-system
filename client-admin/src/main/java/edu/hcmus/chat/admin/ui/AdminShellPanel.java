package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main frame content of the admin app: menu on the left, one screen at a time on the right.
 * The screens don't know about the menu; this class just stacks them in a CardLayout
 * and switches cards when a menu button is clicked.
 *
 * Owner: Member A (admin).
 */
public class AdminShellPanel extends JPanel {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel screenContainer = new JPanel(cardLayout);
    private final JPanel menuPanel = new JPanel();
    private final List<JButton> menuButtons = new ArrayList<>();

    public AdminShellPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(screenContainer, BorderLayout.CENTER);

        // Menu order = order of the admin requirements
        addScreen("Người dùng", new UserManagementPanel());
        addScreen("Lịch sử đăng nhập", new LoginHistoryPanel());
        addScreen("Nhóm chat", new GroupManagementPanel());
        addScreen("Báo cáo spam", new SpamReportPanel());
        addScreen("Thống kê", new StatisticsPanel());

        showScreen(0);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Theme.BG_SURFACE);
        sidebar.setPreferredSize(new Dimension(190, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));

        JPanel brand = new JPanel(new BorderLayout(0, 2));
        brand.setOpaque(false);
        brand.setBorder(Theme.createPadding(20, 16, 20, 16));
        JLabel appNameLabel = new JLabel("HCMUS Chat");
        appNameLabel.setFont(Theme.FONT_TITLE);
        appNameLabel.setForeground(Theme.PRIMARY_COLOR);
        JLabel roleLabel = new JLabel("Trang quản trị");
        roleLabel.setFont(Theme.FONT_SMALL);
        roleLabel.setForeground(Theme.TEXT_MUTED);
        brand.add(appNameLabel, BorderLayout.NORTH);
        brand.add(roleLabel, BorderLayout.SOUTH);
        sidebar.add(brand, BorderLayout.NORTH);

        // Buttons stack top to bottom; BoxLayout keeps them at their own height
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        menuPanel.setBorder(Theme.createPadding(0, 10, 0, 10));
        sidebar.add(menuPanel, BorderLayout.CENTER);

        return sidebar;
    }

    /**
     * Adds the screen as a card, plus a menu button that shows it.
     */
    private void addScreen(String title, JPanel screen) {
        int index = menuButtons.size();
        screenContainer.add(screen, getCardName(index));

        JButton button = new JButton(title);
        button.setFont(Theme.FONT_REGULAR);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(Theme.createPadding(10, 14, 10, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        // BoxLayout won't stretch a button past its maximum size, so allow full width
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
        button.addActionListener(e -> showScreen(index));

        menuButtons.add(button);
        menuPanel.add(button);
        menuPanel.add(Box.createVerticalStrut(4));
    }

    /**
     * Shows the card and highlights its menu button.
     */
    private void showScreen(int index) {
        cardLayout.show(screenContainer, getCardName(index));
        for (int i = 0; i < menuButtons.size(); i++) {
            JButton button = menuButtons.get(i);
            boolean isActive = i == index;
            button.setBackground(isActive ? Theme.PRIMARY_COLOR : Theme.BG_SURFACE);
            button.setForeground(isActive ? Theme.PRIMARY_TEXT : Theme.TEXT_DARK);
            button.setFont(isActive ? Theme.FONT_BOLD : Theme.FONT_REGULAR);
        }
    }

    private String getCardName(int index) {
        return "CARD_" + index;
    }
}
