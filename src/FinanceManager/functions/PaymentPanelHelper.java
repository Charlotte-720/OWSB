/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import model.PurchaseOrder;

/**
 *
 * @author charl
 */
public class PaymentPanelHelper {
     public static ArrayList<PurchaseOrder> readPaidPOsFromFile(String path) {
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(", ");
                String poID = fields[0].split(": ")[1];
                String supplierName = fields[1].split(": ")[1];
                String item = fields[2].split(": ")[1];
                String quantity = fields[3].split(": ")[1];
                String unitPrice = fields[4].split(": ")[1];
                String totalPrice = fields[5].split(": ")[1];
                String date = fields[6].split(": ")[1];
                String status = fields[7].split(": ")[1];

                if ("Verified".equalsIgnoreCase(status)) {
                    PurchaseOrder po = new PurchaseOrder(poID, supplierName, item, quantity, unitPrice, totalPrice, date, status);
                    poList.add(po);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return poList;
    }

    public static void updatePOStatusToPaid(String path, String poIDToUpdate) {
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("PO_ID: " + poIDToUpdate)) {
                    String[] parts = line.split(", ");
                    if (parts.length >= 8) {
                        parts[7] = "Status: Paid";
                        line = String.join(", ", parts);
                    }
                }
                updatedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
