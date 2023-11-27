package com.example.resto_inventory_clerk_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.Employee;
import model.Item;
import service.Service;

public class StaffActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener {

    EditText edItemId, edItemName, edQuantity, edUnitPrice, edUnitOfMeasure,edTotalPrice,edQuantityConsumed;
    Button btnConsume, btnReturn;
    Item receivedItem;
    DatabaseReference itemDatabase;
    ArrayList<Item> listOfItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        initialize();
    }

    private void initialize() {
        edItemId = findViewById(R.id.edItemId);
        edItemName = findViewById(R.id.edItemName);
        edQuantity = findViewById(R.id.edQuantity);
        edUnitPrice = findViewById(R.id.edUnitPrice);
        edUnitOfMeasure = findViewById(R.id.edUnitOfMeasure);
        edTotalPrice = findViewById(R.id.edTotalPrice);
        edQuantityConsumed = findViewById(R.id.edQuantityConsumed);
        btnConsume = findViewById(R.id.btnConsume);
        btnReturn = findViewById(R.id.btnReturn);
        btnConsume.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        listOfItems = new ArrayList<>();

        itemDatabase = FirebaseDatabase.getInstance().getReference("Item");
        itemDatabase.addChildEventListener(this);

        receivedItem = (Item) getIntent().getSerializableExtra("receivedItem");
        edItemId.setText(String.valueOf(receivedItem.getItemId()));
        edItemName.setText(receivedItem.getName());
        edQuantity.setText(String.valueOf(receivedItem.getQuantity()));
        edUnitOfMeasure.setText(receivedItem.getUnitOfMeasure());
        edUnitPrice.setText(String.valueOf(receivedItem.getUnitPrice()));
        edTotalPrice.setText(String.valueOf(receivedItem.getUnitPrice()*receivedItem.getQuantity()));

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnReturn) {
            finish();
        }
        else if (v.getId() == R.id.btnConsume) {
            updateQuantity(v," has been updated.");
            edQuantityConsumed.setText(null);
        }

    }

    private void updateQuantity(View v, String message) {
        try {
            String id = edItemId.getText().toString();
            String name = edItemName.getText().toString();
            String currentQuantityStr = edQuantity.getText().toString();
            String measure = edUnitOfMeasure.getText().toString();
            String price = edUnitPrice.getText().toString();
            String consumedQuantityStr = edQuantityConsumed.getText().toString();

            if (consumedQuantityStr.isEmpty()) {
                Snackbar.make(v, "Please enter consumed quantity", Snackbar.LENGTH_LONG).show();
                return;
            }

            int currentQuantity = Integer.parseInt(currentQuantityStr);
            int consumedQuantity = Integer.parseInt(consumedQuantityStr);

            // Check if there's enough quantity to consume
            if (consumedQuantity > currentQuantity) {
                Snackbar.make(v, "Consumed quantity cannot exceed current quantity", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Calculate the new quantity after consumption
            int newQuantity = currentQuantity - consumedQuantity;

            // Update the local Item object
            Item item = new Item(Integer.valueOf(id), name, newQuantity, measure, Double.valueOf(price));

            // Update the Firebase Database with the new quantity
            itemDatabase.child(id).setValue(item);

            // Clear the consumed quantity input
            edQuantityConsumed.setText(null);

            Intent intent = new Intent();
            intent.putExtra("new_item", item);
            setResult(RESULT_OK, intent);
            Snackbar.make(v, "The Item with id: " + id + message, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Item item = snapshot.getValue(Item.class);
        Log.d("FIREBASE",item.toString());
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(snapshot.exists()){
            String quantity = snapshot.child("quantity").getValue().toString();
            edQuantity.setText(quantity);
        }
        else {
            Toast.makeText(this,"No document",Toast.LENGTH_LONG).show();
        }
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

}