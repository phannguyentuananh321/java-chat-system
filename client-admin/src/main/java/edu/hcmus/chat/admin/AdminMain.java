package edu.hcmus.chat.admin;

import edu.hcmus.chat.admin.ui.UserManagementPanel;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Điểm khởi chạy chính (Main Entrypoint) của phân hệ Quản trị viên (client-admin).
 */
public class AdminMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame mainFrame = new JFrame("Hệ thống Chat HCMUS - Quản trị viên");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 650);
            mainFrame.setLocationRelativeTo(null);

            // Sử dụng CardLayout để quản lý các màn hình chức năng của phân hệ
            CardLayout cardLayout = new CardLayout();
            JPanel rootPanel = new JPanel(cardLayout);

            // Đăng ký màn hình Quản lý người dùng của Thành viên A
            UserManagementPanel userManagementPanel = new UserManagementPanel();
            rootPanel.add(userManagementPanel, "CARD_USER_MANAGEMENT");

            // sau này thay bằng khung chính của đồ án
            // (bao gồm Sidebar menu điều hướng, thanh Header người dùng đăng nhập, và chuyển đổi các CardLayout)
            cardLayout.show(rootPanel, "CARD_USER_MANAGEMENT");

            mainFrame.setContentPane(rootPanel);
            mainFrame.setVisible(true);
        });
    }
}
