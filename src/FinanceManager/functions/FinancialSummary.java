/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FinanceManager.functions;

import java.util.LinkedHashMap;

/**
 *
 * @author charl
 */
public class FinancialSummary {
    public int totalPO;
    public int pendingApprovals;
    public int rejectedOrders;
    public double totalPayment;
    public double outstandingAmount;
    public LinkedHashMap<String, Integer> topItems;

    public FinancialSummary() {
        topItems = new LinkedHashMap<>();
    }
}
