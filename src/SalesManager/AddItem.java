package SalesManager;

import SalesManager.DataHandlers.ItemFileHandler;
import SalesManager.DataHandlers.ItemFileHandler.POItem;
import SalesManager.Functions.itemFunction;
import model.Item;
import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddItem extends javax.swing.JFrame {
   private ItemEntry entryWindow;
   private List<Item> itemList;
   private List<POItem> paidPOItems;
   
public AddItem(ItemEntry entryWindow) {
        this.entryWindow = entryWindow;
        
        // Load items using functions
        try {
            itemList = ItemFileHandler.loadAllItems();
            paidPOItems = ItemFileHandler.loadPaidPOItems();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading items: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
            itemList = new ArrayList<>();
            paidPOItems = new ArrayList<>();
        }
        
        initComponents();
        setupUI();
    }

    private void setupUI() {
        populateItemNameFromPO();
        setupItemNameListener();
        populateItemCategories();
    }
    
    private void populateItemCategories() {
        String[] categories = itemFunction.getItemCategories();
        itemCategory.setModel(new DefaultComboBoxModel<>(categories));
    }
    
    private void populateItemNameFromPO() {
        List<String> itemNames = itemFunction.extractUniqueItemNames(paidPOItems);
        if (itemName instanceof JComboBox) {
            ((JComboBox<String>) itemName).setModel(
                new DefaultComboBoxModel<>(itemNames.toArray(new String[0]))
            );
        }
    }
    
    private void setupItemNameListener() {
        itemName.addActionListener(e -> {
            String selectedItemName = (String) itemName.getSelectedItem();
            if (selectedItemName != null && !selectedItemName.equals("Select Item")) {
                updatePriceAndQuantityLabels(selectedItemName);
            } else {
                clearPriceAndQuantityLabels();
            }
        });
    }
    
    private void updatePriceAndQuantityLabels(String selectedItemName) {
        POItem foundItem = itemFunction.findPOItemByName(paidPOItems, selectedItemName);

        if (foundItem != null) {
            itemPrice.setText(String.format("%.2f", foundItem.itemPrice));
            itemQuantity.setText(String.valueOf(foundItem.itemQuantity));
            supplierName.setText(foundItem.supplierName);
        } else {
            clearPriceAndQuantityLabels();
        }
    }
    
    private void clearPriceAndQuantityLabels() {
        itemPrice.setText(" ");
        itemQuantity.setText(" ");
        supplierName.setText(" ");
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        NameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NameLabel.setText("Name :");

        PriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PriceLabel.setText("Price (RM) :");

        QuantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        QuantityLabel.setText("Quantity :");

        expiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredDateActionPerformed(evt);
            }
        });

        CategoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CategoryLabel.setText("Category :");

        ExpiredDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ExpiredDateLabel.setText("Expired Date :");

        saveButton.setBackground(new java.awt.Color(255, 153, 153));
        saveButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        SupplierLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        SupplierLabel.setText("Supplier Name :");

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
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(supplierName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(expiredDate)
                    .addComponent(itemCategory, 0, 104, Short.MAX_VALUE)
                    .addComponent(itemQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(60, Short.MAX_VALUE))
            .addComponent(ItemEntryPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityLabel)
                    .addComponent(itemQuantity))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CategoryLabel)
                    .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ExpiredDateLabel)
                    .addComponent(expiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SupplierLabel)
                    .addComponent(supplierName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try {
            // Validate inputs using ItemFunctions
            itemFunction.validateItem(
                (String) itemName.getSelectedItem(),
                itemPrice.getText(),
                itemQuantity.getText(),
                expiredDate.getText(),
                (String) itemCategory.getSelectedItem(),
                supplierName.getText()
            );
            
            // Create item object using ItemFunctions
            String itemID = ItemFileHandler.generateItemID();
            String name = itemName.getSelectedItem().toString();
            double price = Double.parseDouble(itemPrice.getText());
            String category = itemCategory.getSelectedItem().toString();
            LocalDate expiredDateValue = itemFunction.validateExpiredDate(expiredDate.getText());
            String supplier = supplierName.getText();
            int quantity = Integer.parseInt(itemQuantity.getText());
            
            // Check for duplicates using ItemFunctions
            if (itemFunction.isDuplicateItem((String) itemName.getSelectedItem())) {
                // Check if existing item matches all criteria
                Item existingItem = itemFunction.findExistingItem(name, category, supplier, price);
                
                if (existingItem != null) {
                    handleExistingItem(existingItem, quantity, expiredDateValue);
                } else {
                    showError("Item name already exists with different specifications");
                }
                return;
            }
            
            // Create new item using ItemFunctions
            Item newItem = itemFunction.createItem(itemID, name, price, category, 
                                                  expiredDateValue, supplier, quantity);
            
            // Save item using existing handler
            ItemFileHandler.saveItem(newItem);
            
            // Update UI
            entryWindow.loadItemsToTable();
            showSuccess("Item saved successfully!");
            clearFields();
            
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
        }
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
            try {
                // Update stock using ItemFunctions
                itemFunction.updateItemStock(
                    existingItem.getItemID(),
                    existingItem.getTotalStock() + additionalQuantity,
                    newExpiredDate
                );
                
                entryWindow.loadItemsToTable();
                showSuccess("Stock updated successfully!");
                clearFields();
                
            } catch (Exception e) {
                showError("Error updating stock: " + e.getMessage());
            }
        } else {
            try {
                // Create new item with generated ID using ItemFunctions
                String newItemID = ItemFileHandler.generateItemID();
                itemFunction.createNewItem(
                    newItemID,
                    existingItem.getItemName(),
                    existingItem.getPrice(),
                    additionalQuantity,
                    existingItem.getCategory(),
                    newExpiredDate,
                    existingItem.getSupplierID()
                );
                
                entryWindow.loadItemsToTable();
                showSuccess("New item variant created successfully!");
                clearFields();
                
            } catch (Exception e) {
                showError("Error creating new item: " + e.getMessage());
            }
        }
    }
    
    private void clearFields() {
        if (itemName.getItemCount() > 0) {
            itemName.setSelectedIndex(0);
        }
        itemPrice.setText(" ");
        itemQuantity.setText(" ");
        if (itemCategory.getItemCount() > 0) {
            itemCategory.setSelectedIndex(0);
        }
        expiredDate.setText("");
        supplierName.setText(" ");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
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