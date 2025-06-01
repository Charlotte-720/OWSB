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
import java.nio.file.Files;
import model.Employee;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class EditEmployeeManager {
    
    private static final String EMPLOYEE_DATA_FILE = "src/txtFile/Employee_data.txt";
    private static final String USER_CREDENTIALS_FILE = "src/txtFile/user_credentials.txt";

    public Employee getEmployeeData(String identifier) {
        try (BufferedReader employeeReader = new BufferedReader(new FileReader(EMPLOYEE_DATA_FILE))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean isMatchingBlock = false;

            while ((line = employeeReader.readLine()) != null) {
                if (line.startsWith("EmployeeID: ")) {
                    String employeeID = line.substring(12).trim();
                    if (employeeID.equals(identifier)) {
                        isMatchingBlock = true;
                        sb.setLength(0);
                    } else if (isMatchingBlock) {
                        break;
                    }
                }
                if (isMatchingBlock) {
                    sb.append(line).append("\n");
                }
            }
            return (isMatchingBlock && sb.length() > 0) ? parseEmployeeData(sb.toString()) : null;
        } catch (IOException ex) {
            System.out.println("Error reading employee data: " + ex.getMessage());
        }
        return null;
    }

    public String loadCredentialsData(String identifier) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_CREDENTIALS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("EmployeeID: " + identifier)) {
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("Password: ")) {
                            return line.substring(10).trim();
                        }
                        if (line.startsWith("EmployeeID: ")) break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading credentials data: " + ex.getMessage());
        }
        return "";
    }

    private Employee parseEmployeeData(String data) {
        System.out.println("Parsing employee block:\n" + data);
        String[] lines = data.split("\n");
        Employee employee = new Employee();

        for (String line : lines) {
            line = line.trim();
            System.out.println("Parsing line: [" + line + "]");

            if (line.startsWith("EmployeeID: ")) {
                employee.setEmployeeID(line.substring(12).trim());
            } else if (line.startsWith("Username: ")) {
                employee.setUsername(line.substring(10).trim());
            } else if (line.startsWith("Fullname: ")) {
                employee.setFullname(line.substring(9).trim());
            } else if (line.startsWith("MyKad(NRIC): ")) {
                employee.setMyKad(line.substring(12).trim());
            } else if (line.startsWith("Phone No: ")) {
                employee.setPhoneno(line.substring(10).trim());
            } else if (line.startsWith("Emergency: ")) {
                employee.setEmergency(line.substring(10).trim());
            } else if (line.startsWith("Address Line 1: ")) {
                employee.setAddLine1(line.substring(16).trim());
            } else if (line.startsWith("Address Line 2: ")) {
                employee.setAddLine2(line.substring(16).trim());
            } else if (line.startsWith("Address Line 3: ")) {
                employee.setAddLine3(line.substring(16).trim());
            } else if (line.startsWith("Address Line 4: ")) {
                employee.setAddLine4(line.substring(16).trim());
            } else if (line.startsWith("Bank Account: ")) {
                employee.setBankAcc(line.substring(14).trim());
            } else if (line.startsWith("Position: ")) {
                employee.setPosition(line.substring(10).trim());
            } else if (line.startsWith("Department: ")) {
                employee.setDepartment(line.substring(12).trim());
            } else if (line.startsWith("Gross Salary: ")) {
                employee.setGrossSalary(line.substring(14).trim());
            } else if (line.startsWith("Gender: ")) {
                employee.setGender(line.substring(8).trim());
            } else if (line.startsWith("Company: ")) {
                employee.setCompany(line.substring(9).trim());
            } else if (line.startsWith("Start Date: ")) {
                employee.setStartDate(line.substring(12).trim());
            } else if (line.startsWith("End Date: ")) {
                employee.setEndDate(line.substring(10).trim());
            } else if (line.startsWith("Job Title: ")) {
                employee.setJobTitle(line.substring(11).trim());
            } else if (line.startsWith("Overview: ")) {
                employee.setOverview(line.substring(10).trim());
            }
        }
        System.out.println("Parsed Employee Data: ID = [" + employee.getEmployeeID() + "], Fullname = [" + employee.getFullname() + "]");
        return employee;
    }
    
    

    public boolean updateEmployeeData(Employee employee) {
    File employeeDataFile = new File("src/txtFile/Employee_data.txt");
    File tempEmployeeFile = new File("src/txtFile/Employee_data_temp.txt");

    try (BufferedReader reader = new BufferedReader(new FileReader(employeeDataFile));
         BufferedWriter writer = new BufferedWriter(new FileWriter(tempEmployeeFile))) {

        String line;
        boolean employeeFound = false;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("EmployeeID: " + employee.getEmployeeID())) {
                // Replace employee data
                writer.write(formatEmployeeData(employee)); // Call helper function
                writer.flush();
                for (int i = 0; i < 21; i++) {
                    reader.readLine(); // Skip next lines after match
                }
                employeeFound = true;
            } else {
                writer.write(line + "\n");
            }
        }

        if (!employeeFound) return false;

    } catch (IOException ex) {
        System.out.println("Error updating employee data: " + ex.getMessage());
        return false;
    }

    return replaceOriginalFile(employeeDataFile, tempEmployeeFile);
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
                          """,
            employee.getEmployeeID(), employee.getUsername(), employee.getFullname(), employee.getMyKad(),
            employee.getPhoneno(), employee.getEmergency(), employee.getAddLine1(), employee.getAddLine2(),
            employee.getAddLine3(), employee.getAddLine4(), employee.getBankAcc(), employee.getPosition(),
            employee.getDepartment(), employee.getGrossSalary(), employee.getGender(), employee.getCompany(),
            employee.getStartDate(), employee.getEndDate(), employee.getJobTitle(), employee.getOverview());
}

    // Helper method to safely replace original file
    private boolean replaceOriginalFile(File original, File temp) {
        try {
            Files.deleteIfExists(original.toPath());
            return temp.renameTo(original);
        } catch (IOException ex) {
            System.out.println("Error replacing file: " + ex.getMessage());
            return false;
        }
    }

    
}
   

