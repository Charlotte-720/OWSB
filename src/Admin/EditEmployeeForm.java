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
import java.nio.file.Files;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author ELYSHA SOPHIA
 */
public class EditEmployeeForm extends javax.swing.JFrame {
    
    private String employeeID;
    private String position;

    public EditEmployeeForm(String identifier) {
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
        
        System.out.println("Loading employee with identifier: " + identifier);
        System.out.println("Identifier passed to EditEmployeeForm: [" + identifier + "]");

        
        // Attempt to load employee data
        Employee employee = getEmployeeData(identifier, "src/txtFile/Employee_data.txt");
        if (employee != null) {
            loadEmployeeData(identifier); // Load data into the form
        } else {
            // Show error message and stop form initialization
            JOptionPane.showMessageDialog(null, "Employee not found for identifier: " + identifier, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose(); // Close the form to prevent display
        }

    }
    
    private void loadEmployeeData(String identifier) {
        System.out.println("Loading employee data for: " + identifier);
        Employee employee = getEmployeeData(identifier, "src/txtFile/Employee_data.txt");
        if (employee != null) {
            txtFullname.setText(employee.getFullname());
            txtUsername.setText(employee.getUsername());
            txtEmployeeID.setText(employee.getEmployeeID());
            txtMyKad.setText(employee.getMyKad());
            txtPhoneNo.setText(employee.getPhoneno());
            txtEmergency.setText(employee.getEmergency());
            txtAddLine1.setText(employee.getAddLine1());
            txtAddLine2.setText(employee.getAddLine2());
            txtAddLine3.setText(employee.getAddLine3());
            txtAddLine4.setText(employee.getAddLine4());
            txtBankAcc.setText(employee.getBankAcc());
            txtPosition.setText(employee.getPosition());
            txtDepartment.setText(employee.getDepartment());
            txtGrossSalary.setText(employee.getGrossSalary());
            txtGender.setText(employee.getGender());
            txtCompany.setText(employee.getCompany());
            txtStartDate.setText(employee.getStartDate());
            txtEndDate.setText(employee.getEndDate());
            txtJobTitle.setText(employee.getJobTitle());
            txtOverview.setText(employee.getOverview());
            
            String password = loadCredentialsData(identifier);
            txtPass.setText(password);
        } 
    }
    
        private String loadCredentialsData(String identifier) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/txtFile/user_credentials.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                // Check if the current line matches the EmployeeID
                if (line.startsWith("EmployeeID: " + identifier)) {
                    // Read and process lines within the matching block
                    String nextLine;
                    while ((nextLine = reader.readLine()) != null) {
                        if (nextLine.startsWith("Password: ")) {
                            return nextLine.substring(10).trim(); // Extract password after "Password: "
                        }

                        // Stop reading the block if a new EmployeeID begins
                        if (nextLine.startsWith("EmployeeID: ")) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error reading credentials data: " + ex.getMessage());
        }
        return ""; // Return empty if no match found
    }



        private Employee getEmployeeData(String identifier, String employeeDataFilePath) {
        try (BufferedReader employeeReader = new BufferedReader(new FileReader(employeeDataFilePath))) {
            StringBuilder sb = new StringBuilder();
            String line;
            boolean isMatchingBlock = false;

            while ((line = employeeReader.readLine()) != null) {
                System.out.println("Reading line: [" + line + "]");

                // Check if the line starts a new employee block
                if (line.startsWith("EmployeeID: ")) {
                    String employeeID = line.substring(12).trim();
                    System.out.println("Comparing identifier: [" + identifier + "] with EmployeeID: [" + employeeID + "]");

                    if (employeeID.equals(identifier)) {
                        isMatchingBlock = true; // Found matching block
                        sb.setLength(0); // Clear any previous block data
                    } else if (isMatchingBlock) {
                        break; // End of the matching block
                    }
                }

                // Append all lines within the matching block
                if (isMatchingBlock) {
                    sb.append(line).append("\n");
                }
            }

            // Parse and return the employee if a block was found
            if (isMatchingBlock && sb.length() > 0) {
                System.out.println("Final employee block to parse:\n" + sb.toString());
                return parseEmployeeData(sb.toString());
            } else {
                System.out.println("No matching employee found for identifier: [" + identifier + "]");
            }
        } catch (IOException ex) {
            System.out.println("Error reading employee data: " + ex.getMessage());
        }

        return null; // Return null if no match was found
    }


    
        private Employee parseEmployeeData(String data) {
        System.out.println("Parsing employee block:\n" + data);
        String[] lines = data.split("\n");
        Employee employee = new Employee();

        for (String line : lines) {
            line = line.trim(); // Remove leading/trailing spaces
            System.out.println("Parsing line: [" + line + "]");

            if (line.startsWith("EmployeeID: ")) {
                employee.setEmployeeID(line.substring(12).trim());
            } else if (line.startsWith("Username: ")) {
                employee.setUsername(line.substring(10).trim());
            } else if (line.startsWith("Fullname: ")) {
                employee.setFullname(line.substring(9).trim()); // Correct substring to extract "Fullname"
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblFullname = new javax.swing.JLabel();
        txtFullname = new javax.swing.JTextField();
        lblMyKad = new javax.swing.JLabel();
        txtMyKad = new javax.swing.JTextField();
        lblEmpID = new javax.swing.JLabel();
        txtEmployeeID = new javax.swing.JTextField();
        lblPhoneNo = new javax.swing.JLabel();
        txtPhoneNo = new javax.swing.JTextField();
        lblAddress = new javax.swing.JLabel();
        txtAddLine1 = new javax.swing.JTextField();
        txtAddLine2 = new javax.swing.JTextField();
        txtAddLine3 = new javax.swing.JTextField();
        txtAddLine4 = new javax.swing.JTextField();
        lblBankAcc = new javax.swing.JLabel();
        txtBankAcc = new javax.swing.JTextField();
        lblEmergency = new javax.swing.JLabel();
        txtEmergency = new javax.swing.JTextField();
        lblPosition = new javax.swing.JLabel();
        txtPosition = new javax.swing.JTextField();
        lblDepartment = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        lblGrossSalary = new javax.swing.JLabel();
        txtGrossSalary = new javax.swing.JTextField();
        lblCompany = new javax.swing.JLabel();
        txtCompany = new javax.swing.JTextField();
        lblStartDate = new javax.swing.JLabel();
        txtStartDate = new javax.swing.JTextField();
        lblEndDate = new javax.swing.JLabel();
        txtEndDate = new javax.swing.JTextField();
        lblJobTitle = new javax.swing.JLabel();
        txtJobTitle = new javax.swing.JTextField();
        lblOverview = new javax.swing.JLabel();
        txtOverview = new javax.swing.JTextField();
        lblGender = new javax.swing.JLabel();
        txtGender = new javax.swing.JTextField();
        lblPass = new javax.swing.JLabel();
        txtPass = new javax.swing.JTextField();
        lblUserN = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        btnClear1 = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        mainPanel.setBackground(new java.awt.Color(231, 240, 220));
        mainPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(153, 153, 153)));

        headerPanel.setBackground(new java.awt.Color(89, 116, 69));
        headerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(231, 240, 220));
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        headerPanel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(231, 240, 220));
        jLabel2.setText("Edit Employee Profile");
        headerPanel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, -1, -1));

        lblFullname.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblFullname.setForeground(new java.awt.Color(72, 89, 4));
        lblFullname.setText("Fullname : ");

        txtFullname.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtFullname.setForeground(new java.awt.Color(72, 89, 4));
        txtFullname.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtFullname.setName(""); // NOI18N
        txtFullname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFullnameActionPerformed(evt);
            }
        });

        lblMyKad.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblMyKad.setForeground(new java.awt.Color(72, 89, 4));
        lblMyKad.setText("MyKad (NRIC) : ");

        txtMyKad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtMyKad.setForeground(new java.awt.Color(72, 89, 4));
        txtMyKad.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtMyKad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMyKadActionPerformed(evt);
            }
        });

        lblEmpID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEmpID.setForeground(new java.awt.Color(72, 89, 4));
        lblEmpID.setText("Employee ID : ");

        txtEmployeeID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEmployeeID.setForeground(new java.awt.Color(72, 89, 4));
        txtEmployeeID.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtEmployeeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmployeeIDActionPerformed(evt);
            }
        });

        lblPhoneNo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPhoneNo.setForeground(new java.awt.Color(72, 89, 4));
        lblPhoneNo.setText("Phone No. : ");

        txtPhoneNo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPhoneNo.setForeground(new java.awt.Color(72, 89, 4));
        txtPhoneNo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtPhoneNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneNoActionPerformed(evt);
            }
        });

        lblAddress.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblAddress.setForeground(new java.awt.Color(72, 89, 4));
        lblAddress.setText("Address Home : ");

        txtAddLine1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAddLine1.setForeground(new java.awt.Color(72, 89, 4));
        txtAddLine1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtAddLine1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddLine1ActionPerformed(evt);
            }
        });

        txtAddLine2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAddLine2.setForeground(new java.awt.Color(72, 89, 4));
        txtAddLine2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtAddLine2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddLine2ActionPerformed(evt);
            }
        });

        txtAddLine3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAddLine3.setForeground(new java.awt.Color(72, 89, 4));
        txtAddLine3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtAddLine3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddLine3ActionPerformed(evt);
            }
        });

        txtAddLine4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtAddLine4.setForeground(new java.awt.Color(72, 89, 4));
        txtAddLine4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtAddLine4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAddLine4ActionPerformed(evt);
            }
        });

        lblBankAcc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblBankAcc.setForeground(new java.awt.Color(72, 89, 4));
        lblBankAcc.setText("Bank Account No. : ");

        txtBankAcc.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtBankAcc.setForeground(new java.awt.Color(72, 89, 4));
        txtBankAcc.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtBankAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBankAccActionPerformed(evt);
            }
        });

        lblEmergency.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEmergency.setForeground(new java.awt.Color(72, 89, 4));
        lblEmergency.setText("Emergency Contact : ");

        txtEmergency.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEmergency.setForeground(new java.awt.Color(72, 89, 4));
        txtEmergency.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtEmergency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmergencyActionPerformed(evt);
            }
        });

        lblPosition.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPosition.setForeground(new java.awt.Color(72, 89, 4));
        lblPosition.setText(" Position : ");

        txtPosition.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPosition.setForeground(new java.awt.Color(72, 89, 4));
        txtPosition.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtPosition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPositionActionPerformed(evt);
            }
        });

        lblDepartment.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDepartment.setForeground(new java.awt.Color(72, 89, 4));
        lblDepartment.setText(" Department : ");

        txtDepartment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDepartment.setForeground(new java.awt.Color(72, 89, 4));
        txtDepartment.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtDepartment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDepartmentActionPerformed(evt);
            }
        });

        lblGrossSalary.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblGrossSalary.setForeground(new java.awt.Color(72, 89, 4));
        lblGrossSalary.setText(" Monthly Gross Salary : ");

        txtGrossSalary.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtGrossSalary.setForeground(new java.awt.Color(72, 89, 4));
        txtGrossSalary.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtGrossSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGrossSalaryActionPerformed(evt);
            }
        });

        lblCompany.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCompany.setForeground(new java.awt.Color(72, 89, 4));
        lblCompany.setText(" Company : ");

        txtCompany.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtCompany.setForeground(new java.awt.Color(72, 89, 4));
        txtCompany.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtCompany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCompanyActionPerformed(evt);
            }
        });

        lblStartDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblStartDate.setForeground(new java.awt.Color(72, 89, 4));
        lblStartDate.setText(" Start (DD/MM/YYYY) : ");

        txtStartDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtStartDate.setForeground(new java.awt.Color(72, 89, 4));
        txtStartDate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtStartDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStartDateActionPerformed(evt);
            }
        });

        lblEndDate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblEndDate.setForeground(new java.awt.Color(72, 89, 4));
        lblEndDate.setText("End (DD/MM/YYYY) :  ");

        txtEndDate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtEndDate.setForeground(new java.awt.Color(72, 89, 4));
        txtEndDate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtEndDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndDateActionPerformed(evt);
            }
        });

        lblJobTitle.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblJobTitle.setForeground(new java.awt.Color(72, 89, 4));
        lblJobTitle.setText(" Job Title : ");

        txtJobTitle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtJobTitle.setForeground(new java.awt.Color(72, 89, 4));
        txtJobTitle.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtJobTitle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJobTitleActionPerformed(evt);
            }
        });

        lblOverview.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblOverview.setForeground(new java.awt.Color(72, 89, 4));
        lblOverview.setText("Overview of Leave Entitlement :");

        txtOverview.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtOverview.setForeground(new java.awt.Color(72, 89, 4));
        txtOverview.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtOverview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOverviewActionPerformed(evt);
            }
        });

        lblGender.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblGender.setForeground(new java.awt.Color(72, 89, 4));
        lblGender.setText(" Gender (Male/Female) : ");

        txtGender.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtGender.setForeground(new java.awt.Color(72, 89, 4));
        txtGender.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtGender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGenderActionPerformed(evt);
            }
        });

        lblPass.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPass.setForeground(new java.awt.Color(72, 89, 4));
        lblPass.setText("Password : ");

        txtPass.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtPass.setForeground(new java.awt.Color(72, 89, 4));
        txtPass.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtPass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPassActionPerformed(evt);
            }
        });

        lblUserN.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblUserN.setForeground(new java.awt.Color(72, 89, 4));
        lblUserN.setText("Username : ");

        txtUsername.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(72, 89, 4));
        txtUsername.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        txtUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsernameActionPerformed(evt);
            }
        });

        btnClear1.setBackground(new java.awt.Color(89, 116, 69));
        btnClear1.setFont(new java.awt.Font("Bookman Old Style", 1, 16)); // NOI18N
        btnClear1.setForeground(new java.awt.Color(231, 240, 220));
        btnClear1.setText("Clear");
        btnClear1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        btnClear1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClear1MouseClicked(evt);
            }
        });
        btnClear1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClear1ActionPerformed(evt);
            }
        });

        btnUpdate.setBackground(new java.awt.Color(89, 116, 69));
        btnUpdate.setFont(new java.awt.Font("Bookman Old Style", 1, 16)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(231, 240, 220));
        btnUpdate.setText("Update");
        btnUpdate.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 2, 1, new java.awt.Color(153, 153, 153)));
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnUpdateMouseClicked(evt);
            }
        });
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 987, Short.MAX_VALUE)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFullname)
                    .addComponent(lblEmpID)
                    .addComponent(txtEmployeeID, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPhoneNo)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmergency)
                    .addComponent(txtEmergency, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddLine3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAddLine4, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAddress)
                    .addComponent(lblUserN)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                        .addComponent(txtGender, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblGrossSalary, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblGender, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtGrossSalary))
                                    .addComponent(lblBankAcc)
                                    .addComponent(txtBankAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtJobTitle)
                                    .addComponent(txtOverview)
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblOverview)
                                            .addComponent(lblPass))
                                        .addGap(0, 117, Short.MAX_VALUE))
                                    .addComponent(txtPass)))
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(lblPosition)
                                    .addComponent(txtPosition, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(lblDepartment)
                                    .addComponent(lblMyKad)
                                    .addComponent(txtMyKad))
                                .addGap(40, 40, 40)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                                        .addComponent(lblJobTitle)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(lblStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtStartDate))
                                        .addGap(18, 18, 18)
                                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(mainPanelLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(lblEndDate))
                                            .addComponent(txtEndDate)))
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addComponent(lblCompany)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtCompany)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFullname)
                    .addComponent(lblMyKad))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtFullname, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCompany, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCompany))
                        .addGap(13, 13, 13))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMyKad, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStartDate)
                    .addComponent(lblEndDate)
                    .addComponent(lblPosition)
                    .addComponent(lblUserN))
                .addGap(12, 12, 12)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPosition, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmpID, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblJobTitle)
                        .addComponent(lblDepartment)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeeID, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtJobTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPhoneNo)
                    .addComponent(lblOverview)
                    .addComponent(lblGrossSalary))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(txtOverview)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblPass)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(90, 90, 90)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnClear1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(136, 136, 136))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGrossSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEmergency)
                            .addComponent(lblGender))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtEmergency, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtGender, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAddress)
                            .addComponent(lblBankAcc))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAddLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBankAcc, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddLine3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAddLine4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 989, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 598, Short.MAX_VALUE)
        );

        pack();
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

    
    private void txtMyKadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMyKadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMyKadActionPerformed

    private void txtFullnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFullnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFullnameActionPerformed

    private void txtEmployeeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeeIDActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_txtEmployeeIDActionPerformed

    private void txtEmergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmergencyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmergencyActionPerformed

    private void txtAddLine2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddLine2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddLine2ActionPerformed

    private void txtPhoneNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneNoActionPerformed

    private void txtAddLine3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddLine3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddLine3ActionPerformed

    private void txtAddLine4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddLine4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddLine4ActionPerformed

    private void txtAddLine1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAddLine1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAddLine1ActionPerformed

    private void txtPositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPositionActionPerformed

    private void txtBankAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBankAccActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBankAccActionPerformed

    private void txtDepartmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDepartmentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDepartmentActionPerformed

    private void txtStartDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStartDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStartDateActionPerformed

    private void txtGrossSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGrossSalaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGrossSalaryActionPerformed

    private void txtCompanyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCompanyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCompanyActionPerformed

    private void txtJobTitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJobTitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtJobTitleActionPerformed

    private void txtOverviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOverviewActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOverviewActionPerformed

    private void txtEndDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEndDateActionPerformed

    private void txtGenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGenderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGenderActionPerformed

    private void btnClear1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClear1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClear1MouseClicked

    private void btnClear1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClear1ActionPerformed
        // TODO add your handling code here:
        // Clear all text fields
        txtFullname.setText("");
        txtMyKad.setText("");
        txtUsername.setText("");
        txtEmployeeID.setText("");
        txtPhoneNo.setText("");
        txtEmergency.setText("");
        txtAddLine1.setText("");
        txtAddLine2.setText("");
        txtAddLine3.setText("");
        txtAddLine4.setText("");
        txtBankAcc.setText("");
        txtPosition.setText("");
        txtDepartment.setText("");
        txtGrossSalary.setText("");
        txtGender.setText("");
        txtCompany.setText("");
        txtStartDate.setText("");
        txtEndDate.setText("");
        txtJobTitle.setText("");
        txtOverview.setText("");
        txtPass.setText("");
    }

        // Employee class to hold employee data
        class Employee {
        private String employeeID;
        private String username;
        private String fullname;
        private String myKad;
        private String phoneno;
        private String emergency;
        private String addLine1, addLine2, addLine3, addLine4;
        private String bankAcc;
        private String position;
        private String department;
        private String grossSalary;
        private String gender;
        private String company;
        private String startDate;
        private String endDate;
        private String jobTitle;
        private String overview;

        // Getters and Setters for each field
        public String getEmployeeID() { return employeeID; }
        public void setEmployeeID(String employeeID) { this.employeeID = employeeID; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getFullname() { return fullname; }
        public void setFullname(String fullname) { this.fullname = fullname; }
        public String getMyKad() { return myKad; }
        public void setMyKad(String myKad) { this.myKad = myKad; }
        public String getPhoneno() { return phoneno; }
        public void setPhoneno(String phoneno) { this.phoneno = phoneno; }
        public String getEmergency() { return emergency; }
        public void setEmergency(String emergency) { this.emergency = emergency; }
        public String getAddLine1() { return addLine1; }
        public void setAddLine1(String addLine1) { this.addLine1 = addLine1; }
        public String getAddLine2() { return addLine2; }
        public void setAddLine2(String addLine2) { this.addLine2 = addLine2; }
        public String getAddLine3() { return addLine3; }
        public void setAddLine3(String addLine3) { this.addLine3 = addLine3; }
        public String getAddLine4() { return addLine4; }
        public void setAddLine4(String addLine4) { this.addLine4 = addLine4; }
        public String getBankAcc() { return bankAcc; }
        public void setBankAcc(String bankAcc) { this.bankAcc = bankAcc; }
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        public String getDepartment() { return department; }
        public void setDepartment(String department) { this.department = department; }
        public String getGrossSalary() { return grossSalary; }
        public void setGrossSalary(String grossSalary) { this.grossSalary = grossSalary; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getJobTitle() { return jobTitle; }
        public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
        public String getOverview() { return overview; }
        public void setOverview(String overview) { this.overview = overview; }
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new Loginpage1().setVisible(true);
                //new Registerpage1().setVisible(true);
                //new AdminPage().setVisible(true);
            }
        });
    }//GEN-LAST:event_btnClear1ActionPerformed

    private void txtPassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPassActionPerformed

    private void btnUpdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnUpdateMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateMouseClicked

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        // Collect updated data from form fields
        String currentEmployeeID = txtEmployeeID.getText(); // Get the current email (if you need to identify the employee)
        String employeeID = txtEmployeeID.getText();
        String username = txtUsername.getText();
        
        if (txtEmployeeID.getText().isEmpty() || txtUsername.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "EmployeeID or Username field is empty.");
            return;
        }

        String fullname = txtFullname.getText();
        String mykad = txtMyKad.getText();
        String phoneno = txtPhoneNo.getText();
        String emergency = txtEmergency.getText();
        String addline1 = txtAddLine1.getText();
        String addline2 = txtAddLine2.getText();
        String addline3 = txtAddLine3.getText();
        String addline4 = txtAddLine4.getText();
        String bankacc = txtBankAcc.getText();
        String position = txtPosition.getText();
        String department = txtDepartment.getText();
        String grosssalary = txtGrossSalary.getText();
        String gender = txtGender.getText();
        String company = txtCompany.getText();
        String startdate = txtStartDate.getText();
        String enddate = txtEndDate.getText();
        String jobtitle = txtJobTitle.getText();
        String overview = txtOverview.getText();
        String password = txtPass.getText();

        // Validate all fields are filled
        if (fullname.isEmpty() || username.isEmpty() || mykad.isEmpty() || employeeID.isEmpty() || phoneno.isEmpty() || emergency.isEmpty() ||
            addline1.isEmpty() || bankacc.isEmpty() || position.isEmpty() ||
            department.isEmpty() || grosssalary.isEmpty() || gender.isEmpty() || company.isEmpty() ||
            startdate.isEmpty() || jobtitle.isEmpty() || overview.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields.");
            return;
        }

        // Prepare the updated employee data string
        String updatedEmployeeData = String.format("""
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
                                                   Status: %s
                                                   """,
            employeeID, username, fullname, mykad, phoneno, emergency, addline1, addline2, addline3, addline4,
            bankacc, position, department, grosssalary, gender, company, startdate, enddate, jobtitle, overview, "Active"
        );

        // Update Employee_data.txt
        File employeeDataFile = new File("src/txtFile/Employee_data.txt");
        File tempEmployeeFile = new File("src/txtFile/Employee_data_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(employeeDataFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempEmployeeFile))) {

            String line;
            boolean employeeFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("EmployeeID: " + currentEmployeeID) || line.startsWith("Username: " + txtUsername.getText())) {
                // Match found, replace existing employee data
                writer.write(updatedEmployeeData); // Write updated data
                writer.flush();
                // Skip lines of the existing employee data
                for (int i = 0; i < 21; i++) {
                    reader.readLine(); // skip the next 18 lines after the match line
                }
                employeeFound = true;
                } else {
                    // Preserve other employee data
                    writer.write(line + "\n");
                }
            }

            if (!employeeFound) {
                JOptionPane.showMessageDialog(null, "Employee not found.");
                return;
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error updating employee data: " + ex.getMessage());
            ex.printStackTrace(); // Log the exception for debugging
            return;
        }

        // Delete the original Employee_data.txt file and rename the temporary file
        try {
            Files.deleteIfExists(employeeDataFile.toPath()); // Delete original file if exists
            if (!tempEmployeeFile.renameTo(employeeDataFile)) {
                throw new IOException("Could not rename temporary file to original file.");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error renaming temporary file: " + ex.getMessage());
            ex.printStackTrace(); // Log the exception for debugging
            return;
        }

        // Update user credentials in user_credentials.txt
        File credentialsFile = new File("src/txtFile/user_credentials.txt");
        File tempCredentialsFile = new File("src/txtFile/user_credentials_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempCredentialsFile))) {

            String line;
            boolean userFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("EmployeeID: " + currentEmployeeID)) {
                    writer.write(String.format("EmployeeID: %s\nUsername: %s\nPassword: %s\nPosition: %s\nStatus: %s\nFailedAttempts: %s\n\n",
                        employeeID, txtUsername.getText(), password, position, "Active","0"));
                    writer.flush();
                    reader.readLine(); // skip username line
                    reader.readLine(); // skip password line
                    reader.readLine(); // skip role line
                    reader.readLine(); // skip status line
                    reader.readLine(); // skip attempts line
                    userFound = true;
                } else {
                    // Preserve other user credentials
                    writer.write(line + "\n");
                }
            }

            if (!userFound) {
                JOptionPane.showMessageDialog(null, "User not found.");
                return;
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error updating user credentials: " + ex.getMessage());
            ex.printStackTrace(); // Log the exception for debugging
            return;
        }

        // Delete the original user_credentials.txt file and rename the temporary file
        try {
            Files.deleteIfExists(credentialsFile.toPath()); // Delete original file if exists
            if (!tempCredentialsFile.renameTo(credentialsFile)) {
                throw new IOException("Could not rename temporary file to original file.");
            }
            JOptionPane.showMessageDialog(null, "Employee data and credentials updated successfully.");
            
            // Close the registration form
                this.dispose();
                
            // Navigate back to AdminPage
            AdminPage adminPage = new AdminPage(txtUsername.getText()); // Pass identifier if needed
            adminPage.setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error renaming temporary credentials file: " + ex.getMessage());
            ex.printStackTrace(); // Log the exception for debugging
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear1;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblBankAcc;
    private javax.swing.JLabel lblCompany;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmergency;
    private javax.swing.JLabel lblEmpID;
    private javax.swing.JLabel lblEndDate;
    private javax.swing.JLabel lblFullname;
    private javax.swing.JLabel lblGender;
    private javax.swing.JLabel lblGrossSalary;
    private javax.swing.JLabel lblJobTitle;
    private javax.swing.JLabel lblMyKad;
    private javax.swing.JLabel lblOverview;
    private javax.swing.JLabel lblPass;
    private javax.swing.JLabel lblPhoneNo;
    private javax.swing.JLabel lblPosition;
    private javax.swing.JLabel lblStartDate;
    private javax.swing.JLabel lblUserN;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField txtAddLine1;
    private javax.swing.JTextField txtAddLine2;
    private javax.swing.JTextField txtAddLine3;
    private javax.swing.JTextField txtAddLine4;
    private javax.swing.JTextField txtBankAcc;
    private javax.swing.JTextField txtCompany;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtEmergency;
    private javax.swing.JTextField txtEmployeeID;
    private javax.swing.JTextField txtEndDate;
    private javax.swing.JTextField txtFullname;
    private javax.swing.JTextField txtGender;
    private javax.swing.JTextField txtGrossSalary;
    private javax.swing.JTextField txtJobTitle;
    private javax.swing.JTextField txtMyKad;
    private javax.swing.JTextField txtOverview;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtPosition;
    private javax.swing.JTextField txtStartDate;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
