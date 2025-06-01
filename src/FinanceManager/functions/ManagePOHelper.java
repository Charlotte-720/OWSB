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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import model.PurchaseOrder;

/**
 *
 * @author charl
 */
public class ManagePOHelper {
   public static ArrayList<PurchaseOrder> readPOFile(String filePath) {
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        Map<String, String> flaggedReasons = readFlaggedReasons("src/txtFile/flagReason.txt");
        Map<String, String> supplierMap = readSupplierMap("src/txtFile/suppliers.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                String poID = fields[0].split(": ")[1];
                String itemID = fields[1].split(": ")[1];
                String supplierID  = fields[2].split(": ")[1]; 
                String item = fields[3].split(": ")[1];
                String quantity = fields[4].split(": ")[1];
                String unitPrice = fields[5].split(": ")[1];    
                String totalPrice = fields[6].split(": ")[1];
                String date = fields[7].split(": ")[1];
                String status = fields[8].split(": ")[1];

                String supplierName = supplierMap.getOrDefault(supplierID, "Unknown");
                String flagReason = flaggedReasons.getOrDefault(poID, "-");

                poList.add(new PurchaseOrder(poID, supplierID, supplierName, itemID, item, quantity,
                                             unitPrice, totalPrice, date, status, flagReason));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return poList;
    }


    public static List<String> loadActiveSuppliersForItem(String itemName) {
        List<String> validSuppliers = new ArrayList<>();

        try (BufferedReader supplierReader = new BufferedReader(new FileReader("src/txtFile/suppliers.txt"));
             BufferedReader prReader = new BufferedReader(new FileReader("src/txtFile/pr.txt"))) {

            Map<String, String> idToName = new HashMap<>(); // Supplier ID -> Supplier Name
            Set<String> activeIDs = new HashSet<>();

            // Step 1: Read active suppliers and store mapping
            String line;
            while ((line = supplierReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String supplierID = "", supplierName = "", isActive = "";
                String[] parts = line.split(", ");
                for (String part : parts) {
                    if (part.startsWith("Supplier ID: ")) {
                        supplierID = part.substring("Supplier ID: ".length()).trim();
                    } else if (part.startsWith("Supplier Name: ")) {
                        supplierName = part.substring("Supplier Name: ".length()).trim();
                    } else if (part.startsWith("Active: ")) {
                        isActive = part.substring("Active: ".length()).trim();
                    }
                }

                if (isActive.equalsIgnoreCase("true")) {
                    activeIDs.add(supplierID);
                    idToName.put(supplierID, supplierName);
                }
            }

            // Step 2: Find supplier IDs that supply this item
            while ((line = prReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String supplierID = "", item = "";
                String[] parts = line.split(", ");
                for (String part : parts) {
                    if (part.startsWith("Supplier ID: ")) {
                        supplierID = part.substring("Supplier ID: ".length()).trim();
                    } else if (part.startsWith("Item Name: ")) {
                        item = part.substring("Item Name: ".length()).trim();
                    }
                }

                if (item.equalsIgnoreCase(itemName) && activeIDs.contains(supplierID)) {
                    String name = idToName.get(supplierID);
                    if (name != null && !validSuppliers.contains(name)) {
                        validSuppliers.add(name);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return validSuppliers;
    }



    public static void savePOData(List<List<Object>> tableData, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Object> row : tableData) {
                
                String line = String.format("PO_ID: %s, Item ID: %s, Supplier ID: %s, Item Name: %s, Quantity: %s, Unit Price: %s, Total Price: %s, Date: %s, Status: %s",
                        row.get(0), row.get(1), row.get(2), row.get(4), row.get(5), row.get(6), row.get(7), row.get(8), row.get(9));
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static Map<String, String> readFlaggedReasons(String filePath) {
        Map<String, String> flaggedMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Format: PO_ID: 11, Reason: Not fresh
                String[] parts = line.split(", ");
                if (parts.length == 2) {
                    String poIDPart = parts[0].trim();     // "PO_ID: 11"
                    String reasonPart = parts[1].trim();   // "Reason: Not fresh"

                    String poID = poIDPart.split(":")[1].trim();
                    String reason = reasonPart.split(":")[1].trim();

                    flaggedMap.put(poID, reason);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return flaggedMap;
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
                        String key = parts[0].trim();
                        String value = parts[1].trim();

                        if (key.equalsIgnoreCase("Supplier ID")) {
                            supplierID = value;
                        } else if (key.equalsIgnoreCase("Supplier Name")) {
                            supplierName = value;
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
