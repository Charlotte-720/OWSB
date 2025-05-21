package SalesManager;

import model.Item;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;


public class EditItem extends javax.swing.JFrame {
    private String currentItemID;
    /**
     * Creates new form EditItem
     */
    public EditItem() {
        initComponents();
    }
    
    public EditItem(String itemID) {
        initComponents();
        this.currentItemID = itemID;
        loadItemForEditing();
        populateSupplierID();
    }
    
    private String generateItemID() { 
    try {
            return FileHandler.generateItemID();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error generating ID", "Error", JOptionPane.ERROR_MESSAGE);
            return "ITM" + System.currentTimeMillis(); // Fallback
        }
    }
    
    private void loadItemForEditing() {
        if (currentItemID == null) {
            return;
        }

        try {
            Item item = FileHandler.getItemById(currentItemID);
            if (item != null) {
                populateItemFields(item);
                saveButton.setText("Update");
            } else {
                JOptionPane.showMessageDialog(this, "Item not found!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading item data: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }

    private void populateItemFields(Item item) {
        itemName.setText(item.getItemName());
        itemPrice.setText(String.format("%.2f", item.getPrice()));
        itemQuantity.setText(String.valueOf(item.getTotalStock()));
        itemCategory.setText(item.getCategory());
        expiredDate.setText(item.getExpiredDate().toString());
        supplierID.setSelectedItem(item.getSupplierID());
    }
    
    private Item loadItemData(String itemID) {
        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 8 && parts[0].trim().equals(itemID)) {
                    return new Item(
                        parts[0].trim(),        // itemID
                        parts[1].trim(),        // name
                        Double.parseDouble(parts[2].trim()), // price
                        parts[3].trim(),        // category
                        LocalDate.parse(parts[4].trim()), // expiredDate
                        parts[5].trim(),        // supplierID
                        Integer.parseInt(parts[6].trim()), // totalStock
                        LocalDate.parse(parts[7].trim())  // updatedDate
                    );
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void populateSupplierID() {
        try {
        List<String> supplierIDs = FileHandler.loadSupplierIDs();
        
        DefaultComboBoxModel<String> supplierModel = new DefaultComboBoxModel<>(
            supplierIDs.toArray(new String[0])
        );
        supplierID.setModel(supplierModel);
        
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error loading suppliers: " + e.getMessage(),
            "File Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    private boolean isDuplicateItem(String itemNameToCheck) {
    try {
        return FileHandler.isItemNameDuplicate(itemNameToCheck);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this,
            "Error checking for duplicate items: " + e.getMessage(),
            "File Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return true;    
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        itemQuantity = new javax.swing.JTextField();
        itemPrice = new javax.swing.JTextField();
        itemCategory = new javax.swing.JTextField();
        QuantityLabel = new javax.swing.JLabel();
        supplierID = new javax.swing.JComboBox<>();
        CategoryLabel = new javax.swing.JLabel();
        ExpiredDateLabel = new javax.swing.JLabel();
        SupplierLabel = new javax.swing.JLabel();
        PriceLabel = new javax.swing.JLabel();
        ClearButton = new javax.swing.JButton();
        ItemEntryPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        itemName = new javax.swing.JTextField();
        saveButton = new javax.swing.JButton();
        NameLabel = new javax.swing.JLabel();
        expiredDate = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        QuantityLabel.setText("Quantity");

        supplierID.setEditable(true);
        supplierID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        supplierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIDActionPerformed(evt);
            }
        });

        CategoryLabel.setText("Category");

        ExpiredDateLabel.setText("Expired Date");

        SupplierLabel.setText("Supplier ID");

        PriceLabel.setText("Price (RM)");

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

        itemName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNameActionPerformed(evt);
            }
        });

        saveButton.setBackground(new java.awt.Color(255, 255, 102));
        saveButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        saveButton.setText("Update");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        NameLabel.setText("Name");

        expiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredDateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(SupplierLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(expiredDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(supplierID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(CategoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(QuantityLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(NameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PriceLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(52, 52, 52)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(itemPrice, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(itemName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(53, 53, 53)
                                            .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(54, 54, 54)
                                        .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameLabel)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PriceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityLabel)
                    .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 17, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CategoryLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(SupplierLabel)
                    .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
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

    private void supplierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIDActionPerformed

    }//GEN-LAST:event_supplierIDActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
         if (currentItemID == null) {
    } else {
        handleItemUpdate();
    }
}

    private void handleItemUpdate() {
        try {
            String name = itemName.getText().trim();
            if (name.isEmpty()) {
                showError("Please enter item name");
                return;
            }

            double price = validatePrice();
            if (price <= 0) return;

            int quantity = validateQuantity();
            if (quantity <= 0) return;

            String category = itemCategory.getText().trim();
            if (category.isEmpty()) {
                showError("Please enter category");
                return;
            }

            LocalDate newExpiredDate = validateExpiredDate();
            if (newExpiredDate == null) return;

            String supplierIDStr = (String) supplierID.getSelectedItem();
            if (supplierIDStr == null || supplierIDStr.isEmpty()) {
                showError("Please select a supplier");
                return;
            }

            updateItemInFile(currentItemID, name, price, quantity, category, 
                    newExpiredDate, supplierIDStr);
            showSuccess("Item updated successfully!");
            this.dispose();
        } catch (Exception e) {
            showError("Error updating item: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double validatePrice() {
        try {
            String priceText = itemPrice.getText().trim();
            if (priceText.isEmpty()) {
                showError("Please enter price");
                return -1;
            }
            double price = Double.parseDouble(priceText);
            if (price <= 0) {
                showError("Price must be positive");
                return -1;
            }
            return price;
        } catch (NumberFormatException e) {
            showError("Invalid price format");
            return -1;
        }
    }

    private int validateQuantity() {
        try {
            String quantityText = itemQuantity.getText().trim();
            if (quantityText.isEmpty()) {
                showError("Please enter quantity");
                return -1;
            }
            int quantity = Integer.parseInt(quantityText);
            if (quantity <= 0) {
                showError("Quantity must be positive");
                return -1;
            }
            return quantity;
        } catch (NumberFormatException e) {
            showError("Invalid quantity format");
            return -1;
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

    private void updateItemInFile(String itemID, String name, double price, int quantity, 
                                String category, LocalDate expiredDate, String supplierID) {
        List<String> lines = new ArrayList<>();
        LocalDate updatedDate = LocalDate.now();

        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 8 && parts[0].trim().equals(itemID)) {
                    line = String.format("%s,%s,%.2f,%s,%s,%s,%d,%s",
                        itemID, name, price, category, expiredDate, supplierID, quantity, updatedDate);
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
   
        
           
    }//GEN-LAST:event_saveButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        clearFields();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void itemNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemNameActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked
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
    private void clearFields() {
        itemName.setText("");
        itemPrice.setText("");
        itemQuantity.setText("");                                             
        itemCategory.setText("");
        expiredDate.setText("");
        supplierID.setSelectedIndex(0);
    }
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(EditItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new EditItem().setVisible(true);
//            }
//        });
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
