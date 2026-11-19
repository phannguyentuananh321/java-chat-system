package edu.hcmus.chat.user.service;

/**
 * Nhà máy dịch vụ trung tâm (Service Factory) cho Phân hệ Người dùng.
 * Cung cấp công tắc duy nhất USE_MOCK_DATA giúp chuyển đổi giữa Dữ liệu mẫu (Phiên bản 1)
 * và Dữ liệu kết nối Socket TCP thật (Phiên bản 2) mà không cần chỉnh sửa giao diện.
 */
public final class UserServiceFactory {

    // =========================================================================
    // 👉 CÔNG TẮC DUY NHẤT:
    // true  = Đang phát triển Giao diện Phiên bản 1 (Dùng dữ liệu mẫu)
    // false = Kết nối Socket TCP và CSDL thật ở Phiên bản 2
    // =========================================================================
    public static final boolean USE_MOCK_DATA = true;

    private static final AuthService AUTH_SERVICE = new MockAuthService();
    private static final ChatService CHAT_SERVICE = new MockChatService();
    private static final FriendService FRIEND_SERVICE = new MockFriendService();

    private UserServiceFactory() {}

    public static AuthService getAuthService() {
        if (USE_MOCK_DATA) {
            return AUTH_SERVICE;
        }
        // Phiên bản 2: return new SocketAuthService();
        return AUTH_SERVICE;
    }

    public static ChatService getChatService() {
        if (USE_MOCK_DATA) {
            return CHAT_SERVICE;
        }
        // Phiên bản 2: return new SocketChatService();
        return CHAT_SERVICE;
    }

    public static FriendService getFriendService() {
        if (USE_MOCK_DATA) {
            return FRIEND_SERVICE;
        }
        // Phiên bản 2: return new SocketFriendService();
        return FRIEND_SERVICE;
    }
}
