package edu.hcmus.chat.common.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Đối tượng Người dùng ánh xạ chính xác với bảng users trong CSDL.
 */
public record User(
        int userId,
        String username,
        String passwordHash,
        boolean mustChangePassword,
        String fullName,
        String email,
        String address,
        LocalDate dateOfBirth,
        GenderType gender,
        AccountStatus accountStatus,
        UserRole role,
        String publicKey,
        String encryptedPrivateKey,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
    /**
     * Constructor thuận tiện cho hiển thị giao diện và dữ liệu mẫu.
     */
    public User(
            int userId,
            String username,
            String fullName,
            String email,
            String address,
            LocalDate dateOfBirth,
            GenderType gender,
            AccountStatus accountStatus,
            UserRole role
    ) {
        this(userId, username, "", false, fullName, email, address, dateOfBirth, gender, accountStatus, role, null, null, LocalDateTime.now(), null, null);
    }
}
