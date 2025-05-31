package SalesManager;

import SalesManager.Functions.prFunction;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.Supplier;

public class EditPRNewItem extends javax.swing.JFrame {
    private String currentPRID;
    private String currentSalesManagerID;
    private List<Supplier> activeSuppliers;
    private prFunction prFunc;
    
    public EditPRNewItem() {
        initComponents();
        this.prFunc = new prFunction();
    }
    
    public EditPRNewItem(String prID, String salesManagerID) {
        initComponents();
        this.prFunc = new prFunction();
        this.currentPRID = prID;
        this.currentSalesManagerID = salesManagerID;
        salesManager.setText(salesManagerID);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        loadActiveSuppliers();
        loadCurrentPRData();
    }
    
    private void loadCurrentPRData() {
        try {
            String[] prData = prFunc.loadPRData(currentPRID);
            
            System.out.println("PR Data found with " + prData.length + " elements");
            for (int i = 0; i < prData.length; i++) {
                System.out.println("Index " + i + ": " + prData[i]);
            }
            
            itemName.setText(prData[3].trim()); // Item Name
            itemQuantity.setText(prData[4].trim()); // Quantity
            itemPrice.setText(prData[5].trim()); // Unit Price
            requiredDeliveryDate.setText(prData[9].trim()); // Required Delivery Date
            
            // Set the supplier in combo box
            String currentSupplierID = prData[7].trim(); // Supplier ID
            setSupplierInComboBox(currentSupplierID);
            
            System.out.println("Successfully loaded PR data");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading PR data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void loadActiveSuppliers() {
        try {
            activeSuppliers = prFunc.loadAndFormatActiveSuppliers();
            
            // Create combo box model with active suppliers
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            
            if (activeSuppliers.isEmpty()) {
                model.addElement("No active suppliers available");
            } else {
                String[] supplierDisplayNames = prFunc.formatSuppliersForDisplay(activeSuppliers);
                for (String displayName : supplierDisplayNames) {
                    model.addElement(displayName);
                }
            }
            
            supplierID.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading suppliers: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            
            // Set default model if error occurs
            DefaultComboBoxModel<String> errorModel = new DefaultComboBoxModel<>();
            errorModel.addElement("Error loading suppliers");
            supplierID.setModel(errorModel);
        }
    }
    
    private String getSelectedSupplierID() {
        if (activeSuppliers.isEmpty()) {
            return null;
        }
        
        int selectedIndex = supplierID.getSelectedIndex();
        return prFunc.getSupplierIDFromSelection(activeSuppliers, selectedIndex);
    }
    
    private void setSupplierInComboBox(String currentSupplierID) {
        // Clear selection first
        supplierID.setSelectedIndex(-1);
        
        // Try to find in current combo box items
        for (int i = 0; i < supplierID.getItemCount(); i++) {
            String comboItem = supplierID.getItemAt(i);
            
            // Check if the combo item starts with the supplier ID
            if (comboItem.startsWith(currentSupplierID + " - ")) {
                supplierID.setSelectedIndex(i);
                System.out.println("Found and set supplier to: " + comboItem);
                return;
            }
        }
        
        // If not found in combo box, try to load supplier and add it
        if (!currentSupplierID.isEmpty()) {
            try {
                Supplier supplier = prFunc.getSupplierById(currentSupplierID);
                if (supplier != null && supplier.isActive()) {
                    String displayText = supplier.getSupplierID() + " - " + supplier.getSupplierName();
                    supplierID.addItem(displayText);
                    supplierID.setSelectedItem(displayText);
                    
                    // Also add to activeSuppliers list
                    if (activeSuppliers != null) {
                        activeSuppliers.add(supplier);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error loading supplier details: " + e.getMessage());
            }
        }
    }
    
    private void saveUpdatedPRData() {
        try {
            // Get input values
            String newItemName = itemName.getText().trim();
            String quantityText = itemQuantity.getText().trim();
            String priceText = itemPrice.getText().trim();
            String newSupplierID = getSelectedSupplierID();
            String deliveryDate = requiredDeliveryDate.getText().trim();
            
            // Validate supplier selection
            if (newSupplierID == null) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a valid supplier!", 
                    "Validation Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Use prFunction to edit PR item
            boolean success = prFunc.editPRItem(currentPRID, newItemName, quantityText, 
                                               priceText, newSupplierID, deliveryDate);
            
            if (success) {
                // Create and show success message
                String successMessage = prFunc.createUpdateSuccessMessage(currentPRID, newItemName, 
                                                                         quantityText, priceText, newSupplierID);
                JOptionPane.showMessageDialog(this, successMessage, 
                    "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Close the form after successful save
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to update PR item!", 
                    "Update Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (IllegalArgumentException e) {
            // Handle validation errors
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving PR data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearAllFields() {
        itemName.setText("");
        itemQuantity.setText("");
        itemPrice.setText("");
        requiredDeliveryDate.setText("");
        if (supplierID.getItemCount() > 0) {
            supplierID.setSelectedIndex(0);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        PRTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        supplierID = new javax.swing.JComboBox<>();
        quantityLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        requiredDateLabel = new javax.swing.JLabel();
        supplierLabel = new javax.swing.JLabel();
        requiredDeliveryDate = new javax.swing.JTextField();
        itemName = new javax.swing.JTextField();
        itemQuantity = new javax.swing.JTextField();
        itemPrice = new javax.swing.JTextField();
        salesLabel = new javax.swing.JLabel();
        salesManager = new javax.swing.JLabel();
        itemLabel = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        PRTitle.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        PRTitle.setText("Edit New Item");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(236, 255, 255));

        supplierID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        quantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        quantityLabel.setText("Quantity :");

        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        priceLabel.setText("Price :");

        requiredDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        requiredDateLabel.setText("Required Delivery Date :");

        supplierLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierLabel.setText("Supplier ID :");

        requiredDeliveryDate.setText(" ");
        requiredDeliveryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requiredDeliveryDateActionPerformed(evt);
            }
        });

        itemName.setText(" ");

        itemQuantity.setText(" ");

        salesLabel.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        salesLabel.setForeground(new java.awt.Color(0, 0, 204));
        salesLabel.setText("Sales Manager :");

        salesManager.setText(" ");

        itemLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemLabel.setText("Item :");

        submitButton.setBackground(new java.awt.Color(204, 255, 204));
        submitButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        clearButton.setBackground(new java.awt.Color(153, 204, 255));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(47, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(salesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salesManager, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(quantityLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(itemLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(submitButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearButton))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(supplierLabel)
                            .addGap(40, 40, 40)
                            .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(requiredDateLabel)
                        .addGap(18, 18, 18)
                        .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salesLabel)
                    .addComponent(salesManager))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(priceLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(requiredDateLabel)
                    .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierLabel)
                    .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        saveUpdatedPRData();
    }//GEN-LAST:event_submitButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearAllFields();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void requiredDeliveryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDeliveryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDeliveryDateActionPerformed

    /**
     * @param args the command line arguments
     */
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
//            java.util.logging.Logger.getLogger(EditPRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(EditPRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(EditPRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(EditPRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new EditPRNewItem().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel PRTitle;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JTextField itemName;
    private javax.swing.JTextField itemPrice;
    private javax.swing.JTextField itemQuantity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JLabel requiredDateLabel;
    private javax.swing.JTextField requiredDeliveryDate;
    private javax.swing.JLabel salesLabel;
    private javax.swing.JLabel salesManager;
    private javax.swing.JButton submitButton;
    private javax.swing.JComboBox<String> supplierID;
    private javax.swing.JLabel supplierLabel;
    // End of variables declaration//GEN-END:variables
}
