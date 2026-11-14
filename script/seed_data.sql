-- ============================================================================
-- ĐỒ ÁN MÔN LẬP TRÌNH ỨNG DỤNG JAVA - HỆ THỐNG CHAT (HCMUS)
-- SCRIPT DỮ LIỆU MẪU (SEED DATA)
-- ============================================================================

USE chat_system;

-- 1. USERS (Mật khẩu mặc định: 'password123' đã băm SHA-256 hoặc BCrypt)
-- Hash mẫu SHA-256 của 'password123': ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f
INSERT INTO users (user_id, username, password_hash, must_change_password, full_name, email, address, date_of_birth, gender, account_status, role, created_at) VALUES
(1, 'admin', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', FALSE, 'Quản trị viên hệ thống', 'admin@chat.hcmus.edu.vn', '227 Nguyễn Văn Cừ, Q.5, TP.HCM', '1995-05-15', 'MALE', 'ACTIVE', 'ADMIN', '2026-01-01 08:00:00'),
(2, 'nguyenvana', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', FALSE, 'Nguyễn Văn A', 'nva@student.hcmus.edu.vn', 'Linh Trung, TP.Thủ Đức, TP.HCM', '2004-03-12', 'MALE', 'ACTIVE', 'USER', '2026-01-15 09:30:00'),
(3, 'tranthib', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', FALSE, 'Trần Thị B', 'ttb@student.hcmus.edu.vn', 'Quận 10, TP.HCM', '2004-07-25', 'FEMALE', 'ACTIVE', 'USER', '2026-02-01 14:15:00'),
(4, 'lequangc', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', FALSE, 'Lê Quang C', 'lqc@student.hcmus.edu.vn', 'Bình Thạnh, TP.HCM', '2004-11-05', 'MALE', 'ACTIVE', 'USER', '2026-02-20 16:45:00'),
(5, 'phamthid', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', FALSE, 'Phạm Thị D', 'ptd@student.hcmus.edu.vn', 'Gò Vấp, TP.HCM', '2004-09-18', 'FEMALE', 'LOCKED', 'USER', '2026-03-10 10:00:00'),
(6, 'hoangvane', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', TRUE, 'Hoàng Văn E', 'hve@student.hcmus.edu.vn', 'Tân Bình, TP.HCM', '2003-12-30', 'MALE', 'ACTIVE', 'USER', '2026-03-25 11:20:00');

-- 2. USER SESSIONS (Dữ liệu đăng nhập cho biểu đồ và thống kê hoạt động)
INSERT INTO user_sessions (session_id, user_id, login_at, last_seen_at, logout_at) VALUES
(1, 1, '2026-01-02 08:00:00', '2026-01-02 17:00:00', '2026-01-02 17:00:00'),
(2, 2, '2026-01-16 09:00:00', '2026-01-16 11:30:00', '2026-01-16 11:30:00'),
(3, 2, '2026-02-10 13:00:00', '2026-02-10 15:45:00', '2026-02-10 15:45:00'),
(4, 3, '2026-02-15 10:00:00', '2026-02-15 12:00:00', '2026-02-15 12:00:00'),
(5, 4, '2026-03-01 14:00:00', '2026-03-01 16:30:00', '2026-03-01 16:30:00'),
(6, 2, NOW(), NOW(), NULL), -- User 2 đang online
(7, 3, NOW(), NOW(), NULL); -- User 3 đang online

-- 3. FRIENDSHIPS (Quan hệ bạn bè: user_low_id < user_high_id)
INSERT INTO friendships (user_low_id, user_high_id, created_at) VALUES
(2, 3, '2026-02-05 10:00:00'), -- A là bạn của B
(2, 4, '2026-02-22 15:00:00'), -- A là bạn của C
(3, 4, '2026-02-25 09:00:00'), -- B là bạn của C
(4, 6, '2026-03-26 14:00:00'); -- C là bạn của E (E là "bạn của bạn" với A)

-- 4. FRIEND REQUESTS (Lời mời kết bạn đang chờ)
INSERT INTO friend_requests (request_id, sender_id, receiver_id, status, created_at) VALUES
(1, 6, 2, 'PENDING', '2026-03-27 08:30:00');

-- 5. USER BLOCKS (Chặn)
INSERT INTO user_blocks (blocker_user_id, blocked_user_id, created_at) VALUES
(3, 5, '2026-03-12 11:00:00'); -- B block D (D không thấy B khi tìm kiếm)

-- 6. CONVERSATIONS
-- Cuộc trò chuyện 1-1 giữa A (2) và B (3)
INSERT INTO conversations (conversation_id, conversation_type, title, created_by_user_id, direct_user_low_id, direct_user_high_id, is_encrypted, current_key_version, created_at) VALUES
(1, 'DIRECT', NULL, 2, 2, 3, FALSE, 0, '2026-02-05 10:05:00'),
-- Nhóm chat: Nhóm Đồ Án Java HCMUS (A, B, C)
(2, 'GROUP', 'Nhóm Đồ Án Java HCMUS', 2, NULL, NULL, FALSE, 0, '2026-02-26 09:00:00');

-- 7. CONVERSATION MEMBERS
-- Cho cuộc trò chuyện 1-1
INSERT INTO conversation_members (conversation_id, user_id, member_role, joined_at) VALUES
(1, 2, 'MEMBER', '2026-02-05 10:05:00'),
(1, 3, 'MEMBER', '2026-02-05 10:05:00');

-- Cho nhóm chat đồ án (User 2 là ADMIN)
INSERT INTO conversation_members (conversation_id, user_id, member_role, joined_at) VALUES
(2, 2, 'ADMIN', '2026-02-26 09:00:00'),
(2, 3, 'MEMBER', '2026-02-26 09:05:00'),
(2, 4, 'MEMBER', '2026-02-26 09:10:00');

-- 8. MESSAGES
INSERT INTO messages (message_id, conversation_id, sender_id, content, sent_at) VALUES
(1, 1, 2, 'Chào bạn B, mình đã tạo xong khung đồ án nhé!', '2026-02-05 10:10:00'),
(2, 1, 3, 'Tuyệt vời A ơi! Để mình kiểm tra giao diện bên client-user.', '2026-02-05 10:12:00'),
(3, 2, 2, 'Chào mừng mọi người vào nhóm Đồ án Java!', '2026-02-26 09:15:00'),
(4, 2, 3, 'Chào cả nhóm, mình phụ trách phần chat nhé.', '2026-02-26 09:16:00'),
(5, 2, 4, 'Ok, mình chuẩn bị dữ liệu và test các chức năng.', '2026-02-26 09:18:00');

-- 9. SPAM REPORTS
INSERT INTO spam_reports (report_id, reporter_user_id, reported_user_id, related_message_id, reason, status, created_at, handled_by_user_id, handled_at) VALUES
(1, 3, 5, NULL, 'Người dùng này liên tục nhắn tin quấy rối', 'REVIEWED', '2026-03-11 14:00:00', 1, '2026-03-11 15:30:00');
