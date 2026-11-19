package edu.hcmus.chat.user.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lớp chứa dữ liệu mẫu phục vụ hiển thị thử nghiệm giao diện Người dùng (Thành viên B).
 * Tách biệt hoàn toàn khỏi mã nguồn giao diện, chuẩn bị cho việc chuyển đổi sang Socket ở Phiên bản 2.
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

    public record DummyFriend(
            int userId,
            String username,
            String fullName,
            String email,
            boolean isOnline
    ) {}

    public record DummyFriendRequest(
            int requestId,
            int senderId,
            String senderName,
            String senderEmail,
            String requestTime
    ) {}

    private static final List<DummyContact> DUMMY_CONTACTS = new ArrayList<>();
    private static final List<DummyMessage> DUMMY_MESSAGES = new ArrayList<>();
    private static final List<DummyFriend> DUMMY_FRIENDS = new ArrayList<>();
    private static final List<DummyFriendRequest> DUMMY_REQUESTS = new ArrayList<>();

    static {
        // Danh sách hội thoại bên trái (Gồm cả trạng thái đã đọc và chưa đọc)
        DUMMY_CONTACTS.add(new DummyContact(2, "Nhóm Đồ Án Java HCMUS", "Lê Quang C: Ok, mình chuẩn bị dữ liệu và test các chức năng.", "09:18", true, true, 2));
        DUMMY_CONTACTS.add(new DummyContact(1, "Trần Thị B", "Tuyệt vời A ơi! Để mình kiểm tra giao diện bên client-user.", "10:12", false, true, 0));
        DUMMY_CONTACTS.add(new DummyContact(3, "Lê Quang C", "File cấu hình mẫu ở đâu vậy bạn?", "Hôm qua", false, false, 0));
        DUMMY_CONTACTS.add(new DummyContact(4, "Hoàng Văn E", "Chào bạn, khi nào nộp đồ án nhỉ?", "08/11", false, false, 0));

        // Tin nhắn trong hội thoại
        DUMMY_MESSAGES.add(new DummyMessage(1, 2, "Nguyễn Văn A", "Chào bạn B, mình đã tạo xong khung đồ án nhé!", "10:10", true));
        DUMMY_MESSAGES.add(new DummyMessage(2, 3, "Trần Thị B", "Tuyệt vời A ơi! Để mình kiểm tra giao diện bên client-user.", "10:12", false));
        DUMMY_MESSAGES.add(new DummyMessage(3, 2, "Nguyễn Văn A", "Đã xong, hai chúng ta làm việc trên hai module riêng biệt không lo conflict.", "10:15", true));
        DUMMY_MESSAGES.add(new DummyMessage(4, 3, "Trần Thị B", "Ok bạn, mình đang hoàn thiện phần bong bóng chat bằng 100% Java Swing thuần.", "10:16", false));

        // Danh sách bạn bè
        DUMMY_FRIENDS.add(new DummyFriend(3, "tranthib", "Trần Thị B", "ttb@student.hcmus.edu.vn", true));
        DUMMY_FRIENDS.add(new DummyFriend(4, "lequangc", "Lê Quang C", "lqc@student.hcmus.edu.vn", true));
        DUMMY_FRIENDS.add(new DummyFriend(6, "hoangvane", "Hoàng Văn E", "hve@student.hcmus.edu.vn", false));

        // Lời mời kết bạn đang chờ
        DUMMY_REQUESTS.add(new DummyFriendRequest(1, 5, "Phạm Thị D", "ptd@student.hcmus.edu.vn", "Hôm qua 15:30"));
    }

    private DummyUserData() {}

    public static List<DummyContact> getDummyContacts() {
        return Collections.unmodifiableList(DUMMY_CONTACTS);
    }

    public static List<DummyMessage> getDummyMessages() {
        return Collections.unmodifiableList(DUMMY_MESSAGES);
    }

    public static List<DummyFriend> getDummyFriends() {
        return Collections.unmodifiableList(DUMMY_FRIENDS);
    }

    public static List<DummyFriendRequest> getDummyRequests() {
        return Collections.unmodifiableList(DUMMY_REQUESTS);
    }
}
