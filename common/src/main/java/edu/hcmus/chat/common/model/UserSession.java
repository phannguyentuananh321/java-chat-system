package edu.hcmus.chat.common.model;

import java.time.LocalDateTime;

/**
 * Phiên đăng nhập ánh xạ với bảng user_sessions.
 */
public record UserSession(
        long sessionId,
        int userId,
        LocalDateTime loginAt,
        LocalDateTime lastSeenAt,
        LocalDateTime logoutAt
) {}
