package edu.hcmus.chat.user.ui.components;

import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.ui.DummyUserData.DummyContact;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Bộ vẽ từng dòng trong danh sách cuộc trò chuyện bên trái.
 * Phân biệt rõ ràng giữa trạng thái Đã đọc (Seen - chữ mờ) và Chưa đọc (Unseen - chữ đậm + badge).
 * 100% Java Swing thuần túy.
 */
public class ConversationListCellRenderer extends JPanel implements ListCellRenderer<DummyContact> {

    private final JLabel avatarLabel = new JLabel();
    private final JLabel nameLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JLabel timeLabel = new JLabel();
    private final JLabel statusDotLabel = new JLabel("●");
    private final JLabel unreadBadge = new JLabel();

    public ConversationListCellRenderer() {
        setLayout(new BorderLayout(8, 0));
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setOpaque(true);

        // 1. Avatar tròn bên trái (40x40)
        avatarLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setOpaque(true);
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        avatarLabel.setPreferredSize(new Dimension(40, 40));

        JPanel avatarWrapper = new JPanel(new BorderLayout());
        avatarWrapper.setOpaque(false);
        avatarWrapper.add(avatarLabel, BorderLayout.CENTER);
        add(avatarWrapper, BorderLayout.WEST);

        // 2. Khu vực giữa: Tên (trên) & Tin nhắn xem trước (dưới)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        centerPanel.add(nameLabel);
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(messageLabel);
        add(centerPanel, BorderLayout.CENTER);

        // 3. Khu vực bên phải: Thời gian & Huy hiệu chưa đọc
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new Dimension(65, 40));

        timeLabel.setFont(Theme.FONT_SMALL);
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel badgeWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        badgeWrapper.setOpaque(false);
        badgeWrapper.setAlignmentX(Component.RIGHT_ALIGNMENT);

        statusDotLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));

        unreadBadge.setFont(Theme.FONT_SMALL);
        unreadBadge.setForeground(Color.WHITE);
        unreadBadge.setBackground(Theme.PRIMARY_COLOR);
        unreadBadge.setOpaque(true);
        unreadBadge.setBorder(new EmptyBorder(1, 5, 1, 5));

        badgeWrapper.add(statusDotLabel);
        badgeWrapper.add(unreadBadge);

        rightPanel.add(timeLabel);
        rightPanel.add(Box.createVerticalStrut(3));
        rightPanel.add(badgeWrapper);
        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends DummyContact> list,
            DummyContact contact,
            int index,
            boolean isSelected,
            boolean cellHasFocus
    ) {
        if (contact == null) {
            return this;
        }

        // Tạo ký tự viết tắt Avatar
        String initial = contact.name().isEmpty() ? "?" : contact.name().substring(0, 1).toUpperCase();
        avatarLabel.setText(initial);
        avatarLabel.setBackground(contact.isGroup() ? new Color(103, 58, 183) : Theme.PRIMARY_COLOR);

        // Gán thời gian
        timeLabel.setText(contact.lastTime());

        // Đèn báo Online / Offline
        if (contact.isOnline()) {
            statusDotLabel.setForeground(Theme.SUCCESS_COLOR);
            statusDotLabel.setToolTipText("Trực tuyến");
        } else {
            statusDotLabel.setForeground(new Color(185, 190, 195));
            statusDotLabel.setToolTipText("Ngoại tuyến");
        }

        // =====================================================================
        // 👉 CƠ CHẾ XỬ LÝ: CHƯA ĐỌC (UNSEEN) vs ĐÃ ĐỌC (SEEN)
        // =====================================================================
        if (contact.unreadCount() > 0) {
            // UNSEEN: Tên và tin nhắn xem trước TÔ ĐẬM (BOLD), hiện số tin chưa đọc
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            nameLabel.setText(contact.name());
            nameLabel.setForeground(Theme.TEXT_DARK);

            messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            messageLabel.setForeground(Theme.TEXT_DARK);
            messageLabel.setText(truncate(contact.lastMessage(), 24));

            unreadBadge.setText(String.valueOf(contact.unreadCount()));
            unreadBadge.setVisible(true);
        } else {
            // SEEN: Chữ thường (PLAIN), tin nhắn màu xám mờ (MUTED)
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            nameLabel.setText(contact.name());
            nameLabel.setForeground(Theme.TEXT_DARK);

            messageLabel.setFont(Theme.FONT_REGULAR);
            messageLabel.setForeground(Theme.TEXT_MUTED);
            messageLabel.setText(truncate(contact.lastMessage(), 24));

            unreadBadge.setVisible(false);
        }

        // Hiệu ứng khi click chọn dòng
        if (isSelected) {
            setBackground(new Color(230, 240, 255));
        } else {
            setBackground(Color.WHITE);
        }

        return this;
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength) + "...";
    }
}
