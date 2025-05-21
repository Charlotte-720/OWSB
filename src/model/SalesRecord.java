package model;

import java.time.LocalDate;

public class SalesRecord {
    private String saleID;
    private String itemID;       // Links to Item.itemID
    private int quantitySold;
    private LocalDate saleDate;
    private double totalAmount;  // quantitySold * itemPrice

    // Constructor
    public SalesRecord(String saleID, String itemID, int quantitySold, LocalDate saleDate, double itemPrice) {
        this.saleID = saleID;
        this.itemID = itemID;
        this.quantitySold = quantitySold;
        this.saleDate = saleDate;
        this.totalAmount = quantitySold * itemPrice;
    }

    // Getters
    public String getSaleID() { 
        return saleID; 
    }
    public String getItemID() { 
        return itemID; 
    }
    public int getQuantitySold() {
        return quantitySold; 
    }
    public LocalDate getSaleDate() { 
        return saleDate;
    }
    public double getTotalAmount() { 
        return totalAmount; 
    }
    
    public void setSaleID(String saleID) {
        this.saleID = saleID;
    }

    @Override
    public String toString() {
        return "SalesRecord{" +
                "saleID='" + saleID + '\'' +
                ", itemID='" + itemID + '\'' +
                ", quantitySold=" + quantitySold +
                ", saleDate=" + saleDate +
                ", totalAmount=" + totalAmount +
                '}';
    }
}