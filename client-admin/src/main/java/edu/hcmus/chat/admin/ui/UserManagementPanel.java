package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Screen: user management (admin requirement 1).
 * Table of all accounts, with search / status filter / sort on top and the
 * per-user actions at the bottom (add, edit, delete, lock, password, history, friends).
 *
 * Owner: Member A (admin).
 */
public class UserManagementPanel extends JPanel {

    // Status sits early on purpose: the table scrolls sideways and locked accounts
    // should be visible without scrolling. Wide text columns go last.
    private static final String[] COLUMNS = {
            "ID", "Tên đăng nhập", "Họ và tên", "Trạng thái", "Giới tính", "Ngày sinh", "Email", "Địa chỉ", "Ngày tạo"
    };
    private static final int COLUMN_ID = 0;
    private static final int COLUMN_STATUS = 3;

    private DefaultTableModel userTableModel;
    private JTable userTable;

    public UserManagementPanel() {
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

        add(card, BorderLayout.CENTER);
        add(createActionBar(), BorderLayout.SOUTH);
    }

    /**
     * V1: the controls are laid out but not wired yet. Filtering and sorting will be
     * done by the server query in V2, so there is no point writing it twice here.
     */
    private JPanel createFilterBar() {
        JPanel bar = AdminUi.createFilterBar();

        JTextField searchField = AdminUi.createTextField("Họ tên hoặc tên đăng nhập", 16);
        JComboBox<String> statusComboBox = AdminUi.createComboBox("Tất cả", "Hoạt động", "Bị khóa");
        JComboBox<String> sortComboBox = AdminUi.createComboBox("Họ tên (A-Z)", "Ngày tạo (mới nhất)");
        JButton filterButton = Theme.createPrimaryButton("Lọc");

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

        addButton.addActionListener(e -> new UserFormDialog(getOwnerWindow(), null).setVisible(true));
        editButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new UserFormDialog(getOwnerWindow(), user).setVisible(true);
            }
        });
        deleteButton.addActionListener(e -> deleteSelectedUser());
        lockButton.addActionListener(e -> toggleLockOfSelectedUser());
        passwordButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new ChangePasswordDialog(getOwnerWindow(), user).setVisible(true);
            }
        });
        historyButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new UserDetailDialog(getOwnerWindow(), user, UserDetailDialog.TAB_LOGIN_HISTORY).setVisible(true);
            }
        });
        friendsButton.addActionListener(e -> {
            User user = getSelectedUser();
            if (user != null) {
                new UserDetailDialog(getOwnerWindow(), user, UserDetailDialog.TAB_FRIENDS).setVisible(true);
            }
        });

        return actionBar;
    }

    private void loadData() {
        userTableModel.setRowCount(0);
        for (User user : DummyAdminData.getDummyUsers()) {
            userTableModel.addRow(new Object[]{
                    user.userId(),
                    user.username(),
                    user.fullName(),
                    AdminFormat.accountStatus(user.accountStatus()),
                    AdminFormat.gender(user.gender()),
                    AdminFormat.date(user.dateOfBirth()),
                    user.email(),
                    user.address(),
                    AdminFormat.date(user.createdAt())
            });
        }
    }

    /**
     * Reads the id from the selected row and looks the user up.
     * Shows a warning and returns null when nothing is selected.
     */
    private User getSelectedUser() {
        int row = userTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một người dùng trong bảng.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        int userId = (int) userTableModel.getValueAt(row, COLUMN_ID);
        return DummyAdminData.findUserById(userId);
    }

    // V1: delete and lock only change the table on screen, the dummy list stays as is.

    private void deleteSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Xóa tài khoản \"" + user.username() + "\"? Thao tác này không thể hoàn tác.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            userTableModel.removeRow(userTable.getSelectedRow());
        }
    }

    private void toggleLockOfSelectedUser() {
        User user = getSelectedUser();
        if (user == null) {
            return;
        }
        int row = userTable.getSelectedRow();
        String lockedText = AdminFormat.accountStatus(AccountStatus.LOCKED);
        boolean isLocked = lockedText.equals(userTableModel.getValueAt(row, COLUMN_STATUS));

        String question = isLocked
                ? "Mở khóa tài khoản \"" + user.username() + "\"?"
                : "Khóa tài khoản \"" + user.username() + "\"? Người dùng sẽ không thể đăng nhập.";
        int answer = JOptionPane.showConfirmDialog(this, question, "Xác nhận",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            AccountStatus newStatus = isLocked ? AccountStatus.ACTIVE : AccountStatus.LOCKED;
            userTableModel.setValueAt(AdminFormat.accountStatus(newStatus), row, COLUMN_STATUS);
        }
    }

    private Window getOwnerWindow() {
        return SwingUtilities.getWindowAncestor(this);
    }
}
