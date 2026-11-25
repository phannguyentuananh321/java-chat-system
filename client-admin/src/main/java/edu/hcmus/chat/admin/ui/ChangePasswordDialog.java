package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.admin.service.ServiceException;
import edu.hcmus.chat.admin.service.UserService;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog: admin sets a new password for a user (admin requirement 1d).
 * Length and "both fields match" are checked by UserService.
 *
 * Owner: Member A (admin).
 */
public class ChangePasswordDialog extends JDialog {

    private final UserService userService;
    private final User user;

    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public ChangePasswordDialog(Window owner, UserService userService, User user) {
        super(owner, "Đổi mật khẩu", ModalityType.APPLICATION_MODAL);
        this.userService = userService;
        this.user = user;
        initUI();
    }

    private void initUI() {
        setSize(420, 300);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(Theme.BG_SURFACE);
        content.setBorder(Theme.createPadding(20, 20, 20, 20));
        setContentPane(content);

        JPanel header = new JPanel(new BorderLayout(0, 4));
        header.setOpaque(false);
        JLabel titleLabel = new JLabel("ĐỔI MẬT KHẨU");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.PRIMARY_COLOR);
        JLabel userLabel = new JLabel("Tài khoản: " + user.username() + " (" + user.fullName() + ")");
        userLabel.setFont(Theme.FONT_REGULAR);
        userLabel.setForeground(Theme.TEXT_MUTED);
        header.add(titleLabel, BorderLayout.NORTH);
        header.add(userLabel, BorderLayout.SOUTH);
        content.add(header, BorderLayout.NORTH);

        newPasswordField = new JPasswordField(18);
        newPasswordField.setFont(Theme.FONT_REGULAR);
        confirmPasswordField = new JPasswordField(18);
        confirmPasswordField.setFont(Theme.FONT_REGULAR);

        // Two label/field rows, small enough for a GridLayout
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 12));
        form.setOpaque(false);
        form.add(AdminUi.createLabel("Mật khẩu mới"));
        form.add(newPasswordField);
        form.add(AdminUi.createLabel("Nhập lại mật khẩu"));
        form.add(confirmPasswordField);

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(form, BorderLayout.NORTH);
        content.add(formWrapper, BorderLayout.CENTER);

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonBar.setOpaque(false);
        JButton cancelButton = Theme.createSecondaryButton("Hủy");
        JButton saveButton = Theme.createPrimaryButton("Cập nhật mật khẩu");
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> save());
        buttonBar.add(cancelButton);
        buttonBar.add(saveButton);
        content.add(buttonBar, BorderLayout.SOUTH);
    }

    private void save() {
        try {
            userService.changePassword(user.userId(),
                    new String(newPasswordField.getPassword()),
                    new String(confirmPasswordField.getPassword()));
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Không đổi được mật khẩu",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "Đã cập nhật mật khẩu cho " + user.username() + ".",
                "Thành công", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
