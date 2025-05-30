/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author charl
 */
public class VerifyInventoryHelper {
    public static void loadDeliveredInventory(JTable table, String filePath) {
        String[] columnNames = {
            "PO ID", "Supplier Name", "Item", "Quantity",
            "Unit Price", "Total Price", "Date", "Status"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] fields = line.split(", ");
                String[] rowData = new String[columnNames.length];
                String status = "";

                for (int i = 0; i < fields.length && i < rowData.length; i++) {
                    String[] keyValue = fields[i].split(": ");
                    if (keyValue.length == 2) {
                        rowData[i] = keyValue[1].trim();
                        if (fields[i].startsWith("Status:")) {
                            status = keyValue[1].trim();
                        }
                    }
                }

                if (status.equalsIgnoreCase("Received")) {
                    model.addRow(rowData);
                }
            }

            table.setModel(model);

        } catch (IOException e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Failed to load PO data.");
            e.printStackTrace();
        }
    }
    
    public static void updatePOStatus(String poID, String newStatus) {
        File inputFile = new File("src/txtFile/po.txt");
        File tempFile = new File("src/txtFile/temp_po.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                if (line.contains("PO_ID: " + poID + ",")) {
                    // Replace the Status value
                    line = line.replaceAll("Status: \\w+", "Status: " + newStatus);
                }

                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to update PO status.");
            return;
        }

        // Replace original file
        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(null, "Error saving updated PO file.");
        }
    }
    
    public static void logFlaggedPO(String poID, String reason) {
        String logEntry = "PO_ID: " + poID + ", Reason: " + reason;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/txtFile/flagReason.txt", true))) {
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to log flagged PO reason.");
        }
    }
}
