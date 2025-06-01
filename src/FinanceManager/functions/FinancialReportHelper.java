/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.io.BufferedReader;
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
import javax.swing.JOptionPane;

/**
 *
 * @author charl
 */
public class FinancialReportHelper {
    public static FinancialSummary loadSummary(String filePath) {
        FinancialSummary summary = new FinancialSummary();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                summary.totalPO++;

                String[] parts = line.split(", ");
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
                    case "Paid" -> summary.totalPayment += totalPrice;
                    case "Verified" -> summary.outstandingAmount += totalPrice;
                    case "Pending" -> summary.pendingApprovals++;
                    case "Rejected" -> summary.rejectedOrders++;
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to read " + filePath);
            e.printStackTrace();
        }

        return summary;
    }

    public static LinkedHashMap<String, Integer> getTopItems(String filePath) {
        Map<String, Integer> itemQuantities = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] fields = line.split(", ");
                String item = "";
                int quantity = 0;
                String status = "";

                try {
                    for (String field : fields) {
                        if (field.startsWith("Item Name:")) {
                            item = field.split(": ")[1].trim();
                        } else if (field.startsWith("Quantity:")) {
                            quantity = Integer.parseInt(field.split(": ")[1].trim());
                        } else if (field.startsWith("Status:")) {
                            status = field.split(": ")[1].trim();
                        }
                    }

                    if (status.equalsIgnoreCase("Paid")) {
                        itemQuantities.put(item, itemQuantities.getOrDefault(item, 0) + quantity);
                    }

                } catch (Exception e) {
                    System.out.println("Error, can't fetch the data.");
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading " + filePath);
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


    public static Set<String> extractMonths(String filePath) {
        Set<String> months = new TreeSet<>(Comparator.reverseOrder());
        DateTimeFormatter fileFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMMM yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");
                for (String part : parts) {
                    if (part.startsWith("Date:")) {
                        String dateStr = part.split(":")[1].trim();
                        try {
                            LocalDate date = LocalDate.parse(dateStr, fileFormat);
                            months.add(date.format(displayFormat));
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to read months from file.");
        }

        return months;
    }
    
    public static FinancialSummary filterByMonth(String filePath, String selectedMonth) {
        FinancialSummary summary = new FinancialSummary();
        DateTimeFormatter fileFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter displayFormat = DateTimeFormatter.ofPattern("MMMM yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
                    } else if (part.startsWith("Item Name:")) {
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

                if (selectedMonth.equals("All Months") || poMonth.equals(selectedMonth)) {
                    summary.totalPO++;
                    if (status.equalsIgnoreCase("Paid")) {
                        summary.topItems.put(item, summary.topItems.getOrDefault(item, 0) + quantity);
                    }


                    switch (status) {
                        case "Paid" -> summary.totalPayment += totalPrice;
                        case "Verified" -> summary.outstandingAmount += totalPrice;
                        case "Pending" -> summary.pendingApprovals++;
                        case "Rejected" -> summary.rejectedOrders++;
                    }
                }
            }

            // Keep only top 3 items
            summary.topItems = summary.topItems.entrySet()
                    .stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(3)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error filtering data.");
            e.printStackTrace();
        }

        return summary;
    }

}
