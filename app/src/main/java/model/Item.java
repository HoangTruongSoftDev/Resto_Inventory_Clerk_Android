package model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Item implements Serializable {
    private int itemId;
    private String name;
    private int quantity;
    private String unitOfMeasure;
    private Double unitPrice;

    public Item(int itemId, String name, int quantity, String unitOfMeasure, Double unitPrice) {
        this.itemId = itemId;
        this.name = name;
        this.quantity = quantity;
        this.unitOfMeasure = unitOfMeasure;
        this.unitPrice = unitPrice;
    }

    public Item() {
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @NonNull
    @Override
    public String toString() {
        return "Item Id: " + itemId +" Item Name: " + name + " Quantity: " + quantity +
                " Unit of Measure: " + unitOfMeasure + " Unit Price: " + unitPrice;
    }
}
