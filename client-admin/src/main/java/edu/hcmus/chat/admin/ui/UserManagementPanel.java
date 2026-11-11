package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Màn hình: Quản lý người dùng (Phân hệ Quản trị viên).
 * Kế thừa JPanel theo chuẩn kiến trúc module hoá giao diện.
 *
 * Người phụ trách: Thành viên A (Quản trị viên)
 */
public class UserManagementPanel extends JPanel {

    public UserManagementPanel() {
        initComponents();
    }

    private void initComponents() {
        // Tuân thủ quy tắc: Sử dụng layout manager tiêu chuẩn, KHÔNG dùng null layout
        setLayout(new BorderLayout(15, 15));
        setBackground(Theme.BG_MAIN);
        setBorder(Theme.createPadding(20, 20, 20, 20));

        // 1. Vùng tiêu đề phía trên (NORTH)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel titleLabel = Theme.createHeaderLabel("QUẢN LÝ NGƯỜI DÙNG");
        JLabel subtitleLabel = new JLabel("Danh sách tài khoản, trạng thái đăng nhập và phân quyền hệ thống");
        subtitleLabel.setFont(Theme.FONT_REGULAR);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // 2. Vùng trung tâm (CENTER) - Khung bố cục tối thiểu chạy được
        // TODO(Thành viên A): Thiết kế bảng dữ liệu người dùng (JTable), thanh tìm kiếm, bộ lọc theo trạng thái
        // và khu vực form xem chi tiết/chỉnh sửa thông tin tài khoản.
        // Gợi ý sử dụng DummyAdminData.getDummyUsers() để nạp dữ liệu mẫu ban đầu.
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setBackground(Theme.BG_SURFACE);
        centerContainer.setBorder(Theme.createCardBorder(24));

        JLabel placeholderLabel = new JLabel(
                "<html><center><h3>[KHU VỰC NỘI DUNG QUẢN LÝ NGƯỜI DÙNG]</h3>"
                        + "<p>Thành viên A hiện thực: Bảng danh sách người dùng, tìm kiếm, khóa/mở tài khoản tại đây.</p></center></html>",
                SwingConstants.CENTER
        );
        placeholderLabel.setFont(Theme.FONT_REGULAR);
        placeholderLabel.setForeground(Theme.TEXT_MUTED);
        centerContainer.add(placeholderLabel, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // 3. Vùng thanh thao tác phía dưới (SOUTH)
        // TODO(Thành viên A): Thêm các nút thao tác như Thêm mới, Khóa tài khoản, Mở khóa, Đặt lại mật khẩu...
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        JButton refreshButton = Theme.createSecondaryButton("Làm mới");
        JButton addUserButton = Theme.createPrimaryButton("+ Thêm người dùng");

        actionPanel.add(refreshButton);
        actionPanel.add(addUserButton);

        add(actionPanel, BorderLayout.SOUTH);
    }
}
