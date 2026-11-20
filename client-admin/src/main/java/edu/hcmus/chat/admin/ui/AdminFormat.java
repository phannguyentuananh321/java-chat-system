package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.MemberRole;
import edu.hcmus.chat.common.model.ReportStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Turns model values (dates, enums) into the Vietnamese text shown in tables.
 * Kept in one place so every screen shows the same wording.
 */
public final class AdminFormat {

    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private AdminFormat() {
        // static helpers only
    }

    public static String date(LocalDate date) {
        return date == null ? "" : date.format(DATE);
    }

    public static String date(LocalDateTime time) {
        return time == null ? "" : time.format(DATE);
    }

    /**
     * A missing time (e.g. logout of a session that is still open) shows as a dash.
     */
    public static String dateTime(LocalDateTime time) {
        return time == null ? "—" : time.format(DATE_TIME);
    }

    public static String gender(GenderType gender) {
        return switch (gender) {
            case MALE -> "Nam";
            case FEMALE -> "Nữ";
            case OTHER -> "Khác";
        };
    }

    public static String accountStatus(AccountStatus status) {
        return switch (status) {
            case ACTIVE -> "Hoạt động";
            case LOCKED -> "Bị khóa";
        };
    }

    public static String reportStatus(ReportStatus status) {
        return switch (status) {
            case OPEN -> "Chưa xử lý";
            case REVIEWED -> "Đã xử lý";
            case DISMISSED -> "Đã bỏ qua";
        };
    }

    public static String memberRole(MemberRole role) {
        return switch (role) {
            case MEMBER -> "Thành viên";
            case ADMIN -> "Quản trị nhóm";
        };
    }
}
