package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Standalone runner for LoginHistoryPanel, used to check the layout and take report screenshots.
 *
 * Owner: Member A (admin).
 */
public class LoginHistoryPanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame frame = new JFrame("Demo - Lịch sử đăng nhập");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new LoginHistoryPanel());
            frame.setVisible(true);
        });
    }
}
