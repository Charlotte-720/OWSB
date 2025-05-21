/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.functions;

import InventoryManager.models.Item;
import InventoryManager.models.PurchaseOrder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author reymy
 */
public class InventoryService {
    public static List<Item> loadItemsFromFile(String filePath) {
        List<Item> itemList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    itemList.add(new Item(
                        parts[0], parts[1],
                        Double.parseDouble(parts[2]),
                        parts[3],
                        LocalDate.parse(parts[4]),
                        parts[5],
                        Integer.parseInt(parts[6]),
                        LocalDate.parse(parts[7])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }
    
    public static List<PurchaseOrder> loadPOsFromFile(String filePath) {
        List<PurchaseOrder> poList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String poID = parts[0];
                    String supplier = parts[1];
                    Date date = java.sql.Date.valueOf(parts[2]); // match java.util.Date
                    String status = parts[3];
                    String[] itemCodes = parts[4].split(",");

                    // Match item codes with Item objects
                    List<Item> allItems = loadItemsFromFile("items.txt");
                    
                    List<Item> poItems = new ArrayList<>();
                    for (String code : itemCodes) {
                        for (Item item : allItems) {
                            if (item.getItemID().equalsIgnoreCase(code.trim())) {
                                poItems.add(item);
                                break;
                            }
                        }
                    }

                    PurchaseOrder po = new PurchaseOrder(poID, supplier, date, status, poItems);
                    poList.add(po);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return poList;
    }
    
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
    
    public static void savePOsToFile(List<PurchaseOrder> poList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (PurchaseOrder po : poList) {
                StringBuilder itemCodes = new StringBuilder();
                List<Item> items = po.getItems();
                for (int i = 0; i < items.size(); i++) {
                    itemCodes.append(items.get(i).getItemID());
                    if (i < items.size() - 1) itemCodes.append(",");
                }

                String line = po.getPoID() + "|" +
                              po.getSupplier() + "|" +
                              new java.sql.Date(po.getDate().getTime()) + "|" +
                              po.getStatus() + "|" +
                              itemCodes.toString();

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
