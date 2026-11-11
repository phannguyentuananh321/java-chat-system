package edu.hcmus.chat.common.ui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Quản lý định dạng giao diện chuẩn của toàn bộ hệ thống chat.
 * Cung cấp hằng số màu sắc, font chữ chuẩn, phương thức kích hoạt Look and Feel,
 * và các tiện ích tạo giao diện nhất quán cho cả phân hệ Quản trị và Người dùng.
 */
public final class Theme {

    // --- BẢNG MÀU CHUẨN (COLOR PALETTE) ---
    public static final Color PRIMARY_COLOR = new Color(24, 119, 242);       // Xanh dương chủ đạo
    public static final Color PRIMARY_HOVER = new Color(13, 100, 210);       // Hover nút chính
    public static final Color PRIMARY_TEXT = new Color(255, 255, 255);       // Chữ trên nút chính
    public static final Color SECONDARY_COLOR = new Color(228, 230, 235);    // Xám nhạt nút phụ
    public static final Color SECONDARY_HOVER = new Color(216, 218, 223);    // Hover nút phụ
    public static final Color TEXT_DARK = new Color(28, 30, 33);             // Chữ đen đậm
    public static final Color TEXT_MUTED = new Color(101, 103, 107);         // Chữ ghi phụ
    public static final Color BG_MAIN = new Color(240, 242, 245);            // Nền tổng thể
    public static final Color BG_SURFACE = new Color(255, 255, 255);         // Nền panel/card trắng
    public static final Color BORDER_COLOR = new Color(204, 208, 213);       // Màu đường viền mảnh
    public static final Color SUCCESS_COLOR = new Color(46, 125, 50);        // Xanh lá thành công/online
    public static final Color DANGER_COLOR = new Color(211, 47, 47);          // Đỏ cảnh báo/xóa

    // --- FONT CHỮ CHUẨN (FONTS) ---
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    private Theme() {
        // Lớp tiện ích không khởi tạo
    }

    /**
     * Cài đặt Look and Feel (ưu tiên FlatLaf, tự động dự phòng sang System L&F nếu có lỗi).
     */
    public static void install() {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 8);
            UIManager.put("ScrollBar.width", 10);
        } catch (Exception ex) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // Giữ nguyên giao diện mặc định của Swing nếu có lỗi
            }
        }
    }

    /**
     * Tạo nút hành động chính (Primary button: nền xanh, chữ trắng).
     */
    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_BOLD);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(PRIMARY_TEXT);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Tạo nút hành động phụ (Secondary button: nền xám nhạt, chữ đen).
     */
    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FONT_REGULAR);
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(TEXT_DARK);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Tạo khoảng đệm trống (padding).
     */
    public static Border createPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    /**
     * Tạo đường viền card có viền xám mảnh và khoảng đệm bên trong.
     */
    public static Border createCardBorder(int padding) {
        return new CompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true),
                new EmptyBorder(padding, padding, padding, padding)
        );
    }

    /**
     * Tạo tiêu đề khu vực chuẩn.
     */
    public static JLabel createHeaderLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_DARK);
        return label;
    }
}
