package edu.hcmus.chat.user.service;

import edu.hcmus.chat.user.ui.DummyUserData;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Hiện thực ChatService dùng dữ liệu mẫu cho Phiên bản 1.
 * Đã hỗ trợ phân tách tin nhắn theo từng cuộc trò chuyện (conversationId)
 * và lưu trữ tin nhắn mới gửi trong bộ nhớ phiên làm việc.
 */
public class MockChatService implements ChatService {

    @Override
    public List<DummyUserData.DummyContact> getConversations() {
        return new ArrayList<>(DummyUserData.getDummyContacts());
    }

    @Override
    public List<DummyUserData.DummyMessage> getMessages(int conversationId) {
        return new ArrayList<>(DummyUserData.getDummyMessages(conversationId));
    }

    @Override
    public void sendMessage(int conversationId, String content) {
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        DummyUserData.DummyMessage newMsg = new DummyUserData.DummyMessage(
                System.currentTimeMillis(),
                1,
                "Tôi",
                content,
                currentTime,
                true
        );
        DummyUserData.addMessage(conversationId, newMsg);
    }

    @Override
    public boolean createGroup(String groupName, List<Integer> memberIds) {
        return groupName != null && !groupName.trim().isEmpty();
    }
}
