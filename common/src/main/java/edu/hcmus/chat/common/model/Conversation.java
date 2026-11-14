package edu.hcmus.chat.common.model;

import java.time.LocalDateTime;

/**
 * Cuộc trò chuyện ánh xạ với bảng conversations.
 */
public record Conversation(
        int conversationId,
        ConversationType conversationType,
        String title,
        int createdByUserId,
        Integer directUserLowId,
        Integer directUserHighId,
        boolean isEncrypted,
        int currentKeyVersion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
