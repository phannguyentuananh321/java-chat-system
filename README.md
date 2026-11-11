# HỆ THỐNG CHAT (CHAT SYSTEM) - MÔN LẬP TRÌNH ỨNG DỤNG JAVA

Dự án đồ án học phần **Lập trình ứng dụng Java**, xây dựng theo mô hình **Maven Multi-Module** với giao diện máy khách desktop bằng **Java Swing**. Cấu trúc mã nguồn được thiết kế phân tách rõ ràng để 2 sinh viên có thể cộng tác phát triển song song trên một Git Repository mà không lo ngại xung đột (merge conflicts).

---

## 1. Thông tin chung & Bảng phân công quyền sở hữu

- **Ngôn ngữ:** Java 17 LTS (UTF-8)
- **Công cụ build:** Apache Maven (Multi-Module)
- **Giao diện:** Java Swing kết hợp FlatLaf Look and Feel
- **groupId:** `edu.hcmus`
- **artifactId gốc:** `chat-system`
- **package gốc:** `edu.hcmus.chat`

### Bảng phân công trách nhiệm & quyền sở hữu tệp (Code Ownership)

| Thành viên | Phân hệ phụ trách | Module quản lý | Lớp giao diện (JPanel) | Lớp Demo chạy thử | Phạm vi quyền hạn |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Thành viên A** | Quản trị viên (Admin) | `client-admin` | `UserManagementPanel` | `UserManagementPanelDemo` | Toàn quyền trong thư mục `client-admin/` |
| **Thành viên B** | Người dùng (User) | `client-user` | `ChatMainPanel` | `ChatMainPanelDemo` | Toàn quyền trong thư mục `client-user/` |
| **Đồng sở hữu (A & B)** | Dùng chung & Cấu hình | `common`, `pom.xml`, `docs/`, `.gitignore` | `Theme.java` | — | Cần thảo luận trước và tạo Pull Request để người kia duyệt |

---

## 2. Cấu trúc thư mục dự án

```text
chat-system/
├─ pom.xml                                    # Parent POM quản lý versions, plugins và 3 modules con
├─ .gitignore                                 # Loại bỏ target, file IDE, log, cấu hình nhạy cảm
├─ config.properties.example                  # File cấu hình mẫu tham khảo (không commit file thật)
├─ README.md                                  # Hướng dẫn build, chạy demo và quy trình làm việc Git
├─ docs/
│  ├─ decisions.md                            # Ràng buộc từ GV, quyết định công nghệ & quyền sở hữu
│  └─ screens.md                              # Bảng theo dõi danh mục và tiến độ các màn hình
├─ script/
│  └─ .gitkeep                                # Nơi lưu script chạy / build sau này
├─ git/
│  └─ .gitkeep                                # Nơi lưu git hooks nếu cần
├─ common/                                    # Module chứa tài nguyên dùng chung
│  ├─ pom.xml
│  └─ src/main/java/edu/hcmus/chat/common/ui/
│     └─ Theme.java                           # Bảng màu, font, Theme.install() & tiện ích giao diện
├─ client-admin/                              # Phân hệ Quản trị viên (Thành viên A)
│  ├─ pom.xml                                 # Cấu hình exec-maven-plugin và shade (admin.jar)
│  └─ src/
│     ├─ main/resources/images/.gitkeep       # Chứa icon/hình ảnh nạp bằng relative classpath
│     └─ main/java/edu/hcmus/chat/admin/
│        ├─ AdminMain.java                    # Entrypoint phân hệ Admin (JFrame CardLayout)
│        └─ ui/
│           ├─ UserManagementPanel.java       # Màn hình quản lý người dùng (JPanel)
│           ├─ UserManagementPanelDemo.java   # Demo chạy độc lập (1000x650) để chụp ảnh
│           └─ DummyAdminData.java            # Dữ liệu mẫu (không gọi DB/mạng trong UI)
└─ client-user/                               # Phân hệ Người dùng (Thành viên B)
   ├─ pom.xml                                 # Cấu hình exec-maven-plugin và shade (user.jar)
   └─ src/
      ├─ main/resources/images/.gitkeep        # Chứa icon/hình ảnh nạp bằng relative classpath
      └─ main/java/edu/hcmus/chat/user/
         ├─ UserMain.java                     # Entrypoint phân hệ User (JFrame CardLayout)
         ├─ network/.gitkeep                  # Thư mục chứa TCP Socket Client sau này
         └─ ui/
            ├─ ChatMainPanel.java             # Màn hình chat chính (JPanel)
            ├─ ChatMainPanelDemo.java         # Demo chạy độc lập (1000x650) để chụp ảnh
            └─ DummyUserData.java             # Dữ liệu mẫu (không gọi DB/mạng trong UI)
```

---

## 3. Hướng dẫn Biên dịch (Build) dự án

Mở terminal tại thư mục gốc của dự án (`chat-system/`):

### 3.1. Biên dịch toàn bộ các module
```bash
mvn clean compile
```

### 3.2. Đóng gói kiểm tra và tạo file Fat JAR thực thi (.jar)
```bash
mvn clean package
```
Sau lệnh trên, các file JAR thực thi sẽ được tạo ra tại:
- `client-admin/target/admin.jar`
- `client-user/target/user.jar`

---

## 4. Hướng dẫn chạy thử giao diện và các bản Demo

### Cách 1: Chạy trực tiếp từng màn hình Demo (1000 x 650)

- **Màn hình Quản lý người dùng của Thành viên A (Admin):**
  ```bash
  mvn exec:java -pl client-admin -Dexec.mainClass="edu.hcmus.chat.admin.ui.UserManagementPanelDemo"
  ```

- **Màn hình Khung chat chính của Thành viên B (User):**
  ```bash
  mvn exec:java -pl client-user -Dexec.mainClass="edu.hcmus.chat.user.ui.ChatMainPanelDemo"
  ```

### Cách 2: Chạy ứng dụng chính qua Maven `exec:java`

- **Khởi chạy ứng dụng Admin:**
  ```bash
  mvn exec:java -pl client-admin
  ```

- **Khởi chạy ứng dụng Người dùng:**
  ```bash
  mvn exec:java -pl client-user
  ```

### Cách 3: Chạy từ file JAR đóng gói (Sau khi đã chạy `mvn clean package`)

- **Chạy Admin:**
  ```bash
  java -jar client-admin/target/admin.jar
  ```

- **Chạy User:**
  ```bash
  java -jar client-user/target/user.jar
  ```

---

## 5. Quy trình làm việc với Git & GitHub (Bắt buộc tuân thủ)

Để bảo đảm 2 thành viên không bao giờ xảy ra xung đột mã nguồn:

### 5.1. Cấu trúc nhánh (Branching Model)
- **`main`**: Nhánh lưu trữ mã nguồn ổn định nhất, chỉ merge khi đã kiểm thử xong.
- **`develop`**: Nhánh tích hợp thường xuyên của cả nhóm.
- **Nhánh tính năng (Feature branches):**
  - Thành viên A tạo nhánh từ `develop`: `feature/admin-<tên-màn-hình>` (Ví dụ: `feature/admin-user-management`).
  - Thành viên B tạo nhánh từ `develop`: `feature/user-<tên-màn-hình>` (Ví dụ: `feature/user-chat-main`).

### 5.2. Quy tắc Commit (Conventional Commits)
- Chia nhỏ commit theo từng công việc cụ thể, thông điệp rõ ràng:
  - `feat: <nội dung>` (khi thêm giao diện hoặc chức năng mới)
  - `fix: <nội dung>` (khi sửa lỗi giao diện hoặc logic)
  - `docs: <nội dung>` (khi cập nhật tài liệu hướng dẫn)
  - `refactor: <nội dung>` (khi tối ưu hóa mã nguồn mà không đổi hành vi)

### 5.3. Quy trình Pull Request & Review
1. Tuyệt đối **không commit trực tiếp** lên nhánh `main` hoặc `develop`.
2. Khi hoàn thành công việc trên nhánh tính năng, đẩy lên GitHub và mở **Pull Request (PR)** vào nhánh `develop`.
3. Thành viên còn lại sẽ review code. Kiểm tra không có xung đột mới tiến hành Merge.

### 5.4. Quy tắc an toàn & bảo mật
- **Chỉ sửa file trong module của mình:** Thành viên A chỉ sửa trong `client-admin/`, Thành viên B chỉ sửa trong `client-user/`.
- Nếu có nhu cầu bổ sung thư viện trong `pom.xml`, chỉnh sửa `common/` hoặc tài liệu `docs/`, bắt buộc phải thông báo và thống nhất trước với thành viên kia.
- Tuyệt đối **không commit mật khẩu, tài khoản CSDL, file cấu hình thật** lên Git. Chỉ commit file mẫu `config.properties.example`.
