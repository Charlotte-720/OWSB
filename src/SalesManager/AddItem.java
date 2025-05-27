package SalesManager;

import model.Item;
import javax.swing.*;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddItem extends javax.swing.JFrame {
   private ItemEntry entryWindow;
   private List<Item> itemList;
   private List<POItem> paidPOItems;
   
   // Inner class to represent PO items
   private static class POItem {
       String itemName;
       int itemQuantity;
       double itemPrice;
       String status;
       String supplierName;
       
       POItem(String itemName, int itemQuantity, double itemPrice, String status, String supplierName) {
           this.itemName = itemName;
           this.itemQuantity = itemQuantity;
           this.itemPrice = itemPrice;
           this.status = status;
           this.supplierName = supplierName;
       }
   }
   
    public AddItem(ItemEntry entryWindow) {
    this.entryWindow = entryWindow;
    // load items
    try {
        itemList = FileHandler.loadAllItems();
        paidPOItems = loadPaidPOItems();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading items: " + e.getMessage(),
            "File Error",
            JOptionPane.ERROR_MESSAGE);
        itemList = new ArrayList<>();
        paidPOItems = new ArrayList<>();
    }
    initComponents();
    populateItemNameFromPO();
    setupItemNameListener();
    populateItemCategories();
}
    
    private void populateItemCategories() {
        String[] categories = {
            "Groceries",
            "Fresh Produce",
            "Dairy Products", 
            "Frozen Foods",
            "Meat & Seafood",
        };
        itemCategory.setModel(new DefaultComboBoxModel<>(categories));
    }
    
    // Method to load items with PAID status from po.txt
    private List<POItem> loadPaidPOItems() throws IOException {
        List<POItem> paidItems = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/po.txt"))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String itemName = extractValue(line, "Item:");
                    String quantityStr = extractValue(line, "Quantity:");
                    String priceStr = extractValue(line, "Unit Price:");
                    String status = extractValue(line, "Status:");
                    String supplierName = extractValue(line, "Supplier Name:");

                    int itemQuantity = Integer.parseInt(quantityStr.trim());
                    double itemPrice = Double.parseDouble(priceStr.trim());

                    // Check if status is PAID 
                    if ("Paid".equalsIgnoreCase(status.trim())) {
                        POItem newItem = new POItem(itemName.trim(), itemQuantity, itemPrice, status.trim(),supplierName.trim());
                        paidItems.add(newItem);
                    }

                } catch (Exception e) {
                    continue; // Skip this record and continue with next line
                }
            }
        } catch (IOException e) {
            throw e;
        }

        return paidItems;
    }

    // Helper method to extract values from the formatted line
    private String extractValue(String line, String label) {
        int startIndex = line.indexOf(label);
        if (startIndex == -1) {
            throw new RuntimeException("Label '" + label + "' not found in line: " + line);
        }

        startIndex += label.length();

        // Skip any whitespace after the label
        while (startIndex < line.length() && Character.isWhitespace(line.charAt(startIndex))) {
            startIndex++;
        }

        int endIndex = line.indexOf(",", startIndex);

        String value;
        if (endIndex == -1) {
            // Last value in the line
            value = line.substring(startIndex).trim();
        } else {
            value = line.substring(startIndex, endIndex).trim();
        }

        return value;
    }
    
    // Add this method to handle item selection changes
    private void setupItemNameListener() {
        if (itemName instanceof JComboBox) {
            ((JComboBox<String>) itemName).addActionListener(e -> {
                String selectedItem = (String) ((JComboBox<String>) itemName).getSelectedItem();
                if (selectedItem != null && !selectedItem.equals("Select Item")) {
                    updatePriceAndQuantityLabels(selectedItem);
                } else {
                    // Clear labels when no item is selected
                    itemPrice.setText(" ");
                    itemQuantity.setText(" ");
                }
            });
        }
    }
    
    // Method to update price and quantity labels based on selected item
    private void updatePriceAndQuantityLabels(String selectedItemName) {
        boolean found = false;

        for (POItem poItem : paidPOItems) {
            if (poItem.itemName.equals(selectedItemName)) {
                itemPrice.setText(String.format("%.2f", poItem.itemPrice));
                itemQuantity.setText(String.valueOf(poItem.itemQuantity));
                supplierName.setText(poItem.supplierName);
                System.out.println("Match found! Price: " + poItem.itemPrice + ", Quantity: " + poItem.itemQuantity);
                found = true;
                break;
            }
        }

        if (!found) {
            itemPrice.setText(" ");
            itemQuantity.setText(" ");
            supplierName.setText(" ");
        }
    }
    
    // Method to populate item name combobox with PAID PO items
    private void populateItemNameFromPO() {
        List<String> itemNames = new ArrayList<>();
        itemNames.add("Select Item"); // Default option
        
        for (POItem poItem : paidPOItems) {
            if (!itemNames.contains(poItem.itemName)) {
                itemNames.add(poItem.itemName);
            }
        }
        if (itemName instanceof JComboBox) {
            ((JComboBox<String>) itemName).setModel(
                new DefaultComboBoxModel<>(itemNames.toArray(new String[0]))
            );
        }
    }
    
    // Method to get supplier name for selected item from PO
    private String getSupplierNameForSelectedItem(String selectedItemName) {
        for (POItem poItem : paidPOItems) {
            if (poItem.itemName.equals(selectedItemName)) {
                return poItem.supplierName;
            }
        }
        return "";
    }
    
    // Method to get price for selected item from PO
    private double getPriceForSelectedItem(String selectedItemName) {
        for (POItem poItem : paidPOItems) {
            if (poItem.itemName.equals(selectedItemName)) {
                return poItem.itemPrice;
            }
        }
        return 0.0;
    }
        
    private String generateItemID() {
        try {
            return FileHandler.generateItemID();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating ID", "Error", JOptionPane.ERROR_MESSAGE);
            return "ITM" + System.currentTimeMillis(); // Fallback
        }
    }
    
    
    private boolean isDuplicateItem(String itemNameToCheck) {
        try {
            return FileHandler.itemExists(itemNameToCheck);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error checking duplicates", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ItemEntryPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        NameLabel = new javax.swing.JLabel();
        PriceLabel = new javax.swing.JLabel();
        QuantityLabel = new javax.swing.JLabel();
        expiredDate = new javax.swing.JTextField();
        CategoryLabel = new javax.swing.JLabel();
        ExpiredDateLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        SupplierLabel = new javax.swing.JLabel();
        ClearButton = new javax.swing.JButton();
        itemCategory = new javax.swing.JComboBox<>();
        itemName = new javax.swing.JComboBox<>();
        itemPrice = new javax.swing.JLabel();
        itemQuantity = new javax.swing.JLabel();
        supplierName = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        ItemEntryPanel.setBackground(new java.awt.Color(255, 204, 153));

        jLabel1.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Item Entry");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel2.setText("X");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ItemEntryPanelLayout = new javax.swing.GroupLayout(ItemEntryPanel);
        ItemEntryPanel.setLayout(ItemEntryPanelLayout);
        ItemEntryPanelLayout.setHorizontalGroup(
            ItemEntryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemEntryPanelLayout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        ItemEntryPanelLayout.setVerticalGroup(
            ItemEntryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemEntryPanelLayout.createSequentialGroup()
                .addGroup(ItemEntryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ItemEntryPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ItemEntryPanelLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jLabel1)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        NameLabel.setText("Name");

        PriceLabel.setText("Price (RM)");

        QuantityLabel.setText("Quantity");

        expiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredDateActionPerformed(evt);
            }
        });

        CategoryLabel.setText("Category");

        ExpiredDateLabel.setText("Expired Date");

        saveButton.setBackground(new java.awt.Color(255, 153, 153));
        saveButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        SupplierLabel.setText("Supplier Name");

        ClearButton.setBackground(new java.awt.Color(153, 204, 255));
        ClearButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        itemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCategoryActionPerformed(evt);
            }
        });

        itemName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNameActionPerformed(evt);
            }
        });

        itemPrice.setText(" ");

        itemQuantity.setText(" ");

        supplierName.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(PriceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(NameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(QuantityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CategoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(SupplierLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addGap(57, 57, 57)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(expiredDate)
                    .addComponent(itemCategory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supplierName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameLabel)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PriceLabel)
                    .addComponent(itemPrice))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityLabel)
                    .addComponent(itemQuantity))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CategoryLabel)
                    .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ExpiredDateLabel)
                    .addComponent(expiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupplierLabel)
                    .addComponent(supplierName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            if (!validateInputs()) return;
            
            Item newItem = createItemFromInputs();
            
            if (isDuplicateItem(newItem.getItemName())) {
                JOptionPane.showMessageDialog(this, "Item name already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            FileHandler.saveItemToFile(newItem);
            entryWindow.loadItemsToTable();
            JOptionPane.showMessageDialog(this, "Item saved successfully!");
            clearFields();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Item createItemFromInputs() {
        return new Item(
            generateItemID(),                           // Auto-generated
            itemName.getSelectedItem().toString(),      // Name from combobox
            Double.parseDouble(itemPrice.getText()),    // Price from label
            itemCategory.getSelectedItem().toString(),  // Category
            LocalDate.parse(expiredDate.getText()),     // Expiry
            supplierName.getText(),      // Supplier
            Integer.parseInt(itemQuantity.getText()),   // Quantity from label
            LocalDate.now()                            // Current date
        );
    }
    
    // Updated validation method
    private boolean validateInputs() {
        // Validation for combobox selection
        if (itemName.getSelectedItem() == null || 
            itemName.getSelectedItem().toString().equals("Select Item")) {
            showError("Please select an item name");
            return false;
        }

        // Check if price and quantity labels have been populated
        if (itemPrice.getText().trim().isEmpty() || itemPrice.getText().equals(" ")) {
            showError("Please select a valid item to get price information");
            return false;
        }

        if (itemQuantity.getText().trim().isEmpty() || itemQuantity.getText().equals(" ")) {
            showError("Please select a valid item to get quantity information");
            return false;
        }

        try {
            Double.parseDouble(itemPrice.getText());
            Integer.parseInt(itemQuantity.getText());
            LocalDate expiredDate = validateExpiredDate();
            if (expiredDate == null) 
                return false;
            
        } catch (Exception e) {
            showError("Invalid number or date format");
            return false;
        }
        return true;
    }

    private Item findExistingItem(String name, String category, String supplierID, double price) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 8) {
                    Item existingItem = new Item(
                        parts[0].trim(),        // itemID
                        parts[1].trim(),        // name
                        Double.parseDouble(parts[2].trim()), // price
                        parts[3].trim(),        // category
                        LocalDate.parse(parts[4].trim()), // expiredDate
                        parts[5].trim(),        // supplierID
                        Integer.parseInt(parts[6].trim()), // totalStock
                        LocalDate.parse(parts[7].trim())  // updatedDate
                    );

                    if (existingItem.getItemName().equalsIgnoreCase(name) &&
                        existingItem.getCategory().equalsIgnoreCase(category) &&
                        existingItem.getSupplierID().equals(supplierID) &&
                        Math.abs(existingItem.getPrice() - price) < 0.001) {
                        return existingItem;
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleExistingItem(Item existingItem, int additionalQuantity, LocalDate newExpiredDate) {
        int option = JOptionPane.showConfirmDialog(
            this,
            "This item already exists (ID: " + existingItem.getItemID() + ").\n" +
            "Current stock: " + existingItem.getTotalStock() + "\n\n" +
            "Do you want to add to existing stock?",
            "Item Exists",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            updateItemStock(
                existingItem.getItemID(),
                existingItem.getTotalStock() + additionalQuantity,
                newExpiredDate
            );
            showSuccess("Stock updated successfully!");
            clearFields();
        } else {
            createNewItem(
                existingItem.getItemName(),
                existingItem.getPrice(),
                additionalQuantity,
                existingItem.getCategory(),
                newExpiredDate,
                existingItem.getSupplierID()
            );
        }
    }
    
    private LocalDate validateExpiredDate() {
        try {
            String dateText = expiredDate.getText().trim();
            if (dateText.isEmpty()) {
                showError("Please enter expired date");
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expired = LocalDate.parse(dateText, formatter);
            LocalDate today = LocalDate.now();

            if (expired.isBefore(today)) {
                showError("Expired date cannot be in the past!");
                return null;
            }
            return expired;
        } catch (Exception e) {
            showError("Invalid date format. Please use YYYY-MM-DD");
            return null;
        }
    }

    private void updateItemStock(String itemID, int newQuantity, LocalDate newExpiredDate) {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 8 && parts[0].trim().equals(itemID)) {
                    parts[6] = String.valueOf(newQuantity);
                    parts[4] = newExpiredDate.toString();
                    parts[7] = LocalDate.now().toString();
                    line = String.join(",", parts);
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try (FileWriter writer = new FileWriter("src/txtFile/items.txt")) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createNewItem(String name, double price, int quantity, 
                             String category, LocalDate expiredDate, String supplierID) {
        String itemID = generateItemID();
        LocalDate createdDate = LocalDate.now();
        
        String itemLine = String.format("%s,%s,%.2f,%s,%s,%s,%d,%s",
                itemID, name, price, category, expiredDate, supplierID, quantity, createdDate);
        
        try (FileWriter writer = new FileWriter("src/txtFile/items.txt", true)) {
            writer.write(itemLine + "\n");
            showSuccess("New item created successfully!");
            clearFields();
        } catch (IOException e) {
            showError("Error saving item: " + e.getMessage());
        }
    }
                                                

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);

    
    }//GEN-LAST:event_saveButtonActionPerformed

    private void expiredDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiredDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expiredDateActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
         clearFields();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void itemCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCategoryActionPerformed

    private void itemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemNameActionPerformed

    private void clearFields() {
        itemName.setSelectedIndex(0);
        itemPrice.setText("");
        itemQuantity.setText("");                                             
        itemCategory.setSelectedIndex(0);
        expiredDate.setText("");
        supplierName.setText(""); 
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> new AddItem().setVisible(true));
//    }  
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CategoryLabel;
    private javax.swing.JButton ClearButton;
    private javax.swing.JLabel ExpiredDateLabel;
    private javax.swing.JPanel ItemEntryPanel;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JLabel PriceLabel;
    private javax.swing.JLabel QuantityLabel;
    private javax.swing.JLabel SupplierLabel;
    private javax.swing.JTextField expiredDate;
    private javax.swing.JComboBox<String> itemCategory;
    private javax.swing.JComboBox<String> itemName;
    private javax.swing.JLabel itemPrice;
    private javax.swing.JLabel itemQuantity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel supplierName;
    // End of variables declaration//GEN-END:variables
}