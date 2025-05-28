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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.PurchaseOrder;

/**
 *
 * @author charl
 */
public class ManagePOHelper {
    public static ArrayList<PurchaseOrder> readPOFile(String filePath) {
        ArrayList<PurchaseOrder> poList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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

                poList.add(new PurchaseOrder(poID, supplierName, item, quantity, unitPrice, totalPrice, date, status));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return poList;
    }

    public static Map<String, String> loadSupplierNamesWithSupplies(String filePath) {
        Map<String, String> supplierMap = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(", ");
                String name = "", active = "", supplies = "";

                for (String part : parts) {
                    if (part.startsWith("Supplier Name: ")) {
                        name = part.substring("Supplier Name: ".length()).trim();
                    } else if (part.startsWith("Supplies: ")) {
                        supplies = part.substring("Supplies: ".length()).trim();
                    } else if (part.startsWith("Active: ")) {
                        active = part.substring("Active: ".length()).trim();
                    }
                }

                if (active.equalsIgnoreCase("true") && !name.isEmpty()) {
                    supplierMap.put(name, supplies);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return supplierMap;
    }

    public static void savePOData(List<List<Object>> tableData, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (List<Object> row : tableData) {
                String line = String.format("PO_ID: %s, Supplier Name: %s, Item: %s, Quantity: %s, Unit Price: %s, Total Price: %s, Date: %s, Status: %s",
                        row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5), row.get(6), row.get(7));
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
