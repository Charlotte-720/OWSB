package PurchaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class deletePO {

    // Method to delete a PO from both table and file
    public boolean deletePO(JTable table, String poIdToDelete, String filePath) {
        // Remove the row from the JTable
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

        // If the PO_ID is found in the table, remove the row
        if (rowToDelete != -1) {
            model.removeRow(rowToDelete); // Removes the row from the table
            return removeDataFromFile(poIdToDelete, filePath);  // Removes the corresponding data from the file
        }

        return false;  // Return false if PO_ID not found in the table
    }

    // Method to remove the line containing the PO_ID from the file
    private boolean removeDataFromFile(String poIdToDelete, String filePath) {
        try {
            File file = new File(filePath);
            List<String> lines = new ArrayList<>();
            boolean deleted = false;

            // Read the file and collect all lines except the one to delete
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("PO_ID: " + poIdToDelete)) {
                        // Skip this line if it's the one to delete
                        deleted = true;
                    } else {
                        // Add line to list
                        lines.add(line);
                    }
                }
            }

            // If the line was found and deleted
            if (deleted) {
                // Write back the remaining lines to the file (overwriting it)
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (String line : lines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                return true;  // Return true if deletion was successful
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;  // Return false if there was an error
    }
}

