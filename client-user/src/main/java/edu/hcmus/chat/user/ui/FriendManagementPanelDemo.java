package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Runner chạy độc lập cho FriendManagementPanel.
 * Kích thước chuẩn 1000x650 để kiểm thử và chụp ảnh màn hình báo cáo.
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class FriendManagementPanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame frame = new JFrame("Demo - Quản lý bạn bè & Lời mời kết bạn (Người dùng)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new FriendManagementPanel());
            frame.setVisible(true);
        });
    }
}
