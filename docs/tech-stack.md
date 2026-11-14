# TÀI LIỆU CÔNG NGHỆ (TECH STACK SPECIFICATION)
**Đồ án:** Hệ thống Chat (Chat System)  
**Môn học:** Lập trình ứng dụng Java - BM. Công nghệ phần mềm, Khoa CNTT, Trường ĐH KHTN (HCMUS)  
**Phiên bản:** 1.0.0 (Áp dụng cho Phiên bản 1 & Chuẩn bị Phiên bản 2)

---

## 1. Tổng quan kiến trúc công nghệ

Hệ thống được xây dựng theo mô hình **Client – Server tập trung** qua kết nối socket TCP hai chiều, với giao diện máy khách desktop bằng **Java Swing thuần** và quản lý mã nguồn đa phân hệ bằng **Maven Multi-Module**.

```
                           +--------------------------+
                           |       chat-system        |
                           |       (Parent POM)       |
                           +------------+-------------+
                                        |
         +------------------------------+------------------------------+
         |                              |                              |
         v                              v                              v
+-----------------+            +-----------------+            +-----------------+
|     common      |            |  client-admin   |            |   client-user   |
| (Theme, Models, |            | (Swing Desktop  |            | (Swing Desktop  |
|  Protocol DTO)  |            |   Quản trị)     |            |   Người dùng)   |
+--------+--------+            +--------+--------+            +--------+--------+
         ^                              |                              |
         |                              v                              v
         |                     +-----------------+            +-----------------+
         +---------------------+    admin.jar    |            |    user.jar     |
                               +-----------------+            +-----------------+
                                        |                              |
                                        | [TCP Socket java.net.Socket] |
                                        +--------------+---------------+
                                                       |
                                                       v
                                            +---------------------+
                                            |     server.jar      |
                                            | (Spring Boot Server |
                                            |  Non-web, TCP Only) |
                                            +----------+----------+
                                                       |
                                                       v [JDBC / JPA]
                                            +---------------------+
                                            |      MySQL 8.0+     |
                                            |  (Database Schema)  |
                                            +---------------------+
```

---

## 2. Chi tiết các công nghệ sử dụng (Tech Stack Detail)

### 2.1. Nền tảng cốt lõi (Core Runtime & Language)
* **Ngôn ngữ:** Java 17 LTS (Đảm bảo tính tương thích lâu dài, hỗ trợ Records, Text Blocks, Pattern Matching, Switch Expressions).
* **Mã hóa ký tự:** `UTF-8` toàn diện (source code, compiler, JVM arguments và database) để đảm bảo hỗ trợ tiếng Việt có dấu và biểu tượng cảm xúc (Emoji).
* **Môi trường thực thi:** OpenJDK 17+ (Temurin / Adoptium / Oracle JDK).

### 2.2. Công cụ xây dựng dự án & Quản lý gói (Build Tool)
* **Apache Maven 3.9+ (Multi-Module):**
  * `chat-system`: POM gốc quản lý tập trung phiên bản dependencies (`dependencyManagement`) và plugins (`pluginManagement`).
  * `common`: Module thư viện nội bộ chứa Theme, Enums, Models và giao thức truyền tin.
  * `client-admin`: Phân hệ quản trị viên (Thành viên A).
  * `client-user`: Phân hệ người dùng trò chuyện (Thành viên B).
  * `server`: *(Bổ sung ở Phiên bản 2)* Phân hệ máy chủ dịch vụ.
* **Maven Plugins chính:**
  * `maven-compiler-plugin (3.13.0)`: Biên dịch chuẩn Java 17 (`<release>17</release>`).
  * `maven-shade-plugin (3.5.2)`: Đóng gói thành Fat JAR (Uber JAR) độc lập chứa toàn bộ thư viện phụ thuộc (`admin.jar`, `user.jar`).
  * `exec-maven-plugin (3.2.0)`: Cho phép chạy trực tiếp các màn hình Demo hoặc hàm `main` ngay trong dòng lệnh.

### 2.3. Giao diện người dùng (UI / UX Desktop)
* **Framework:** **Java Swing thuần** (`javax.swing.*`, `java.awt.*`) – Tuân thủ nghiêm ngặt ràng buộc của môn học (không dùng Web, WebView, HTML).
* **Look and Feel (Giao diện hiện đại):** `com.formdev:flatlaf` (v3.5.4) – Giao diện phẳng chuẩn quốc tế, hỗ trợ bo góc, scrollbar hiện đại, tự động tương thích DPI cao.
* **Định vị & Quản lý bố cục (Layout Managers):**
  * `BorderLayout`: Bố cục chia khung chính (Header, Content, Action Bar).
  * `CardLayout`: Quản lý và chuyển đổi mượt mà giữa các màn hình/panel mà không cần tạo nhiều `JFrame`.
  * `GridBagLayout` & `BoxLayout`: Căn chỉnh form nhập liệu, danh sách tin nhắn.
  * **Quy tắc:** Tuyệt đối không dùng `null layout` (`setLayout(null)`).
* **Luồng giao diện:** Toàn bộ giao diện được khởi tạo và cập nhật thông qua `SwingUtilities.invokeLater()`.

### 2.4. Mạng & Giao tiếp Client - Server (Networking)
* **Giao thức:** **TCP Stream thuần** thông qua `java.net.Socket` và `java.net.ServerSocket`.
* **Ràng buộc:** Tuyệt đối không dùng HTTP/REST, WebSocket hay STOMP cho kênh giao tiếp chính.
* **Mô hình xử lý tại Server:** Multi-threaded Socket Server hoặc Thread Pool (ExecutorService) tiếp nhận kết nối đồng thời từ nhiều client.
* **Cơ chế Presence (Trực tuyến/Ngoại tuyến):** Client gửi gói tin nhịp tim (Heartbeat) định kỳ 30–60s để server cập nhật trường `last_seen_at`.

### 2.5. Cơ sở dữ liệu & Lưu trữ (Database & Storage)
* **Hệ quản trị CSDL:** **MySQL 8.0+** (hoặc MariaDB 10.5+).
* **Bảng mã & Collation:** `utf8mb4` và `utf8mb4_unicode_ci` (Lưu trữ văn bản tiếng Việt chuẩn Unicode và Emoji 4-byte).
* **Kiến trúc Schema v2:**
  * **11 bảng chuẩn hóa:** `users`, `user_sessions`, `conversations`, `conversation_members`, `messages`, `message_deletions`, `friendships`, `friend_requests`, `user_blocks`, `spam_reports`, `conversation_member_keys`.
  * **1 VIEW hỗ trợ:** `friend_edges` (xử lý quan hệ bạn bè 2 chiều và tính "bạn của bạn").
  * **Toàn vẹn dữ liệu:** Ràng buộc `CHECK` logic (`low_id < high_id`, loại trừ tự kết bạn/tự chặn/tự báo cáo).
  * **Tìm kiếm:** `FULLTEXT INDEX` trên trường `messages.content` phục vụ tìm kiếm lịch sử chat nhanh chóng.

### 2.6. Khung ứng dụng Server ở Phiên bản 2 (Backend Framework)
* **Spring Boot (Phiên bản không web):**
  * Cấu hình bắt buộc: `spring.main.web-application-type=none`.
  * Chỉ sử dụng Spring Boot để tận dụng: Dependency Injection (IoC), Quản lý cấu hình (`application.properties`), Spring Data JPA / JDBC và Transaction Management (`@Transactional`).
* **Định tuyến cấu hình ngoài:** Đọc thông tin kết nối DB từ file ngoài `config.properties` mà không cần biên dịch lại mã nguồn (đáp ứng tiêu chí nộp bài Phiên bản 2).

### 2.7. Bảo mật & Mật mã học (Security & Cryptography)
* **Bảo mật mật khẩu:** Băm mật khẩu bằng thuật toán một chiều an toàn kèm muối (BCrypt / PBKDF2 / SHA-256 + Salt).
* **Mã hóa tin nhắn nhóm (End-to-End Encryption - E2EE):**
  * Thuật toán mã hóa đối xứng: **AES-256-GCM** (Nội dung mã hóa kèm Authentication Tag và Nonce 12-byte).
  * Thuật toán bất đối xứng trao đổi khóa: **RSA-2048** hoặc **ECDH** (Server chỉ lưu `encrypted_group_key` của từng thành viên, không nắm giữ khóa giải mã tin nhắn).

### 2.8. Kiểm thử & Quản lý mã nguồn (Testing & Git Workflow)
* **Kiểm thử tự động:** JUnit 5 Jupiter (`5.10.2`) cho các unit test logic kiểm tra format, mã hóa, xử lý chuỗi.
* **Hệ thống quản lý phiên bản:** Git & GitHub.
* **Mô hình phân nhánh (Git Flow):**
  * `main`: Nhánh sản phẩm ổn định.
  * `develop`: Nhánh tích hợp chính của nhóm.
  * `feature/admin-*` & `feature/user-*`: Nhánh tính năng riêng của từng thành viên để tránh xung đột mã nguồn.

---

## 3. Bảng tổng hợp phiên bản công nghệ (Matrix Versions)

| Thành phần | Công nghệ / Thư viện | Phiên bản | Mục đích sử dụng |
| :--- | :--- | :---: | :--- |
| **Ngôn ngữ** | Java (JDK) | `17 LTS` | Nền tảng lập trình toàn hệ thống |
| **Quản lý dự án** | Apache Maven | `3.9.x` | Quản lý vòng đời build đa module |
| **Giao diện** | Java Swing | Chuẩn JDK | Xây dựng giao diện desktop |
| **Giao diện mở rộng** | FlatLaf | `3.5.4` | Look and Feel hiện đại, bo góc |
| **Cơ sở dữ liệu** | MySQL Server | `8.0+` | Lưu trữ dữ liệu quan hệ utf8mb4 |
| **Mạng** | Java TCP Sockets | `java.net.*` | Kênh truyền thông thời gian thực |
| **Backend (V2)** | Spring Boot (Non-web) | `3.2.x / 3.3.x` | IoC, Quản lý Transaction & JPA cho Server |
| **Kiểm thử** | JUnit Jupiter | `5.10.2` | Viết unit tests kiểm thử logic |
| **Đóng gói** | Maven Shade Plugin | `3.5.2` | Tạo file `admin.jar` và `user.jar` |

---

## 4. Cam kết tuân thủ quy chế môn học

Dự án cam kết 100% tuân thủ các quy tắc của Giảng viên và Bộ môn:
1. **Không sử dụng ngôn ngữ ngoài Java** (Không Python, Node.js, JS, TS).
2. **Không làm giao diện Web** (Chỉ dùng Swing thuần).
3. **Không dùng WebSocket / REST API** cho giao tiếp chat chính (Dùng Socket TCP thuần).
4. **Không nhúng Spring Boot vào Client** (Client là Swing độc lập).
5. **Cấu trúc nộp bài chuẩn xác** với đầy đủ các thư mục `src/`, `script/`, `git/`, `release/`.
