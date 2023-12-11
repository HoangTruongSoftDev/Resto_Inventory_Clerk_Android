package com.example.resto_inventory_clerk_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import model.Item;
import model.Order;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener, TextWatcher {

    EditText  edItemName,edQuantity,edPricePer,edUofMeasure,edCardNumber,
            edNameOnCard,edExpirationDate,edCSV;
    Button btnPlaceOrder,btnCancel;
    TextView tvTotalPriceAmount;
    Order order;
    DatabaseReference orderDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initialize();
    }
    private void initialize(){
        edItemName = findViewById(R.id.edItemName);
        edQuantity = findViewById(R.id.edQuantity);
        edPricePer = findViewById(R.id.edPricePer);
        edUofMeasure = findViewById(R.id.edUofMeasure);
        edCardNumber = findViewById(R.id.edCardNumber);
        edNameOnCard = findViewById(R.id.edNameOnCard);
        edExpirationDate = findViewById(R.id.edExpirationDate);
        edCSV = findViewById(R.id.edCSV);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnCancel = findViewById(R.id.btnCancel);
        tvTotalPriceAmount = findViewById(R.id.tvTotalPriceAmount);
        edPricePer.addTextChangedListener(this);
        edQuantity.addTextChangedListener(this);

        order = new Order();
        btnPlaceOrder.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        orderDb = FirebaseDatabase.getInstance().getReference("Order");
        orderDb.addChildEventListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnCancel){
            Intent intent = new Intent(this, OrderItemListActivity.class);
            startActivity(intent);
        }
        else if(v.getId()==R.id.btnPlaceOrder){
            placeOrder(v);
        }
    }

    private void placeOrder(View v) {
        try {
            String name = edItemName.getText().toString();
            String Quantity = edQuantity.getText().toString();
            String measure = edUofMeasure.getText().toString();
            String price = edPricePer.getText().toString();
            String cardNum = edCardNumber.getText().toString();
            String nameOnCard = edNameOnCard.getText().toString();

            double totalPrice = Double.valueOf(Quantity)*Double.valueOf(price);
            if (name.isEmpty() || Quantity.isEmpty() || measure.isEmpty()
                    || price.isEmpty() || cardNum.isEmpty()|| nameOnCard.isEmpty()) {
                Snackbar.make(v, "Cannot be blank", Snackbar.LENGTH_LONG).show();
                return;
            }

            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
            Date todayDate = new Date();
            String date = currentDate.format(todayDate);

            // Update the local Item object
            Order o = new Order( name,  nameOnCard, Integer.valueOf(Quantity) ,
                    Double.valueOf(price), measure, totalPrice, cardNum, date);

            // Update the Firebase Database with the new quantity
//            orderDb.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists()){
//                        Snackbar.make(v, "This order id existed please use another one", Snackbar.LENGTH_LONG).show();
//
//                        return;
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
            orderDb.push().setValue(o);
//            orderDb.child(id).setValue(o);

            // Clear the consumed quantity input


            //setResult(RESULT_OK, intent);

//            Intent intent = new Intent();
//            intent.putExtra("new_order", o);
//            setResult(RESULT_OK, intent);
            Snackbar.make(v, "Order has been placed", Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Order item = snapshot.getValue(Order.class);
        Log.d("FIREBASE",item.toString());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e("FIREBASE", "Database operation canceled: " + error.getMessage());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        calculateTotal();
    }

    private void calculateTotal() {
        try {
            if(edQuantity.getText().toString()==null||edPricePer.getText().toString()==null){
                tvTotalPriceAmount.setText("0");
            }

            String Quantity = edQuantity.getText().toString();

            String price = edPricePer.getText().toString();
            double totalPrice = Double.valueOf(Quantity)*Double.valueOf(price);

            tvTotalPriceAmount.setText(Double.toString(totalPrice));
        }
        catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }


    }
}