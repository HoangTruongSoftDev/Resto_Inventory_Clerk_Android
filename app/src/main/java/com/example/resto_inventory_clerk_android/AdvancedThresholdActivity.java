package com.example.resto_inventory_clerk_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Item;
import model.Threshold;

public class AdvancedThresholdActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner spinnerListSearch;
    Switch swAutoOrder;
    EditText edMinQuantity,edOrderAmount,edPrice,edCardNumber,edNameOnCard,edExpirationDate,edCSV;
    Button btnSave,btnClear,btnReturn,btnDelete;
    DatabaseReference orderDB;
    DatabaseReference itemDB;
    DatabaseReference thresholdDB;
    ArrayList<String> itemIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_threshold);
        initialize();

    }

    private void initialize() {
        spinnerListSearch = findViewById(R.id.spinnerListSearch);
        swAutoOrder = findViewById(R.id.swAutoOrder);
        edMinQuantity = findViewById(R.id.edMinQuantity);
        edOrderAmount = findViewById(R.id.edOrderAmount);
        edCardNumber = findViewById(R.id.edCardNumber);
        edNameOnCard = findViewById(R.id.edNameOnCard);
        edExpirationDate = findViewById(R.id.edExpirationDate);
        edPrice = findViewById(R.id.edPrice);

        edCSV = findViewById(R.id.edCSV);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnReturn = findViewById(R.id.btnReturn);
        btnDelete =findViewById(R.id.btnDelete);
        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        itemIdList = new ArrayList<>();

        orderDB = FirebaseDatabase.getInstance().getReference("Order");
        itemDB = FirebaseDatabase.getInstance().getReference("Item");
        thresholdDB = FirebaseDatabase.getInstance().getReference("Threshold");

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemIdList);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListSearch.setAdapter(adapterSpinner);
        spinnerListSearch.setOnItemSelectedListener(this);
        loadItemKeysToSpinner();
        swAutoOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update the text based on the Switch state
                if (isChecked) {
                    swAutoOrder.setText("Yes");
                } else {
                    swAutoOrder.setText("No");
                }
            }
        });
    }

    private void loadItemKeysToSpinner() {
        itemDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemIdList.clear(); // Clear the list before adding keys to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String itemId = snapshot.getKey();
                    itemIdList.add(itemId);
                }

                // Create an ArrayAdapter and set it to the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdvancedThresholdActivity.this, android.R.layout.simple_spinner_item, itemIdList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerListSearch.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading item keys: " + databaseError.getMessage());
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSave){
            SaveThreshold();
            Log.d("fIX", "onClick: sAVE");
        } else if (v.getId() ==R.id.btnClear) {
            clearAll();
        } else if (v.getId() == R.id.btnReturn) {
            finish();
        } else if (v.getId() ==R.id.btnDelete){
            deleteThreshold();
        }
    }

    private void deleteThreshold() {
        try {
            String itemId = spinnerListSearch.getSelectedItem().toString();

            // Use removeValue to delete the threshold from the database
            thresholdDB.child(itemId).removeValue();

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };


    private void clearAll() {

        swAutoOrder.setChecked(false);
        edMinQuantity.setText(null);
        edOrderAmount.setText(null);
        edCardNumber.setText(null);
        edNameOnCard.setText(null);
        edExpirationDate.setText(null);
        edPrice.setText(null);
        edCSV.setText(null);
        //spinnerListSearch.setSelection(1);
    }

    private void SaveThreshold() {
        Log.d("Fix", "SaveThreshold: 1");

        try{
            String itemId = spinnerListSearch.getSelectedItem().toString();
            int minQuantity = Integer.valueOf(edMinQuantity.getText().toString());
            boolean isAutoOrder = swAutoOrder.isChecked();
            int orderAmount = Integer.valueOf(edOrderAmount.getText().toString());
            String cardNum = edCardNumber.getText().toString();
            String nameOnCard = edNameOnCard.getText().toString();
            double price = Double.valueOf(edPrice.getText().toString());
            String expdate = edExpirationDate.getText().toString();
            String csv = edCSV.getText().toString();

            if(edMinQuantity.getText().toString().isEmpty()||edOrderAmount.getText().toString().isEmpty()||
                    cardNum.isEmpty()||nameOnCard.isEmpty()||edPrice.getText().toString().isEmpty()){
                Toast.makeText(this, "Field cannot be null", Toast.LENGTH_SHORT).show();
            }

            Threshold threshold = new Threshold(Integer.valueOf(itemId),minQuantity,isAutoOrder,orderAmount,cardNum,nameOnCard,price,expdate,csv);
            Log.d("Fix", "SaveThreshold: Here");


            thresholdDB.child(itemId).setValue(threshold);
            Toast.makeText(this,"Save threshold successfully",Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Toast.makeText(this,"Cannot save"+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        Log.d("Fix", "SaveThreshold: 2");

    }

    public  void loadThreshold(){
        String selectedThresholdId = spinnerListSearch.getSelectedItem().toString();

        thresholdDB.child(selectedThresholdId);
        thresholdDB.child(selectedThresholdId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Threshold with the specified ID exists
                    Threshold selectedThreshold = dataSnapshot.getValue(Threshold.class);
                    displayThresholdData(selectedThreshold);
                } else {
                    // Threshold with the specified ID does not exist
                    clearAll();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error loading threshold: " + error.getMessage());
                // Assume non-existence on error
                clearAll();
            }


        });
    }

    private void displayThresholdData(Threshold threshold) {
        // Display the threshold data in appropriate UI elements
        if (threshold != null) {
            swAutoOrder.setChecked(threshold.isAutoOrder());
            edMinQuantity.setText(String.valueOf(threshold.getMinQuantity()));
            edOrderAmount.setText(String.valueOf(threshold.getOrderAmount()));
            edCardNumber.setText(threshold.getCardNum());
            edNameOnCard.setText(threshold.getNameOnCard());
            edExpirationDate.setText(threshold.getExpDate());
            edPrice.setText(String.valueOf(threshold.getPrice()));
            edCSV.setText(threshold.getCsv());
            // Update other UI elements as needed...
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedThresholdId = spinnerListSearch.getSelectedItem().toString();
        // Call a method to load and display the threshold data based on the selected ID
        loadThreshold();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}