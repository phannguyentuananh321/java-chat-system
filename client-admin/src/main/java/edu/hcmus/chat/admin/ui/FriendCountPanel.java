package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tab: every user with their number of direct friends, and the number when
 * friends of friends are counted too (admin requirement 7).
 *
 * Owner: Member A (admin).
 */
public class FriendCountPanel extends JPanel {

    private static final String[] COLUMNS = {
            "Tên đăng nhập", "Họ và tên", "Ngày tạo", "Bạn trực tiếp", "Tính cả bạn của bạn"
    };

    private DefaultTableModel friendTableModel;

    public FriendCountPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Theme.BG_SURFACE);
        setBorder(Theme.createCardBorder(12));

        // V1: layout only. The count filter compares against the direct friend count.
        JPanel filterBar = AdminUi.createFilterBar();
        filterBar.add(AdminUi.createTextField("Họ tên hoặc tên đăng nhập", 14));
        filterBar.add(AdminUi.createLabel("Số bạn trực tiếp"));
        filterBar.add(AdminUi.createCompareComboBox());
        filterBar.add(AdminUi.createTextField("0", 4));
        filterBar.add(AdminUi.createLabel("Sắp xếp"));
        filterBar.add(AdminUi.createComboBox("Họ tên", "Ngày tạo"));
        filterBar.add(Theme.createPrimaryButton("Lọc"));
        add(filterBar, BorderLayout.NORTH);

        friendTableModel = AdminUi.createReadOnlyModel(COLUMNS);
        JTable table = AdminUi.createTable(friendTableModel);
        AdminUi.setColumnWidths(table, 110, 150, 90, 100, 140);
        add(AdminUi.createTableScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        friendTableModel.setRowCount(0);
        for (DummyAdminData.FriendCountRecord row : DummyAdminData.getFriendCounts()) {
            friendTableModel.addRow(new Object[]{
                    row.username(),
                    row.fullName(),
                    AdminFormat.date(row.createdAt()),
                    row.directFriends(),
                    row.friendsWithFriendsOfFriends()
            });
        }
    }
}
