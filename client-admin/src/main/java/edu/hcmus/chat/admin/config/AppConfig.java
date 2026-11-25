package edu.hcmus.chat.admin.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Reads config.properties from the folder the app is started in
 * (next to admin.jar). Nothing about the database is hardcoded in the source.
 *
 * The file is read once, on the first get().
 */
public final class AppConfig {

    private static final String FILE_NAME = "config.properties";

    private static Properties properties;

    private AppConfig() {
        // static access only
    }

    /**
     * Returns the value for the key, trimmed. An empty value is allowed
     * (e.g. a local root user with no password), a missing key is not.
     */
    public static synchronized String get(String key) {
        if (properties == null) {
            properties = load();
        }
        String value = properties.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Thiếu khóa \"" + key + "\" trong file " + FILE_NAME);
        }
        return value.trim();
    }

    private static Properties load() {
        Path path = Path.of(FILE_NAME).toAbsolutePath();
        Properties loaded = new Properties();
        // Reader + UTF-8, because Properties.load(InputStream) assumes ISO-8859-1
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            loaded.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Không đọc được file cấu hình: " + path, e);
        }
        return loaded;
    }
}
