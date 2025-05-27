package SalesManager;

import java.awt.Component;
import model.PurchaseRequisition;
import model.SalesRecord;
import model.Item;
import model.Supplier;
import model.PRItem;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FileHandler {

    private static final String ITEM_FILE = "src/txtFile/items.txt";
    private static final String SUPPLIER_FILE = "src/txtFile/suppliers.txt";
    private static final String SALES_FILE = "src/txtFile/sales_records.txt";
    private static final String PR_FILE = "src/txtFile/pr.txt";
    
    // Helper method to parse formatted line back to array
    private static String[] parseFormattedLine(String line, String[] expectedKeys) {
        String[] values = new String[expectedKeys.length];
        for (int i = 0; i < expectedKeys.length; i++) {
            String key = expectedKeys[i] + ": ";
            int startIndex = line.indexOf(key);
            if (startIndex != -1) {
                startIndex += key.length();
                int endIndex = line.indexOf(", ", startIndex);
                if (endIndex == -1) {
                    endIndex = line.length();
                }
                values[i] = line.substring(startIndex, endIndex).trim();
            } else {
                values[i] = "";
            }
        }
        return values;
    }
    
    public static List<String[]> readAllRecords(String filename) throws IOException {
        List<String[]> records = new ArrayList<>();
        if (!new File(filename).exists()) {
            return records;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Check if it's old format (comma-separated) or new format
                    if (line.contains(": ")) {
                        // New formatted style - need to determine which type based on content
                        if (line.contains("Item ID: ")) {
                            String[] keys = {"Item ID", "Item Name", "Price", "Category", "Expired Date", "Supplier ID", "Total Stock", "Updated Date"};
                            records.add(parseFormattedLine(line, keys));
                        } else if (line.contains("Supplier ID: ")) {
                            String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Active"};
                            records.add(parseFormattedLine(line, keys));
                        } else if (line.contains("Sale ID: ")) {
                            String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                            records.add(parseFormattedLine(line, keys));
                        } else if (line.contains("PR ID: ")) {
                            String[] keys = {"PR ID", "Raised By", "Request Date", "Required Delivery Date", "Status", "Item ID", "Quantity", "Unit Price", "Total Price", "Supplier ID"};
                            records.add(parseFormattedLine(line, keys));
                        }
                    } else {
                        // Old format - comma separated
                        records.add(line.split(","));
                    }
                }
            }
        }
        return records;
    }
    
    
    // ===================== ITEM METHODS =====================
    
    public static void saveItemToFile(Item item) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ITEM_FILE, true))) {
            writer.write(itemToFormattedString(item));
            writer.newLine();
        }
    }

    private static String itemToFormattedString(Item item) {
        return "Item ID: " + item.getItemID() + 
               ", Item Name: " + item.getItemName() + 
               ", Price: " + item.getPrice() +
               ", Category: " + item.getCategory() + 
               ", Expired Date: " + item.getExpiredDate() + 
               ", Supplier ID: " + item.getSupplierID() +
               ", Total Stock: " + item.getTotalStock() + 
               ", Updated Date: " + item.getUpdatedDate();
    }

    public static String generateItemID() throws IOException {
        int lastItemNumber = 0;

        if (!new File(ITEM_FILE).exists()) {
            return "ITM01";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 0) {
                    String itemID = "";
                    if (line.contains("Item ID: ")) {
                        int start = line.indexOf("Item ID: ") + 9;
                        int end = line.indexOf(",", start);
                        if (end == -1) end = line.length();
                        itemID = line.substring(start, end).trim();
                    } else {
                        // Old format fallback
                        String[] parts = line.split(",");
                        if (parts.length > 0) {
                            itemID = parts[0].trim();
                        }
                    }
                    
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

    public static boolean itemExists(String itemName) throws IOException {
        return loadAllItems().stream()
            .anyMatch(item -> item.getItemName().equalsIgnoreCase(itemName.trim()));
    }

    public static boolean isItemNameDuplicate(String itemName) throws IOException {
    if (itemName == null || itemName.trim().isEmpty()) {
        return false;
    }

    // Check in items.txt file
    if (new File(ITEM_FILE).exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String existingName = "";
                if (line.contains("Item Name: ")) {
                    int start = line.indexOf("Item Name: ") + 11;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    existingName = line.substring(start, end).trim();
                } else {
                    // Old format fallback
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 2) {
                        existingName = parts[1].trim();
                    }
                }
                
                if (existingName.equalsIgnoreCase(itemName.trim())) {
                    return true;
                }
            }
        }
    }

    // Also check in pr.txt file
    if (new File(PR_FILE).exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String existingItemName = "";
                if (line.contains("Item Name: ")) {
                    int start = line.indexOf("Item Name: ") + 11;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    existingItemName = line.substring(start, end).trim();
                }
                
                if (existingItemName.equalsIgnoreCase(itemName.trim())) {
                    return true;
                }
            }
        }
    }
    
    return false;
}

    public static Item getItemById(String itemId) throws IOException {
        if (itemId == null || itemId.trim().isEmpty()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String currentItemId = "";
                if (line.contains("Item ID: ")) {
                    int start = line.indexOf("Item ID: ") + 9;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    currentItemId = line.substring(start, end).trim();
                } else {
                    // Old format fallback
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 8) {
                        currentItemId = parts[0].trim();
                    }
                }
                
                if (currentItemId.equals(itemId)) {
                    String[] keys = {"Item ID", "Item Name", "Price", "Category", "Expired Date", "Supplier ID", "Total Stock", "Updated Date"};
                    String[] parts;
                    if (line.contains(": ")) {
                        parts = parseFormattedLine(line, keys);
                    } else {
                        parts = line.split(",", -1);
                    }
                    
                    if (parts.length >= 8) {
                        return new Item(
                            parts[0].trim(),
                            parts[1].trim(),
                            Double.parseDouble(parts[2].trim()),
                            parts[3].trim(),
                            LocalDate.parse(parts[4].trim()),
                            parts[5].trim(),
                            Integer.parseInt(parts[6].trim()),
                            LocalDate.parse(parts[7].trim())
                        );
                    }
                }
            }
        }
        return null;
    }

    public static Item loadItemData(String itemID) throws IOException {
        return getItemById(itemID);
    }

    public static List<Item> loadItemsFromFile() throws IOException {
        List<Item> items = new ArrayList<>();
        File file = new File(ITEM_FILE);

        if (!file.exists()) {
            return items;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] keys = {"Item ID", "Item Name", "Price", "Category", "Expired Date", "Supplier ID", "Total Stock", "Updated Date"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 8) {
                    Item item = new Item(
                        parts[0], 
                        parts[1],
                        Double.parseDouble(parts[2]),
                        parts[3],
                        LocalDate.parse(parts[4]),
                        parts[5],
                        Integer.parseInt(parts[6]),
                        LocalDate.parse(parts[7])
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    public static List<Item> loadAllItems() throws IOException {
        return loadItemsFromFile();
    }

    public static void saveAllItems(List<Item> items) throws IOException {
        try (FileWriter writer = new FileWriter(ITEM_FILE)) {
            for (Item item : items) {
                writer.write(itemToFormattedString(item) + "\n");
            }
        }
    }

    public static void deleteItemFromFile(String itemID) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String currentItemId = "";
                if (line.contains("Item ID: ")) {
                    int start = line.indexOf("Item ID: ") + 9;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    currentItemId = line.substring(start, end).trim();
                } else {
                    // Old format fallback
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 1) {
                        currentItemId = parts[0];
                    }
                }
                
                if (!currentItemId.equals(itemID)) {
                    lines.add(line);
                }
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ITEM_FILE))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        }
    }
    
    // ===================== SUPPLIER METHODS =====================
    
    public static void saveSupplier(Supplier supplier) throws IOException {
        try (FileWriter writer = new FileWriter(SUPPLIER_FILE, true)) {
            writer.write(supplierToFormattedString(supplier) + "\n");
        }
    }

    private static String supplierToFormattedString(Supplier supplier) {
        return "Supplier ID: " + supplier.getSupplierID() +
               ", Supplier Name: " + supplier.getSupplierName() +
               ", Contact No: " + supplier.getContactNo() +
                ", Supplies: " + supplier.getSupplies() +
               ", Active: " + supplier.isActive();
    }
    
    public static String generateSupplierID() throws IOException {
        int lastSupplierNumber = 0;
        
        if (!new File(SUPPLIER_FILE).exists()) {
            return "S01";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String supplierID = "";
                if (line.contains("Supplier ID: ")) {
                    int start = line.indexOf("Supplier ID: ") + 13;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    supplierID = line.substring(start, end).trim();
                } else {
                    // Old format fallback
                    String[] parts = line.split(",", -1);
                    if (parts.length > 0) {
                        supplierID = parts[0].trim();
                    }
                }
                
                if (supplierID.startsWith("S")) {
                    try {
                        int supplierNumber = Integer.parseInt(supplierID.substring(1));
                        if (supplierNumber > lastSupplierNumber) {
                            lastSupplierNumber = supplierNumber;
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        }
        
        return "S" + String.format("%02d", lastSupplierNumber + 1);
    }
    
    // Method to read suppliers from file
     public static List<Supplier> readSuppliersFromFile(String fileName) throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Supplies", "Active"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 5) {  
                    String supplierID = parts[0].trim();
                    String supplierName = parts[1].trim();
                    String contactNo = parts[2].trim();
                    String supplies = parts[3].trim();
                    boolean active = Boolean.parseBoolean(parts[4].trim());

                    Supplier supplier = new Supplier(supplierID, supplierName, contactNo, supplies, active);
                    suppliers.add(supplier);
                }
            }
        }
        return suppliers;
    }
    
    // Method to delete supplier from file
    public static void deleteSupplier(String supplierID) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                found = true;
                String currentSupplierID = "";
                if (line.contains("Supplier ID: ")) {
                    int start = line.indexOf("Supplier ID: ") + 13;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    currentSupplierID = line.substring(start, end).trim();
                } else {
                    String[] parts = line.split(",", -1);
                    if (parts.length > 0) {
                        currentSupplierID = parts[0];
                    }
                }
                
                if (!currentSupplierID.equals(supplierID)) {
                    lines.add(line);
                    continue;
                }
            }
        }

        try (FileWriter writer = new FileWriter(SUPPLIER_FILE)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }
    }
    
    // Method to add new supplier
    public static void addSupplier(String name, String contactNo, String supplies, boolean active) throws IOException {
        String supplierID = generateSupplierID();
        String supplierData = "Supplier ID: " + supplierID + 
                             ", Supplier Name: " + name + 
                             ", Contact No: " + contactNo + 
                             ", Supplies: " + supplies + 
                             ", Active: " + active;
        
        try (FileWriter writer = new FileWriter(SUPPLIER_FILE, true)) {
            writer.write(supplierData + "\n");
        }
    }


    
    public static void updateSupplier(String supplierID, String name, String contactNo, String supplies, boolean active) throws IOException {
        System.out.println("FileHandler: Updating supplier: ID=" + supplierID + ", Name=" + name);
        
        List<String> lines = Files.readAllLines(Paths.get(SUPPLIER_FILE));
        System.out.println("Read " + lines.size() + " lines from supplier file");

        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String currentSupplierID = "";
            
            if (line.contains("Supplier ID: ")) {
                int start = line.indexOf("Supplier ID: ") + 13;
                int end = line.indexOf(",", start);
                if (end == -1) end = line.length();
                currentSupplierID = line.substring(start, end).trim();
            } else {
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    currentSupplierID = parts[0];
                }
            }
            
            System.out.println("Checking line " + i + ": " + line);
            
            if (currentSupplierID.equals(supplierID)) {
                System.out.println("Found matching supplier ID at line " + i);
                String updatedLine = "Supplier ID: " + supplierID + 
                                   ", Supplier Name: " + name + 
                                   ", Contact No: " + contactNo + 
                                    ", Supplies: " + supplies + 
                                   ", Active: " + active;
                lines.set(i, updatedLine);
                System.out.println("Updated line to: " + updatedLine);
                found = true;
                break;
            }
        }

        if (found) {
            System.out.println("Writing " + lines.size() + " lines back to file");
            Files.write(Paths.get(SUPPLIER_FILE), lines);
            System.out.println("File updated successfully");
        } else {
            System.out.println("Supplier with ID " + supplierID + " not found in file");
            throw new IOException("Supplier with ID " + supplierID + " not found");
        }
    }
    
    // Method to get a single supplier by ID
    public static Supplier getSupplierById(String supplierID) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Supplies", "Active"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 5 && parts[0].trim().equals(supplierID)) {
                    return new Supplier(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Boolean.parseBoolean(parts[4].trim())
                    );
                }
            }
        }
        return null;
    }

    public static List<String> loadSupplierIDs() throws IOException {
        List<String> supplierIDs = new ArrayList<>();
        if (!new File(SUPPLIER_FILE).exists()) {
            return supplierIDs;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String supplierID = "";
                if (line.contains("Supplier ID: ")) {
                    int start = line.indexOf("Supplier ID: ") + 13;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    supplierID = line.substring(start, end).trim();
                } else {
                    String[] parts = line.split(",", -1);
                    if (parts.length > 0) {
                        supplierID = parts[0].trim();
                    }
                }
                
                if (!supplierID.isEmpty()) {
                    supplierIDs.add(supplierID);
                }
            }
        }
        return supplierIDs;
    }

    public static List<Supplier> loadAllSuppliers() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();

        if (!new File(SUPPLIER_FILE).exists()) {
            return suppliers;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Supplies", "Active"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 5) {  
                    suppliers.add(new Supplier(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        Boolean.parseBoolean(parts[4].trim())
                    ));
                }
            }
        }
        return suppliers;
    }

    public static boolean isSupplierNameDuplicate(String supplierName, String currentSupplierID) throws IOException {
        if (!new File(SUPPLIER_FILE).exists()) {
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String existingSupplierID = "";
                String existingSupplierName = "";
                
                if (line.contains(": ")) {
                    String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Supplies", "Active"};
                    String[] parts = parseFormattedLine(line, keys);
                    if (parts.length > 1) {
                        existingSupplierID = parts[0].trim();
                        existingSupplierName = parts[1].trim();
                    }
                } else {
                    String[] parts = line.split(",", -1);
                    if (parts.length > 1) {
                        existingSupplierID = parts[0].trim();
                        existingSupplierName = parts[1].trim();
                    }
                }
                
                if (currentSupplierID != null && existingSupplierID.equals(currentSupplierID)) {
                    continue;
                }
                if (existingSupplierName.equalsIgnoreCase(supplierName.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

// ===================== SALES RECORD METHODS =====================
   

public static void saveSalesRecord(SalesRecord sale) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_FILE, true))) {
            String formattedSale = "Sale ID: " + sale.getSaleID() +
                                 ", Item ID: " + sale.getItemID() +
                                 ", Quantity Sold: " + sale.getQuantitySold() +
                                 ", Sale Date: " + sale.getSaleDate() +
                                 ", Total Amount: " + String.format("%.2f", sale.getTotalAmount());
            writer.write(formattedSale);
            writer.newLine();
        }
    }


    public static String generateSalesID() throws IOException {
        int lastNumber = 0;
        
        if (!new File(SALES_FILE).exists()) {
            return "SAL001";
        }

        List<String[]> records = readAllRecords(SALES_FILE);
        for (String[] record : records) {
            if (record.length > 0 && record[0].startsWith("SAL")) {
                try {
                    int num = Integer.parseInt(record[0].substring(3));
                    lastNumber = Math.max(lastNumber, num);
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return String.format("SAL%03d", lastNumber + 1);
    }
    
    public static List<String[]> readSalesRecords() throws IOException {
    List<String[]> records = new ArrayList<>();
    if (!new File(SALES_FILE).exists()) {
        return records;
    }
    
    try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                    String[] parts;
                    
                    if (line.contains(": ")) {
                        parts = parseFormattedLine(line, keys);
                    } else {
                        parts = line.split(",");
                    }
                    
                    if (parts.length >= 4) {
                        records.add(parts);
                    }
                }
            }
        }
        return records;
    }

    public static void updateItemStockAfterSale(String itemID, int quantitySold) throws IOException {
    // Load all items
    List<Item> items = loadAllItems();
    boolean itemFound = false;
    
    // Step 2: Find and update the specific item
    for (Item item : items) {
        if (item.getItemID().equals(itemID)) {
            int oldStock = item.getTotalStock();
            int newStock = oldStock - quantitySold;
            
            item.setTotalStock(newStock);
            itemFound = true;
            break;
        }
    }
    
    if (!itemFound) {
        throw new IOException("Item not found: " + itemID);
    }
    
    // Save all items back to file
    saveAllItems(items);
}

// ===================== PURCHASE REQUISITION METHODS =====================

public static void savePurchaseRequisition(PurchaseRequisition pr) throws IOException {
    if (pr == null || pr.getItems() == null || pr.getItems().isEmpty()) {
        throw new IllegalArgumentException("Purchase requisition must have at least one item");
    }
    
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(PR_FILE, true))) {
        // Write each item as a single line with all PR and item information including item name
        for (PRItem item : pr.getItems()) {
            String itemData = "PR ID: " + pr.getPrID() +
                            ", Item ID: " + item.getItemID() +
                            ", Item Name: " + item.getItemName() +  // Add item name here
                            ", Quantity: " + item.getQuantity() +
                            ", Unit Price: " + String.format("%.2f", item.getUnitPrice()) +
                            ", Total Price: " + String.format("%.2f", item.getTotalPrice()) +
                            ", Supplier ID: " + item.getSupplierID() +
                            ", Raised By: " + pr.getRaisedBy() +
                            ", Required Delivery Date: " + item.getRequiredDeliveryDate() +
                            ", Request Date: " + pr.getRequestDate() +
                            ", Status: " + pr.getStatus();
            writer.write(itemData);
            writer.newLine();
        }
    }
}

    public static String generatePRID() throws IOException {
    int lastPRNumber = 0;

    if (!new File(PR_FILE).exists()) {
        return "PR001";
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String prID = "";
            if (line.contains("PR ID: ")) {
                int start = line.indexOf("PR ID: ") + 7;
                int end = line.indexOf(",", start);
                if (end == -1) end = line.length();
                prID = line.substring(start, end).trim();
            } else {
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    prID = parts[0];
                }
            }
            
            if (prID.startsWith("PR")) {
                try {
                    int prNumber = Integer.parseInt(prID.substring(2));
                    if (prNumber > lastPRNumber) {
                        lastPRNumber = prNumber;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }

    return String.format("PR%03d", lastPRNumber + 1);
}

    public static List<String[]> readPurchaseRequisitions() throws IOException {
    List<String[]> records = new ArrayList<>();
    if (!new File(PR_FILE).exists()) {
        return records;
    }

     try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] keys = {"PR ID", "Item ID", "Item Name", "Quantity", "Unit Price", "Total Price", "Supplier ID", "Raised By", "Required Delivery Date", "Request Date", "Status"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 11) {
                    records.add(parts);
                }
            }
    }
    return records;
}}
    
    public static List<String[]> readPurchaseRequisitionsForTable() throws IOException {
        List<String[]> tableRecords = new ArrayList<>();
        List<String[]> allRecords = readPurchaseRequisitions();

        for (String[] record : allRecords) {
            if (record.length >= 11) {
                String[] tableRow = new String[10];  
                tableRow[0] = record[0]; // PR ID
                tableRow[1] = record[1]; // Item ID
                tableRow[2] = record[3]; // Quantity
                tableRow[3] = record[4]; // Unit Price
                tableRow[4] = record[5]; // Total Price
                tableRow[5] = record[8]; // Required Delivery Date
                tableRow[6] = record[6]; // Supplier ID
                tableRow[7] = record[9]; // Request Date
                tableRow[8] = record[10]; // Status
                
                tableRecords.add(tableRow);
            }
        }

        return tableRecords;
    }

public static boolean deletePurchaseRequisition(String prID) throws IOException {
    if (!new File(PR_FILE).exists()) {
        return false;
    }

    List<String> linesToKeep = new ArrayList<>();
    boolean found = false;

    try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String currentPRID = "";
            if (line.contains("PR ID: ")) {
                int start = line.indexOf("PR ID: ") + 7;
                int end = line.indexOf(",", start);
                if (end == -1) end = line.length();
                currentPRID = line.substring(start, end).trim();
            } else {
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    currentPRID = parts[0].trim();
                }
            }
            
            if (!currentPRID.equals(prID)) {
                linesToKeep.add(line);
            } else {
                found = true;
            }
        }
    }

    // Only rewrite the file if we found and removed the PR
    if (found) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PR_FILE))) {
            for (String line : linesToKeep) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    return found;
}

    public static List<PurchaseRequisition> loadAllPurchaseRequisitions() throws IOException {
        List<PurchaseRequisition> prList = new ArrayList<>();

        if (!new File(PR_FILE).exists()) {
            return prList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            PurchaseRequisition currentPR = null;
            List<PRItem> currentItems = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                // Parse the line to determine if it's a header or item line
                String[] keys;
                String[] parts;

                if (line.contains("Raised By: ")) {
                    // This is a PR header line
                    keys = new String[]{"PR ID", "Raised By", "Request Date", "Required Delivery Date", "Status"};
                    parts = parseFormattedLine(line, keys);

                    // Save previous PR if exists
                    if (currentPR != null) {
                        currentPR.setItems(new ArrayList<>(currentItems));
                        prList.add(currentPR);
                        currentItems.clear();
                    }

                    if (parts.length >= 5) {
                        currentPR = new PurchaseRequisition(
                            parts[0].trim(), // prID
                            parts[1].trim(), // raisedBy
                            LocalDate.parse(parts[2].trim()), // requestDate
                            LocalDate.parse(parts[3].trim()), // requiredDeliveryDate
                            parts[4].trim(), // status
                            new ArrayList<>() // items will be set later
                        );
                    }
                } else if (line.contains("Item ID: ") && currentPR != null) {
                    // This is a PR item line - updated to include Item Name
                    keys = new String[]{"PR ID", "Item ID", "Item Name", "Quantity", "Unit Price", "Total Price", "Supplier ID", "Required Delivery Date"};
                    parts = parseFormattedLine(line, keys);

                    if (parts.length >= 8) {
                        PRItem item = new PRItem(
                            parts[1].trim(), // itemID
                            Integer.parseInt(parts[3].trim()), // quantity
                            parts[6].trim(), // supplierID
                            parts[2].trim(), // itemName
                            Double.parseDouble(parts[4].trim()), // unitPrice
                            LocalDate.parse(parts[7].trim()) // requiredDeliveryDate
                        );
                        currentItems.add(item);
                    }
                }
            }

            // Add the last PR if exists
            if (currentPR != null) {
                currentPR.setItems(new ArrayList<>(currentItems));
                prList.add(currentPR);
            }
        }

        return prList;
    }
    
    // Method to get a specific PR by ID
    public static PurchaseRequisition getPurchaseRequisitionByID(String prID) throws IOException {
    List<PurchaseRequisition> allPRs = loadAllPurchaseRequisitions();
    for (PurchaseRequisition pr : allPRs) {
        if (pr.getPrID().equals(prID)) {
            return pr;
        }
    }
    return null;
}

// Write lines to a file
public static void writeLinesToFile(String fileName, List<String> lines) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    }
}

public static String[] getPurchaseRequisitionById(String prID) throws IOException {
    if (!new File(PR_FILE).exists()) {
        return null;
    }
    
    try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            
            String currentPRID = "";
            if (line.contains("PR ID: ")) {
                int start = line.indexOf("PR ID: ") + 7;
                int end = line.indexOf(",", start);
                if (end == -1) end = line.length();
                currentPRID = line.substring(start, end).trim();
            } else {
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    currentPRID = parts[0].trim();
                }
            }
            
            if (currentPRID.equals(prID)) {
                String[] keys = {"PR ID", "Item ID","Quantity", "Unit Price", "Total Price", "Supplier ID", "Raised By", "Required Delivery Date", "Request Date", "Status"};
                if (line.contains(": ")) {
                    return parseFormattedLine(line, keys);
                } else {
                    return line.split(",", -1);
                }
            }
        }
    }
    return null;
}

public static boolean updatePurchaseRequisition(String prID, String itemID, int quantity, 
    double price, double totalPrice, LocalDate creationDate, String supplierID, 
    LocalDate requiredDeliveryDate, String status) throws IOException {

    List<String> lines = new ArrayList<>();
    boolean found = false;
    
    try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String currentPRID = "";
            if (line.contains("PR ID: ")) {
                int start = line.indexOf("PR ID: ") + 7;
                int end = line.indexOf(",", start);
                if (end == -1) end = line.length();
                currentPRID = line.substring(start, end).trim();
            } else {
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    currentPRID = parts[0].trim();
                }
            }
            
            if (currentPRID.equals(prID)) {
                found = true;
                // Create updated line in new formatted style
                line = "PR ID: " + prID +
                      ", Item ID: " + itemID +
                      ", Quantity: " + quantity +
                      ", Unit Price: " + String.format("%.2f", price) +
                      ", Total Price: " + String.format("%.2f", totalPrice) +
                      ", Supplier ID: " + supplierID +
                      ", Raised By: " + "Updated" + // You may want to get this from existing data
                      ", Required Delivery Date: " + requiredDeliveryDate +
                      ", Request Date: " + creationDate +
                      ", Status: " + status;
            }
            lines.add(line);
        }
    }
    
    if (found) {
        writeLinesToFile(PR_FILE, lines);
        return true;
    }
    
    return false;
}
    public static List<Supplier> loadActiveSuppliers() throws IOException {
        List<Supplier> activeSuppliers = new ArrayList<>();
        List<Supplier> allSuppliers = loadAllSuppliers();

        for (Supplier supplier : allSuppliers) {
            if (supplier.isActive()) {
                activeSuppliers.add(supplier);
            }
        }

        return activeSuppliers;
    }
    
    public static boolean updatePRStatus(String prID, String newStatus) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String currentPRID = "";
                if (line.contains("PR ID: ")) {
                    int start = line.indexOf("PR ID: ") + 7;
                    int end = line.indexOf(",", start);
                    if (end == -1) end = line.length();
                    currentPRID = line.substring(start, end).trim();
                } else {
                    String[] parts = line.split(",", -1);
                    if (parts.length > 0) {
                        currentPRID = parts[0].trim();
                    }
                }

                if (currentPRID.equals(prID)) {
                    found = true;

                    // Parse existing line to get current values
                    String[] keys = {"PR ID", "Item ID", "Quantity", "Unit Price", "Total Price", "Supplier ID", "Raised By", "Required Delivery Date", "Request Date", "Status"};
                    String[] parts;

                    if (line.contains(": ")) {
                        parts = parseFormattedLine(line, keys);
                    } else {
                        parts = line.split(",", -1);
                    }

                    if (parts.length >= 10) {
                        // Reconstruct line with new status in the new format
                        line = "PR ID: " + parts[0] +
                              ", Item ID: " + parts[1] +
                              ", Quantity: " + parts[2] +
                              ", Unit Price: " + parts[3] +
                              ", Total Price: " + parts[4] +
                              ", Supplier ID: " + parts[5] +
                              ", Raised By: " + parts[6] +
                              ", Required Delivery Date: " + parts[7] +
                              ", Request Date: " + parts[8] +
                              ", Status: " + newStatus;
                    }
                }
                lines.add(line);
            }
        }

        if (found) {
            writeLinesToFile(PR_FILE, lines);
            return true;
        }

        return false;
    }
    //-----------------monthly sales report----------------
    
    public static List<Item> getMonthlySalesItems() throws IOException {
        return loadAllItems();
    }

    
    public static List<String[]> getMonthlySalesRecords() throws IOException {
        List<String[]> records = new ArrayList<>();
        if (!new File(SALES_FILE).exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                    String[] parts;
                    
                    if (line.contains(": ")) {
                        parts = parseFormattedLine(line, keys);
                    } else {
                        parts = line.split(",", -1);
                    }
                    
                    if (parts.length >= 5) {
                        // Make sure the sales record contains all needed fields:
                        // SaleID, ItemID, Quantity, Date, TotalAmount
                        records.add(parts);
                    }
                }
            }
        }
        return records;
    }
}

