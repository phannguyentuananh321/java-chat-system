package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.admin.ui.components.MonthlyBarChart;
import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.function.IntFunction;

/**
 * Tab: pick a year, see a 12-month bar chart for it (admin requirements 6 and 9).
 *
 * Both charts look the same and only differ in their numbers, so this panel takes
 * the data source as a parameter: a function from year to 12 monthly values.
 *
 * showYearTotal: a yearly total makes sense for registrations (each person registers
 * once), but not for active users (the same person is active in many months).
 *
 * Owner: Member A (admin).
 */
public class YearlyChartPanel extends JPanel {

    private final String chartTitle;
    private final IntFunction<int[]> monthlyDataSource;
    private final boolean showYearTotal;

    private final MonthlyBarChart chart = new MonthlyBarChart();
    private JComboBox<Integer> yearComboBox;
    private JLabel totalLabel;

    public YearlyChartPanel(String chartTitle, IntFunction<int[]> monthlyDataSource, boolean showYearTotal) {
        this.chartTitle = chartTitle;
        this.monthlyDataSource = monthlyDataSource;
        this.showYearTotal = showYearTotal;
        initUI();
        showSelectedYear();
    }

    private void initUI() {
        setLayout(new BorderLayout(0, 12));
        setBackground(Theme.BG_SURFACE);
        setBorder(Theme.createCardBorder(12));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        JLabel titleLabel = new JLabel(chartTitle);
        titleLabel.setFont(Theme.FONT_SUBTITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);
        topBar.add(titleLabel, BorderLayout.WEST);

        yearComboBox = new JComboBox<>();
        yearComboBox.setFont(Theme.FONT_REGULAR);
        for (int year : DummyAdminData.getChartYears()) {
            yearComboBox.addItem(year);
        }
        // Unlike the filters, this one is wired: changing the year redraws the chart
        yearComboBox.addActionListener(e -> showSelectedYear());

        JPanel yearPicker = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        yearPicker.setOpaque(false);
        yearPicker.add(AdminUi.createLabel("Năm"));
        yearPicker.add(yearComboBox);
        topBar.add(yearPicker, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(chart, BorderLayout.CENTER);

        totalLabel = new JLabel();
        totalLabel.setFont(Theme.FONT_BOLD);
        totalLabel.setForeground(Theme.TEXT_MUTED);
        totalLabel.setVisible(showYearTotal);
        add(totalLabel, BorderLayout.SOUTH);
    }

    private void showSelectedYear() {
        int year = (Integer) yearComboBox.getSelectedItem();
        int[] monthlyValues = monthlyDataSource.apply(year);
        chart.setValues(monthlyValues);

        int total = 0;
        for (int value : monthlyValues) {
            total += value;
        }
        totalLabel.setText("Tổng cả năm " + year + ": " + total);
    }
}
