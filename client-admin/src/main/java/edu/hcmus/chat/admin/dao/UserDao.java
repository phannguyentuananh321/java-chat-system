package edu.hcmus.chat.admin.dao;

import edu.hcmus.chat.common.model.AccountStatus;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.model.UserRole;
import edu.hcmus.chat.common.model.UserSession;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for the users table and the two lists hanging off a user
 * (login sessions and friends). Only SQL lives here: no validation, no UI text.
 *
 * Every value from outside goes through PreparedStatement parameters.
 * Deleted users are soft-deleted (deleted_at is set), so every read filters
 * on deleted_at IS NULL.
 */
public class UserDao {

    // password_hash is left out on purpose: the admin screens never need to see it
    private static final String USER_COLUMNS =
            "user_id, username, must_change_password, full_name, email, address, date_of_birth, "
                    + "gender, account_status, role, created_at, updated_at";

    /**
     * Users matching the filter.
     *
     * @param keyword part of the username or full name; null or blank means no filter
     * @param status  only this status; null means both
     */
    public List<User> findUsers(String keyword, AccountStatus status, UserSortOrder sortOrder) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT " + USER_COLUMNS + " FROM users WHERE deleted_at IS NULL");
        List<Object> parameters = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (username LIKE ? OR full_name LIKE ?)");
            String pattern = "%" + escapeLike(keyword.trim()) + "%";
            parameters.add(pattern);
            parameters.add(pattern);
        }
        if (status != null) {
            sql.append(" AND account_status = ?");
            parameters.add(status.name());
        }

        // Fixed text per enum value; only the WHERE values are parameters
        if (sortOrder == UserSortOrder.CREATED_NEWEST) {
            sql.append(" ORDER BY created_at DESC, user_id DESC");
        } else {
            sql.append(" ORDER BY full_name ASC, username ASC");
        }

        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            List<User> users = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(readUser(resultSet));
                }
            }
            return users;
        }
    }

    /**
     * Returns null when the user does not exist or was deleted.
     */
    public User findById(int userId) throws SQLException {
        String sql = "SELECT " + USER_COLUMNS + " FROM users WHERE user_id = ? AND deleted_at IS NULL";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? readUser(resultSet) : null;
            }
        }
    }

    /**
     * Deleted users count too: username and email stay UNIQUE in the table.
     */
    public boolean existsByUsername(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * @param exceptUserId the user being edited (their own email is not a clash); 0 when adding
     */
    public boolean existsByEmail(String email, int exceptUserId) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ? AND user_id <> ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setInt(2, exceptUserId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Inserts a new normal user and returns the generated user_id.
     */
    public int insert(User user, String passwordHash) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, full_name, email, address, date_of_birth, gender, role) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.username());
            statement.setString(2, passwordHash);
            statement.setString(3, user.fullName());
            statement.setString(4, user.email());
            statement.setString(5, user.address());
            setDate(statement, 6, user.dateOfBirth());
            statement.setString(7, user.gender() == null ? null : user.gender().name());
            statement.setString(8, UserRole.USER.name());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }

    /**
     * Updates the profile fields. Username, role and status are not touched here.
     */
    public void updateProfile(User user) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, email = ?, address = ?, date_of_birth = ?, gender = ? "
                + "WHERE user_id = ? AND deleted_at IS NULL";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.fullName());
            statement.setString(2, user.email());
            statement.setString(3, user.address());
            setDate(statement, 4, user.dateOfBirth());
            statement.setString(5, user.gender() == null ? null : user.gender().name());
            statement.setInt(6, user.userId());
            statement.executeUpdate();
        }
    }

    public void updatePassword(int userId, String passwordHash) throws SQLException {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ? AND deleted_at IS NULL";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, passwordHash);
            statement.setInt(2, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Changes the account status. Locking also closes the user's open sessions,
     * so the two updates run in one transaction: either both happen or neither.
     */
    public void updateStatus(int userId, AccountStatus status) throws SQLException {
        String updateUser = "UPDATE users SET account_status = ? WHERE user_id = ? AND deleted_at IS NULL";

        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(updateUser)) {
                statement.setString(1, status.name());
                statement.setInt(2, userId);
                statement.executeUpdate();

                if (status == AccountStatus.LOCKED) {
                    closeOpenSessions(connection, userId);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    /**
     * Soft delete: marks the user deleted and closes their open sessions, in one transaction.
     * The row stays, so login history and old messages keep pointing at a real user.
     */
    public void softDelete(int userId) throws SQLException {
        String markDeleted = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP WHERE user_id = ? AND deleted_at IS NULL";

        try (Connection connection = Database.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(markDeleted)) {
                statement.setInt(1, userId);
                statement.executeUpdate();

                closeOpenSessions(connection, userId);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        }
    }

    /**
     * Part of a bigger transaction, so it uses the caller's connection.
     */
    private void closeOpenSessions(Connection connection, int userId) throws SQLException {
        String sql = "UPDATE user_sessions SET logout_at = CURRENT_TIMESTAMP WHERE user_id = ? AND logout_at IS NULL";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        }
    }

    /**
     * Login sessions of one user, newest first (admin requirement 1e).
     */
    public List<UserSession> findSessions(int userId) throws SQLException {
        String sql = "SELECT session_id, user_id, login_at, last_seen_at, logout_at FROM user_sessions "
                + "WHERE user_id = ? ORDER BY login_at DESC";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            List<UserSession> sessions = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(new UserSession(
                            resultSet.getLong("session_id"),
                            resultSet.getInt("user_id"),
                            resultSet.getObject("login_at", LocalDateTime.class),
                            resultSet.getObject("last_seen_at", LocalDateTime.class),
                            resultSet.getObject("logout_at", LocalDateTime.class)));
                }
            }
            return sessions;
        }
    }

    /**
     * Direct friends of one user (admin requirement 1f).
     * A friendship is stored once as (low id, high id), so the friend is
     * whichever side is not this user.
     */
    public List<User> findFriends(int userId) throws SQLException {
        String sql = "SELECT " + prefixed("u") + " FROM friendships f "
                + "JOIN users u ON u.user_id = IF(f.user_low_id = ?, f.user_high_id, f.user_low_id) "
                + "WHERE (f.user_low_id = ? OR f.user_high_id = ?) AND u.deleted_at IS NULL "
                + "ORDER BY u.full_name";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, userId);
            List<User> friends = new ArrayList<>();
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    friends.add(readUser(resultSet));
                }
            }
            return friends;
        }
    }

    private static String prefixed(String alias) {
        return alias + "." + USER_COLUMNS.replace(", ", ", " + alias + ".");
    }

    private User readUser(ResultSet resultSet) throws SQLException {
        String gender = resultSet.getString("gender");
        return new User(
                resultSet.getInt("user_id"),
                resultSet.getString("username"),
                "", // hash not loaded, see USER_COLUMNS
                resultSet.getBoolean("must_change_password"),
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("address"),
                resultSet.getObject("date_of_birth", LocalDate.class),
                gender == null ? null : GenderType.valueOf(gender),
                AccountStatus.valueOf(resultSet.getString("account_status")),
                UserRole.valueOf(resultSet.getString("role")),
                null,
                null,
                resultSet.getObject("created_at", LocalDateTime.class),
                resultSet.getObject("updated_at", LocalDateTime.class),
                null);
    }

    private static void setDate(PreparedStatement statement, int index, LocalDate date) throws SQLException {
        if (date == null) {
            statement.setNull(index, java.sql.Types.DATE);
        } else {
            statement.setDate(index, Date.valueOf(date));
        }
    }

    /**
     * % and _ are wildcards in LIKE; escape them so a search for "a_b" means exactly that.
     */
    private static String escapeLike(String text) {
        return text.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
