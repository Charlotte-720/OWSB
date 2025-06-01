package SalesManager;

import SalesManager.DataHandlers.ItemFileHandler;
import SalesManager.DataHandlers.ItemFileHandler.POItem;
import SalesManager.Functions.itemFunction;
import java.awt.Component;
import model.Item;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;


public class EditItem extends javax.swing.JFrame {
    private String currentItemID;
    private ItemEntry entryWindow;
    private Item originalItem;
    private List<POItem> paidPOItems;
    private boolean isInitializing = false;
    private Component previousComponent;
    
    public EditItem() {
        initComponents();
    }
    
    public EditItem(String itemID) {
        initComponents();
        this.currentItemID = itemID;
        initializeData();
    }
    
    public EditItem(String itemID, ItemEntry entryWindow) {
        initComponents();
        this.currentItemID = itemID;
        this.entryWindow = entryWindow;
        this.previousComponent = entryWindow;
        initializeData();
    }
    
    private void initializeData() {
        try {
            paidPOItems = ItemFileHandler.loadPaidPOItems();
            populateItemNames();
            populateItemCategories();
            loadItemForEditing();
        } catch (IOException e) {
            showError("Error loading data: " + e.getMessage());
            paidPOItems = new ArrayList<>();
        }
    }
    
    private void populateItemCategories() {
        String[] categories = itemFunction.getItemCategories();
        itemCategory.setModel(new DefaultComboBoxModel<>(categories));
    }
    
    private void populateItemNames() {
        List<String> itemNames = itemFunction.extractUniqueItemNames(paidPOItems);
        
        // Create combo box model
        DefaultComboBoxModel<String> itemNameModel = new DefaultComboBoxModel<>(
            itemNames.toArray(new String[0])
        );
        itemName.setModel(itemNameModel);
    
    for (var listener : itemName.getActionListeners()) {
            itemName.removeActionListener(listener);
        }
        itemName.addActionListener(e -> {
            if (!isInitializing) {
                onItemNameChanged();
            }
        });
    }
    
    private void onItemNameChanged() {
        String selectedItemName = (String) itemName.getSelectedItem();
        if (selectedItemName != null) {
            updateFieldsFromPOItem(selectedItemName);
        }
    }
    
    private void updateFieldsFromPOItem(String selectedItemName) {
        POItem poItem = itemFunction.findPOItemByName(paidPOItems, selectedItemName);
        if (poItem != null) {
            itemPrice.setText(String.format("%.2f", poItem.itemPrice));
            itemQuantity.setText(String.valueOf(poItem.itemQuantity));
            supplierID.setText(poItem.supplierID);
        }
    }
    
    private void loadItemForEditing() {
        if (currentItemID == null) {
            return;
        }

        try {
            Item item = itemFunction.getItemById(currentItemID);
            if (item != null) {
                this.originalItem = item;
                populateItemFields(item);
                saveButton.setText("Update");
            } else {
                showError("Item not found!");
                this.dispose();
            }
        } catch (IOException e) {
            showError("Error loading item data: " + e.getMessage());
            this.dispose();
        }
    }
    
    private void populateItemFields(Item item) {
        isInitializing = true;
        itemName.setSelectedItem(item.getItemName());
        itemCategory.setSelectedItem(item.getCategory());
        expiredDate.setText(item.getExpiredDate().toString());
        updateFieldsFromPOItem(item.getItemName());
        isInitializing = false;
    }
    
    private void handleItemUpdate() {
        try {
            // Get values from UI components
            String selectedItemName = (String) itemName.getSelectedItem();
            String priceText = itemPrice.getText();
            String quantityText = itemQuantity.getText();
            String selectedCategory = (String) itemCategory.getSelectedItem();
            String expiredDateText = expiredDate.getText();
            String supplierText = supplierID.getText();
            
            // Validate the input using ItemFileHandler's validation method
            itemFunction.validateItem(selectedItemName, priceText, quantityText, 
                                       expiredDateText, selectedCategory, supplierText);

            // Parse the values
            double price = Double.parseDouble(priceText.trim());
            int quantity = Integer.parseInt(quantityText.trim());
            LocalDate expiredDateParsed = itemFunction.validateExpiredDate(expiredDateText);

            // Check for duplicate item name (excluding current item)
            if (itemFunction.isDuplicateItemExcludingCurrent(selectedItemName, currentItemID)) {
                showError("Item name already exists");
                return;
            }

            // Create updated item
            Item updatedItem = new Item(
                currentItemID,
                selectedItemName,
                price,
                selectedCategory,
                expiredDateParsed,
                supplierText,
                quantity,
                LocalDate.now()
            );

            // Update the item using ItemFileHandler
            ItemFileHandler.updateItem(updatedItem);

            // Refresh the parent window if it exists
            if (entryWindow != null) {
                entryWindow.loadItemsToTable();
            }

            showSuccess("Item updated successfully!");
            this.dispose();

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Error updating item: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void clearFields() {
        if (itemName.getItemCount() > 0) {
            itemName.setSelectedIndex(0);
        }
        itemPrice.setText("");
        itemQuantity.setText("");
        if (itemCategory.getItemCount() > 0) {
            itemCategory.setSelectedIndex(0);
        }
        expiredDate.setText("");
        supplierID.setText("");
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        QuantityLabel = new javax.swing.JLabel();
        CategoryLabel = new javax.swing.JLabel();
        ExpiredDateLabel = new javax.swing.JLabel();
        SupplierLabel = new javax.swing.JLabel();
        PriceLabel = new javax.swing.JLabel();
        ClearButton = new javax.swing.JButton();
        ItemEntryPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        NameLabel = new javax.swing.JLabel();
        expiredDate = new javax.swing.JTextField();
        itemName = new javax.swing.JComboBox<>();
        itemCategory = new javax.swing.JComboBox<>();
        supplierID = new javax.swing.JLabel();
        itemPrice = new javax.swing.JLabel();
        itemQuantity = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        QuantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        QuantityLabel.setText("Quantity :");

        CategoryLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CategoryLabel.setText("Category :");

        ExpiredDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        ExpiredDateLabel.setText("Expired Date :");

        SupplierLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        SupplierLabel.setText("Supplier ID :");

        PriceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PriceLabel.setText("Price (RM) :");

        ClearButton.setBackground(new java.awt.Color(153, 204, 255));
        ClearButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

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
                .addContainerGap(83, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        ItemEntryPanelLayout.setVerticalGroup(
            ItemEntryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemEntryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemEntryPanelLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        saveButton.setBackground(new java.awt.Color(255, 255, 102));
        saveButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        saveButton.setText("Update");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        NameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        NameLabel.setText("Name :");

        expiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredDateActionPerformed(evt);
            }
        });

        itemName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        itemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCategoryActionPerformed(evt);
            }
        });

        supplierID.setText(" ");

        itemPrice.setText(" ");

        itemQuantity.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(CategoryLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(QuantityLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(NameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PriceLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(52, 52, 52)
                                        .addComponent(itemName, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(itemPrice, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(itemQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(SupplierLabel)
                                    .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(supplierID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(expiredDate, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(65, 65, 65)
                        .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameLabel)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PriceLabel)
                    .addComponent(itemPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityLabel)
                    .addComponent(itemQuantity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CategoryLabel)
                    .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ExpiredDateLabel)
                    .addComponent(expiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierID)
                    .addComponent(SupplierLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void expiredDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiredDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expiredDateActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
         if (currentItemID == null) {
            showError("No item selected for editing");
            return;
    } else {
        handleItemUpdate();
    }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        clearFields();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
        if (previousComponent != null) {
            previousComponent.setVisible(true);
        }
    }//GEN-LAST:event_jLabel2MouseClicked

    private void itemCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCategoryActionPerformed
    
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
    private javax.swing.JLabel supplierID;
    // End of variables declaration//GEN-END:variables
}
