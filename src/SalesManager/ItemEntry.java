
package SalesManager;

import model.Item;
import SalesManager.Actions.TableActionEvent;
import SalesManager.Actions.TableActionCellRender;
import SalesManager.Actions.TableActionCellEditor;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ItemEntry extends javax.swing.JFrame {
private List<Item> allItems = new ArrayList<>();
    /**
     * Creates new form ItemEntryTest
     */
    public ItemEntry() {
        initComponents();
        try {
            allItems = FileHandler.loadItemsFromFile();
            populateTable(allItems);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading items: " + e.getMessage());
        }
        
        setupActionColumn();
        setupSearchFunction(); 
    }
    
    private void setupSearchFunction() {
        SearchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
        });
    }
    private void performSearch() {
        String searchText = SearchField.getText().trim().toLowerCase();

        if (searchText.isEmpty()) {
            // If search field is empty, show all items
            populateTable(allItems);
            return;
        }

        // Filter items based on search text
        List<Item> filteredItems = new ArrayList<>();

        for (Item item : allItems) {
            if (matchesSearchCriteria(item, searchText)) {
                filteredItems.add(item);
            }
        }

        // Update table with filtered results
        populateTable(filteredItems);
    }

    // New method to check if an item matches search criteria
    private boolean matchesSearchCriteria(Item item, String searchText) {
        // Check all fields of the item
        String[] searchableFields = {
            item.getItemID() != null ? item.getItemID().toLowerCase() : "",
            item.getItemName() != null ? item.getItemName().toLowerCase() : "",
            String.valueOf(item.getPrice()).toLowerCase(),
            item.getCategory() != null ? item.getCategory().toLowerCase() : "",
            item.getExpiredDate() != null ? item.getExpiredDate().toString().toLowerCase() : "",
            item.getSupplierID() != null ? item.getSupplierID().toLowerCase() : "",
            String.valueOf(item.getTotalStock()).toLowerCase(),
            item.getUpdatedDate() != null ? item.getUpdatedDate().toString().toLowerCase() : ""
        };

        // Check if search text is found in any field
        for (String field : searchableFields) {
            if (field.contains(searchText)) {
                return true;
            }
        }

        return false;
    }
    
    // Modified refreshTable method to maintain search results
    private void refreshTable() {
        try {
            allItems = FileHandler.loadItemsFromFile();
            // Re-apply current search if any
            performSearch();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error refreshing table: " + e.getMessage());
        }
    }

    // You can also add a clear search button method (optional)
    private void clearSearch() {
        SearchField.setText("");
        populateTable(allItems);
    }

    private String getFieldValueByIndex(Item item, int columnIndex) {
    switch (columnIndex) {
        case 0: return item.getItemID() != null ? item.getItemID() : "";
        case 1: return item.getItemName() != null ? item.getItemName() : "";
        case 2: return String.valueOf(item.getPrice());
        case 3: return item.getCategory() != null ? item.getCategory() : "";
        case 4: return item.getExpiredDate() != null ? item.getExpiredDate().toString() : "";
        case 5: return item.getSupplierID() != null ? item.getSupplierID() : "";
        case 6: return String.valueOf(item.getTotalStock());
        case 7: return item.getUpdatedDate() != null ? item.getUpdatedDate().toString() : "";
        default: return "";
    }
}

    // Separate method for edit action
    private void editItem(int row) {
        DefaultTableModel model = (DefaultTableModel) ItemTable.getModel();
        String itemID = (String) model.getValueAt(row, 0);

        EditItem editForm = new EditItem(itemID);
        editForm.setVisible(true);

        editForm.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                refreshTable();
            }
        });
    }

    // Separate method for delete action
    private void deleteItem(int row) {
        if (ItemTable.isEditing()) {
            ItemTable.getCellEditor().stopCellEditing();
        }

        DefaultTableModel model = (DefaultTableModel) ItemTable.getModel();
        String itemID = (String) model.getValueAt(row, 0);

        // Add confirmation dialog
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Delete item " + itemID + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            // Fix 3: Actually delete from file
            deleteFromFile(itemID);
        }
    }

    private void deleteFromFile(String itemID) {
            try {
                FileHandler.deleteItemFromFile(itemID);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error deleting item: " + e.getMessage());
            }
        }

    public void populateTable(List<Item> itemList) {
    DefaultTableModel model = (DefaultTableModel) ItemTable.getModel();
    model.setRowCount(0); // Clear existing rows

    for (Item item : itemList) {
        model.addRow(new Object[]{
            item.getItemID(),
            item.getItemName(),
            item.getPrice(),
            item.getCategory(),
            item.getExpiredDate(),
            item.getSupplierID(),
            item.getTotalStock(),
            item.getUpdatedDate(),
        });
    }
}
    
    private void setupActionColumn() {
    if (ItemTable.getColumnCount() >= 9) {
        ItemTable.getColumnModel().getColumn(8).setCellRenderer(new TableActionCellRender());
        ItemTable.getColumnModel().getColumn(8).setCellEditor(new TableActionCellEditor(new TableActionEvent() {
            @Override
            public void editButton(int row) {
                editItem(row);
            }
            
            @Override
            public void deleteButton(int row) {
                deleteItem(row);
            }
        }));
    }
}
    public void loadItemsToTable() {
        DefaultTableModel model = (DefaultTableModel) ItemTable.getModel();
        model.setRowCount(0); // clear existing data
        try {
            List<Item> items = FileHandler.loadAllItems(); // or from your source
            for (Item item : items) {
                Object[] rowData = {
                    item.getItemID(),
                    item.getItemName(),
                    item.getPrice(),
                    item.getCategory(),        
                    item.getExpiredDate(),
                    item.getSupplierID(),
                    item.getTotalStock(),       
                    item.getUpdatedDate()
                };
                model.addRow(rowData);
                }
        }catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage());
            e.printStackTrace();

        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ItemPanel = new javax.swing.JPanel();
        itemTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        SearchField = new javax.swing.JTextField();
        SearchLabel = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ItemTable = new javax.swing.JTable();
        clearButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(252, 238, 252));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setDoubleBuffered(false);

        ItemPanel.setBackground(new java.awt.Color(255, 204, 153));

        itemTitle.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        itemTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        itemTitle.setText("Item List");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ItemPanelLayout = new javax.swing.GroupLayout(ItemPanel);
        ItemPanel.setLayout(ItemPanelLayout);
        ItemPanelLayout.setHorizontalGroup(
            ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(itemTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(265, 265, 265)
                .addComponent(jLabel1)
                .addContainerGap())
        );
        ItemPanelLayout.setVerticalGroup(
            ItemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemPanelLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemPanelLayout.createSequentialGroup()
                .addGap(0, 38, Short.MAX_VALUE)
                .addComponent(itemTitle)
                .addGap(24, 24, 24))
        );

        SearchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchFieldActionPerformed(evt);
            }
        });

        SearchLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        SearchLabel.setText("Search");

        addButton.setBackground(new java.awt.Color(255, 153, 153));
        addButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        addButton.setText("Add Item");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        ItemTable.setBackground(new java.awt.Color(246, 230, 214));
        ItemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item ID", "Name", "Price", "Category", "Expired Date", "Supplier ID", "Total Stock", "Updated Date", "Actions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ItemTable.setRowHeight(40);
        jScrollPane1.setViewportView(ItemTable);

        clearButton.setBackground(new java.awt.Color(255, 255, 153));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(SearchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addButton)
                .addGap(15, 15, 15))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(ItemPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(ItemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(SearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addButton)
                        .addComponent(SearchLabel))
                    .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SearchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SearchFieldActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        AddItem addItemWindow  = new AddItem(this);
            addItemWindow .setVisible(true); 
    }//GEN-LAST:event_addButtonActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearButton.addActionListener(e -> {
            SearchField.setText("");
            SearchField.requestFocus();
        });
    }//GEN-LAST:event_clearButtonActionPerformed

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ItemEntry.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ItemEntry().setVisible(true);
//                
//            }
//        });
//    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ItemPanel;
    private javax.swing.JTable ItemTable;
    private javax.swing.JTextField SearchField;
    private javax.swing.JLabel SearchLabel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel itemTitle;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
