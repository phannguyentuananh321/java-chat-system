package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình: Khung chat chính (Phân hệ Người dùng).
 * Kế thừa JPanel theo chuẩn kiến trúc module hoá giao diện.
 *
 * Người phụ trách: Thành viên B (Người dùng)
 */
public class ChatMainPanel extends JPanel {

    public ChatMainPanel() {
        initComponents();
    }

    private void initComponents() {
        // Tuân thủ quy tắc: Sử dụng layout manager tiêu chuẩn, KHÔNG dùng null layout
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        // 1. Vùng thanh thông tin phía trên (NORTH)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = Theme.createHeaderLabel("KHUNG CHAT CHÍNH");
        JLabel subtitleLabel = new JLabel("Trò chuyện trực tuyến qua giao thức TCP Socket nội bộ");
        subtitleLabel.setFont(Theme.FONT_REGULAR);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Vùng trung tâm (CENTER) - Khung bố cục tối thiểu chạy được
        // TODO(Thành viên B): Thiết kế danh sách hội thoại/bạn bè bên trái (WEST),
        // vùng hiển thị bong bóng chat/tin nhắn ở giữa (CENTER).
        // Gợi ý sử dụng DummyUserData.getDummyContacts() và DummyUserData.getDummyMessages() để nạp dữ liệu mẫu ban đầu.
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(Theme.BG_SURFACE);
        centerContainer.setBorder(Theme.createCardBorder(24));

        JLabel placeholderLabel = new JLabel(
                "<html><center><h3>[KHU VỰC KHUNG CHAT CHÍNH]</h3>"
                        + "<p>Thành viên B hiện thực: Danh sách bạn bè/phòng chat, danh sách tin nhắn và bong bóng thoại tại đây.</p></center></html>",
                SwingConstants.CENTER
        );
        placeholderLabel.setFont(Theme.FONT_REGULAR);
        placeholderLabel.setForeground(Theme.TEXT_MUTED);
        centerContainer.add(placeholderLabel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // 3. Vùng nhập liệu tin nhắn phía dưới (SOUTH)
        // TODO(Thành viên B): Thêm ô nhập tin nhắn (JTextField hoặc JTextArea), nút gửi file/emoji và nút Gửi tin nhắn.
        JPanel inputPanel = new JPanel(new BorderLayout(10, 0));
        inputPanel.setOpaque(false);

        JTextField messageInput = new JTextField("Nhập tin nhắn...");
        messageInput.setFont(Theme.FONT_REGULAR);
        messageInput.setBorder(Theme.createCardBorder(8));

        JButton sendButton = Theme.createPrimaryButton("Gửi");

        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }
}
