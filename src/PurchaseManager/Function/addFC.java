package PurchaseManager.Function;

import java.io.*;
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

    public static List<String> updatePRStatusByItemIDs(List<String> prLines, List<String> itemIDsToUpdate) {
        List<String> updated = new ArrayList<>();
        for (String line : prLines) {
            boolean updatedLine = false;
            for (String itemID : itemIDsToUpdate) {
                if (line.contains("Item ID: " + itemID) && line.contains("Status: Pending")) {
                    line = line.replace("Status: Pending", "Status: Submit");
                    updatedLine = true;
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
}
