package edu.hcmus.chat.user;

import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.ui.ChatMainPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Điểm khởi chạy chính (Main Entrypoint) của phân hệ Người dùng (client-user).
 */
public class UserMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame mainFrame = new JFrame("Hệ thống Chat HCMUS - Người dùng");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 650);
            mainFrame.setLocationRelativeTo(null);

            // Sử dụng CardLayout để quản lý các màn hình chức năng của phân hệ
            CardLayout cardLayout = new CardLayout();
            JPanel rootPanel = new JPanel(cardLayout);

            // Đăng ký màn hình Khung chat chính của Thành viên B
            ChatMainPanel chatMainPanel = new ChatMainPanel();
            rootPanel.add(chatMainPanel, "CARD_CHAT_MAIN");

            // sau này thay bằng khung chính của đồ án
            // (bao gồm Header người dùng, danh sách bạn bè/phòng chat, chuyển đổi giữa khung chat và cài đặt)
            cardLayout.show(rootPanel, "CARD_CHAT_MAIN");

            mainFrame.setContentPane(rootPanel);
            mainFrame.setVisible(true);
        });
    }
}
