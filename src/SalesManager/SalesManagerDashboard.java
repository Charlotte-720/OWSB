package SalesManager;

import Admin.Loginpage1;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SalesManagerDashboard extends javax.swing.JFrame {

    private String employeeID;
    private String position;
    
    public SalesManagerDashboard(String identifier) {
        String[] parts = identifier.split(":");
        if (parts.length == 2) {
            this.employeeID = parts[0];
            this.position = parts[1];
        } else {
            this.employeeID = "Unknown";
            this.position = "Unknown";
            System.out.println("Error: LoggedInIdentifier has an unexpected format: [" + identifier + "]");
        }
        
        initComponents();   
        // Update the label to show the actual user ID
        jLabel1.setText("Sales Manager, " + this.employeeID);
        // Set layout for jPanel3
        jPanel3.setLayout(new java.awt.BorderLayout());

        // Add the monthly sales report panel directly to jPanel3
        jPanel3.add(reportPanel, java.awt.BorderLayout.CENTER);

        // Refresh the panel
        jPanel3.revalidate();
        jPanel3.repaint();
    }
    
    public String getEmployeeName() {
        // If you have a separate name field, use it. Otherwise, use employeeID
        return "Sales Manager " + this.employeeID; // You can modify this based on your needs
    }
    
    
    MonthlySalesReportPanel reportPanel = new MonthlySalesReportPanel();
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        itemEntryButton = new javax.swing.JButton();
        dailySalesReportButton = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        supplierEntryButton = new javax.swing.JButton();
        purchaseRequisitionButton = new javax.swing.JButton();
        monthlySalesReportButton = new javax.swing.JButton();
        purchaseOrdersButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 235));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBackground(new java.awt.Color(255, 255, 153));

        itemEntryButton.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        itemEntryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon - item.png"))); // NOI18N
        itemEntryButton.setText("Manage Inventory Entries");
        itemEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemEntryButtonActionPerformed(evt);
            }
        });

        dailySalesReportButton.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        dailySalesReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon - daily sales.png"))); // NOI18N
        dailySalesReportButton.setText("Daily Sales Report");
        dailySalesReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dailySalesReportButtonActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Icon - logout.png"))); // NOI18N
        jButton3.setText("Logout");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        supplierEntryButton.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        supplierEntryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon- supplier.png"))); // NOI18N
        supplierEntryButton.setText("Manage Supplier Entries");
        supplierEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierEntryButtonActionPerformed(evt);
            }
        });

        purchaseRequisitionButton.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        purchaseRequisitionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon - pr.png"))); // NOI18N
        purchaseRequisitionButton.setText("Purchase Requisition ");
        purchaseRequisitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseRequisitionButtonActionPerformed(evt);
            }
        });

        monthlySalesReportButton.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        monthlySalesReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon- daily sales.png"))); // NOI18N
        monthlySalesReportButton.setText("Monthly Sales Report");
        monthlySalesReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthlySalesReportButtonActionPerformed(evt);
            }
        });

        purchaseOrdersButton.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        purchaseOrdersButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/Icon - report.png"))); // NOI18N
        purchaseOrdersButton.setText("View Purchase Orders ");
        purchaseOrdersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseOrdersButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(supplierEntryButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchaseRequisitionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dailySalesReportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monthlySalesReportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(itemEntryButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(purchaseOrdersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(48, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(111, 111, 111)
                .addComponent(itemEntryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(supplierEntryButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dailySalesReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(monthlySalesReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(purchaseRequisitionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(purchaseOrdersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 204, 153));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 268, Short.MAX_VALUE)
        );

        jLabel1.setFont(new java.awt.Font("Sitka Text", 0, 20)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Sales Manager, <userid>");

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Sitka Text", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 0));
        jLabel2.setText("Monthly Sales Report");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel4.setText("WELCOME BACK!");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/icon - sales manager.png"))); // NOI18N
        jLabel5.setText("jLabel5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel2))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void dailySalesReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dailySalesReportButtonActionPerformed
        this.setVisible(false);
        DailySalesReport dailySales = new DailySalesReport(this);
        dailySales.setVisible(true);
    }//GEN-LAST:event_dailySalesReportButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int response = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to log out?", 
        "Logout Confirmation", 
        JOptionPane.YES_NO_OPTION, 
        JOptionPane.QUESTION_MESSAGE);

        // Check the user's response
        if (response == JOptionPane.YES_OPTION) {
            // Dispose the AdminTest frame
            this.dispose();

            // Open Loginpage1 frame
            Loginpage1 loginPage = new Loginpage1();
            loginPage.setVisible(true);
            loginPage.pack();
            loginPage.setLocationRelativeTo(null);
            loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void supplierEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierEntryButtonActionPerformed
        this.setVisible(false);
        SupplierEntry supplierEntry = new SupplierEntry(this);
        supplierEntry.setVisible(true);
    }//GEN-LAST:event_supplierEntryButtonActionPerformed

    private void purchaseRequisitionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseRequisitionButtonActionPerformed
        this.setVisible(false);
        PROperations pr = new PROperations(this.employeeID, this);
        pr.setVisible(true);
    }//GEN-LAST:event_purchaseRequisitionButtonActionPerformed

    private void itemEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemEntryButtonActionPerformed
        this.setVisible(false);
        ItemEntry itemEntry = new ItemEntry(this);
        itemEntry.setVisible(true);
    }//GEN-LAST:event_itemEntryButtonActionPerformed

    private void monthlySalesReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthlySalesReportButtonActionPerformed
        this.setVisible(false);
        MonthlySalesReport monthlySales = new MonthlySalesReport(this);
        monthlySales.setVisible(true);
    }//GEN-LAST:event_monthlySalesReportButtonActionPerformed

    private void purchaseOrdersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseOrdersButtonActionPerformed
        this.setVisible(false);
        ViewPO viewPO = new ViewPO(this);
        viewPO.setVisible(true);
    }//GEN-LAST:event_purchaseOrdersButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SalesManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesManagerDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesManagerDashboard("Unexpected position value: ").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton dailySalesReportButton;
    private javax.swing.JButton itemEntryButton;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton monthlySalesReportButton;
    private javax.swing.JButton purchaseOrdersButton;
    private javax.swing.JButton purchaseRequisitionButton;
    private javax.swing.JButton supplierEntryButton;
    // End of variables declaration//GEN-END:variables
}
