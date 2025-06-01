package SalesManager.DataHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import model.Item;

public class ItemFileHandler extends BaseFileHandler {
    private static final String ITEM_FILE = "src/txtFile/items.txt";
    private static final String[] ITEM_KEYS = {
        "Item ID", "Item Name", "Price", "Category", 
        "Expired Date", "Supplier ID", "Total Stock", "Updated Date"
    };

// save single item
     public static void saveItem(Item item) throws IOException {
        validateItemForSave(item);
        ensureFileExists();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ITEM_FILE, true))) {
            writer.write(itemToFormattedString(item));
            writer.newLine();
        }
    }
     
// save all items
     public static void saveAllItems(List<Item> items) throws IOException {
        ensureDirectoryExists();
        try (FileWriter writer = new FileWriter(ITEM_FILE)) {
            for (Item item : items) {
                writer.write(itemToFormattedString(item) + "\n");
            }
        }
    }
     
// load all items
     
public static List<Item> loadAllItems() throws IOException {
        List<Item> items = new ArrayList<>();
        File file = new File(ITEM_FILE);

        if (!file.exists()) {
            System.out.println("Items file does not exist: " + ITEM_FILE);
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;
                
                try {
                    Item item = parseItemFromLine(line);
                    if (item != null) {
                        items.add(item);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
        
        System.out.println("Loaded " + items.size() + " items from file");
        return items;
    }

// update existing item
     public static void updateItem(Item updatedItem) throws IOException {
        validateItemForSave(updatedItem);
        
        List<Item> items = loadAllItems();
        boolean itemFound = false;
        
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemID().equals(updatedItem.getItemID())) {
                items.set(i, updatedItem);
                itemFound = true;
                break;
            }
        }
        
        if (!itemFound) {
            throw new IOException("Item not found: " + updatedItem.getItemID());
        }
        
        saveAllItems(items);
    }
     
// delete item
     public static void deleteItem(String itemID) throws IOException {
        if (itemID == null || itemID.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }

        List<Item> items = loadAllItems();
        boolean itemFound = items.removeIf(item -> item.getItemID().equals(itemID));
        
        if (!itemFound) {
            throw new IOException("Item with ID " + itemID + " not found");
        }
        
        saveAllItems(items);
    }
     
// generate item id
     public static String generateItemID() throws IOException {
        int lastItemNumber = 0;
        ensureFileExists();

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    String itemID = extractItemID(line);
                    
                    if (itemID.startsWith("ITM")) {
                        try {
                            int itemNumber = Integer.parseInt(itemID.substring(3));
                            if (itemNumber > lastItemNumber) {
                                lastItemNumber = itemNumber;
                            }
                        } catch (NumberFormatException e) {
                            // Skip invalid ID
                        }
                    }
                }
            }
        }
        lastItemNumber++;
        return "ITM" + String.format("%02d", lastItemNumber);
    }
     
// load po item
     public static List<POItem> loadPaidPOItems() throws IOException {
        List<POItem> paidItems = new ArrayList<>();
        File poFile = new File("src/txtFile/po.txt");
        
        if (!poFile.exists()) {
            System.out.println("PO file does not exist: " + "src/txtFile/po.txt");
            return paidItems;
        }
        
        // Define the keys for PO file format
        String[] PO_KEYS = {
            "PO_ID", "Item ID","Supplier ID", "Item Name", "Quantity", 
            "Unit Price", "Total Price", "Date", "Status"
        };
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/po.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                try {
                    // Use the inherited parseFormattedLine method
                    String[] values = parseFormattedLine(line, PO_KEYS);
                    
                    if (values.length >= 9) {
                        String status = values[8].trim(); 
                        
                        if (status.equalsIgnoreCase("Paid")) {
                            String supplierID  = values[2].trim(); // Supplier ID
                            String itemName = values[3].trim();     // Item
                            int quantity = Integer.parseInt(values[4].trim()); // Quantity
                            double unitPrice = Double.parseDouble(values[5].trim()); // Unit Price
                            
                            POItem item = new POItem(
                                itemName,           // itemName
                                quantity,           // itemQuantity
                                unitPrice,          // itemPrice
                                status,             // status
                                supplierID         // supplierID 
                            );
                            paidItems.add(item);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing PO line: " + line);
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }
        System.out.println("Loaded " + paidItems.size() + " paid PO items");
        return paidItems;
    }
     
     // ================== PRIVATE HELPER METHODS ==================
    
    private static void ensureDirectoryExists() {
        File dir = new File(ITEM_FILE);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    private static void ensureFileExists() throws IOException {
        ensureDirectoryExists();
        File file = new File(ITEM_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
    
    private static String itemToFormattedString(Item item) {
        return "Item ID: " + item.getItemID() + 
               ", Item Name: " + item.getItemName() + 
               ", Price: " + String.format("%.2f", item.getPrice()) +
               ", Category: " + item.getCategory() + 
               ", Expired Date: " + item.getExpiredDate() + 
               ", Supplier ID: " + item.getSupplierID() +
               ", Total Stock: " + item.getTotalStock() + 
               ", Updated Date: " + item.getUpdatedDate();
    }
    
    private static Item parseItemFromLine(String line) throws IOException {
        try {
            String[] parts;
            
            if (line.contains(": ")) {
                // New labeled format
                parts = parseFormattedLine(line, ITEM_KEYS);
            } else {
                // Old CSV format
                parts = line.split(",", -1);
            }
            
            if (parts.length >= 8) {
                return new Item(
                    parts[0].trim(),                           // itemID
                    parts[1].trim(),                           // itemName
                    Double.parseDouble(parts[2].trim()),       // price
                    parts[3].trim(),                           // category
                    LocalDate.parse(parts[4].trim()),          // expiredDate
                    parts[5].trim(),                           // supplierID
                    Integer.parseInt(parts[6].trim()),         // totalStock
                    LocalDate.parse(parts[7].trim())           // updatedDate
                );
            }
            return null;
        } catch (NumberFormatException | DateTimeParseException e) {
            throw new IOException("Error parsing item data from line: " + line, e);
        }
    }
    
    private static String extractItemID(String line) {
        if (line.contains("Item ID: ")) {
            int start = line.indexOf("Item ID: ") + 9;
            int end = line.indexOf(",", start);
            if (end == -1) end = line.length();
            return line.substring(start, end).trim();
        } else {
            // Old format fallback
            String[] parts = line.split(",");
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }
        return "";
    }
    
    private static void validateItemForSave(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (item.getItemID() == null || item.getItemID().trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }
        if (item.getItemName() == null || item.getItemName().trim().isEmpty()) {
            throw new IllegalArgumentException("Item name cannot be null or empty");
        }
        if (item.getPrice() < 0) {
            throw new IllegalArgumentException("Item price cannot be negative");
        }
        if (item.getTotalStock() < 0) {
            throw new IllegalArgumentException("Item stock cannot be negative");
        }
    }
    
     // ================== INNER CLASS FOR PO ITEMS ==================
    
    public static class POItem {
        public String itemName;
        public int itemQuantity;
        public double itemPrice;
        public String status;
        public String supplierID;
        
        public POItem(String itemName, int itemQuantity, double itemPrice, String status, String supplierID) {
            this.itemName = itemName;
            this.itemQuantity = itemQuantity;
            this.itemPrice = itemPrice;
            this.status = status;
            this.supplierID = supplierID;
        }
        
        @Override
        public String toString() {
            return String.format("POItem{name='%s', quantity=%d, price=%.2f, status='%s', supplier='%s'}", 
                               itemName, itemQuantity, itemPrice, status, supplierID);
        }
    }
     
}
