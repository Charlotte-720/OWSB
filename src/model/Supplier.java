
package model;

public class Supplier {
    private String supplierID;
    private String supplierName;
    private String contactNo;
    private boolean isActive;
    
    //Constructor
    public Supplier(String supplierID, String supplierName, String contactNo, boolean isActive) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contactNo = contactNo;
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
        ", active=" + isActive +
        '}';
}
}
