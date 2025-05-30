package SalesManager.DataHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;


public class SupplierFileHandler extends BaseFileHandler{
    private static final String SUPPLIER_FILE = "src/txtFile/suppliers.txt";
    
    public static void saveSupplier(Supplier supplier) throws IOException {
        try (FileWriter writer = new FileWriter(SUPPLIER_FILE, true)) {
            writer.write(supplierToFormattedString(supplier) + "\n");
        }
    }

    private static String supplierToFormattedString(Supplier supplier) {
        return "Supplier ID: " + supplier.getSupplierID() +
               ", Supplier Name: " + supplier.getSupplierName() +
               ", Contact No: " + supplier.getContactNo() +
               ", Active: " + supplier.isActive();
    }
    
    public static String generateSupplierID() throws IOException {
        int lastSupplierNumber = 0;
        
        if (!fileExists(SUPPLIER_FILE)) {
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
                
                String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Active"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
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
        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
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
    public static void addSupplier(String name, String contactNo, boolean active) throws IOException {
        String supplierID = generateSupplierID();
        String supplierData = "Supplier ID: " + supplierID + 
                             ", Supplier Name: " + name + 
                             ", Contact No: " + contactNo + 
                             ", Active: " + active;
        
        try (FileWriter writer = new FileWriter(SUPPLIER_FILE, true)) {
            writer.write(supplierData + "\n");
        }
    }

    public static void updateSupplier(String supplierID, String name, String contactNo, boolean active) throws IOException {
        System.out.println("SupplierFileHandler: Updating supplier: ID=" + supplierID + ", Name=" + name);
        
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
                
                String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Active"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 4 && parts[0].trim().equals(supplierID)) {
                    return new Supplier(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Boolean.parseBoolean(parts[3].trim())
                    );
                }
            }
        }
        return null;
    }

    public static List<String> loadSupplierIDs() throws IOException {
        List<String> supplierIDs = new ArrayList<>();
        if (!fileExists(SUPPLIER_FILE)) {
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

        if (!fileExists(SUPPLIER_FILE)) {
            return suppliers;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Active"};
                String[] parts;
                
                if (line.contains(": ")) {
                    parts = parseFormattedLine(line, keys);
                } else {
                    parts = line.split(",", -1);
                }
                
                if (parts.length >= 4) {  
                    suppliers.add(new Supplier(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        Boolean.parseBoolean(parts[3].trim())
                    ));
                }
            }
        }
        return suppliers;
    }

    public static boolean isSupplierNameDuplicate(String supplierName, String currentSupplierID) throws IOException {
        if (!fileExists(SUPPLIER_FILE)) {
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SUPPLIER_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                
                String existingSupplierID = "";
                String existingSupplierName = "";
                
                if (line.contains(": ")) {
                    String[] keys = {"Supplier ID", "Supplier Name", "Contact No", "Active"};
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
}