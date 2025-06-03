
package SalesManager;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class ViewPO extends javax.swing.JFrame {
    private Component previousComponent;
    
    public ViewPO(Component previousComponent) {
        this.previousComponent = previousComponent;
        initComponents();
        loadPOData(); 
        poTable.getColumnModel().getColumn(9).setCellRenderer(new StatusColumnCellRenderer());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        poTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        POLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(null);

        jPanel1.setBackground(new java.awt.Color(230, 252, 252));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        poTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PO ID", "Item ID", "Suppliers ID", "Suppliers Name", "Item Name", "Quantity", "Unit Price", "Total Price", "Date", "Status", "Flag Reason"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        poTable.setEditingColumn(4);
        poTable.setRowHeight(40);
        jScrollPane1.setViewportView(poTable);

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        POLabel.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        POLabel.setText("View Purchase Oders");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 839, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(POLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(253, 253, 253))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(11, 11, 11)
                .addComponent(POLabel)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
        if (previousComponent != null) {
            previousComponent.setVisible(true);
        }
    }//GEN-LAST:event_jLabel1MouseClicked

    public void loadPOData() {
        DefaultTableModel model = (DefaultTableModel) poTable.getModel();
        model.setRowCount(0); // Clear existing data

        File poFile = new File("src/txtFile/po.txt");
        File supplierFile = new File("src/txtFile/suppliers.txt");
        
        // Step 1: Build supplier ID -> Name map from suppliers.txt
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
        
        // Load flagged reasons
        Map<String, String> flaggedReasons = readFlaggedReasons();

        // Step 2: Read PO file
        if (poFile.exists()) {
            try (Scanner scanner = new Scanner(poFile)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] fields = line.split(", ");

                    String poID = "", itemID = "", supplierID = "", supplierName = "", itemName = "";
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
                                case "Supplier Name": supplierName = value; break;  // Direct from po.txt
                                case "Item Name": itemName = value; break;
                                case "Quantity": quantity = value; break;
                                case "Unit Price": unitPrice = value; break;
                                case "Total Price": totalPrice = value; break;
                                case "Date": date = value; break;
                                case "Status": status = value; break;
                            }
                        }
                    }

                    // If supplier name not found in po.txt, look it up from suppliers.txt
                    if (supplierName.isEmpty() && !supplierID.isEmpty()) {
                        supplierName = supplierNameMap.getOrDefault(supplierID, "Unknown");
                    }

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

    // Status column renderer with colors (copied from generateandviewpo)
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
                        c.setForeground(new Color(255, 165, 0)); // Orange
                        break;
                    case "approved":
                        c.setForeground(new Color(0, 128, 0)); // Green
                        break;
                    case "received":
                        c.setForeground(new Color(0,102,255)); // Blue
                        break;
                    case "verified":
                        c.setForeground(new Color(255,0,255)); // Magenta
                        break;
                    case "flagged":
                        c.setForeground(new Color(0,153,153)); // Teal
                        break;
                    case "paid":
                        c.setForeground(new Color(0, 100, 0)); // Dark Green
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
    private javax.swing.JLabel POLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable poTable;
    // End of variables declaration//GEN-END:variables
    }
