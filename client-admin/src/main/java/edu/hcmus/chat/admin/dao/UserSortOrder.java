package edu.hcmus.chat.admin.dao;

/**
 * How the user list is sorted (admin requirement 1a).
 * Each value maps to a fixed ORDER BY in UserDao, so sort text from the UI
 * never ends up inside the SQL string.
 */
public enum UserSortOrder {
    FULL_NAME,
    CREATED_NEWEST
}
