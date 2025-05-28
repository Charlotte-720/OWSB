package SalesManager;

import SalesManager.Actions.TableActionCellEditor;
import SalesManager.Actions.TableActionCellRender;
import SalesManager.Actions.TableActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class PROperations extends javax.swing.JFrame {
    private String currentSalesManagerID;
    
    public PROperations(String salesManagerID) {
        this.currentSalesManagerID = salesManagerID;
        initComponents();
        loadPurchaseRequisition();
        setupActionColumn();
        date.setText(LocalDate.now().toString());
    }
        
    private void setupActionColumn() {
        // Set up the table action event
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void editButton(int row) {
                editPR(row);
            }

            @Override
            public void deleteButton(int row) {
                deletePR(row);
            }
        };
        
        // Set up action column (index 9)
        int actionColumnIndex = 9; // "Actions" is the 10th column (index 9)
        if (prTable.getColumnCount() > actionColumnIndex) {
            prTable.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(new TableActionCellRender());
            prTable.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new TableActionCellEditor(event));
        } else {
            System.err.println("Table doesn't have enough columns for actions");
        }
    }
    
    private void editPR(int row) {
    DefaultTableModel model = (DefaultTableModel) prTable.getModel();
    String salesManagerID = getCurrentSalesManagerID();
    
    if (model.getValueAt(row, 0) != null) {
        String prID = model.getValueAt(row, 0).toString();
        
        try {
            EditPR editForm = new EditPR(prID, salesManagerID);
            editForm.setVisible(true);
            
            editForm.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    loadPurchaseRequisition(); 
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error opening Edit form: " + e.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    } else {
        JOptionPane.showMessageDialog(this, 
            "No PR ID found at the selected row",
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}

    private void deletePR(int row) {
        if (prTable.isEditing()) {
            prTable.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        String prID = (String) model.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Delete Purchase Requisition " + prID + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Use FileHandler to delete PR
                boolean deleted = FileHandler.deletePurchaseRequisition(prID);
                if (deleted) {
                    loadPurchaseRequisition(); // Refresh table
                    JOptionPane.showMessageDialog(this, 
                        "Purchase Requisition deleted successfully",
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete Purchase Requisition",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error deleting Purchase Requisition: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void loadPurchaseRequisition() {
    DefaultTableModel model = (DefaultTableModel) prTable.getModel();
    model.setRowCount(0); 

    try {
        List<String[]> records = FileHandler.readPurchaseRequisitions();
        for (String[] parts : records) {
            if (parts.length >= 11) { 
                String prID = parts[0];           
                String itemName = parts[2];   
                String quantity = parts[3];      
                String unitPrice = parts[4];     
                String totalPrice = parts[5];     
                String supplierID = parts[6]; 
                String raisedBy = parts[7];       
                String requiredDeliveryDate = parts[8]; 
                String requestDate = parts[9];    
                String status = parts[10];     
                
                model.addRow(new Object[]{
                    prID,                   
                    itemName,                
                    quantity,               
                    unitPrice,               
                    totalPrice,             
                    requestDate,             
                    supplierID,             
                    requiredDeliveryDate,    
                    status,                  
                    ""                       
                });
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error loading Purchase Requisitions: " + e.getMessage(),
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
}
    private String getCurrentSalesManagerID() {
        return this.currentSalesManagerID != null ? this.currentSalesManagerID : "DEFAULT_SM";
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        RestockExistingItemButton = new javax.swing.JButton();
        PRPanel = new javax.swing.JPanel();
        PRTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        PurchaseNewItemButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(221, 246, 246));

        RestockExistingItemButton.setBackground(new java.awt.Color(255, 255, 204));
        RestockExistingItemButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        RestockExistingItemButton.setText("Restock Existing Item");
        RestockExistingItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestockExistingItemButtonActionPerformed(evt);
            }
        });

        PRPanel.setBackground(new java.awt.Color(204, 255, 204));

        PRTitle.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        PRTitle.setText("Purchase Requisitions");

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
                .addContainerGap(269, Short.MAX_VALUE)
                .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(209, 209, 209)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        PRPanelLayout.setVerticalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PRPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(90, 90, 90))
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(PRTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dateLabel.setText("Date");

        date.setText("jLabel3");

        prTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PR ID", "Item Name", "Quantity", "Price", "Total Price", "Required Date", "Supplier ID ", "Delivery Date", "Status", "Actions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        prTable.setEditingColumn(4);
        prTable.setRowHeight(40);
        jScrollPane1.setViewportView(prTable);

        PurchaseNewItemButton.setBackground(new java.awt.Color(255, 255, 204));
        PurchaseNewItemButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PurchaseNewItemButton.setText("Purchase New Items");
        PurchaseNewItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurchaseNewItemButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(RestockExistingItemButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PurchaseNewItemButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(PRPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(PurchaseNewItemButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RestockExistingItemButton)
                    .addComponent(date)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void RestockExistingItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestockExistingItemButtonActionPerformed
        String salesManagerID = getCurrentSalesManagerID();
    
        PRExistingItem addPRDialog = new PRExistingItem(salesManagerID);
            addPRDialog.setVisible(true); 
            // Add listener to refresh table when AddPR dialog is closed
        addPRDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadPurchaseRequisition(); // Refresh table after adding new PR
            }
        });
    }//GEN-LAST:event_RestockExistingItemButtonActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void PurchaseNewItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurchaseNewItemButtonActionPerformed
        String salesManagerID = getCurrentSalesManagerID();
    
        PRNewItem addPRDialog = new PRNewItem(salesManagerID);
            addPRDialog.setVisible(true);
            
        addPRDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadPurchaseRequisition(); // Refresh table after adding new PR
            }
        });
    }//GEN-LAST:event_PurchaseNewItemButtonActionPerformed
    
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
//            java.util.logging.Logger.getLogger(PROperations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PROperations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PROperations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PROperations.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PROperations().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PRPanel;
    private javax.swing.JLabel PRTitle;
    private javax.swing.JButton PurchaseNewItemButton;
    private javax.swing.JButton RestockExistingItemButton;
    private javax.swing.JLabel date;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable prTable;
    // End of variables declaration//GEN-END:variables
}
