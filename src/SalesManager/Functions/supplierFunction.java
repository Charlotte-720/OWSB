package SalesManager.Functions;

import SalesManager.DataHandlers.SupplierFileHandler;
import java.io.IOException;
import java.util.ArrayList;
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
    
//    search function
    public static ListResult searchSuppliers(String searchTerm) {
        try {
            List<Supplier> allSuppliers = SupplierFileHandler.readSuppliersFromFile("src/txtFile/suppliers.txt");
            List<Supplier> filteredSuppliers = new ArrayList<>();
            
            // If search term is empty or null, return all suppliers
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return new ListResult(true, "All suppliers loaded", allSuppliers);
            }
            
            String lowerSearchTerm = searchTerm.toLowerCase().trim();
            
            // Filter suppliers based on search term
            for (Supplier supplier : allSuppliers) {
                if (matchesSearchTerm(supplier, lowerSearchTerm)) {
                    filteredSuppliers.add(supplier);
                }
            }
            
            String message = filteredSuppliers.isEmpty() 
                ? "No suppliers found matching: " + searchTerm
                : filteredSuppliers.size() + " supplier(s) found matching: " + searchTerm;
            
            return new ListResult(true, message, filteredSuppliers);
            
        } catch (IOException e) {
            return new ListResult(false, "Error searching suppliers: " + e.getMessage(), null);
        }
    }
    
    private static boolean matchesSearchTerm(Supplier supplier, String searchTerm) {
        // Search in Supplier ID
        if (supplier.getSupplierID() != null && 
            supplier.getSupplierID().toLowerCase().contains(searchTerm)) {
            return true;
        }
        
        // Search in Supplier Name
        if (supplier.getSupplierName() != null && 
            supplier.getSupplierName().toLowerCase().contains(searchTerm)) {
            return true;
        }
        
        // Search in Contact Number
        if (supplier.getContactNo() != null && 
            supplier.getContactNo().toLowerCase().contains(searchTerm)) {
            return true;
        }
        
        // Search in Active status
        String activeStatus = supplier.isActive() ? "active" : "inactive";
        if (activeStatus.contains(searchTerm)) {
            return true;
        }
        
        // Also check for boolean representations
        String booleanStatus = String.valueOf(supplier.isActive()).toLowerCase();
        if (booleanStatus.contains(searchTerm)) {
            return true;
        }
        
        return false;
    }
    
    public static ListResult searchSuppliersByField(String searchTerm, String searchField) {
        try {
            List<Supplier> allSuppliers = SupplierFileHandler.readSuppliersFromFile("src/txtFile/suppliers.txt");
            List<Supplier> filteredSuppliers = new ArrayList<>();
            
            // If search term is empty, return all suppliers
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return new ListResult(true, "All suppliers loaded", allSuppliers);
            }
            
            String lowerSearchTerm = searchTerm.toLowerCase().trim();
            String lowerSearchField = searchField.toLowerCase().trim();
            
            // Filter suppliers based on specific field
            for (Supplier supplier : allSuppliers) {
                boolean matches = false;
                
                switch (lowerSearchField) {
                    case "id":
                        matches = supplier.getSupplierID() != null && 
                                 supplier.getSupplierID().toLowerCase().contains(lowerSearchTerm);
                        break;
                    case "name":
                        matches = supplier.getSupplierName() != null && 
                                 supplier.getSupplierName().toLowerCase().contains(lowerSearchTerm);
                        break;
                    case "contact":
                        matches = supplier.getContactNo() != null && 
                                 supplier.getContactNo().toLowerCase().contains(lowerSearchTerm);
                        break;
                    case "status":
                        String activeStatus = supplier.isActive() ? "active" : "inactive";
                        String booleanStatus = String.valueOf(supplier.isActive()).toLowerCase();
                        matches = activeStatus.contains(lowerSearchTerm) || 
                                 booleanStatus.contains(lowerSearchTerm);
                        break;
                    default:
                        // If field not recognized, search all fields
                        matches = matchesSearchTerm(supplier, lowerSearchTerm);
                        break;
                }
                
                if (matches) {
                    filteredSuppliers.add(supplier);
                }
            }
            
            String message = filteredSuppliers.isEmpty() 
                ? "No suppliers found in " + searchField + " matching: " + searchTerm
                : filteredSuppliers.size() + " supplier(s) found in " + searchField + " matching: " + searchTerm;
            
            return new ListResult(true, message, filteredSuppliers);
            
        } catch (IOException e) {
            return new ListResult(false, "Error searching suppliers by field: " + e.getMessage(), null);
        }
    }
}
