/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.models;

/**
 *
 * @author reymy
 */

import java.util.Date;
import java.util.List;

public class PurchaseOrder {
    // Fields for the Purchase Order
    private String poID;
    private String supplier;
    private Date date;
    private String status; // example: "Approved", "Pending", "Received"
    private List<Item> items; // The list of items ordered in this PO

    // Constructor
    public PurchaseOrder(String poID, String supplier, Date date, String status, List<Item> items) {
        this.poID = poID;
        this.supplier = supplier;
        this.date = date;
        this.status = status;
        this.items = items;
    }

    // Getters and Setters
    public String getPoID() {
        return poID;
    }

    public void setPoID(String poID) {
        this.poID = poID;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    //A method to update status after verifying received items
    public void verifyAndUpdateStatus() {
        // Implementation logic to update stock based on received items goes here
        // For example, if all items are marked as received, update status to "Received"
    }

    // Optional: Override toString() for debugging 
    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "poID='" + poID + '\'' +
                ", supplier='" + supplier + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", items=" + items +
                '}';
    }
}

