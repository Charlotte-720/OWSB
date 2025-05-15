package PurchaseManager;

import Admin.Loginpage1;
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
        this.setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        logout = new javax.swing.JLabel();
        home = new javax.swing.JLabel();
        ViewItem = new PurchaseManager.button();
        ViewSuppliers = new PurchaseManager.button();
        ViewRequisition = new PurchaseManager.button();
        GenerateViewPO = new PurchaseManager.button();
        wlc = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cancel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(800, 600));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(153, 0, 153));

        logout.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        logout.setForeground(new java.awt.Color(255, 255, 255));
        logout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logout.setText("Logout");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutMouseClicked(evt);
            }
        });

        home.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        home.setForeground(new java.awt.Color(255, 255, 255));
        home.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        home.setText("Dashboard");

        ViewItem.setBackground(new java.awt.Color(204, 0, 204));
        ViewItem.setForeground(new java.awt.Color(255, 255, 255));
        ViewItem.setText("View Item");
        ViewItem.setRound(20);
        ViewItem.setShadowColor(new java.awt.Color(0, 0, 0));
        ViewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewItemActionPerformed(evt);
            }
        });

        ViewSuppliers.setBackground(new java.awt.Color(204, 0, 204));
        ViewSuppliers.setForeground(new java.awt.Color(255, 255, 255));
        ViewSuppliers.setText("View Suppliers");
        ViewSuppliers.setRound(20);
        ViewSuppliers.setShadowColor(new java.awt.Color(0, 0, 0));
        ViewSuppliers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewSuppliersActionPerformed(evt);
            }
        });

        ViewRequisition.setBackground(new java.awt.Color(204, 0, 204));
        ViewRequisition.setForeground(new java.awt.Color(255, 255, 255));
        ViewRequisition.setText("View Requisition");
        ViewRequisition.setRound(20);
        ViewRequisition.setShadowColor(new java.awt.Color(0, 0, 0));
        ViewRequisition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewRequisitionActionPerformed(evt);
            }
        });

        GenerateViewPO.setBackground(new java.awt.Color(204, 0, 204));
        GenerateViewPO.setForeground(new java.awt.Color(255, 255, 255));
        GenerateViewPO.setText("Generat PO and View");
        GenerateViewPO.setRound(20);
        GenerateViewPO.setShadowColor(new java.awt.Color(0, 0, 0));
        GenerateViewPO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GenerateViewPOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ViewItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(home, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                    .addComponent(ViewSuppliers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ViewRequisition, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(GenerateViewPO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(home)
                .addGap(89, 89, 89)
                .addComponent(ViewItem, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ViewSuppliers, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ViewRequisition, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(GenerateViewPO, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(logout)
                .addGap(36, 36, 36))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 600));

        wlc.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setText("Welcome Back Name");

        cancel.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        cancel.setForeground(new java.awt.Color(255, 0, 0));
        cancel.setText("X");
        cancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout wlcLayout = new javax.swing.GroupLayout(wlc);
        wlc.setLayout(wlcLayout);
        wlcLayout.setHorizontalGroup(
            wlcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wlcLayout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 397, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(cancel)
                .addGap(20, 20, 20))
        );
        wlcLayout.setVerticalGroup(
            wlcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(wlcLayout.createSequentialGroup()
                .addGroup(wlcLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(wlcLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(wlcLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(498, Short.MAX_VALUE))
        );

        getContentPane().add(wlc, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 550, 600));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelMouseClicked
        this.dispose();
    }//GEN-LAST:event_cancelMouseClicked

    
    // homepage to other panel
    private void ViewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backActionPerformed
        JFrame frame = new JFrame("View Item");
        frame.setContentPane(new viewitem());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_backActionPerformed

    private void ViewSuppliersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewSuppliersActionPerformed
        JFrame frame = new JFrame("View Suppliers");
        frame.setContentPane(new viewsuppliers());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ViewSuppliersActionPerformed

    private void ViewRequisitionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewRequisitionActionPerformed
        JFrame frame = new JFrame("View Requisition");
        frame.setContentPane(new viewrequisition());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ViewRequisitionActionPerformed

    private void GenerateViewPOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GenerateViewPOActionPerformed
        JFrame frame = new JFrame("Generate and View PO");
        frame.setContentPane(new generatepo());
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

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            //new PM().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private PurchaseManager.button GenerateViewPO;
    private PurchaseManager.button ViewItem;
    private PurchaseManager.button ViewRequisition;
    private PurchaseManager.button ViewSuppliers;
    private javax.swing.JLabel cancel;
    private javax.swing.JLabel home;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel logout;
    private javax.swing.JPanel wlc;
    // End of variables declaration//GEN-END:variables
}
