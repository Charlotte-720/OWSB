package PurchaseManager.Function;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class deletefc {

    // Method to delete a PO from both table and file
    public boolean deletePO(JTable table, String poIdToDelete, String filePath) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = table.getRowCount();

        // Find the row to delete based on PO_ID
        int rowToDelete = -1;
        for (int i = 0; i < rowCount; i++) {
            String poId = (String) table.getValueAt(i, 0);  // Assuming PO_ID is in the first column
            if (poId.equals(poIdToDelete)) {
                rowToDelete = i;
                break;
            }
        }

        if (rowToDelete != -1) {
            // Get Supplier ID from the row before removing (assumed column 2)
            String supplierId = (String) table.getValueAt(rowToDelete, 2);

            model.removeRow(rowToDelete); // Remove the row from the table
            boolean deleted = removeDataFromFile(poIdToDelete, filePath);  // Remove from file

            if (deleted) {
                // Update PR status to Pending in pr.txt based on Supplier ID
                setPRStatusToPendingBySupplier(supplierId, "src/txtFile/pr.txt");  // Update path as needed
            }

            return deleted;
        }

        return false;  // PO_ID not found
    }

    // Method to remove the line containing the PO_ID from the file
    private boolean removeDataFromFile(String poIdToDelete, String filePath) {
        try {
            File file = new File(filePath);
            List<String> lines = new ArrayList<>();
            boolean deleted = false;

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("PO_ID: " + poIdToDelete)) {
                        deleted = true;  // skip this line
                    } else {
                        lines.add(line);
                    }
                }
            }

            if (deleted) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to set PR status to "Pending" in pr.txt based on Supplier ID
    private void setPRStatusToPendingBySupplier(String supplierId, String prFilePath) {
        try {
            File file = new File(prFilePath);
            List<String> updatedLines = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Check if line contains supplier ID anywhere
                    if (line.contains("Supplier ID: " + supplierId)) {
                        // Replace status anywhere in the line
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
