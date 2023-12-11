package model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.security.PrivateKey;

public class Threshold {
    private int itemId;
    private  int minQuantity;
    private boolean isAutoOrder;
    private int orderAmount;
    private  String cardNum;
    private  String nameOnCard;
    private  double price;
    private String expDate;
    private String csv;
    public Threshold(){}
    public Threshold(int itemId, int minQuantity, boolean isAutoOrder, int orderAmount, String cardNum, String nameOnCard,Double price,String expDate,String CSV) {
        this.itemId = itemId;
        this.minQuantity = minQuantity;
        this.isAutoOrder = isAutoOrder;
        this.orderAmount = orderAmount;
        this.cardNum = cardNum;
        this.nameOnCard = nameOnCard;
        this.price =price;
        this.expDate =expDate;
        this.csv = CSV;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public boolean isAutoOrder() {
        return isAutoOrder;
    }

    public void setAutoOrder(boolean autoOrder) {
        isAutoOrder = autoOrder;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }



    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }


}


