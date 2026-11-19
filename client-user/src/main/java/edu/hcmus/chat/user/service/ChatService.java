package edu.hcmus.chat.user.service;

import edu.hcmus.chat.user.ui.DummyUserData;

import java.util.List;

/**
 * Hợp đồng dịch vụ hội thoại và tin nhắn cho Phân hệ Người dùng.
 */
public interface ChatService {

    List<DummyUserData.DummyContact> getConversations();

    List<DummyUserData.DummyMessage> getMessages(int conversationId);

    void sendMessage(int conversationId, String content);

    boolean createGroup(String groupName, List<Integer> memberIds);
}
