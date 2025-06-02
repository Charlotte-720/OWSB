/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package InventoryManager;
import javax.swing.JFrame;

import model.Item;
import InventoryManager.functions.InventoryService;
import InventoryManager.functions.ReportExporter;

import java.util.List;
import java.io.FileOutputStream;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author reymy
 */
public class GenerateStockReport extends javax.swing.JFrame {
    
    /**
     * Creates new form GenerateStockReport
     */
    public GenerateStockReport() {
        setUndecorated(true);
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        loadStockReportTable();
        setupFilterListeners();
    }
    
    private void setupFilterListeners() {
        filterTypeComboBox.addActionListener(e -> updateFilterValues());
        filterValueComboBox.addActionListener(e -> loadFilteredStockReport());
    }
    
    private void updateFilterValues() {
        String filterType = (String) filterTypeComboBox.getSelectedItem();
        filterValueComboBox.removeAllItems();  // Clear previous options

        if (filterType.equals("All")) {
            filterValueComboBox.setEnabled(false);  // No need for selection
            loadFilteredStockReport();  // Show all
        } else {
            filterValueComboBox.setEnabled(true);
            List<Item> items = InventoryService.loadItemsFromFile("src/txtFile/items.txt");

            if (filterType.equals("By Item")) {
                items.stream()
                    .map(Item::getItemName)
                    .distinct()
                    .forEach(filterValueComboBox::addItem);
            } else if (filterType.equals("By Supplier")) {
                items.stream()
                    .map(Item::getSupplierID)
                    .distinct()
                    .forEach(filterValueComboBox::addItem);
            }
        }
    }
    
    private void loadFilteredStockReport() {
        String filterType = (String) filterTypeComboBox.getSelectedItem();
        String selectedValue = (String) filterValueComboBox.getSelectedItem();

        List<Item> allItems = InventoryService.loadItemsFromFile("src/txtFile/items.txt");
        List<Item> filteredItems = new ArrayList<>();

        if (filterType.equals("All") || selectedValue == null) {
            filteredItems = allItems;
        } else if (filterType.equals("By Item")) {
            for (Item item : allItems) {
                if (item.getItemName().equalsIgnoreCase(selectedValue)) {
                    filteredItems.add(item);
                }
            }
        } else if (filterType.equals("By Supplier")) {
            for (Item item : allItems) {
                if (item.getSupplierID().equalsIgnoreCase(selectedValue)) {
                    filteredItems.add(item);
                }
            }
        }

        // Now populate the table
        DefaultTableModel model = (DefaultTableModel) stockReportTable.getModel();
        model.setRowCount(0);

        for (Item item : filteredItems) {
            String status = item.getTotalStock() < InventoryService.LOW_STOCK_THRESHOLD ? "LOW" : "Sufficient";
            Object[] row = {
                item.getItemID(),
                item.getItemName(),
                item.getCategory(),
                item.getTotalStock(),
                InventoryService.LOW_STOCK_THRESHOLD,
                status
            };
            model.addRow(row);
        }
    }

    private void loadStockReportTable() {
        List<Item> items = InventoryService.loadItemsFromFile("src/txtFile/items.txt");
        DefaultTableModel model = (DefaultTableModel) stockReportTable.getModel();
        model.setRowCount(0);

        for (Item item : items) {
            String status = item.getTotalStock() < InventoryService.LOW_STOCK_THRESHOLD ? "LOW" : "Sufficient";
            Object[] row = {
                item.getItemID(),
                item.getItemName(),
                item.getCategory(),
                item.getTotalStock(),
                InventoryService.LOW_STOCK_THRESHOLD, // Displayed as static value
                status
            };
            model.addRow(row);
        }
    }
    
    private List<Item> getItemsFromTable() {
        List<Item> itemList = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) stockReportTable.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            String itemID = model.getValueAt(i, 0).toString();
            String itemName = model.getValueAt(i, 1).toString();
            String category = model.getValueAt(i, 2).toString();
            int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());

            // You can leave price, supplier, date out since report doesn’t need them
            Item item = new Item(itemID, itemName, 0.0, category, null, null, quantity, null);
            itemList.add(item);
        }

        return itemList;
    }

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        closeButton = new javax.swing.JLabel();
        generateButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stockReportTable = new javax.swing.JTable();
        exportButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        filterTypeComboBox = new javax.swing.JComboBox<>();
        filterValueComboBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(232, 249, 255));
        jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        titleLabel.setFont(new java.awt.Font("Sylfaen", 3, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Generate Stock Report");

        closeButton.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        closeButton.setText("X");
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });

        generateButton.setBackground(new java.awt.Color(196, 217, 255));
        generateButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        generateButton.setText("Refresh");
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        stockReportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Item Code", "Item Name", "Category", "Quantity in Stock", "Threshold", "Status"
            }
        ));
        jScrollPane1.setViewportView(stockReportTable);

        exportButton.setBackground(new java.awt.Color(197, 186, 255));
        exportButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        exportButton.setText("Export");
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        backButton.setBackground(new java.awt.Color(212, 221, 238));
        backButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        filterTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "By Item", "By Supplier" }));
        filterTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterTypeComboBoxActionPerformed(evt);
            }
        });

        filterValueComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterValueComboBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("Filter by:");

        jLabel2.setText("Select Value:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 86, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filterValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(titleLabel)
                                .addGap(193, 193, 193)
                                .addComponent(closeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(titleLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(filterValueComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
        this.dispose();
    }//GEN-LAST:event_closeButtonMouseClicked

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed
        loadStockReportTable();
    }//GEN-LAST:event_generateButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        StockReportExportPopup popup = new StockReportExportPopup((java.awt.Frame) this, true);
        popup.setVisible(true);

        String format = popup.getSelectedFormat();
        if (format != null) {
            List<Item> currentTableItems = getItemsFromTable(); // get only filtered items

            try {
                switch (format) {
                    case "PDF":
                        ReportExporter.exportStockReportAsPDF(currentTableItems);
                        break;
                    case "PNG":
                        ReportExporter.exportStockReportAsPNG(currentTableItems);
                        break;
                    default:
                        javax.swing.JOptionPane.showMessageDialog(this, "Unsupported format.");
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to export: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        this.dispose();
        
    }//GEN-LAST:event_backButtonActionPerformed

    private void filterTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterTypeComboBoxActionPerformed

    private void filterValueComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterValueComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_filterValueComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JLabel closeButton;
    private javax.swing.JButton exportButton;
    private javax.swing.JComboBox<String> filterTypeComboBox;
    private javax.swing.JComboBox<String> filterValueComboBox;
    private javax.swing.JButton generateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable stockReportTable;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
