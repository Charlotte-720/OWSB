package PurchaseManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class addPO extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton saveButton;

    private final String prFilePath = "src/txtFile/pr.txt";
    private final String poFilePath = "src/txtFile/po.txt";

    private List<String> prLines = new ArrayList<>();

    private generateandviewpo parentFrame; // Match class name exactly

    // Constructor with parent reference
    public addPO(generateandviewpo   parentFrame) {
        this.parentFrame  = parentFrame;

        setTitle("Add Purchase Order");
        setSize(1000, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = {"Select", "Item ID", "Supplier ID", "Item Name", "Quantity", "Unit Price", "Total Price", "Required Delivery Date"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        saveButton = new JButton("Save PO");
        saveButton.addActionListener(e -> saveSelectedPRs());

        add(scrollPane, BorderLayout.CENTER);
        add(saveButton, BorderLayout.SOUTH);

        loadPRData();

        setVisible(true);
    }

    private void loadPRData() {
        prLines.clear();
        model.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader(prFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                prLines.add(line);
                if (line.toLowerCase().contains("status: pending")) {
                    String itemID = "", supplierID = "", itemName = "", quantity = "", unitPrice = "", totalPrice = "", requiredDeliveryDate = "";

                    String[] parts = line.split(", ");
                    for (String part : parts) {
                        String[] keyValue = part.split(":", 2);
                        if (keyValue.length < 2) continue;

                        String key = keyValue[0].trim().toLowerCase();
                        String value = keyValue[1].trim();

                        switch (key) {
                            case "item id":
                                itemID = value;
                                break;
                            case "supplier id":
                                supplierID = value;
                                break;
                            case "item name":
                                itemName = value;
                                break;
                            case "quantity":
                                quantity = value;
                                break;
                            case "unit price":
                                unitPrice = value;
                                break;
                            case "total price":
                                totalPrice = value;
                                break;
                            case "required delivery date":
                                requiredDeliveryDate = value;
                                break;
                        }
                    }

                    Object[] row = {
                        false, itemID, supplierID, itemName, quantity, unitPrice, totalPrice, requiredDeliveryDate
                    };
                    model.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading PR data.");
        }
    }

    private void saveSelectedPRs() {
        List<Integer> selectedRows = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selected = (Boolean) model.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) {
                selectedRows.add(i);
            }
        }

        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one PR to save.");
            return;
        }

        try (BufferedWriter poWriter = new BufferedWriter(new FileWriter(poFilePath, true))) {
            for (int row : selectedRows) {
                String poID = generateNextPOID();

                String poLine = String.format(
                        "PO_ID: %s, Item ID: %s, Supplier ID: %s, Item Name: %s, Quantity: %s, Unit Price: %s, Total Price: %s, Date: %s, Status: Pending",
                        poID,
                        model.getValueAt(row, 1),
                        model.getValueAt(row, 2),
                        model.getValueAt(row, 3),
                        model.getValueAt(row, 4),
                        model.getValueAt(row, 5),
                        model.getValueAt(row, 6),
                        model.getValueAt(row, 7)
                );

                poWriter.write(poLine);
                poWriter.newLine();

                updatePRStatusByItemID((String) model.getValueAt(row, 1));
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving PO data.");
            return;
        }

        rewritePRFile();

        JOptionPane.showMessageDialog(this, "PO(s) saved successfully!");

        // 🔁 REFRESH parent table
        if (parentFrame != null) {
            parentFrame.loadPOData();
        }

        dispose();
    }

    private String generateNextPOID() {
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

    private void updatePRStatusByItemID(String itemID) {
        for (int i = 0; i < prLines.size(); i++) {
            String line = prLines.get(i);
            if (line.contains("Item ID: " + itemID) && line.contains("Status: Pending")) {
                prLines.set(i, line.replace("Status: Pending", "Status: Submit"));
                break;
            }
        }
    }

    private void rewritePRFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(prFilePath))) {
            for (String line : prLines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating PR file.");
        }
    }

    // Test launcher
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new addPO(null)); // null if no parent
    }
}