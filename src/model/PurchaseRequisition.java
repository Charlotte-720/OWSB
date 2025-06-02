package model;

import model.PRItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class PurchaseRequisition {
    private String prID;
    private String prType;
    private String raisedBySMID; 
    private LocalDate requestDate; 
    private LocalDate requiredDeliveryDate;
    private String status;
    private List<PRItem> items;
    
    public static final String TYPE_RESTOCK = "RESTOCK";
    public static final String TYPE_NEW_ITEM = "NEW_ITEM";
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    // General constructor (modify your existing one)
    public PurchaseRequisition(String prID, String prType, String raisedBy,
            LocalDate requestDate, LocalDate requiredDeliveryDate, 
            String status, List<PRItem> items) {
        this.prID = prID;
        this.prType = prType;
        this.raisedBySMID = raisedBy;
        this.requestDate = requestDate;
        this.requiredDeliveryDate = requiredDeliveryDate;
        this.status = status;
        this.items = items != null ? items : new ArrayList<>();
    }
    
    // Constructor for RESTOCK - when user selects existing items
    public PurchaseRequisition(String prID, String raisedBy, 
            LocalDate requiredDeliveryDate, List<PRItem> existingItems) {
        this.prID = prID;
        this.prType = TYPE_RESTOCK;
        this.raisedBySMID = raisedBy;
        this.requestDate = LocalDate.now();
        this.requiredDeliveryDate = requiredDeliveryDate;
        this.status = STATUS_PENDING;
        this.items = existingItems != null ? existingItems : new ArrayList<>();
    }
    
    // Constructor for NEW_ITEM - when user enters new item details
    public PurchaseRequisition(String prID, String raisedBy, 
            LocalDate requiredDeliveryDate, PRItem newItem) {
        this.prID = prID;
        this.prType = TYPE_NEW_ITEM;
        this.raisedBySMID = raisedBy;
        this.requestDate = LocalDate.now();
        this.requiredDeliveryDate = requiredDeliveryDate;
        this.status = STATUS_PENDING;
        this.items = new ArrayList<>();
        if (newItem != null) {
            this.items.add(newItem);
        }
    }

    // Getters and Setters
    public String getPrID() {
        return prID; 
    }
    
    public String getPrType() {
        return prType;
    }
    
    public String getRaisedBy() { 
        return raisedBySMID; 
    }
    
    public LocalDate getRequestDate() { 
        return requestDate; 
    }
    
    public LocalDate getRequiredDeliveryDate() { 
        return requiredDeliveryDate; 
    }
    
    public List<PRItem> getItems() {
        return items; 
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setItems(List<PRItem> items) { 
        this.items = items; 
    }
    
    public void setPrType(String prType) {
        this.prType = prType;
    }
    
    public void setRequestDate(LocalDate date) { 
        this.requestDate = date; 
    }
    
    public void setRequiredDeliveryDate(LocalDate date) { 
        this.requiredDeliveryDate = date; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
}

