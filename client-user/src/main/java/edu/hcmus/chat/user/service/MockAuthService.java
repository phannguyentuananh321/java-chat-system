package edu.hcmus.chat.user.service;

import edu.hcmus.chat.common.model.GenderType;

import java.time.LocalDate;

/**
 * Hiện thực AuthService dùng dữ liệu mẫu cho Phiên bản 1.
 */
public class MockAuthService implements AuthService {

    @Override
    public boolean login(String username, String password) {
        // Cho phép đăng nhập thử nghiệm thành công
        return username != null && !username.trim().isEmpty() && password != null && !password.isEmpty();
    }

    @Override
    public boolean register(String username, String password, String fullName, String email, String address, LocalDate dateOfBirth, GenderType gender) {
        return true;
    }

    @Override
    public boolean resetPassword(String email) {
        return email != null && email.contains("@");
    }
}
