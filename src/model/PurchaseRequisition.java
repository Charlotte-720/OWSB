package model;

import model.PRItem;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class PurchaseRequisition {
    private String prID;
    private String raisedBySMID; // Reference to a User object instead of just ID
    private LocalDate requestDate; 
    private LocalDate requiredDeliveryDate;
    private String status;
    private List<PRItem> items;
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    public PurchaseRequisition(String prID, String raisedBy,
            LocalDate requestDate, LocalDate requiredDeliveryDate, 
            String status, List<PRItem> items) {
        this.prID = prID;
        this.raisedBySMID = raisedBy;
        this.requestDate = requestDate;
        this.requiredDeliveryDate = requiredDeliveryDate;
        this.status = status;
        this.items = items != null ? items : new ArrayList<>();
    }
    

    // Getters and Setters
    public String getPrID() {
        return prID; 
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

