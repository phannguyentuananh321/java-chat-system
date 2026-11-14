# TÀI LIỆU QUYẾT ĐỊNH KIẾN TRÚC & QUY TẮC PHÁT TRIỂN (ADR)
**Dự án:** Hệ thống Chat (Môn Lập trình ứng dụng Java)  
**Tổ chức:** Đại học Khoa học Tự nhiên - ĐHQG-HCM (HCMUS)  
**Phiên bản:** 1.0.0-SNAPSHOT  

---

## 1. Các ràng buộc nghiêm ngặt từ Giảng viên (Vi phạm là 0 điểm)

| STT | Ràng buộc | Chi tiết cam kết thực hiện |
| :---: | :--- | :--- |
| **1** | **Toàn bộ bằng Java** | Không sử dụng hay tạo bất kỳ file nào thuộc hệ sinh thái Node.js, Python, JavaScript, TypeScript... Chỉ chấp nhận mã nguồn `.java`, kịch bản CSDL `.sql`, và file cấu hình xây dựng Maven (`pom.xml`, `*.properties`). |
| **2** | **Giao diện Swing thuần** | Toàn bộ ứng dụng client là Desktop Java Swing (`javax.swing.*`), tuyệt đối không dùng Web/HTML/WebView hoặc JavaFX lai web. |
| **3** | **TCP Socket thuần** | Giao tiếp mạng giữa Client và Server sử dụng trực tiếp `java.net.Socket` và `java.net.ServerSocket` (TCP Stream thuần). Nghiêm cấm sử dụng WebSocket, STOMP, RESTful API/HTTP làm kênh giao tiếp chính. |
| **4** | **Phân định Backend** | Backend viết bằng Java. Khi mở rộng sang Phiên bản 2 sẽ dùng Spring Boot CHỈ cho server dạng không web (`spring.main.web-application-type=none`). **Tuyệt đối KHÔNG tạo module server ở giai đoạn hiện tại (Phiên bản 1 - Dựng giao diện).** |
| **5** | **Client độc lập** | Cả hai phân hệ client (`client-admin` và `client-user`) là ứng dụng Java Swing thuần, chạy độc lập, không nhúng Spring Boot. |

---

## 2. Quyết định công nghệ (Technology Decisions)

- **Ngôn ngữ & Nền tảng:** Java 17 LTS (mã nguồn mã hóa chuẩn UTF-8 để hỗ trợ tiếng Việt có dấu).
- **Cơ sở dữ liệu:** MySQL 8.0+ (utf8mb4_unicode_ci), schema phiên bản v2 chuẩn hóa (11 bảng + 1 VIEW hỗ trợ bạn của bạn, kiểm tra toàn vẹn CHECK constraints, FULLTEXT index tìm kiếm tin nhắn).
- **Công cụ quản lý dự án & Build:** Apache Maven đa module (Multi-Module Maven Project).
- **Thư viện giao diện:** `com.formdev:flatlaf` (phiên bản 3.5.4) mang lại giao diện hiện đại, sạch sẽ và nhất quán. Có lớp `Theme.java` tự động dự phòng sang `SystemLookAndFeel` nếu môi trường không tương thích.
- **Thư viện kiểm thử:** JUnit 5 (Jupiter BOM `5.10.2`).
- **Cơ chế Layout:** Sử dụng chuẩn `BorderLayout`, `FlowLayout`, `GridBagLayout`, `BoxLayout`. Nghiêm cấm `setLayout(null)` để đảm bảo tương thích mọi kích thước màn hình và độ phân giải hiển thị.
- **Luồng xử lý Swing:** Mọi thao tác dựng hình hoặc cập nhật UI đều phải thực thi qua `SwingUtilities.invokeLater()`.

---

## 3. Cấu trúc Module và Phân tách trách nhiệm

Dự án được phân tách thành 3 module Maven độc lập:

1. **`common`**:
   - Chứa lớp `Theme.java` (quản lý bảng màu, font chữ, tiện ích tạo nút, đường viền, Look and Feel).
   - Nơi chứa các DTO/Model dùng chung, Protocol Message cho giao tiếp Socket (bổ sung sau).
   - Không chứa giao diện nghiệp vụ riêng biệt của Admin hay User.

2. **`client-admin`**:
   - Phân hệ máy khách dành cho Quản trị viên hệ thống.
   - Phụ thuộc vào `common`.
   - Chạy độc lập qua `AdminMain.java` hoặc Fat JAR `admin.jar`.
   - Chứa màn hình quản trị của Thành viên A (`UserManagementPanel`).

3. **`client-user`**:
   - Phân hệ máy khách dành cho Người dùng thông thường (Chat Client).
   - Phụ thuộc vào `common`.
   - Chạy độc lập qua `UserMain.java` hoặc Fat JAR `user.jar`.
   - Chứa màn hình chat của Thành viên B (`ChatMainPanel`).

---

## 4. Bảng phân công quyền sở hữu mã nguồn (Code Ownership)

Để tránh xung đột mã nguồn (Merge Conflict) khi 2 sinh viên làm việc song song trên cùng một Git Repository:

| Khu vực / Module / File | Người sở hữu chính | Trách nhiệm & Quyền hạn |
| :--- | :--- | :--- |
| `client-admin/**` | **Thành viên A (Quản trị viên)** | Toàn quyền thiết kế giao diện quản trị, dữ liệu giả `DummyAdminData`, demo, và `AdminMain`. |
| `client-user/**` | **Thành viên B (Người dùng)** | Toàn quyền thiết kế giao diện chat, dữ liệu giả `DummyUserData`, demo, và `UserMain`. |
| `pom.xml` gốc (parent) | **Đồng sở hữu (Thảo luận trước)** | Cả hai phải thảo luận và thống nhất trước khi bổ sung dependency hay plugin mới. |
| `common/**` | **Đồng sở hữu (Thảo luận trước)** | Nơi chia sẻ tài nguyên chung. Mọi chỉnh sửa cần tạo Pull Request và được thành viên kia phê duyệt. |
| `docs/**`, `.gitignore` | **Đồng sở hữu** | Giữ tài liệu đồng bộ và quy chuẩn chung cho cả nhóm. |
