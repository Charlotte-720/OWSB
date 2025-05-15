/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PurchaseManager;

/**
 *
 * @author charl
 */
public class PurchaseOrder {
    private String poID;
    private String supplierName;
    private String item;
    private String quantity;
    private String date;
    private String status;
    
    public PurchaseOrder(String poID, String supplierName, String item, String quantity, String date, String status) {
        this.poID = poID;
        this.supplierName = supplierName;
        this.item = item;
        this.quantity = quantity;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
