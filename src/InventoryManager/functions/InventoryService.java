/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.functions;

import model.PurchaseOrder;
import model.Item;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author reymy
 */
public class InventoryService {
    
    
    // threshold for low stock alert
    public static int LOW_STOCK_THRESHOLD = 10;
    
    
    // Load Items from txtFile/items.txt
    public static List<Item> loadItemsFromFile(String filePath) {
        List<Item> itemList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 8) {
                    String itemID = parts[0].split(": ")[1].trim();
                    String itemName = parts[1].split(": ")[1].trim();
                    double price = Double.parseDouble(parts[2].split(": ")[1].trim());
                    String category = parts[3].split(": ")[1].trim();
                    LocalDate expiredDate = LocalDate.parse(parts[4].split(": ")[1].trim());
                    String supplierID = parts[5].split(": ")[1].trim();
                    int totalStock = Integer.parseInt(parts[6].split(": ")[1].trim());
                    LocalDate updatedDate = LocalDate.parse(parts[7].split(": ")[1].trim());

                    itemList.add(new Item(itemID, itemName, price, category, expiredDate, supplierID, totalStock, updatedDate));
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return itemList;
    }
    
    
    // Save items to txtFile/items.txt
    public static void saveItemsToFile(List<Item> items, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Item item : items) {
                // ✅ Keep the original stock value
                int originalStock = item.getTotalStock();
                int updatedStock = Math.max(originalStock, InventoryService.LOW_STOCK_THRESHOLD);

                // ✅ Only update the date if stock was changed
                LocalDate updatedDate = (updatedStock > originalStock) ? LocalDate.now() : item.getUpdatedDate();

                String line = String.format(
                    "Item ID: %s, Item Name: %s, Price: %.2f, Category: %s, Expired Date: %s, Supplier ID: %s, Total Stock: %d, Updated Date: %s",
                    item.getItemID(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getCategory(),
                    item.getExpiredDate(),
                    item.getSupplierID(),
                    updatedStock, // ✅ Only update stock if needed
                    updatedDate // ✅ Only update date if stock changed
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static Map<String, String> loadSupplierNames(String filePath) {
        Map<String, String> supplierMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 2) { // Ensure we get at least Supplier ID & Name
                    String supplierID = parts[0].split(": ")[1].trim();
                    String supplierName = parts[1].split(": ")[1].trim();
                    supplierMap.put(supplierID, supplierName); // Store Supplier ID -> Supplier Name
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return supplierMap;
    }


    
    // Load POs from txtFile/po.txt
    public static List<PurchaseOrder> loadPOsFromFile(String filePath) {
        List<PurchaseOrder> poList = new ArrayList<>();
        Map<String, String> supplierNames = loadSupplierNames("src/txtFile/suppliers.txt"); // Load supplier names

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Item> allItems = loadItemsFromFile("src/txtFile/items.txt");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length == 9) {
                    String poID = parts[0].split(": ")[1].trim();
                    String itemID = parts[1].split(": ")[1].trim();
                    String supplierID = parts[2].split(": ")[1].trim();
                    String itemName = parts[3].split(": ")[1].trim();
                    int quantity = Integer.parseInt(parts[4].split(": ")[1].trim());
                    double unitPrice = Double.parseDouble(parts[5].split(": ")[1].trim());
                    double totalPrice = Double.parseDouble(parts[6].split(": ")[1].trim());
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    String dateString = parts[7].split(": ")[1].trim();
                    LocalDate date;

                    try {
                        date = LocalDate.parse(dateString, formatter1); // Try yyyy-MM-dd
                    } catch (DateTimeParseException e) {
                        date = LocalDate.parse(dateString, formatter2); // Fall back to dd-MM-yyyy
                    }

                    String status = parts[8].split(": ")[1].trim();

                    // ✅ Fetch supplier name dynamically from suppliers.txt
                    String supplierName = supplierNames.getOrDefault(supplierID, "Unknown Supplier");

                    // Fetch supplier name dynamically
                    PurchaseOrder po = new PurchaseOrder(poID, supplierID, supplierName, itemID, itemName, String.valueOf(quantity), String.valueOf(unitPrice), String.valueOf(totalPrice), date.toString(), status, "-");

                    // ✅ Match items by Item ID instead of Item Name
                    List<Item> matchedItems = new ArrayList<>();
                    for (Item item : allItems) {
                        if (item.getItemID().equals(itemID)) {
                            matchedItems.add(item);
                            break;
                        }
                    }

                    po.setItems(matchedItems);
                    poList.add(po);
                }
            }
        } catch (IOException | NullPointerException | NumberFormatException e) {
            e.printStackTrace();
        }

        return poList;
    }





    // Save POs to src/txtFile/po.txt
    public static void savePOsToFile(List<PurchaseOrder> poList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PurchaseOrder po : poList) {
                // Write PO data in consistent format
                String line = String.format(
                    "PO_ID: %s, Supplier Name: %s, Item: %s, Quantity: %s, Unit Price: %s, Total Price: %s, Date: %s, Status: %s",
                    po.getPoID(),
                    po.getSupplierName(),
                    po.getItem(),
                    po.getQuantity(),
                    po.getUnitPrice(),
                    po.getTotalPrice(),
                    po.getDate(),
                    po.getStatus()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
