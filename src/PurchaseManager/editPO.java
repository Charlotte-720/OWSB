package PurchaseManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class editPO {
    public static boolean editPO(JFrame parentFrame, String poId, String filePath) {
        String itemId = "", supplierId = "", item = "", quantity = "", unitPrice = "", date = "", status = "";

        // Read original values from file
        File file = new File(filePath);
        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lines.add(line);

                if (line.startsWith("PO_ID: " + poId)) {
                    String[] parts = line.split(", ");
                    for (String part : parts) {
                        if (part.startsWith("Item ID: ")) itemId = part.substring(9);
                        else if (part.startsWith("Supplier ID: ")) supplierId = part.substring(13);
                        else if (part.startsWith("Item Name: ")) item = part.substring(11);
                        else if (part.startsWith("Quantity: ")) quantity = part.substring(10);
                        else if (part.startsWith("Unit Price: ")) unitPrice = part.substring(12);
                        else if (part.startsWith("Date: ")) date = part.substring(6);
                        else if (part.startsWith("Status: ")) status = part.substring(8);
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

        if (!status.equalsIgnoreCase("Pending")) {
            JOptionPane.showMessageDialog(parentFrame,
                    "This PO cannot be edited because its status is: " + status);
            return false;
        }

        // UI fields (no supplier dropdown)
        JTextField itemField = new JTextField(item);
        JTextField quantityField = new JTextField(quantity);
        JTextField unitPriceField = new JTextField(unitPrice);
        unitPriceField.setEditable(false); // Not editable
        JTextField dateField = new JTextField(date);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Item Name:"));
        panel.add(itemField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Unit Price:"));
        panel.add(unitPriceField);
        panel.add(new JLabel("Date:"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Edit PO",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return false;

        // Get updated values
        String newItem = itemField.getText().trim();
        String newQuantity = quantityField.getText().trim();
        String newUnitPrice = unitPriceField.getText().trim(); // unchanged
        String newDate = dateField.getText().trim();

        if (newItem.isEmpty() || newQuantity.isEmpty() || newUnitPrice.isEmpty() || newDate.isEmpty()) {
            JOptionPane.showMessageDialog(parentFrame, "All fields must be filled.");
            return false;
        }

        int quantityInt;
        double unitPriceDouble;
        try {
            quantityInt = Integer.parseInt(newQuantity);
            unitPriceDouble = Double.parseDouble(newUnitPrice);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parentFrame, "Quantity and Unit Price must be numeric.");
            return false;
        }

        double totalPrice = quantityInt * unitPriceDouble;
        String totalPriceStr = String.format("%.2f", totalPrice);

        // Write updated lines
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                if (line.startsWith("PO_ID: " + poId)) {
                    line = "PO_ID: " + poId +
                            ", Item ID: " + itemId +
                            ", Supplier ID: " + supplierId +
                            ", Item Name: " + newItem +
                            ", Quantity: " + newQuantity +
                            ", Unit Price: " + newUnitPrice +
                            ", Total Price: " + totalPriceStr +
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
