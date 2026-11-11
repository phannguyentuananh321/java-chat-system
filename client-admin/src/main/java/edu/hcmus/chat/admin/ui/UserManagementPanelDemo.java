package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Lớp chạy độc lập (Standalone Runner) phục vụ kiểm thử và chụp ảnh màn hình
 * cho UserManagementPanel mà không cần phụ thuộc vào toàn bộ hệ thống.
 *
 * Người phụ trách: Thành viên A (Quản trị viên)
 */
public class UserManagementPanelDemo {

    public static void main(String[] args) {
        // Tuân thủ quy tắc: Khởi tạo giao diện trong SwingUtilities.invokeLater
        SwingUtilities.invokeLater(() -> {
            // Cài đặt Look and Feel chuẩn
            Theme.install();

            JFrame frame = new JFrame("Demo - Quản lý người dùng (Quản trị viên)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null); // Căn giữa màn hình

            // Gán panel làm Content Pane trực tiếp
            frame.setContentPane(new UserManagementPanel());

            frame.setVisible(true);
        });
    }
}
