package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Screen: every login of every user, newest first (admin requirement 2).
 * Columns are the three the requirement asks for: time, username, full name.
 *
 * Owner: Member A (admin).
 */
public class LoginHistoryPanel extends JPanel {

    private static final String[] COLUMNS = {"Thời gian đăng nhập", "Tên đăng nhập", "Họ và tên"};

    private DefaultTableModel loginTableModel;

    public LoginHistoryPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        add(AdminUi.createPageHeader("LỊCH SỬ ĐĂNG NHẬP",
                "Danh sách các lần đăng nhập theo thứ tự thời gian, mới nhất ở trên"), BorderLayout.NORTH);

        JPanel card = AdminUi.createCard();
        card.add(createFilterBar(), BorderLayout.NORTH);

        loginTableModel = AdminUi.createReadOnlyModel(COLUMNS);
        JTable table = AdminUi.createTable(loginTableModel);
        card.add(AdminUi.createTableScrollPane(table), BorderLayout.CENTER);

        add(card, BorderLayout.CENTER);
    }

    /**
     * V1: layout only, same as the other screens.
     */
    private JPanel createFilterBar() {
        JPanel bar = AdminUi.createFilterBar();
        bar.add(AdminUi.createLabel("Từ ngày"));
        bar.add(AdminUi.createDateField());
        bar.add(AdminUi.createLabel("Đến ngày"));
        bar.add(AdminUi.createDateField());
        bar.add(AdminUi.createTextField("Tên đăng nhập", 12));
        bar.add(Theme.createPrimaryButton("Lọc"));
        return bar;
    }

    private void loadData() {
        loginTableModel.setRowCount(0);
        for (DummyAdminData.LoginRecord login : DummyAdminData.getLoginHistory()) {
            loginTableModel.addRow(new Object[]{
                    AdminFormat.dateTime(login.loginAt()),
                    login.username(),
                    login.fullName()
            });
        }
    }
}
