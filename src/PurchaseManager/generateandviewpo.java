package PurchaseManager;



import PurchaseManager.Function.deletefc;
import PurchaseManager.addPO;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.Component;

public class generateandviewpo extends javax.swing.JPanel {
    public generateandviewpo() {
        initComponents();
        loadPOData(); // Load data when the window is opened
        jTable1.getColumnModel().getColumn(9).setCellRenderer(new StatusColumnCellRenderer());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cancel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        back1 = new javax.swing.JButton();
        delete = new javax.swing.JButton();
        edit = new javax.swing.JButton();
        add = new javax.swing.JButton();

        setBackground(new java.awt.Color(225, 230, 245));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

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
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "Item ID", "Suppliers ID", "Suppliers N", "Item", "Quantity", "Unit Price", "Total Price", "RD Date", "Status", "Flag Reason"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, false, true, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setIntercellSpacing(new java.awt.Dimension(10, 10));
        jTable1.setRowHeight(25);
        jScrollPane1.setViewportView(jTable1);

        back1.setBackground(new java.awt.Color(85, 85, 110));
        back1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        back1.setForeground(new java.awt.Color(220, 220, 220));
        back1.setText("Back");
        back1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                back1ActionPerformed(evt);
            }
        });

        delete.setBackground(new java.awt.Color(85, 85, 110));
        delete.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        delete.setForeground(new java.awt.Color(220, 220, 220));
        delete.setText("Delete");
        delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteActionPerformed(evt);
            }
        });

        edit.setBackground(new java.awt.Color(85, 85, 110));
        edit.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        edit.setForeground(new java.awt.Color(220, 220, 220));
        edit.setText("Edit");
        edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editActionPerformed(evt);
            }
        });

        add.setBackground(new java.awt.Color(85, 85, 110));
        add.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        add.setForeground(new java.awt.Color(220, 220, 220));
        add.setText("Add");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(cancel))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 38, Short.MAX_VALUE)
                        .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(back1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(29, 29, 29)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(back1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(delete, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edit, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(add, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
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

    private void back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_back1ActionPerformed
        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        currentFrame.dispose();
        PM pmFrame = new PM("exampleFinanceManager"); 
        pmFrame.setVisible(true);
        pmFrame.setLocationRelativeTo(null);
    }//GEN-LAST:event_back1ActionPerformed

    private void deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteActionPerformed
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            String poIdToDelete = (String) jTable1.getValueAt(selectedRow, 0);
            String status = (String) jTable1.getValueAt(selectedRow, 9);

            if (!status.equalsIgnoreCase("Pending")) {
                JOptionPane.showMessageDialog(this, "Cannot delete PO, only Pending can be delete!");
                return; // Stop here to avoid calling deletePO
            }

            deletefc deletePO = new deletefc();
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

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        addPO addPO = new addPO(this); // Opens the Add Purchase Order JFrame
    }//GEN-LAST:event_addActionPerformed

   
    
    // read the txt file and show it (read function)
    public void loadPOData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data

        File poFile = new File("src/txtFile/po.txt");
        File supplierFile = new File("src/txtFile/suppliers.txt");

        // Step 1: Build supplier ID -> Name map
        Map<String, String> supplierNameMap = new HashMap<>();
        if (supplierFile.exists()) {
            try (Scanner supplierScanner = new Scanner(supplierFile)) {
                while (supplierScanner.hasNextLine()) {
                    String line = supplierScanner.nextLine();
                    String[] parts = line.split(",");

                    String supplierID = "";
                    String supplierName = "";

                    for (String part : parts) {
                        part = part.trim();
                        if (part.startsWith("Supplier ID:")) {
                            supplierID = part.split(":", 2)[1].trim();
                        } else if (part.startsWith("Supplier Name:")) {
                            supplierName = part.split(":", 2)[1].trim();
                        }
                    }

                    if (!supplierID.isEmpty() && !supplierName.isEmpty()) {
                        supplierNameMap.put(supplierID, supplierName);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Optional: Load flagged reasons
        Map<String, String> flaggedReasons = readFlaggedReasons();

        // Step 2: Read PO file
        if (poFile.exists()) {
            try (Scanner scanner = new Scanner(poFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(", ");

                    String poID = "", itemID = "", supplierID = "", itemName = "";
                    String quantity = "", unitPrice = "", totalPrice = "";
                    String date = "", status = "";

                    for (String field : fields) {
                        String[] parts = field.split(": ", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();

                            switch (key) {
                                case "PO_ID": poID = value; break;
                                case "Item ID": itemID = value; break;
                                case "Supplier ID": supplierID = value; break;
                                case "Item Name": itemName = value; break;
                                case "Quantity": quantity = value; break;
                                case "Unit Price": unitPrice = value; break;
                                case "Total Price": totalPrice = value; break;
                                case "Date": date = value; break;
                                case "Status": status = value; break;
                            }
                        }
                    }

                    String supplierName = supplierNameMap.getOrDefault(supplierID, "Unknown");
                    String flagReason = flaggedReasons.getOrDefault(poID, "-");

                    model.addRow(new Object[]{
                        poID, itemID, supplierID, supplierName, itemName,
                        quantity, unitPrice, totalPrice, date, status, flagReason
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File not found: " + poFile.getAbsolutePath());
        }
    }

    public Map<String, String> readFlaggedReasons() {
        Map<String, String> flaggedMap = new HashMap<>();
        File reasonFile = new File("src/txtFile/flagReason.txt");

        if (reasonFile.exists()) {
            try (Scanner scanner = new Scanner(reasonFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] parts = line.split(", ");
                    if (parts.length == 2) {
                        String poID = parts[0].split(":")[1].trim();
                        String reason = parts[1].split(":")[1].trim();
                        flaggedMap.put(poID, reason);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return flaggedMap;
    }

    class StatusColumnCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
               boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString().toLowerCase();

                switch (status) {
                    case "rejected":
                        c.setForeground(Color.RED);
                        break;
                    case "pending":
                        c.setForeground(new Color(255, 165, 0));
                        break;
                    case "approved":
                        c.setForeground(new Color(0, 128, 0));
                        break;
                    case "received":
                        c.setForeground(new Color(0,102,255));
                        break;
                    case "verified":
                        c.setForeground(new Color(255,0,255));
                        break;
                    case "flagged":
                        c.setForeground(new Color(0,153,153));
                        break;
                    default:
                        c.setForeground(Color.BLACK);
                        break;
                }
            } else {
                c.setForeground(Color.BLACK);
            }

            return c;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add;
    private javax.swing.JButton back1;
    private javax.swing.JButton back3;
    private javax.swing.JButton back4;
    private javax.swing.JLabel cancel;
    private javax.swing.JButton delete;
    private javax.swing.JButton edit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
