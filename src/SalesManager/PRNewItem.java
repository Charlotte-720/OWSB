package SalesManager;

import static SalesManager.FileHandler.generateItemID;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.Supplier;
import model.PurchaseRequisition;
import model.PRItem; 

public class PRNewItem extends javax.swing.JFrame {
    private String currentSalesManagerID;
    private List<Supplier> activeSuppliers;
    
    public PRNewItem() {
        initComponents();
        initializeForm();
    }

    public PRNewItem(String salesManagerID) {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.currentSalesManagerID = salesManagerID;
        salesManager.setText(salesManagerID);
        initializeForm();
    }
    
    private void initializeForm() {
        if (currentSalesManagerID != null) {
            salesManager.setText(currentSalesManagerID);
        }
        loadActiveSuppliers();
        clearForm();
    }
    
    private void loadActiveSuppliers() {
        try {
            // Get all suppliers and filter for active ones
            List<Supplier> allSuppliers = FileHandler.loadAllSuppliers();
            activeSuppliers = new ArrayList<>();
            
            for (Supplier supplier : allSuppliers) {
                if (supplier.isActive()) {
                    activeSuppliers.add(supplier);
                }
            }
            
            // Create combo box model with active suppliers
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            
            if (activeSuppliers.isEmpty()) {
                model.addElement("No active suppliers available");
            } else {
                for (Supplier supplier : activeSuppliers) {
                    // Display format: "ID - Name"
                    model.addElement(supplier.getSupplierID() + " - " + supplier.getSupplierName());
                }
            }
            
            supplierID.setModel(model);
            
        } catch (IOException e) {
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
        if (selectedIndex >= 0 && selectedIndex < activeSuppliers.size()) {
            return activeSuppliers.get(selectedIndex).getSupplierID();
        }
        
        return null;
    }
    
    private boolean isDuplicateItem(String itemNameToCheck) {
    try {
        return FileHandler.isItemNameDuplicate(itemNameToCheck);
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, 
            "Error checking for duplicate items: " + e.getMessage(), 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        return false; // Assume no duplicate if there's an error
    }
}
        
    private boolean validateInput() {
        if (itemName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter item name", "Validation Error", JOptionPane.WARNING_MESSAGE);
            itemName.requestFocus();
            return false;
        }
        
        // Check for duplicate item name
        if (isDuplicateItem(itemName.getText().trim())) {
            JOptionPane.showMessageDialog(this, 
                "Item with this name already exists. Please use a different name.", 
                "Duplicate Item", 
                JOptionPane.WARNING_MESSAGE);
            itemName.requestFocus();
            return false;
        }
        
        if (itemQuantity.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Validation Error", JOptionPane.WARNING_MESSAGE);
            itemQuantity.requestFocus();
            return false;
        }
        
        if (itemPrice.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter price", "Validation Error", JOptionPane.WARNING_MESSAGE);
            itemPrice.requestFocus();
            return false;
        }
        
        if (requiredDeliveryDate.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter required delivery date", "Validation Error", JOptionPane.WARNING_MESSAGE);
            requiredDeliveryDate.requestFocus();
            return false;
        }
        
        try {
            int quantity = Integer.parseInt(itemQuantity.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be a positive number", "Validation Error", JOptionPane.WARNING_MESSAGE);
                itemQuantity.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quantity (numbers only)", "Validation Error", JOptionPane.WARNING_MESSAGE);
            itemQuantity.requestFocus();
            return false;
        }
        
        try {
            double price = Double.parseDouble(itemPrice.getText().trim());
            if (price < 0) {
                JOptionPane.showMessageDialog(this, "Price cannot be negative", "Validation Error", JOptionPane.WARNING_MESSAGE);
                itemPrice.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price (numbers only)", "Validation Error", JOptionPane.WARNING_MESSAGE);
            itemPrice.requestFocus();
            return false;
        }
        
        try {
            LocalDate deliveryDate = validateDeliveryDate();
            if (deliveryDate == null) 
                return false;
        } catch (Exception e) {
            showError("Invalid number or date format");
            return false;
        }
        return true;
    }
    
    private LocalDate validateDeliveryDate() {
        try {
            String dateText = requiredDeliveryDate.getText().trim();
            if (dateText.isEmpty()) {
                showError("Please enter delivery date (yyyy-MM-dd)");
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate deliveryDate = LocalDate.parse(dateText, formatter);
            LocalDate today = LocalDate.now();

            if (deliveryDate.isBefore(today)) {
                showError("Delivery date cannot be in the past!");
                return null;
            }
            return deliveryDate;
        } catch (Exception e) {
            showError("Invalid date format. Please use YYYY-MM-DD.");
            return null;
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
        
    private PRItem createPRItemFromInput() throws IOException {
        String newItemID = generateItemID();
        String name = itemName.getText().trim();
        double price = Double.parseDouble(itemPrice.getText().trim());
        int quantity = Integer.parseInt(itemQuantity.getText().trim());
        LocalDate deliveryDate = LocalDate.parse(requiredDeliveryDate.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String selectedSupplierID = getSelectedSupplierID();

        // Create PRItem with the new item details including item name
        PRItem prItem = new PRItem(
            newItemID, 
            quantity, 
            name,               
            selectedSupplierID, 
            price, 
            deliveryDate
        );
        return prItem;
    }
    
    private PurchaseRequisition createPurchaseRequisition(PRItem item) throws IOException {
        String prID = FileHandler.generatePRID();
        LocalDate requestDate = LocalDate.now();
        LocalDate requiredDate = item.getRequiredDeliveryDate();
        String status = "Pending";
        
        List<PRItem> items = new ArrayList<>();
        items.add(item);
        
        return new PurchaseRequisition(prID, currentSalesManagerID, requestDate, requiredDate, status, items);
    }
        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        PRTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        itemLabel = new javax.swing.JLabel();
        itemName = new javax.swing.JTextField();
        quantityLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        supplierIDLabel = new javax.swing.JLabel();
        salesManagerLabel = new javax.swing.JLabel();
        salesManager = new javax.swing.JLabel();
        itemQuantity = new javax.swing.JTextField();
        itemPrice = new javax.swing.JTextField();
        requiredDeliveryDate = new javax.swing.JTextField();
        submitButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        RequiredDeliveryDateLabel = new javax.swing.JLabel();
        supplierID = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(239, 252, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        PRTitle.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        PRTitle.setText("Purchase New Item");

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
                .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

        itemLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemLabel.setText("Item :");

        itemName.setText(" ");

        quantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        quantityLabel.setText("Quantity :");

        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        priceLabel.setText("Price :");

        supplierIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierIDLabel.setText("Supplier ID :");

        salesManagerLabel.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        salesManagerLabel.setForeground(new java.awt.Color(0, 0, 153));
        salesManagerLabel.setText("Sales Manager :");

        salesManager.setForeground(new java.awt.Color(0, 0, 153));
        salesManager.setText(" ");

        itemQuantity.setText(" ");

        itemPrice.setText(" ");

        requiredDeliveryDate.setText(" ");

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

        RequiredDeliveryDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        RequiredDeliveryDateLabel.setText("Required Delivery Date :");

        supplierID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        supplierID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(supplierIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(85, 85, 85))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(salesManagerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(itemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(54, 54, 54)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(43, 43, 43)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(salesManager, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(RequiredDeliveryDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(33, 41, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearButton)
                        .addGap(58, 58, 58))))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salesManagerLabel)
                    .addComponent(salesManager))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemLabel)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quantityLabel)
                    .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(priceLabel)
                    .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RequiredDeliveryDateLabel)
                    .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierIDLabel)
                    .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        try {
            // Validate and collect input data
            if (!validateInput()) {
                return;
            }
            
            PRItem newItem = createPRItemFromInput();
            PurchaseRequisition pr = createPurchaseRequisition(newItem);
            
            FileHandler.savePurchaseRequisition(pr);
            
            JOptionPane.showMessageDialog(this, 
                "Purchase Requisition created successfully!\nPR ID: " + pr.getPrID() + 
                "\nItem: " + itemName.getText().trim() +
                "\nQuantity: " + itemQuantity.getText().trim() +
                "\nStatus: Pending", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Clear form after successful submission
            clearForm();
            
        }catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saving Purchase Requisition: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_submitButtonActionPerformed
    private void clearForm() {
        itemName.setText("");
        itemQuantity.setText("");
        itemPrice.setText("");
        requiredDeliveryDate.setText("");
        if (supplierID.getItemCount() > 0) {
            supplierID.setSelectedIndex(0);
        }
        itemName.requestFocus(); // Focus on first field
    }
    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearForm();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void supplierIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierIDActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

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
//            java.util.logging.Logger.getLogger(PRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PRNewItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PRNewItem().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel PRTitle;
    private javax.swing.JLabel RequiredDeliveryDateLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JTextField itemName;
    private javax.swing.JTextField itemPrice;
    private javax.swing.JTextField itemQuantity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField requiredDeliveryDate;
    private javax.swing.JLabel salesManager;
    private javax.swing.JLabel salesManagerLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JComboBox<String> supplierID;
    private javax.swing.JLabel supplierIDLabel;
    // End of variables declaration//GEN-END:variables
}
