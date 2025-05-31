package PurchaseManager.Function;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class addFC {

    public static List<String> loadAllPRs(String prFilePath) {
        List<String> allLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(prFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allLines;
    }


    public static String generateNextPOID(String poFilePath) {
        int maxID = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(poFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("PO_ID:")) {
                    String[] parts = line.split(",")[0].split(":");
                    String idStr = parts[1].trim().replaceAll("[^0-9]", "");
                    if (!idStr.isEmpty()) {
                        int id = Integer.parseInt(idStr);
                        if (id > maxID) maxID = id;
                    }
                }
            }
        } catch (IOException ignored) {}

        return String.format("%04d", maxID + 1);
    }

    public static void savePOs(List<String> poLines, String poFilePath) throws IOException {
        try (BufferedWriter poWriter = new BufferedWriter(new FileWriter(poFilePath, true))) {
            for (String poLine : poLines) {
                poWriter.write(poLine);
                poWriter.newLine();
            }
        }
    }

    public static List<String> updatePRStatusByItemIDs(List<String> prLines, Map<String, String> itemToSupplierMap) {
        List<String> updated = new ArrayList<>();
        Set<String> updatedItems = new HashSet<>(); // To avoid duplicate updates

        for (String line : prLines) {
            boolean isUpdated = false;

            for (Map.Entry<String, String> entry : itemToSupplierMap.entrySet()) {
                String itemID = entry.getKey();
                String supplierID = entry.getValue();

                if (line.contains("Item ID: " + itemID)
                    && line.contains("Supplier ID: " + supplierID)
                    && line.contains("Status: Pending")
                    && !updatedItems.contains(itemID + supplierID)) {

                    line = line.replace("Status: Pending", "Status: Submit");
                    updatedItems.add(itemID + supplierID);
                    isUpdated = true;
                    break;
                }
            }

            updated.add(line);
        }

        return updated;
    }


    public static void rewritePRFile(List<String> prLines, String prFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(prFilePath))) {
            for (String line : prLines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    // <-- NEW METHOD ADDED BELOW -->
    // Returns existing PO_ID for a supplier if found, else null
    public static String getPOIDBySupplier(String supplierID, String poFilePath) {
        String currentPOID = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(poFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("PO_ID:")) {
                    currentPOID = line.split(":")[1].trim();
                }
                if (line.contains("Supplier ID: " + supplierID)) {
                    return currentPOID; // Found PO_ID for this supplier
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Supplier not found
    }
    // Reads the PO file and returns a Map<SupplierID, PO_ID> for all existing POs
    public static Map<String, String> loadSupplierPOMap(String poFilePath) throws IOException {
        Map<String, String> map = new HashMap<>();
        List<String> lines = Files.readAllLines(Paths.get(poFilePath));
        for (String line : lines) {
            String supplierID = null;
            String poID = null;

            String[] parts = line.split(",\\s*");
            for (String part : parts) {
                String[] keyVal = part.split(":", 2);
                if (keyVal.length < 2) continue;

                String key = keyVal[0].trim().toLowerCase();
                String value = keyVal[1].trim();

                if ("supplier id".equals(key)) {
                    supplierID = value;
                } else if ("po_id".equals(key)) {
                    poID = value;
                }
            }

            if (supplierID != null && poID != null) {
                map.put(supplierID, poID);
            }
        }
        return map;
    }

}
