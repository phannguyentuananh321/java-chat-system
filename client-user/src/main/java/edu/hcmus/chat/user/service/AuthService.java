package edu.hcmus.chat.user.service;

import edu.hcmus.chat.common.model.GenderType;

import java.time.LocalDate;

/**
 * Hợp đồng dịch vụ xác thực tài khoản dành riêng cho Phân hệ Người dùng (Thành viên B).
 */
public interface AuthService {

    boolean login(String username, String password);

    boolean register(String username, String password, String fullName, String email, String address, LocalDate dateOfBirth, GenderType gender);

    boolean resetPassword(String email);
}
