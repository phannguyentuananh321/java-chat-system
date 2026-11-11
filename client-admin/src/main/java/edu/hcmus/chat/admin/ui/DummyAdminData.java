package edu.hcmus.chat.admin.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lớp chứa dữ liệu mẫu phục vụ hiển thị thử nghiệm giao diện Quản trị.
 * Không chứa mã nguồn truy vấn CSDL hay kết nối mạng thực tế.
 */
public final class DummyAdminData {

    public record DummyUser(
            long id,
            String username,
            String fullName,
            String email,
            String role,
            String status,
            String createdAt
    ) {}

    private static final List<DummyUser> DUMMY_USERS = new ArrayList<>();

    static {
        DUMMY_USERS.add(new DummyUser(1L, "admin", "Quản trị viên hệ thống", "admin@chat.hcmus.edu.vn", "ADMIN", "Hoạt động", "2026-01-10"));
        DUMMY_USERS.add(new DummyUser(2L, "nguyenvana", "Nguyễn Văn A", "nva@student.hcmus.edu.vn", "USER", "Hoạt động", "2026-02-15"));
        DUMMY_USERS.add(new DummyUser(3L, "tranthib", "Trần Thị B", "ttb@student.hcmus.edu.vn", "USER", "Bị khóa", "2026-02-18"));
        DUMMY_USERS.add(new DummyUser(4L, "lequangc", "Lê Quang C", "lqc@student.hcmus.edu.vn", "USER", "Hoạt động", "2026-03-01"));
        DUMMY_USERS.add(new DummyUser(5L, "phamthid", "Phạm Thị D", "ptd@student.hcmus.edu.vn", "USER", "Chờ duyệt", "2026-03-05"));
    }

    private DummyAdminData() {
        // Lớp tiện ích không khởi tạo
    }

    /**
     * Lấy danh sách người dùng mẫu không thể sửa đổi (unmodifiable).
     */
    public static List<DummyUser> getDummyUsers() {
        return Collections.unmodifiableList(DUMMY_USERS);
    }
}
