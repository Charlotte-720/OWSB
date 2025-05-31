package model;

import java.util.List;


public class PurchaseOrder {
    private String poID;
    private String supplierName;
    private String item;
    private String quantity;
    private String unitPrice;
    private String totalPrice;
    private String date;
    private String status;  
    private List<Item> items; // for inventory manager role
    private String flagReason;
    
    public PurchaseOrder(String poID, String supplierName, String item, String quantity, String unitPrice, String totalPrice, String date, String status, String flagReason) {
        this.poID = poID;
        this.supplierName = supplierName;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
        this.flagReason = flagReason;
    }
    
    //setter for IM
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPoID() {
        return poID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getItem() {
        return item;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }
    
    public String getTotalPrice() {
        return totalPrice;
    }
    
    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
    
    public String getFlagReason() {
        return flagReason;
    }
    
    // Add getters and setters for items
    public List<Item> getItems() {
        return items;
    }
    
    public void setItems(List<Item> items) {
        this.items = items;
    }
    
}
