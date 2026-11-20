package edu.hcmus.chat.admin.ui.components;

import edu.hcmus.chat.common.ui.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * A plain bar chart with 12 bars, one per month (T1..T12).
 * Drawn by hand with Graphics2D, so the project needs no chart library.
 *
 * It only knows how to draw 12 numbers; where the numbers come from is the
 * caller's business (see YearlyChartPanel).
 */
public class MonthlyBarChart extends JPanel {

    // Space around the plot area, for the axis labels
    private static final int PADDING_LEFT = 48;
    private static final int PADDING_RIGHT = 16;
    private static final int PADDING_TOP = 24;
    private static final int PADDING_BOTTOM = 32;

    private int[] values = new int[12];

    public MonthlyBarChart() {
        setBackground(Theme.BG_SURFACE);
        setPreferredSize(new Dimension(600, 320));
    }

    /**
     * Expects 12 values, January first.
     */
    public void setValues(int[] values) {
        this.values = values.clone();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(Theme.FONT_SMALL);

        int plotWidth = getWidth() - PADDING_LEFT - PADDING_RIGHT;
        int plotHeight = getHeight() - PADDING_TOP - PADDING_BOTTOM;
        if (plotWidth > 0 && plotHeight > 0) {
            int maxValue = getMaxValue();
            int step = getGridStep(maxValue);
            // Round the top of the axis up to a whole step, so the last grid line sits at the top
            int axisMax = ((maxValue + step - 1) / step) * step;

            drawGridLines(g2, plotWidth, plotHeight, axisMax, step);
            drawBars(g2, plotWidth, plotHeight, axisMax);
        }
        g2.dispose();
    }

    /**
     * Horizontal lines with the value on the left, from 0 up to axisMax.
     */
    private void drawGridLines(Graphics2D g2, int plotWidth, int plotHeight, int axisMax, int step) {
        FontMetrics metrics = g2.getFontMetrics();
        int bottom = PADDING_TOP + plotHeight;

        for (int value = 0; value <= axisMax; value += step) {
            int y = bottom - value * plotHeight / axisMax;
            g2.setColor(value == 0 ? Theme.TEXT_MUTED : Theme.SECONDARY_COLOR);
            g2.drawLine(PADDING_LEFT, y, PADDING_LEFT + plotWidth, y);

            String label = String.valueOf(value);
            g2.setColor(Theme.TEXT_MUTED);
            g2.drawString(label, PADDING_LEFT - 8 - metrics.stringWidth(label), y + metrics.getAscent() / 2 - 1);
        }
    }

    /**
     * One bar per month, centered in its slot, with the value above it.
     */
    private void drawBars(Graphics2D g2, int plotWidth, int plotHeight, int axisMax) {
        FontMetrics metrics = g2.getFontMetrics();
        int slotWidth = plotWidth / 12;
        int barWidth = slotWidth * 3 / 5;
        int bottom = PADDING_TOP + plotHeight;

        for (int month = 0; month < 12; month++) {
            int slotX = PADDING_LEFT + month * slotWidth;
            int barX = slotX + (slotWidth - barWidth) / 2;
            int barHeight = values[month] * plotHeight / axisMax;

            g2.setColor(Theme.PRIMARY_COLOR);
            g2.fillRect(barX, bottom - barHeight, barWidth, barHeight);

            if (values[month] > 0) {
                String valueText = String.valueOf(values[month]);
                g2.setColor(Theme.TEXT_DARK);
                g2.drawString(valueText, barX + (barWidth - metrics.stringWidth(valueText)) / 2,
                        bottom - barHeight - 4);
            }

            String monthText = "T" + (month + 1);
            g2.setColor(Theme.TEXT_MUTED);
            g2.drawString(monthText, slotX + (slotWidth - metrics.stringWidth(monthText)) / 2,
                    bottom + metrics.getAscent() + 6);
        }
    }

    /**
     * Largest of the 12 values, but at least 1: bar heights divide by it,
     * and a year with no data must not divide by zero.
     */
    private int getMaxValue() {
        int max = 1;
        for (int value : values) {
            max = Math.max(max, value);
        }
        return max;
    }

    /**
     * Picks a round step so the chart gets about 4 to 6 grid lines.
     */
    private int getGridStep(int max) {
        int[] steps = {1, 2, 5, 10, 20, 50, 100, 200, 500, 1000};
        for (int step : steps) {
            if (max / step <= 6) {
                return step;
            }
        }
        return 1000;
    }
}
