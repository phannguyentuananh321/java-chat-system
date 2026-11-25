package edu.hcmus.chat.admin.dao;

import edu.hcmus.chat.admin.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Opens JDBC connections using the settings in config.properties.
 * Every DAO call opens its own connection and closes it with try-with-resources;
 * no pool, the admin app only makes a few queries at a time.
 */
public final class Database {

    static {
        // JDBC 4 drivers register themselves, loading it by name just makes it explicit
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Không tìm thấy MySQL JDBC driver", e);
        }
    }

    private Database() {
        // static access only
    }

    /**
     * A missing or broken config.properties is reported as an SQLException too,
     * so callers only have one kind of "can't reach the database" error to handle.
     */
    public static Connection getConnection() throws SQLException {
        String url;
        String username;
        String password;
        try {
            url = AppConfig.get("db.url");
            username = AppConfig.get("db.username");
            password = AppConfig.get("db.password");
        } catch (IllegalStateException e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url, username, password);
    }
}
