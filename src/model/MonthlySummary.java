/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author charl
 */
public class MonthlySummary {
    private int totalPO;
    private double totalPayment;
    private int pendingPO;

    public MonthlySummary(int totalPO, double totalPayment, int pendingPO) {
        this.totalPO = totalPO;
        this.totalPayment = totalPayment;
        this.pendingPO = pendingPO;
    }

    public int getTotalPO() {
        return totalPO;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public int getPendingPO() {
        return pendingPO;
    }
}
