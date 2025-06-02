/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin.functions;

import Admin.userAccForm;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Employee;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class userAccManager {
    private static final String CREDENTIALS_FILE = "src/txtFile/user_credentials.txt";
    private static final String EMPLOYEE_FILE = "src/txtFile/Employee_data.txt";

    // Load user account details
    public Employee getUserAccount(String employeeID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            Employee employee = new Employee();
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("EmployeeID: " + employeeID)) {
                    found = true;
                    employee.setEmployeeID(employeeID);
                } else if (found) {
                    if (line.startsWith("Username: ")) employee.setUsername(line.substring(10).trim());
                    else if (line.startsWith("Position: ")) employee.setPosition(line.substring(10).trim());
                    else if (line.startsWith("Status: ")) break; // Stop after status
                }
            }
            return found ? employee : null;
        } catch (IOException ex) {
            System.out.println("Error reading user account data: " + ex.getMessage());
        }
        return null;
    }

    // Load users and populate JTable
    public void loadUserData(DefaultTableModel model) {
        try (BufferedReader credentialsReader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;

            while ((line = credentialsReader.readLine()) != null) {
                if (line.startsWith("EmployeeID: ")) {
                    String employeeID = line.substring(12).trim();
                    String username = credentialsReader.readLine().substring(10).trim();
                    String password = credentialsReader.readLine().substring(10).trim();
                    String position = credentialsReader.readLine().substring(10).trim();
                    String status = credentialsReader.readLine().substring(8).trim();
                    int failedAttempts = Integer.parseInt(credentialsReader.readLine().substring(15).trim());

                    // Lock account if failed attempts exceed threshold
                    if (failedAttempts >= 3) status = "Locked";

                    // Fetch employee details
                    Employee employee = getEmployeeData(employeeID);

                    if (employee != null) {
                        model.addRow(new Object[]{
                            employeeID, username, password,
                            employee.getFullname(), employee.getPhoneno(),
                            employee.getGender(), employee.getPosition(),
                            employee.getDepartment(), status
                        });
                    } else {
                        System.out.println("No employee data found for EmployeeID: " + employeeID);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading user data: " + ex.getMessage());
        }
    }

    // Get employee details
    private Employee getEmployeeData(String employeeID) {
        try (BufferedReader reader = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            String line;
            Employee employee = new Employee();
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("EmployeeID: " + employeeID)) found = true;
                if (found) {
                    if (line.startsWith("Fullname: ")) employee.setFullname(line.substring(9).trim());
                    else if (line.startsWith("Phone No: ")) employee.setPhoneno(line.substring(10).trim());
                    else if (line.startsWith("Gender: ")) employee.setGender(line.substring(8).trim());
                    else if (line.startsWith("Position: ")) employee.setPosition(line.substring(10).trim());
                    else if (line.startsWith("Department: ")) employee.setDepartment(line.substring(12).trim());
                    else if (line.isEmpty()) break; // Stop after blank line
                }
            }
            return found ? employee : null;
        } catch (IOException ex) {
            System.out.println("Error reading employee data: " + ex.getMessage());
        }
        return null;
    }
    
    
    
    public boolean updateUserAccount(String identifier, int failedAttempts, String status, userAccForm form) {
        File inputFile = new File("src/txtFile/user_credentials.txt");
        File tempFile = new File("src/txtFile/user_credentials_temp.txt");
        boolean isMatched = false;

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

                    isMatched = true;

                    // Preserve existing data
                    writer.write(line + "\n");

                    // Read and write Username
                    line = reader.readLine();
                    if (line.startsWith("Username: ")) writer.write(line + "\n");

                    // Read and write Password
                    line = reader.readLine();
                    if (line.startsWith("Password: ")) writer.write(line + "\n");

                    // Read and write Position
                    line = reader.readLine();
                    if (line.startsWith("Position: ")) writer.write(line + "\n");

                    // Update Status
                    writer.write("Status: " + status + "\n");

                    // Update FailedAttempts
                    writer.write("FailedAttempts: " + failedAttempts + "\n\n");

                    // Skip original status and failed attempts in the old file
                    reader.readLine();
                    reader.readLine();
                } else {
                    writer.write(line + "\n");
                }
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "File write error: " + ex.getMessage());
            return false;
        }

        if (isMatched) {
            try {
                Files.deleteIfExists(inputFile.toPath());
                Files.move(tempFile.toPath(), inputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Ensure the table refreshes in the UI
                form.refreshUserTable(); // Calls the method in userAccForm

                JOptionPane.showMessageDialog(null, "Account updated successfully!");
                return true;
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "File update error: " + ex.getMessage());
                return false;
            }
        }
        return false;
    }   
} 

    


    

