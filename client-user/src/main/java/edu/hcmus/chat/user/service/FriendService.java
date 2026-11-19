package edu.hcmus.chat.user.service;

import edu.hcmus.chat.user.ui.DummyUserData;

import java.util.List;

/**
 * Hợp đồng dịch vụ quản lý bạn bè và kết bạn cho Phân hệ Người dùng.
 */
public interface FriendService {

    List<DummyUserData.DummyFriend> getFriends();

    List<DummyUserData.DummyFriendRequest> getFriendRequests();

    boolean sendFriendRequest(String targetUsername);

    boolean acceptFriendRequest(int requestId);

    boolean rejectFriendRequest(int requestId);

    boolean removeFriend(int friendId);

    boolean blockUser(int targetUserId);
}
