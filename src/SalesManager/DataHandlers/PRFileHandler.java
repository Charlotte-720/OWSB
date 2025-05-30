package SalesManager.DataHandlers;

import static SalesManager.DataHandlers.SupplierFileHandler.loadAllSuppliers;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Item;
import model.PRItem;
import model.PurchaseRequisition;
import model.Supplier;

public class PRFileHandler extends BaseFileHandler {
    
    private static final String PR_FILE = "src/txtFile/pr.txt";
    
    
    public static void savePurchaseRequisition(PurchaseRequisition pr) throws IOException {
        if (pr == null || pr.getItems() == null || pr.getItems().isEmpty()) {
            throw new IllegalArgumentException("Purchase requisition must have at least one item");
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PR_FILE, true))) {
            // Write each item as a single line with all PR and item information
            for (PRItem item : pr.getItems()) {
                String itemData = formatPRItemString(pr, item);
                writer.write(itemData);
                writer.newLine();
            }
        }
    }
    
//    Formats PR and item data into a structured string
    private static String formatPRItemString(PurchaseRequisition pr, PRItem item) {
        return "PR ID: " + pr.getPrID() +
               ", PR Type: " + pr.getPrType() + 
               ", Item ID: " + item.getItemID() +
               ", Item Name: " + item.getItemName() +
               ", Quantity: " + item.getQuantity() +
               ", Unit Price: " + String.format("%.2f", item.getUnitPrice()) +
               ", Total Price: " + String.format("%.2f", item.getTotalPrice()) +
               ", Supplier ID: " + item.getSupplierID() +
               ", Raised By: " + pr.getRaisedBy() +
               ", Required Delivery Date: " + item.getRequiredDeliveryDate() +
               ", Request Date: " + pr.getRequestDate() +
               ", Status: " + pr.getStatus();
    }
    
//    Generates a unique PR ID
    public static String generatePRID() throws IOException {
        int lastPRNumber = 0;

        if (!new File(PR_FILE).exists()) {
            return "PR001";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String prID = extractPRID(line);
                
                if (prID.startsWith("PR")) {
                    try {
                        int prNumber = Integer.parseInt(prID.substring(2));
                        if (prNumber > lastPRNumber) {
                            lastPRNumber = prNumber;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid PR IDs
                    }
                }
            }
        }

        return String.format("PR%03d", lastPRNumber + 1);
    }
    
//    Extracts PR ID from a formatted line
    private static String extractPRID(String line) {
        if (line.contains("PR ID: ")) {
            int start = line.indexOf("PR ID: ") + 7;
            int end = line.indexOf(",", start);
            if (end == -1) end = line.length();
            return line.substring(start, end).trim();
        } else {
            // Old format fallback
            String[] parts = line.split(",", -1);
            if (parts.length > 0) {
                return parts[0].trim();
            }
        }
        return "";
    }
    
//     Reads all purchase requisition records and returns them as String arrays
    public static List<String[]> readPurchaseRequisitions() throws IOException {
        List<String[]> records = new ArrayList<>();
        if (!new File(PR_FILE).exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] keys = {"PR ID", "PR Type", "Item ID", "Item Name", "Quantity", 
                                   "Unit Price", "Total Price", "Supplier ID", "Raised By", 
                                   "Required Delivery Date", "Request Date", "Status"};
                    String[] parts = parseFormattedLine(line, keys);

                    if (parts.length >= 12) {
                        records.add(parts);
                    }
                }
            }
        }
        return records;
    }
    
//    Reads purchase requisitions formatted for table display
    public List<String[]> readPurchaseRequisitionsForTable() throws IOException {
        List<String[]> tableRecords = new ArrayList<>();
        List<String[]> allRecords = readPurchaseRequisitions();

        for (String[] record : allRecords) {
            if (record.length >= 12) {
                String[] tableRow = new String[10];  
                tableRow[0] = record[0];  // PR ID
                tableRow[1] = record[1];  // PR Type
                tableRow[2] = record[2];  // Item ID
                tableRow[3] = record[4];  // Quantity
                tableRow[4] = record[5];  // Unit Price
                tableRow[5] = record[6];  // Total Price
                tableRow[6] = record[9];  // Required Delivery Date
                tableRow[7] = record[7];  // Supplier ID
                tableRow[8] = record[10]; // Request Date
                tableRow[9] = record[11]; // Status
                
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
                String currentPRID = extractPRID(line);
                
                if (!currentPRID.equals(prID)) {
                    linesToKeep.add(line);
                } else {
                    found = true;
                }
            }
        }

        // Only rewrite the file if we found and removed the PR
        if (found) {
            writeLinesToFile(linesToKeep);
        }
        
        return found;
    }
    
    private static void writeLinesToFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PR_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
}
//    Loads all purchase requisitions from file and groups them by PR ID
    public List<PurchaseRequisition> loadAllPurchaseRequisitions() throws IOException {
        List<PurchaseRequisition> prList = new ArrayList<>();

        if (!new File(PR_FILE).exists()) {
            return prList;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            Map<String, PurchaseRequisition> prMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] keys = {"PR ID", "PR Type", "Item ID", "Item Name", "Quantity", 
                               "Unit Price", "Total Price", "Supplier ID", "Raised By", 
                               "Required Delivery Date", "Request Date", "Status"};
                String[] parts = parseFormattedLine(line, keys);

                if (parts.length >= 12) {
                    String prID = parts[0].trim();
                    String prType = parts[1].trim();

                    // Get or create PR
                    PurchaseRequisition pr = prMap.get(prID);
                    if (pr == null) {
                        pr = new PurchaseRequisition(
                            prID,                                    // prID
                            prType,                                  // prType
                            parts[8].trim(),                        // raisedBy
                            LocalDate.parse(parts[10].trim()),      // requestDate
                            LocalDate.parse(parts[9].trim()),       // requiredDeliveryDate
                            parts[11].trim(),                       // status
                            new ArrayList<>()                       // items
                        );
                        prMap.put(prID, pr);
                    }

                    // Create and add PRItem
                    PRItem item = new PRItem(
                        parts[2].trim(),                           // itemID
                        Integer.parseInt(parts[4].trim()),         // quantity
                        parts[3].trim(),                           // itemName
                        parts[7].trim(),                           // supplierID
                        Double.parseDouble(parts[5].trim()),       // unitPrice
                        LocalDate.parse(parts[9].trim())           // requiredDeliveryDate
                    );
                    pr.getItems().add(item);
                }
            }

            prList.addAll(prMap.values());
        }

        return prList;
    }
    
    public PurchaseRequisition getPurchaseRequisitionByID(String prID) throws IOException {
        List<PurchaseRequisition> allPRs = loadAllPurchaseRequisitions();
        for (PurchaseRequisition pr : allPRs) {
            if (pr.getPrID().equals(prID)) {
                return pr;
            }
        }
        return null;
    }
    
    public String[] loadPRData(String prID) throws IOException {
        if (prID == null || prID.trim().isEmpty()) {
            throw new IllegalArgumentException("PR ID cannot be empty");
        }
        
        String[] prData = getPurchaseRequisitionById(prID.trim());
        if (prData == null || prData.length < 11) {
            throw new IOException("PR data not found or incomplete for PR ID: " + prID);
        }
        
        return prData;
    }
    
    public static String[] getPurchaseRequisitionById(String prID) throws IOException {
        if (!new File(PR_FILE).exists()) {
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String currentPRID = extractPRID(line);
                
                if (currentPRID.equals(prID)) {
                    // Check if line contains PR Type or not
                    if (line.contains("PR Type: ")) {
                        // New format with PR Type
                        String[] keys = {"PR ID", "PR Type", "Item ID", "Item Name", "Quantity", 
                                       "Unit Price", "Total Price", "Supplier ID", "Raised By", 
                                       "Required Delivery Date", "Request Date", "Status"};
                        return parseFormattedLine(line, keys);
                    } else {
                        // Old format without PR Type - add default PR Type
                        String[] keys = {"PR ID", "Item ID", "Item Name", "Quantity", 
                                       "Unit Price", "Total Price", "Supplier ID", "Raised By", 
                                       "Required Delivery Date", "Request Date", "Status"};
                        String[] parts = parseFormattedLine(line, keys);
                        
                        // Create new array with PR Type inserted at index 1
                        String[] result = new String[parts.length + 1];
                        result[0] = parts[0]; // PR ID
                        result[1] = "RESTOCK"; // Default PR Type
                        
                        // Copy the rest of the data, shifting indices by 1
                        System.arraycopy(parts, 1, result, 2, parts.length - 1);
                        
                        return result;
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean updatePurchaseRequisition(String prID, String prType, String itemID, String itemName, 
            int quantity, double price, double totalPrice, String supplierID, String raisedBy,
            LocalDate requiredDeliveryDate, LocalDate requestDate, String status) throws IOException {

        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String currentPRID = extractPRID(line);
                
                if (currentPRID.equals(prID)) {
                    found = true;
                    // Create updated line with all information
                    line = "PR ID: " + prID +
                          ", PR Type: " + prType +
                          ", Item ID: " + itemID +
                          ", Item Name: " + itemName +
                          ", Quantity: " + quantity +
                          ", Unit Price: " + String.format("%.2f", price) +
                          ", Total Price: " + String.format("%.2f", totalPrice) +
                          ", Supplier ID: " + supplierID +
                          ", Raised By: " + raisedBy +
                          ", Required Delivery Date: " + requiredDeliveryDate +
                          ", Request Date: " + requestDate +
                          ", Status: " + status;
                }
                lines.add(line);
            }
        }
        
        if (found) {
            writeLinesToFile(lines);
            return true;
        }
        
        return false;
    }
    
    public boolean updatePRStatus(String prID, String newStatus) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String currentPRID = extractPRID(line);

                if (currentPRID.equals(prID)) {
                    found = true;

                    // Parse existing line to get current values
                    String[] keys = {"PR ID", "PR Type", "Item ID", "Item Name", "Quantity", 
                                   "Unit Price", "Total Price", "Supplier ID", "Raised By", 
                                   "Required Delivery Date", "Request Date", "Status"};
                    String[] parts = parseFormattedLine(line, keys);

                    if (parts.length >= 12) {
                        // Reconstruct line with new status
                        line = "PR ID: " + parts[0] +
                              ", PR Type: " + parts[1] +
                              ", Item ID: " + parts[2] +
                              ", Item Name: " + parts[3] +
                              ", Quantity: " + parts[4] +
                              ", Unit Price: " + parts[5] +
                              ", Total Price: " + parts[6] +
                              ", Supplier ID: " + parts[7] +
                              ", Raised By: " + parts[8] +
                              ", Required Delivery Date: " + parts[9] +
                              ", Request Date: " + parts[10] +
                              ", Status: " + newStatus;
                    }
                }
                lines.add(line);
            }
        }

        if (found) {
            writeLinesToFile(lines);
            return true;
        }

        return false;
    }
    
    
    public static boolean isItemNameInPR(String itemName) throws IOException {
        if (itemName == null || itemName.trim().isEmpty()) {
            return false;
        }

        if (!new File(PR_FILE).exists()) {
            return false;
        }

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
        
        return false;
    }
    
//  Gets all unique PR statuses from the file
    public List<String> getAllPRStatuses() throws IOException {
        List<String> statuses = new ArrayList<>();
        
        if (!new File(PR_FILE).exists()) {
            return statuses;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String status = "";
                if (line.contains("Status: ")) {
                    int start = line.lastIndexOf("Status: ") + 8;
                    status = line.substring(start).trim();
                    
                    if (!status.isEmpty() && !statuses.contains(status)) {
                        statuses.add(status);
                    }
                }
            }
        }
        
        return statuses;
    }
    
    public Map<String, Integer> getPRCountByStatus() throws IOException {
        Map<String, Integer> statusCount = new HashMap<>();
        
        if (!new File(PR_FILE).exists()) {
            return statusCount;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(PR_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String status = "";
                if (line.contains("Status: ")) {
                    int start = line.lastIndexOf("Status: ") + 8;
                    status = line.substring(start).trim();
                    
                    if (!status.isEmpty()) {
                        statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
                    }
                }
            }
        }
        
        return statusCount;
    }
    
    public static List<Supplier> loadAndFormatActiveSuppliers() throws IOException {
        // Get all suppliers and filter for active ones
        List<Supplier> allSuppliers = SupplierFileHandler.loadAllSuppliers();
        List<Supplier> activeSuppliers = new ArrayList<>();
        
        for (Supplier supplier : allSuppliers) {
            if (supplier.isActive()) {
                activeSuppliers.add(supplier);
            }
        }
        
        return activeSuppliers;
    }
    
    public static PurchaseRequisition createAndSavePR(String prType, String salesManagerID, 
                                                     PRItem item) throws IOException {
        String prID = generatePRID();
        LocalDate requestDate = LocalDate.now();
        LocalDate requiredDate = item.getRequiredDeliveryDate();
        String status = "Pending";
        
        List<PRItem> items = new ArrayList<>();
        items.add(item);
        
        PurchaseRequisition pr = new PurchaseRequisition(prID, prType, salesManagerID, 
                                                        requestDate, requiredDate, status, items);
        
        savePurchaseRequisition(pr);
        return pr;
    }
    
    public static boolean updatePRWithNewData(String prID, String[] currentData, String newItemName, 
                                             int quantity, double unitPrice, String supplierID, 
                                             LocalDate deliveryDate) throws IOException {
        
        double totalPrice = quantity * unitPrice;
        LocalDate requestDate = LocalDate.parse(currentData[10].trim());
        
        return updatePurchaseRequisition(
            prID,                           // PR ID
            currentData[1].trim(),          // PR Type (preserve existing)
            currentData[2].trim(),          // Item ID (preserve existing)
            newItemName.trim(),             // Item Name (updated)
            quantity,                       // Quantity (updated)
            unitPrice,                      // Unit Price (updated)
            totalPrice,                     // Total Price (calculated)
            supplierID.trim(),              // Supplier ID (updated)
            currentData[8].trim(),          // Raised By (preserve existing)
            deliveryDate,                   // Required Delivery Date (updated)
            requestDate,                    // Request Date (preserve existing)
            currentData[11].trim()          // Status (preserve existing)
        );
    }
    
    public static boolean updateRestockPRWithNewData(String prID, String itemID, String itemName,
                                                    int quantity, double unitPrice, String supplierID,
                                                    LocalDate deliveryDate, String prType, 
                                                    String salesManagerID, LocalDate creationDate) 
                                                    throws IOException {
        
        double totalPrice = quantity * unitPrice;
        
        return updatePurchaseRequisition(
            prID,              // PR ID
            prType,            // PR Type 
            itemID,            // Item ID
            itemName,          // Item Name
            quantity,          // Quantity (updated)
            unitPrice,         // Unit Price (updated)
            totalPrice,        // Total Price (calculated)
            supplierID,        // Supplier ID (updated)
            salesManagerID,    // Raised By
            deliveryDate,      // Required Delivery Date (updated)
            creationDate,      // Creation Date
            "Pending"          // Status
        );
    }
}
