/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import model.Employee;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class RegisterManager {
    private static final String EMPLOYEE_FILE = "src/txtFile/Employee_data.txt";
    private static final String CREDENTIALS_FILE = "src/txtFile/user_credentials.txt";

    public boolean registerEmployee(Employee employee, String password) {
        if (!validateEmployee(employee)) {
            System.out.println("Registration failed: Duplicate EmployeeID or Username found.");
            return false; // ❌ Duplicate entry detected
        }

        // ✅ Save employee data
        if (!writeToFile(EMPLOYEE_FILE, formatEmployeeData(employee))) {
            return false;
        }

        // ✅ Save credentials
        String credentials = String.format("""
            EmployeeID: %s
            Username: %s
            Password: %s
            Position: %s
            Status: Active
            FailedAttempts: 0

            """, employee.getEmployeeID(), employee.getUsername(), password, employee.getPosition());

        return writeToFile(CREDENTIALS_FILE, credentials);
    }

    private boolean validateEmployee(Employee employee) {
        if (employee.getEmployeeID() == null || employee.getEmployeeID().isEmpty() ||
            employee.getUsername() == null || employee.getUsername().isEmpty()) {
            return false;
        }

        // ✅ Check for duplicate EmployeeID or Username
        try (BufferedReader empReader = new BufferedReader(new FileReader(EMPLOYEE_FILE));
             BufferedReader credReader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {

            String line;
            while ((line = empReader.readLine()) != null) {
                if (line.contains("EmployeeID: " + employee.getEmployeeID()) || line.contains("Username: " + employee.getUsername())) {
                    System.out.println("Duplicate EmployeeID or Username found in Employee_data.txt");
                    return false;
                }
            }

            while ((line = credReader.readLine()) != null) {
                if (line.contains("EmployeeID: " + employee.getEmployeeID()) || line.contains("Username: " + employee.getUsername())) {
                    System.out.println("Duplicate EmployeeID or Username found in user_credentials.txt");
                    return false;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
            return false;
        }

        return true; // ✅ Employee is unique
    }


    private boolean writeToFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data);
            writer.flush();
            return true;
        } catch (IOException ex) {
            System.out.println("File write error: " + ex.getMessage());
            return false;
        }
    }

    private String formatEmployeeData(Employee employee) {
        return String.format("""
            EmployeeID: %s
            Username: %s
            Fullname: %s
            MyKad(NRIC): %s
            Phone No: %s
            Emergency: %s
            Address Line 1: %s
            Address Line 2: %s
            Address Line 3: %s
            Address Line 4: %s
            Bank Account: %s
            Position: %s
            Department: %s
            Gross Salary: %s
            Gender: %s
            Company: %s
            Start Date: %s
            End Date: %s
            Job Title: %s
            Overview: %s
            Status: Active

            """, employee.getEmployeeID(), employee.getUsername(), employee.getFullname(), employee.getMyKad(),
            employee.getPhoneno(), employee.getEmergency(), employee.getAddLine1(), employee.getAddLine2(),
            employee.getAddLine3(), employee.getAddLine4(), employee.getBankAcc(), employee.getPosition(),
            employee.getDepartment(), employee.getGrossSalary(), employee.getGender(), employee.getCompany(),
            employee.getStartDate(), employee.getEndDate(), employee.getJobTitle(), employee.getOverview());
    }
    
    public String generateNextPassword() {
        int maxPassword = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Password: ")) {
                    String passwordStr = line.substring(10).trim();
                    if (passwordStr.matches("\\d+")) { // Ensure it's numeric
                        int currentPassword = Integer.parseInt(passwordStr);
                        maxPassword = Math.max(maxPassword, currentPassword);
                    }
                }
            }
        } catch (IOException | NumberFormatException ex) {
            System.out.println("Error reading credentials file: " + ex.getMessage());
        }
        return String.format("%06d", maxPassword + 1); // Ensure format like "000001"
    }
    
}
