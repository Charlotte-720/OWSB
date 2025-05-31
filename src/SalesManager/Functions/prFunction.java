package SalesManager.Functions;

import SalesManager.DataHandlers.ItemFileHandler;
import static SalesManager.DataHandlers.ItemFileHandler.generateItemID;
import SalesManager.DataHandlers.PRFileHandler;
import static SalesManager.DataHandlers.PRFileHandler.savePurchaseRequisition;
import SalesManager.DataHandlers.SupplierFileHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import model.Item;
import model.PRItem;
import model.PurchaseRequisition;
import model.Supplier;


public class prFunction {
    private PRFileHandler prFileHandler;
    
    public prFunction() {
        this.prFileHandler = new PRFileHandler();
    }
    
    
//    validate delivery date
    public LocalDate validateDeliveryDate(String dateText) throws IllegalArgumentException {
        if (dateText == null || dateText.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter delivery date (yyyy-MM-dd)");
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate deliveryDate = LocalDate.parse(dateText.trim(), formatter);
            LocalDate today = LocalDate.now();

            if (deliveryDate.isBefore(today)) {
                throw new IllegalArgumentException("Delivery date cannot be in the past!");
            }
            return deliveryDate;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
    
//    validate item quantity
    public int validateQuantity(String quantityText) throws IllegalArgumentException {
        if (quantityText == null || quantityText.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter quantity");
        }
        
        try {
            int quantity = Integer.parseInt(quantityText.trim());
            if (quantity <= 0) {
                throw new IllegalArgumentException("Quantity must be a positive number");
            }
            return quantity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid quantity (numbers only)");
        }
    }
    
//    validate item price
    public double validatePrice(String priceText) throws IllegalArgumentException {
        if (priceText == null || priceText.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter price");
        }
        
        try {
            double price = Double.parseDouble(priceText.trim());
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
            return price;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid price (numbers only)");
        }
    }
    
//    validates item name and check duplicates
    public void validateItemName(String itemName) throws IllegalArgumentException, IOException {
        if (itemName == null || itemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter item name");
        }
        
        if (PRFileHandler.isItemNameInPR(itemName.trim())) {
            throw new IllegalArgumentException("Item with this name already exists. Please use a different name.");
        }
    }
    
//    validate all inputs
    public void validateAllInput(String itemName, String quantityText, String priceText, 
                                String deliveryDateText, String supplierID) 
                                throws IllegalArgumentException, IOException {
        validateItemName(itemName);
        validateQuantity(quantityText);
        validatePrice(priceText);
        validateDeliveryDate(deliveryDateText);
        
        if (supplierID == null || supplierID.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select a supplier");
        }
    }
    
    public PRItem createPRItem(String itemName, String quantityText, String priceText, 
                              String deliveryDateText, String supplierID) 
                              throws IOException, IllegalArgumentException {
        
        // Validate all inputs first
        validateAllInput(itemName, quantityText, priceText, deliveryDateText, supplierID);
        
        // Generate new item ID
        String newItemID = generateItemID();
        
        // Parse validated inputs
        String name = itemName.trim();
        int quantity = validateQuantity(quantityText);
        double price = validatePrice(priceText);
        LocalDate deliveryDate = validateDeliveryDate(deliveryDateText);
        
        // Create and return PRItem
        return new PRItem(newItemID, quantity, name, supplierID, price, deliveryDate);
    }
    
//    create new PR
    public PurchaseRequisition createPurchaseRequisition(PRItem item, String salesManagerID) 
                                                        throws IOException {
        String prID = PRFileHandler.generatePRID();
        String prType = PurchaseRequisition.TYPE_NEW_ITEM;
        LocalDate requestDate = LocalDate.now();
        LocalDate requiredDate = item.getRequiredDeliveryDate();
        String status = "Pending";
        
        List<PRItem> items = new ArrayList<>();
        items.add(item);
        
        return new PurchaseRequisition(prID, prType, salesManagerID, requestDate, requiredDate, status, items);
    }
    
//    create and save
    public PurchaseRequisition createAndSaveNewItemPR(String itemName, String quantityText, 
                                                     String priceText, String deliveryDateText, 
                                                     String supplierID, String salesManagerID) 
                                                     throws IOException, IllegalArgumentException {
        
        // Create PRItem from input
        PRItem newItem = createPRItem(itemName, quantityText, priceText, deliveryDateText, supplierID);
        
        // Create Purchase Requisition
        PurchaseRequisition pr = createPurchaseRequisition(newItem, salesManagerID);
        
        // Save to file
        savePurchaseRequisition(pr);
        
        return pr;
    }
    
    public String getSupplierIDFromSelection(List<Supplier> suppliers, int selectedIndex) {
        if (suppliers == null || suppliers.isEmpty() || selectedIndex < 0 || selectedIndex >= suppliers.size()) {
            return null;
        }
        return suppliers.get(selectedIndex).getSupplierID();
    }
    
    public String[] formatSuppliersForDisplay(List<Supplier> suppliers) {
        if (suppliers == null || suppliers.isEmpty()) {
            return new String[]{"No active suppliers available"};
        }
        
        String[] displayNames = new String[suppliers.size()];
        for (int i = 0; i < suppliers.size(); i++) {
            Supplier supplier = suppliers.get(i);
            displayNames[i] = supplier.getSupplierID() + " - " + supplier.getSupplierName();
        }
        return displayNames;
    }
    
//    EDIT
//    load current PR by PRID
    public String[] loadPRData(String prID) throws IOException {
        return prFileHandler.loadPRData(prID);
    }
    
//    Load active suppliers and format for ComboBox 
    public List<Supplier> loadAndFormatActiveSuppliers() throws IOException {
        return prFileHandler.loadAndFormatActiveSuppliers();
    }
    
//    Find active supplier
    public Supplier findSupplierInList(List<Supplier> suppliers, String supplierID) {
        if (suppliers == null || supplierID == null || supplierID.trim().isEmpty()) {
            return null;
        }
        
        for (Supplier supplier : suppliers) {
            if (supplier.getSupplierID().equals(supplierID.trim())) {
                return supplier;
            }
        }
        return null;
    }
    
//    get supplier by id
    public Supplier getSupplierById(String supplierID) throws IOException {
        if (supplierID == null || supplierID.trim().isEmpty()) {
            return null;
        }
        return SupplierFileHandler.getSupplierById(supplierID.trim());
    }
    
//    validate item name for editing
    public void validateItemNameForEdit(String newItemName, String currentItemName) 
            throws IllegalArgumentException, IOException {
        if (newItemName == null || newItemName.trim().isEmpty()) {
            throw new IllegalArgumentException("Item name is required!");
        }
        
        // Only check for duplicate if the name has changed
        if (!newItemName.trim().equalsIgnoreCase(currentItemName.trim())) {
            if (PRFileHandler.isItemNameInPR(newItemName.trim())) {
                throw new IllegalArgumentException(
                    "Item name '" + newItemName + "' already exists!\nPlease choose a different name.");
            }
        }
    }
    
//    validate all inputs for edidting
    public void validateEditInput(String itemName, String currentItemName, String quantityText, 
                                 String priceText, String deliveryDateText, String supplierID) 
                                 throws IllegalArgumentException, IOException {
        
        validateItemNameForEdit(itemName, currentItemName);
        validateQuantity(quantityText);
        validatePrice(priceText);
        validateDeliveryDate(deliveryDateText);
        
        if (supplierID == null || supplierID.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select a valid supplier!");
        }
    }
    
    public LocalDate parseDate(String dateText) throws IllegalArgumentException {
        if (dateText == null || dateText.trim().isEmpty()) {
            throw new IllegalArgumentException("Date is required");
        }
        
        try {
            return LocalDate.parse(dateText.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format! Please use YYYY-MM-DD format.");
        }
    }
    
//    update pr data
    public boolean updatePRData(String prID, String[] currentData, String newItemName, 
                               String quantityText, String priceText, String supplierID, 
                               String deliveryDateText) throws IOException, IllegalArgumentException {
        
        // Validate current item name from data
        String currentItemName = (currentData != null && currentData.length > 3) ? 
                                currentData[3].trim() : "";
        
        // Validate all inputs
        validateEditInput(newItemName, currentItemName, quantityText, priceText, 
                         deliveryDateText, supplierID);
        
        // Parse validated inputs
        int newQuantity = validateQuantity(quantityText);
        double newUnitPrice = validatePrice(priceText);
        double newTotalPrice = newQuantity * newUnitPrice;
        LocalDate newDeliveryDate = parseDate(deliveryDateText);
        LocalDate requestDate = parseDate(currentData[10].trim());
        
        // Update PR using FileHandler
        return PRFileHandler.updatePurchaseRequisition(
            prID,                                      // PR ID
            currentData[1].trim(),                     // PR Type (preserve existing)
            currentData[2].trim(),                     // Item ID (preserve existing)
            newItemName.trim(),                        // Item Name (updated)
            newQuantity,                               // Quantity (updated)
            newUnitPrice,                              // Unit Price (updated)
            newTotalPrice,                             // Total Price (calculated)
            supplierID.trim(),                         // Supplier ID (updated)
            currentData[8].trim(),                     // Raised By (preserve existing)
            newDeliveryDate,                           // Required Delivery Date (updated)
            requestDate,                               // Request Date (preserve existing)
            currentData[11].trim()                     // Status (preserve existing)
        );
    }
    
    public boolean editPRItem(String prID, String newItemName, String quantityText, 
                             String priceText, String supplierID, String deliveryDateText) 
                             throws IOException, IllegalArgumentException {
        
        // Load current PR data
        String[] currentData = loadPRData(prID);
        
        // Update and return result
        return updatePRData(prID, currentData, newItemName, quantityText, 
                           priceText, supplierID, deliveryDateText);
    }
    
    public String createUpdateSuccessMessage(String prID, String itemName, String quantityText, 
                                           String priceText, String supplierID) {
        try {
            int quantity = Integer.parseInt(quantityText.trim());
            double unitPrice = Double.parseDouble(priceText.trim());
            double totalPrice = quantity * unitPrice;
            
            return "PR item updated successfully!\n" +
                   "PR ID: " + prID + "\n" +
                   "Item: " + itemName + "\n" +
                   "Quantity: " + quantity + "\n" +
                   "Unit Price: RM" + String.format("%.2f", unitPrice) + "\n" +
                   "Total Price: RM" + String.format("%.2f", totalPrice) + "\n" +
                   "Supplier: " + supplierID;
        } catch (NumberFormatException e) {
            return "PR item updated successfully!\nPR ID: " + prID;
        }
    }
//    Restock PR
//    Load all available items for selection
    public List<Item> loadAllItems() throws IOException {
        return ItemFileHandler.loadAllItems();
    }
    
//    get item by id for restock PR
    public Item getItemById(String itemID) throws IOException {
        if (itemID == null || itemID.trim().isEmpty()) {
            return null;
        }
        return itemFunction.getItemById(itemID.trim());
    }
    
    public void validateRestockInputs(String selectedItemID, String quantityText, 
                                    String deliveryDateText) throws IllegalArgumentException {
        
        if (selectedItemID == null || selectedItemID.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select an item");
        }
        
        validateQuantity(quantityText);
        validateDeliveryDate(deliveryDateText);
    }
    
    public PRItem createRestockPRItem(String itemID, String quantityText, String deliveryDateText) 
                                     throws IOException, IllegalArgumentException {
        
        // Validate inputs
        validateRestockInputs(itemID, quantityText, deliveryDateText);
        
        // Get existing item details
        Item existingItem = getItemById(itemID);
        if (existingItem == null) {
            throw new IllegalArgumentException("Item not found: " + itemID);
        }
        // Parse validated inputs
        int quantity = validateQuantity(quantityText);
        LocalDate deliveryDate = validateDeliveryDate(deliveryDateText);
        
        // Create PRItem using existing item data
        return new PRItem(
            existingItem.getItemID(),
            quantity,
            existingItem.getItemName(),
            "", // Supplier will be set separately
            existingItem.getPrice(),
            deliveryDate
        );
    }
    public PurchaseRequisition createRestockPurchaseRequisition(PRItem item, String salesManagerID) 
                                                              throws IOException {
        String prID = PRFileHandler.generatePRID();
        String prType = PurchaseRequisition.TYPE_RESTOCK; // Assuming this constant exists
        LocalDate requestDate = LocalDate.now();
        LocalDate requiredDate = item.getRequiredDeliveryDate();
        String status = "Pending";
        
        List<PRItem> items = new ArrayList<>();
        items.add(item);
        
        return new PurchaseRequisition(prID, prType, salesManagerID, requestDate, requiredDate, status, items);
    }
    
    /**
     * Update restock PR data
     */
    public boolean updateRestockPRData(String prID, String itemID, String quantityText, 
                                     String unitPriceText, String supplierID, 
                                     String deliveryDateText, String currentPRType, 
                                     String salesManagerID, String creationDateText) 
                                     throws IOException, IllegalArgumentException {
        
        // Validate inputs
        validateRestockInputs(itemID, quantityText, deliveryDateText);
        
        if (supplierID == null || supplierID.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter supplier ID");
        }
        
        // Get item details
        Item item = getItemById(itemID);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemID);
        }
        
        // Parse inputs
        int quantity = validateQuantity(quantityText);
        double unitPrice = validatePrice(unitPriceText);
        double totalPrice = quantity * unitPrice;
        LocalDate deliveryDate = validateDeliveryDate(deliveryDateText);
        LocalDate creationDate = parseDate(creationDateText);
        
        // Update PR using FileHandler
        return PRFileHandler.updatePurchaseRequisition(
            prID,                    // PR ID
            currentPRType,           // PR Type 
            itemID,                  // Item ID
            item.getItemName(),      // Item Name
            quantity,                // Quantity (updated)
            unitPrice,               // Unit Price (updated)
            totalPrice,              // Total Price (calculated)
            supplierID,              // Supplier ID (updated)
            salesManagerID,          // Raised By
            deliveryDate,            // Required Delivery Date (updated)
            creationDate,            // Creation Date
            "Pending"                // Status
        );
    }
    
    /**
     * Create and save restock PR
     */
    public PurchaseRequisition createAndSaveRestockPR(String itemID, String quantityText, 
                                                     String deliveryDateText, String salesManagerID) 
                                                     throws IOException, IllegalArgumentException {
        
        // Create restock PR item
        PRItem restockItem = createRestockPRItem(itemID, quantityText, deliveryDateText);
        
        // Create Purchase Requisition
        PurchaseRequisition pr = createRestockPurchaseRequisition(restockItem, salesManagerID);
        
        // Save to file
        savePurchaseRequisition(pr);
        
        return pr;
    }
    
    /**
     * Calculate total amount for table display
     */
    public double calculateTotalAmount(String quantityText, String unitPriceText) 
                                     throws IllegalArgumentException {
        int quantity = validateQuantity(quantityText);
        double unitPrice = validatePrice(unitPriceText);
        return quantity * unitPrice;
    }
    
    /**
     * Create success message for restock PR
     */
    public String createRestockSuccessMessage(String prID, String itemName, String quantityText, 
                                            String unitPriceText) {
        try {
            int quantity = Integer.parseInt(quantityText.trim());
            double unitPrice = Double.parseDouble(unitPriceText.trim());
            double totalPrice = quantity * unitPrice;
            
            return "Restock PR updated successfully!\n" +
                   "PR ID: " + prID + "\n" +
                   "Item: " + itemName + "\n" +
                   "Quantity: " + quantity + "\n" +
                   "Unit Price: RM" + String.format("%.2f", unitPrice) + "\n" +
                   "Total Amount: RM" + String.format("%.2f", totalPrice);
        } catch (NumberFormatException e) {
            return "Restock PR updated successfully!\nPR ID: " + prID;
        }
    }
    
    public String[] getItemIDs() throws IOException {
        return loadAllItems().stream().map(Item::getItemID).toArray(String[]::new);
    }

    public Item getItemDetails(String itemID) throws IOException {
        return getItemById(itemID);
    }

    public double calculateTotal(String qtyText, String priceText) {
        int qty = Integer.parseInt(qtyText);
        double price = Double.parseDouble(priceText);
        return qty * price;
    }
    
    public boolean updateRestockPR(String prID, String salesManagerID, String prType,
                                String itemID, String qty, String price, String deliveryDate,
                                String creationDate, String supplierID) throws IOException {
     int quantity = Integer.parseInt(qty);
     double unitPrice = Double.parseDouble(price);
     double total = quantity * unitPrice;
     return PRFileHandler.updatePurchaseRequisition(
         prID, prType, itemID, getItemById(itemID).getItemName(), quantity,
         unitPrice, total, supplierID, salesManagerID,
         LocalDate.parse(deliveryDate), LocalDate.parse(creationDate), "Pending"
     );
 }
    
//    PR Operations
    public boolean deletePurchaseRequisition(String prID) throws IOException {
        if (prID == null || prID.trim().isEmpty()) {
            throw new IllegalArgumentException("PR ID cannot be null or empty");
        }
        return PRFileHandler.deletePurchaseRequisition(prID.trim());
    }
    
    /**
     * Get PR ID from table data
     */
    public String extractPRIDFromTableRow(Object[] rowData) {
        if (rowData != null && rowData.length > 0 && rowData[0] != null) {
            return rowData[0].toString();
        }
        return null;
    }
    
    /**
     * Create table row data from PR record
     */
    public Object[] createTableRowData(String[] prRecord) {
        if (prRecord == null || prRecord.length < 12) {
            return null;
        }
        
        return new Object[]{
            prRecord[0],  // PR ID
            prRecord[3],  // Item Name
            prRecord[4],  // Quantity
            prRecord[5],  // Unit Price
            prRecord[6],  // Total Price
            prRecord[10], // Request Date
            prRecord[7],  // Supplier ID
            prRecord[9],  // Required Delivery Date
            prRecord[11], // Status
            ""            // Actions column (empty for buttons)
        };
    }
    
    /**
     * Process all PR records for table display
     */
    public List<Object[]> processRecordsForTable(List<String[]> records) {
        List<Object[]> tableData = new ArrayList<>();
        
        for (String[] record : records) {
            Object[] rowData = createTableRowData(record);
            if (rowData != null) {
                tableData.add(rowData);
            }
        }
        
        return tableData;
    }
    
    /**
     * Determine PR type from PR data
     */
    public String determinePRType(String[] prData) {
        // Check if PR Type field exists and is not empty
        if (prData.length >= 2 && prData[1] != null && !prData[1].trim().isEmpty()) {
            return prData[1].trim();
        }
        return "RESTOCK"; // Default assumption
    }
    
    /**
     * Validate PR ID format and existence
     */
    public boolean isValidPRID(String prID) {
        if (prID == null || prID.trim().isEmpty()) {
            return false;
        }
        
        try {
            String[] prData = loadPRData(prID.trim());
            return prData != null && prData.length >= 12;
        } catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Check if PR can be edited (based on status)
     */
    public boolean canEditPR(String prID) throws IOException {
        String[] prData = loadPRData(prID);
        if (prData == null || prData.length < 12) {
            return false;
        }
        
        String status = prData[11].trim();
        // Only allow editing of pending PRs
        return "Pending".equalsIgnoreCase(status);
    }
    
    /**
     * Check if PR can be deleted (based on status)
     */
    public boolean canDeletePR(String prID) throws IOException {
        String[] prData = loadPRData(prID);
        if (prData == null || prData.length < 12) {
            return false;
        }
        
        String status = prData[11].trim();
        // Only allow deletion of pending PRs
        return "Pending".equalsIgnoreCase(status);
    }
    
    /**
     * Get PR summary information for display
     */
    public String getPRSummary(String prID) throws IOException {
        String[] prData = loadPRData(prID);
        if (prData == null || prData.length < 12) {
            return "PR not found";
        }
        
        return String.format(
            "PR ID: %s\nItem: %s\nQuantity: %s\nTotal: RM%s\nStatus: %s",
            prData[0], prData[3], prData[4], prData[6], prData[11]
        );
    }
    
    /**
     * Get all pending PRs
     */
    public List<String[]> getPendingPRs() throws IOException {
        List<String[]> allPRs = loadPurchaseRequisitions();
        List<String[]> pendingPRs = new ArrayList<>();
        
        for (String[] pr : allPRs) {
            if (pr.length >= 12 && "Pending".equalsIgnoreCase(pr[11].trim())) {
                pendingPRs.add(pr);
            }
        }
        
        return pendingPRs;
    }
    
    /**
     * Get PRs by status
     */
    public List<String[]> getPRsByStatus(String status) throws IOException {
        List<String[]> allPRs = loadPurchaseRequisitions();
        List<String[]> filteredPRs = new ArrayList<>();
        
        for (String[] pr : allPRs) {
            if (pr.length >= 12 && status.equalsIgnoreCase(pr[11].trim())) {
                filteredPRs.add(pr);
            }
        }
        
        return filteredPRs;
    }
    
    /**
     * Get PRs by type
     */
    public List<String[]> getPRsByType(String prType) throws IOException {
        List<String[]> allPRs = loadPurchaseRequisitions();
        List<String[]> filteredPRs = new ArrayList<>();
        
        for (String[] pr : allPRs) {
            if (pr.length >= 12 && prType.equalsIgnoreCase(pr[1].trim())) {
                filteredPRs.add(pr);
            }
        }
        
        return filteredPRs;
    }
    
    public List<String[]> loadPurchaseRequisitions() throws IOException {
        return PRFileHandler.readPurchaseRequisitions();
    }
    
    public void handleDeletePROperation(String prID, 
                                  java.util.function.Function<String, Boolean> confirmationCallback,
                                  java.util.function.Consumer<String> successCallback,
                                  java.util.function.Consumer<String> errorCallback,
                                  Runnable refreshCallback) {
    try {
        // Validate PR ID
        if (prID == null || prID.trim().isEmpty()) {
            errorCallback.accept("Invalid PR ID");
            return;
        }
        
        // Get confirmation from UI
        boolean confirmed = confirmationCallback.apply(prID);
        if (!confirmed) {
            return; // User cancelled deletion
        }
        
        // Perform the deletion
        boolean deleted = PRFileHandler.deletePurchaseRequisition(prID);
        
        if (deleted) {
            refreshCallback.run(); // Refresh the table
            successCallback.accept("Purchase Requisition " + prID + " deleted successfully");
        } else {
            errorCallback.accept("Failed to delete Purchase Requisition " + prID);
        }
        
    } catch (IOException e) {
        errorCallback.accept("Error deleting Purchase Requisition: " + e.getMessage());
    } catch (Exception e) {
        errorCallback.accept("Unexpected error during deletion: " + e.getMessage());
    }
}
    
//    Search function
    public List<String[]> searchPRRecords(List<String[]> allRecords, String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return new ArrayList<>(allRecords);
        }
        
        String searchLower = searchText.toLowerCase().trim();
        List<String[]> filteredRecords = new ArrayList<>();
        
        for (String[] record : allRecords) {
            if (record != null && record.length >= 12) {
                if (matchesPRSearchCriteria(record, searchLower)) {
                    filteredRecords.add(record);
                }
            }
        }
        
        return filteredRecords;
    }
    
    private boolean matchesPRSearchCriteria(String[] record, String searchLower) {
        // Search in these fields: PR ID, Item Name, Supplier ID, Status
        String prID = (record[0] != null) ? record[0].toLowerCase() : "";
        String itemName = (record[3] != null) ? record[3].toLowerCase() : "";
        String supplierID = (record[7] != null) ? record[7].toLowerCase() : "";
        String status = (record[11] != null) ? record[11].toLowerCase() : "";
        
        // Return true if any field contains the search text
        return prID.contains(searchLower) || 
               itemName.contains(searchLower) || 
               supplierID.contains(searchLower) || 
               status.contains(searchLower);
    }
    
    public List<Object[]> performSearchAndGetTableData(List<String[]> allRecords, String searchText) {
        List<String[]> filteredRecords = searchPRRecords(allRecords, searchText);
        return processRecordsForTable(filteredRecords);
    }
    
    
}
