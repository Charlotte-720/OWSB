package SalesManager;

import SalesManager.DataHandlers.ItemFileHandler;
import SalesManager.DataHandlers.PRFileHandler;
import java.awt.Component;
import model.Item;
import model.PurchaseRequisition;
import model.PRItem;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class AddPRRestock extends javax.swing.JFrame {
    private java.util.List<Item> itemList;
    private String currentSalesManagerID;
    private Component previousComponent;
    
     public AddPRRestock(String salesManagerID, Component previousComponent) {
        this.currentSalesManagerID = salesManagerID;
        this.previousComponent = previousComponent;
        
        itemList = loadItemsFromFile(); 
        initComponents();
        creationDate.setText(LocalDate.now().toString());
        salesManager.setText(salesManagerID);
        
        populateItemIDs();
        itemID.addActionListener(e -> updateItemName());
    }
    
    private double getUnitPrice(String itemID) {
        for (Item item : itemList) {
            if (item.getItemID().equals(itemID)) {
                return item.getPrice();
            }
        }
        return 0.0; 
    }
    
    private String getSupplierID(String itemID) {
        for (Item item : itemList) {
            if (item.getItemID().equals(itemID)) {
                return item.getSupplierID();
            }
        }
        return ""; // Supplier not found
    }
    
    private java.util.List<Item> loadItemsFromFile() {
        try {
            // Use FileHandler to load items
            return ItemFileHandler.loadAllItems();
        } catch (IOException e) {
            System.err.println("Error loading items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    private void populateItemIDs() {
        itemID.removeAllItems(); 
        for (Item item : itemList) {
            itemID.addItem(item.getItemID());
        }
    }
    
    private void updateItemName() {
        String selectedItemID = (String) itemID.getSelectedItem();
        for (Item item : itemList) {
            if (item.getItemID().equals(selectedItemID)) {
                itemName.setText(item.getItemName());
                unitPrice.setText(String.valueOf(item.getPrice()));
                supplierID.setText(item.getSupplierID());
                return;
            }
        }
        itemName.setText(""); 
        unitPrice.setText(""); 
        supplierID.setText(""); 
    }
    
    private void clearForm() {
        quantity.setText("");
        requiredDeliveryDate.setText("");
        if (itemID.getItemCount() > 0) {
            itemID.setSelectedIndex(0);
            updateItemName(); // Update fields when clearing
        }
    }
    
    private String getCurrentSalesManagerID() {
        return this.currentSalesManagerID;
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        quantityLabel = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        supplierIDLabel = new javax.swing.JLabel();
        supplierID = new javax.swing.JLabel();
        unitPrice = new javax.swing.JLabel();
        quantity = new javax.swing.JTextField();
        itemName = new javax.swing.JLabel();
        itemNameLabel = new javax.swing.JLabel();
        itemQuantity = new javax.swing.JLabel();
        itemID = new javax.swing.JComboBox<>();
        requiredDeliveryDateLabel = new javax.swing.JLabel();
        requiredDeliveryDate = new javax.swing.JTextField();
        submitButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        creationDateLabel = new javax.swing.JLabel();
        creationDate = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        SMLabel = new javax.swing.JLabel();
        PRPanel = new javax.swing.JPanel();
        createPRTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        salesManager = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(239, 252, 251));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        quantityLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        quantityLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        quantityLabel.setText("Quantity");

        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        priceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        priceLabel.setText("Price: ");

        supplierIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        supplierIDLabel.setText("Supplier ID:");

        itemNameLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemNameLabel.setText("Item Name:");

        itemQuantity.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemQuantity.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        itemQuantity.setText("Item:");
        itemQuantity.setToolTipText("");
        itemQuantity.setFocusable(false);

        itemID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemIDActionPerformed(evt);
            }
        });

        requiredDeliveryDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        requiredDeliveryDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        requiredDeliveryDateLabel.setText("Required Delivery Date:");

        requiredDeliveryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requiredDeliveryDateActionPerformed(evt);
            }
        });

        submitButton.setBackground(new java.awt.Color(204, 255, 204));
        submitButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        submitButton.setText("Submit ");
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

        prTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Item Name", "Quantity", "Price", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        prTable.setRowHeight(30);
        jScrollPane2.setViewportView(prTable);

        creationDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        creationDateLabel.setText("Creation Date: ");

        saveButton.setBackground(new java.awt.Color(255, 255, 204));
        saveButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        saveButton.setText("Save");
        saveButton.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, java.awt.Color.orange, new java.awt.Color(153, 255, 0)));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        SMLabel.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        SMLabel.setForeground(new java.awt.Color(51, 0, 153));
        SMLabel.setText("Sales Manager : ");

        PRPanel.setBackground(new java.awt.Color(204, 255, 204));

        createPRTitle.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        createPRTitle.setText("Restock Existing Item");

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
                .addComponent(createPRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(67, 67, 67)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        PRPanelLayout.setVerticalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(0, 39, Short.MAX_VALUE))
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(createPRTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(56, 56, 56)
                                .addComponent(clearButton))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(supplierIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(requiredDeliveryDateLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemNameLabel)
                            .addComponent(quantityLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(152, 152, 152)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(unitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemID, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(SMLabel)
                        .addGap(4, 4, 4)
                        .addComponent(salesManager, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(creationDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(creationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20))))
            .addComponent(PRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(PRPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(creationDateLabel)
                            .addComponent(creationDate, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SMLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(salesManager, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(itemID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(itemQuantity))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(itemNameLabel)
                            .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(quantityLabel)
                            .addComponent(quantity, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(priceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(unitPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(supplierIDLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(supplierID, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(requiredDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(requiredDeliveryDateLabel))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10))))
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

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        try {
            if (quantity.getText().trim().isEmpty()) {
                showError("Please enter quantity");
                return;
            }
            
        // Validate delivery date
            LocalDate deliveryDate = validateDeliveryDate();
            if (deliveryDate == null) {
                return; // Stop if date is invalid
            }

            String selectedItemID = (String) itemID.getSelectedItem();
            if (selectedItemID == null) {
                showError("Please select an item");
                return;
            }
            
            int qty = Integer.parseInt(quantity.getText().trim());
            if (qty <= 0) {
                showError("Quantity must be greater than 0");
                return;
            }
            
            double pricePerUnit = Double.parseDouble(unitPrice.getText());
            double totalPrice = qty * pricePerUnit;
            
            String currentSupplierID = getSupplierID(selectedItemID);
            if (currentSupplierID.isEmpty()) {
                showError("Supplier not found for the selected item.");
                return;
            }
            
            String selectedItemName = getItemName(selectedItemID);
            
            // Add data into the table
            DefaultTableModel model = (DefaultTableModel) prTable.getModel();
            model.addRow(new Object[]{selectedItemID,selectedItemName, qty, pricePerUnit, totalPrice});

            // Clear the input fields after adding
            quantity.setText("");
            javax.swing.JOptionPane.showMessageDialog(this, 
                    "Data prepared successfully! Please review the table and click 'Update' to save changes.", "Success", 
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for quantity.");
            }
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
    private String getItemName(String itemID) {
        for (Item item : itemList) {
            if (item.getItemID().equals(itemID)) {
                return item.getItemName();
            }
        }
        return ""; // Item name not found
    }
    
    private void showError(String message) {
    javax.swing.JOptionPane.showMessageDialog(this, message);

    
    }//GEN-LAST:event_submitButtonActionPerformed

    private void requiredDeliveryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requiredDeliveryDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_requiredDeliveryDateActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearForm();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        if (model.getRowCount() == 0) {
            showError("Please add at least one item before saving the PR");
            return;
        }
        
        if (requiredDeliveryDate.getText().trim().isEmpty()) {
            showError("Please enter required delivery date");
            return;
        }
        
        try {
            // Generate PR ID using FileHandler
            String prNumber = PRFileHandler.generatePRID();
            String prType = PurchaseRequisition.TYPE_RESTOCK; // Add PR Type
            LocalDate creationDate = LocalDate.now();
            String status = "Pending";
            String salesManagerID = getCurrentSalesManagerID();
            LocalDate deliveryDate = LocalDate.parse(requiredDeliveryDate.getText().trim());
            
            // Create list of PR items
            List<PRItem> prItems = new ArrayList<>();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0) != null) { 
                    // Fix the casting issue - use correct column indices
                    String itemIDValue = (String) model.getValueAt(i, 0);        // Column 0: Item ID
                    String itemNameValue = (String) model.getValueAt(i, 1);      // Column 1: Item Name
                    
                    // Handle quantity - might be Integer or String depending on how it was added
                    int quantityValue;
                    Object qtyObj = model.getValueAt(i, 2);  // Column 2: Quantity
                    if (qtyObj instanceof Integer) {
                        quantityValue = (Integer) qtyObj;
                    } else if (qtyObj instanceof String) {
                        quantityValue = Integer.parseInt((String) qtyObj);
                    } else {
                        quantityValue = ((Number) qtyObj).intValue();
                    }
                    
                    // Handle price - might be Double or String depending on how it was added
                    double price;
                    Object priceObj = model.getValueAt(i, 3);  // Column 3: Price
                    if (priceObj instanceof Double) {
                        price = (Double) priceObj;
                    } else if (priceObj instanceof String) {
                        price = Double.parseDouble((String) priceObj);
                    } else {
                        price = ((Number) priceObj).doubleValue();
                    }
                    
                    String currentSupplierID = getSupplierID(itemIDValue);
                    
                    // Create PRItem using the corrected values
                    PRItem prItem = new PRItem(
                        itemIDValue, 
                        quantityValue, 
                        itemNameValue,    // Use the item name from table
                        currentSupplierID, 
                        price, 
                        deliveryDate
                    );
                    prItems.add(prItem);
                }
            }
            
            // Create PurchaseRequisition object with PR Type
            PurchaseRequisition pr = new PurchaseRequisition(
                prNumber,
                prType,           // Add PR Type parameter
                salesManagerID,
                creationDate,
                deliveryDate,
                status,
                prItems
            );
            
            // Save using FileHandler
            PRFileHandler.savePurchaseRequisition(pr);
            
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Purchase Requisition " + prNumber + " saved successfully!\n" +
                "PR Type: " + prType + "\n" +  // Add PR Type to success message
                "Status: " + status + "\n" +
                "Created by: " + getCurrentSalesManagerID(),
                "PR Saved", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose(); 
            
        } catch (IOException e) {
            showError("Error saving PR: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("Error processing PR: " + e.getMessage());
            e.printStackTrace();
        }  
    
    }//GEN-LAST:event_saveButtonActionPerformed

    private void itemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemIDActionPerformed
        updateItemName();
    }//GEN-LAST:event_itemIDActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
        if (previousComponent != null) {
            previousComponent.setVisible(true);
        }
    }//GEN-LAST:event_jLabel1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PRPanel;
    private javax.swing.JLabel SMLabel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel createPRTitle;
    private javax.swing.JLabel creationDate;
    private javax.swing.JLabel creationDateLabel;
    private javax.swing.JComboBox<String> itemID;
    private javax.swing.JLabel itemName;
    private javax.swing.JLabel itemNameLabel;
    private javax.swing.JLabel itemQuantity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable prTable;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JTextField quantity;
    private javax.swing.JLabel quantityLabel;
    private javax.swing.JTextField requiredDeliveryDate;
    private javax.swing.JLabel requiredDeliveryDateLabel;
    private javax.swing.JLabel salesManager;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JLabel supplierID;
    private javax.swing.JLabel supplierIDLabel;
    private javax.swing.JLabel unitPrice;
    // End of variables declaration//GEN-END:variables
}
