/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author charl
 */
public class StatusFormat {
     public static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String status = value != null ? value.toString() : "";

            switch (status.toLowerCase()) {
                case "pending":
                    cell.setBackground(new Color(255, 255, 204)); // Light Yellow
                    break;
                case "approved":
                    cell.setBackground(new Color(204, 255, 204)); // Light Green
                    break;
                case "rejected":
                    cell.setBackground(new Color(255, 204, 204)); // Light Red
                    break;
                case "verified":
                    cell.setBackground(new Color(183, 177, 242)); // Light purple
                    break;
                case "flagged":
                    cell.setBackground(new Color(255, 220, 220)); // Light pink
                    break;
                case "received":
                    cell.setBackground(new Color(255, 253, 246)); // 
                    break;
                case "paid":
                    cell.setBackground(new Color(204, 229, 255)); // Light Blue
                    break;
                default:
                    cell.setBackground(Color.WHITE);
            }

            // Maintain selection highlight
            if (isSelected) {
                cell.setBackground(table.getSelectionBackground());
                cell.setForeground(table.getSelectionForeground());
            } else {
                cell.setForeground(Color.BLACK);
            }

            return cell;
        }
    }
}
