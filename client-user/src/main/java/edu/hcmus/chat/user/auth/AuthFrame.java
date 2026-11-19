package edu.hcmus.chat.user.auth;

import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.service.AuthService;
import edu.hcmus.chat.user.service.UserServiceFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Cửa sổ Xác thực (Đăng nhập / Đăng ký / Quên mật khẩu) cho Phân hệ Người dùng.
 * Sử dụng CardLayout để chuyển đổi mượt mà giữa các form trên cùng 1 cửa sổ.
 * 100% Java Swing thuần túy.
 */
public class AuthFrame extends JFrame {

    public static final String CARD_LOGIN = "CARD_LOGIN";
    public static final String CARD_REGISTER = "CARD_REGISTER";
    public static final String CARD_FORGOT = "CARD_FORGOT";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(cardLayout);

    private final AuthService authService = UserServiceFactory.getAuthService();
    private Runnable onLoginSuccessCallback;

    public AuthFrame() {
        initUI();
    }

    public void setOnLoginSuccess(Runnable callback) {
        this.onLoginSuccessCallback = callback;
    }

    private void initUI() {
        setTitle("Hệ thống Chat HCMUS - Xác thực tài khoản");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 640);
        setLocationRelativeTo(null);
        setResizable(false);

        rootPanel.add(createLoginPanel(), CARD_LOGIN);
        rootPanel.add(createRegisterPanel(), CARD_REGISTER);
        rootPanel.add(createForgotPanel(), CARD_FORGOT);

        setContentPane(rootPanel);
        cardLayout.show(rootPanel, CARD_LOGIN);
    }

    // =========================================================================
    // 1. FORM ĐĂNG NHẬP
    // =========================================================================
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Theme.BG_SURFACE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        JLabel title = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_COLOR);

        JLabel subtitle = new JLabel("Chào mừng bạn quay trở lại hệ thống chat", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_REGULAR);
        subtitle.setForeground(Theme.TEXT_MUTED);

        header.add(title);
        header.add(subtitle);
        panel.add(header, BorderLayout.NORTH);

        // Form fields
        JPanel form = new JPanel(new GridLayout(5, 1, 0, 10));
        form.setOpaque(false);

        JTextField usernameField = new JTextField("nguyenvana");
        usernameField.setFont(Theme.FONT_REGULAR);
        usernameField.putClientProperty("JTextField.placeholderText", "Tên đăng nhập");

        JPasswordField passwordField = new JPasswordField("password123");
        passwordField.setFont(Theme.FONT_REGULAR);
        passwordField.putClientProperty("JTextField.placeholderText", "Mật khẩu");

        JButton loginButton = Theme.createPrimaryButton("Đăng nhập");
        loginButton.addActionListener(e -> {
            boolean success = authService.login(usernameField.getText(), new String(passwordField.getPassword()));
            if (success) {
                if (onLoginSuccessCallback != null) {
                    onLoginSuccessCallback.run();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnGoForgot = new JButton("Quên mật khẩu?");
        btnGoForgot.setBorderPainted(false);
        btnGoForgot.setContentAreaFilled(false);
        btnGoForgot.setForeground(Theme.PRIMARY_COLOR);
        btnGoForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGoForgot.addActionListener(e -> cardLayout.show(rootPanel, CARD_FORGOT));

        JButton btnGoRegister = new JButton("Chưa có tài khoản? Đăng ký ngay");
        btnGoRegister.setBorderPainted(false);
        btnGoRegister.setContentAreaFilled(false);
        btnGoRegister.setForeground(Theme.TEXT_MUTED);
        btnGoRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGoRegister.addActionListener(e -> cardLayout.show(rootPanel, CARD_REGISTER));

        form.add(usernameField);
        form.add(passwordField);
        form.add(loginButton);
        form.add(btnGoForgot);
        form.add(btnGoRegister);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // 2. FORM ĐĂNG KÝ TÀI KHOẢN MỚI
    // =========================================================================
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setBackground(Theme.BG_SURFACE);
        panel.setBorder(new EmptyBorder(25, 40, 25, 40));

        JLabel title = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_COLOR);
        panel.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(8, 1, 0, 8));
        form.setOpaque(false);

        JTextField usernameField = new JTextField();
        usernameField.putClientProperty("JTextField.placeholderText", "Tên đăng nhập (chữ cái, số)");

        JTextField fullNameField = new JTextField();
        fullNameField.putClientProperty("JTextField.placeholderText", "Họ và tên");

        JTextField emailField = new JTextField();
        emailField.putClientProperty("JTextField.placeholderText", "Email sinh viên (nhận mã / mật khẩu)");

        JPasswordField passwordField = new JPasswordField();
        passwordField.putClientProperty("JTextField.placeholderText", "Mật khẩu");

        JTextField addressField = new JTextField();
        addressField.putClientProperty("JTextField.placeholderText", "Địa chỉ");

        JComboBox<GenderType> genderCombo = new JComboBox<>(GenderType.values());

        JButton registerButton = Theme.createPrimaryButton("Tạo tài khoản");
        registerButton.addActionListener(e -> {
            boolean ok = authService.register(
                    usernameField.getText(),
                    new String(passwordField.getPassword()),
                    fullNameField.getText(),
                    emailField.getText(),
                    addressField.getText(),
                    LocalDate.of(2004, 1, 1),
                    (GenderType) genderCombo.getSelectedItem()
            );
            if (ok) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(rootPanel, CARD_LOGIN);
            }
        });

        JButton btnBackLogin = new JButton("Đã có tài khoản? Quay lại Đăng nhập");
        btnBackLogin.setBorderPainted(false);
        btnBackLogin.setContentAreaFilled(false);
        btnBackLogin.setForeground(Theme.PRIMARY_COLOR);
        btnBackLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBackLogin.addActionListener(e -> cardLayout.show(rootPanel, CARD_LOGIN));

        form.add(usernameField);
        form.add(fullNameField);
        form.add(emailField);
        form.add(passwordField);
        form.add(addressField);
        form.add(genderCombo);
        form.add(registerButton);
        form.add(btnBackLogin);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }

    // =========================================================================
    // 3. FORM QUÊN MẬT KHẨU (Gửi mật khẩu ngẫu nhiên tới email theo đề bài)
    // =========================================================================
    private JPanel createForgotPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(Theme.BG_SURFACE);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new GridLayout(3, 1, 0, 6));
        header.setOpaque(false);

        JLabel title = new JLabel("KHỞI TẠO LẠI MẬT KHẨU", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_COLOR);

        JLabel desc1 = new JLabel("Nhập email đã đăng ký tài khoản của bạn.", SwingConstants.CENTER);
        desc1.setFont(Theme.FONT_REGULAR);
        desc1.setForeground(Theme.TEXT_MUTED);

        JLabel desc2 = new JLabel("Hệ thống sẽ tạo mật khẩu ngẫu nhiên và gửi tới email.", SwingConstants.CENTER);
        desc2.setFont(Theme.FONT_SMALL);
        desc2.setForeground(Theme.TEXT_MUTED);

        header.add(title);
        header.add(desc1);
        header.add(desc2);
        panel.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 12));
        form.setOpaque(false);

        JTextField emailField = new JTextField("nva@student.hcmus.edu.vn");
        emailField.setFont(Theme.FONT_REGULAR);
        emailField.putClientProperty("JTextField.placeholderText", "Địa chỉ email của bạn");

        JButton sendButton = Theme.createPrimaryButton("Gửi mật khẩu mới");
        sendButton.addActionListener(e -> {
            boolean ok = authService.resetPassword(emailField.getText());
            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Mật khẩu ngẫu nhiên mới đã được gửi tới email " + emailField.getText() + "!\nVui lòng kiểm tra hộp thư để đăng nhập.",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(rootPanel, CARD_LOGIN);
            }
        });

        JButton btnBack = new JButton("Quay lại màn hình Đăng nhập");
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setForeground(Theme.PRIMARY_COLOR);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> cardLayout.show(rootPanel, CARD_LOGIN));

        form.add(emailField);
        form.add(sendButton);
        form.add(btnBack);

        panel.add(form, BorderLayout.CENTER);
        return panel;
    }
}
