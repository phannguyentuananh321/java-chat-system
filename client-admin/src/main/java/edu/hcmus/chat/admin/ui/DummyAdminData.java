package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.MemberRole;
import edu.hcmus.chat.common.model.ReportStatus;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserRole;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * A group conversation (conversation_type = GROUP), with the creator's username.
     */
    public record GroupRecord(
            int conversationId,
            String title,
            String createdByUsername,
            LocalDateTime createdAt
    ) {}

    /**
     * One row of conversation_members for a group, joined with the user's names.
     */
    public record GroupMemberRecord(
            int conversationId,
            int userId,
            String username,
            String fullName,
            MemberRole role,
            LocalDateTime joinedAt
    ) {}

    /**
     * One row of spam_reports, with both usernames filled in.
     */
    public record SpamReportRecord(
            int reportId,
            LocalDateTime createdAt,
            String reporterUsername,
            int reportedUserId,
            String reportedUsername,
            String reason,
            ReportStatus status
    ) {}

    /**
     * Friend counts of one user (admin requirement 7).
     * friendsWithFriendsOfFriends counts each person once, the user himself excluded.
     */
    public record FriendCountRecord(
            int userId,
            String username,
            String fullName,
            LocalDateTime createdAt,
            int directFriends,
            int friendsWithFriendsOfFriends
    ) {}

    /**
     * Activity of one user inside a time range (admin requirement 8).
     */
    public record UserActivityRecord(
            int userId,
            String username,
            String fullName,
            LocalDateTime createdAt,
            int appOpens,
            int chattedWithUsers,
            int chattedInGroups
    ) {}

    private static final List<User> DUMMY_USERS = new ArrayList<>();
    private static final List<LoginRecord> DUMMY_LOGINS = new ArrayList<>();
    private static final List<UserActivityRecord> DUMMY_ACTIVITY = new ArrayList<>();
    private static final List<GroupRecord> DUMMY_GROUPS = new ArrayList<>();
    private static final List<GroupMemberRecord> DUMMY_GROUP_MEMBERS = new ArrayList<>();
    private static final List<SpamReportRecord> DUMMY_SPAM_REPORTS = new ArrayList<>();

    /**
     * Accepted friendships as {userId, userId} pairs, the same shape as the friendships table.
     */
    private static final int[][] FRIEND_PAIRS = {
            {2, 3}, {2, 4}, {2, 6}, {3, 4}, {3, 5}, {3, 7},
            {4, 8}, {6, 9}, {7, 10}, {8, 11}, {9, 10}, {11, 12}
    };

    // Chart numbers, January to December. Hand-written so the charts are readable,
    // so they don't add up to the 12 users above. 2026 stops at November (today).
    private static final int[] YEARS = {2026, 2025, 2024};
    private static final int[][] MONTHLY_REGISTRATIONS = {
            {12, 16, 21, 19, 24, 28, 23, 26, 31, 27, 9, 0},
            {8, 11, 15, 13, 17, 20, 18, 16, 25, 22, 19, 14},
            {3, 5, 8, 6, 9, 12, 10, 7, 14, 11, 9, 6}
    };
    private static final int[][] MONTHLY_ACTIVE_USERS = {
            {165, 170, 182, 190, 205, 214, 208, 220, 245, 251, 236, 0},
            {75, 80, 92, 101, 110, 118, 115, 120, 140, 152, 160, 158},
            {10, 15, 22, 27, 33, 41, 46, 50, 62, 68, 72, 70}
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

        // Activity in the sample range: app opens, people chatted with, groups chatted in
        addActivity(2, 42, 6, 2);
        addActivity(3, 35, 5, 3);
        addActivity(4, 18, 3, 2);
        addActivity(5, 1, 0, 0);
        addActivity(6, 12, 3, 1);
        addActivity(7, 27, 4, 2);
        addActivity(8, 9, 2, 1);
        addActivity(9, 15, 3, 2);
        addActivity(10, 11, 2, 2);
        addActivity(11, 7, 2, 1);
        addActivity(12, 3, 9, 1);

        // Groups, then their members. The creator joins as group admin.
        addGroup(101, "Nhóm Đồ Án Java HCMUS", 2, LocalDateTime.of(2026, 10, 1, 9, 0));
        addGroupMember(101, 3, MemberRole.ADMIN, LocalDateTime.of(2026, 10, 1, 9, 0));
        addGroupMember(101, 4, MemberRole.MEMBER, LocalDateTime.of(2026, 10, 1, 9, 5));
        addGroupMember(101, 6, MemberRole.MEMBER, LocalDateTime.of(2026, 10, 2, 14, 20));

        addGroup(102, "Lớp 24KTPM2", 3, LocalDateTime.of(2025, 9, 5, 7, 30));
        addGroupMember(102, 2, MemberRole.MEMBER, LocalDateTime.of(2025, 9, 5, 7, 31));
        addGroupMember(102, 4, MemberRole.MEMBER, LocalDateTime.of(2025, 9, 5, 7, 31));
        addGroupMember(102, 7, MemberRole.MEMBER, LocalDateTime.of(2025, 9, 6, 10, 0));
        addGroupMember(102, 8, MemberRole.MEMBER, LocalDateTime.of(2025, 12, 1, 8, 0));
        addGroupMember(102, 9, MemberRole.MEMBER, LocalDateTime.of(2026, 2, 20, 19, 45));

        addGroup(103, "CLB Cầu lông", 6, LocalDateTime.of(2026, 3, 12, 18, 0));
        addGroupMember(103, 9, MemberRole.MEMBER, LocalDateTime.of(2026, 3, 12, 18, 1));
        addGroupMember(103, 10, MemberRole.MEMBER, LocalDateTime.of(2026, 5, 15, 17, 30));

        addGroup(104, "Ôn thi cuối kỳ", 7, LocalDateTime.of(2026, 11, 10, 20, 15));
        addGroupMember(104, 10, MemberRole.ADMIN, LocalDateTime.of(2026, 11, 10, 20, 15));
        addGroupMember(104, 3, MemberRole.MEMBER, LocalDateTime.of(2026, 11, 10, 20, 16));
        addGroupMember(104, 11, MemberRole.MEMBER, LocalDateTime.of(2026, 11, 11, 8, 40));

        // Spam reports, newest first
        addSpamReport(5, LocalDateTime.of(2026, 11, 18, 21, 15), 3, 12,
                "Gửi liên tục đường link quảng cáo", ReportStatus.OPEN);
        addSpamReport(4, LocalDateTime.of(2026, 11, 17, 9, 40), 2, 12,
                "Tin nhắn rác lặp lại nhiều lần trong nhóm", ReportStatus.OPEN);
        addSpamReport(3, LocalDateTime.of(2026, 11, 12, 14, 5), 7, 8,
                "Dùng ngôn từ không phù hợp", ReportStatus.REVIEWED);
        addSpamReport(2, LocalDateTime.of(2026, 11, 5, 20, 30), 4, 5,
                "Mạo danh người khác để mượn tiền", ReportStatus.REVIEWED);
        addSpamReport(1, LocalDateTime.of(2026, 10, 28, 8, 12), 9, 10,
                "Báo cáo nhầm", ReportStatus.DISMISSED);
    }

    private static void addSpamReport(int reportId, LocalDateTime createdAt, int reporterUserId, int reportedUserId,
                                      String reason, ReportStatus status) {
        DUMMY_SPAM_REPORTS.add(new SpamReportRecord(reportId, createdAt, findUserById(reporterUserId).username(),
                reportedUserId, findUserById(reportedUserId).username(), reason, status));
    }

    private static void addLogin(int userId, LocalDateTime loginAt, LocalDateTime logoutAt) {
        User user = findUserById(userId);
        DUMMY_LOGINS.add(new LoginRecord(userId, user.username(), user.fullName(), loginAt, logoutAt));
    }

    private static void addActivity(int userId, int appOpens, int chattedWithUsers, int chattedInGroups) {
        User user = findUserById(userId);
        DUMMY_ACTIVITY.add(new UserActivityRecord(userId, user.username(), user.fullName(), user.createdAt(),
                appOpens, chattedWithUsers, chattedInGroups));
    }

    private static void addGroup(int conversationId, String title, int createdByUserId, LocalDateTime createdAt) {
        User creator = findUserById(createdByUserId);
        DUMMY_GROUPS.add(new GroupRecord(conversationId, title, creator.username(), createdAt));
        addGroupMember(conversationId, createdByUserId, MemberRole.ADMIN, createdAt);
    }

    private static void addGroupMember(int conversationId, int userId, MemberRole role, LocalDateTime joinedAt) {
        User user = findUserById(userId);
        DUMMY_GROUP_MEMBERS.add(new GroupMemberRecord(conversationId, userId, user.username(), user.fullName(),
                role, joinedAt));
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

    public static List<GroupRecord> getGroups() {
        return Collections.unmodifiableList(DUMMY_GROUPS);
    }

    /**
     * Members of one group, group admins included (check the role to tell them apart).
     */
    public static List<GroupMemberRecord> getGroupMembers(int conversationId) {
        List<GroupMemberRecord> result = new ArrayList<>();
        for (GroupMemberRecord member : DUMMY_GROUP_MEMBERS) {
            if (member.conversationId() == conversationId) {
                result.add(member);
            }
        }
        return result;
    }

    public static List<SpamReportRecord> getSpamReports() {
        return Collections.unmodifiableList(DUMMY_SPAM_REPORTS);
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

    /**
     * Friend counts for every normal user (admins are left out).
     *
     * "Including friends of friends" is read as: everyone within two steps, each
     * counted once. Example: A-B, B-C, A-C gives A two people, not three.
     * The real version will be a SQL query; this loop is only for the sample data.
     */
    public static List<FriendCountRecord> getFriendCounts() {
        List<FriendCountRecord> result = new ArrayList<>();
        for (User user : DUMMY_USERS) {
            if (user.role() == UserRole.ADMIN) {
                continue;
            }
            List<User> friends = getFriendsOf(user.userId());

            Set<Integer> withinTwoSteps = new HashSet<>();
            for (User friend : friends) {
                withinTwoSteps.add(friend.userId());
                for (User friendOfFriend : getFriendsOf(friend.userId())) {
                    withinTwoSteps.add(friendOfFriend.userId());
                }
            }
            withinTwoSteps.remove(user.userId());

            result.add(new FriendCountRecord(user.userId(), user.username(), user.fullName(), user.createdAt(),
                    friends.size(), withinTwoSteps.size()));
        }
        return result;
    }

    public static List<UserActivityRecord> getUserActivity() {
        return Collections.unmodifiableList(DUMMY_ACTIVITY);
    }

    /**
     * Years that have chart data, newest first.
     */
    public static int[] getChartYears() {
        return YEARS.clone();
    }

    /**
     * New registrations per month of the given year (12 numbers, January first).
     */
    public static int[] getMonthlyRegistrations(int year) {
        return findYearRow(MONTHLY_REGISTRATIONS, year);
    }

    /**
     * Users who opened the app at least once, per month of the given year.
     */
    public static int[] getMonthlyActiveUsers(int year) {
        return findYearRow(MONTHLY_ACTIVE_USERS, year);
    }

    /**
     * Row i of the table belongs to YEARS[i]. Unknown year gives all zeros.
     */
    private static int[] findYearRow(int[][] table, int year) {
        for (int i = 0; i < YEARS.length; i++) {
            if (YEARS[i] == year) {
                return table[i].clone();
            }
        }
        return new int[12];
    }
}
