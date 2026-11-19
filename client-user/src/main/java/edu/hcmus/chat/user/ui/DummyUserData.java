package edu.hcmus.chat.user.ui;

import java.util.*;

/**
 * Lớp chứa dữ liệu mẫu phục vụ hiển thị thử nghiệm giao diện Người dùng (Thành viên B).
 * Dữ liệu được phân tách riêng theo từng mã cuộc trò chuyện (conversationId).
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
    private static final Map<Integer, List<DummyMessage>> CONVERSATION_MESSAGES = new HashMap<>();
    private static final List<DummyFriend> DUMMY_FRIENDS = new ArrayList<>();
    private static final List<DummyFriendRequest> DUMMY_REQUESTS = new ArrayList<>();

    static {
        // 1. Danh sách 4 cuộc trò chuyện khác nhau
        DUMMY_CONTACTS.add(new DummyContact(2, "Nhóm Đồ Án Java HCMUS", "Lê Quang C: Ok, mình chuẩn bị dữ liệu và test các chức năng.", "09:18", true, true, 2));
        DUMMY_CONTACTS.add(new DummyContact(1, "Trần Thị B", "Ok bạn, mình đang hoàn thiện phần bong bóng chat.", "10:16", false, true, 0));
        DUMMY_CONTACTS.add(new DummyContact(3, "Lê Quang C", "Cảm ơn bạn, mình thấy rồi!", "Hôm qua", false, false, 0));
        DUMMY_CONTACTS.add(new DummyContact(4, "Hoàng Văn E", "Ok cảm ơn bạn nhiều!", "08/11", false, false, 0));

        // 2. Tin nhắn riêng biệt cho từng cuộc trò chuyện:

        // Cuộc trò chuyện 2: Nhóm Đồ Án Java HCMUS
        List<DummyMessage> groupMsgs = new ArrayList<>();
        groupMsgs.add(new DummyMessage(1, 2, "Nguyễn Văn A", "Chào mừng mọi người vào nhóm Đồ án Java!", "09:00", false));
        groupMsgs.add(new DummyMessage(2, 3, "Trần Thị B", "Chào cả nhóm, mình phụ trách phân hệ người dùng nhé.", "09:05", false));
        groupMsgs.add(new DummyMessage(3, 4, "Lê Quang C", "Ok, mình chuẩn bị dữ liệu và test các chức năng.", "09:18", false));
        groupMsgs.add(new DummyMessage(4, 1, "Tôi", "Nhóm mình nhớ tuân thủ các quy định của thầy về Java thuần và Socket nhé!", "09:20", true));
        CONVERSATION_MESSAGES.put(2, groupMsgs);

        // Cuộc trò chuyện 1: Chat 1-1 với Trần Thị B
        List<DummyMessage> bMsgs = new ArrayList<>();
        bMsgs.add(new DummyMessage(10, 1, "Tôi", "Chào bạn B, mình đã tạo xong khung đồ án nhé!", "10:10", true));
        bMsgs.add(new DummyMessage(11, 3, "Trần Thị B", "Tuyệt vời A ơi! Để mình kiểm tra giao diện bên client-user.", "10:12", false));
        bMsgs.add(new DummyMessage(12, 1, "Tôi", "Đã xong, hai chúng ta làm việc trên hai module riêng biệt không lo conflict.", "10:15", true));
        bMsgs.add(new DummyMessage(13, 3, "Trần Thị B", "Ok bạn, mình đang hoàn thiện phần bong bóng chat bằng 100% Java Swing thuần.", "10:16", false));
        CONVERSATION_MESSAGES.put(1, bMsgs);

        // Cuộc trò chuyện 3: Chat 1-1 với Lê Quang C
        List<DummyMessage> cMsgs = new ArrayList<>();
        cMsgs.add(new DummyMessage(20, 4, "Lê Quang C", "File cấu hình mẫu ở đâu vậy bạn?", "Hôm qua 14:00", false));
        cMsgs.add(new DummyMessage(21, 1, "Tôi", "File config.properties.example ở ngay thư mục gốc của repo nhé C.", "Hôm qua 14:05", true));
        cMsgs.add(new DummyMessage(22, 4, "Lê Quang C", "Cảm ơn bạn, mình thấy rồi!", "Hôm qua 14:10", false));
        CONVERSATION_MESSAGES.put(3, cMsgs);

        // Cuộc trò chuyện 4: Chat 1-1 với Hoàng Văn E
        List<DummyMessage> eMsgs = new ArrayList<>();
        eMsgs.add(new DummyMessage(30, 6, "Hoàng Văn E", "Chào bạn, khi nào nộp đồ án nhỉ?", "08/11 08:30", false));
        eMsgs.add(new DummyMessage(31, 1, "Tôi", "Phiên bản 1 nộp giao diện và CSDL trước bạn nhé.", "08/11 08:35", true));
        eMsgs.add(new DummyMessage(32, 6, "Hoàng Văn E", "Ok cảm ơn bạn nhiều!", "08/11 08:40", false));
        CONVERSATION_MESSAGES.put(4, eMsgs);

        // 3. Danh sách bạn bè
        DUMMY_FRIENDS.add(new DummyFriend(3, "tranthib", "Trần Thị B", "ttb@student.hcmus.edu.vn", true));
        DUMMY_FRIENDS.add(new DummyFriend(4, "lequangc", "Lê Quang C", "lqc@student.hcmus.edu.vn", true));
        DUMMY_FRIENDS.add(new DummyFriend(6, "hoangvane", "Hoàng Văn E", "hve@student.hcmus.edu.vn", false));

        // 4. Lời mời kết bạn đang chờ
        DUMMY_REQUESTS.add(new DummyFriendRequest(1, 5, "Phạm Thị D", "ptd@student.hcmus.edu.vn", "Hôm qua 15:30"));
    }

    private DummyUserData() {}

    public static List<DummyContact> getDummyContacts() {
        return Collections.unmodifiableList(DUMMY_CONTACTS);
    }

    public static List<DummyMessage> getDummyMessages(int conversationId) {
        List<DummyMessage> list = CONVERSATION_MESSAGES.get(conversationId);
        if (list == null) {
            list = new ArrayList<>();
            CONVERSATION_MESSAGES.put(conversationId, list);
        }
        return list;
    }

    public static void addMessage(int conversationId, DummyMessage message) {
        List<DummyMessage> list = CONVERSATION_MESSAGES.computeIfAbsent(conversationId, k -> new ArrayList<>());
        list.add(message);
    }

    public static List<DummyFriend> getDummyFriends() {
        return Collections.unmodifiableList(DUMMY_FRIENDS);
    }

    public static List<DummyFriendRequest> getDummyRequests() {
        return Collections.unmodifiableList(DUMMY_REQUESTS);
    }
}
