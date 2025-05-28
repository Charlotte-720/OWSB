package SalesManager;

import model.Item;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class EditPRRestock extends javax.swing.JFrame {
    private String currentPRID;
    private String currentSalesManagerID;
    private String currentPRType;

    public EditPRRestock() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        loadItemIDs();
    }

    public EditPRRestock(String prID, String salesManagerID) {
        initComponents();
        this.currentPRID = prID;
        this.currentSalesManagerID = salesManagerID;
        salesManager.setText(salesManagerID);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        loadItemIDs();
        loadPRData();
    }
    
    private String getCurrentSalesManagerID() {
        return this.currentSalesManagerID;
    }
    
    private void loadPRData() {
        try {
            String[] prData = FileHandler.getPurchaseRequisitionById(currentPRID);
            if (prData != null && prData.length >= 12) {
                populateFields(prData);
                updateButton.setText("Update");
            } else {
                showError("Purchase Requisition not found!");
                this.dispose();
            }
        } catch (IOException e) {
            showError("Error loading PR data: " + e.getMessage());
            this.dispose();
        }
    }
    
    private void populateFields(String[] prData) {
    try {
        currentPRType = prData[1].trim();
        String prItemID = prData[2];
        
        // Check if the item ID exists in the combo box
        boolean itemFound = false;
        for (int i = 0; i < itemID.getItemCount(); i++) {
            if (itemID.getItemAt(i).equals(prItemID)) {
                itemID.setSelectedIndex(i);
                itemFound = true;
                break;
            }
        }
        
        if (!itemFound) {
            showError("Item ID " + prItemID + " not found in system!");
            return;
        }
        
        // Set other fields with PR data
        quantity.setText(prData[4]); 
        unitPrice.setText(prData[5]); 
        supplierID.setText(prData[7]); 
        requiredDeliveryDate.setText(prData[9]); 
        creationDate.setText(prData[10]); 
        
        // Update item name display - this will use the item from combo box selection
        updateItemDisplayForEdit();
        
        // Add to table
        updateTable();
        
    } catch (Exception e) {
        showError("Error loading PR data: " + e.getMessage());
    }
}
    
    private void updateItemDisplayForEdit() {
        String selectedItemID = (String) itemID.getSelectedItem();
        if (selectedItemID != null && !selectedItemID.trim().isEmpty()) {
            try {
                Item item = FileHandler.getItemById(selectedItemID);
                if (item != null) {
                    itemName.setText(item.getItemName());
                    // DON'T overwrite unitPrice here when editing - keep the PR's price
                    System.out.println("Item found: " + item.getItemName());
                } else {
                    System.out.println("Item not found for ID: " + selectedItemID);
                    itemName.setText("Item not found");
                }
            } catch (IOException e) {
                System.out.println("Error loading item: " + e.getMessage());
                itemName.setText("Error loading item");
            }
        } else {
            itemName.setText("");
            System.out.println("No item selected");
        }
    }
    
    private void updateItemDisplay() {
        String selectedItemID = (String) itemID.getSelectedItem();
        if (selectedItemID != null && !selectedItemID.trim().isEmpty()) {
            try {
                Item item = FileHandler.getItemById(selectedItemID);
                if (item != null) {
                    itemName.setText(item.getItemName());
                    // Only set unit price if we're NOT editing (currentPRID is null)
                    if (currentPRID == null) {
                        unitPrice.setText(String.valueOf(item.getPrice()));
                    }
                    System.out.println("Item found: " + item.getItemName());
                } else {
                    System.out.println("Item not found for ID: " + selectedItemID);
                    itemName.setText("Item not found");
                    if (currentPRID == null) {
                        unitPrice.setText("0.0");
                    }
                }
            } catch (IOException e) {
                System.out.println("Error loading item: " + e.getMessage());
                itemName.setText("Error loading item");
                if (currentPRID == null) {
                    unitPrice.setText("0.0");
                }
            }
        } else {
            itemName.setText("");
            if (currentPRID == null) {
                unitPrice.setText("");
            }
            System.out.println("No item selected");
        }
    }
    
    private void loadItemIDs() {
        try {
            List<Item> items = FileHandler.loadAllItems();
            if (items.isEmpty()) {
                showError("No items found in the system!");
                return;
            }
            
            String[] itemIDs = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                itemIDs[i] = items.get(i).getItemID();
            }
            
            itemID.setModel(new DefaultComboBoxModel<>(itemIDs));
            itemID.addActionListener(e -> updateItemDisplay());
            
            // Update display for the first item
            if (itemIDs.length > 0) {
                updateItemDisplay();
            }
            
        } catch (IOException e) {
            showError("Error loading items: " + e.getMessage());
        }
    }
    
    // Helper method to update table
    private void updateTable() {
        try {
            String selectedItemID = (String) itemID.getSelectedItem();
            if (selectedItemID == null || quantity.getText().trim().isEmpty()) {
                return; 
            }
            
            int qty = Integer.parseInt(quantity.getText());
            double price = Double.parseDouble(unitPrice.getText());
            double total = qty * price;
            
            // Get item name
            String itemNameText = itemName.getText();
            
            // Update table
            DefaultTableModel model = (DefaultTableModel) prTable.getModel();
            model.setRowCount(0);
            model.addRow(new Object[]{selectedItemID, itemNameText, qty, price, total});
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format in updateTable: " + e.getMessage());
        }
    }
    
    private boolean validateInputs() {
        if (itemID.getSelectedItem() == null) {
            showError("Please select an item");
            return false;
        }
        
        if (quantity.getText().trim().isEmpty()) {
            showError("Please enter quantity");
            return false;
        }
        
        try {
            int qty = Integer.parseInt(quantity.getText());
            if (qty <= 0) {
                showError("Quantity must be positive");
                return false;
            }
        } catch (NumberFormatException e) {
            showError("Invalid quantity format");
            return false;
        }
        
        if (requiredDeliveryDate.getText().trim().isEmpty()) {
            showError("Please enter delivery date");
            return false;
        }
        
        try {
            LocalDate deliveryDate = LocalDate.parse(requiredDeliveryDate.getText());
            if (deliveryDate.isBefore(LocalDate.now())) {
                showError("Delivery date cannot be in the past!");
                return false;
            }
        } catch (DateTimeParseException e) {
            showError("Invalid date format. Please use YYYY-MM-DD");
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearFields() {
        if (itemID.getItemCount() > 0) {
            itemID.setSelectedIndex(0);
        }
        itemName.setText("");
        quantity.setText("");
        unitPrice.setText("");
        requiredDeliveryDate.setText("");
        
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        model.setRowCount(0);
    }
            
            
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PRPanel = new javax.swing.JPanel();
        PRTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        salesManagerLabel = new javax.swing.JLabel();
        itemLabel = new javax.swing.JLabel();
        itemNameLabel = new javax.swing.JLabel();
        quantityLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        supplierIDLabel = new javax.swing.JLabel();
        requiredDeliveryDateLabel = new javax.swing.JLabel();
        itemID = new javax.swing.JComboBox<>();
        itemName = new javax.swing.JLabel();
        quantity = new javax.swing.JTextField();
        unitPrice = new javax.swing.JLabel();
        supplierID = new javax.swing.JLabel();
        requiredDeliveryDate = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        creationDateLabel = new javax.swing.JLabel();
        creationDate = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        salesManager = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(239, 252, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        PRPanel.setBackground(new java.awt.Color(204, 255, 204));

        PRTitle.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        PRTitle.setText("Edit Purchase Requisition");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PRPanelLayout = new javax.swing.GroupLayout(PRPanel);
        PRPanel.setLayout(PRPanelLayout);
        PRPanelLayout.setHorizontalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 446, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(75, 75, 75)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        PRPanelLayout.setVerticalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addGroup(PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PRPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(PRPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(PRTitle)))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        salesManagerLabel.setBackground(new java.awt.Color(0, 0, 204));
        salesManagerLabel.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        salesManagerLabel.setForeground(new java.awt.Color(0, 0, 204));
        salesManagerLabel.setText("Sales Manager :");

        itemLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemLabel.setText("Item :");

        itemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemNameLabel.setText("Item Name :");

        quantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        quantityLabel.setText("Quantity :");

        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        priceLabel.setText("Price :");

        supplierIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierIDLabel.setText("Supplier ID :");

        requiredDeliveryDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        requiredDeliveryDateLabel.setText("Required Delivery Date :");

        itemID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        quantity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantityActionPerformed(evt);
            }
        });

        requiredDeliveryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requiredDeliveryDateActionPerformed(evt);
            }
        });

        prTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "Quantity", "Price", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        prTable.setRowHeight(30);
        jScrollPane1.setViewportView(prTable);

        creationDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        creationDateLabel.setText("Creation Date:");

        updateButton.setBackground(new java.awt.Color(255, 255, 204));
        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

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

        salesManager.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(requiredDeliveryDateLabel)
                                .addGap(18, 18, 18)
                                .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clearButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(supplierIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(salesManager, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(unitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(itemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(salesManagerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(creationDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(creationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(PRPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(creationDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(creationDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateButton, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salesManagerLabel)
                            .addComponent(salesManager))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(itemLabel)
                            .addComponent(itemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemNameLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(quantityLabel)
                            .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(unitPrice)
                            .addComponent(priceLabel))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(supplierIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supplierID))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(requiredDeliveryDateLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                            .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
         if (!validateInputs()) return;
        
        try {
            String selectedItemID = (String) itemID.getSelectedItem();
            String itemNameText = itemName.getText();
            int quantityValue = Integer.parseInt(quantity.getText());
            double priceValue = Double.parseDouble(unitPrice.getText());
            double totalPrice = priceValue * quantityValue;
            LocalDate deliveryDate = LocalDate.parse(requiredDeliveryDate.getText());
            LocalDate creationDateValue = LocalDate.parse(creationDate.getText());
            String supplierIDValue = supplierID.getText();
            String raisedBy = getCurrentSalesManagerID();
            
            // Update table
            updateTable();
            
            // Update PR using existing FileHandler method
            boolean updated = FileHandler.updatePurchaseRequisition(
            currentPRID, 
            currentPRType,       
            selectedItemID, 
            itemNameText,        
            quantityValue, 
            priceValue, 
            totalPrice, 
            supplierIDValue,
            raisedBy,           
            deliveryDate,
            creationDateValue, 
            "PENDING"
        );
            
            if (updated) {
                showSuccess("Purchase Requisition updated successfully!");
            } else {
                showError("Purchase Requisition not found in file");
                }
            } catch (Exception e) {
                showError("Error updating Purchase Requisition: " + e.getMessage());
            }
    }//GEN-LAST:event_updateButtonActionPerformed

    private void quantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityActionPerformed
        updateTable();

    }//GEN-LAST:event_quantityActionPerformed

    private void requiredDeliveryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDeliveryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDeliveryDateActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearFields();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
         if (!validateInputs()) return;
        
        try {
            String selectedItemID = (String) itemID.getSelectedItem();
            String itemNameText = itemName.getText();
            int quantityValue = Integer.parseInt(quantity.getText());
            double priceValue = Double.parseDouble(unitPrice.getText());
            double totalPrice = priceValue * quantityValue;
            
            // Update table
            updateTable();
            
            showSuccess("Data prepared successfully! Please review the table and click 'Update' to save changes.");
        
        // Enable the update button to make the flow clear
        updateButton.setEnabled(true);
        
    } catch (NumberFormatException e) {
        showError("Error calculating values: " + e.getMessage());
    } catch (Exception e) {
        showError("Error preparing data: " + e.getMessage());
    }

    }//GEN-LAST:event_submitButtonActionPerformed

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
//            java.util.logging.Logger.getLogger(EditPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(EditPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(EditPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(EditPR.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new EditPR().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PRPanel;
    private javax.swing.JLabel PRTitle;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel creationDate;
    private javax.swing.JLabel creationDateLabel;
    private javax.swing.JComboBox<String> itemID;
    private javax.swing.JLabel itemLabel;
    private javax.swing.JLabel itemName;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable prTable;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField quantity;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField requiredDeliveryDate;
    private javax.swing.JLabel requiredDeliveryDateLabel;
    private javax.swing.JLabel salesManager;
    private javax.swing.JLabel salesManagerLabel;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel supplierID;
    private javax.swing.JLabel supplierIDLabel;
    private javax.swing.JLabel unitPrice;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
