package SalesManager;

import SalesManager.Actions.TableActionCellEditor;
import SalesManager.Actions.TableActionCellRender;
import SalesManager.Actions.TableActionEvent;
import SalesManager.Functions.prFunction;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;


public class PROperations extends javax.swing.JFrame {
    private String currentSalesManagerID;
    private prFunction prFunc;
    private List<String[]> allPRRecords = new ArrayList<>(); 
    
    public PROperations(String salesManagerID) {
        this.currentSalesManagerID = salesManagerID;
        this.prFunc = new prFunction();
        initComponents();
        initializeUI();
        date.setText(LocalDate.now().toString());
    }
    
    private void initializeUI() {
        loadPurchaseRequisitions();
        setupActionColumn();
        setupSearchFunction(); 
    }
        
    private void setupActionColumn() {
        // Set up the table action event
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void editButton(int row) {
                handleEditPR(row);
            }

            @Override
            public void deleteButton(int row) {
                handleDeletePR(row);
            }
        };
        
        // Set up action column (index 9)
        int actionColumnIndex = 9; // "Actions" is the 10th column (index 9)
        if (prTable.getColumnCount() > actionColumnIndex) {
            prTable.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(new TableActionCellRender());
            prTable.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new TableActionCellEditor(event));
        } else {
            System.err.println("Table doesn't have enough columns for actions");
        }
    }
    
    private void setupSearchFunction() {
        searchField.getDocument().addDocumentListener(new DocumentListener() {
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
        String searchText = searchField.getText().trim();
        
        try {
            List<String[]> filteredRecords = prFunc.searchPRRecords(allPRRecords, searchText);
            populatePRTable(filteredRecords);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error performing search: " + e.getMessage());
            populatePRTable(allPRRecords);
        }
    }
   
    private void populatePRTable(List<String[]> prRecords) {
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        model.setRowCount(0);
        
        List<Object[]> tableData = prFunc.processRecordsForTable(prRecords);
        
        for (Object[] rowData : tableData) {
            model.addRow(rowData);
        }
    }
    
    private void loadPurchaseRequisitions() {
        try {
            DefaultTableModel model = (DefaultTableModel) prTable.getModel();
            model.setRowCount(0);

            List<String[]> records = prFunc.loadPurchaseRequisitions();
            this.allPRRecords = new ArrayList<>(records);
            List<Object[]> tableData = prFunc.processRecordsForTable(records);

            for (Object[] rowData : tableData) {
                model.addRow(rowData);
            }

        } catch (IOException e) {
            handleException("Error loading Purchase Requisitions", e);
        }
    }
    
    private String getPRIDFromTable(int row) {
        DefaultTableModel model = (DefaultTableModel) prTable.getModel();
        Object value = model.getValueAt(row, 0);
        return value != null ? value.toString() : null;
    }
    
    private void handleEditPR(int row) {
        try {
            String prID = getPRIDFromTable(row);
            if (prID == null) {
                showErrorMessage("No PR ID found at the selected row");
                return;
            }
            
            // Load PR data using business logic
            String[] prData = prFunc.loadPRData(prID);
            if (prData == null || prData.length < 12) {
                showErrorMessage("Could not retrieve PR data for PR ID: " + prID);
                return;
            }
            
            // Determine PR type and open appropriate edit form
            String prType = prFunc.determinePRType(prData);
            openEditForm(prID, prType);
            
        } catch (IOException e) {
            handleException("Error loading PR data", e);
        } catch (Exception e) {
            handleException("Error opening Edit form", e);
        }
    }
    
    private void openEditForm(String prID, String prType) {
        String salesManagerID = getCurrentSalesManagerID();
        
        if ("NEW_ITEM".equalsIgnoreCase(prType)) {
            openEditPRNewItem(prID, salesManagerID);
        } else {
            openEditPRRestock(prID, salesManagerID);
        }
    }
    
    private void openEditPRNewItem(String prID, String salesManagerID) {
        EditPRNewItem editForm = new EditPRNewItem(prID, salesManagerID);
        editForm.setVisible(true);
        editForm.addWindowListener(createRefreshTableListener());
    }
    
    private void openEditPRRestock(String prID, String salesManagerID) {
        EditPRRestock editForm = new EditPRRestock(prID, salesManagerID);
        editForm.setVisible(true);
        editForm.addWindowListener(createRefreshTableListener());
    }
     
    private void handleDeletePR(int row) {
        try {
            if (prTable.isEditing()) {
                prTable.getCellEditor().stopCellEditing();
            }
            
            String prID = getPRIDFromTable(row);
            if (prID == null) {
                showErrorMessage("No PR ID found at the selected row");
                return;
            }
            
            // Delegate to prFunction
            prFunc.handleDeletePROperation(prID, 
                this::confirmDeletion, 
                this::showSuccessMessage, 
                this::showErrorMessage,
                this::loadPurchaseRequisitions);
            
        } catch (Exception e) {
            handleException("Error during deletion", e);
        }
    }
    
    private boolean confirmDeletion(String prID) {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Delete Purchase Requisition " + prID + "?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION
        );
        return confirm == JOptionPane.YES_OPTION;
    }
    
    private void performDeletion(String prID) throws IOException {
        boolean deleted = SalesManager.DataHandlers.PRFileHandler.deletePurchaseRequisition(prID);
        
        if (deleted) {
            loadPurchaseRequisitions(); 
            showSuccessMessage("Purchase Requisition deleted successfully");
        } else {
            showErrorMessage("Failed to delete Purchase Requisition");
        }
    }
     
    private java.awt.event.WindowAdapter createRefreshTableListener() {
        return new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadPurchaseRequisitions();
            }
        };
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleException(String context, Exception e) {
        e.printStackTrace();
        showErrorMessage(context + ": " + e.getMessage());
    }
    
    private String getCurrentSalesManagerID() {
        return this.currentSalesManagerID != null ? this.currentSalesManagerID : "DEFAULT_SM";
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        RestockExistingItemButton = new javax.swing.JButton();
        PRPanel = new javax.swing.JPanel();
        PRTitle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        clearButton = new javax.swing.JButton();
        date = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        prTable = new javax.swing.JTable();
        PurchaseNewItemButton = new javax.swing.JButton();
        SearchLabel = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(221, 246, 246));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        RestockExistingItemButton.setBackground(new java.awt.Color(255, 255, 204));
        RestockExistingItemButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        RestockExistingItemButton.setText("Restock Existing Item");
        RestockExistingItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestockExistingItemButtonActionPerformed(evt);
            }
        });

        PRPanel.setBackground(new java.awt.Color(204, 255, 204));

        PRTitle.setFont(new java.awt.Font("Times New Roman", 3, 36)); // NOI18N
        PRTitle.setText("Purchase Requisitions");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel1.setText("X");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout PRPanelLayout = new javax.swing.GroupLayout(PRPanel);
        PRPanel.setLayout(PRPanelLayout);
        PRPanelLayout.setHorizontalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addContainerGap(267, Short.MAX_VALUE)
                .addComponent(PRTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(209, 209, 209)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        PRPanelLayout.setVerticalGroup(
            PRPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PRPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(90, 90, 90))
            .addGroup(PRPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(PRTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dateLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        dateLabel.setText("Date");

        clearButton.setBackground(new java.awt.Color(255, 255, 153));
        clearButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        clearButton.setText("Clear");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        date.setText(" ");

        prTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "PR ID", "Item Name", "Quantity", "Price", "Total Price", "Required Date", "Supplier ID ", "Delivery Date", "Status", "Actions"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        prTable.setEditingColumn(4);
        prTable.setRowHeight(40);
        jScrollPane1.setViewportView(prTable);

        PurchaseNewItemButton.setBackground(new java.awt.Color(255, 255, 204));
        PurchaseNewItemButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        PurchaseNewItemButton.setText("Purchase New Items");
        PurchaseNewItemButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PurchaseNewItemButtonActionPerformed(evt);
            }
        });

        SearchLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        SearchLabel.setText("Search");

        searchField.setText(" ");
        searchField.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PRPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(SearchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(clearButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(RestockExistingItemButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PurchaseNewItemButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(10, 10, 10))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(PRPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PurchaseNewItemButton)
                    .addComponent(date)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RestockExistingItemButton)
                    .addComponent(SearchLabel)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void RestockExistingItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestockExistingItemButtonActionPerformed
       String salesManagerID = getCurrentSalesManagerID();
        AddPRRestock addPRDialog = new AddPRRestock(salesManagerID);
        addPRDialog.setVisible(true);
        addPRDialog.addWindowListener(createRefreshTableListener());
    }//GEN-LAST:event_RestockExistingItemButtonActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void PurchaseNewItemButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PurchaseNewItemButtonActionPerformed
        String salesManagerID = getCurrentSalesManagerID();
        AddPRNewItem addPRDialog = new AddPRNewItem(salesManagerID);
        addPRDialog.setVisible(true);
        addPRDialog.addWindowListener(createRefreshTableListener());
    }//GEN-LAST:event_PurchaseNewItemButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearButton.addActionListener(e -> {
        searchField.setText("");
        searchField.requestFocus();
        });
    }//GEN-LAST:event_clearButtonActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PRPanel;
    private javax.swing.JLabel PRTitle;
    private javax.swing.JButton PurchaseNewItemButton;
    private javax.swing.JButton RestockExistingItemButton;
    private javax.swing.JLabel SearchLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JLabel date;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable prTable;
    private javax.swing.JTextField searchField;
    // End of variables declaration//GEN-END:variables
}
