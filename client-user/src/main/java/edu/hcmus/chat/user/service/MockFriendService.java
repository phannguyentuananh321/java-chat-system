package edu.hcmus.chat.user.service;

import edu.hcmus.chat.user.ui.DummyUserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Hiện thực FriendService dùng dữ liệu mẫu cho Phiên bản 1.
 */
public class MockFriendService implements FriendService {

    @Override
    public List<DummyUserData.DummyFriend> getFriends() {
        return new ArrayList<>(DummyUserData.getDummyFriends());
    }

    @Override
    public List<DummyUserData.DummyFriendRequest> getFriendRequests() {
        return new ArrayList<>(DummyUserData.getDummyRequests());
    }

    @Override
    public boolean sendFriendRequest(String targetUsername) {
        return true;
    }

    @Override
    public boolean acceptFriendRequest(int requestId) {
        return true;
    }

    @Override
    public boolean rejectFriendRequest(int requestId) {
        return true;
    }

    @Override
    public boolean removeFriend(int friendId) {
        return true;
    }

    @Override
    public boolean blockUser(int targetUserId) {
        return true;
    }
}
