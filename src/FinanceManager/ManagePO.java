/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FinanceManager;

import FinanceManager.StatusFormat.StatusCellRenderer;
import java.awt.Color;
import model.PurchaseOrder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author charl
 */
public class ManagePO extends javax.swing.JFrame {

    /**
     * Creates new form ManagerPO
     */
    public ManagePO() {
        initComponents();
        loadPOData();
        poTable.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());
        TableStyle.styleTableHeader(poTable, new Color(166, 214, 214), Color.BLACK);
        
        comboFilter.addItem("All Statuses");
        comboFilter.addItem("Pending");
        comboFilter.addItem("Approved");
        comboFilter.addItem("Rejected");
        comboFilter.addItem("Paid");

        comboFilter.addActionListener(e -> {
            String selectedStatus = (String) comboFilter.getSelectedItem();
            filterPOByStatus(selectedStatus);
        });

    }
    
    public ArrayList<PurchaseOrder> readPOFile() {
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/po.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                String poID = fields[0].split(": ")[1];
                String supplierName = fields[1].split(": ")[1];
                String item = fields[2].split(": ")[1];
                String quantity = fields[3].split(": ")[1];
                String unitPrice = fields[4].split(": ")[1];    
                String totalPrice = fields[5].split(": ")[1];
                String date = fields[6].split(": ")[1];
                String status = fields[7].split(": ")[1];

                PurchaseOrder po = new PurchaseOrder(poID, supplierName, item, quantity, unitPrice, totalPrice, date, status);
                poList.add(po);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return poList;
    }
    
    public void loadPOData() {
        DefaultTableModel model = (DefaultTableModel) poTable.getModel(); 
        model.setRowCount(0); 

        ArrayList<PurchaseOrder> poList = readPOFile();
        for (PurchaseOrder po : poList) {
            model.addRow(new Object[] {
                po.getPoID(),
                po.getSupplierName(),
                po.getItem(),
                po.getQuantity(),
                po.getUnitPrice(),
                po.getTotalPrice(),
                po.getDate(),
                po.getStatus()
            });
        }
    }
    
    private void saveTableToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/txtFile/po.txt"))) {
            for (int i = 0; i < poTable.getRowCount(); i++) {
                String poID = poTable.getValueAt(i, 0).toString();
                String supplierName = poTable.getValueAt(i, 1).toString();
                String item = poTable.getValueAt(i, 2).toString();
                String quantity = poTable.getValueAt(i, 3).toString();
                String unitPrice = poTable.getValueAt(i, 4).toString();
                String totalPrice = poTable.getValueAt(i, 5).toString();
                String date = poTable.getValueAt(i, 6).toString();
                String status = poTable.getValueAt(i, 7).toString();

                String line = "PO_ID: " + poID + ", Supplier Name: " + supplierName + ", Item: " + item +
                              ", Quantity: " + quantity + ", Unit Price: " + unitPrice + ", Total Price: " + totalPrice + ", Date: " + date + ", Status: " + status;

                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving changes to file!");
        }
    }

    public String[] loadSupplierNames() {
        ArrayList<String> supplierNames = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("suppliers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 4 && fields[3].equalsIgnoreCase("true")) {
                    supplierNames.add(fields[1]); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supplierNames.toArray(new String[0]);
    }

    private void filterPOByStatus(String statusFilter) {
        DefaultTableModel model = (DefaultTableModel) poTable.getModel();
        model.setRowCount(0); // clear existing rows

        ArrayList<PurchaseOrder> poList = readPOFile();

        for (PurchaseOrder po : poList) {
            if (statusFilter.equals("All Statuses") || po.getStatus().equalsIgnoreCase(statusFilter)) {
                model.addRow(new Object[] {
                    po.getPoID(),
                    po.getSupplierName(),
                    po.getItem(),
                    po.getQuantity(),
                    po.getUnitPrice(),
                    po.getTotalPrice(),
                    po.getDate(),
                    po.getStatus()
                });
            }
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnReject = new javax.swing.JButton();
        labelTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        poTable = new javax.swing.JTable();
        btnApprove = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        comboFilter = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 253, 236));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        btnReject.setBackground(new java.awt.Color(255, 207, 207));
        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });

        labelTitle.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        labelTitle.setIcon(new javax.swing.ImageIcon("C:\\Users\\charl\\OneDrive\\Documents\\NetBeansProjects\\OWSB\\src\\Icons\\Icon - checklist.png")); // NOI18N
        labelTitle.setText("Purchase Order List");

        poTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO_ID", "Supplier Name", "Item", "Quantity", "Unit Price", "Total Price", "Date", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        poTable.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        jScrollPane1.setViewportView(poTable);

        btnApprove.setBackground(new java.awt.Color(134, 167, 136));
        btnApprove.setForeground(new java.awt.Color(255, 255, 255));
        btnApprove.setText("Approve");
        btnApprove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApproveActionPerformed(evt);
            }
        });

        btnEdit.setBackground(new java.awt.Color(255, 226, 226));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnBack.setBackground(new java.awt.Color(255, 255, 204));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        comboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(179, 179, 179)
                                .addComponent(btnApprove)
                                .addGap(45, 45, 45)
                                .addComponent(btnReject)
                                .addGap(44, 44, 44)
                                .addComponent(btnEdit)
                                .addGap(53, 53, 53)
                                .addComponent(btnBack))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(215, 215, 215)
                                .addComponent(labelTitle)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(comboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 738, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTitle)
                    .addComponent(comboFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApprove)
                    .addComponent(btnReject)
                    .addComponent(btnEdit)
                    .addComponent(btnBack))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(801, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int row = poTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row.");
            return;
        }
        
        String currentStatus = poTable.getValueAt(row, 7).toString();
        if (currentStatus.equalsIgnoreCase("Approved") || currentStatus.equalsIgnoreCase("Rejected") || currentStatus.equalsIgnoreCase("Paid")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order has been " + currentStatus + ", so you cannot edit it.");
            return;
        }

        String item = poTable.getValueAt(row, 2).toString();
        String quantity = poTable.getValueAt(row, 3).toString();
        String unitPrice = poTable.getValueAt(row, 4).toString();
        String supplier = poTable.getValueAt(row, 1).toString(); 
        String[] suppliers = loadSupplierNames();

        EditPOForm dialog = new EditPOForm(this, true); 
        dialog.setPOData(item, quantity, unitPrice, supplier, suppliers);
        dialog.setLocationRelativeTo(this); 
        dialog.setVisible(true); 

        if (dialog.isConfirmed()) {
            poTable.setValueAt(dialog.getSelectedSupplier(), row, 1);
            poTable.setValueAt(dialog.getUpdatedQuantity(), row, 3);
            poTable.setValueAt(dialog.getUpdatedTotalPrice(), row, 5);
            saveTableToFile();
        }

    }//GEN-LAST:event_btnEditActionPerformed

    private void btnApproveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApproveActionPerformed
        // TODO add your handling code here:
        int row = poTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to approve.");
            return;
        }
        
        String currentStatus = poTable.getValueAt(row, 7).toString(); 
        if (currentStatus.equalsIgnoreCase("Approved")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order is already approved.");
            return;
        } else if (currentStatus.equalsIgnoreCase("Rejected")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order has been rejected. Cannot approve.");
            return;
        } else if (currentStatus.equalsIgnoreCase("Paid")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order has been paid. Cannot approve.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to approve this Purchase Order?", 
        "Confirm Approve", 
        JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            poTable.setValueAt("Approved", row, 7);
            saveTableToFile();
        }
    
    }//GEN-LAST:event_btnApproveActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        int row = poTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to reject.");
            return;
        }
        
        String currentStatus = poTable.getValueAt(row, 7).toString();
        if (currentStatus.equalsIgnoreCase("Rejected")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order is already rejected.");
            return;
        } else if (currentStatus.equalsIgnoreCase("Approved")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order has been approved. Cannot reject.");
            return;
        } else if (currentStatus.equalsIgnoreCase("Paid")) {
            JOptionPane.showMessageDialog(this, "This Purchase Order has been paid. Cannot reject.");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to reject this Purchase Order?", 
        "Confirm Reject", 
        JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            poTable.setValueAt("Rejected", row, 7);
            saveTableToFile();
        }
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
         this.dispose();
         FinanceManagerPanel fmg = new FinanceManagerPanel("exampleFinanceManager");
         fmg.setVisible(true);
         
    }//GEN-LAST:event_btnBackActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // TODO add your handling code here:
        this.dispose();
        FinanceManagerPanel fmg = new FinanceManagerPanel("exampleFinanceManager");
        fmg.setVisible(true);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void comboFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboFilterActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApprove;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnReject;
    private javax.swing.JComboBox<String> comboFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JTable poTable;
    // End of variables declaration//GEN-END:variables
}
