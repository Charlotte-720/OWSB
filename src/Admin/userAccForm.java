/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class userAccForm extends javax.swing.JFrame {

    
    private String employeeID;
    private String position;
    
    public userAccForm(String identifier) {
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
        loadUserData();
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        btnUnlocked = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUserData = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblUpdate = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        mainPanel.setBackground(new java.awt.Color(231, 240, 220));
        mainPanel.setForeground(new java.awt.Color(255, 255, 255));
        mainPanel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        headerPanel.setBackground(new java.awt.Color(89, 116, 69));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnUnlocked.setBackground(new java.awt.Color(32, 52, 15));
        btnUnlocked.setFont(new java.awt.Font("Bookman Old Style", 1, 22)); // NOI18N
        btnUnlocked.setForeground(new java.awt.Color(231, 240, 220));
        btnUnlocked.setText("Unlocked");
        btnUnlocked.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(0, 0, 0)));
        btnUnlocked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnlockedActionPerformed(evt);
            }
        });
        headerPanel.add(btnUnlocked, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 100, 120, 40));

        btnMenu.setBackground(new java.awt.Color(255, 204, 102));
        btnMenu.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        btnMenu.setForeground(new java.awt.Color(32, 52, 15));
        btnMenu.setText("Menu");
        btnMenu.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(0, 0, 0)));
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });
        headerPanel.add(btnMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 160, 44));

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\ELYSHA SOPHIA\\Downloads\\icons8-unlock-30.png")); // NOI18N
        headerPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 30, 40));

        jLabel3.setFont(new java.awt.Font("Bookman Old Style", 1, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(32, 52, 15));
        jLabel3.setText("Employee's Account Status Page!");

        tblUserData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EmployeeID", "Username", "Password", "Fullname", "Phone No.", "Gender", "Position", "Department", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblUserData);

        btnRefresh.setBackground(new java.awt.Color(32, 52, 15));
        btnRefresh.setFont(new java.awt.Font("Bookman Old Style", 1, 16)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(231, 240, 220));
        btnRefresh.setText("Refresh");
        btnRefresh.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(0, 0, 0)));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 1, 30)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(32, 52, 15));
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        lblUpdate.setIcon(new javax.swing.ImageIcon("C:\\Users\\ELYSHA SOPHIA\\Downloads\\icons8-update-30.png")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 817, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                                .addComponent(jLabel1)))
                        .addGap(19, 19, 19))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUpdate)))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(27, 27, 27)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1021, 636));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        // Dispose the userAccForm frame
        this.dispose();

        // Open AdminTest frame
        AdminPage adminPage = new AdminPage("exampleIdentifier:Position"); // Pass identifier to AdminPage
        adminPage.setVisible(true);
        adminPage.pack();
        adminPage.setLocationRelativeTo(null);
        adminPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }//GEN-LAST:event_jLabel1MouseClicked

    // Method to load user data from text file and populate the JTable
        private void loadUserData() {
            String credentialsFilePath = "user_credentials.txt";
            String employeeDataFilePath = "Employee_data.txt";

            try (BufferedReader credentialsReader = new BufferedReader(new FileReader(credentialsFilePath))) {
                String line;
                DefaultTableModel model = (DefaultTableModel) tblUserData.getModel();

                // Ensure table columns are set
                String[] columnNames = {"Employee ID", "Username", "Password", "Fullname", "Phone No.", "Gender", "Position", "Department", "Status"};
                model.setColumnIdentifiers(columnNames);

                while ((line = credentialsReader.readLine()) != null) {
                    if (line.startsWith("EmployeeID: ")) {
                        String employeeID = line.substring(12).trim();
                        System.out.println("Processing EmployeeID: " + employeeID);

                        // Read username
                        line = credentialsReader.readLine();
                        String username = line != null && line.startsWith("Username: ") ? line.substring(10).trim() : "";

                        // Read password
                        line = credentialsReader.readLine();
                        String password = line != null && line.startsWith("Password: ") ? line.substring(10).trim() : "";

                        // Read position
                        line = credentialsReader.readLine();
                        String position = line != null && line.startsWith("Position: ") ? line.substring(10).trim() : "";

                        // Read status
                        line = credentialsReader.readLine();
                        String status = line != null && line.startsWith("Status: ") ? line.substring(8).trim() : "";

                        // Read failed attempts
                        line = credentialsReader.readLine();
                        int failedAttempts = line != null && line.startsWith("FailedAttempts: ") 
                                             ? Integer.parseInt(line.substring(15).trim()) 
                                             : 0;

                        // Compute the status dynamically based on failed attempts
                        if (failedAttempts >= 3) {
                            status = "Locked";
                        }

                        // Retrieve employee data from Employee_data.txt
                        Employee employee = getEmployeeData(employeeID, employeeDataFilePath);

                        if (employee != null) {
                            System.out.println("Adding row for EmployeeID: " + employeeID);
                            model.addRow(new Object[]{
                                employeeID,
                                username,
                                password,
                                employee.getFullname(),
                                employee.getPhoneno(),
                                employee.getGender(),
                                employee.getPosition(),
                                employee.getDepartment(),
                                status // Use dynamic status
                            });
                        } else {
                            JOptionPane.showMessageDialog(null, "Error: No employee data found for EmployeeID: " + employeeID);
                            System.out.println("No employee data found for EmployeeID: " + employeeID);
                        }
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading user data: " + ex.getMessage());
            }
        }


        // Method to parse employee data from the formatted string
        private Employee parseEmployeeData(String employeeBlock) {
        String[] lines = employeeBlock.split("\n");
        String employeeID = "", fullname = "", phoneNo = "", gender = "", position = "", department = "", password = "", username = "", status = "";

        for (String line : lines) {
            if (line.startsWith("EmployeeID: ")) {
                employeeID = line.substring(12).trim();
            } else if (line.startsWith("Fullname: ")) {
                fullname = line.substring(9).trim();
            } else if (line.startsWith("Password: ")) {
                password = line.substring(10).trim();
            } else if (line.startsWith("Username: ")) {
                username = line.substring(10).trim();
            } else if (line.startsWith("Phone No: ")) {
                phoneNo = line.substring(10).trim();
            } else if (line.startsWith("Gender: ")) {
                gender = line.substring(8).trim();
            } else if (line.startsWith("Position: ")) {
                position = line.substring(10).trim();
            } else if (line.startsWith("Department: ")) {
                department = line.substring(12).trim();
            } else if (line.startsWith("Status: ")) {
                status = line.substring(8).trim();
            }
        }

        // Return an Employee object with the parsed data
        return new Employee(employeeID, password, fullname, username, phoneNo, gender, position, department, status); // Set default status as "Active"
    }



    private Employee getEmployeeData(String identifier, String employeeDataFilePath) {
        try (BufferedReader employeeReader = new BufferedReader(new FileReader(employeeDataFilePath))) {
        String line;
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        while ((line = employeeReader.readLine()) != null) {
            // Check for matching EmployeeID or Username
            if (line.startsWith("EmployeeID: ")) {
                String employeeID = line.substring(12).trim();
                if (employeeID.equals(identifier)) {
                    found = true;
                } else {
                    found = false;
                    sb.setLength(0); // Clear buffer if no match
                }
            } else if (line.startsWith("Username: ")) {
                String username = line.substring(10).trim();
                if (username.equals(identifier)) {
                    found = true;
                } else if (!found) {
                    sb.setLength(0); // Clear buffer if no match
                }
            }

            // Append lines from a matching block
            if (found) {
                sb.append(line).append("\n");
                if (line.isEmpty()) { // End of block
                    break;
                }
            }
        }

        if (found) {
            System.out.println("Employee data found for identifier: " + identifier);
            return parseEmployeeData(sb.toString()); // Parse and return the employee object
        }
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Error reading employee data: " + ex.getMessage());
    }

    return null; // Return null if no match is found
    }

    

    // Employee class to hold employee data
    class Employee {
        private String fullname;
        private String phoneno;
        private String gender;
        private String position;
        private String department;
        private String status;
        private String username; // Add Username field
        private String employeeID; // Add EmployeeID field for clarity
        private String password;

        private Employee(String employeeID,String password, String fullname, String username, String phoneNo, String gender, String position, String department, String status) {
            this.employeeID = employeeID;
            this.username = username;
            this.password = password;
            this.fullname = fullname;
            this.phoneno = phoneNo;
            this.gender = gender;
            this.position = position;
            this.department = department;
            this.status = status;
        }

        

        // Getters and Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmployeeID() { return employeeID; }
        public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }
        public String getFullname() { return fullname; }
        public void setFullname(String fullname) { this.fullname = fullname; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getPhoneno() { return phoneno; }
        public void setPhoneno(String phoneno) { this.phoneno = phoneno; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    
    
    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblUserData.getModel();
        model.setRowCount(0); // Clear existing rows
        loadUserData(); // Method to reload the employee data
        
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnUnlockedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnlockedActionPerformed
        int selectedRow = tblUserData.getSelectedRow();
        if (selectedRow != -1) {
            String employeeID = tblUserData.getValueAt(selectedRow, 0).toString();
            String status = tblUserData.getValueAt(selectedRow, 8).toString(); // Get Status from table

            if ("Active".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(null, "The account is already active. No need to unlock.");
            } else {
                updateUserAccount(employeeID, 0, "Active");
                JOptionPane.showMessageDialog(null, "Account unlocked successfully.");

                // Refresh table data
                loadUserData();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a row to unlock.");
        }
    }//GEN-LAST:event_btnUnlockedActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        // Dispose the UserAccForm frame
        this.dispose();

        // Open AdminTest frame
        AdminTest adminTest = new AdminTest("exampleIdentifier:Position"); // Pass identifier to AdminTest
        adminTest.setVisible(true);
        adminTest.pack();
        adminTest.setLocationRelativeTo(null);
        adminTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }//GEN-LAST:event_btnMenuActionPerformed

    
        private void updateUserAccount(String identifier, int failedAttempts, String status) {
            File inputFile = new File("user_credentials.txt");
            File tempFile = new File("user_credentials_temp.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                boolean isMatched = false;

                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        writer.newLine();
                        continue;
                    }

                    // Check for matching EmployeeID or Username
                    if ((line.startsWith("EmployeeID: ") && line.substring(12).trim().equals(identifier)) ||
                        (line.startsWith("Username: ") && line.substring(10).trim().equals(identifier))) {

                        isMatched = true; // Found matching block

                        // Write updated block
                        writer.write(line); // Write EmployeeID or Username
                        writer.newLine();
                        writer.write(reader.readLine()); // Write Username
                        writer.newLine();
                        writer.write(reader.readLine()); // Write Password
                        writer.newLine();
                        writer.write(reader.readLine()); // Write Position
                        writer.newLine();
                        writer.write("Status: " + status); // Update Status
                        writer.newLine();
                        writer.write("FailedAttempts: " + failedAttempts); // Update FailedAttempts
                        writer.newLine();
                        writer.newLine();

                        // Skip remaining lines in the current block
                        reader.readLine(); // Skip original Status
                        reader.readLine(); // Skip original FailedAttempts
                    } else {
                        writer.write(line); // Write non-matching line
                        writer.newLine();
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "File write error: " + ex.getMessage());
            }

            // Replace the old file with the updated file
            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(null, "File update error.");
            }
        }

    

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
            java.util.logging.Logger.getLogger(userAccForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(userAccForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(userAccForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(userAccForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new userAccForm().setVisible(true);
                //new Loginpage1().setVisible(true);
                //new Registerpage1().setVisible(true);
                //new AdminPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUnlocked;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblUpdate;
    private javax.swing.JPanel mainPanel;
    private static javax.swing.JTable tblUserData;
    // End of variables declaration//GEN-END:variables
}
