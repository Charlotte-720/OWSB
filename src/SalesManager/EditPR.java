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


public class EditPR extends javax.swing.JFrame {
     private String currentPRID;

    public EditPR() {
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    public EditPR(String prID) {
        initComponents();
        this.currentPRID = prID;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        PRTitle.setText("Edit Purchase Requisition - " + prID);
        loadPurchaseRequisitionData();
        populateItemIDs();
    }
    
    private void loadPurchaseRequisitionData() {
        if (currentPRID == null) {
            return;
        }

        try {
            String[] prData = loadPRById(currentPRID);
            
            if (prData != null && prData.length >= 8) {
                populatePRFields(prData);
                saveButton.setText("Update");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Purchase Requisition not found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading purchase requisition data: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    private void populatePRFields(String[] prData) {
        String itemIDValue = prData[1];
        int quantityValue = Integer.parseInt(prData[2]);
        double priceValue = Double.parseDouble(prData[3]);
        double totalPriceValue = Double.parseDouble(prData[4]);
        LocalDate creationDateValue = LocalDate.parse(prData[5]);
        String supplierIDValue = prData[6];
        LocalDate requiredDeliveryDateValue = LocalDate.parse(prData[7]);
        
        // Set the form fields with the loaded data
        itemID.setSelectedItem(itemIDValue);
        quantity.setText(String.valueOf(quantityValue));
        unitPrice.setText(String.valueOf(priceValue));
        supplierID.setText(supplierIDValue);
        requiredDeliveryDate.setText(requiredDeliveryDateValue.toString());
        creationDate.setText(creationDateValue.toString());
        
        // Add the item to the table
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        model.setRowCount(0); // Clear the table
        model.addRow(new Object[]{
            itemIDValue,
            quantityValue,
            priceValue,
            totalPriceValue
        });
        
        try {
            Item item = FileHandler.getItemById(itemIDValue);
            if (item != null) {
                itemName.setText(item.getItemName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private String[] loadPRById(String prID) throws IOException {
        return FileHandler.getPurchaseRequisitionById(prID);
    }
    
    private void populateItemIDs() {
        try {
            List<Item> items = FileHandler.loadAllItems();
            String[] itemIDs = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                itemIDs[i] = items.get(i).getItemID();
            }
            
            DefaultComboBoxModel<String> itemModel = new DefaultComboBoxModel<>(itemIDs);
            itemID.setModel(itemModel);
            
            // Set up listener to update item name when selection changes
            itemID.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    updateItemNameAndPrice();
                }
            });
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading items: " + e.getMessage(),
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    private void updateItemNameAndPrice() {
        String selectedItemID = (String) itemID.getSelectedItem();
        if (selectedItemID != null && !selectedItemID.isEmpty()) {
            try {
                Item item = FileHandler.getItemById(selectedItemID);
                if (item != null) {
                    itemName.setText(item.getItemName());
                    unitPrice.setText(String.valueOf(item.getPrice()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Validates quantity input
     */
    private int validateQuantity() {
        try {
            String quantityText = quantity.getText().trim();
            if (quantityText.isEmpty()) {
                showError("Please enter quantity");
                return -1;
            }
            int qty = Integer.parseInt(quantityText);
            if (qty <= 0) {
                showError("Quantity must be positive");
                return -1;
            }
            return qty;
        } catch (NumberFormatException e) {
            showError("Invalid quantity format");
            return -1;
        }
    }
    
    /**
     * Validates delivery date input
     */
    private LocalDate validateDeliveryDate() {
        try {
            String dateText = requiredDeliveryDate.getText().trim();
            if (dateText.isEmpty()) {
                showError("Please enter required delivery date");
                return null;
            }
            
            LocalDate deliveryDate = LocalDate.parse(dateText);
            LocalDate today = LocalDate.now();
            
            if (deliveryDate.isBefore(today)) {
                showError("Delivery date cannot be in the past!");
                return null;
            }
            return deliveryDate;
        } catch (DateTimeParseException e) {
            showError("Invalid date format. Please use YYYY-MM-DD");
            return null;
        }
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
        saveButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

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
                .addGroup(PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PRPanelLayout.createSequentialGroup()
                        .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(147, 147, 147))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PRPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        PRPanelLayout.setVerticalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PRPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PRTitle)
                .addGap(14, 14, 14))
        );

        salesManagerLabel.setText("Sales Manager:");

        itemLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemLabel.setText("Item");

        itemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemNameLabel.setText("Item Name");

        quantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        quantityLabel.setText("Quantity");

        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        priceLabel.setText("Price");

        supplierIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierIDLabel.setText("Supplier ID");

        requiredDeliveryDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        requiredDeliveryDateLabel.setText("Required Delivery Date");

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
                "Item ID", "Quantity", "Price", "Total Amount"
            }
        ));
        prTable.setRowHeight(30);
        jScrollPane1.setViewportView(prTable);

        creationDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        creationDateLabel.setText("Creation Date:");

        saveButton.setBackground(new java.awt.Color(255, 255, 204));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(salesManagerLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(creationDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(creationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(requiredDeliveryDateLabel)
                                .addGap(18, 18, 18)
                                .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(supplierIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(unitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clearButton)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(375, 375, 375)
                                .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(PRPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(creationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salesManagerLabel)
                            .addComponent(creationDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
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
                        .addGap(18, 18, 18)
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
                .addContainerGap())
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
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
         try {
        // Validate inputs
        String selectedItemID = (String) itemID.getSelectedItem();
        if (selectedItemID == null || selectedItemID.isEmpty()) {
            showError("Please select an item");
            return;
        }
        
        int quantityValue = validateQuantity();
        if (quantityValue <= 0) return;
        
        double priceValue;
        try {
            priceValue = Double.parseDouble(unitPrice.getText());
            if (priceValue <= 0) {
                showError("Price must be positive");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid price format");
            return;
        }
        
        LocalDate deliveryDate = validateDeliveryDate();
        if (deliveryDate == null) return;
        
        // Calculate total price
        double totalPrice = priceValue * quantityValue;
        
        // Get creation date from the form
        LocalDate creationDateValue;
        try {
            creationDateValue = LocalDate.parse(creationDate.getText());
        } catch (DateTimeParseException e) {
            creationDateValue = LocalDate.now();
        }
        
        // Get supplier ID from the form
        String supplierIDValue = supplierID.getText();
        
        // Update the table to show the changes
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        model.setRowCount(0); // Clear the table
        model.addRow(new Object[]{
            selectedItemID,
            quantityValue,
            priceValue,
            totalPrice
        });
        
        // Try to get the existing status or use default
        String status = "SAVED"; // Default status
        try {
            String[] prData = FileHandler.getPurchaseRequisitionById(currentPRID);
            if (prData != null && prData.length > 8) {
                status = prData[8]; // Keep the existing status
            }
        } catch (Exception e) {
            // If there's an error getting the status, just use the default
        }
        
        // Update PR in file using FileHandler
        boolean updated = FileHandler.updatePurchaseRequisition(
            currentPRID,
            selectedItemID,
            quantityValue,
            priceValue,
            totalPrice,
            creationDateValue,
            supplierIDValue,
            deliveryDate,
            status
        );
        
        if (updated) {
            showSuccess("Purchase Requisition saved successfully!");
        } else {
            throw new IOException("Purchase Requisition not found in file");
        }
    } catch (Exception e) {
        showError("Error saving Purchase Requisition: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void quantityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantityActionPerformed
        try {
        int qty = Integer.parseInt(quantity.getText());
        double price = Double.parseDouble(unitPrice.getText());
        double total = qty * price;
        
        // Update the table to show the changes
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        String selectedItemID = (String) itemID.getSelectedItem();
        
        if (model.getRowCount() > 0) {
            model.setValueAt(qty, 0, 1);
            model.setValueAt(total, 0, 3);
        } else if (selectedItemID != null && !selectedItemID.isEmpty()) {
            // If there's no row yet, add one
            model.addRow(new Object[]{
                selectedItemID,
                qty,
                price,
                total
            });
        }
    } catch (NumberFormatException e) {
        // Ignore if inputs aren't valid numbers yet
    }

    }//GEN-LAST:event_quantityActionPerformed

    private void requiredDeliveryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDeliveryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDeliveryDateActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearFields();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        try {
        // Validate inputs
        String selectedItemID = (String) itemID.getSelectedItem();
        if (selectedItemID == null || selectedItemID.isEmpty()) {
            showError("Please select an item");
            return;
        }
        
        int quantityValue = validateQuantity();
        if (quantityValue <= 0) return;
        
        double priceValue;
        try {
            priceValue = Double.parseDouble(unitPrice.getText());
            if (priceValue <= 0) {
                showError("Price must be positive");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid price format");
            return;
        }
        
        LocalDate deliveryDate = validateDeliveryDate();
        if (deliveryDate == null) return;
        
        // Calculate total price
        double totalPrice = priceValue * quantityValue;
        
        // Get creation date and supplier ID from the form
        LocalDate creationDateValue = LocalDate.parse(creationDate.getText());
        String supplierIDValue = supplierID.getText();
        
        // Update the table to show the changes
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        model.setRowCount(0); // Clear the table
        model.addRow(new Object[]{
            selectedItemID,
            quantityValue,
            priceValue,
            totalPrice
        });
        
        // Update PR in file with SUBMITTED status using FileHandler
        boolean updated = FileHandler.updatePurchaseRequisition(
            currentPRID,
            selectedItemID,
            quantityValue,
            priceValue,
            totalPrice,
            creationDateValue,
            supplierIDValue,
            deliveryDate,
            "SUBMITTED"
        );
        
        if (updated) {
            showSuccess("Purchase Requisition submitted successfully!");
            this.dispose(); // Close the form after submitting
        } else {
            throw new IOException("Purchase Requisition not found in file");
        }
    } catch (Exception e) {
        showError("Error submitting Purchase Requisition: " + e.getMessage());
        e.printStackTrace();
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
    private javax.swing.JLabel salesManagerLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel supplierID;
    private javax.swing.JLabel supplierIDLabel;
    private javax.swing.JLabel unitPrice;
    // End of variables declaration//GEN-END:variables
}
