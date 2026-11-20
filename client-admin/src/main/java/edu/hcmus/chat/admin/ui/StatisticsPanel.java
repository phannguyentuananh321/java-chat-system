package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Screen: statistics and reports (admin requirements 5 to 9).
 * Each report is its own panel; this screen only puts them in tabs.
 *
 * Owner: Member A (admin).
 */
public class StatisticsPanel extends JPanel {

    public StatisticsPanel() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        add(AdminUi.createPageHeader("THỐNG KÊ & BÁO CÁO",
                "Người dùng mới, bạn bè, mức độ hoạt động và biểu đồ theo năm"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_BOLD);
        tabs.addTab("Người dùng mới", new NewUsersPanel());
        tabs.addTab("Biểu đồ đăng ký", new YearlyChartPanel(
                "Số người đăng ký mới theo tháng", DummyAdminData::getMonthlyRegistrations, true));
        tabs.addTab("Bạn bè", new FriendCountPanel());
        tabs.addTab("Người dùng hoạt động", new ActiveUsersPanel());
        tabs.addTab("Biểu đồ hoạt động", new YearlyChartPanel(
                "Số người dùng có mở ứng dụng theo tháng", DummyAdminData::getMonthlyActiveUsers, false));
        add(tabs, BorderLayout.CENTER);
    }
}
