package model;

import java.time.LocalDate;

public class PRItem {
    private String itemID;
    private String itemName;
    private int quantity;
    private String supplierID;
    private double unitPrice;
    private LocalDate requiredDeliveryDate;

    public PRItem(String itemID, int quantity, String itemName, String supplierID, double unitPrice, LocalDate requiredDeliveryDate) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.supplierID = supplierID; 
        this.unitPrice = unitPrice;
        this.requiredDeliveryDate = requiredDeliveryDate;
    }

    // Getters
    public String getItemID() { 
        return itemID; 
    }
    
    public int getQuantity() {
        return quantity; 
    }
    
    public String getItemName() {
        return itemName;
    }
    
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    public String getSupplierID() {
        return supplierID; 
    }
    
    public LocalDate getRequiredDeliveryDate(){
        return requiredDeliveryDate;
    }
    
    public double getUnitPrice() {
        return unitPrice;
    }
     
    public double getTotalPrice() {
        return unitPrice * quantity;
    }
    
    // Setters
    
    public void setItemID(String itemID) { 
        this.itemID = itemID;
    }
    
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.quantity = quantity; 
    }
    
    public void setUnitPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = price; 
    }
    
    public void setRequiredDeliveryDate(LocalDate date) {
        this.requiredDeliveryDate = date; 
    }
    
}    
