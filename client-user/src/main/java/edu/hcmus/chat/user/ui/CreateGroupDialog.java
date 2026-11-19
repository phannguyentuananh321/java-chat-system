package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.service.ChatService;
import edu.hcmus.chat.user.service.FriendService;
import edu.hcmus.chat.user.service.UserServiceFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hộp thoại Modal: Tạo nhóm chat mới.
 * Yêu cầu: Nhập tên nhóm và chọn tối thiểu 1 người bạn tham gia nhóm (Yêu cầu User 12a).
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class CreateGroupDialog extends JDialog {

    private final ChatService chatService = UserServiceFactory.getChatService();
    private final FriendService friendService = UserServiceFactory.getFriendService();

    private JTextField groupNameField;
    private DefaultTableModel friendsTableModel;
    private boolean groupCreated = false;

    public CreateGroupDialog(Frame owner) {
        super(owner, "Tạo nhóm trò chuyện mới", true);
        initUI();
        loadFriends();
    }

    public boolean isGroupCreated() {
        return groupCreated;
    }

    private void initUI() {
        setSize(480, 520);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Theme.BG_SURFACE);
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header & Tên nhóm
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("TẠO NHÓM MỚI");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.PRIMARY_COLOR);

        groupNameField = new JTextField();
        groupNameField.setFont(Theme.FONT_REGULAR);
        groupNameField.putClientProperty("JTextField.placeholderText", "Nhập tên nhóm trò chuyện...");

        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(groupNameField, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // 2. Danh sách bạn bè kèm Checkbox để tick chọn
        JPanel centerPanel = new JPanel(new BorderLayout(0, 8));
        centerPanel.setOpaque(false);

        JLabel selectLabel = new JLabel("Chọn bạn bè tham gia (Tối thiểu 1 thành viên):");
        selectLabel.setFont(Theme.FONT_BOLD);

        String[] columns = {"Chọn", "ID", "Họ và tên", "Email"};
        friendsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // Chỉ cho phép click checkbox ở cột 0
            }
        };

        JTable table = new JTable(friendsTableModel);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(28);
        table.getColumnModel().getColumn(0).setMaxWidth(60);
        table.getColumnModel().getColumn(1).setMaxWidth(60);

        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(selectLabel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // 3. Nút hành động
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setOpaque(false);

        JButton btnCancel = Theme.createSecondaryButton("Hủy bỏ");
        btnCancel.addActionListener(e -> dispose());

        JButton btnCreate = Theme.createPrimaryButton("Tạo nhóm ngay");
        btnCreate.addActionListener(e -> handleCreate());

        bottomPanel.add(btnCancel);
        bottomPanel.add(btnCreate);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadFriends() {
        friendsTableModel.setRowCount(0);
        for (DummyUserData.DummyFriend f : friendService.getFriends()) {
            friendsTableModel.addRow(new Object[]{
                    false,
                    f.userId(),
                    f.fullName(),
                    f.email()
            });
        }
    }

    private void handleCreate() {
        String groupName = groupNameField.getText().trim();
        if (groupName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhóm chat!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Integer> selectedUserIds = new ArrayList<>();
        for (int i = 0; i < friendsTableModel.getRowCount(); i++) {
            Boolean checked = (Boolean) friendsTableModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(checked)) {
                int id = (int) friendsTableModel.getValueAt(i, 1);
                selectedUserIds.add(id);
            }
        }

        if (selectedUserIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Theo quy định, bạn phải chọn tối thiểu 1 người bạn để tạo nhóm!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = chatService.createGroup(groupName, selectedUserIds);
        if (ok) {
            groupCreated = true;
            JOptionPane.showMessageDialog(this, "Đã tạo nhóm \"" + groupName + "\" thành công với " + selectedUserIds.size() + " thành viên!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
    }
}
