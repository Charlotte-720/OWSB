package SalesManager.Interfaces;

import SalesManager.Interfaces.DailySalesReport;
import SalesManager.DataHandlers.ItemFileHandler;
import SalesManager.DataHandlers.SalesRecordFileHandler;
import SalesManager.Functions.salesFunction;
import model.Item;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.SalesRecord;

public class AddDailySales extends javax.swing.JFrame {
private java.util.List<Item> itemList;
private DailySalesReport parentFrame;
    
    public AddDailySales(DailySalesReport parent) {
        this.parentFrame = parent;
        
        try {
            itemList = ItemFileHandler.loadAllItems();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading items: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
            itemList = new ArrayList<>();
        }
        initComponents();
        saleDate.setText(LocalDate.now().toString());
        populateItemIDs();
    }
    
    private double getPrice(String itemID) throws IOException {
        return salesFunction.getItemPrice(itemID);
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
                // Display item name with available stock
                String displayText = String.format("%s (Stock: %d)", 
                    item.getItemName(), item.getTotalStock());
                itemName.setText(item.getItemName());
                stockAvailable.setText(displayText);
                return;
            }
        }
        stockAvailable.setText(""); 
    }
    
    private void validateQuantityInRealTime() {
        try {
            String quantityText = quantitySold.getText().trim();
            if (quantityText.isEmpty()) {
                return;
            }

            int requestedQuantity = Integer.parseInt(quantityText);
            String selectedID = (String) itemID.getSelectedItem();

            if (selectedID != null) {
                int availableStock = salesFunction.getAvailableStock(selectedID);
                if (requestedQuantity > availableStock) {
                    quantitySold.setBackground(new java.awt.Color(255, 200, 200));
                    quantitySold.setToolTipText("Quantity exceeds available stock (" + availableStock + ")");
                } else {
                    quantitySold.setBackground(java.awt.Color.WHITE);
                    quantitySold.setToolTipText(null);
                }
            }
        } catch (NumberFormatException | IOException e) {
            quantitySold.setBackground(java.awt.Color.WHITE);
            quantitySold.setToolTipText(null);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        DailySalesReportTitle = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        itemIDLabel = new javax.swing.JLabel();
        quantitySoldLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        submitButton = new javax.swing.JButton();
        saleDate = new javax.swing.JLabel();
        itemID = new javax.swing.JComboBox<>();
        quantitySold = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        stockAvailable = new javax.swing.JLabel();
        stockAvailableLabel = new javax.swing.JLabel();
        itemName = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(252, 239, 239));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        DailySalesReportTitle.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        DailySalesReportTitle.setText("Daily Sales Report");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 25)); // NOI18N
        jLabel2.setText("X");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(DailySalesReportTitle)
                        .addGap(92, 92, 92))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DailySalesReportTitle)
                .addGap(30, 30, 30))
        );

        itemIDLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        itemIDLabel.setText("Item ID :");

        quantitySoldLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        quantitySoldLabel.setText("Quantity Sold :");

        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dateLabel.setText("Date:");

        submitButton.setBackground(new java.awt.Color(255, 204, 153));
        submitButton.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        itemID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemIDActionPerformed(evt);
            }
        });

        quantitySold.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantitySoldActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Item Name :");

        stockAvailable.setText(" ");

        stockAvailableLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        stockAvailableLabel.setText("Stock Available :");

        itemName.setText(" ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(itemIDLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(saleDate, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(stockAvailableLabel)
                                        .addGap(23, 23, 23))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(quantitySoldLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(quantitySold, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(stockAvailable, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(65, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saleDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemIDLabel)
                    .addComponent(itemID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(stockAvailableLabel)
                    .addComponent(stockAvailable, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(quantitySoldLabel)
                    .addComponent(quantitySold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(submitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        try {
            String selectedID = (String) itemID.getSelectedItem();
            String quantityText = quantitySold.getText().trim();

            // Validate input - this returns an int (validated quantity)
            int validatedQuantity = salesFunction.validateSaleInput(selectedID, quantityText);

            // Process the sale - this returns a SalesRecord object
            SalesRecord saleRecord = salesFunction.processSale(selectedID, validatedQuantity);

            try {
                itemList = salesFunction.loadAllItems();
            } catch (IOException e) {
                System.out.println("Error reloading item list: " + e.getMessage());
            }

            // Show success message
            JOptionPane.showMessageDialog(this,
                String.format("Sale processed successfully!\nSale ID: %s\nTotal Amount: RM%.2f", 
                             saleRecord.getSaleID(), saleRecord.getTotalAmount()),
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            // Notify parent and close
            if (parentFrame != null) {
                parentFrame.refreshSalesData();
            }

            quantitySold.setText("");
            this.dispose();

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                "Error processing sale: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Unexpected error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_submitButtonActionPerformed

    private void quantitySoldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantitySoldActionPerformed
        validateQuantityInRealTime();
    }//GEN-LAST:event_quantitySoldActionPerformed

    private void itemIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemIDActionPerformed
        updateItemName();
        updateStockDisplay();
    }//GEN-LAST:event_itemIDActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked
    private void updateStockDisplay() {
    String selectedItemID = (String) itemID.getSelectedItem();
    for (Item item : itemList) {
        if (item.getItemID().equals(selectedItemID)) {
            if (stockAvailable != null) {
                stockAvailable.setText(" " + item.getTotalStock());
                stockAvailable.setForeground(
                    item.getTotalStock() > 0 ? java.awt.Color.BLACK : java.awt.Color.RED
                );
            }
            return;
        }
    }
    if (stockAvailable!= null) {
        stockAvailable.setText("0");
        stockAvailable.setForeground(java.awt.Color.RED);
    }
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel DailySalesReportTitle;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JComboBox<String> itemID;
    private javax.swing.JLabel itemIDLabel;
    private javax.swing.JLabel itemName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField quantitySold;
    private javax.swing.JLabel quantitySoldLabel;
    private javax.swing.JLabel saleDate;
    private javax.swing.JLabel stockAvailable;
    private javax.swing.JLabel stockAvailableLabel;
    private javax.swing.JButton submitButton;
    // End of variables declaration//GEN-END:variables
}
