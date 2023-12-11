package model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kotlin.random.URandomKt;

public class ThresholdWarning {
    public interface ThresholdCheckerListener {
        void onOrderAdded();
        void onQuantityBelowThreshold(Item item);
    }

    public static void checkThresholdsAndShowWarnings(DatabaseReference itemDB, DatabaseReference thresholdDB, DatabaseReference orderDB,ThresholdCheckerListener listener) {
        itemDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {

                for (DataSnapshot itemData : itemSnapshot.getChildren()) {
                    String itemId = itemData.getKey();

                    if (itemId != null) {
                        checkThresholdAndShowWarning(itemDB, thresholdDB, orderDB, itemId,listener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError itemError) {
                Log.e("Firebase", "Error loading items: " + itemError.getMessage());
            }
        });
    }

    public static void checkThresholdAndShowWarning(DatabaseReference itemDB, DatabaseReference thresholdDB, DatabaseReference orderDB, String itemId,ThresholdCheckerListener listener) {
        itemDB.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot itemSnapshot) {
                if (itemSnapshot.exists()) {
                    Item item = itemSnapshot.getValue(Item.class);

                    if (item != null) {
                        thresholdDB.child(itemId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot thresholdSnapshot) {
                                if (thresholdSnapshot.exists()) {
                                    Threshold threshold = thresholdSnapshot.getValue(Threshold.class);
                                    if (threshold != null) {

                                        int itemQuantity = item.getQuantity();
                                        int thresholdMinQuantity = threshold.getMinQuantity();
                                        boolean isAutoOrder = threshold.isAutoOrder();
                                        if (isAutoOrder && itemQuantity < thresholdMinQuantity) {
                                            // Add an order to Firebase
                                            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
                                            Date todayDate = new Date();
                                            String date = currentDate.format(todayDate);
                                            double total  = threshold.getPrice() *threshold.getOrderAmount();
                                            Order neworder = new Order(item.getName(), threshold.getNameOnCard(),
                                                    threshold.getOrderAmount(),threshold.getPrice(),item.getUnitOfMeasure(),total,threshold.getCardNum(),date);

                                            addOrder(orderDB, neworder);
                                            item.setQuantity(thresholdMinQuantity+threshold.getOrderAmount());
                                            itemDB.child(itemId).setValue(item);
                                            listener.onOrderAdded();

                                        } else if (itemQuantity < thresholdMinQuantity) {
                                            //show warning
                                            listener.onQuantityBelowThreshold(item);
                                        }
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError thresholdError) {
                                Log.e("Firebase", "Error loading threshold: " + thresholdError.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError itemError) {
                Log.e("Firebase", "Error loading item: " + itemError.getMessage());
            }
        });
    }

    private static String addOrder(DatabaseReference orderDB, Order order) {
        // Add an order to Firebase
        // You need to implement the logic for adding an order based on your data structure
        // For simplicity, this example assumes an Order class with an addItem method

        try {
            Log.d("Result", "RIGHT");
            orderDB.push().setValue(order);
            Log.d("Result", "RIGHT1");
        }
        catch (Exception e){
            Log.d("Result", "WRONG");
            return "Could not order "+ e.getMessage();
        }
        return "Auto ordered";
    }
}
