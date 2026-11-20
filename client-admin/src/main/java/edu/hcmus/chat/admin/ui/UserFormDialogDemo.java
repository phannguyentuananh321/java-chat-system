package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Standalone runner for UserFormDialog, used to check the layout and take report screenshots.
 * The dialog has no owner window here, so closing it ends the program.
 *
 * Owner: Member A (admin).
 */
public class UserFormDialogDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();
            new UserFormDialog(null, DummyAdminData.findUserById(2)).setVisible(true);
        });
    }
}
