# DANH MỤC MÀN HÌNH GIAO DIỆN (UI SCREENS CATALOG)
**Môn học:** Lập trình ứng dụng Java  
**Đồ án:** Hệ thống Chat (Chat System)  

---

## Bảng theo dõi tiến độ màn hình Swing

| STT | Tên màn hình | Phân hệ (Module) | Lớp giao diện (JPanel) | Lớp Demo kiểm thử | Người phụ trách | Trạng thái | Ghi chú |
| :---: | :--- | :--- | :--- | :--- | :--- | :---: | :--- |
| 1 | **Quản lý người dùng** | `client-admin` | `edu.hcmus.chat.admin.ui.UserManagementPanel` | `edu.hcmus.chat.admin.ui.UserManagementPanelDemo` | **Thành viên A** (Quản trị) | Đã dựng khung (Scaffolded) | Khung bố cục chuẩn, tích hợp sẵn dữ liệu mẫu trong `DummyAdminData` |
| 2 | **Khung chat chính** | `client-user` | `edu.hcmus.chat.user.ui.ChatMainPanel` | `edu.hcmus.chat.user.ui.ChatMainPanelDemo` | **Thành viên B** (Người dùng) | Đã dựng khung (Scaffolded) | Khung bố cục chuẩn, tích hợp sẵn dữ liệu mẫu trong `DummyUserData` |

---

## Tiêu chuẩn kỹ thuật cho mỗi màn hình khi hoàn thiện
1. Kế thừa trực tiếp `javax.swing.JPanel`.
2. Bố cục bằng các `LayoutManager` chuẩn (BorderLayout, GridBagLayout, BoxLayout, FlowLayout), không dùng `setLayout(null)`.
3. Có lớp `...Demo.java` riêng biệt với kích thước cửa sổ chuẩn `1000 x 650`, căn giữa màn hình (`setLocationRelativeTo(null)`), đóng ứng dụng khi tắt (`EXIT_ON_CLOSE`), và cài đặt `Theme.install()` trước khi hiển thị.
4. Chỉ lấy dữ liệu hiển thị từ `DummyXxxData`, tuyệt đối không viết code kết nối mạng hoặc truy vấn CSDL trong lớp giao diện.
5. Hình ảnh và icon phải được nạp tương đối từ classpath thông qua `getClass().getResource(...)` trong thư mục `src/main/resources/images/`.
