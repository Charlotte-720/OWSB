/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin.functions;

import Admin.AdminTest;
import FinanceManager.FinanceManagerPanel;
import InventoryManager.InventoryManagerDashboard;
import PurchaseManager.PM;
import SalesManager.SalesManagerDashboard;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class LoginManager {
    
    private String loggedInIdentifier = "";
    private String position = "";
    
    public boolean authenticateUser(String inputIDOrUsername, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/txtFile/user_credentials.txt"))) {
            String line;
            StringBuilder userBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    if (userBuilder.length() > 0) {
                        if (processUserBlock(userBuilder, inputIDOrUsername, password)) {
                            return true;
                        }
                        userBuilder.setLength(0);
                    }
                } else {
                    userBuilder.append(line).append("\n");
                }
            }

            if (userBuilder.length() > 0) {
                return processUserBlock(userBuilder, inputIDOrUsername, password);
            }
        } catch (IOException ex) {
            System.out.println("File read error: " + ex.getMessage());
        }

        return false;
    }

    private boolean processUserBlock(StringBuilder userBuilder, String inputIDOrUsername, String password) {
        String[] userInfo = userBuilder.toString().trim().split("\\r?\\n");
        if (userInfo.length < 6) return false;

        try {
            String storedUsername = userInfo[1].substring(10).trim(); 
            String storedPassword = userInfo[2].substring(10).trim(); 
            position = userInfo[3].substring(10).trim(); 
            String status = userInfo[4].substring(8).trim();
            int failedAttempts = Integer.parseInt(userInfo[5].substring(15).trim());

            if (status.equals("Locked")) {
                JOptionPane.showMessageDialog(null, "Account is locked due to too many failed login attempts.");
                return false;
            }

            if (inputIDOrUsername.equalsIgnoreCase(storedUsername) && password.equals(storedPassword)) {
                // **Reset failed attempts on success**
                updateUserAccount(storedUsername, 0, "Active");
                loggedInIdentifier = inputIDOrUsername + ":" + position;
                return true;
            } else {
                // **Increase failed attempts**
                failedAttempts++;

                if (failedAttempts >= 3) {
                    updateUserAccount(storedUsername, failedAttempts, "Locked");
                    JOptionPane.showMessageDialog(null, "Login failed. Your account is now locked.");
                } else {
                    updateUserAccount(storedUsername, failedAttempts, "Active");
                    JOptionPane.showMessageDialog(null, "Login failed. Attempts used: " + failedAttempts + "/3");
                }

                return false;
            }
        } catch (Exception e) {
            System.out.println("Error processing user block: " + e.getMessage());
        }

        return false;
    }


    public void navigateToRolePage() {
        if (!loggedInIdentifier.contains(":")) {
            JOptionPane.showMessageDialog(null, "Invalid identifier format: " + loggedInIdentifier);
            return;
        }

        switch (position) {
            case "Admin":
                openPage(new AdminTest(loggedInIdentifier));
                break;
            case "Sales Manager":
                openPage(new SalesManagerDashboard(loggedInIdentifier));
                break;
            case "Purchase Manager":
                openPage(new PM(loggedInIdentifier));
                break;
            case "Inventory Manager":
                openPage(new InventoryManagerDashboard(loggedInIdentifier));
                break;
            case "Finance Manager":
                openPage(new FinanceManagerPanel(loggedInIdentifier));
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid position: " + position);
                break;
        }
    }

    private void openPage(JFrame page) {
        page.setVisible(true);
        page.pack();
        page.setLocationRelativeTo(null);
        page.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void handleLoginFailure(String inputIDOrUsername) {
        JOptionPane.showMessageDialog(null, "Invalid login attempt for: " + inputIDOrUsername);

        File inputFile = new File("src/txtFile/user_credentials.txt");
        File tempFile = new File("src/txtFile/user_credentials_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.newLine();
                    continue;
                }

                if ((line.startsWith("EmployeeID: ") && line.substring(12).trim().equals(inputIDOrUsername)) ||
                    (line.startsWith("Username: ") && line.substring(10).trim().equalsIgnoreCase(inputIDOrUsername))) {

                    writer.write(line); 
                    writer.newLine();
                    writer.write(reader.readLine()); 
                    writer.newLine();
                    writer.write(reader.readLine()); 
                    writer.newLine();
                    writer.write(reader.readLine()); 
                    writer.newLine();

                    String statusLine = reader.readLine();
                    String failedAttemptsLine = reader.readLine();

                    int failedAttempts = Integer.parseInt(failedAttemptsLine.substring(15).trim()) + 1;

                    if (failedAttempts >= 3) {
                        writer.write("Status: Locked");
                        JOptionPane.showMessageDialog(null, "Account locked due to too many failed attempts.");
                    } else {
                        writer.write(statusLine);
                    }
                    writer.newLine();
                    writer.write("FailedAttempts: " + failedAttempts);
                    writer.newLine();
                } else {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "File write error: " + ex.getMessage());
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(null, "File update error.");
        }
    }
    
    public void updateUserAccount(String identifier, int failedAttempts, String status) {
        File inputFile = new File("src/txtFile/user_credentials.txt");
        File tempFile = new File("src/txtFile/user_credentials_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writer.newLine();
                    continue;
                }

                if ((line.startsWith("EmployeeID: ") && line.substring(12).trim().equals(identifier)) ||
                    (line.startsWith("Username: ") && line.substring(10).trim().equals(identifier))) {

                    writer.write(line); // EmployeeID or Username
                    writer.newLine();
                    writer.write(reader.readLine()); // Username
                    writer.newLine();
                    writer.write(reader.readLine()); // Password
                    writer.newLine();
                    writer.write(reader.readLine()); // Role
                    writer.newLine();
                    writer.write("Status: " + status); // Update status
                    writer.newLine();
                    writer.write("FailedAttempts: " + failedAttempts); // Update failed attempts
                    writer.newLine();

                    reader.readLine(); // Skip original Status line
                    reader.readLine(); // Skip original FailedAttempts line
                } else {
                    writer.write(line);
                    writer.newLine();
                    writer.write(reader.readLine());
                    writer.newLine();
                    writer.write(reader.readLine());
                    writer.newLine();
                    writer.write(reader.readLine());
                    writer.newLine();
                    writer.write(reader.readLine());
                    writer.newLine();
                    writer.write(reader.readLine());
                    writer.newLine();
                    writer.write(reader.readLine());
                    writer.newLine();
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "File write error: " + ex.getMessage());
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(null, "File update error.");
        }
    }
}
    

