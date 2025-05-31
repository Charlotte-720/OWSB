package SalesManager;

import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import InventoryManager.functions.InventoryService;
import SalesManager.Functions.salesFunction;
import java.awt.Component;
import java.io.IOException;
import model.SalesRecord;
import model.Item;

public class DailySalesReport extends javax.swing.JFrame {
    private static final int LOW_STOCK_THRESHOLD = InventoryService.LOW_STOCK_THRESHOLD;
    private LocalDate currentDate;
    private Component previousComponent;
    
    
    public DailySalesReport(Component previousComponent) {
        this.previousComponent = previousComponent;
        initComponents();
        this.currentDate = LocalDate.now();
        showLowStockAlertsIfAny();
        loadTodaysSalesData();
        saleDate.setText(currentDate.toString());
    }
    
    private void loadTodaysSalesData() {
        loadSalesDataForDate(LocalDate.now());
    }
    
    public void loadSalesDataForDate(LocalDate date) {
        try {
            this.currentDate = date;
            
            // Use SalesRecordFileHandler2 to get sales records for date range
            List<SalesRecord> salesRecords = salesFunction.getSalesRecordsByDateRange(date, date);
            
            updateTableWithSalesData(salesRecords);
            updateTotalAmountDisplay(salesRecords);
            saleDate.setText(date.toString());
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading sales data: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTableWithSalesData(List<SalesRecord> salesRecords) {
        DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
        model.setRowCount(0); // Clear table
        
        try {
            for (SalesRecord record : salesRecords) {
                // Call salesFunction.getItemById() to get item details
                Item item = salesFunction.getItemById(record.getItemID());
                
                if (item != null) {
                    Object[] rowData = {
                        record.getSaleID(),
                        record.getItemID(),
                        item.getItemName(),
                        item.getTotalStock(),
                        record.getQuantitySold(),
                        item.getPrice(),
                        String.format("RM%.2f", record.getTotalAmount() / record.getQuantitySold()) 
                    };
                    model.addRow(rowData);
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating table: " + e.getMessage());
        }
    }
    
    private void updateTotalAmountDisplay(List<SalesRecord> salesRecords) {
        double total = 0.0;
        for (SalesRecord record : salesRecords) {
            total += record.getTotalAmount() / record.getQuantitySold();
        }
        this.totalAmount.setText(String.format("RM%.2f", total));
    }

    private void showLowStockAlertsIfAny() {
        try {
            // Call salesFunction.getCurrentLowStockAlerts()
            List<String> alerts = salesFunction.getCurrentLowStockAlerts();
            
            if (!alerts.isEmpty()) {
                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("LOW STOCK WARNINGS:\n\n");
                
                for (int i = 0; i < alerts.size(); i++) {
                    alertMessage.append(i + 1).append(". ").append(alerts.get(i)).append("\n\n");
                }
                
                JOptionPane.showMessageDialog(this, 
                    alertMessage.toString(),
                    "Low Stock Warning - Inventory Alert",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            System.err.println("Error checking low stock: " + e.getMessage());
        }
    }
    
    public void refreshSalesData() {
        loadSalesDataForDate(currentDate);
        showLowStockAlertsIfAny();
    }
    
    public List<String> getLowStockAlerts() {
        try {
            return salesFunction.getCurrentLowStockAlerts();
        } catch (IOException e) {
            System.err.println("Error getting alerts: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    
    public boolean hasLowStockAlerts() {
        try {
            return !salesFunction.getCurrentLowStockAlerts().isEmpty();
        } catch (IOException e) {
            return false;
        }
    }
    
    public double getCurrentTotalSales() {
        try {
            if (currentDate.equals(LocalDate.now())) {
                // Call salesFunction.getTodaysTotalSales()
                return salesFunction.getTodaysTotalSales();
            } else {
                // Call salesFunction.getSalesRecordsByDateRange()
                List<SalesRecord> records = salesFunction.getSalesRecordsByDateRange(currentDate, currentDate);
                return records.stream().mapToDouble(SalesRecord::getTotalAmount).sum();
            }
        } catch (IOException e) {
            return 0.0;
        }
    }
    
    public int getCurrentSalesCount() {
        try {
            if (currentDate.equals(LocalDate.now())) {
                // Call salesFunction.getTodaysSalesCount()
                return salesFunction.getTodaysSalesCount();
            } else {
                // Call salesFunction.getSalesRecordsByDateRange()
                List<SalesRecord> records = salesFunction.getSalesRecordsByDateRange(currentDate, currentDate);
                return records.size();
            }
        } catch (IOException e) {
            return 0;
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        salesReportPanel = new javax.swing.JPanel();
        salesReportTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        addSalesButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        saleDate = new javax.swing.JLabel();
        SalesAmountLabel = new javax.swing.JLabel();
        totalAmount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(249, 237, 247));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        salesReportPanel.setBackground(new java.awt.Color(255, 153, 204));

        salesReportTitle.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        salesReportTitle.setText("Sales Report");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout salesReportPanelLayout = new javax.swing.GroupLayout(salesReportPanel);
        salesReportPanel.setLayout(salesReportPanelLayout);
        salesReportPanelLayout.setHorizontalGroup(
            salesReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesReportPanelLayout.createSequentialGroup()
                .addGap(234, 234, 234)
                .addComponent(salesReportTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        salesReportPanelLayout.setVerticalGroup(
            salesReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(salesReportPanelLayout.createSequentialGroup()
                .addGroup(salesReportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(salesReportPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(salesReportTitle)))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        dateLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        dateLabel.setText("Date :");

        addSalesButton.setBackground(new java.awt.Color(255, 255, 204));
        addSalesButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        addSalesButton.setText("Add Sales");
        addSalesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addSalesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSalesButtonActionPerformed(evt);
            }
        });

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Sales ID", "Item ID", "Item Name", "Total Stock", "Quantity Sold", "Price", "Total Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        salesTable.setEditingColumn(3);
        salesTable.setRowHeight(40);
        jScrollPane1.setViewportView(salesTable);

        SalesAmountLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 12)); // NOI18N
        SalesAmountLabel.setText("Sales Amount:");

        totalAmount.setBackground(new java.awt.Color(255, 255, 204));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saleDate, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(453, 453, 453)
                        .addComponent(addSalesButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(SalesAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 13, Short.MAX_VALUE))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(salesReportPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(124, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(addSalesButton)
                    .addComponent(saleDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(SalesAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(salesReportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 318, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
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

    private void addSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSalesButtonActionPerformed
        this.setVisible(false);
        AddDailySales addSalesDialog = new AddDailySales(this);
        addSalesDialog.setVisible(true);
    }//GEN-LAST:event_addSalesButtonActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
        if (previousComponent != null) {
            previousComponent.setVisible(true);
        }
    }//GEN-LAST:event_jLabel1MouseClicked
    
    
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
//            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(DailySalesReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                DailySalesReport frame = new DailySalesReport();
//                frame.setVisible(true);
//                
//                for (String alert : frame.lowStockAlerts) {
//                javax.swing.JOptionPane.showMessageDialog(frame, 
//                    alert,
//                    "Low Stock Warning",
//                    javax.swing.JOptionPane.WARNING_MESSAGE
//                );
//            }
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel SalesAmountLabel;
    private javax.swing.JButton addSalesButton;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel saleDate;
    private javax.swing.JPanel salesReportPanel;
    private javax.swing.JLabel salesReportTitle;
    private javax.swing.JTable salesTable;
    private javax.swing.JLabel totalAmount;
    // End of variables declaration//GEN-END:variables
}
