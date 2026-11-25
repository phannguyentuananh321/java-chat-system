package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.admin.service.UserService;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Standalone runner for UserDetailDialog, used to check the layout and take report screenshots.
 * The dialog has no owner window here, so closing it ends the program.
 * Needs config.properties and the database, same as the real app.
 *
 * Owner: Member A (admin).
 */
public class UserDetailDialogDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();
            new UserDetailDialog(null, new UserService(), DummyAdminData.findUserById(2), UserDetailDialog.TAB_LOGIN_HISTORY).setVisible(true);
        });
    }
}
