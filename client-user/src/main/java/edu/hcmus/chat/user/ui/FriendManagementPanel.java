package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.service.FriendService;
import edu.hcmus.chat.user.service.UserServiceFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Màn hình: Quản lý bạn bè và Lời mời kết bạn (Phân hệ Người dùng).
 * Kế thừa JPanel, tích hợp JTabbedPane chia tách danh sách bạn bè và lời mời.
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class FriendManagementPanel extends JPanel {

    private final FriendService friendService = UserServiceFactory.getFriendService();

    private DefaultTableModel friendsTableModel;
    private DefaultTableModel requestsTableModel;

    public FriendManagementPanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header: Tiêu đề và Thanh tìm kiếm kết bạn mới
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);

        JLabel titleLabel = Theme.createHeaderLabel("QUẢN LÝ BẠN BÈ & KẾT NỐI");
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchBar = new JPanel(new BorderLayout(8, 0));
        searchBar.setOpaque(false);
        JTextField searchNewUserField = new JTextField();
        searchNewUserField.setFont(Theme.FONT_REGULAR);
        searchNewUserField.putClientProperty("JTextField.placeholderText", "Nhập tên đăng nhập để tìm kiếm và gửi lời mời kết bạn...");

        JButton btnAddFriend = Theme.createPrimaryButton("+ Gửi kết bạn");
        btnAddFriend.addActionListener(e -> {
            String target = searchNewUserField.getText().trim();
            if (!target.isEmpty()) {
                friendService.sendFriendRequest(target);
                JOptionPane.showMessageDialog(this, "Đã gửi lời mời kết bạn tới " + target, "Thành công", JOptionPane.INFORMATION_MESSAGE);
                searchNewUserField.setText("");
            }
        });

        searchBar.add(searchNewUserField, BorderLayout.CENTER);
        searchBar.add(btnAddFriend, BorderLayout.EAST);
        headerPanel.add(searchBar, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // 2. Nội dung trung tâm: JTabbedPane chia 2 tab
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BOLD);

        tabbedPane.addTab("Danh sách bạn bè", createFriendsTab());
        tabbedPane.addTab("Lời mời kết bạn (1)", createRequestsTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createFriendsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Theme.BG_SURFACE);
        panel.setBorder(Theme.createCardBorder(12));

        // Bảng danh sách bạn bè
        String[] columns = {"ID", "Tên đăng nhập", "Họ và tên", "Email", "Trạng thái"};
        friendsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(friendsTableModel);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(32);
        table.getTableHeader().setFont(Theme.FONT_BOLD);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Thanh công cụ bên dưới
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbar.setOpaque(false);

        JButton btnChat = Theme.createPrimaryButton("Nhắn tin");
        JButton btnUnfriend = Theme.createSecondaryButton("Hủy kết bạn");
        JButton btnBlock = Theme.createSecondaryButton("Chặn tài khoản (Block)");
        btnBlock.setForeground(Theme.DANGER_COLOR);

        btnBlock.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) friendsTableModel.getValueAt(selectedRow, 0);
                String name = (String) friendsTableModel.getValueAt(selectedRow, 2);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Bạn có chắc muốn chặn " + name + "? Người này sẽ bị hủy kết bạn và không thể tìm thấy bạn.",
                        "Xác nhận chặn", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    friendService.blockUser(id);
                    friendsTableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(this, "Đã chặn người dùng " + name, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một người bạn trong danh sách để chặn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnUnfriend.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) friendsTableModel.getValueAt(selectedRow, 0);
                String name = (String) friendsTableModel.getValueAt(selectedRow, 2);
                friendService.removeFriend(id);
                friendsTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Đã hủy kết bạn với " + name, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một người bạn để hủy kết bạn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        toolbar.add(btnChat);
        toolbar.add(btnUnfriend);
        toolbar.add(btnBlock);

        panel.add(toolbar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createRequestsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Theme.BG_SURFACE);
        panel.setBorder(Theme.createCardBorder(12));

        String[] columns = {"Mã lời mời", "Người gửi", "Email", "Thời gian nhận"};
        requestsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(requestsTableModel);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(32);
        table.getTableHeader().setFont(Theme.FONT_BOLD);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbar.setOpaque(false);

        JButton btnAccept = Theme.createPrimaryButton("Đồng ý kết bạn");
        JButton btnReject = Theme.createSecondaryButton("Từ chối");

        btnAccept.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int reqId = (int) requestsTableModel.getValueAt(selectedRow, 0);
                friendService.acceptFriendRequest(reqId);
                requestsTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Đã chấp nhận lời mời kết bạn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        btnReject.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int reqId = (int) requestsTableModel.getValueAt(selectedRow, 0);
                friendService.rejectFriendRequest(reqId);
                requestsTableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Đã từ chối lời mời kết bạn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        toolbar.add(btnAccept);
        toolbar.add(btnReject);

        panel.add(toolbar, BorderLayout.SOUTH);
        return panel;
    }

    private void loadData() {
        friendsTableModel.setRowCount(0);
        for (DummyUserData.DummyFriend f : friendService.getFriends()) {
            friendsTableModel.addRow(new Object[]{
                    f.userId(),
                    f.username(),
                    f.fullName(),
                    f.email(),
                    f.isOnline() ? "● Trực tuyến" : "○ Ngoại tuyến"
            });
        }

        requestsTableModel.setRowCount(0);
        for (DummyUserData.DummyFriendRequest r : friendService.getFriendRequests()) {
            requestsTableModel.addRow(new Object[]{
                    r.requestId(),
                    r.senderName(),
                    r.senderEmail(),
                    r.requestTime()
            });
        }
    }
}
