package model;

import java.util.List;

public class PurchaseOrder {
    private String poID;
    private String supplierID;
    private String supplierName;
    private String itemID;
    private String item;
    private String quantity;
    private String unitPrice;
    private String totalPrice;
    private String date;
    private String status;  
    private List<Item> items; // for inventory manager role
    private String flagReason;

    public PurchaseOrder(String poID, String supplierID, String supplierName, String itemID, String item, String quantity, String unitPrice, String totalPrice, String date, String status, String flagReason) {
        this.poID = poID;
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.itemID = itemID;
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.date = date;
        this.status = status;
        this.flagReason = flagReason;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // Getters
    public String getPoID() {
        return poID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getItemID() {
        return itemID;
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

    public List<Item> getItems() {
        return items;
    }
}
