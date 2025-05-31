package SalesManager.Functions;

import SalesManager.DataHandlers.SupplierFileHandler;
import java.io.IOException;
import java.util.List;
import model.Supplier;

public class supplierFunction {
    
    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        
        public ValidationResult(boolean valid, String message) {
            this.valid = valid;
            this.message = message;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
    public static class ListResult {
        private final boolean success;
        private final String message;
        private final List<Supplier> suppliers;
        
        public ListResult(boolean success, String message, List<Supplier> suppliers) {
            this.success = success;
            this.message = message;
            this.suppliers = suppliers;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public List<Supplier> getSuppliers() {
            return suppliers;
        }
    }

    public static class SupplierResult {
        private final boolean success;
        private final String message;
        private final Supplier supplier;
        
        public SupplierResult(boolean success, String message, Supplier supplier) {
            this.success = success;
            this.message = message;
            this.supplier = supplier;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        public Supplier getSupplier() {
            return supplier;
        }
    }
    
//    validates supplier input data
    public static ValidationResult validateSupplierInput(String name, String contact) {
        // Check if fields are empty
        if (name == null || name.trim().isEmpty() || contact == null || contact.trim().isEmpty()) {
            return new ValidationResult(false, "Please fill all fields.");
        }
        
        // Validate contact number format (10 or 11 digits)
        if (!contact.trim().matches("^\\d{10,11}$")) {
            return new ValidationResult(false, 
                "Invalid contact number!\n" +
                "Format: 10 or 11 digits\n" +
                "Example: 0123456789 or 01234567890");
        }
        
        return new ValidationResult(true, "Valid input");
    }
    
    public static boolean isSupplierNameDuplicate(String name, String currentSupplierID) throws IOException {
        return SupplierFileHandler.isSupplierNameDuplicate(name, currentSupplierID);
    }
    
    public static class OperationResult {
        private final boolean success;
        private final String message;
        
        public OperationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
    }
    
     public static OperationResult addSupplier(String name, String contact) {
        try {
            // Validate input
            ValidationResult validation = validateSupplierInput(name, contact);
            if (!validation.isValid()) {
                return new OperationResult(false, validation.getMessage());
            }
            
            // Check for duplicate name
            if (isSupplierNameDuplicate(name.trim(), "")) {
                return new OperationResult(false, "Duplicate supplier name detected!");
            }
            
            // Create and save supplier
            Supplier newSupplier = new Supplier(
                SupplierFileHandler.generateSupplierID(),
                name.trim(),
                contact.trim(),
                true 
            );
            
            SupplierFileHandler.saveSupplier(newSupplier);
            return new OperationResult(true, "Supplier added successfully!");
            
        } catch (IOException e) {
            return new OperationResult(false, "Error saving supplier: " + e.getMessage());
        }
    }
     
     public static OperationResult updateSupplier(String supplierID, String name, String contact, boolean active) {
        try {
            // Validate input
            ValidationResult validation = validateSupplierInput(name, contact);
            if (!validation.isValid()) {
                return new OperationResult(false, validation.getMessage());
            }
            
            // Check for duplicate name (excluding current supplier)
            if (isSupplierNameDuplicate(name.trim(), supplierID)) {
                return new OperationResult(false, "Duplicate supplier name detected!");
            }
            
            // Update supplier
            SupplierFileHandler.updateSupplier(supplierID, name.trim(), contact.trim(), active);
            return new OperationResult(true, "Supplier updated successfully!");
            
        } catch (IOException e) {
            return new OperationResult(false, "Error updating supplier: " + e.getMessage());
        }
    }
     
     public static OperationResult deleteSupplier(String supplierID) {
        try {
            SupplierFileHandler.deleteSupplier(supplierID);
            return new OperationResult(true, "Supplier deleted successfully!");
        } catch (IOException e) {
            return new OperationResult(false, "Error deleting supplier: " + e.getMessage());
        }
     }
     
     public static ListResult getAllSuppliers() {
        try {
            List<Supplier> suppliers = SupplierFileHandler.readSuppliersFromFile("src/txtFile/suppliers.txt");
            return new ListResult(true, "Suppliers loaded successfully", suppliers);
        } catch (IOException e) {
            return new ListResult(false, "Error loading suppliers: " + e.getMessage(), null);
        }
    }

    public static SupplierResult getSupplierById(String supplierID) {
        try {
            Supplier supplier = SupplierFileHandler.getSupplierById(supplierID);
            if (supplier != null) {
                return new SupplierResult(true, "Supplier found", supplier);
            } else {
                return new SupplierResult(false, "Supplier not found", null);
            }
        } catch (IOException e) {
            return new SupplierResult(false, "Error loading supplier: " + e.getMessage(), null);
        }
    }
    
    public static OperationResult deleteSupplierWithConfirmation(String supplierID) {
        try {
            SupplierFileHandler.deleteSupplier(supplierID);
            return new OperationResult(true, "Supplier deleted successfully!");
        } catch (IOException e) {
            return new OperationResult(false, "Error deleting supplier: " + e.getMessage());
        }
    }
}
