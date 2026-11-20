package edu.hcmus.chat.admin;

import edu.hcmus.chat.admin.ui.AdminShellPanel;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Entry point of the admin app (client-admin).
 * Opens one window; the sidebar and screen switching live in AdminShellPanel.
 *
 * Owner: Member A (admin).
 */
public class AdminMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame mainFrame = new JFrame("Hệ thống Chat HCMUS - Quản trị viên");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(1000, 650);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setContentPane(new AdminShellPanel());
            mainFrame.setVisible(true);
        });
    }
}
