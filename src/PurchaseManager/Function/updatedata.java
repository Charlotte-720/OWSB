package PurchaseManager.Function;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class updatedata {   
    public static void loadSupplierData(JTable table) {
        String[] columnNames = {"Suppliers ID", "Suppliers Name", "Contact No","Suppliers","Active"};
        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/suppliers.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 5) {
                    Object[] row = new Object[5];
                    for (int i = 0; i < 5; i++) {
                        String[] parts = values[i].split(":", 2);
                        row[i] = parts.length == 2 ? parts[1].trim() : values[i].trim();
                    }
                    rows.add(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
    }
    
    public static void loadItemData(JTable table){
        
        String[] columnNames = {"Item ID", "Item Name", "Price", "Category", "Expired Date", "Suppliers ID", "Total Stock", "Updated Date"};

        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 8) {
                    Object[] row = new Object[8];
                    for (int i = 0; i < 8; i++) {
                        String[] parts = values[i].split(":", 2);
                        row[i] = parts.length == 2 ? parts[1].trim() : values[i].trim();                    
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
        String[] columnNames = {"PR ID","PR Type","Item ID", "Item Name", "Quantity", "Unit Price", "Total Price", "Suppliers ID", "Raised By","RD Date","R Date","Status"};

        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/pr.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming Suppliers.txt stores data in CSV format like:
                // SupplierID,Name,Contact,IsActive
                String[] values = line.split(",");
                if (values.length >= 12) {
                    Object[] row = new Object[12];
                    for (int i = 0; i < 12; i++) {
                        String[] parts = values[i].split(":", 2);
                        row[i] = parts.length == 2 ? parts[1].trim() : values[i].trim();                       }
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
        
    public static void loadstatusPOData(JTable table) {
        String[] columnNames = {"PO_ID", "Status"};
        List<Object[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/po.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String poId = "", status = "";

                for (String part : parts) {
                    if (part.startsWith("PO_ID:")) {
                        poId = part.substring("PO_ID:".length()).trim();
                    } else if (part.startsWith("Status:")) {
                        status = part.substring("Status:".length()).trim();
                    }
                }

                if (!poId.isEmpty() && !status.isEmpty()) {
                    rows.add(new Object[]{poId, status});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Object[][] data = new Object[rows.size()][];
        for (int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setModel(model);
        
        table.getColumnModel().getColumn(1).setCellRenderer(new StatusColumnCellRenderer());
    }
   
    // Custom renderer for Status column to color text based on status
    static class StatusColumnCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
               boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value != null) {
                String status = value.toString().toLowerCase();

                switch (status) {
                    case "rejected":
                        c.setForeground(Color.RED);
                        break;
                    case "pending":
                        c.setForeground(new Color(255, 165, 0)); // orange-ish yellow
                        break;
                    case "approved":
                        c.setForeground(new Color(0, 128, 0)); // dark green\
                        break;
                    case "received":
                        c.setForeground(new Color(0,102,255)); // dark green
                        break;
                    case "verified":
                        c.setForeground(new Color(255,0,255)); // dark green
                        break;
                    case "flagged":
                        c.setForeground(new Color(0,153,153)); // dark green
                        break;
                    default:
                        c.setForeground(Color.BLACK);
                        break;
                }
            } else {
                c.setForeground(Color.BLACK);
            }

            if (isSelected) {
                c.setBackground(table.getSelectionBackground());
            } else {
                c.setBackground(table.getBackground());
            }

            return c;
        }
    }
    
}
