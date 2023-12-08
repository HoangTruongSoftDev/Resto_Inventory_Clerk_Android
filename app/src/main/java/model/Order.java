package model;

import java.io.Serializable;

public class Order implements Serializable {
    private  String itemName;
    private  String nameOnCard;
    private  int orderId;
    private int quantities;
    private Double unitPrice;
    private String unitOfMeasure;
    private  double totalPrice;

    public Order(String itemName, String nameOnCard, int orderId, int quantities, Double unitPrice, String unitOfMeasure, double totalPrice) {
        this.itemName = itemName;
        this.nameOnCard = nameOnCard;
        this.orderId = orderId;
        this.quantities = quantities;
        this.unitPrice = unitPrice;
        this.unitOfMeasure = unitOfMeasure;
        this.totalPrice = totalPrice;
    }

    public int getQuantities() {
        return quantities;
    }

    public void setQuantities(int quantities) {
        this.quantities = quantities;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
