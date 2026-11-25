package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.admin.service.ServiceException;
import edu.hcmus.chat.admin.service.UserService;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserSession;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Dialog: login history and friend list of one user (admin requirements 1e, 1f).
 * Both are "look at one user" views, so they share one dialog with two tabs;
 * the button that opens it picks the starting tab.
 *
 * Owner: Member A (admin).
 */
public class UserDetailDialog extends JDialog {

    public static final int TAB_LOGIN_HISTORY = 0;
    public static final int TAB_FRIENDS = 1;

    private final UserService userService;
    private final User user;

    public UserDetailDialog(Window owner, UserService userService, User user, int startTab) {
        super(owner, "Chi tiết người dùng", ModalityType.APPLICATION_MODAL);
        this.userService = userService;
        this.user = user;
        initUI(startTab);
    }

    private void initUI(int startTab) {
        setSize(620, 460);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(Theme.BG_SURFACE);
        content.setBorder(Theme.createPadding(20, 20, 20, 20));
        setContentPane(content);

        content.add(AdminUi.createPageHeader(user.fullName().toUpperCase(),
                "Tên đăng nhập: " + user.username() + "   •   Email: " + user.email()), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_BOLD);
        tabs.addTab("Lịch sử đăng nhập", createLoginHistoryTab());
        tabs.addTab("Danh sách bạn bè", createFriendsTab());
        tabs.setSelectedIndex(startTab);
        content.add(tabs, BorderLayout.CENTER);

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonBar.setOpaque(false);
        JButton closeButton = Theme.createSecondaryButton("Đóng");
        closeButton.addActionListener(e -> dispose());
        buttonBar.add(closeButton);
        content.add(buttonBar, BorderLayout.SOUTH);
    }

    private JComponent createLoginHistoryTab() {
        DefaultTableModel model = AdminUi.createReadOnlyModel(new String[]{"Đăng nhập lúc", "Đăng xuất lúc"});
        try {
            for (UserSession session : userService.getLoginHistory(user.userId())) {
                model.addRow(new Object[]{
                        AdminFormat.dateTime(session.loginAt()),
                        // still open session: no logout time yet
                        session.logoutAt() == null ? "Đang trực tuyến" : AdminFormat.dateTime(session.logoutAt())
                });
            }
        } catch (ServiceException e) {
            return createErrorLabel(e);
        }
        return AdminUi.createTableScrollPane(AdminUi.createTable(model));
    }

    private JComponent createFriendsTab() {
        DefaultTableModel model = AdminUi.createReadOnlyModel(new String[]{"Tên đăng nhập", "Họ và tên", "Email"});
        try {
            for (User friend : userService.getFriends(user.userId())) {
                model.addRow(new Object[]{friend.username(), friend.fullName(), friend.email()});
            }
        } catch (ServiceException e) {
            return createErrorLabel(e);
        }
        return AdminUi.createTableScrollPane(AdminUi.createTable(model));
    }

    /**
     * Shown in place of the table when the data can't be loaded.
     */
    private JComponent createErrorLabel(ServiceException e) {
        JLabel label = new JLabel(e.getMessage(), SwingConstants.CENTER);
        label.setFont(Theme.FONT_REGULAR);
        label.setForeground(Theme.DANGER_COLOR);
        return label;
    }
}
