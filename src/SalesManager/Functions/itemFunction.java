package SalesManager.Functions;

import SalesManager.DataHandlers.ItemFileHandler;
import SalesManager.DataHandlers.ItemFileHandler.POItem;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import model.Item;

public class itemFunction {
   
// get item by id
    public static Item getItemById(String itemId) throws IOException {
        if (itemId == null || itemId.trim().isEmpty()) {
            return null;
        }

        List<Item> items = ItemFileHandler.loadAllItems();
        return items.stream()
                .filter(item -> item.getItemID().equals(itemId))
                .findFirst()
                .orElse(null);
    }
    
//    get all item
    public static List<Item> getAllItems() throws IOException {
        return ItemFileHandler.loadAllItems();
    }
    
//    validate item iputs
    public static void validateItem(String itemName, String itemPrice, String itemQuantity, 
                                  String expiredDate, String category, String supplierName) {
        if (itemName == null || itemName.trim().isEmpty() || itemName.equals("Select Item")) {
            throw new IllegalArgumentException("Please select an item name");
        }
        
        if (itemPrice == null || itemPrice.trim().isEmpty() || itemPrice.trim().equals(" ")) {
            throw new IllegalArgumentException("Item price is required");
        }
        
        if (itemQuantity == null || itemQuantity.trim().isEmpty() || itemQuantity.trim().equals(" ")) {
            throw new IllegalArgumentException("Item quantity is required");
        }
        
        if (expiredDate == null || expiredDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Expired date is required");
        }
        
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select a category");
        }
        
        if (supplierName == null || supplierName.trim().isEmpty() || supplierName.trim().equals(" ")) {
            throw new IllegalArgumentException("Supplier name is required");
        }
        
        // Validate numeric values
        try {
            double price = Double.parseDouble(itemPrice.trim());
            if (price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format");
        }
        
        try {
            int quantity = Integer.parseInt(itemQuantity.trim());
            if (quantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid quantity format");
        }
    }
    
//    validate expired date
    public static LocalDate validateExpiredDate(String dateText) throws IllegalArgumentException {
        if (dateText == null || dateText.trim().isEmpty()) {
            throw new IllegalArgumentException("Please enter expired date");
        }
        
        try {
            LocalDate expired = LocalDate.parse(dateText.trim());
            LocalDate today = LocalDate.now();

            if (expired.isBefore(today)) {
                throw new IllegalArgumentException("Expired date cannot be in the past!");
            }
            return expired;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD");
        }
    }
    
//    check item exists by name
    public static boolean itemExists(String itemName) throws IOException {
        return ItemFileHandler.loadAllItems().stream()
            .anyMatch(item -> item.getItemName().equalsIgnoreCase(itemName.trim()));
    }
    
//    check item name is duplicate
    public static boolean isItemNameDuplicate(String itemName) throws IOException {
        if (itemName == null || itemName.trim().isEmpty()) {
            return false;
        }

        return ItemFileHandler.loadAllItems().stream()
            .anyMatch(item -> item.getItemName().equalsIgnoreCase(itemName.trim()));
    }
    
    public static boolean isDuplicateItem(String itemName) throws IOException {
        return isItemNameDuplicate(itemName);
    }
    
//  check excluding current item (for updates)  
    public static boolean isDuplicateItemExcludingCurrent(String itemNameToCheck, String currentItemID) throws IOException {
        return ItemFileHandler.loadAllItems().stream()
            .anyMatch(item -> !item.getItemID().equals(currentItemID) && 
                             item.getItemName().equalsIgnoreCase(itemNameToCheck.trim()));
    }
    
//    find item
    public static Item findExistingItem(String name, String category, String supplier, double price) throws IOException {
        return ItemFileHandler.loadAllItems().stream()
            .filter(item -> item.getItemName().equalsIgnoreCase(name.trim()) &&
                           item.getCategory().equalsIgnoreCase(category.trim()) &&
                           item.getSupplierID().equalsIgnoreCase(supplier.trim()) &&
                           Math.abs(item.getPrice() - price) < 0.01) // Compare prices with tolerance
            .findFirst()
            .orElse(null);
    }
    
//    create new item
    public static Item createItem(String itemID, String name, double price, String category,
                                LocalDate expiredDate, String supplier, int quantity) {
        return new Item(itemID, name, price, category, expiredDate, supplier, quantity, LocalDate.now());
    }
    
    public static void createNewItem(String itemID, String name, double price, int quantity,
                                   String category, LocalDate expiredDate, String supplierID) throws IOException {
        Item newItem = new Item(itemID, name, price, category, expiredDate, supplierID, quantity, LocalDate.now());
        ItemFileHandler.saveItem(newItem);
    }
    
//    update item stock after sale
    public static void updateItemStockAfterSale(String itemID, int quantitySold) throws IOException {
        if (quantitySold < 0) {
            throw new IllegalArgumentException("Quantity sold cannot be negative");
        }

        List<Item> items = ItemFileHandler.loadAllItems();
        boolean itemFound = false;
        
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                int oldStock = item.getTotalStock();
                int newStock = oldStock - quantitySold;
                
                if (newStock < 0) {
                    throw new IllegalStateException("Insufficient stock. Available: " + oldStock + ", Required: " + quantitySold);
                }
                
                item.setTotalStock(newStock);
                item.setUpdatedDate(LocalDate.now());
                itemFound = true;
                break;
            }
        }
        
        if (!itemFound) {
            throw new IOException("Item not found: " + itemID);
        }
        
        ItemFileHandler.saveAllItems(items);
    }
    
//    update item stocck
    public static void updateItemStock(String itemID, int newStock, LocalDate expiredDate) throws IOException {
        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        List<Item> items = ItemFileHandler.loadAllItems();
        boolean itemFound = false;
        
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                item.setTotalStock(newStock);
                if (expiredDate != null) {
                    item.setExpiredDate(expiredDate);
                }
                item.setUpdatedDate(LocalDate.now());
                itemFound = true;
                break;
            }
        }
        
        if (!itemFound) {
            throw new IOException("Item not found: " + itemID);
        }
        
        ItemFileHandler.saveAllItems(items);
    }
    
//    get paid po item
     public static List<POItem> getPaidPOItems() throws IOException {
        return ItemFileHandler.loadPaidPOItems();
    }
     
//    extract unique item names from PO items 
     public static List<String> extractUniqueItemNames(List<POItem> paidPOItems) {
        List<String> uniqueNames = paidPOItems.stream()
            .map(item -> item.itemName)
            .distinct()
            .collect(Collectors.toList());
        
        uniqueNames.add(0, "Select Item");
        return uniqueNames;
    }
     
//     find PO item by name
     public static POItem findPOItemByName(List<POItem> paidPOItems, String itemName) {
        return paidPOItems.stream()
            .filter(item -> item.itemName.equals(itemName))
            .findFirst()
            .orElse(null);
    }
     
     // ================== SEARCH AND FILTER OPERATIONS ==================
    
    /**
     * Search items by text
     */
    public static List<Item> searchItems(String searchText) throws IOException {
        if (searchText == null || searchText.trim().isEmpty()) {
            return ItemFileHandler.loadAllItems();
        }

        String searchLower = searchText.trim().toLowerCase();
        List<Item> allItems = ItemFileHandler.loadAllItems();
        List<Item> filteredItems = new ArrayList<>();

        for (Item item : allItems) {
            if (matchesSearchCriteria(item, searchLower)) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }
    
    /**
     * Get items by category
     */
    public static List<Item> getItemsByCategory(String category) throws IOException {
        if (category == null || category.trim().isEmpty()) {
            return ItemFileHandler.loadAllItems();
        }

        return ItemFileHandler.loadAllItems().stream()
            .filter(item -> item.getCategory().equalsIgnoreCase(category.trim()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get items by supplier
     */
    public static List<Item> getItemsBySupplier(String supplierID) throws IOException {
        if (supplierID == null || supplierID.trim().isEmpty()) {
            return ItemFileHandler.loadAllItems();
        }

        return ItemFileHandler.loadAllItems().stream()
            .filter(item -> item.getSupplierID().equalsIgnoreCase(supplierID.trim()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get low stock items
     */
    public static List<Item> getLowStockItems(int threshold) throws IOException {
        return ItemFileHandler.loadAllItems().stream()
            .filter(item -> item.getTotalStock() < threshold)
            .collect(Collectors.toList());
    }

    /**
     * Get items expiring within specified days
     */
    public static List<Item> getItemsExpiringWithin(int days) throws IOException {
        LocalDate cutoffDate = LocalDate.now().plusDays(days);

        return ItemFileHandler.loadAllItems().stream()
            .filter(item -> item.getExpiredDate() != null && 
                           !item.getExpiredDate().isAfter(cutoffDate))
            .collect(Collectors.toList());
    }
    
    /**
     * Get items by price range
     */
    public static List<Item> getItemsByPriceRange(double minPrice, double maxPrice) throws IOException {
        return ItemFileHandler.loadAllItems().stream()
            .filter(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice)
            .collect(Collectors.toList());
    }
    
    // ================== UTILITY METHODS ==================
    
    /**
     * Get available item categories
     */
    public static String[] getItemCategories() {
        return new String[] {
            "Fresh Produce", 
            "Groceries",
            "Beverages",
            "Bakery", 
            "Dairy Products",
            "Frozen Foods",
            "Meat & Seafood",
            "Snacks",
            "Household Essentials",
            "Personal Care Items"
        };
    }
    
    // ================== PRIVATE HELPER METHODS ==================
    
    private static boolean matchesSearchCriteria(Item item, String searchText) {
        String[] searchableFields = {
            item.getItemID() != null ? item.getItemID().toLowerCase() : "",
            item.getItemName() != null ? item.getItemName().toLowerCase() : "",
            String.valueOf(item.getPrice()).toLowerCase(),
            item.getCategory() != null ? item.getCategory().toLowerCase() : "",
            item.getExpiredDate() != null ? item.getExpiredDate().toString().toLowerCase() : "",
            item.getSupplierID() != null ? item.getSupplierID().toLowerCase() : "",
            String.valueOf(item.getTotalStock()).toLowerCase(),
            item.getUpdatedDate() != null ? item.getUpdatedDate().toString().toLowerCase() : ""
        };

        for (String field : searchableFields) {
            if (field.contains(searchText)) {
                return true;
            }
        }
        return false;
    }
}
