package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.admin.dao.UserSortOrder;
import edu.hcmus.chat.admin.service.ServiceException;
import edu.hcmus.chat.admin.service.UserService;
import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Screen: user management (admin requirement 1).
 * Table of all accounts, with search / status filter / sort on top and the
 * per-user actions at the bottom (add, edit, delete, lock, password, history, friends).
 *
 * Presentation layer only: every read and write goes through UserService.
 *
 * Owner: Member A (admin).
 */
public class UserManagementPanel extends JPanel {

    // Status sits early on purpose: the table scrolls sideways and locked accounts
    // should be visible without scrolling. Wide text columns go last.
    private static final String[] COLUMNS = {
            "ID", "Tên đăng nhập", "Họ và tên", "Trạng thái", "Giới tính", "Ngày sinh", "Email", "Địa chỉ", "Ngày tạo"
    };

    private final UserService userService;

    private DefaultTableModel userTableModel;
    private JTable userTable;
    private JTextField searchField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> sortComboBox;
    private JLabel countLabel;

    // Rows currently in the table, same order, so a selected row maps to a User
    private List<User> shownUsers = new ArrayList<>();

    public UserManagementPanel() {
        this(new UserService());
    }

    public UserManagementPanel(UserService userService) {
        this.userService = userService;
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        add(AdminUi.createPageHeader("QUẢN LÝ NGƯỜI DÙNG",
                "Tìm kiếm, thêm, sửa, xóa và khóa tài khoản người dùng"), BorderLayout.NORTH);

        JPanel card = AdminUi.createCard();
        card.add(createFilterBar(), BorderLayout.NORTH);

        userTableModel = AdminUi.createReadOnlyModel(COLUMNS);
        userTable = AdminUi.createTable(userTableModel);
        // 9 columns don't fit in the window, so keep real widths and let it scroll sideways
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        AdminUi.setColumnWidths(userTable, 40, 105, 145, 85, 70, 90, 200, 210, 90);
        card.add(AdminUi.createTableScrollPane(userTable), BorderLayout.CENTER);

        countLabel = new JLabel(" ");
        countLabel.setFont(Theme.FONT_SMALL);
        countLabel.setForeground(Theme.TEXT_MUTED);
        card.add(countLabel, BorderLayout.SOUTH);

        add(card, BorderLayout.CENTER);
        add(createActionBar(), BorderLayout.SOUTH);
    }

    /**
     * Filters are applied by the database query (see UserDao.findUsers).
     * "Lọc" or Enter in the search box reloads; changing a combo box reloads right away.
     */
    private JPanel createFilterBar() {
        JPanel bar = AdminUi.createFilterBar();

        searchField = AdminUi.createTextField("Họ tên hoặc tên đăng nhập", 16);
        statusComboBox = AdminUi.createComboBox("Tất cả", "Hoạt động", "Bị khóa");
        sortComboBox = AdminUi.createComboBox("Họ tên (A-Z)", "Ngày tạo (mới nhất)");
        JButton filterButton = Theme.createPrimaryButton("Lọc");

        searchField.addActionListener(e -> loadData());
        statusComboBox.addActionListener(e -> loadData());
        sortComboBox.addActionListener(e -> loadData());
        filterButton.addActionListener(e -> loadData());

        bar.add(AdminUi.createLabel("Tìm kiếm"));
        bar.add(searchField);
        bar.add(AdminUi.createLabel("Trạng thái"));
        bar.add(statusComboBox);
        bar.add(AdminUi.createLabel("Sắp xếp"));
        bar.add(sortComboBox);
        bar.add(filterButton);
        return bar;
    }

    /**
     * Left group changes the account list, right group acts on the selected account.
     */
    private JPanel createActionBar() {
        JPanel actionBar = new JPanel(new BorderLayout());
        actionBar.setOpaque(false);

        JPanel leftGroup = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        leftGroup.setOpaque(false);
        JButton addButton = Theme.createPrimaryButton("+ Thêm");
        JButton editButton = Theme.createSecondaryButton("Sửa");
        JButton deleteButton = Theme.createSecondaryButton("Xóa");
        deleteButton.setForeground(Theme.DANGER_COLOR);
        leftGroup.add(addButton);
        leftGroup.add(editButton);
        leftGroup.add(deleteButton);

        JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightGroup.setOpaque(false);
        JButton lockButton = Theme.createSecondaryButton("Khóa / Mở khóa");
        JButton passwordButton = Theme.createSecondaryButton("Đổi mật khẩu");
        JButton historyButton = Theme.createSecondaryButton("Lịch sử đăng nhập");
        JButton friendsButton = Theme.createSecondaryButton("Bạn bè");
        rightGroup.add(lockButton);
        rightGroup.add(passwordButton);
        rightGroup.add(historyButton);
        rightGroup.add(friendsButton);

        actionBar.add(leftGroup, BorderLayout.WEST);
        actionBar.add(rightGroup, BorderLayout.EAST);

        addButton.addActionListener(e -> {
            UserFormDialog dialog = new UserFormDialog(getOwnerWindow(), userService, null);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadData();
            }
        });
        editButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                UserFormDialog dialog = new UserFormDialog(getOwnerWindow(), userService, user);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    loadData();
                }
            }
        });
        deleteButton.addActionListener(e -> deleteSelectedUser());
        lockButton.addActionListener(e -> toggleLockOfSelectedUser());
        passwordButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new ChangePasswordDialog(getOwnerWindow(), userService, user).setVisible(true);
            }
        });
        historyButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new UserDetailDialog(getOwnerWindow(), userService, user, UserDetailDialog.TAB_LOGIN_HISTORY)
                        .setVisible(true);
            }
        });
        friendsButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new UserDetailDialog(getOwnerWindow(), userService, user, UserDetailDialog.TAB_FRIENDS)
                        .setVisible(true);
            }
        });

        return actionBar;
    }

    /**
     * Reads the filter controls, asks the service, and refills the table.
     */
    private void loadData() {
        AccountStatus status = switch (statusComboBox.getSelectedIndex()) {
            case 1 -> AccountStatus.ACTIVE;
            case 2 -> AccountStatus.LOCKED;
            default -> null;
        };
        UserSortOrder sortOrder = sortComboBox.getSelectedIndex() == 1
                ? UserSortOrder.CREATED_NEWEST
                : UserSortOrder.FULL_NAME;

        userTableModel.setRowCount(0);
        shownUsers = new ArrayList<>();
        try {
            shownUsers = userService.getUsers(searchField.getText(), status, sortOrder);
        } catch (ServiceException e) {
            countLabel.setText("Không tải được danh sách người dùng.");
            showError(e);
            return;
        }

        for (User user : shownUsers) {
            userTableModel.addRow(new Object[]{
                    user.userId(),
                    user.username(),
                    user.fullName(),
                    AdminFormat.accountStatus(user.accountStatus()),
                    user.gender() == null ? "" : AdminFormat.gender(user.gender()),
                    AdminFormat.date(user.dateOfBirth()),
                    user.email(),
                    user.address() == null ? "" : user.address(),
                    AdminFormat.date(user.createdAt())
            });
        }
        countLabel.setText(shownUsers.size() + " người dùng");
    }

    /**
     * Shows a warning and returns null when nothing is selected.
     */
    private User getSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người dùng trong bảng.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return shownUsers.get(row);
    }

    private void deleteSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Xóa tài khoản \"" + user.username() + "\"? Người dùng sẽ không thể đăng nhập nữa.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            userService.deleteUser(user.userId());
            loadData();
        } catch (ServiceException e) {
            showError(e);
        }
    }

    private void toggleLockOfSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        boolean isLocked = user.accountStatus() == AccountStatus.LOCKED;
        String question = isLocked
                ? "Mở khóa tài khoản \"" + user.username() + "\"?"
                : "Khóa tài khoản \"" + user.username() + "\"? Người dùng sẽ bị đăng xuất và không thể đăng nhập.";
        int answer = JOptionPane.showConfirmDialog(this, question, "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            userService.setLocked(user.userId(), !isLocked);
            loadData();
        } catch (ServiceException e) {
            showError(e);
        }
    }

    private void showError(ServiceException e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    private Window getOwnerWindow() {
        return SwingUtilities.getWindowAncestor(this);
    }
}
