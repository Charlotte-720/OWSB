package SalesManager.DataHandlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseFileHandler {
    
// Common file operations
    protected static List<String[]> readAllRecords(String filename) throws IOException {
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
                            String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Supplies", "Active"};
                            records.add(parseFormattedLine(line, keys));
                        } else if (line.contains("Sale ID: ")) {
                            String[] keys = {"Sale ID", "Item ID", "Quantity Sold", "Sale Date", "Total Amount"};
                            records.add(parseFormattedLine(line, keys));
                        } else if (line.contains("PR ID: ")) {
                            String[] keys = {"PR ID", "PR Type", "Item ID", "Item Name", "Quantity", "Unit Price", "Total Price", "Supplier ID", "Raised By", "Required Delivery Date", "Request Date", "Status"};
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
    
    // Helper method to parse formatted line back to array
    protected static String[] parseFormattedLine(String line, String[] expectedKeys) {
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
    
    // Common method to write lines to a file
    protected static void writeLinesToFile(String fileName, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    // Check if file exists
    protected static boolean fileExists(String filename) {
        return new File(filename).exists();
    }
    
    // Create file if it doesn't exist
    protected static void createFileIfNotExists(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }
}
