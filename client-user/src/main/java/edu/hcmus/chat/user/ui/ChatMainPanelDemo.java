package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Lớp chạy độc lập (Standalone Runner) phục vụ kiểm thử và chụp ảnh màn hình
 * cho ChatMainPanel mà không cần phụ thuộc vào toàn bộ hệ thống.
 *
 * Người phụ trách: Thành viên B (Người dùng)
 */
public class ChatMainPanelDemo {

    public static void main(String[] args) {
        // Tuân thủ quy tắc: Khởi tạo giao diện trong SwingUtilities.invokeLater
        SwingUtilities.invokeLater(() -> {
            // Cài đặt Look and Feel chuẩn
            Theme.install();

            JFrame frame = new JFrame("Demo - Khung chat chính (Người dùng)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null); // Căn giữa màn hình

            // Gán panel làm Content Pane trực tiếp
            frame.setContentPane(new ChatMainPanel());

            frame.setVisible(true);
        });
    }
}
