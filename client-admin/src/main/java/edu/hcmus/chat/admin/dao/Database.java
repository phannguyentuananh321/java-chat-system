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

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                AppConfig.get("db.url"),
                AppConfig.get("db.username"),
                AppConfig.get("db.password"));
    }
}
