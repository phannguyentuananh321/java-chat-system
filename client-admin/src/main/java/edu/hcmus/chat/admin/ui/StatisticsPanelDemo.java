package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;

/**
 * Standalone runner for StatisticsPanel, used to check the layout and take report screenshots.
 *
 * Owner: Member A (admin).
 */
public class StatisticsPanelDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.install();

            JFrame frame = new JFrame("Demo - Thống kê & báo cáo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 650);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new StatisticsPanel());
            frame.setVisible(true);
        });
    }
}
