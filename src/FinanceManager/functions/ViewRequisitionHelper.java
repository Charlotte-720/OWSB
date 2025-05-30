/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author charl
 */ 
public class ViewRequisitionHelper {
    public static void loadPRTable(JTable table, String filePath, String statusFilter) {
        String[] columnNames = {
        "PR ID", "PR Type", "Item ID", "Item Name", "Quantity",
        "Unit Price", "Total Price", "Supplier ID", "Raised By",
        "Delivery Date", "Request Date", "Status"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] fields = line.split(", ");
                String[] rowData = new String[columnNames.length];
                String statusValue = "";

                for (int i = 0; i < fields.length && i < rowData.length; i++) {
                    String[] keyValue = fields[i].split(": ");
                    if (keyValue.length == 2) {
                        rowData[i] = keyValue[1].trim();
                        if (fields[i].startsWith("Status:")) {
                            statusValue = keyValue[1].trim();
                        }
                    }
                }

                // Apply filter (if not "All")
                if (statusFilter.equals("All") || statusValue.equalsIgnoreCase(statusFilter)) {
                    model.addRow(rowData);
                }
            }

            table.setModel(model);

        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Failed to load PR data.");
            e.printStackTrace();
        }
    }
}
