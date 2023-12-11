package model;

import java.io.Serializable;

public class Order implements Serializable {
    private  String itemName;
    private  String nameOnCard;
    private int quantities;
    private Double unitPrice;
    private String unitOfMeasure;
    private  double totalPrice;
    private  String cardNum;
    private String date;
    public Order(){};
    public Order(String itemName, String nameOnCard, int quantities, Double unitPrice,
                 String unitOfMeasure, double totalPrice, String cardNum,String date) {
        this.itemName = itemName;
        this.nameOnCard = nameOnCard;
        this.quantities = quantities;
        this.unitPrice = unitPrice;
        this.unitOfMeasure = unitOfMeasure;
        this.totalPrice = totalPrice;
        this.cardNum = cardNum;
        this.date = date;
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

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
