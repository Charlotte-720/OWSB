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
   
    public AddItem(ItemEntry entryWindow) {
    this.entryWindow = entryWindow;
    // load items
    try {
        itemList = FileHandler.loadAllItems();
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading items: " + e.getMessage(),
            "File Error",
            JOptionPane.ERROR_MESSAGE);
        itemList = new ArrayList<>();
    }
    initComponents();
    populateSupplierID();
}
    
    private String generateItemID() {
        try {
            return FileHandler.generateItemID();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error generating ID", "Error", JOptionPane.ERROR_MESSAGE);
            return "ITM" + System.currentTimeMillis(); // Fallback
        }
    }
    
    // Method to load supplier IDs from the file
    private void populateSupplierID() {
        try {
            List<String> supplierIDs = FileHandler.loadSupplierIDs();
            supplierID.setModel(new DefaultComboBoxModel<>(supplierIDs.toArray(new String[0])));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers", "Error", JOptionPane.ERROR_MESSAGE);
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
        itemName = new javax.swing.JTextField();
        itemPrice = new javax.swing.JTextField();
        NameLabel = new javax.swing.JLabel();
        itemQuantity = new javax.swing.JTextField();
        PriceLabel = new javax.swing.JLabel();
        itemCategory = new javax.swing.JTextField();
        QuantityLabel = new javax.swing.JLabel();
        expiredDate = new javax.swing.JTextField();
        CategoryLabel = new javax.swing.JLabel();
        supplierID = new javax.swing.JComboBox<>();
        ExpiredDateLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        SupplierLabel = new javax.swing.JLabel();
        ClearButton = new javax.swing.JButton();

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

        itemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNameActionPerformed(evt);
            }
        });

        NameLabel.setText("Name");

        PriceLabel.setText("Price (RM)");

        QuantityLabel.setText("Quantity");

        expiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredDateActionPerformed(evt);
            }
        });

        CategoryLabel.setText("Category");

        supplierID.setEditable(true);
        supplierID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        supplierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIDActionPerformed(evt);
            }
        });

        ExpiredDateLabel.setText("Expired Date");

        saveButton.setBackground(new java.awt.Color(255, 153, 153));
        saveButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        SupplierLabel.setText("Supplier ID");

        ClearButton.setBackground(new java.awt.Color(153, 204, 255));
        ClearButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(PriceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(NameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(QuantityLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(CategoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(SupplierLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(saveButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemPrice)
                    .addComponent(itemQuantity)
                    .addComponent(itemCategory)
                    .addComponent(expiredDate)
                    .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PriceLabel)
                    .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityLabel)
                    .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CategoryLabel)
                    .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ExpiredDateLabel)
                    .addComponent(expiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupplierLabel)
                    .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
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

    private void itemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemNameActionPerformed

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
    
    private boolean validateInputs() {
        // Simple validation - expand as needed
        if (itemName.getText().trim().isEmpty()) {
            showError("Item name cannot be empty");
            return false;
        }
        try {
            Double.parseDouble(itemPrice.getText());
            Integer.parseInt(itemQuantity.getText());
            LocalDate.parse(expiredDate.getText()); // Validate date format
        } catch (Exception e) {
            showError("Invalid number or date format");
            return false;
        }
        return true;
    }
    
    private Item createItemFromInputs() {
        return new Item(
            generateItemID(),                     // Auto-generated
            itemName.getText().trim(),             // Name
            Double.parseDouble(itemPrice.getText()), // Price
            itemCategory.getText().trim(),         // Category
            LocalDate.parse(expiredDate.getText()), // Expiry
            (String) supplierID.getSelectedItem(), // Supplier
            Integer.parseInt(itemQuantity.getText()), // Quantity
            LocalDate.now()                       // Current date
        );
    }

    private Item findExistingItem(String name, String category, String supplierID, double price) {
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
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

    private void updateItemStock(String itemID, int newQuantity, LocalDate newExpiredDate) {
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
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
        
        try (FileWriter writer = new FileWriter("items.txt")) {
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
        
        try (FileWriter writer = new FileWriter("items.txt", true)) {
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

    private void supplierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIDActionPerformed
        
    }//GEN-LAST:event_supplierIDActionPerformed

    private void expiredDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiredDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expiredDateActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
         clearFields();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void clearFields() {
        itemName.setText("");
        itemPrice.setText("");
        itemQuantity.setText("");                                             
        itemCategory.setText("");
        expiredDate.setText("");
        supplierID.setSelectedIndex(0);
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
    private javax.swing.JTextField itemCategory;
    private javax.swing.JTextField itemName;
    private javax.swing.JTextField itemPrice;
    private javax.swing.JTextField itemQuantity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox<String> supplierID;
    // End of variables declaration//GEN-END:variables
}