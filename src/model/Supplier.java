
package model;

public class Supplier {
    private String supplierID;
    private String supplierName;
    private String contactNo;
    private String supplies;
    private boolean isActive;
    
    //Constructor
    public Supplier(String supplierID, String supplierName, String contactNo, String supplies, boolean isActive) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contactNo = contactNo;
        this.supplies = supplies;
        this.isActive = isActive;
    }
    //Getters
    public String getSupplierID(){
        return supplierID;
    }
    
    public String getSupplierName(){
        return supplierName;
    }
    
    public String getContactNo(){
        return contactNo;
    }
    
    public String getSupplies() {
        return supplies;
    }

    public boolean isActive() { 
        return isActive;
    }
    
    // Setters
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
//    @Override
//    public String toString() {
//        return supplierID;
//    }
    
@Override
public String toString() {
    return "Supplier{" +
        "supplierID='" + supplierID + '\'' +
        ", supplierName='" + supplierName + '\'' +
        ", contactNo='" + contactNo + '\'' +
        ", supplies='" + supplies + '\'' +
        ", active=" + isActive +
        '}';
}
}
