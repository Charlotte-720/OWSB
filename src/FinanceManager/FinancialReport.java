/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package FinanceManager;

import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 *
 * @author charl
 */
public class FinancialReport extends javax.swing.JFrame {

    /**
     * Creates new form FinancialReport
     */ 
    private LinkedHashMap<String, Integer> topItems;
    
    public FinancialReport() {
        initComponents();
        loadDataFromPOFile();
        topItems = getTopPurchasedItems();
        ((BarChartPanel) barChartPanel).setData(topItems);
        populateMonthComboBox();
        comboMonthFilter.addActionListener(e -> applyMonthFilter());

    }
    
    private void loadDataFromPOFile() {
        int totalPO = 0;
        int pendingApprovals = 0;
        int rejectedOrders = 0;
        double totalPayment = 0;
        double outstandingAmount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseManager/po.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                totalPO++; 

                String[] parts = line.split(",");
                String status = "";
                double totalPrice = 0;

                for (String part : parts) {
                    part = part.trim();

                    if (part.startsWith("Total Price:")) {
                        try {
                            totalPrice = Double.parseDouble(part.split(":")[1].trim());
                        } catch (NumberFormatException e) {
                            totalPrice = 0;
                        }
                    } else if (part.startsWith("Status:")) {
                        status = part.split(":")[1].trim();
                    }
                }

                switch (status) {
                    case "Paid":
                        totalPayment += totalPrice;
                        break;
                    case "Approved":
                        outstandingAmount += totalPrice;
                        break;
                    case "Pending":
                        pendingApprovals++;
                        break;
                    case "Rejected":
                        rejectedOrders++;
                        break;
                }
            }

            TotalPO.setText(String.valueOf(totalPO));
            TotalPayment.setText("RM " + String.format("%.2f", totalPayment));
            OutstandingAmount.setText("RM " + String.format("%.2f", outstandingAmount));
            PendingApprovals.setText(String.valueOf(pendingApprovals));
            RejectedOrder.setText(String.valueOf(rejectedOrders));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read po.txt.");
            e.printStackTrace();
        }
    }
    
    public void exportToPNG(Component panel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PNG Report");
        fileChooser.setSelectedFile(new File("FinancialReport.png"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            panel.paint(g2);
            g2.dispose();

            try {
                ImageIO.write(image, "png", fileToSave);
                JOptionPane.showMessageDialog(this, "Report saved as PNG:\n" + fileToSave.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to save PNG.");
            }
        }
    }

    public void exportToPDF(Component panel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setSelectedFile(new File("FinancialReport.pdf"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try {
                // 1. Capture panel as image
                BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                panel.paint(g2);
                g2.dispose();

                // 2. Convert BufferedImage to iText Image
                Image pdfImage = Image.getInstance(image, null);

                // 3. Set document size based on image size
                float width = pdfImage.getWidth();
                float height = pdfImage.getHeight();
                Document doc = new Document(new Rectangle(width, height), 0, 0, 0, 0);
                PdfWriter.getInstance(doc, new FileOutputStream(fileToSave));
                doc.open();

                // 4. Position image (optional centering)
                pdfImage.setAbsolutePosition(0, 0);
                doc.add(pdfImage);

                doc.close();

                JOptionPane.showMessageDialog(this, "Report saved as PDF:\n" + fileToSave.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Failed to save PDF.");
            }
        }
    }


    private LinkedHashMap<String, Integer> getTopPurchasedItems() {
        Map<String, Integer> itemQuantities = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("po.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] fields = line.split(", ");
                    String item = fields[2].split(": ")[1];
                    int quantity = Integer.parseInt(fields[3].split(": ")[1]);

                    itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + quantity);

                } catch (Exception e) {
                    System.out.println("Skipping malformed line: " + line);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading po.txt for item count.");
            e.printStackTrace();
        }

        return itemQuantities.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }

    private void populateMonthComboBox() {
        comboMonthFilter.removeAllItems();
        comboMonthFilter.addItem("All Months");

        Set<String> months = new TreeSet<>(Comparator.reverseOrder()); // latest first
        DateTimeFormatter fileFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMMM yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseManager/po.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                for (String part : parts) {
                    if (part.startsWith("Date:")) {
                        String dateStr = part.split(":")[1].trim();
                        try {
                            LocalDate date = LocalDate.parse(dateStr, fileFormat);
                            months.add(date.format(displayFormat));
                        } catch (Exception e) {
                            System.out.println("Skipping invalid date: " + dateStr);
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to read dates from po.txt.");
            e.printStackTrace();
        }

        for (String month : months) {
            comboMonthFilter.addItem(month);
        }
    }

    private void applyMonthFilter() {
        String selected = (String) comboMonthFilter.getSelectedItem();
        if (selected == null) return;

        int totalPO = 0;
        int pendingApprovals = 0;
        int rejectedOrders = 0;
        double totalPayment = 0;
        double outstandingAmount = 0;
        Map<String, Integer> itemQuantities = new HashMap<>();

        DateTimeFormatter fileFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMMM yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader("src/PurchaseManager/po.txt"))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(", ");
                String dateStr = "", item = "", status = "";
                double totalPrice = 0;
                int quantity = 0;

                for (String part : parts) {
                    part = part.trim();

                    if (part.startsWith("Date:")) {
                        dateStr = part.split(":")[1].trim();
                    } else if (part.startsWith("Item:")) {
                        item = part.split(":")[1].trim();
                    } else if (part.startsWith("Quantity:")) {
                        quantity = Integer.parseInt(part.split(":")[1].trim());
                    } else if (part.startsWith("Total Price:")) {
                        totalPrice = Double.parseDouble(part.split(":")[1].trim());
                    } else if (part.startsWith("Status:")) {
                        status = part.split(":")[1].trim();
                    }
                }

                LocalDate date;
                try {
                    date = LocalDate.parse(dateStr, fileFormat);
                } catch (Exception e) {
                    continue;
                }

                String poMonth = date.format(displayFormat);

                // Only count matching month or all
                if (selected.equals("All Months") || poMonth.equals(selected)) {
                    totalPO++;
                    itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + quantity);

                    switch (status) {
                        case "Paid": totalPayment += totalPrice; break;
                        case "Approved": outstandingAmount += totalPrice; break;
                        case "Pending": pendingApprovals++; break;
                        case "Rejected": rejectedOrders++; break;
                    }
                }
            }

            // Update labels
            TotalPO.setText(String.valueOf(totalPO));
            TotalPayment.setText("RM " + String.format("%.2f", totalPayment));
            OutstandingAmount.setText("RM " + String.format("%.2f", outstandingAmount));
            PendingApprovals.setText(String.valueOf(pendingApprovals));
            RejectedOrder.setText(String.valueOf(rejectedOrders));

            // Update bar chart (top 3)
            topItems = itemQuantities.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));

            ((BarChartPanel) barChartPanel).setData(topItems);
            barChartPanel.repaint();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error filtering data.");
            e.printStackTrace();
        }
    }




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        reportPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        TotalPO = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        TotalPayment = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        PendingApprovals = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        OutstandingAmount = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        RejectedOrder = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        barChartPanel = new BarChartPanel(null);
        jLabel12 = new javax.swing.JLabel();
        btnGenerate = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        comboMonthFilter = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 253, 247));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));
        jPanel1.setOpaque(false);

        reportPanel.setBackground(new java.awt.Color(255, 253, 236));
        reportPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Financial Report");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Total Purchase Order: ");

        TotalPO.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TotalPO.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Total Payment: ");

        TotalPayment.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        TotalPayment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setText("Pending Approvals: ");

        PendingApprovals.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PendingApprovals.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Rejected Orders: ");

        OutstandingAmount.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        OutstandingAmount.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Outstanding Amount: ");

        RejectedOrder.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        RejectedOrder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jPanel2.setBackground(new java.awt.Color(255, 226, 226));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel3.setText("Top 3 Purchased Items:");

        barChartPanel.setMaximumSize(new java.awt.Dimension(300, 140));

        javax.swing.GroupLayout barChartPanelLayout = new javax.swing.GroupLayout(barChartPanel);
        barChartPanel.setLayout(barChartPanelLayout);
        barChartPanelLayout.setHorizontalGroup(
            barChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        barChartPanelLayout.setVerticalGroup(
            barChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 219, Short.MAX_VALUE))
                    .addComponent(barChartPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addContainerGap(70, Short.MAX_VALUE)
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(reportPanelLayout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(RejectedOrder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(reportPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PendingApprovals, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(reportPanelLayout.createSequentialGroup()
                                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(TotalPayment, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(TotalPO, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                    .addComponent(OutstandingAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel4)))
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel1)))
                .addGap(70, 70, 70))
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(TotalPO, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, reportPanelLayout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addGap(18, 18, 18))
                            .addGroup(reportPanelLayout.createSequentialGroup()
                                .addComponent(OutstandingAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(PendingApprovals, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(TotalPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(RejectedOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel12.setText("X");
        jLabel12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel12MouseClicked(evt);
            }
        });

        btnGenerate.setBackground(new java.awt.Color(255, 226, 226));
        btnGenerate.setText("Export Report");
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(255, 255, 204));
        btnCancel.setForeground(new java.awt.Color(51, 51, 51));
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        comboMonthFilter.setBackground(new java.awt.Color(255, 207, 207));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(141, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(comboMonthFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(btnGenerate)
                        .addGap(53, 53, 53)
                        .addComponent(btnCancel))
                    .addComponent(reportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(127, 127, 127)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addContainerGap(574, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(reportPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGenerate)
                    .addComponent(btnCancel)
                    .addComponent(comboMonthFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
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

        setSize(new java.awt.Dimension(800, 625));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel12MouseClicked
        // TODO add your handling code here:
        this.dispose();
        FinanceManagerPanel fmg = new FinanceManagerPanel("Unexpected position value: ");
        fmg.setVisible(true);
    }//GEN-LAST:event_jLabel12MouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        this.dispose();
        FinanceManagerPanel fmg = new FinanceManagerPanel("exampleFinanceManager");
        fmg.setVisible(true);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // TODO add your handling code here:
        FinancialReportFormat dialog = new FinancialReportFormat(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        String format = dialog.getSelectedFormat();

        if (format == null) return;

        if (format.equals("PDF")) {
            exportToPDF(reportPanel);  // or pass a report panel
        } else if (format.equals("PNG")) {
            exportToPNG(reportPanel);
        }
    }//GEN-LAST:event_btnGenerateActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel OutstandingAmount;
    private javax.swing.JLabel PendingApprovals;
    private javax.swing.JLabel RejectedOrder;
    private javax.swing.JLabel TotalPO;
    private javax.swing.JLabel TotalPayment;
    private javax.swing.JPanel barChartPanel;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JComboBox<String> comboMonthFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel reportPanel;
    // End of variables declaration//GEN-END:variables
}
