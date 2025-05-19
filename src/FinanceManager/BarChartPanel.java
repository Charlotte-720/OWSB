/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author charl
 */
public class BarChartPanel extends JPanel {

    private LinkedHashMap<String, Integer> data;

    public BarChartPanel(LinkedHashMap<String, Integer> data) {
        this.data = data;
        setBackground(Color.WHITE);

        // Ensure NetBeans respects size
        setPreferredSize(new Dimension(300, 140));
        setMinimumSize(new Dimension(300, 140));
        setMaximumSize(new Dimension(300, 140));
    }

    public void setData(LinkedHashMap<String, Integer> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Panel dimensions
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Layout padding
        int paddingLeft = 80;
        int paddingRight = 50; // increased to prevent cutoff
        int paddingTop = 10;
        int paddingBottom = 10;
        int spacing = 10;

        int totalBars = data.size();
        int availableHeight = panelHeight - paddingTop - paddingBottom - ((totalBars - 1) * spacing);
        int barHeight = availableHeight / totalBars;

        int maxBarWidth = panelWidth - paddingLeft - paddingRight;
        int maxValue = Collections.max(data.values());

        int y = paddingTop;

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue();

            // ✅ Safe bar width (max limit to avoid overflow)
            int rawWidth = (int) ((quantity / (double) maxValue) * maxBarWidth);
            int barWidth = Math.min(rawWidth, maxBarWidth - 10); // -10 ensures space for label

            // Draw bar fill
            g2.setColor(new Color(255, 153, 102));
            g2.fillRect(paddingLeft, y, barWidth, barHeight);

            // Draw border
            g2.setColor(Color.BLACK);
            g2.drawRect(paddingLeft, y, barWidth, barHeight);

            // Draw item name (left)
            g2.drawString(item, 10, y + barHeight - 5);

            // Draw quantity (right, after bar)
            g2.drawString(String.valueOf(quantity), paddingLeft + barWidth + 5, y + barHeight - 5);

            y += barHeight + spacing;
        }
    }
}
