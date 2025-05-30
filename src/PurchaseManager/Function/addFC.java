package PurchaseManager.Backend;

import java.io.*;
import java.util.*;

public class addFC {

    public static List<String> loadPendingPRs(String prFilePath) {
        List<String> prLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(prFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("status: pending")) {
                    prLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prLines;
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

    public static List<String> updatePRStatusByItemIDs(List<String> originalPRLines, List<String> itemIDsToUpdate) {
        List<String> updated = new ArrayList<>(originalPRLines);
        for (int i = 0; i < updated.size(); i++) {
            for (String itemID : itemIDsToUpdate) {
                if (updated.get(i).contains("Item ID: " + itemID) && updated.get(i).contains("Status: Pending")) {
                    updated.set(i, updated.get(i).replace("Status: Pending", "Status: Submit"));
                }
            }
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
