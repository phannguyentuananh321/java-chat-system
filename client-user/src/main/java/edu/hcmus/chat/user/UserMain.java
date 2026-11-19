package edu.hcmus.chat.user;

import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.auth.AuthFrame;
import edu.hcmus.chat.user.ui.ChatMainPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Điểm khởi chạy chính (Main Entrypoint) của phân hệ Người dùng (client-user).
 * Quy trình: Khởi động AuthFrame -> Đăng nhập thành công -> Mở khung giao diện chat chính.
 *
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class UserMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            // 1. Mở cửa sổ Đăng nhập / Đăng ký
            AuthFrame authFrame = new AuthFrame();

            // 2. Khi đăng nhập thành công -> Kích hoạt mở Khung chat chính của đồ án
            authFrame.setOnLoginSuccess(() -> {
                JFrame mainFrame = new JFrame("Hệ thống Chat HCMUS - Người dùng");
                mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainFrame.setSize(1000, 650);
                mainFrame.setLocationRelativeTo(null);

                // Sử dụng CardLayout để quản lý các màn hình chức năng của phân hệ
                CardLayout cardLayout = new CardLayout();
                JPanel rootPanel = new JPanel(cardLayout);

                ChatMainPanel chatMainPanel = new ChatMainPanel();
                rootPanel.add(chatMainPanel, "CARD_CHAT_MAIN");

                // sau này thay bằng khung chính của đồ án
                // (bao gồm Header người dùng, danh sách bạn bè/phòng chat, chuyển đổi giữa khung chat và cài đặt)
                cardLayout.show(rootPanel, "CARD_CHAT_MAIN");

                mainFrame.setContentPane(rootPanel);
                mainFrame.setVisible(true);
            });

            authFrame.setVisible(true);
        });
    }
}
