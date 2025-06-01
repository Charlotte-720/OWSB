package PurchaseManager;

import PurchaseManager.Function.updatedata;
import PurchaseManager.viewitem;
import PurchaseManager.viewsuppliers;
import PurchaseManager.viewrequisition;
import PurchaseManager.generateandviewpo;
import Admin.Loginpage1;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;



public class PM extends javax.swing.JFrame {  
    private String employeeID;
    private String position;
    public PM(String identifier) {
        String[] parts = identifier.split(":");
        if (parts.length == 2) {
            this.employeeID = parts[0];
            this.position = parts[1];
        } else {
            this.employeeID = "Unknown";
            this.position = "Unknown";
            System.out.println("Error: LoggedInIdentifier has an unexpected format: [" + identifier + "]");
        }
        System.out.println("EmployeeID: " + employeeID);
        System.out.println("Position: " + position);
        
        initComponents();
        // Update the label to show the actual user ID
        jLabel1.setText("Purchase Manager, " + this.employeeID);
        this.setLocationRelativeTo(null);
        
        updatedata.loadstatusPOData(jTable1);
        readAndCountStatuses(); // Add this call here
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        home1 = new javax.swing.JLabel();
        wlc = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        PO = new javax.swing.JLabel();
        Reject = new javax.swing.JLabel();
        Approve = new javax.swing.JLabel();
        Pending = new javax.swing.JLabel();
        Paid = new javax.swing.JLabel();
        Received = new javax.swing.JLabel();
        Verified = new javax.swing.JLabel();
        Flagged = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(40, 44, 52));

        logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        logout.setForeground(new java.awt.Color(220, 220, 220));
        logout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logout.setText("Logout");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });

        home1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        home1.setForeground(new java.awt.Color(220, 220, 220));
        home1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        home1.setText("Dashboard");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(home1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(home1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 432, Short.MAX_VALUE)
                .addComponent(logout)
                .addGap(46, 46, 46))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 600));

        wlc.setBackground(new java.awt.Color(245, 245, 245));
        wlc.setPreferredSize(new java.awt.Dimension(800, 600));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel4.setText("WELCOME BACK!");

        jLabel1.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel1.setText("Purchase Manager");

        jPanel2.setBackground(new java.awt.Color(50, 50, 60));

        jScrollPane1.setBackground(new java.awt.Color(0, 0, 0));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "PO_ ID", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setIntercellSpacing(new java.awt.Dimension(10, 10));
        jTable1.setRowHeight(25);
        jScrollPane1.setViewportView(jTable1);

        PO.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        PO.setForeground(new java.awt.Color(220, 220, 220));
        PO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        PO.setText("Purchase Order");

        Reject.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Reject.setForeground(new java.awt.Color(220, 220, 220));
        Reject.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Reject.setText("Rejected     :");

        Approve.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Approve.setForeground(new java.awt.Color(220, 220, 220));
        Approve.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Approve.setText("Approved  :");

        Pending.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Pending.setForeground(new java.awt.Color(220, 220, 220));
        Pending.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Pending.setText("Pending     :");

        Paid.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Paid.setForeground(new java.awt.Color(220, 220, 220));
        Paid.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Paid.setText("Paid            :");

        Received.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Received.setForeground(new java.awt.Color(220, 220, 220));
        Received.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Received.setText("Received     :");

        Verified.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Verified.setForeground(new java.awt.Color(220, 220, 220));
        Verified.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Verified.setText("Verified      :");

        Flagged.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        Flagged.setForeground(new java.awt.Color(220, 220, 220));
        Flagged.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Flagged.setText("Flagged      :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Reject)
                    .addComponent(Approve)
                    .addComponent(Pending)
                    .addComponent(Paid)
                    .addComponent(Received)
                    .addComponent(Flagged)
                    .addComponent(Verified))
                .addContainerGap(105, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PO)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(Reject)
                        .addGap(18, 18, 18)
                        .addComponent(Approve)
                        .addGap(18, 18, 18)
                        .addComponent(Pending)
                        .addGap(18, 18, 18)
                        .addComponent(Paid)
                        .addGap(18, 18, 18)
                        .addComponent(Received)
                        .addGap(18, 18, 18)
                        .addComponent(Verified)
                        .addGap(18, 18, 18)
                        .addComponent(Flagged))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout wlcLayout = new javax.swing.GroupLayout(wlc);
        wlc.setLayout(wlcLayout);
        wlcLayout.setHorizontalGroup(
            wlcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wlcLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(wlcLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(wlcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wlcLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1))
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        wlcLayout.setVerticalGroup(
            wlcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wlcLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        getContentPane().add(wlc, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 550, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // go to other panel
    private void ViewSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewSuppliersActionPerformed
        viewsuppliers Viewsuppliers = new viewsuppliers();
        Viewsuppliers.setVisible(true);
    }//GEN-LAST:event_ViewSuppliersActionPerformed

    private void ViewRequisitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewRequisitionActionPerformed
        viewrequisition view = new viewrequisition(this);
        view.setVisible(true);
    }//GEN-LAST:event_ViewRequisitionActionPerformed

    private void GenerateViewPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateViewPOActionPerformed
        JFrame frame = new JFrame("Generate and View PO");
        frame.setContentPane(new generateandviewpo());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_GenerateViewPOActionPerformed

    private void logoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseClicked
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

    }//GEN-LAST:event_logoutMouseClicked

    private void ViewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewItemActionPerformed
        viewitem Viewitem = new viewitem();
        Viewitem.setVisible(true);
    }//GEN-LAST:event_ViewItemActionPerformed

    private void moredetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moredetailActionPerformed
        JFrame frame = new JFrame("Generate and View PO");
        frame.setContentPane(new generateandviewpo());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_moredetailActionPerformed

    //show the number of how many reject, pendinf ...
    private void readAndCountStatuses() {
    int countReject = 0;
    int countApprove = 0;
    int countPaid = 0;
    int countPending = 0;
    int countReceived = 0;
    int countVerified = 0;
    int countFlagged  = 0;
    
    try {
        // Read all lines from po.txt
        List<String> lines = Files.readAllLines(Paths.get("src/txtFile/po.txt"));

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length < 2) continue;

            // Get last part which contains "Status: ..."
            String lastPart = parts[parts.length - 1].trim();

            if (lastPart.toLowerCase().startsWith("status:")) {
                String status = lastPart.substring(7).trim(); // get text after "Status:"

                // Count status occurrences (handle singular and past tense)
                switch (status.toLowerCase()) {
                    case "reject":
                    case "rejected":
                        countReject++;
                        break;
                    case "approve":
                    case "approved":
                        countApprove++;
                        break;
                    case "paid":
                        countPaid++;
                        break;
                    case "pending":
                        countPending++;
                        break;
                    case "receive":
                        countReceived++;
                        break;
                    case "verified":
                        countVerified++;               
                        break;
                    case "flagged":
                        countFlagged++;
                        break;
                }
            }
        }

        // Update the labels with counts
        Reject.setText("Reject        : " + countReject);
        Approve.setText("Approve   : " + countApprove);
        Paid.setText("Paid           : " + countPaid);
        Pending.setText("Pending    : " + countPending);
        Received.setText("Received    : " + countReceived);
        Verified.setText("Virified       :" + countVerified);
        Flagged.setText("Flagged      :" + countFlagged);
        
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Approve;
    private javax.swing.JLabel Flagged;
    private javax.swing.JLabel PO;
    private javax.swing.JLabel Paid;
    private javax.swing.JLabel Pending;
    private javax.swing.JLabel Received;
    private javax.swing.JLabel Reject;
    private javax.swing.JLabel Verified;
    private javax.swing.JLabel home1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel logout;
    private javax.swing.JPanel wlc;
    // End of variables declaration//GEN-END:variables
}
