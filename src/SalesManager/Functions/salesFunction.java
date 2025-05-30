/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SalesManager.Functions;

import InventoryManager.functions.InventoryService;
import SalesManager.DataHandlers.ItemFileHandler;
import SalesManager.DataHandlers.SalesRecordFileHandler;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Item;
import model.SalesRecord;


public class salesFunction {
    private static final int LOW_STOCK_THRESHOLD = InventoryService.LOW_STOCK_THRESHOLD;
   
    private ItemFileHandler itemFileHandler;
    
    public salesFunction() {
        this.itemFileHandler = new ItemFileHandler();
}
//    load all items
    public static List<Item> loadAllItems() throws IOException {
        return ItemFileHandler.loadAllItems();
    }
    
//    get item by id
    public static Item getItemById(String itemId) {
        try {
            List<Item> allItems = ItemFileHandler.loadAllItems();
        for (Item item : allItems) {
            if (item.getItemID().equals(itemId)) {
                return item;
            }
        }
        return null;
        
    } catch (IOException e) {
        System.out.println("ERROR in getItemById: " + e.getMessage());
        e.printStackTrace();
        return null;
    }
}
    
//    check sufficient stock
    public static boolean isStockSufficient(String itemID, int requestedQuantity) throws IOException {
        Item item = getItemById(itemID);
        return item != null && item.getTotalStock() >= requestedQuantity;
    }
    
//    get available stock
    public static int getAvailableStock(String itemID) throws IOException {
        Item item = getItemById(itemID);
        return item != null ? item.getTotalStock() : 0;
    }
    
//    get item price by id
    public static double getItemPrice(String itemID) {
        Item item = getItemById(itemID);
        return item != null ? item.getPrice() : 0.0;
    }
    
//    validate sale input data
     public static int validateSaleInput(String itemID, String quantityText) throws IllegalArgumentException, IOException {
        if (itemID == null || itemID.trim().isEmpty()) {
            throw new IllegalArgumentException("Please select an item");
        }
        
        int quantity;
        try {
            quantity = Integer.parseInt(quantityText.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a valid quantity");
        }
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        // Check stock availability
        int availableStock = getAvailableStock(itemID);
        if (availableStock < quantity) {
            throw new IllegalArgumentException(String.format(
                "Insufficient stock!\nRequested: %d\nAvailable: %d\nPlease enter %d or less.",
                quantity, availableStock, availableStock
            ));
        }
        
        return quantity; // Return validated quantity
    }
     
//     process sale transaction
     
    public static SalesRecord processSale(String itemID, int quantity) throws IOException, IllegalArgumentException {
        // Get item price
        double price = getItemPrice(itemID);
        
        // Final stock check
        if (!isStockSufficient(itemID, quantity)) {
            int availableStock = getAvailableStock(itemID);
            throw new IllegalArgumentException(String.format(
                "Insufficient stock! Available: %d, Requested: %d", 
                availableStock, quantity));
        }
        
        // Generate sales ID and create record
        String salesID = SalesRecordFileHandler.generateSalesID();
        SalesRecord newSale = new SalesRecord(
            salesID,
            itemID,
            quantity,
            LocalDate.now(),
            price
        );
        
        // Save the sale
        SalesRecordFileHandler.saveSalesRecord(newSale);
        
        // Update stock
        SalesRecordFileHandler.updateItemStockAfterSale(itemID, quantity);
        
        return newSale; // Return the created sales record
    }
     
//     get salescount by item
    public Map<String, Integer> getSalesCountByItem() throws IOException {
        Map<String, Integer> itemStats = new HashMap<>();
        List<SalesRecord> records = SalesRecordFileHandler.loadAllSalesRecords();
        
        for (SalesRecord record : records) {
            itemStats.put(record.getItemID(), 
                itemStats.getOrDefault(record.getItemID(), 0) + record.getQuantitySold());
        }
        
        return itemStats;
    }
     
//   get monthly sales statistics
    public Map<String, Double> getMonthlySalesStatistics() throws IOException {
        Map<String, Double> monthlyStats = new HashMap<>();
        List<SalesRecord> records = SalesRecordFileHandler.loadAllSalesRecords();
        
        for (SalesRecord record : records) {
            String monthYear = record.getSaleDate().getYear() + "-" + 
                              String.format("%02d", record.getSaleDate().getMonthValue());
            
            monthlyStats.put(monthYear, 
                monthlyStats.getOrDefault(monthYear, 0.0) + record.getTotalAmount());
        }
        
        return monthlyStats;
    }
     
//     get total sales amount
    public double getTotalSalesAmount() throws IOException {
        List<String[]> records = SalesRecordFileHandler.readSalesRecords();
        double total = 0.0;
        
        for (String[] record : records) {
            if (record.length >= 5) {
                try {
                    double amount = Double.parseDouble(record[4]);
                    total += amount;
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        
        return total;
    }
     
//     get salesrecords by date range
    public static List<SalesRecord> getSalesRecordsByDateRange(LocalDate startDate, LocalDate endDate) throws IOException {
        List<SalesRecord> allRecords = SalesRecordFileHandler.loadAllSalesRecords();
        List<SalesRecord> dateRangeSales = new ArrayList<>();
        
        for (SalesRecord record : allRecords) {
            LocalDate saleDate = record.getSaleDate();
            if (!saleDate.isBefore(startDate) && !saleDate.isAfter(endDate)) {
                dateRangeSales.add(record);
            }
        }
        return dateRangeSales;
    }
    
    
    public Map<LocalDate, Double> getDailySalesForMonth(YearMonth yearMonth) throws IOException {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<SalesRecord> records = getSalesRecordsByDateRange(startDate, endDate);
        Map<LocalDate, Double> dailySales = new HashMap<>();
        
        for (SalesRecord record : records) {
            dailySales.merge(record.getSaleDate(), record.getTotalAmount(), Double::sum);
        }
        
        return dailySales;
    }
    
    /**
     * Check if item has sufficient stock
     */
    public boolean checkStockAvailability(String itemID, int requestedQuantity) {
        Item item = getItemById(itemID);
        return item != null && item.getTotalStock() >= requestedQuantity;
    }
    
    /**
     * Get low stock items (items with stock below threshold)
     */
    public List<Item> getLowStockItems(int threshold) throws IOException {
        List<Item> allItems = itemFileHandler.loadAllItems();
        List<Item> lowStockItems = new ArrayList<>();
        
        for (Item item : allItems) {
            if (item.getTotalStock() <= threshold) {
                lowStockItems.add(item);
            }
        }
        
        return lowStockItems;
    }

    public static List<String> getCurrentLowStockAlerts() throws IOException {
        List<String> lowStockAlerts = new ArrayList<>();
        salesFunction sf = new salesFunction();
        List<Item> lowStockItems = sf.getLowStockItems(LOW_STOCK_THRESHOLD);

        for (Item item : lowStockItems) {
            lowStockAlerts.add("Item ID: " + item.getItemName() + " (" + item.getItemID() + ")\nStock left: " + item.getTotalStock());
        }

        return lowStockAlerts;
    }

    
    public static double getTodaysTotalSales() throws IOException {
        List<SalesRecord> allRecords = SalesRecordFileHandler.loadAllSalesRecords();
        LocalDate today = LocalDate.now();
        double total = 0.0;
        
        for (SalesRecord record : allRecords) {
            if (record.getSaleDate().equals(today)) {
                total += record.getTotalAmount();
            }
        }
        
        return total;
    }
    
    public static int getTodaysSalesCount() throws IOException {
        List<SalesRecord> allRecords = SalesRecordFileHandler.loadAllSalesRecords();
        LocalDate today = LocalDate.now();
        int count = 0;
        
        for (SalesRecord record : allRecords) {
            if (record.getSaleDate().equals(today)) {
                count++;
            }
        }
        
        return count;
    }
}
    