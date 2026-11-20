package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserRole;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Tab: users who registered inside a time range (admin requirement 5).
 *
 * Owner: Member A (admin).
 */
public class NewUsersPanel extends JPanel {

    private static final String[] COLUMNS = {"Tên đăng nhập", "Họ và tên", "Email", "Ngày đăng ký"};

    private DefaultTableModel userTableModel;

    public NewUsersPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Theme.BG_SURFACE);
        setBorder(Theme.createCardBorder(12));

        // V1: layout only
        JPanel filterBar = AdminUi.createFilterBar();
        filterBar.add(AdminUi.createLabel("Từ"));
        filterBar.add(AdminUi.createDateField());
        filterBar.add(AdminUi.createLabel("Đến"));
        filterBar.add(AdminUi.createDateField());
        filterBar.add(AdminUi.createTextField("Họ tên hoặc tên đăng nhập", 14));
        filterBar.add(AdminUi.createLabel("Sắp xếp"));
        filterBar.add(AdminUi.createComboBox("Ngày đăng ký", "Họ tên"));
        filterBar.add(Theme.createPrimaryButton("Lọc"));
        add(filterBar, BorderLayout.NORTH);

        userTableModel = AdminUi.createReadOnlyModel(COLUMNS);
        add(AdminUi.createTableScrollPane(AdminUi.createTable(userTableModel)), BorderLayout.CENTER);
    }

    private void loadData() {
        userTableModel.setRowCount(0);

        // The sample list is stored oldest first; show newest first
        List<User> users = DummyAdminData.getDummyUsers();
        for (int i = users.size() - 1; i >= 0; i--) {
            User user = users.get(i);
            if (user.role() == UserRole.ADMIN) {
                continue;
            }
            userTableModel.addRow(new Object[]{
                    user.username(),
                    user.fullName(),
                    user.email(),
                    AdminFormat.date(user.createdAt())
            });
        }
    }
}
