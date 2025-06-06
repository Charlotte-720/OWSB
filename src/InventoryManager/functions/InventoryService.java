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
    
    private static final String THRESHOLD_FILE = "src/txtFile/low_stock_threshold.txt";

    // Call this at startup (before opening your dashboard)
    public static int loadThresholdFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(THRESHOLD_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line.trim());
            }
        } catch (Exception e) {
            // If file doesn't exist or is invalid, fall back to default
        }
        return 10; // default
    }

    // Save threshold to file (called when updating)
    public static void saveThresholdToFile(int threshold) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(THRESHOLD_FILE))) {
            writer.write(String.valueOf(threshold));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update both field and file
    public static void setLowStockThreshold(int threshold) {
        LOW_STOCK_THRESHOLD = threshold;
        saveThresholdToFile(threshold);
    }
    
    // Get Item By ID
    public static Item getItemByID(String itemID, String filePath) {
        List<Item> items = loadItemsFromFile(filePath);
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                return item;
            }
        }
        return null; // Not found
    }
    
    //Update Item Stock by ID
    public static boolean updateItemStock(String itemID, int newStock, String filePath) {
        if (newStock < 0) return false;
        List<Item> items = loadItemsFromFile(filePath);
        boolean updated = false;
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                item.setTotalStock(newStock);
                item.setUpdatedDate(java.time.LocalDate.now());
                updated = true;
                break;
            }
        }
        if (updated) {
            saveItemsToFile(items, filePath);
        }
        return updated;
    }
    
    // Get Count of Items Below Threshold
    public static int getLowStockCount(List<Item> items) {
        int count = 0;
        for (Item item : items) {
            if (item.getTotalStock() < LOW_STOCK_THRESHOLD) {
                count++;
            }
        }
        return count;
    }
    
    //Get Count of Pending POs
    public static int getPendingPOCount(List<PurchaseOrder> poList) {
        int count = 0;
        for (PurchaseOrder po : poList) {
            if ("Pending".equalsIgnoreCase(po.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    //Confirm/Receive a PO (and Update Stock)
    public static boolean confirmAndReceivePO(String poID, String poFilePath, String itemFilePath) {
        List<PurchaseOrder> poList = loadPOsFromFile(poFilePath);
        List<Item> items = loadItemsFromFile(itemFilePath);
        boolean foundPO = false;
        for (PurchaseOrder po : poList) {
            if (po.getPoID().equals(poID) && "Approved".equalsIgnoreCase(po.getStatus())) {
                // Find item by ID and update stock
                for (Item item : items) {
                    if (item.getItemID().equals(po.getItemID())) {
                        int qtyToAdd = Integer.parseInt(po.getQuantity());
                        item.increaseStock(qtyToAdd);
                        item.setUpdatedDate(java.time.LocalDate.now());
                        break;
                    }
                }
                po.setStatus("Received");
                foundPO = true;
                break;
            }
        }
        if (foundPO) {
            saveItemsToFile(items, itemFilePath);
            savePOsToFile(poList, poFilePath);
        }
        return foundPO;
    }
    
    //Get Summary
    public static InventorySummary getInventorySummary(String itemFilePath, String poFilePath) {
        InventorySummary summary = new InventorySummary();
        List<Item> items = loadItemsFromFile(itemFilePath);
        List<PurchaseOrder> poList = loadPOsFromFile(poFilePath);
        summary.totalItems = items.size();
        summary.lowStockItems = getLowStockCount(items);
        summary.pendingPOs = getPendingPOCount(poList);
        summary.lastUpdated = java.time.LocalDateTime.now().toString();
        summary.currentThreshold = LOW_STOCK_THRESHOLD;
        return summary;
    }
    
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
                    String quantity = parts[4].split(": ")[1].trim();
                    String unitPrice = parts[5].split(": ")[1].trim();
                    String totalPrice = parts[6].split(": ")[1].trim();
                    String date = parts[7].split(": ")[1].trim();
                    String status = parts[8].split(": ")[1].trim();

                    String supplierName = supplierNames.getOrDefault(supplierID, "Unknown Supplier");

                    PurchaseOrder po = new PurchaseOrder(poID, supplierID, supplierName, itemID, itemName, quantity, unitPrice, totalPrice, date, status, "");

                    // Attach items by ID
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
                String line = String.format(
                    "PO_ID: %s, Item ID: %s, Supplier ID: %s, Item Name: %s, Quantity: %s, Unit Price: %s, Total Price: %s, Date: %s, Status: %s",
                    po.getPoID(),
                    po.getItemID(),
                    po.getSupplierID(),
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

    
    // Returns items below the given threshold
    public static List<Item> getLowStockItems(String filePath, int threshold) {
        List<Item> allItems = loadItemsFromFile(filePath);
        List<Item> lowStockItems = new ArrayList<>();
        for (Item item : allItems) {
            if (item.getTotalStock() < threshold) {
                lowStockItems.add(item);
            }
        }
        return lowStockItems;
    }

    
    
}
