
package SalesManager;

import model.Item;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class MonthlySalesReportPanel extends javax.swing.JPanel {
    private YearMonth currentMonth;
    private DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    private DateTimeFormatter monthFileFormat = DateTimeFormatter.ofPattern("yyyy-MM");
    private double totalMonthlyAmount = 0.0;
    
    public MonthlySalesReportPanel() {
        initComponents();
        currentMonth = YearMonth.now();
        updateMonthDisplay();
        loadMonthlySalesData();
    }
    
    private void updateMonthDisplay() {
            month.setText(currentMonth.format(monthFormatter));
        }
    
    private void loadMonthlySalesData() {
        DefaultTableModel model = (DefaultTableModel) monthlyTable.getModel();
        model.setRowCount(0);
        totalMonthlyAmount = 0.0;
        
        try {
            // Get all items first
            List<Item> allItems = FileHandler.getMonthlySalesItems();
            List<String[]> salesRecords = FileHandler.getMonthlySalesRecords();
            String monthFilter = currentMonth.format(monthFileFormat);
            
            // For each item, calculate its monthly sales
            for (Item item : allItems) {
                String itemID = item.getItemID();
                String itemName = item.getItemName();
                double price = item.getPrice();
                int totalQuantitySold = 0;
                
                // Find all sales for this item in the current month
                for (String[] saleParts : salesRecords) {
                    if (saleParts[1].equals(itemID) && saleParts[3].startsWith(monthFilter)) {
                        int quantitySold = Integer.parseInt(saleParts[2]);
                        totalQuantitySold += quantitySold;
                        totalMonthlyAmount += (price * quantitySold);
                    }
                }
                
                // Only add items that had sales this month
                if (totalQuantitySold > 0) {
                    double itemTotalAmount = price * totalQuantitySold;
                    model.addRow(new Object[]{
                        itemID,
                        itemName,
                        totalQuantitySold,
                        price,
                        itemTotalAmount,
                        String.format("RM%.2f", price),
                        String.format("RM%.2f", itemTotalAmount)    
                    });
                }
            }
            totalSales.setText(String.format("RM%.2f", totalMonthlyAmount));
            
        }catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading sales data: " + e.getMessage(), 
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        totalSalesAmount = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        monthDisplayLabel = new javax.swing.JLabel();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        monthlyTable = new javax.swing.JTable();
        month = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        totalSales = new javax.swing.JLabel();

        totalSalesAmount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 204, 102));

        monthDisplayLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        monthDisplayLabel.setText("Month : ");

        previousButton.setText("<");
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousButtonActionPerformed(evt);
            }
        });

        nextButton.setText(">");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        monthlyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Item ID", "Item Name", "Total Quantity", "Price", "Total Amount"
            }
        ));
        monthlyTable.setRowHeight(40);
        jScrollPane1.setViewportView(monthlyTable);

        month.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        month.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Total Sales Amount:");

        totalSales.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        totalSales.setText("jLabel2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(totalSales)
                        .addGap(15, 15, 15)))
                .addGap(19, 19, 19))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addComponent(previousButton)
                .addGap(33, 33, 33)
                .addComponent(monthDisplayLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(nextButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nextButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monthDisplayLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(previousButton))
                    .addComponent(month, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(totalSales))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void previousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousButtonActionPerformed
        currentMonth = currentMonth.minusMonths(1);
        updateMonthDisplay();
        loadMonthlySalesData();
    }//GEN-LAST:event_previousButtonActionPerformed

    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextButtonActionPerformed
        currentMonth = currentMonth.plusMonths(1);
        updateMonthDisplay();
        loadMonthlySalesData();
    }//GEN-LAST:event_nextButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel month;
    private javax.swing.JLabel monthDisplayLabel;
    private javax.swing.JTable monthlyTable;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton previousButton;
    private javax.swing.JLabel totalSales;
    private javax.swing.JLabel totalSalesAmount;
    // End of variables declaration//GEN-END:variables
}
