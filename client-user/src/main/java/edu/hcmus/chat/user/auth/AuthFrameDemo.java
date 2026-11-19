package edu.hcmus.chat.user.auth;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Runner chạy độc lập kiểm thử và chụp ảnh màn hình cho AuthFrame.
 * Người phụ trách: Thành viên B (Người dùng)
 */
public class AuthFrameDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();
            AuthFrame frame = new AuthFrame();
            frame.setOnLoginSuccess(() -> {
                JOptionPane.showMessageDialog(null, "Đăng nhập thành công! (Chế độ Demo)", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            });
            frame.setVisible(true);
        });
    }
}
