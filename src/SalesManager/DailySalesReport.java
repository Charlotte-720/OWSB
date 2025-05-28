package SalesManager;

import model.Item;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import InventoryManager.functions.InventoryService;

public class DailySalesReport extends javax.swing.JFrame {
    private List<String> lowStockAlerts = new ArrayList<>();
    private static final int LOW_STOCK_THRESHOLD = InventoryService.LOW_STOCK_THRESHOLD;

    public DailySalesReport() {
        initComponents();
        loadSalesRecords();
        saleDate.setText(LocalDate.now().toString());
    }
    
    private double getPrice(String itemID) {
        try {
            Item item = FileHandler.getItemById(itemID);
            return item != null ? item.getPrice() : 0.0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    
    private int getTotalStock(String itemID) {
        try {
            Item item = FileHandler.getItemById(itemID);
            return item != null ? item.getTotalStock() : 0;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private String getItemName(String itemID) {
        try {
            Item item = FileHandler.getItemById(itemID);
            return item != null ? item.getItemName() : "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void loadSalesRecords() {
    DefaultTableModel model = (DefaultTableModel) salesTable.getModel();
    model.setRowCount(0); 
    double totalSalesAmount = 0.0;
    lowStockAlerts.clear();

    // Set to track items already checked for low stock
    java.util.Set<String> checkedItems = new java.util.HashSet<>();
        
    try {
        List<String[]> salesRecords = FileHandler.readSalesRecords();
        String today = LocalDate.now().toString();
        for (String[] parts : salesRecords) {
            String saleID = parts[0];
            String itemID = parts[1];
            int quantitySold = Integer.parseInt(parts[2]);
            String saleDate = parts[3];

            if (!saleDate.equals(today)) {
                continue; // skip sales not made today
            }

            String itemName = getItemName(itemID);
            int totalStock = getTotalStock(itemID);
            double price = getPrice(itemID);
            double totalAmount = quantitySold * price;

            model.addRow(new Object[]{
                saleID, 
                itemID, 
                itemName, 
                totalStock, 
                quantitySold, 
                price, 
                totalAmount
            });
            
            totalSalesAmount += totalAmount;
            
            if (totalStock < LOW_STOCK_THRESHOLD && !checkedItems.contains(itemID)) { 
                    lowStockAlerts.add("Item ID: " + itemName + " (" + itemID + ")\nStock left: " + totalStock);
                    checkedItems.add(itemID); // Mark this item as checked
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading sales data", 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
        }
            this.totalAmount.setText(String.format("%.2f", totalSalesAmount));
        }
    
    public void refreshSalesData() {
    loadSalesRecords(); 
    
    try {
        loadSalesRecords(); 
    } catch (Exception e) {
        System.out.println("Error in refreshSalesData: " + e.getMessage());
        e.printStackTrace();
    }
        showLowStockAlerts();
    }
    
    private void showLowStockAlerts() {
            if (!lowStockAlerts.isEmpty()) {
                StringBuilder alertMessage = new StringBuilder();
                alertMessage.append("LOW STOCK WARNINGS:\n\n");

                for (int i = 0; i < lowStockAlerts.size(); i++) {
                    alertMessage.append(i + 1).append(". ").append(lowStockAlerts.get(i)).append("\n\n");
                }

                JOptionPane.showMessageDialog(this, 
                    alertMessage.toString(),
                    "Low Stock Warning - Inventory Alert",
                    JOptionPane.WARNING_MESSAGE);
            }
        }
    
    public List<String> getLowStockAlerts() {
        return new ArrayList<>(lowStockAlerts);
    }
    
    public boolean hasLowStockAlerts() {
        return !lowStockAlerts.isEmpty();
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(SalesAmountLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saleDate, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(453, 453, 453)
                        .addComponent(addSalesButton)))
                .addGap(0, 21, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(SalesAmountLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                    .addComponent(totalAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(salesReportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 323, Short.MAX_VALUE)))
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void addSalesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSalesButtonActionPerformed
        AddDailySales addSalesDialog = new AddDailySales(this);
        addSalesDialog.setVisible(true);
    }//GEN-LAST:event_addSalesButtonActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
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
