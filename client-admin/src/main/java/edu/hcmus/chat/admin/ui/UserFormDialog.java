package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.admin.service.ServiceException;
import edu.hcmus.chat.admin.service.UserService;
import edu.hcmus.chat.common.model.GenderType;
import edu.hcmus.chat.common.model.User;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog: add a new user, or edit an existing one (admin requirement 1b).
 * Pass null to add; pass a user to edit (username is locked, no password field,
 * because the password has its own dialog).
 *
 * The dialog only collects text; UserService checks it and saves it.
 * After setVisible(true) returns, isSaved() tells the caller whether to reload.
 *
 * Owner: Member A (admin).
 */
public class UserFormDialog extends JDialog {

    // Same order as the gender combo box items
    private static final GenderType[] GENDERS = {GenderType.MALE, GenderType.FEMALE, GenderType.OTHER};

    private final UserService userService;
    private final User editingUser;
    private boolean saved = false;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField addressField;
    private JTextField dateOfBirthField;
    private JComboBox<String> genderComboBox;

    public UserFormDialog(Window owner, UserService userService, User editingUser) {
        super(owner, editingUser == null ? "Thêm người dùng" : "Cập nhật người dùng", ModalityType.APPLICATION_MODAL);
        this.userService = userService;
        this.editingUser = editingUser;
        initUI();
        if (editingUser != null) {
            fillForm(editingUser);
        }
    }

    public boolean isSaved() {
        return saved;
    }

    private boolean isAddMode() {
        return editingUser == null;
    }

    private void initUI() {
        setSize(460, isAddMode() ? 470 : 430);
        setLocationRelativeTo(getOwner());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(0, 15));
        content.setBackground(Theme.BG_SURFACE);
        content.setBorder(Theme.createPadding(20, 20, 20, 20));
        setContentPane(content);

        JLabel titleLabel = new JLabel(isAddMode() ? "THÊM NGƯỜI DÙNG" : "CẬP NHẬT THÔNG TIN");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.PRIMARY_COLOR);
        content.add(titleLabel, BorderLayout.NORTH);

        // The form sits in NORTH of a wrapper so the rows keep their height
        // instead of stretching when the dialog is taller than the form.
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setOpaque(false);
        formWrapper.add(createForm(), BorderLayout.NORTH);
        content.add(formWrapper, BorderLayout.CENTER);

        content.add(createButtonBar(), BorderLayout.SOUTH);
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        usernameField = AdminUi.createTextField("Chữ cái và số, không dấu", 20);
        passwordField = new JPasswordField(20);
        passwordField.setFont(Theme.FONT_REGULAR);
        fullNameField = AdminUi.createTextField("Họ và tên", 20);
        emailField = AdminUi.createTextField("ten@example.com", 20);
        addressField = AdminUi.createTextField("Địa chỉ", 20);
        dateOfBirthField = AdminUi.createTextField("dd/MM/yyyy", 20);
        genderComboBox = AdminUi.createComboBox("Nam", "Nữ", "Khác");

        int row = 0;
        addRow(form, row++, "Tên đăng nhập", usernameField);
        if (isAddMode()) {
            addRow(form, row++, "Mật khẩu", passwordField);
        }
        addRow(form, row++, "Họ và tên", fullNameField);
        addRow(form, row++, "Email", emailField);
        addRow(form, row++, "Địa chỉ", addressField);
        addRow(form, row++, "Ngày sinh", dateOfBirthField);
        addRow(form, row, "Giới tính", genderComboBox);
        return form;
    }

    /**
     * Label in column 0, field in column 1 (the field takes the extra width).
     */
    private void addRow(JPanel form, int row, String labelText, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(6, 0, 6, 12);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        form.add(AdminUi.createLabel(labelText), c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 0, 6, 0);
        form.add(field, c);
    }

    private JPanel createButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setOpaque(false);

        JButton cancelButton = Theme.createSecondaryButton("Hủy");
        JButton saveButton = Theme.createPrimaryButton(isAddMode() ? "Thêm người dùng" : "Lưu thay đổi");
        cancelButton.addActionListener(e -> dispose());
        saveButton.addActionListener(e -> save());

        bar.add(cancelButton);
        bar.add(saveButton);
        return bar;
    }

    private void fillForm(User user) {
        usernameField.setText(user.username());
        usernameField.setEditable(false);
        fullNameField.setText(user.fullName());
        emailField.setText(user.email());
        addressField.setText(user.address() == null ? "" : user.address());
        dateOfBirthField.setText(AdminFormat.date(user.dateOfBirth()));
        if (user.gender() != null) {
            genderComboBox.setSelectedItem(AdminFormat.gender(user.gender()));
        }
    }

    /**
     * Hands the raw text to the service. If a rule fails, the dialog stays open
     * with the service's message so the admin can fix the field.
     */
    private void save() {
        GenderType gender = GENDERS[genderComboBox.getSelectedIndex()];
        try {
            if (isAddMode()) {
                userService.addUser(usernameField.getText(), new String(passwordField.getPassword()),
                        fullNameField.getText(), emailField.getText(), addressField.getText(),
                        dateOfBirthField.getText(), gender);
            } else {
                userService.updateUser(editingUser.userId(), fullNameField.getText(), emailField.getText(),
                        addressField.getText(), dateOfBirthField.getText(), gender);
            }
        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Không lưu được", JOptionPane.WARNING_MESSAGE);
            return;
        }

        saved = true;
        JOptionPane.showMessageDialog(this, "Đã lưu thông tin người dùng.", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
