package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.MemberRole;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Screen: group chats (admin requirement 3).
 * Left: all groups, with name filter and sort. Right: members and group admins
 * of the group selected on the left.
 *
 * Owner: Member A (admin).
 */
public class GroupManagementPanel extends JPanel {

    private static final String[] GROUP_COLUMNS = {"ID", "Tên nhóm", "Số người", "Người tạo", "Ngày tạo"};
    private static final String[] MEMBER_COLUMNS = {"Tên đăng nhập", "Họ và tên", "Vai trò"};
    private static final String[] ADMIN_COLUMNS = {"Tên đăng nhập", "Họ và tên"};

    private DefaultTableModel groupTableModel;
    private DefaultTableModel memberTableModel;
    private DefaultTableModel adminTableModel;
    private JTable groupTable;
    private JLabel selectedGroupLabel;

    public GroupManagementPanel() {
        initUI();
        loadGroups();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        add(AdminUi.createPageHeader("QUẢN LÝ NHÓM CHAT",
                "Danh sách nhóm chat, thành viên và quản trị viên của từng nhóm"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(15, 0));
        body.setOpaque(false);
        body.add(createGroupListCard(), BorderLayout.CENTER);
        body.add(createMemberCard(), BorderLayout.EAST);
        add(body, BorderLayout.CENTER);
    }

    private JPanel createGroupListCard() {
        JPanel card = AdminUi.createCard();

        // V1: filter and sort are layout only
        JPanel filterBar = AdminUi.createFilterBar();
        filterBar.add(AdminUi.createTextField("Tên nhóm", 12));
        filterBar.add(AdminUi.createLabel("Sắp xếp"));
        filterBar.add(AdminUi.createComboBox("Tên nhóm", "Ngày tạo"));
        filterBar.add(Theme.createPrimaryButton("Lọc"));
        card.add(filterBar, BorderLayout.NORTH);

        groupTableModel = AdminUi.createReadOnlyModel(GROUP_COLUMNS);
        groupTable = AdminUi.createTable(groupTableModel);
        AdminUi.setColumnWidths(groupTable, 30, 140, 80, 90, 85);
        card.add(AdminUi.createTableScrollPane(groupTable), BorderLayout.CENTER);

        // Picking a group on the left fills the member tables on the right.
        // getValueIsAdjusting() is true while the mouse is still dragging, skip those.
        groupTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showMembersOfSelectedGroup();
            }
        });
        return card;
    }

    private JPanel createMemberCard() {
        JPanel card = AdminUi.createCard();
        card.setPreferredSize(new Dimension(330, 0));

        selectedGroupLabel = new JLabel("Chọn một nhóm để xem thành viên");
        selectedGroupLabel.setFont(Theme.FONT_SUBTITLE);
        selectedGroupLabel.setForeground(Theme.TEXT_DARK);
        card.add(selectedGroupLabel, BorderLayout.NORTH);

        memberTableModel = AdminUi.createReadOnlyModel(MEMBER_COLUMNS);
        adminTableModel = AdminUi.createReadOnlyModel(ADMIN_COLUMNS);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(Theme.FONT_BOLD);
        tabs.addTab("Thành viên", AdminUi.createTableScrollPane(AdminUi.createTable(memberTableModel)));
        tabs.addTab("Quản trị nhóm", AdminUi.createTableScrollPane(AdminUi.createTable(adminTableModel)));
        card.add(tabs, BorderLayout.CENTER);
        return card;
    }

    private void loadGroups() {
        groupTableModel.setRowCount(0);
        for (DummyAdminData.GroupRecord group : DummyAdminData.getGroups()) {
            groupTableModel.addRow(new Object[]{
                    group.conversationId(),
                    group.title(),
                    DummyAdminData.getGroupMembers(group.conversationId()).size(),
                    group.createdByUsername(),
                    AdminFormat.date(group.createdAt())
            });
        }
        if (groupTableModel.getRowCount() > 0) {
            groupTable.setRowSelectionInterval(0, 0);
        }
    }

    private void showMembersOfSelectedGroup() {
        memberTableModel.setRowCount(0);
        adminTableModel.setRowCount(0);

        int row = groupTable.getSelectedRow();
        if (row < 0) {
            selectedGroupLabel.setText("Chọn một nhóm để xem thành viên");
            return;
        }
        int conversationId = (int) groupTableModel.getValueAt(row, 0);
        selectedGroupLabel.setText(groupTableModel.getValueAt(row, 1).toString());

        List<DummyAdminData.GroupMemberRecord> members = DummyAdminData.getGroupMembers(conversationId);
        for (DummyAdminData.GroupMemberRecord member : members) {
            memberTableModel.addRow(new Object[]{
                    member.username(),
                    member.fullName(),
                    AdminFormat.memberRole(member.role())
            });
            if (member.role() == MemberRole.ADMIN) {
                adminTableModel.addRow(new Object[]{member.username(), member.fullName()});
            }
        }
    }
}
