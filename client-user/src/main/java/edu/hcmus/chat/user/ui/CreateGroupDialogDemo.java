package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Runner chạy độc lập kiểm thử CreateGroupDialog để chụp ảnh màn hình báo cáo.
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class CreateGroupDialogDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();
            JFrame owner = new JFrame("Demo Chủ thể");
            owner.setSize(600, 400);
            owner.setLocationRelativeTo(null);
            owner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            CreateGroupDialog dialog = new CreateGroupDialog(owner);
            dialog.setVisible(true);
        });
    }
}
