package edu.hcmus.chat.user.ui.components;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Thành phần hiển thị một bong bóng tin nhắn (Chat Bubble).
 * THUẦN 100% JAVA SWING (JTextArea + JPanel) - TUYỆT ĐỐI KHÔNG DÙNG THẺ HTML/CSS.
 */
public class ChatBubblePanel extends JPanel {

    public ChatBubblePanel(String senderName, String messageContent, String time, boolean isMe) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(4, 10, 4, 10));

        // Khung chứa nội dung tin nhắn (Bong bóng)
        JPanel bubbleContainer = new JPanel();
        bubbleContainer.setLayout(new BoxLayout(bubbleContainer, BoxLayout.Y_AXIS));
        bubbleContainer.setOpaque(true);

        // 1. Tên người gửi (nếu là tin nhắn của đối phương)
        if (!isMe && senderName != null && !senderName.isEmpty()) {
            JLabel nameLabel = new JLabel(senderName);
            nameLabel.setFont(Theme.FONT_SMALL);
            nameLabel.setForeground(Theme.PRIMARY_COLOR);
            nameLabel.setBorder(new EmptyBorder(0, 0, 2, 0));
            bubbleContainer.add(nameLabel);
        }

        // 2. Ô văn bản tin nhắn (Dùng JTextArea tự động ngắt dòng, 100% Java thuần)
        JTextArea messageArea = new JTextArea(messageContent);
        messageArea.setFont(Theme.FONT_REGULAR);
        messageArea.setEditable(false);
        messageArea.setFocusable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setOpaque(false);
        // Giới hạn chiều rộng tối đa để tin nhắn không bị kéo dài hết bề ngang màn hình
        messageArea.setColumns(Math.min(30, Math.max(12, messageContent.length())));

        bubbleContainer.add(messageArea);

        // 3. Thời gian gửi
        JLabel timeLabel = new JLabel(time);
        timeLabel.setFont(Theme.FONT_SMALL);
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Phối màu & Bo góc theo người gửi
        if (isMe) {
            bubbleContainer.setBackground(Theme.PRIMARY_COLOR);
            messageArea.setForeground(Theme.PRIMARY_TEXT);
            timeLabel.setForeground(new Color(220, 235, 255));
            bubbleContainer.setBorder(new CompoundBorder(
                    new LineBorder(Theme.PRIMARY_HOVER, 1, true),
                    new EmptyBorder(8, 12, 6, 12)
            ));
        } else {
            bubbleContainer.setBackground(Theme.SECONDARY_COLOR);
            messageArea.setForeground(Theme.TEXT_DARK);
            timeLabel.setForeground(Theme.TEXT_MUTED);
            bubbleContainer.setBorder(new CompoundBorder(
                    new LineBorder(Theme.BORDER_COLOR, 1, true),
                    new EmptyBorder(8, 12, 6, 12)
            ));
        }

        bubbleContainer.add(Box.createVerticalStrut(2));
        bubbleContainer.add(timeLabel);

        // Căn chỉnh vị trí Trái / Phải
        JPanel alignmentPanel = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 0));
        alignmentPanel.setOpaque(false);
        alignmentPanel.add(bubbleContainer);

        add(alignmentPanel, BorderLayout.CENTER);
    }
}
