/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InventoryManager.models;

/**
 *
 * @author reymy
 */

public class Item {
    // Fields for the item
    private String itemCode;
    private String itemName;
    private String category;
    private int quantity;
    private int threshold;

    // Constructor
    public Item(String itemCode, String itemName, String category, int quantity, int threshold) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.threshold = threshold;
    }

    // Getters and Setters
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    //A helper method to check if the item is low on stock
    public boolean isLowStock() {
        return quantity <= threshold;
    }

    //Override toString() for debugging or display
    @Override
    public String toString() {
        return "Item{" +
                "itemCode='" + itemCode + '\'' +
                ", itemName='" + itemName + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", threshold=" + threshold +
                '}';
    }
}

