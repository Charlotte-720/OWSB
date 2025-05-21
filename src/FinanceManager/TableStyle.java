/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author charl
 */
public class TableStyle {
    public static void styleTableHeader(JTable table, Color backgroundColor, Color textColor) {
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                java.awt.Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                comp.setBackground(backgroundColor);
                comp.setForeground(textColor);
                comp.setFont(comp.getFont().deriveFont(Font.BOLD));
                return comp;
            }
        });

        // Optional: force repaint
        header.repaint();
    }
}
