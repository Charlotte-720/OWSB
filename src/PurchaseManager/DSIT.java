package PurchaseManager;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DSIT {        
    public static void loadSupplierData(JTable table) {
        // Define columns for suppliers
        String[] columnNames = {"Supplier ID", "Name", "Contact", "Is Active"};

        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Suppliers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming Suppliers.txt stores data in CSV format like:
                // SupplierID,Name,Contact,IsActive
                String[] values = line.split(",");
                if (values.length >= 4) {
                    Object[] row = new Object[4];
                    for (int i = 0; i < 4; i++) {
                        row[i] = values[i].trim();
                    }
                    rows.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert List to 2D Object array for table model
        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        // Create model with non-editable cells
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Set model on the table
        table.setModel(model);
    }
    
    public static void loadItemData(JTable table){
        
        String[] columnNames = {"Item ID", "Item Name", "Price", "Category", "Expired Date", "Suppliers ID", "Total Stock", "Updated Date"};

        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming Suppliers.txt stores data in CSV format like:
                // SupplierID,Name,Contact,IsActive
                String[] values = line.split(",");
                if (values.length >= 8) {
                    Object[] row = new Object[8];
                    for (int i = 0; i < 8; i++) {
                        row[i] = values[i].trim();
                    }
                    rows.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Convert List to 2D Object array for table model
        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        // Create model with non-editable cells
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Set model on the table
        table.setModel(model);
    }
    
        public static void loadRequisitionData(JTable table){
            
            String[] columnNames = {"PR ID", "Item ID", "Item Name", "Quantity", "Price", "Total Amount", "Suppliers ID", "Delivery Date", "Status"};

                    List<Object[]> rows = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("pr.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Assuming Suppliers.txt stores data in CSV format like:
                    // SupplierID,Name,Contact,IsActive
                    String[] values = line.split(",");
                    if (values.length >= 9) {
                        Object[] row = new Object[9];
                        for (int i = 0; i < 9; i++) {
                            row[i] = values[i].trim();
                        }
                        rows.add(row);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            // Convert List to 2D Object array for table model
            Object[][] data = new Object[rows.size()][];
            for (int i = 0; i < rows.size(); i++) {
                data[i] = rows.get(i);
            }

            // Create model with non-editable cells
            DefaultTableModel model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Set model on the table
            table.setModel(model);
        }
}
