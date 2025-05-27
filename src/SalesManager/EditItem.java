package SalesManager;

import model.Item;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;


public class EditItem extends javax.swing.JFrame {
    private String currentItemID;
    private ItemEntry entryWindow;
    private Item originalItem;
    private List<POItem> paidPOItems;
    private boolean isInitializing = false;
    
    // Inner class to represent PO items
   private static class POItem {
       String itemName;
       int itemQuantity;
       double itemPrice;
       String status;
       String supplierName;
       
       POItem(String itemName, int itemQuantity, double itemPrice, String status, String supplierName) {
           this.itemName = itemName;
           this.itemQuantity = itemQuantity;
           this.itemPrice = itemPrice;
           this.status = status;
           this.supplierName = supplierName;
       }
   }
   
    public EditItem() {
        initComponents();
    }
    
    public EditItem(String itemID) {
        initComponents();
        this.currentItemID = itemID;
        initializeData();
    }
    
    public EditItem(String itemID, ItemEntry entryWindow) {
        initComponents();
        this.currentItemID = itemID;
        this.entryWindow = entryWindow;
        initializeData();
    }
    
    private void initializeData() {
        try {
            paidPOItems = loadPaidPOItems();
            
            populateItemNames();
            populateItemCategories();
            loadItemForEditing();
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + e.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
            paidPOItems = new ArrayList<>();
        }
    }
    
    private void populateItemCategories() {
        String[] categories = {
            "Groceries",
            "Fresh Produce", 
            "Dairy Products",
            "Frozen Foods",
            "Meat & Seafood"
        };
        itemCategory.setModel(new DefaultComboBoxModel<>(categories));
    }
    
    private void populateItemNames() {
        List<String> itemNames = new ArrayList<>();
        
        // Add items from paid PO items
        for (POItem poItem : paidPOItems) {
            if (!itemNames.contains(poItem.itemName)) {
                itemNames.add(poItem.itemName);
            }
        }
        
        // Sort the list for better user experience
        itemNames.sort(String::compareToIgnoreCase);
        
        // Create combo box model
        DefaultComboBoxModel<String> itemNameModel = new DefaultComboBoxModel<>(
            itemNames.toArray(new String[0])
        );
        itemName.setModel(itemNameModel);
        
        for (var listener : itemName.getActionListeners()) {
            itemName.removeActionListener(listener);
        }
        itemName.addActionListener(e -> {
            if (!isInitializing) {
                onItemNameChanged();
            }
        });
    }
    
    private void onItemNameChanged() {
        String selectedItemName = (String) itemName.getSelectedItem();
        if (selectedItemName != null) {
            updateFieldsFromPOItem(selectedItemName);
        }
    }
    
    private void updateFieldsFromPOItem(String selectedItemName) {
        for (POItem poItem : paidPOItems) {
            if (poItem.itemName.equals(selectedItemName)) {
                itemPrice.setText(String.format("%.2f", poItem.itemPrice));
                itemQuantity.setText(String.valueOf(poItem.itemQuantity));
                supplierName.setText(poItem.supplierName);
                break;
            }
        }
    }
    
    private void loadItemForEditing() {
        if (currentItemID == null) {
            return;
        }

        try {
            Item item = FileHandler.getItemById(currentItemID);
            if (item != null) {
                this.originalItem = item;
                populateItemFields(item);
                saveButton.setText("Update");
            } else {
                JOptionPane.showMessageDialog(this, "Item not found!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading item data: " + e.getMessage(), 
                "File Error", 
                JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }
    
     private void populateItemFields(Item item) {
        itemName.setSelectedItem(item.getItemName());
        itemCategory.setSelectedItem(item.getCategory());
        expiredDate.setText(item.getExpiredDate().toString());
        updateFieldsFromPOItem(item.getItemName());
    }
    
    // Method to load items with PAID status from po.txt
    private List<POItem> loadPaidPOItems() throws IOException {
        List<POItem> paidItems = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/po.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                try {
                    String itemName = extractValue(line, "Item:");
                    String quantityStr = extractValue(line, "Quantity:");
                    String priceStr = extractValue(line, "Unit Price:");
                    String status = extractValue(line, "Status:");
                    String supplierName = extractValue(line, "Supplier Name:");

                    int itemQuantity = Integer.parseInt(quantityStr.trim());
                    double itemPrice = Double.parseDouble(priceStr.trim());

                    if ("Paid".equalsIgnoreCase(status.trim())) {
                        POItem newItem = new POItem(itemName.trim(), itemQuantity, itemPrice, 
                                                  status.trim(), supplierName.trim());
                        paidItems.add(newItem);
                    }

                } catch (Exception e) {
                    continue;
                }
            }
        }

        return paidItems;
    }

    private String extractValue(String line, String label) {
        int startIndex = line.indexOf(label);
        if (startIndex == -1) {
            throw new RuntimeException("Label '" + label + "' not found in line: " + line);
        }

        startIndex += label.length();

        // Skip any whitespace after the label
        while (startIndex < line.length() && Character.isWhitespace(line.charAt(startIndex))) {
            startIndex++;
        }

        int endIndex = line.indexOf(",", startIndex);

        String value;
        if (endIndex == -1) {
            // Last value in the line
            value = line.substring(startIndex).trim();
        } else {
            value = line.substring(startIndex, endIndex).trim();
        }

        return value;
    }
    
    private boolean isDuplicateItemExcludingCurrent(String itemNameToCheck, String currentItemID) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String existingItemID = extractItemIdFromLine(line);
                String existingItemName = extractItemNameFromLine(line);

                if (existingItemID != null && existingItemName != null) {
                    if (existingItemID.equals(currentItemID)) {
                        continue;
                    }
                    if (existingItemName.equalsIgnoreCase(itemNameToCheck.trim())) {
                        return true; 
                    }
                }
            }
        } catch (IOException e) {
            return true; 
        }
        return false; 
    }
    
    private String extractItemNameFromLine(String line) {
        try {
            String pattern = "Item Name: ";
            int startIndex = line.indexOf(pattern);
            if (startIndex != -1) {
                startIndex += pattern.length();
                int endIndex = line.indexOf(",", startIndex);
                if (endIndex != -1) {
                    return line.substring(startIndex, endIndex).trim();
                }
            }
        } catch (Exception e) {
        
        }
        return null;
    }
    
    private String extractFieldFromLine(String line, String fieldName) {
        try {
            String pattern = fieldName + ": ";
            int startIndex = line.indexOf(pattern);
            if (startIndex != -1) {
                startIndex += pattern.length();
                int endIndex = line.indexOf(",", startIndex);
                if (endIndex != -1) {
                    return line.substring(startIndex, endIndex).trim();
                } else {
                    return line.substring(startIndex).trim();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    private boolean verifyItemExists(String itemID) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/items.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String fileItemID = extractItemIdFromLine(line);
                if (fileItemID != null && fileItemID.equals(itemID.trim())) {
                    return true;
                }
            }
        } catch (IOException e) {
        }
        return false;
    }
    private void handleItemUpdate() {
        try {
            if (!validateInputs()) {
                return;
            }

            Item updatedItem = createItemFromInputs();

            if (isDuplicateItemExcludingCurrent(updatedItem.getItemName(), currentItemID)) {
                showError("Item name already exists");
                return;
            }

            updateItemInFile(updatedItem);

            if (entryWindow != null) {
                entryWindow.loadItemsToTable();
            }

            showSuccess("Item updated successfully!");
            this.dispose();

        } catch (Exception e) {
            showError("Error updating item: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private Item createItemFromInputs() {
        String selectedItemName = (String) itemName.getSelectedItem();
        String supplierID = originalItem.getSupplierID(); 
        for (POItem poItem : paidPOItems) {
            if (poItem.itemName.equals(selectedItemName)) {
                supplierID = poItem.supplierName;
                break;
            }
        }
        double price = Double.parseDouble(itemPrice.getText().trim());
        int quantity = Integer.parseInt(itemQuantity.getText().trim());
        LocalDate expDate = LocalDate.parse(expiredDate.getText().trim());
        
        return new Item(
            currentItemID,                              // Keep existing ID
            selectedItemName,                           // Name from combo box
            price,                                      // Price from PO item
            itemCategory.getSelectedItem().toString(),  // Category from combo box
            expDate,                                    // Expiry from text field
            supplierID,                                 // Supplier from PO item
            quantity,                                   // Quantity from PO item
            LocalDate.now()                            // Updated date
        );
    }
    
    private boolean validateInputs() {
        // Validate item name
        if (itemName.getSelectedItem() == null || itemName.getSelectedItem().toString().trim().isEmpty()) {
            showError("Please select an item name");
            return false;
        }

        // Validate category
        if (itemCategory.getSelectedItem() == null) {
            showError("Please select a category");
            return false;
        }

        // Validate expired date
        if (validateExpiredDate() == null) return false;

        // Validate that we have PO item data
        if (itemPrice.getText().trim().isEmpty()) {
            showError("Price information is missing");
            return false;
        }

        if (itemQuantity.getText().trim().isEmpty()) {
            showError("Quantity information is missing");
            return false;
        }
        
        // Validate price format
        try {
            Double.parseDouble(itemPrice.getText().trim());
        } catch (NumberFormatException e) {
            showError("Invalid price format");
            return false;
        }

        // Validate quantity format
        try {
            Integer.parseInt(itemQuantity.getText().trim());
        } catch (NumberFormatException e) {
            showError("Invalid quantity format");
            return false;
        }

        return true;
    }

    private LocalDate validateExpiredDate() {
        try {
            String dateText = expiredDate.getText().trim();
            if (dateText.isEmpty()) {
                showError("Please enter expired date");
                return null;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate expired = LocalDate.parse(dateText, formatter);
            LocalDate today = LocalDate.now();

            if (expired.isBefore(today)) {
                showError("Expired date cannot be in the past!");
                return null;
            }
            return expired;
        } catch (Exception e) {
            showError("Invalid date format. Please use YYYY-MM-DD");
            return null;
        }
    }

    private void updateItemInFile(Item updatedItem) throws IOException {
        List<String> lines = new ArrayList<>();
        boolean itemFound = false;
        String targetItemID = currentItemID.trim();

        // Read all lines from the file
        try (BufferedReader br = new BufferedReader(new FileReader("src/txtFile/items.txt"))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
            
            if (line.trim().isEmpty()) {
                lines.add(line);
                continue;
            }
            String fileItemID = extractItemIdFromLine(line);
                if (fileItemID != null && fileItemID.equals(targetItemID)) {
                // Create updated line in the same labeled format
                String newLine = String.format(
                    "Item ID: %s, Item Name: %s, Price: %.1f, Category: %s, Expired Date: %s, Supplier ID: %s, Total Stock: %d, Updated Date: %s",
                    updatedItem.getItemID(),
                    updatedItem.getItemName(),
                    updatedItem.getPrice(),
                    updatedItem.getCategory(),
                    updatedItem.getExpiredDate(),
                    updatedItem.getSupplierID(),
                    updatedItem.getTotalStock(),
                    updatedItem.getUpdatedDate()
                );
                lines.add(newLine);
                itemFound = true;
            } else {
                lines.add(line); // Keep original line
            }
        }
    }

        if (!itemFound) {
            System.err.println("ERROR: Item with ID '" + targetItemID + "' not found in file");
            throw new IOException("Item with ID " + targetItemID + " not found in file");
        }

        // Write all lines back to the file
        try (FileWriter writer = new FileWriter("src/txtFile/items.txt")) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        }

        System.out.println("File updated successfully");
    }
    
    private String extractItemIdFromLine(String line) {
    try {
        String pattern = "Item ID: ";
        int startIndex = line.indexOf(pattern);
        if (startIndex != -1) {
            startIndex += pattern.length();
            int endIndex = line.indexOf(",", startIndex);
            if (endIndex != -1) {
                return line.substring(startIndex, endIndex).trim();
            } else {
                return line.substring(startIndex).trim();
            }
        }
    } catch (Exception e) {
        System.err.println("Error extracting Item ID from line: " + line);
        e.printStackTrace();
    }
    return null;
}

    private void clearFields() {
        if (itemName.getItemCount() > 0) {
            itemName.setSelectedIndex(0);
        }
        itemPrice.setText("");
        itemQuantity.setText("");
        if (itemCategory.getItemCount() > 0) {
            itemCategory.setSelectedIndex(0);
        }
        expiredDate.setText("");
        supplierName.setText("");
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        QuantityLabel = new javax.swing.JLabel();
        CategoryLabel = new javax.swing.JLabel();
        ExpiredDateLabel = new javax.swing.JLabel();
        SupplierLabel = new javax.swing.JLabel();
        PriceLabel = new javax.swing.JLabel();
        ClearButton = new javax.swing.JButton();
        ItemEntryPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        NameLabel = new javax.swing.JLabel();
        expiredDate = new javax.swing.JTextField();
        itemName = new javax.swing.JComboBox<>();
        itemCategory = new javax.swing.JComboBox<>();
        supplierName = new javax.swing.JLabel();
        itemPrice = new javax.swing.JLabel();
        itemQuantity = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        QuantityLabel.setText("Quantity");

        CategoryLabel.setText("Category");

        ExpiredDateLabel.setText("Expired Date");

        SupplierLabel.setText("Supplier Name");

        PriceLabel.setText("Price (RM)");

        ClearButton.setBackground(new java.awt.Color(153, 204, 255));
        ClearButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        ItemEntryPanel.setBackground(new java.awt.Color(255, 204, 153));

        jLabel1.setFont(new java.awt.Font("Sylfaen", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Item Entry");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel2.setText("X");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ItemEntryPanelLayout = new javax.swing.GroupLayout(ItemEntryPanel);
        ItemEntryPanel.setLayout(ItemEntryPanelLayout);
        ItemEntryPanelLayout.setHorizontalGroup(
            ItemEntryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemEntryPanelLayout.createSequentialGroup()
                .addContainerGap(83, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(jLabel2)
                .addContainerGap())
        );
        ItemEntryPanelLayout.setVerticalGroup(
            ItemEntryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ItemEntryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ItemEntryPanelLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        saveButton.setBackground(new java.awt.Color(255, 255, 102));
        saveButton.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        saveButton.setText("Update");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        NameLabel.setText("Name");

        expiredDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                expiredDateActionPerformed(evt);
            }
        });

        itemName.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        itemCategory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        itemCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemCategoryActionPerformed(evt);
            }
        });

        supplierName.setText(" ");

        itemPrice.setText(" ");

        itemQuantity.setText(" ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(SupplierLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(expiredDate, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                                    .addComponent(supplierName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(CategoryLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(QuantityLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(NameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(PriceLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(53, 53, 53)
                                                .addComponent(itemQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGap(52, 52, 52)
                                                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGap(53, 53, 53)
                                                    .addComponent(itemPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(1, 1, 1))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(60, 60, 60)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(ItemEntryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NameLabel)
                    .addComponent(itemName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PriceLabel)
                    .addComponent(itemPrice))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(QuantityLabel)
                    .addComponent(itemQuantity))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CategoryLabel)
                    .addComponent(itemCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(expiredDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExpiredDateLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(supplierName)
                    .addComponent(SupplierLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void expiredDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_expiredDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_expiredDateActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
         if (currentItemID == null) {
            showError("No item selected for editing");
            return;
    } else {
        handleItemUpdate();
    }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        clearFields();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel2MouseClicked

    private void itemCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemCategoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemCategoryActionPerformed
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
    
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CategoryLabel;
    private javax.swing.JButton ClearButton;
    private javax.swing.JLabel ExpiredDateLabel;
    private javax.swing.JPanel ItemEntryPanel;
    private javax.swing.JLabel NameLabel;
    private javax.swing.JLabel PriceLabel;
    private javax.swing.JLabel QuantityLabel;
    private javax.swing.JLabel SupplierLabel;
    private javax.swing.JTextField expiredDate;
    private javax.swing.JComboBox<String> itemCategory;
    private javax.swing.JComboBox<String> itemName;
    private javax.swing.JLabel itemPrice;
    private javax.swing.JLabel itemQuantity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel supplierName;
    // End of variables declaration//GEN-END:variables
}
