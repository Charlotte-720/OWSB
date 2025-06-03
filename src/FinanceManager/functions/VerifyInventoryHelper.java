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
import java.util.HashMap;
import java.util.Map;
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
        Map<String, String> supplierMap = readSupplierMap("src/txtFile/suppliers.txt"); 

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] fields = line.split(", ");
                String poID = "", supplierID = "", supplierName = "", item = "", quantity = "",
                       unitPrice = "", totalPrice = "", date = "", status = "";

                for (String field : fields) {
                    String[] parts = field.split(": ");
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        switch (key) {
                            case "PO_ID": poID = value; break;
                            case "Supplier ID": supplierID = value; break;
                            case "Item Name": item = value; break;
                            case "Quantity": quantity = value; break;
                            case "Unit Price": unitPrice = value; break;
                            case "Total Price": totalPrice = value; break;
                            case "Date": date = value; break;
                            case "Status": status = value; break;
                        }
                    }
                }

                if (status.equalsIgnoreCase("Received")) {
                    supplierName = supplierMap.getOrDefault(supplierID, "Unknown");
                    model.addRow(new Object[]{poID, supplierName, item, quantity, unitPrice, totalPrice, date, status});
                }
            }

            table.setModel(model);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to load PO data.");
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
                    line = line.replaceAll("Status: [^,]+", "Status: " + newStatus);
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/txtFile/flagReason.txt", true))) {
        writer.write("PO_ID: " + poID + ", Reason: " + reason);
        writer.newLine(); 
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to log flagged PO reason.");
        }
    }
    
    public static Map<String, String> readSupplierMap(String filePath) {
        Map<String, String> supplierMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                String supplierID = "";
                String supplierName = "";

                for (String field : fields) {
                    String[] parts = field.split(": ");
                    if (parts.length == 2) {
                        if (parts[0].trim().equalsIgnoreCase("Supplier ID")) {
                            supplierID = parts[1].trim();
                        } else if (parts[0].trim().equalsIgnoreCase("Supplier Name")) {
                            supplierName = parts[1].trim();
                        }
                    }
                }

                if (!supplierID.isEmpty() && !supplierName.isEmpty()) {
                    supplierMap.put(supplierID, supplierName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return supplierMap;
    }

}
