/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.functions;

import model.PurchaseOrder;
import model.Item;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
                String line = String.format(
                    "Item ID: %s, Item Name: %s, Price: %.2f, Category: %s, Expired Date: %s, Supplier ID: %s, Total Stock: %d, Updated Date: %s",
                    item.getItemID(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getCategory(),
                    item.getExpiredDate(),
                    item.getSupplierID(),
                    item.getTotalStock(),
                    item.getUpdatedDate()
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    
    // Load POs from txtFile/po.txt
    public static List<PurchaseOrder> loadPOsFromFile(String filePath) {
        List<PurchaseOrder> poList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<Item> allItems = loadItemsFromFile("src/txtFile/items.txt"); 

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 8) {
                    String poID = parts[0].split(": ")[1].trim();
                    String supplierName = parts[1].split(": ")[1].trim();
                    String itemName = parts[2].split(": ")[1].trim();
                    String quantity = parts[3].split(": ")[1].trim();
                    String unitPrice = parts[4].split(": ")[1].trim();
                    String totalPrice = parts[5].split(": ")[1].trim();
                    String date = parts[6].split(": ")[1].trim();
                    String status = parts[7].split(": ")[1].trim();

                    PurchaseOrder po = new PurchaseOrder(poID, supplierName, itemName, quantity, unitPrice, totalPrice, date, status, "-");

                    // ✅ Find and attach matching item by name
                    List<Item> matchedItems = new ArrayList<>();
                    for (Item item : allItems) {
                        if (item.getItemName().equalsIgnoreCase(itemName)) {
                            matchedItems.add(item);
                            break;
                        }
                    }

                    po.setItems(matchedItems);
                    poList.add(po);
                }
            }
        } catch (IOException | NullPointerException e) {
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
