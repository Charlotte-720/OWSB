package PurchaseManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class editPO {
    public static boolean editPO(JFrame parentFrame, String poId, String filePath) {
        String supplierName = "", item = "", quantity = "", date = "";

        // Read original values from file
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);

                if (line.startsWith("PO_ID: " + poId)) {
                    // Parse values
                    String[] parts = line.split(", ");
                    for (String part : parts) {
                        if (part.startsWith("Supplier Name: ")) supplierName = part.substring(15);
                        else if (part.startsWith("Item: ")) item = part.substring(6);
                        else if (part.startsWith("Quantity: ")) quantity = part.substring(10);
                        else if (part.startsWith("Date: ")) date = part.substring(6);
                    }
                    found = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Error reading PO file.");
            return false;
        }

        if (!found) {
            JOptionPane.showMessageDialog(parentFrame, "PO_ID not found.");
            return false;
        }

        // Show editable fields
        JTextField supplierField = new JTextField(supplierName);
        JTextField itemField = new JTextField(item);
        JTextField quantityField = new JTextField(quantity);
        JTextField dateField = new JTextField(date);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Supplier Name:"));
        panel.add(supplierField);
        panel.add(new JLabel("Item:"));
        panel.add(itemField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Edit PO",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return false;

        // Get new values
        String newSupplier = supplierField.getText().trim();
        String newItem = itemField.getText().trim();
        String newQuantity = quantityField.getText().trim();
        String newDate = dateField.getText().trim();

        if (newSupplier.isEmpty() || newItem.isEmpty() || newQuantity.isEmpty() || newDate.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "All fields must be filled.");
            return false;
        }

        // Write back modified content
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                if (line.startsWith("PO_ID: " + poId)) {
                    line = "PO_ID: " + poId +
                            ", Supplier Name: " + newSupplier +
                            ", Item: " + newItem +
                            ", Quantity: " + newQuantity +
                            ", Date: " + newDate +
                            ", Status: Pending";
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Error writing to file.");
            return false;
        }

        JOptionPane.showMessageDialog(parentFrame, "PO updated successfully!");
        return true;
    }
}
