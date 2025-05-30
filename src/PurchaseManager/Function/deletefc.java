package PurchaseManager.Function;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class deletefc {

    // Method to delete a specific PO line based on PO_ID and Item ID
    public boolean deletePO(JTable table, String poIdToDelete, String filePath) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = table.getRowCount();

        int rowToDelete = -1;
        for (int i = 0; i < rowCount; i++) {
            String poId = (String) table.getValueAt(i, 0);  // PO_ID is column 0
            if (poId.equals(poIdToDelete)) {
                rowToDelete = i;
                break;
            }
        }

        if (rowToDelete != -1) {
            String supplierId = (String) table.getValueAt(rowToDelete, 2); // Supplier ID is column 2
            String itemId = (String) table.getValueAt(rowToDelete, 1);     // Item ID is column 1

            model.removeRow(rowToDelete); // Remove from table

            boolean deleted = removeDataFromFile(poIdToDelete, itemId, filePath); // Remove from file

            if (deleted) {
                // Update PR status to Pending based on Supplier ID and Item ID
                setPRStatusToPendingBySupplierAndItem(supplierId, itemId, "src/txtFile/pr.txt");
            }

            return deleted;
        }

        return false;
    }

    // Remove the line from the file that matches both PO_ID and Item ID
    private boolean removeDataFromFile(String poIdToDelete, String itemIdToDelete, String filePath) {
        try {
            File file = new File(filePath);
            List<String> lines = new ArrayList<>();
            boolean deleted = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Match exact PO_ID and Item ID
                    if (!deleted && line.contains("PO_ID: " + poIdToDelete) && line.contains("Item ID: " + itemIdToDelete)) {
                        deleted = true; // Skip this line
                        continue;
                    }
                    lines.add(line);
                }
            }

            if (deleted) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            return deleted;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Set PR status to Pending in pr.txt based on Supplier ID and Item ID
    private void setPRStatusToPendingBySupplierAndItem(String supplierId, String itemId, String prFilePath) {
        try {
            File file = new File(prFilePath);
            List<String> updatedLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Match by Supplier ID and Item ID
                    if (line.contains("Supplier ID: " + supplierId) && line.contains("Item ID: " + itemId)) {
                        line = line.replaceAll("Status: [^,]*", "Status: Pending");
                    }
                    updatedLines.add(line);
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
