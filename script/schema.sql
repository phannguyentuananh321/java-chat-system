-- ============================================================================
-- ĐỒ ÁN MÔN LẬP TRÌNH ỨNG DỤNG JAVA - HỆ THỐNG CHAT (HCMUS)
-- SCRIPT TẠO CƠ SỞ DỮ LIỆU (DATABASE SCHEMA V2)
-- Tương thích: MySQL 8.0+ / MariaDB 10.5+
-- Bảng mã: utf8mb4 (Hỗ trợ tiếng Việt có dấu đầy đủ và Emoji trong tin nhắn)
-- ============================================================================

CREATE DATABASE IF NOT EXISTS chat_system 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE chat_system;

-- Tắt kiểm tra khóa ngoại tạm thời để khởi tạo lại nếu cần
SET FOREIGN_KEY_CHECKS = 0;

DROP VIEW IF EXISTS friend_edges;
DROP TABLE IF EXISTS conversation_member_keys;
DROP TABLE IF EXISTS spam_reports;
DROP TABLE IF EXISTS user_blocks;
DROP TABLE IF EXISTS friend_requests;
DROP TABLE IF EXISTS friendships;
DROP TABLE IF EXISTS message_deletions;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS conversation_members;
DROP TABLE IF EXISTS conversations;
DROP TABLE IF EXISTS user_sessions;
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 1. BẢNG USERS (Tài khoản người dùng và quản trị viên)
-- ============================================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    must_change_password BOOLEAN NOT NULL DEFAULT FALSE,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE,
    address VARCHAR(255) NULL,
    date_of_birth DATE NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NULL,
    account_status ENUM('ACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    public_key TEXT NULL COMMENT 'Khóa công khai RSA/ECDH để trao đổi khóa mã hóa nhóm',
    encrypted_private_key TEXT NULL COMMENT 'Khóa riêng tư đã được mã hóa ở Client',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL COMMENT 'Mốc thời gian xóa mềm (soft delete)',

    INDEX idx_users_full_name (full_name),
    INDEX idx_users_created_at (created_at),
    INDEX idx_users_status_created (account_status, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 2. BẢNG USER_SESSIONS (Phiên đăng nhập & theo dõi hoạt động / online)
-- ============================================================================
CREATE TABLE user_sessions (
    session_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    login_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_seen_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_at TIMESTAMP NULL,

    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_sessions_user_login (user_id, login_at),
    INDEX idx_sessions_login (login_at),
    INDEX idx_sessions_user_presence (user_id, logout_at, last_seen_at),
    INDEX idx_sessions_cleanup (logout_at, last_seen_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 3. BẢNG CONVERSATIONS (Cuộc trò chuyện 1-1 hoặc trò chuyện nhóm)
-- ============================================================================
CREATE TABLE conversations (
    conversation_id INT AUTO_INCREMENT PRIMARY KEY,
    conversation_type ENUM('DIRECT', 'GROUP') NOT NULL,
    title VARCHAR(100) NULL COMMENT 'Tên nhóm chat (NULL đối với chat 1-1)',
    created_by_user_id INT NOT NULL,
    direct_user_low_id INT NULL COMMENT 'User ID nhỏ hơn trong cặp chat 1-1',
    direct_user_high_id INT NULL COMMENT 'User ID lớn hơn trong cặp chat 1-1',
    is_encrypted BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Bật mã hóa đầu cuối (E2EE) cho nhóm',
    current_key_version INT NOT NULL DEFAULT 0 COMMENT 'Phiên bản khóa đối xứng hiện hành của nhóm',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id),
    FOREIGN KEY (direct_user_low_id) REFERENCES users(user_id),
    FOREIGN KEY (direct_user_high_id) REFERENCES users(user_id),

    UNIQUE KEY uk_direct_conversation (direct_user_low_id, direct_user_high_id),
    INDEX idx_conversations_type_title (conversation_type, title),
    INDEX idx_conversations_type_created (conversation_type, created_at),

    CONSTRAINT chk_direct_pair CHECK (
        (conversation_type = 'DIRECT' AND direct_user_low_id IS NOT NULL AND direct_user_high_id IS NOT NULL AND direct_user_low_id < direct_user_high_id)
        OR
        (conversation_type = 'GROUP' AND direct_user_low_id IS NULL AND direct_user_high_id IS NULL)
    ),
    CONSTRAINT chk_group_encryption CHECK (
        conversation_type = 'GROUP' OR is_encrypted = FALSE
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 4. BẢNG CONVERSATION_MEMBERS (Thành viên cuộc trò chuyện & phân quyền nhóm)
-- ============================================================================
CREATE TABLE conversation_members (
    conversation_id INT NOT NULL,
    user_id INT NOT NULL,
    member_role ENUM('MEMBER', 'ADMIN') NOT NULL DEFAULT 'MEMBER',
    joined_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    left_at TIMESTAMP NULL COMMENT 'Đánh dấu thành viên đã rời/bị xóa khỏi nhóm',
    history_cleared_at TIMESTAMP NULL COMMENT 'Mốc thời gian người dùng tự xóa toàn bộ lịch sử',
    last_read_at TIMESTAMP NULL COMMENT 'Mốc đọc tin nhắn cuối cùng (dùng đếm tin chưa đọc)',

    PRIMARY KEY (conversation_id, user_id),
    FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_members_active (user_id, left_at, conversation_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 5. BẢNG MESSAGES (Tin nhắn trao đổi)
-- ============================================================================
CREATE TABLE messages (
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    conversation_id INT NOT NULL,
    sender_id INT NOT NULL,
    content TEXT NOT NULL COMMENT 'Nội dung tin nhắn gốc hoặc ciphertext (Base64) nếu nhóm mã hóa',
    key_version INT NULL COMMENT 'Phiên bản khóa mã hóa (chỉ dùng với nhóm mã hóa)',
    nonce VARCHAR(64) NULL COMMENT 'Giá trị IV/Nonce AES-GCM (chỉ dùng với nhóm mã hóa)',
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    edited_at TIMESTAMP NULL,

    FOREIGN KEY (conversation_id, sender_id) REFERENCES conversation_members(conversation_id, user_id) ON DELETE CASCADE,
    INDEX idx_messages_conv_sent (conversation_id, sent_at),
    INDEX idx_messages_sender_sent (sender_id, sent_at),
    FULLTEXT KEY ft_messages_content (content),

    CONSTRAINT chk_encryption_params CHECK (
        (key_version IS NULL AND nonce IS NULL) OR (key_version IS NOT NULL AND nonce IS NOT NULL)
    )
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 6. BẢNG MESSAGE_DELETIONS (Xóa 1 hoặc vài dòng tin nhắn ở phía người dùng)
-- ============================================================================
CREATE TABLE message_deletions (
    message_id BIGINT NOT NULL,
    user_id INT NOT NULL,
    deleted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (message_id, user_id),
    FOREIGN KEY (message_id) REFERENCES messages(message_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_deletions_user (user_id, deleted_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 7. BẢNG FRIENDSHIPS (Quan hệ bạn bè hai chiều: low_id < high_id)
-- ============================================================================
CREATE TABLE friendships (
    user_low_id INT NOT NULL,
    user_high_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_low_id, user_high_id),
    FOREIGN KEY (user_low_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (user_high_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_friendships_high (user_high_id),

    CONSTRAINT chk_friendship_order CHECK (user_low_id < user_high_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 8. BẢNG FRIEND_REQUESTS (Lời mời kết bạn)
-- ============================================================================
CREATE TABLE friend_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    responded_at TIMESTAMP NULL,

    FOREIGN KEY (sender_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_friend_request (sender_id, receiver_id),
    INDEX idx_requests_receiver (receiver_id, status, created_at),
    INDEX idx_requests_sender (sender_id, status),

    CONSTRAINT chk_request_distinct_users CHECK (sender_id <> receiver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 9. BẢNG USER_BLOCKS (Danh sách chặn tài khoản)
-- ============================================================================
CREATE TABLE user_blocks (
    blocker_user_id INT NOT NULL,
    blocked_user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (blocker_user_id, blocked_user_id),
    FOREIGN KEY (blocker_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (blocked_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_blocks_blocked (blocked_user_id),

    CONSTRAINT chk_block_distinct_users CHECK (blocker_user_id <> blocked_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 10. BẢNG SPAM_REPORTS (Danh sách báo cáo spam người dùng)
-- ============================================================================
CREATE TABLE spam_reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    reporter_user_id INT NOT NULL,
    reported_user_id INT NOT NULL,
    related_message_id BIGINT NULL,
    reason TEXT NULL,
    status ENUM('OPEN', 'REVIEWED', 'DISMISSED') NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    handled_by_user_id INT NULL,
    handled_at TIMESTAMP NULL,

    FOREIGN KEY (reporter_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reported_user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (related_message_id) REFERENCES messages(message_id) ON DELETE SET NULL,
    FOREIGN KEY (handled_by_user_id) REFERENCES users(user_id) ON DELETE SET NULL,

    INDEX idx_reports_created (created_at),
    INDEX idx_reports_reported (reported_user_id, created_at),
    INDEX idx_reports_reporter (reporter_user_id, created_at),
    INDEX idx_reports_status_created (status, created_at),

    CONSTRAINT chk_report_distinct_users CHECK (reporter_user_id <> reported_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 11. BẢNG CONVERSATION_MEMBER_KEYS (Phân phối khóa mã hóa nhóm E2EE)
-- ============================================================================
CREATE TABLE conversation_member_keys (
    conversation_id INT NOT NULL,
    user_id INT NOT NULL,
    key_version INT NOT NULL,
    encrypted_group_key TEXT NOT NULL COMMENT 'Khóa AES của nhóm đã được mã hóa bằng Public Key của user này',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (conversation_id, user_id, key_version),
    FOREIGN KEY (conversation_id, user_id) REFERENCES conversation_members(conversation_id, user_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- 12. VIEW FRIEND_EDGES (Hỗ trợ truy vấn 2 chiều: danh sách bạn bè & bạn của bạn)
-- ============================================================================
CREATE OR REPLACE VIEW friend_edges AS
SELECT user_low_id AS user_id, user_high_id AS friend_id, created_at FROM friendships
UNION ALL
SELECT user_high_id AS user_id, user_low_id AS friend_id, created_at FROM friendships;
