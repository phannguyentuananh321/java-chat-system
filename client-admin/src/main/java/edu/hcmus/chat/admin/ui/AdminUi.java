package edu.hcmus.chat.admin.ui;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Builds the pieces every admin screen repeats: page header, card, filter bar
 * widgets, read-only tables and the red "danger" button.
 *
 * Theme (in common) already has colors, fonts and the primary/secondary buttons.
 * This class only adds what the admin side needs, so common/ stays untouched.
 */
public final class AdminUi {

    private AdminUi() {
        // static helpers only
    }

    /**
     * Big title with one muted line under it, placed at the top of a screen.
     */
    public static JPanel createPageHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout(0, 4));
        header.setOpaque(false);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(Theme.FONT_REGULAR);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);

        header.add(Theme.createHeaderLabel(title), BorderLayout.NORTH);
        header.add(subtitleLabel, BorderLayout.SOUTH);
        return header;
    }

    /**
     * White box with a thin border. Content goes in with BorderLayout positions.
     */
    public static JPanel createCard() {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(Theme.BG_SURFACE);
        card.setBorder(Theme.createCardBorder(12));
        return card;
    }

    /**
     * One row of filter controls, left aligned.
     * FlowLayout does not wrap nicely inside BorderLayout.NORTH, so keep each row short.
     */
    public static JPanel createFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bar.setOpaque(false);
        return bar;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT_REGULAR);
        label.setForeground(Theme.TEXT_DARK);
        return label;
    }

    public static JTextField createTextField(String placeholder, int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(Theme.FONT_REGULAR);
        // FlatLaf shows this grey hint text while the field is empty
        field.putClientProperty("JTextField.placeholderText", placeholder);
        return field;
    }

    /**
     * Plain text field for dates. A real date picker is not part of Swing,
     * so the admin types dd/MM/yyyy (parsed later, when filters are wired).
     */
    public static JTextField createDateField() {
        return createTextField("dd/MM/yyyy", 8);
    }

    public static JComboBox<String> createComboBox(String... items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(Theme.FONT_REGULAR);
        return comboBox;
    }

    /**
     * The "=, <, >" picker used by the count filters (friend count, activity count).
     */
    public static JComboBox<String> createCompareComboBox() {
        return createComboBox("=", "<", ">");
    }

    /**
     * Same shape as Theme.createPrimaryButton, but red. Used for lock / delete.
     */
    public static JButton createDangerButton(String text) {
        JButton button = Theme.createPrimaryButton(text);
        button.setBackground(Theme.DANGER_COLOR);
        return button;
    }

    /**
     * Table model where the user can't type into cells (admin edits go through dialogs).
     */
    public static DefaultTableModel createReadOnlyModel(String[] columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(Theme.FONT_REGULAR);
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(Theme.FONT_BOLD);
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }

    /**
     * Preferred width per column, in order. The table still stretches to fill the
     * space, these numbers only set the ratio (so ID columns stay narrow).
     */
    public static void setColumnWidths(JTable table, int... widths) {
        TableColumnModel columns = table.getColumnModel();
        for (int i = 0; i < widths.length && i < columns.getColumnCount(); i++) {
            columns.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Wraps a table in a scroll pane with the same thin border as the cards.
     */
    public static JScrollPane createTableScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        return scrollPane;
    }
}
