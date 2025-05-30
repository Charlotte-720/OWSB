package SalesManager.DataHandlers;

import static SalesManager.DataHandlers.BaseFileHandler.parseFormattedLine;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import model.Item;
import model.SalesRecord;

public class SalesRecordFileHandler {
    private static final String SALES_FILE = "src/txtFile/sales_records.txt";
    
    public static void saveSalesRecord(SalesRecord sale) throws IOException {
        if (sale == null) {
            throw new IllegalArgumentException("Sales record cannot be null");
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_FILE, true))) {
            String formattedSale = formatSalesRecordString(sale);
            writer.write(formattedSale);
            writer.newLine();
        }
    }
    
    private static String formatSalesRecordString(SalesRecord sale) {
        return "Sale ID: " + sale.getSaleID() +
               ", Item ID: " + sale.getItemID() +
               ", Quantity Sold: " + sale.getQuantitySold() +
               ", Sale Date: " + sale.getSaleDate() +
               ", Total Amount: " + String.format("%.2f", sale.getTotalAmount());
    }
    
    public static String generateSalesID() throws IOException {
        int lastSalesNumber = 0;
        
        if (!new File(SALES_FILE).exists()) {
            return "SAL001";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String saleID = extractSaleID(line);
                
                if (saleID.startsWith("SAL")) {
                    try {
                        int saleNumber = Integer.parseInt(saleID.substring(3));
                        if (saleNumber > lastSalesNumber) {
                            lastSalesNumber = saleNumber;
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid Sale IDs
                    }
                }
            }
        }

        return String.format("SAL%03d", lastSalesNumber + 1);
    }
    
    private static String extractSaleID(String line) {
        if (line.contains("Sale ID: ")) {
            int start = line.indexOf("Sale ID: ") + 9;
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
                    String[] parts = parseFormattedLine(line, keys);
                    
                    if (parts.length >= 5) {
                        records.add(parts);
                    }
                }
            }
        }
        return records;
    }
    
    public static List<String[]> readSalesRecords(File file) throws IOException {
        List<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    records.add(parts);
                }
            }
        }

        return records;
    }
    
    public static List<SalesRecord> loadAllSalesRecords() throws IOException {
        List<SalesRecord> salesList = new ArrayList<>();
        
        if (!new File(SALES_FILE).exists()) {
            return salesList;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                String[] parts = parseFormattedLine(line, keys);
                
                if (parts.length >= 5) {
                    try {
                        SalesRecord sale = new SalesRecord(
                            parts[0].trim(),                        // saleID
                            parts[1].trim(),                        // itemID
                            Integer.parseInt(parts[2].trim()),      // quantitySold
                            LocalDate.parse(parts[3].trim()),       // saleDate
                            Double.parseDouble(parts[4].trim())     // totalAmount
                        );
                        salesList.add(sale);
                    } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
                        // Skip invalid records
                        System.err.println("Skipping invalid sales record: " + line);
                    }
                }
            }
        }
        
        return salesList;
    }
    
    public SalesRecord getSalesRecordByID(String saleID) throws IOException {
        List<SalesRecord> allSales = loadAllSalesRecords();
        for (SalesRecord sale : allSales) {
            if (sale.getSaleID().equals(saleID)) {
                return sale;
            }
        }
        return null;
    }
    
    public String[] getSalesRecordById(String saleID) throws IOException {
        if (!new File(SALES_FILE).exists()) {
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String currentSaleID = extractSaleID(line);
                
                if (currentSaleID.equals(saleID)) {
                    String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                    return parseFormattedLine(line, keys);
                }
            }
        }
        return null;
    }
    
    public static void updateItemStockAfterSale(String itemID, int quantitySold) throws IOException {
        ItemFileHandler itemHandler = new ItemFileHandler();
        List<Item> items = itemHandler.loadAllItems();
        boolean itemFound = false;
        
        for (Item item : items) {
            if (item.getItemID().equals(itemID)) {
                int oldStock = item.getTotalStock();
                int newStock = oldStock - quantitySold;
                
                if (newStock < 0) {
                    throw new IllegalArgumentException("Insufficient stock for item: " + itemID);
                }
                
                item.setTotalStock(newStock);
                itemFound = true;
                break;
            }
        }
        
        if (!itemFound) {
            throw new IOException("Item not found: " + itemID);
        }
        
        itemHandler.saveAllItems(items);
    }
    
    public List<String[]> getSalesRecordsByItemID(String itemID) throws IOException {
        List<String[]> allRecords = readSalesRecords();
        List<String[]> itemSales = new ArrayList<>();
        
        for (String[] record : allRecords) {
            if (record.length >= 2 && record[1].equals(itemID)) {
                itemSales.add(record);
            }
        }
        return itemSales;
    }
    
    public boolean deleteSalesRecord(String saleID) throws IOException {
        if (!new File(SALES_FILE).exists()) {
            return false;
        }

        List<String> linesToKeep = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String currentSaleID = extractSaleID(line);
                
                if (!currentSaleID.equals(saleID)) {
                    linesToKeep.add(line);
                } else {
                    found = true;
                }
            }
        }

        if (found) {
            writeLinesToFile(linesToKeep);
        }
        
        return found;
    }
    
    public boolean updateSalesRecord(String saleID, String itemID, int quantitySold, 
            LocalDate saleDate, double totalAmount) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean found = false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SALES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String currentSaleID = extractSaleID(line);
                
                if (currentSaleID.equals(saleID)) {
                    found = true;
                    // Create updated line
                    line = "Sale ID: " + saleID +
                          ", Item ID: " + itemID +
                          ", Quantity Sold: " + quantitySold +
                          ", Sale Date: " + saleDate +
                          ", Total Amount: " + String.format("%.2f", totalAmount);
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
    
    private void writeLinesToFile(List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SALES_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    public static List<String[]> getMonthlySalesRecords() throws IOException {
        return readSalesRecords();
    }
     
    public static class MonthlySalesData {
        private List<Object[]> salesData;
        private double totalAmount;
        
        public MonthlySalesData(List<Object[]> salesData, double totalAmount) {
            this.salesData = salesData;
            this.totalAmount = totalAmount;
        }
        
        public List<Object[]> getSalesData() {
            return salesData;
        }
        
        public double getTotalAmount() {
            return totalAmount;
        }
    }
    
    public static List<String[]> getMonthlySalesRecords(YearMonth targetMonth) throws IOException {
        List<String[]> records = new ArrayList<>();
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        File file = new File("src/txtFile/sales_records.txt"); // or use SALES_FILE constant

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                    String[] parts;

                    if (line.contains(": ")) {
                        parts = parseFormattedLine(line, keys); // Your own method
                    } else {
                        parts = line.split(",", -1);
                    }

                    if (parts.length >= 5) {
                        String saleDate = parts[3].trim(); // Should be something like "2025-05-28"
                        if (saleDate.startsWith(targetMonth.format(monthFormatter))) {
                            records.add(parts);
                        }
                    }
                }
            }
        }
        return records;
    }

    
   public static MonthlySalesData loadDailyGroupedSales(YearMonth targetMonth) {
    Map<LocalDate, List<String[]>> salesByDate = new TreeMap<>();
    double monthlyTotal = 0.0;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    File file = new File("src/txtFile/sales_records.txt");

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;

        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(",");
                String saleID = parts[0].split(":")[1].trim();
                String saleDateStr = parts[3].split(":")[1].trim();
                String amountStr = parts[4].split(":")[1].trim();

                LocalDate saleDate = LocalDate.parse(saleDateStr, dateFormatter);
                if (YearMonth.from(saleDate).equals(targetMonth)) {
                    salesByDate.putIfAbsent(saleDate, new ArrayList<>());
                    salesByDate.get(saleDate).add(parts);
                }
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading salesrecord.txt: " + e.getMessage());
    }

    // Prepare data
    List<Object[]> tableData = new ArrayList<>();

    for (Map.Entry<LocalDate, List<String[]>> entry : salesByDate.entrySet()) {
        LocalDate date = entry.getKey();
        List<String[]> dailySales = entry.getValue();

        Set<String> saleIDs = new LinkedHashSet<>();
        double dailyTotal = 0.0;

        for (String[] sale : dailySales) {
            String saleID = sale[0].split(":")[1].trim();
            String amountStr = sale[4].split(":")[1].trim();

            saleIDs.add(saleID);
            dailyTotal += Double.parseDouble(amountStr);
        }

        monthlyTotal += dailyTotal;
        tableData.add(new Object[]{
            String.join(", ", saleIDs),
            date.toString(),
            String.format("RM%.2f", dailyTotal)
        });
    }

    return new MonthlySalesData(tableData, monthlyTotal);
}



}
