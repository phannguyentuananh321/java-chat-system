package edu.hcmus.chat.user.service;

import edu.hcmus.chat.user.ui.DummyUserData;

import java.util.ArrayList;
import java.util.List;

/**
 * Hiện thực ChatService dùng dữ liệu mẫu cho Phiên bản 1.
 */
public class MockChatService implements ChatService {

    @Override
    public List<DummyUserData.DummyContact> getConversations() {
        return new ArrayList<>(DummyUserData.getDummyContacts());
    }

    @Override
    public List<DummyUserData.DummyMessage> getMessages(int conversationId) {
        return new ArrayList<>(DummyUserData.getDummyMessages());
    }

    @Override
    public void sendMessage(int conversationId, String content) {
        // Mock gửi tin nhắn thành công
    }

    @Override
    public boolean createGroup(String groupName, List<Integer> memberIds) {
        return groupName != null && !groupName.trim().isEmpty();
    }
}
