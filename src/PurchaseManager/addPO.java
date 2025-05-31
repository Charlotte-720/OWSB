package PurchaseManager;

import PurchaseManager.Function.addFC;
import PurchaseManager.generateandviewpo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class addPO extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton saveButton;

    private final String prFilePath = "src/txtFile/pr.txt";
    private final String poFilePath = "src/txtFile/po.txt";

    private List<String> prLines = new ArrayList<>();

    private generateandviewpo parentFrame;

    public addPO(generateandviewpo parentFrame) {
        this.parentFrame = parentFrame;

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
        prLines = addFC.loadAllPRs(prFilePath);
        model.setRowCount(0);

        for (String line : prLines) {
            if (!line.contains("Status: Pending")) {
                continue;  // skip lines not pending
            }

            String itemID = "", supplierID = "", itemName = "", quantity = "", unitPrice = "", totalPrice = "", requiredDeliveryDate = "";

            String[] parts = line.split(", ");
            for (String part : parts) {
                String[] keyValue = part.split(":", 2);
                if (keyValue.length < 2) continue;

                String key = keyValue[0].trim().toLowerCase();
                String value = keyValue[1].trim();

                switch (key) {
                    case "item id" -> itemID = value;
                    case "supplier id" -> supplierID = value;
                    case "item name" -> itemName = value;
                    case "quantity" -> quantity = value;
                    case "unit price" -> unitPrice = value;
                    case "total price" -> totalPrice = value;
                    case "required delivery date" -> requiredDeliveryDate = value;
                }
            }

            Object[] row = {false, itemID, supplierID, itemName, quantity, unitPrice, totalPrice, requiredDeliveryDate};
            model.addRow(row);
        }
    }


private void saveSelectedPRs() {
    List<Integer> selectedRows = new ArrayList<>();
    Map<String, String> itemToSupplierMap = new HashMap<>();
    List<String> poLines = new ArrayList<>();

    // Collect selected rows
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

    try {
        // Load existing PO_IDs for all suppliers in the PO file
        Map<String, String> existingPOs = addFC.loadSupplierPOMap(poFilePath);
        Map<String, String> supplierPOMap = new HashMap<>(existingPOs);

        // Generate PO_IDs for new suppliers
        for (int row : selectedRows) {
            String supplierID = (String) model.getValueAt(row, 2);
            if (!supplierPOMap.containsKey(supplierID)) {
                String newPOID = addFC.generateNextPOID(poFilePath);
                supplierPOMap.put(supplierID, newPOID);
            }
        }

        // Build PO lines and item-supplier map
        for (int row : selectedRows) {
            String supplierID = (String) model.getValueAt(row, 2);
            String poID = supplierPOMap.get(supplierID);
            String itemID = (String) model.getValueAt(row, 1);

            itemToSupplierMap.put(itemID, supplierID);  // Link item to supplier

            String poLine = String.format(
                    "PO_ID: %s, Item ID: %s, Supplier ID: %s, Item Name: %s, Quantity: %s, Unit Price: %s, Total Price: %s, Date: %s, Status: Pending",
                    poID,
                    itemID,
                    supplierID,
                    model.getValueAt(row, 3),
                    model.getValueAt(row, 4),
                    model.getValueAt(row, 5),
                    model.getValueAt(row, 6),
                    model.getValueAt(row, 7)
            );

            poLines.add(poLine);
        }

        // Save PO lines and update PR status
        addFC.savePOs(poLines, poFilePath);
        prLines = addFC.updatePRStatusByItemIDs(prLines, itemToSupplierMap); // Use updated method
        addFC.rewritePRFile(prLines, prFilePath);

        JOptionPane.showMessageDialog(this, "PO(s) saved successfully!");

        if (parentFrame != null) {
            parentFrame.loadPOData();
        }

        dispose();
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error saving PO data.");
    }
}
  
}
