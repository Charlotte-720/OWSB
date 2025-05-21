package SalesManager;

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

public class FileHandler {

    private static final String ITEM_FILE = "items.txt";
    private static final String SUPPLIER_FILE = "suppliers.txt";
    private static final String SALES_FILE = "sales_records.txt";
    private static final String PR_FILE = "pr.txt";

    public static List<String[]> readAllRecords(String filename) throws IOException {
        List<String[]> records = new ArrayList<>();
        if (!new File(filename).exists()) {
            return records;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(line.split(","));
                }
            }
        }
        return records;
    }
    
    
    // ===================== ITEM METHODS =====================
    
    public static void saveItemToFile(Item item) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ITEM_FILE, true))) {
            writer.write(itemToFileString(item));
            writer.newLine();
        }
    }

    private static String itemToFileString(Item item) {
        return item.getItemID() + "," + item.getItemName() + "," + item.getPrice() + ","
                + item.getCategory() + "," + item.getExpiredDate() + "," + item.getSupplierID() + ","
                + item.getTotalStock() + "," + item.getUpdatedDate();
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
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        String itemID = parts[0].trim();
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

        if (!new File(ITEM_FILE).exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length >= 2) {
                    String existingName = parts[1].trim();
                    if (existingName.equalsIgnoreCase(itemName.trim())) {
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
                String[] parts = line.split(",", -1);
                if (parts.length >= 8 && parts[0].trim().equals(itemId)) {
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
        return null;
    }

    public static Item loadItemData(String itemID) throws IOException {
        if (itemID == null || itemID.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty");
        }

        if (!new File(ITEM_FILE).exists()) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(",", -1);
                if (parts.length >= 8 && parts[0].trim().equals(itemID)) {
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
        return null;
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
                String[] parts = line.split(",", -1);
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
                writer.write(itemToFileString(item) + "\n");
            }
        }
    }

    public static void deleteItemFromFile(String itemID) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(ITEM_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 1 && !parts[0].equals(itemID)) {
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
            writer.write(supplierToString(supplier) + "\n");
        }
    }

    private static String supplierToString(Supplier supplier) {
        return String.format("%s,%s,%s,%b",
            supplier.getSupplierID(),
            supplier.getSupplierName(),
            supplier.getContactNo(),
            supplier.isActive()
        );
    }
    
    public static String generateSupplierID() throws IOException {
        int lastSupplierNumber = 0;
        
        if (!new File(SUPPLIER_FILE).exists()) {
            return "S01"; // First ID if file doesn't exist
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    String supplierID = parts[0].trim();
                    if (supplierID.startsWith("S")) {
                        try {
                            int supplierNumber = Integer.parseInt(supplierID.substring(1));
                            if (supplierNumber > lastSupplierNumber) {
                                lastSupplierNumber = supplierNumber;
                            }
                        } catch (NumberFormatException e) {
                            continue; // Skip invalid IDs
                        }
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
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {  
                    String supplierID = parts[0].trim();
                    String supplierName = parts[1].trim();
                    String contactNo = parts[2].trim();
                    boolean active = Boolean.parseBoolean(parts[3].trim());

                    Supplier supplier = new Supplier(supplierID, supplierName, contactNo, active);
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
        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_FILE))) {  // Changed to use SUPPLIER_FILE constant
            String line;
            while ((line = br.readLine()) != null) {
                found = true;
                String[] parts = line.split(",", -1);
                if (parts.length > 0 && !parts[0].equals(supplierID)) {
                    lines.add(line);
                    continue;
                }
            }
        }

        try (FileWriter writer = new FileWriter(SUPPLIER_FILE)) {  // Changed to use SUPPLIER_FILE constant
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }
    }
    
    // Method to add new supplier
    public static void addSupplier(String name, String contactNo, boolean active) throws IOException {
        String supplierID = generateSupplierID();
        String supplierData = String.format("%s,%s,%s,%b", supplierID, name, contactNo, active);
        
        try (FileWriter writer = new FileWriter(SUPPLIER_FILE, true)) {
            writer.write(supplierData + "\n");
        }
    }

    
    public static void updateSupplier(String supplierID, String name, String contactNo, boolean active) throws IOException {
    System.out.println("FileHandler: Updating supplier: ID=" + supplierID + ", Name=" + name);
    
    // Read all lines from the supplier file
    List<String> lines = Files.readAllLines(Paths.get(SUPPLIER_FILE));
    System.out.println("Read " + lines.size() + " lines from supplier file");

    boolean found = false;

    // Loop through the lines to find the supplier with the matching supplierID
    for (int i = 0; i < lines.size(); i++) {
        String line = lines.get(i);
        String[] parts = line.split(",", -1);
        System.out.println("Checking line " + i + ": " + line);
        
        if (parts.length > 0 && parts[0].equals(supplierID)) {
            System.out.println("Found matching supplier ID at line " + i);
            // Update the supplier data with the new information
            String updatedLine = String.format("%s,%s,%s,%b", supplierID, name, contactNo, active);
            lines.set(i, updatedLine);
            System.out.println("Updated line to: " + updatedLine);
            found = true;
            break;
        }
    }

    if (found) {
        // If the supplier was found and updated, write the updated lines back to the file
        System.out.println("Writing " + lines.size() + " lines back to file");
        Files.write(Paths.get(SUPPLIER_FILE), lines);
        System.out.println("File updated successfully");
    } else {
        // If the supplier was not found, throw an error
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
                
                String[] parts = line.split(",", -1);
                if (parts.length >= 4 && parts[0].trim().equals(supplierID)) {  // Changed to check for all 5 fields
                    return new Supplier(
                        parts[0].trim(),  // supplierID
                        parts[1].trim(),  // name 
                        parts[2].trim(),  // contactNo
                        Boolean.parseBoolean(parts[3].trim())  // active
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
                
                String[] parts = line.split(",", -1);
                if (parts.length > 0) {
                    supplierIDs.add(parts[0].trim());
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

                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {  
                    suppliers.add(new Supplier(
                        parts[0].trim(),  // supplierID
                        parts[1].trim(),  // supplierName
                        parts[2].trim(),  // contactNo
                        Boolean.parseBoolean(parts[3].trim())  // active
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
                
                String[] parts = line.split(",", -1);
                if (parts.length > 1) {
                    String existingSupplierID = parts[0].trim();
                    String existingSupplierName = parts[1].trim();
                    // Skip comparing with the current supplier when updating
                    if (currentSupplierID != null && existingSupplierID.equals(currentSupplierID)) {
                        continue;
                    }
                    if (existingSupplierName.equalsIgnoreCase(supplierName.trim())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

// ===================== SALES RECORD METHODS =====================
   

public static void saveSalesRecord(SalesRecord sale) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_FILE, true))) {
        writer.write(String.format("%s,%s,%d,%s,%.2f",
            sale.getSaleID(),
            sale.getItemID(),
            sale.getQuantitySold(),
            sale.getSaleDate(),
            sale.getTotalAmount()
        ));
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
                String[] parts = line.split(",");
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
        // Write PR header
        writer.write(String.format("%s,%s,%s,%s,%s",
            pr.getPrID(),
            pr.getRaisedBy(),
            pr.getRequestDate(),
            pr.getRequiredDeliveryDate(),
            pr.getStatus()
        ));
        writer.newLine();
        
        // Write items
        for (PRItem item : pr.getItems()) {
            writer.write(String.format("%s,%s,%d,%.2f,%.2f,%s,%s",
                pr.getPrID(),
                item.getItemID(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice(),
                item.getSupplierID(),
                item.getRequiredDeliveryDate()
            ));
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

                String[] parts = line.split(",", -1);
                if (parts.length > 0 && parts[0].startsWith("PR")) {
                    try {
                        int prNumber = Integer.parseInt(parts[0].substring(2));
                        if (prNumber > lastPRNumber) {
                            lastPRNumber = prNumber;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid IDs
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
                    String[] parts = line.split(",", -1);
                    if (parts.length >= 8) {  // Ensure we have all required fields
                        records.add(parts);
                    }
                }
            }
        }
        return records;
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
                String[] parts = line.split(",", -1);
                if (parts.length > 0 && !parts[0].equals(prID)) {
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
            
            String[] parts = line.split(",", -1);
            
            if (parts.length == 5) {
                // PR header line
                if (currentPR != null) {
                    currentPR.setItems(currentItems);
                    prList.add(currentPR);
                    currentItems = new ArrayList<>();
                }
                
                currentPR = new PurchaseRequisition(
                    parts[0], // prID
                    parts[1], // salesManagerID
                    LocalDate.parse(parts[2]), // requestDate
                    LocalDate.parse(parts[3]), // requiredDeliveryDate
                    parts[4], // status
                    new ArrayList<>() // empty items list for now
                );
            } else if (parts.length >= 7 && currentPR != null) {
                // PR item line
                PRItem item = new PRItem(
                    parts[1], // itemID
                    Integer.parseInt(parts[2]), // quantity
                    parts[5], // supplierID
                    Double.parseDouble(parts[3]), // unitPrice
                    LocalDate.parse(parts[6]) // requiredDeliveryDate
                );
                currentItems.add(item);
            }
        }
        
        // Add the last PR if exists
        if (currentPR != null) {
            currentPR.setItems(currentItems);
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
        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8 && parts[0].equals(prID)) {
                    return parts;
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
            String[] parts = line.split(",");
            if (parts.length >= 8 && parts[0].equals(prID)) {
                found = true;
                
                // Create updated line
                line = String.format("%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s",
                    prID,
                    itemID,
                    quantity,
                    price,
                    totalPrice,
                    creationDate,
                    supplierID,
                    requiredDeliveryDate,
                    status
                );
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
    public static boolean updatePRStatus(String prID, String newStatus) throws IOException {
    List<String> lines = new ArrayList<>();
    boolean found = false;
    
    try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 8 && parts[0].equals(prID)) {
                found = true;
                
                // Get existing values
                String itemID = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                double price = Double.parseDouble(parts[3]);
                double totalPrice = Double.parseDouble(parts[4]);
                LocalDate creationDate = LocalDate.parse(parts[5]);
                String supplierID = parts[6];
                LocalDate requiredDeliveryDate = LocalDate.parse(parts[7]);
                
                // Create updated line with new status
                line = String.format("%s,%s,%d,%.2f,%.2f,%s,%s,%s,%s",
                    prID,
                    itemID,
                    quantity,
                    price,
                    totalPrice,
                    creationDate,
                    supplierID,
                    requiredDeliveryDate,
                    newStatus
                );
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
                    String[] parts = line.split(",");
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

