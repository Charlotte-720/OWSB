/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class AdminManager {
    
    private static final String EMPLOYEE_DATA_FILE = "src/txtFile/Employee_data.txt";
    private static final String USER_CREDENTIALS_FILE = "src/txtFile/user_credentials.txt";
    private boolean userFound;
    
   
    public void populateUserTable(DefaultTableModel model) {
        try (BufferedReader employeeReader = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE))) {
            String line;
            StringBuilder employeeBlock = new StringBuilder();

            while ((line = employeeReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    Employee employee = parseEmployeeData(employeeBlock.toString());
                    if (employee != null) {
                        model.addRow(new Object[]{
                                employee.getEmployeeID(),
                                employee.getUsername(),
                                employee.getFullname(),
                                employee.getPosition(),
                                employee.getDepartment()
                        });
                    }
                    employeeBlock.setLength(0); // Reset buffer for the next employee
                } else {
                    employeeBlock.append(line).append("\n");
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading employee data: " + ex.getMessage());
        }
    }

    private Employee parseEmployeeData(String employeeBlock) {
        String[] lines = employeeBlock.split("\n");
        String employeeID = "", username = "", fullname = "", phoneNo = "", gender = "", position = "", department = "";

        for (String line : lines) {
            if (line.startsWith("EmployeeID: ")) employeeID = line.substring(12).trim();
            if (line.startsWith("Username: ")) username = line.substring(9).trim();
            if (line.startsWith("Fullname: ")) fullname = line.substring(9).trim();
            if (line.startsWith("Phone No: ")) phoneNo = line.substring(10).trim();
            if (line.startsWith("Gender: ")) gender = line.substring(8).trim();
            if (line.startsWith("Position: ")) position = line.substring(10).trim();
            if (line.startsWith("Department: ")) department = line.substring(12).trim();
        }

        return new Employee(employeeID, username, fullname, phoneNo, gender, position, department);
    }
    
    public boolean removeUser(String identifier) {
        boolean removedFromEmployeeData = removeUserFromFile(identifier, EMPLOYEE_DATA_FILE);
        boolean removedFromCredentials = removeUserFromFile(identifier, USER_CREDENTIALS_FILE);

        return removedFromEmployeeData && removedFromCredentials;
    }

    private boolean removeUserFromFile(String identifier, String fileName) {
        File inputFile = new File(fileName);
        File tempFile = new File(fileName + "_temp");

        List<String> updatedLines = new ArrayList<>();
        boolean userFound = false;
        boolean insideUserBlock = false; // Track if we are inside a block to be deleted

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();

                // Detect start of user block
                if (trimmedLine.startsWith("EmployeeID: " + identifier) || trimmedLine.startsWith("Username: " + identifier)) {
                    userFound = true;
                    insideUserBlock = true; // Begin skipping lines
                    continue; // Skip the first line
                }

                // Continue skipping until an empty line is reached
                if (insideUserBlock) {
                    if (trimmedLine.isEmpty()) {
                        insideUserBlock = false; // Stop skipping when reaching an empty line
                    }
                    continue;
                }

                // Add lines that are NOT part of the block to keep
                updatedLines.add(line);
            }
        } catch (IOException ex) {
            System.out.println("File read error: " + ex.getMessage());
            return false;
        }

        // If the user was found, write back the updated file
        if (userFound) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException ex) {
                System.out.println("File write error: " + ex.getMessage());
                return false;
            }

            // Ensure the original file is deleted before renaming the temp file
            if (!inputFile.delete()) {
                System.out.println("ERROR: Failed to delete original file: " + fileName);
                return false;
            }

            if (!tempFile.renameTo(inputFile)) {
                System.out.println("ERROR: Failed to rename temp file: " + fileName);
                return false;
            }
        } else {
            tempFile.delete(); // Cleanup unused temp file if user was not found
        }

        return userFound;
    }


    
    class Employee {
        private final String employeeID;
        private final String username;
        private final String fullname;
        private final String phoneno;
        private final String gender;
        private final String position;
        private final String department;

        public Employee(String employeeID, String username, String fullname, String phoneno, String gender, String position, String department) {
            this.employeeID = employeeID;
            this.username = username;
            this.fullname = fullname;
            this.phoneno = phoneno;
            this.gender = gender;
            this.position = position;
            this.department = department;
        }

        public String getEmployeeID() { return employeeID; }
        public String getUsername() { return username; }
        public String getFullname() { return fullname; }
        public String getPhoneno() { return phoneno; }
        public String getGender() { return gender; }
        public String getPosition() { return position; }
        public String getDepartment() { return department; }

    }
}
