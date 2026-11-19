package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Runner chạy độc lập cho ChatMainPanel để kiểm thử và chụp ảnh màn hình nộp báo cáo.
 * Kích thước chuẩn: 1000 x 650.
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class ChatMainPanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame frame = new JFrame("Demo - Khung chat chính (Người dùng)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null);

            frame.setContentPane(new ChatMainPanel());
            frame.setVisible(true);
        });
    }
}
