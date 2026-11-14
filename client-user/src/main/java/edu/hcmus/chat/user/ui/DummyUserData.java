package edu.hcmus.chat.user.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lớp chứa dữ liệu mẫu phục vụ hiển thị thử nghiệm giao diện Người dùng.
 * Các trường thông tin phản ánh đúng mô hình hội thoại và tin nhắn trong CSDL.
 */
public final class DummyUserData {

    public record DummyContact(
            int conversationId,
            String name,
            String lastMessage,
            String lastTime,
            boolean isGroup,
            boolean isOnline,
            int unreadCount
    ) {}

    public record DummyMessage(
            long messageId,
            int senderId,
            String senderName,
            String content,
            String timestamp,
            boolean isMe
    ) {}

    private static final List<DummyContact> DUMMY_CONTACTS = new ArrayList<>();
    private static final List<DummyMessage> DUMMY_MESSAGES = new ArrayList<>();

    static {
        DUMMY_CONTACTS.add(new DummyContact(2, "Nhóm Đồ Án Java HCMUS", "Lê Quang C: Ok, mình chuẩn bị dữ liệu và test các chức năng.", "09:18", true, true, 2));
        DUMMY_CONTACTS.add(new DummyContact(1, "Trần Thị B", "Tuyệt vời A ơi! Để mình kiểm tra giao diện bên client-user.", "10:12", false, true, 0));
        DUMMY_CONTACTS.add(new DummyContact(3, "Lê Quang C", "File cấu hình mẫu ở đâu vậy bạn?", "Hôm qua", false, false, 0));
        DUMMY_CONTACTS.add(new DummyContact(4, "Hoàng Văn E", "Chào bạn, khi nào nộp đồ án nhỉ?", "08/11", false, false, 0));

        DUMMY_MESSAGES.add(new DummyMessage(1, 2, "Nguyễn Văn A", "Chào bạn B, mình đã tạo xong khung đồ án nhé!", "10:10", true));
        DUMMY_MESSAGES.add(new DummyMessage(2, 3, "Trần Thị B", "Tuyệt vời A ơi! Để mình kiểm tra giao diện bên client-user.", "10:12", false));
        DUMMY_MESSAGES.add(new DummyMessage(3, 2, "Nguyễn Văn A", "Đã xong, hai chúng ta làm việc trên hai module riêng biệt không lo conflict.", "10:15", true));
    }

    private DummyUserData() {
        // Lớp tiện ích không khởi tạo
    }

    /**
     * Lấy danh sách liên hệ mẫu.
     */
    public static List<DummyContact> getDummyContacts() {
        return Collections.unmodifiableList(DUMMY_CONTACTS);
    }

    /**
     * Lấy danh sách tin nhắn mẫu trong hội thoại hiện tại.
     */
    public static List<DummyMessage> getDummyMessages() {
        return Collections.unmodifiableList(DUMMY_MESSAGES);
    }
}
