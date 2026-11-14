package edu.hcmus.chat.common.model;

import java.time.LocalDateTime;

/**
 * Tin nhắn trao đổi ánh xạ với bảng messages.
 */
public record Message(
        long messageId,
        int conversationId,
        int senderId,
        String content,
        Integer keyVersion,
        String nonce,
        LocalDateTime sentAt,
        LocalDateTime editedAt
) {}
