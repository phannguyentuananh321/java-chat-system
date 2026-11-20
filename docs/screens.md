# DANH MỤC MÀN HÌNH GIAO DIỆN (UI SCREENS CATALOG)
**Môn học:** Lập trình ứng dụng Java  
**Đồ án:** Hệ thống Chat (Chat System)  

---

## Bảng theo dõi tiến độ màn hình Swing

| STT | Tên màn hình | Phân hệ (Module) | Lớp giao diện (JPanel/JFrame) | Lớp Demo kiểm thử | Người phụ trách | Trạng thái | Ghi chú |
| :---: | :--- | :--- | :--- | :--- | :--- | :---: | :--- |
| 1 | **Khung điều hướng Quản trị (Shell)** | `client-admin` | `edu.hcmus.chat.admin.ui.AdminShellPanel` | `edu.hcmus.chat.admin.AdminMain` | **Thành viên A** (Quản trị) | **Hoàn thành UI (100%)** | Sidebar chuyển đổi 5 tab chức năng, header thương hiệu |
| 2 | **Quản lý người dùng** | `client-admin` | `edu.hcmus.chat.admin.ui.UserManagementPanel` | `edu.hcmus.chat.admin.ui.UserManagementPanelDemo` | **Thành viên A** (Quản trị) | **Hoàn thành UI (100%)** | Bảng danh sách, lọc, sắp xếp, Thêm/Sửa/Xóa, Khóa nick, Đổi MK |
| 3 | **Lịch sử đăng nhập** | `client-admin` | `edu.hcmus.chat.admin.ui.LoginHistoryPanel` | `edu.hcmus.chat.admin.ui.LoginHistoryPanelDemo` | **Thành viên A** (Quản trị) | **Hoàn thành UI (100%)** | Danh sách đăng nhập theo thời gian, lọc ngày & tên đăng nhập |
| 4 | **Quản lý nhóm chat** | `client-admin` | `edu.hcmus.chat.admin.ui.GroupManagementPanel` | `edu.hcmus.chat.admin.ui.GroupManagementPanelDemo` | **Thành viên A** (Quản trị) | **Hoàn thành UI (100%)** | Danh sách nhóm, lọc/sắp xếp, xem danh sách thành viên & admin |
| 5 | **Báo cáo vi phạm Spam** | `client-admin` | `edu.hcmus.chat.admin.ui.SpamReportPanel` | `edu.hcmus.chat.admin.ui.SpamReportPanelDemo` | **Thành viên A** (Quản trị) | **Hoàn thành UI (100%)** | Danh sách báo cáo, lọc theo thời gian/tên, xử lý khóa nick/bỏ qua |
| 6 | **Thống kê & Biểu đồ năm** | `client-admin` | `edu.hcmus.chat.admin.ui.StatisticsPanel` | `edu.hcmus.chat.admin.ui.StatisticsPanelDemo` | **Thành viên A** (Quản trị) | **Hoàn thành UI (100%)** | 5 tab: Đăng ký mới, Bạn bè (trực tiếp & FOF), Hoạt động, 2 biểu đồ cột |
| 7 | **Xác thực (Đăng nhập / Đăng ký / Quên MK)** | `client-user` | `edu.hcmus.chat.user.auth.AuthFrame` | `edu.hcmus.chat.user.auth.AuthFrameDemo` | **Thành viên B** (Người dùng) | **Hoàn thành UI (100%)** | CardLayout 3 view: Đăng nhập, Đăng ký, Quên mật khẩu ngẫu nhiên |
| 8 | **Khung chat chính** | `client-user` | `edu.hcmus.chat.user.ui.ChatMainPanel` | `edu.hcmus.chat.user.ui.ChatMainPanelDemo` | **Thành viên B** (Người dùng) | **Hoàn thành UI (100%)** | 100% Java Swing thuần, bong bóng chat, chọn liên hệ, gửi tin |
| 9 | **Quản lý bạn bè & Lời mời** | `client-user` | `edu.hcmus.chat.user.ui.FriendManagementPanel` | `edu.hcmus.chat.user.ui.FriendManagementPanelDemo` | **Thành viên B** (Người dùng) | **Hoàn thành UI (100%)** | Tab Bạn bè (Online/Offline, Block) & Tab Lời mời (Đồng ý/Từ chối) |
| 10 | **Tạo nhóm trò chuyện** | `client-user` | `edu.hcmus.chat.user.ui.CreateGroupDialog` | `edu.hcmus.chat.user.ui.CreateGroupDialogDemo` | **Thành viên B** (Người dùng) | **Hoàn thành UI (100%)** | Dialog chọn tối thiểu 1 bạn bè có checkbox tick chọn |

---

## Tiêu chuẩn kỹ thuật cho mỗi màn hình khi hoàn thiện
1. Kế thừa trực tiếp `javax.swing.JPanel`.
2. Bố cục bằng các `LayoutManager` chuẩn (BorderLayout, GridBagLayout, BoxLayout, FlowLayout), không dùng `setLayout(null)`.
3. Có lớp `...Demo.java` riêng biệt với kích thước cửa sổ chuẩn `1000 x 650`, căn giữa màn hình (`setLocationRelativeTo(null)`), đóng ứng dụng khi tắt (`EXIT_ON_CLOSE`), và cài đặt `Theme.install()` trước khi hiển thị.
4. Chỉ lấy dữ liệu hiển thị từ `DummyXxxData`, tuyệt đối không viết code kết nối mạng hoặc truy vấn CSDL trong lớp giao diện.
5. Hình ảnh và icon phải được nạp tương đối từ classpath thông qua `getClass().getResource(...)` trong thư mục `src/main/resources/images/`.
