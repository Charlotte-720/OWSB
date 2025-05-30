package PurchaseManager.Function;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class editFC {

    public static Map<String, String> getPOData(String poId, String filePath) {
        Map<String, String> poData = new HashMap<>();
        File file = new File(filePath);

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("PO_ID: " + poId)) {
                    String[] parts = line.split(", ");
                    for (String part : parts) {
                        String[] kv = part.split(": ", 2);
                        if (kv.length == 2) {
                            poData.put(kv[0].trim(), kv[1].trim());
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return poData;
    }

    public static boolean updatePO(String poId, Map<String, String> updatedData, String filePath) {
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                if (line.startsWith("PO_ID: " + poId)) {
                    String newLine = "PO_ID: " + poId +
                            ", Item ID: " + updatedData.get("Item ID") +
                            ", Supplier ID: " + updatedData.get("Supplier ID") +
                            ", Item Name: " + updatedData.get("Item Name") +
                            ", Quantity: " + updatedData.get("Quantity") +
                            ", Unit Price: " + updatedData.get("Unit Price") +
                            ", Total Price: " + updatedData.get("Total Price") +
                            ", Date: " + updatedData.get("Date") +
                            ", Status: Pending";
                    writer.write(newLine);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
