package PurchaseManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class editPO {
    public static boolean editPO(JFrame parentFrame, String poId, String filePath) {
        String supplierName = "", item = "", quantity = "", unitPrice = "", date = "", status = "";

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
                        if (part.startsWith("Supplier Name: ")) supplierName = part.substring(15);
                        else if (part.startsWith("Item: ")) item = part.substring(6);
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

        // Read suppliers from file and extract only Supplier Name
        List<String> supplierNames = new ArrayList<>();
        File supplierFile = new File("src/txtFile/suppliers.txt"); // Ensure this path is correct

        try (Scanner supplierScanner = new Scanner(supplierFile)) {
            while (supplierScanner.hasNextLine()) {
                String line = supplierScanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split(", ");
                    for (String part : parts) {
                        if (part.startsWith("Supplier Name: ")) {
                            String name = part.substring(15).trim();
                            supplierNames.add(name);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parentFrame, "Error reading supplier list.");
            return false;
        }

        JComboBox<String> supplierCombo = new JComboBox<>(supplierNames.toArray(new String[0]));
        supplierCombo.setSelectedItem(supplierName);

        // UI fields
        JTextField itemField = new JTextField(item);
        JTextField quantityField = new JTextField(quantity);
        JTextField unitPriceField = new JTextField(unitPrice);
        unitPriceField.setEditable(false); // Not editable
        JTextField dateField = new JTextField(date);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Supplier Name:"));
        panel.add(supplierCombo);
        panel.add(new JLabel("Item:"));
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

        // Get new values
        String newSupplier = (String) supplierCombo.getSelectedItem();
        String newItem = itemField.getText().trim();
        String newQuantity = quantityField.getText().trim();
        String newUnitPrice = unitPriceField.getText().trim(); // unchanged
        String newDate = dateField.getText().trim();

        if (newSupplier.isEmpty() || newItem.isEmpty() || newQuantity.isEmpty() || newUnitPrice.isEmpty() || newDate.isEmpty()) {
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
                            ", Supplier Name: " + newSupplier +
                            ", Item: " + newItem +
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
