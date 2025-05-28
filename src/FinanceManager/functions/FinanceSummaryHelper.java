/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author charl
 */
public class FinanceSummaryHelper {
    public static class MonthlySummary {
        public int totalPO;
        public double totalPayment;
        public int pendingPO;

        public MonthlySummary(int totalPO, double totalPayment, int pendingPO) {
            this.totalPO = totalPO;
            this.totalPayment = totalPayment;
            this.pendingPO = pendingPO;
        }
    }

    public static MonthlySummary loadSummaryFor(String selectedMonthYear) {
        int totalPO = 0;
        double totalPayment = 0;
        int pendingPO = 0;

        try {
            DateTimeFormatter fileDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate targetMonth = LocalDate.parse("01 " + selectedMonthYear, DateTimeFormatter.ofPattern("dd MMMM yyyy"));

            BufferedReader reader = new BufferedReader(new FileReader("src/txtFile/po.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                try {
                    String[] fields = line.split(", ");
                    String dateStr = fields[6].split(": ")[1];
                    LocalDate poDate = LocalDate.parse(dateStr, fileDateFormat);

                    if (poDate.getMonth() == targetMonth.getMonth() && poDate.getYear() == targetMonth.getYear()) {
                        totalPO++;
                        String totalPriceStr = fields[5].split(": ")[1];
                        String status = fields[7].split(": ")[1];
                        double totalPrice = Double.parseDouble(totalPriceStr);

                        if (status.equals("Paid")) totalPayment += totalPrice;
                        if (status.equals("Pending")) pendingPO++;
                    }
                } catch (Exception e) {
                    System.out.println("Skipping malformed line: " + line);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new MonthlySummary(totalPO, totalPayment, pendingPO);
    }
}
