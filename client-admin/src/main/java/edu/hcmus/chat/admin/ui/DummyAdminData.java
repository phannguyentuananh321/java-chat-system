package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sample data for the admin screens (V1 has no server or database yet).
 *
 * Users follow the users table. The other lists are "row" records: one record per
 * table row a screen shows, already joined (e.g. a login row carries the username).
 * That is roughly what the admin side will ask the server for later.
 */
public final class DummyAdminData {

    /**
     * One row of user_sessions, joined with the user's names.
     * logoutAt is null while the session is still open.
     */
    public record LoginRecord(
            int userId,
            String username,
            String fullName,
            LocalDateTime loginAt,
            LocalDateTime logoutAt
    ) {}

    private static final List<User> DUMMY_USERS = new ArrayList<>();
    private static final List<LoginRecord> DUMMY_LOGINS = new ArrayList<>();

    /**
     * Accepted friendships as {userId, userId} pairs, the same shape as the friendships table.
     */
    private static final int[][] FRIEND_PAIRS = {
            {2, 3}, {2, 4}, {2, 6}, {3, 4}, {3, 5}, {3, 7},
            {4, 8}, {6, 9}, {7, 10}, {8, 11}, {9, 10}, {11, 12}
    };

    static {
        DUMMY_USERS.add(newUser(1, "admin", "Quản trị viên hệ thống", "admin@chat.hcmus.edu.vn",
                "227 Nguyễn Văn Cừ, Q.5, TP.HCM", LocalDate.of(1995, 5, 15), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.ADMIN, LocalDateTime.of(2024, 1, 5, 8, 0)));
        DUMMY_USERS.add(newUser(2, "nguyenvana", "Nguyễn Văn A", "nva@student.hcmus.edu.vn",
                "Linh Trung, TP.Thủ Đức, TP.HCM", LocalDate.of(2004, 3, 12), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2024, 3, 14, 9, 30)));
        DUMMY_USERS.add(newUser(3, "tranthib", "Trần Thị B", "ttb@student.hcmus.edu.vn",
                "Quận 10, TP.HCM", LocalDate.of(2004, 7, 25), GenderType.FEMALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2024, 6, 2, 14, 10)));
        DUMMY_USERS.add(newUser(4, "lequangc", "Lê Quang C", "lqc@student.hcmus.edu.vn",
                "Bình Thạnh, TP.HCM", LocalDate.of(2004, 11, 5), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2024, 9, 21, 20, 45)));
        DUMMY_USERS.add(newUser(5, "phamthid", "Phạm Thị D", "ptd@student.hcmus.edu.vn",
                "Gò Vấp, TP.HCM", LocalDate.of(2004, 9, 18), GenderType.FEMALE,
                AccountStatus.LOCKED, UserRole.USER, LocalDateTime.of(2025, 1, 10, 10, 5)));
        DUMMY_USERS.add(newUser(6, "hoangvane", "Hoàng Văn E", "hve@student.hcmus.edu.vn",
                "Tân Bình, TP.HCM", LocalDate.of(2003, 12, 30), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2025, 4, 18, 16, 20)));
        DUMMY_USERS.add(newUser(7, "vothif", "Võ Thị F", "vtf@student.hcmus.edu.vn",
                "Quận 7, TP.HCM", LocalDate.of(2005, 2, 9), GenderType.FEMALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2025, 8, 3, 11, 0)));
        DUMMY_USERS.add(newUser(8, "dangminhg", "Đặng Minh G", "dmg@student.hcmus.edu.vn",
                "Thủ Dầu Một, Bình Dương", LocalDate.of(2004, 6, 1), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2025, 11, 27, 19, 15)));
        DUMMY_USERS.add(newUser(9, "buithih", "Bùi Thị H", "bth@student.hcmus.edu.vn",
                "Biên Hòa, Đồng Nai", LocalDate.of(2005, 10, 14), GenderType.FEMALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2026, 2, 14, 7, 50)));
        DUMMY_USERS.add(newUser(10, "ngoquangi", "Ngô Quang I", "nqi@student.hcmus.edu.vn",
                "Quận 3, TP.HCM", LocalDate.of(2004, 1, 22), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2026, 5, 9, 13, 35)));
        DUMMY_USERS.add(newUser(11, "duongthik", "Dương Thị K", "dtk@student.hcmus.edu.vn",
                "Phú Nhuận, TP.HCM", LocalDate.of(2005, 8, 8), GenderType.OTHER,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2026, 9, 30, 21, 5)));
        DUMMY_USERS.add(newUser(12, "lyvanl", "Lý Văn L", "lvl@gmail.com",
                "Quận 12, TP.HCM", LocalDate.of(2002, 4, 4), GenderType.MALE,
                AccountStatus.ACTIVE, UserRole.USER, LocalDateTime.of(2026, 11, 2, 23, 40)));

        // Newest first, the order the login history screen shows them in
        addLogin(2, LocalDateTime.of(2026, 11, 20, 8, 15), null);
        addLogin(1, LocalDateTime.of(2026, 11, 20, 7, 58), null);
        addLogin(7, LocalDateTime.of(2026, 11, 19, 22, 4), LocalDateTime.of(2026, 11, 19, 23, 30));
        addLogin(3, LocalDateTime.of(2026, 11, 19, 20, 41), LocalDateTime.of(2026, 11, 19, 21, 12));
        addLogin(12, LocalDateTime.of(2026, 11, 19, 18, 2), LocalDateTime.of(2026, 11, 19, 18, 9));
        addLogin(4, LocalDateTime.of(2026, 11, 19, 9, 30), LocalDateTime.of(2026, 11, 19, 11, 45));
        addLogin(2, LocalDateTime.of(2026, 11, 18, 21, 10), LocalDateTime.of(2026, 11, 18, 23, 55));
        addLogin(10, LocalDateTime.of(2026, 11, 18, 15, 22), LocalDateTime.of(2026, 11, 18, 16, 0));
        addLogin(6, LocalDateTime.of(2026, 11, 18, 7, 45), LocalDateTime.of(2026, 11, 18, 8, 20));
        addLogin(9, LocalDateTime.of(2026, 11, 17, 19, 33), LocalDateTime.of(2026, 11, 17, 20, 50));
        addLogin(3, LocalDateTime.of(2026, 11, 17, 13, 5), LocalDateTime.of(2026, 11, 17, 14, 40));
        addLogin(11, LocalDateTime.of(2026, 11, 16, 22, 18), LocalDateTime.of(2026, 11, 16, 22, 59));
        addLogin(2, LocalDateTime.of(2026, 11, 16, 10, 0), LocalDateTime.of(2026, 11, 16, 12, 30));
        addLogin(8, LocalDateTime.of(2026, 11, 15, 20, 12), LocalDateTime.of(2026, 11, 15, 21, 40));
        addLogin(5, LocalDateTime.of(2026, 11, 3, 9, 10), LocalDateTime.of(2026, 11, 3, 9, 25));
    }

    private static void addLogin(int userId, LocalDateTime loginAt, LocalDateTime logoutAt) {
        User user = findUserById(userId);
        DUMMY_LOGINS.add(new LoginRecord(userId, user.username(), user.fullName(), loginAt, logoutAt));
    }

    private DummyAdminData() {
        // static data only
    }

    /**
     * User has a short constructor, but it stamps createdAt with now(), which makes
     * every user look registered at the same moment. The admin screens sort and
     * filter by created date, so this goes through the full constructor instead.
     */
    private static User newUser(int userId, String username, String fullName, String email, String address,
                                LocalDate dateOfBirth, GenderType gender, AccountStatus status, UserRole role,
                                LocalDateTime createdAt) {
        return new User(userId, username, "", false, fullName, email, address, dateOfBirth, gender,
                status, role, null, null, createdAt, null, null);
    }

    public static List<User> getDummyUsers() {
        return Collections.unmodifiableList(DUMMY_USERS);
    }

    /**
     * Returns null when no user has that id.
     */
    public static User findUserById(int userId) {
        for (User user : DUMMY_USERS) {
            if (user.userId() == userId) {
                return user;
            }
        }
        return null;
    }

    /**
     * Every login of every user, newest first.
     */
    public static List<LoginRecord> getLoginHistory() {
        return Collections.unmodifiableList(DUMMY_LOGINS);
    }

    public static List<LoginRecord> getLoginHistoryOf(int userId) {
        List<LoginRecord> result = new ArrayList<>();
        for (LoginRecord login : DUMMY_LOGINS) {
            if (login.userId() == userId) {
                result.add(login);
            }
        }
        return result;
    }

    /**
     * Direct friends only. A pair counts for both sides.
     */
    public static List<User> getFriendsOf(int userId) {
        List<User> friends = new ArrayList<>();
        for (int[] pair : FRIEND_PAIRS) {
            if (pair[0] == userId) {
                friends.add(findUserById(pair[1]));
            } else if (pair[1] == userId) {
                friends.add(findUserById(pair[0]));
            }
        }
        return friends;
    }
}
