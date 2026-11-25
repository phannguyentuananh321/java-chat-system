package edu.hcmus.chat.admin.service;

import edu.hcmus.chat.admin.dao.UserDao;
import edu.hcmus.chat.admin.dao.UserSortOrder;
import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserRole;
import edu.hcmus.chat.common.model.UserSession;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Business rules for admin requirement 1 (user management).
 * Sits between the screens and UserDao: checks input, applies the rules,
 * hashes passwords, and turns database errors into readable messages.
 */
public class UserService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("[a-zA-Z0-9_]{3,30}");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    private static final int MIN_PASSWORD_LENGTH = 6;

    // uuuu + STRICT so dates like 31/02/2004 are rejected instead of being rolled over
    private static final DateTimeFormatter DATE_INPUT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private final UserDao userDao;

    public UserService() {
        this(new UserDao());
    }

    /**
     * The DAO is passed in so a test can hand over a fake one.
     */
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * @param status null means both active and locked
     */
    public List<User> getUsers(String keyword, AccountStatus status, UserSortOrder sortOrder) throws ServiceException {
        try {
            return userDao.findUsers(keyword, status, sortOrder);
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    /**
     * Adds a normal user (role USER). Returns the new user's id.
     */
    public int addUser(String username, String password, String fullName, String email, String address,
                       String dateOfBirthText, GenderType gender) throws ServiceException {
        username = trim(username);
        checkUsername(username);
        checkPassword(password);
        User user = buildProfile(0, username, fullName, email, address, dateOfBirthText, gender);

        try {
            if (userDao.existsByUsername(username)) {
                throw new ServiceException("Tên đăng nhập \"" + username + "\" đã được sử dụng.");
            }
            if (userDao.existsByEmail(user.email(), 0)) {
                throw new ServiceException("Email \"" + user.email() + "\" đã được sử dụng.");
            }
            return userDao.insert(user, hashPassword(password));
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    /**
     * Updates name, email, address, birth date and gender. The username can't change.
     */
    public void updateUser(int userId, String fullName, String email, String address,
                           String dateOfBirthText, GenderType gender) throws ServiceException {
        User existing = getExistingUser(userId);
        User user = buildProfile(userId, existing.username(), fullName, email, address, dateOfBirthText, gender);

        try {
            if (userDao.existsByEmail(user.email(), userId)) {
                throw new ServiceException("Email \"" + user.email() + "\" đã được sử dụng.");
            }
            userDao.updateProfile(user);
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    /**
     * Soft-deletes the user. Admin accounts are protected, otherwise the
     * system could end up with nobody able to manage it.
     */
    public void deleteUser(int userId) throws ServiceException {
        User user = getExistingUser(userId);
        if (user.role() == UserRole.ADMIN) {
            throw new ServiceException("Không thể xóa tài khoản quản trị viên.");
        }
        try {
            userDao.softDelete(userId);
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    /**
     * Locks or unlocks an account. Locking also ends the user's open sessions.
     */
    public void setLocked(int userId, boolean locked) throws ServiceException {
        User user = getExistingUser(userId);
        if (locked && user.role() == UserRole.ADMIN) {
            throw new ServiceException("Không thể khóa tài khoản quản trị viên.");
        }
        try {
            userDao.updateStatus(userId, locked ? AccountStatus.LOCKED : AccountStatus.ACTIVE);
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    /**
     * Admin sets a new password (requirement 1d). Both fields must match.
     */
    public void changePassword(int userId, String newPassword, String confirmPassword) throws ServiceException {
        checkPassword(newPassword);
        if (!newPassword.equals(confirmPassword)) {
            throw new ServiceException("Mật khẩu nhập lại không khớp.");
        }
        getExistingUser(userId);
        try {
            userDao.updatePassword(userId, hashPassword(newPassword));
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    public List<UserSession> getLoginHistory(int userId) throws ServiceException {
        try {
            return userDao.findSessions(userId);
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    public List<User> getFriends(int userId) throws ServiceException {
        try {
            return userDao.findFriends(userId);
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    // --- checks ---------------------------------------------------------------

    private User getExistingUser(int userId) throws ServiceException {
        try {
            User user = userDao.findById(userId);
            if (user == null) {
                throw new ServiceException("Người dùng không tồn tại hoặc đã bị xóa.");
            }
            return user;
        } catch (SQLException e) {
            throw databaseError(e);
        }
    }

    /**
     * Checks the profile fields shared by add and edit, and builds a User from them.
     */
    private User buildProfile(int userId, String username, String fullName, String email, String address,
                              String dateOfBirthText, GenderType gender) throws ServiceException {
        fullName = trim(fullName);
        email = trim(email);
        address = trim(address);

        if (fullName.isEmpty()) {
            throw new ServiceException("Vui lòng nhập họ và tên.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ServiceException("Email không hợp lệ.");
        }
        LocalDate dateOfBirth = parseDate(dateOfBirthText);
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now())) {
            throw new ServiceException("Ngày sinh không được ở tương lai.");
        }

        return new User(userId, username, fullName, email, address.isEmpty() ? null : address,
                dateOfBirth, gender, AccountStatus.ACTIVE, UserRole.USER);
    }

    private void checkUsername(String username) throws ServiceException {
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            throw new ServiceException("Tên đăng nhập gồm 3-30 ký tự: chữ cái không dấu, số hoặc dấu gạch dưới.");
        }
    }

    private void checkPassword(String password) throws ServiceException {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new ServiceException("Mật khẩu phải có ít nhất " + MIN_PASSWORD_LENGTH + " ký tự.");
        }
    }

    /**
     * Empty text means "no birth date" (the column is nullable).
     */
    private LocalDate parseDate(String text) throws ServiceException {
        text = trim(text);
        if (text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text, DATE_INPUT);
        } catch (DateTimeParseException e) {
            throw new ServiceException("Ngày sinh phải có dạng dd/MM/yyyy, ví dụ 15/05/2004.");
        }
    }

    private static String trim(String text) {
        return text == null ? "" : text.trim();
    }

    /**
     * SHA-256 in hex, the same format as script/seed_data.sql, so seeded and
     * admin-created passwords work the same way at login. No salt yet: BCrypt
     * would be stronger but has to be agreed with the login side first.
     */
    static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // every JVM must ship SHA-256, so this can't really happen
            throw new IllegalStateException(e);
        }
    }

    private static ServiceException databaseError(SQLException e) {
        return new ServiceException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
    }
}
