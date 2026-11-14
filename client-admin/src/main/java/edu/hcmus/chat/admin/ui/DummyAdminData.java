package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserRole;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lớp cung cấp dữ liệu giả phục vụ thiết kế giao diện Quản trị viên.
 * Dữ liệu được đồng bộ 100% với cấu trúc bảng users trong CSDL.
 */
public final class DummyAdminData {

    private static final List<User> DUMMY_USERS = new ArrayList<>();

    static {
        DUMMY_USERS.add(new User(
                1,
                "admin",
                "Quản trị viên hệ thống",
                "admin@chat.hcmus.edu.vn",
                "227 Nguyễn Văn Cừ, Q.5, TP.HCM",
                LocalDate.of(1995, 5, 15),
                GenderType.MALE,
                AccountStatus.ACTIVE,
                UserRole.ADMIN
        ));

        DUMMY_USERS.add(new User(
                2,
                "nguyenvana",
                "Nguyễn Văn A",
                "nva@student.hcmus.edu.vn",
                "Linh Trung, TP.Thủ Đức, TP.HCM",
                LocalDate.of(2004, 3, 12),
                GenderType.MALE,
                AccountStatus.ACTIVE,
                UserRole.USER
        ));

        DUMMY_USERS.add(new User(
                3,
                "tranthib",
                "Trần Thị B",
                "ttb@student.hcmus.edu.vn",
                "Quận 10, TP.HCM",
                LocalDate.of(2004, 7, 25),
                GenderType.FEMALE,
                AccountStatus.ACTIVE,
                UserRole.USER
        ));

        DUMMY_USERS.add(new User(
                4,
                "lequangc",
                "Lê Quang C",
                "lqc@student.hcmus.edu.vn",
                "Bình Thạnh, TP.HCM",
                LocalDate.of(2004, 11, 5),
                GenderType.MALE,
                AccountStatus.ACTIVE,
                UserRole.USER
        ));

        DUMMY_USERS.add(new User(
                5,
                "phamthid",
                "Phạm Thị D",
                "ptd@student.hcmus.edu.vn",
                "Gò Vấp, TP.HCM",
                LocalDate.of(2004, 9, 18),
                GenderType.FEMALE,
                AccountStatus.LOCKED,
                UserRole.USER
        ));

        DUMMY_USERS.add(new User(
                6,
                "hoangvane",
                "Hoàng Văn E",
                "hve@student.hcmus.edu.vn",
                "Tân Bình, TP.HCM",
                LocalDate.of(2003, 12, 30),
                GenderType.MALE,
                AccountStatus.ACTIVE,
                UserRole.USER
        ));
    }

    private DummyAdminData() {
        // Lớp tiện ích không khởi tạo
    }

    /**
     * Lấy danh sách tài khoản mẫu đồng bộ theo chuẩn Model CSDL.
     */
    public static List<User> getDummyUsers() {
        return Collections.unmodifiableList(DUMMY_USERS);
    }
}
