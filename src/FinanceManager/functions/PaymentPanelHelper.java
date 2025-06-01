/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.PurchaseOrder;

/**
 *
 * @author charl
 */
public class PaymentPanelHelper {
     public static ArrayList<PurchaseOrder> readPaidPOsFromFile(String path) {
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        Map<String, String> supplierMap = readSupplierMap("src/txtFile/suppliers.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                String poID = fields[0].split(": ")[1];
                String itemID = fields[1].split(": ")[1];
                String supplierID = fields[1].split(": ")[1];
                String item = fields[3].split(": ")[1];
                String quantity = fields[4].split(": ")[1];
                String unitPrice = fields[5].split(": ")[1];
                String totalPrice = fields[6].split(": ")[1];
                String date = fields[7].split(": ")[1];
                String status = fields[8].split(": ")[1];

                if ("Verified".equalsIgnoreCase(status)) {
                    String supplierName = supplierMap.getOrDefault(supplierID, "Unknown");
                    PurchaseOrder po = new PurchaseOrder(poID, supplierID, supplierName, itemID, item, quantity, unitPrice, totalPrice, date, status, "-");
                    poList.add(po);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return poList;
    }

    public static void updatePOStatusToPaid(String path, String poIDToUpdate) {
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("PO_ID: " + poIDToUpdate)) {
                    String[] parts = line.split(", ");
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].startsWith("Status:")) {
                            parts[i] = "Status: Paid";
                            break;
                        }
                    }
                    line = String.join(", ", parts); // Reconstruct the updated line
                }

                updatedLines.add(line); // Add each (modified or original) line to the list
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static Map<String, String> readSupplierMap(String filePath) {
        Map<String, String> supplierMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String supplierID = "", supplierName = "";
                String[] fields = line.split(", ");

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
