package PurchaseManager;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class generateandviewpo extends javax.swing.JPanel {
    public generateandviewpo() {
        initComponents();
        loadPOData(); // Load data when the window is opened

        JTableHeader header = jTable1.getTableHeader();
        header.setBackground(new java.awt.Color(255,255,255)); // Example: Steel Blue
        header.setForeground(Color.BLACK); // Text color
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        edit = new PurchaseManager.button();
        delete = new PurchaseManager.button();
        add = new PurchaseManager.button();
        back = new PurchaseManager.button();

        setBackground(new java.awt.Color(255, 255, 255));

        cancel.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        cancel.setForeground(new java.awt.Color(255, 0, 0));
        cancel.setText("X");
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Generate PO");

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO_ ID", "Suppliers Nm ", "Item", "Quantity", "Unit Price", "Total Price", "Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setIntercellSpacing(new java.awt.Dimension(10, 10));
        jTable1.setRowHeight(25);
        jScrollPane1.setViewportView(jTable1);

        edit.setBackground(new java.awt.Color(204, 0, 204));
        edit.setForeground(new java.awt.Color(255, 255, 255));
        edit.setText("Edit");
        edit.setRound(20);
        edit.setShadowColor(new java.awt.Color(0, 0, 0));
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(204, 0, 204));
        delete.setForeground(new java.awt.Color(255, 255, 255));
        delete.setText("Delete");
        delete.setRound(20);
        delete.setShadowColor(new java.awt.Color(0, 0, 0));
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        add.setBackground(new java.awt.Color(204, 0, 204));
        add.setForeground(new java.awt.Color(255, 255, 255));
        add.setText("Add");
        add.setRound(20);
        add.setShadowColor(new java.awt.Color(0, 0, 0));
        add.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addMouseClicked(evt);
            }
        });

        back.setBackground(new java.awt.Color(204, 0, 204));
        back.setForeground(new java.awt.Color(255, 255, 255));
        back.setText("Back");
        back.setRound(20);
        back.setShadowColor(new java.awt.Color(0, 0, 0));
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(cancel)
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(464, 464, 464))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    //back to homepage
    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
    JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    currentFrame.dispose(); 
    PM pmFrame = new PM("exampleFinanceManager"); 
    pmFrame.setVisible(true);
    pmFrame.setLocationRelativeTo(null); 
    }//GEN-LAST:event_cancelMouseClicked

    // go to add panel
    private void addMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addMouseClicked
        JFrame frame = new JFrame("Add Purchase Order");
        frame.setContentPane(new addPO(this));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//GEN-LAST:event_addMouseClicked

    //edit funcation 
    private void editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editActionPerformed
        int selectedRow = jTable1.getSelectedRow();
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
                return;
            }

            // Assume first column is PO_ID
            String poId = jTable1.getValueAt(selectedRow, 0).toString();
            String filePath = "src/txtFile/po.txt";
            
            boolean success = editPO.editPO(parentFrame, poId, filePath);

            if (success) {
                loadPOData(); // refresh the table
            }
    }//GEN-LAST:event_editActionPerformed

    // delete funcation
    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            String poIdToDelete = (String) jTable1.getValueAt(selectedRow, 0);
            String status = (String) jTable1.getValueAt(selectedRow, 7);

            if (!status.equalsIgnoreCase("Pending")) {
                JOptionPane.showMessageDialog(this, "Cannot delete PO, only Pending can be delete!");
                return; // Stop here to avoid calling deletePO
            }

            deletePO deletePO = new deletePO();
            String filePath = new File("src/txtFile/po.txt").getAbsolutePath();

            boolean success = deletePO.deletePO(jTable1, poIdToDelete, filePath);

            if (success) {
                JOptionPane.showMessageDialog(this, "Data deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error while deleting data or PO_ID not found.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }//GEN-LAST:event_deleteActionPerformed

    // back function
    private void backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.dispose();
        PM pmFrame = new PM("exampleFinanceManager");
        pmFrame.setVisible(true);
        pmFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_backActionPerformed
    
    
    // read the txt file and show it (read function)
    public void loadPOData() {
       DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data

        File file = new File("src/txtFile/po.txt");

        if (file.exists()) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] details = line.split(",");

                    // Create a new array to store only the values after ':'
                    String[] rowData = new String[details.length];

                    for (int i = 0; i < details.length; i++) {
                        String[] parts = details[i].split(":");
                        if (parts.length > 1) {
                            rowData[i] = parts[1].trim(); // Take the part after ':'
                        } else {
                            rowData[i] = ""; // If no colon, leave empty
                        }
                    }

                    model.addRow(rowData);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found at: " + file.getAbsolutePath());
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private PurchaseManager.button add;
    private PurchaseManager.button back;
    private javax.swing.JLabel cancel;
    private PurchaseManager.button delete;
    private PurchaseManager.button edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
