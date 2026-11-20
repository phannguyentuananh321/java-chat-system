package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.ReportStatus;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Screen: spam reports sent by users (admin requirement 4).
 * The admin reads a report, then either locks the reported account or dismisses it.
 *
 * Owner: Member A (admin).
 */
public class SpamReportPanel extends JPanel {

    private static final String[] COLUMNS = {
            "Mã", "Thời gian", "Người báo cáo", "Người bị báo cáo", "Lý do", "Trạng thái"
    };
    private static final int COLUMN_REPORTED_USERNAME = 3;
    private static final int COLUMN_STATUS = 5;

    private DefaultTableModel reportTableModel;
    private JTable reportTable;

    public SpamReportPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        add(AdminUi.createPageHeader("BÁO CÁO SPAM",
                "Xem các báo cáo spam từ người dùng và khóa tài khoản vi phạm"), BorderLayout.NORTH);

        JPanel card = AdminUi.createCard();
        card.add(createFilterBar(), BorderLayout.NORTH);

        reportTableModel = AdminUi.createReadOnlyModel(COLUMNS);
        reportTable = AdminUi.createTable(reportTableModel);
        AdminUi.setColumnWidths(reportTable, 40, 115, 105, 115, 230, 90);
        card.add(AdminUi.createTableScrollPane(reportTable), BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        add(createActionBar(), BorderLayout.SOUTH);
    }

    /**
     * V1: layout only. Labels are short ("Từ", "Đến") so the row fits in one line.
     */
    private JPanel createFilterBar() {
        JPanel bar = AdminUi.createFilterBar();
        bar.add(AdminUi.createLabel("Từ"));
        bar.add(AdminUi.createDateField());
        bar.add(AdminUi.createLabel("Đến"));
        bar.add(AdminUi.createDateField());
        bar.add(AdminUi.createTextField("Tên đăng nhập", 12));
        bar.add(AdminUi.createLabel("Sắp xếp"));
        bar.add(AdminUi.createComboBox("Thời gian (mới nhất)", "Tên đăng nhập (A-Z)"));
        bar.add(Theme.createPrimaryButton("Lọc"));
        return bar;
    }

    private JPanel createActionBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        bar.setOpaque(false);

        JButton dismissButton = Theme.createSecondaryButton("Bỏ qua báo cáo");
        JButton lockButton = AdminUi.createDangerButton("Khóa tài khoản bị báo cáo");
        dismissButton.addActionListener(e -> dismissSelectedReport());
        lockButton.addActionListener(e -> lockReportedUser());

        bar.add(dismissButton);
        bar.add(lockButton);
        return bar;
    }

    private void loadData() {
        reportTableModel.setRowCount(0);
        for (DummyAdminData.SpamReportRecord report : DummyAdminData.getSpamReports()) {
            reportTableModel.addRow(new Object[]{
                    report.reportId(),
                    AdminFormat.dateTime(report.createdAt()),
                    report.reporterUsername(),
                    report.reportedUsername(),
                    report.reason(),
                    AdminFormat.reportStatus(report.status())
            });
        }
    }

    /**
     * Returns -1 (after a warning) when no row is selected.
     */
    private int getSelectedRowOrWarn() {
        int row = reportTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một báo cáo trong bảng.",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
        return row;
    }

    // V1: both actions only update the status text in the table.

    private void lockReportedUser() {
        int row = getSelectedRowOrWarn();
        if (row < 0) {
            return;
        }
        String username = reportTableModel.getValueAt(row, COLUMN_REPORTED_USERNAME).toString();
        int answer = JOptionPane.showConfirmDialog(this,
                "Khóa tài khoản \"" + username + "\"? Người dùng sẽ không thể đăng nhập.",
                "Xác nhận khóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            reportTableModel.setValueAt(AdminFormat.reportStatus(ReportStatus.REVIEWED), row, COLUMN_STATUS);
        }
    }

    private void dismissSelectedReport() {
        int row = getSelectedRowOrWarn();
        if (row >= 0) {
            reportTableModel.setValueAt(AdminFormat.reportStatus(ReportStatus.DISMISSED), row, COLUMN_STATUS);
        }
    }
}
