package edu.hcmus.chat.user.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lớp chứa dữ liệu mẫu phục vụ hiển thị thử nghiệm giao diện Người dùng.
 * Không chứa mã nguồn truy vấn CSDL hay kết nối mạng socket thực tế.
 */
public final class DummyUserData {

    public record DummyContact(
            String id,
            String name,
            String lastMessage,
            String lastTime,
            boolean isOnline,
            int unreadCount
    ) {}

    public record DummyMessage(
            String sender,
            String content,
            String timestamp,
            boolean isMe
    ) {}

    private static final List<DummyContact> DUMMY_CONTACTS = new ArrayList<>();
    private static final List<DummyMessage> DUMMY_MESSAGES = new ArrayList<>();

    static {
        DUMMY_CONTACTS.add(new DummyContact("u1", "Nhóm Đồ Án Java HCMUS", "Nguyễn Văn A: Đã tạo xong khung dự án!", "10:15", true, 2));
        DUMMY_CONTACTS.add(new DummyContact("u2", "Trần Thị B", "Chào bạn, khi nào nộp đồ án nhỉ?", "09:30", true, 0));
        DUMMY_CONTACTS.add(new DummyContact("u3", "Lê Quang C", "File cấu hình mẫu ở đâu vậy bạn?", "Hôm qua", false, 0));
        DUMMY_CONTACTS.add(new DummyContact("u4", "Thầy Hướng Dẫn", "Nhớ tuân thủ các quy định về socket TCP nhé.", "08/11", false, 0));

        DUMMY_MESSAGES.add(new DummyMessage("Nguyễn Văn A", "Chào mọi người, dự án đã bắt đầu!", "10:00", false));
        DUMMY_MESSAGES.add(new DummyMessage("Tôi", "Chào A, mình đang làm khung giao diện chat.", "10:05", true));
        DUMMY_MESSAGES.add(new DummyMessage("Nguyễn Văn A", "Tuyệt vời, nhớ chia sẻ cấu trúc Git nhé.", "10:10", false));
        DUMMY_MESSAGES.add(new DummyMessage("Tôi", "Đã xong, hai chúng ta làm việc trên hai module riêng biệt không lo conflict.", "10:12", true));
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
