package com.example.resto_inventory_clerk_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;


import model.Order;
import model.OrderArrayAdapter;

public class OrderItemListActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener,
        AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{
    ImageButton imageButtonAdd,imageButtonSearch;
    ListView lvOrders;
    Button btnReturn;
    ArrayList<Order> listOfOrders, listOfSearchOrder;
    ArrayAdapter<Order> orderArrayAdapter;
    DatabaseReference firebaseDatabase;
    Spinner spinnerListSearch;
    EditText edSearch;
    RadioGroup rgPositionSearch;
    RadioButton rbIDSearch, rbNameSearch;
    ActivityResultLauncher<Intent> actResL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_list);
        initialize();
        Log.d("OrderItemListActivity", "onCreate: Here");
    }
    private void initialize() {

        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        btnReturn = findViewById(R.id.btnReturn);

        spinnerListSearch = findViewById(R.id.spinnerListSearch);
        rgPositionSearch = findViewById(R.id.rgPositionSearch);
        rbIDSearch = findViewById(R.id.rbIDSearch);
        rbNameSearch = findViewById(R.id.rbNameSearch);
        edSearch = findViewById(R.id.edSearch);

        imageButtonAdd.setOnClickListener(this);
        btnReturn.setOnClickListener(this);

        imageButtonSearch.setOnClickListener(this);

        listOfOrders = new ArrayList<>();
        listOfSearchOrder = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Order");
        firebaseDatabase.addChildEventListener(this);
        lvOrders = findViewById(R.id.lvOrders);
        lvOrders.setOnItemClickListener(this);

        orderArrayAdapter = new OrderArrayAdapter(this, R.layout.single_order_layout, listOfSearchOrder);
        lvOrders.setAdapter(orderArrayAdapter);


        String[] searchOptions = {"Date","Item Name","All"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item , searchOptions);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListSearch.setAdapter(adapterSpinner);


        spinnerListSearch.setOnItemSelectedListener(this);




        rgPositionSearch.setVisibility(View.INVISIBLE);


        edSearch.setVisibility(View.INVISIBLE);


        imageButtonSearch.setVisibility(View.INVISIBLE);



        //registerActResL();
        Log.d("OrderItemListActivity", "init");

    }

//    private void registerActResL() {
//        actResL = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult o) {
//                        if (o.getResultCode() == RESULT_OK) {
//                            Log.println(Log.ERROR,"debug","here3");
//                            Order receivedOrder = (Order) o.getData().getSerializableExtra("new_order");
//                            Log.println(Log.ERROR,"debug","here4");
//                            for (int i = 0; i < listOfOrders.size(); i++) {
//                                Order currentItem = listOfOrders.get(i);
//                                if (currentItem.getOrderId() == receivedOrder.getOrderId()) {
//                                    listOfOrders.set(i, receivedOrder);
//                                    break;
//                                }
//                            }
//                            for (int i = 0; i < listOfSearchOrder.size(); i++) {
//                                Order currentOrder = listOfSearchOrder.get(i);
//                                if (currentOrder.getOrderId() == receivedOrder.getOrderId()) {
//                                    listOfSearchOrder.set(i, receivedOrder);
//                                    break;
//                                }
//                            }
//                            orderArrayAdapter.notifyDataSetChanged();
//                        }
//
//
//                    }
//                }
//        );
//        Log.d("OrderItemListActivity", "register");
//
//
//    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnReturn) {
           Intent intent = new Intent(this, ManagerItemListActivity.class);
           startActivity(intent);
        } else if (v.getId() == R.id.imageButtonSearch) {
            Log.d("TAG", "onClick:button ");
            searchItem(String.valueOf(spinnerListSearch.getSelectedItem()));
        }
        else if (v.getId() == R.id.imageButtonAdd) {
            Intent intent = new Intent(this, OrderActivity.class);
            //intent.putExtra("listOfItems", listOfOrders);
            //actResL.launch(intent);
            startActivity(intent);
        }

    }


    private void searchItem(String searchOption) {
        listOfSearchOrder.clear();
        switch (searchOption) {
            case "Date":
                String searchItemDate = edSearch.getText().toString().trim();
                Log.d("Date", "searchItem: Date");
                for (Order currentItem : listOfOrders) {
                    if (currentItem.getDate().contains(searchItemDate)) {
                        listOfSearchOrder.add(currentItem);
                    }
                }
                break;
            case "Item Name":
                String searchItemName = edSearch.getText().toString().trim();
                for (Order currentItem : listOfOrders) {
                    if (currentItem.getItemName().toLowerCase().contains(searchItemName.toLowerCase())) {
                        listOfSearchOrder.add(currentItem);
                    }
                }
                break;

        }
        orderArrayAdapter.notifyDataSetChanged();
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Order item = snapshot.getValue(Order.class);
        if (item != null) {
            listOfOrders.add(item);
            listOfSearchOrder.add(item);
            Log.d("Firebase", "Item added: " + item.toString());
            orderArrayAdapter.notifyDataSetChanged();
        } else {
            Log.e("Firebase", "Received null item from database");
        }
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

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Intent intent = new Intent(this, ManagerActivity.class);
        //Order item = listOfSearchOrder.get(position);
        //intent.putExtra("receivedItem", item);
        //intent.putExtra("listOfItems", listOfOrders);
        //actResL.launch(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        edSearch.setText(null);
        rgPositionSearch.clearCheck();
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "Date":
                edSearch.setHint("Enter Date");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "Item Name":
                edSearch.setHint("Enter Item Name");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "All":
                listOfSearchOrder.clear();
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.INVISIBLE);
                listOfSearchOrder.addAll(listOfOrders);
                imageButtonSearch.setVisibility(View.INVISIBLE);
                orderArrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}