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
    public static final int LOW_STOCK_THRESHOLD = 10;
    
    
    // Load Items from txtFile/items.txt
    public static List<Item> loadItemsFromFile(String filePath) {
        List<Item> itemList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    itemList.add(new Item(
                        parts[0],
                        parts[1],
                        Double.parseDouble(parts[2]),
                        parts[3],
                        LocalDate.parse(parts[4]),
                        parts[5],
                        Integer.parseInt(parts[6]),
                        LocalDate.parse(parts[7])
                    ));
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
                String line = String.join(",",
                    item.getItemID(),
                    item.getItemName(),
                    String.valueOf(item.getPrice()),
                    item.getCategory(),
                    item.getExpiredDate().toString(),
                    item.getSupplierID(),
                    String.valueOf(item.getTotalStock()),
                    item.getUpdatedDate().toString()
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

                    PurchaseOrder po = new PurchaseOrder(poID, supplierName, itemName, quantity, unitPrice, totalPrice, date, status);

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
