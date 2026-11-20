package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Tab: users active inside a time range, with three activity numbers each:
 * times the app was opened, people chatted with, groups chatted in (admin requirement 8).
 *
 * Owner: Member A (admin).
 */
public class ActiveUsersPanel extends JPanel {

    private static final String[] COLUMNS = {
            "Tên đăng nhập", "Họ và tên", "Ngày tạo", "Mở ứng dụng", "Chat với (người)", "Chat nhóm"
    };

    private DefaultTableModel activityTableModel;

    public ActiveUsersPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Theme.BG_SURFACE);
        setBorder(Theme.createCardBorder(12));

        add(createFilterRows(), BorderLayout.NORTH);

        activityTableModel = AdminUi.createReadOnlyModel(COLUMNS);
        JTable table = AdminUi.createTable(activityTableModel);
        AdminUi.setColumnWidths(table, 110, 140, 90, 90, 110, 80);
        add(AdminUi.createTableScrollPane(table), BorderLayout.CENTER);
    }

    /**
     * Too many controls for one line, so the filter is split in two rows:
     * time range + name on top, activity count + sort below.
     * V1: layout only. Which number the "=, <, >" filter compares is still open
     * (the requirement only says "activity count"); app opens is the placeholder.
     */
    private JPanel createFilterRows() {
        JPanel rows = new JPanel(new GridLayout(2, 1, 0, 8));
        rows.setOpaque(false);

        JPanel rangeRow = AdminUi.createFilterBar();
        rangeRow.add(AdminUi.createLabel("Từ"));
        rangeRow.add(AdminUi.createDateField());
        rangeRow.add(AdminUi.createLabel("Đến"));
        rangeRow.add(AdminUi.createDateField());
        rangeRow.add(AdminUi.createTextField("Họ tên hoặc tên đăng nhập", 14));

        JPanel countRow = AdminUi.createFilterBar();
        countRow.add(AdminUi.createLabel("Số lần mở ứng dụng"));
        countRow.add(AdminUi.createCompareComboBox());
        countRow.add(AdminUi.createTextField("0", 4));
        countRow.add(AdminUi.createLabel("Sắp xếp"));
        countRow.add(AdminUi.createComboBox("Họ tên", "Ngày tạo"));
        countRow.add(Theme.createPrimaryButton("Lọc"));

        rows.add(rangeRow);
        rows.add(countRow);
        return rows;
    }

    private void loadData() {
        activityTableModel.setRowCount(0);
        for (DummyAdminData.UserActivityRecord row : DummyAdminData.getUserActivity()) {
            activityTableModel.addRow(new Object[]{
                    row.username(),
                    row.fullName(),
                    AdminFormat.date(row.createdAt()),
                    row.appOpens(),
                    row.chattedWithUsers(),
                    row.chattedInGroups()
            });
        }
    }
}
