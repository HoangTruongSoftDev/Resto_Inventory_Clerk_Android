package com.example.resto_inventory_clerk_android;

import static model.ThresholdWarning.checkThresholdsAndShowWarnings;

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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Iterator;

import model.Item;
import model.ItemArrayAdapter;
import model.ThresholdWarning;

public class ManagerItemListActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, ThresholdWarning.ThresholdCheckerListener {

    ImageButton imageButtonAdd,imageButtonSearch;
    ListView lvItems;
    Button btnLogOut, btnAdvanced,btnOrder;
    ArrayList<Item> listOfItems, listOfSearchItems;
    ArrayAdapter<Item> itemArrayAdapter;
    DatabaseReference firebaseDatabase;
    Spinner spinnerListSearch;
    EditText edSearch;
    RadioGroup rgPositionSearch;
    RadioButton rbIDSearch, rbNameSearch;
    ActivityResultLauncher<Intent> actResL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_itemlist);

        checkThresholdsAndShowWarnings( FirebaseDatabase.getInstance().getReference("Item"), FirebaseDatabase.getInstance().getReference("Threshold"), FirebaseDatabase.getInstance().getReference("Order"),this);
        initialize();
    }

    private void initialize() {

        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        btnLogOut = findViewById(R.id.btnLogOut);
        btnAdvanced = findViewById(R.id.btnAdvanced);
        btnOrder = findViewById(R.id.btnOrder);
        spinnerListSearch = findViewById(R.id.spinnerListSearch);
        rgPositionSearch = findViewById(R.id.rgPositionSearch);
        rbIDSearch = findViewById(R.id.rbIDSearch);
        rbNameSearch = findViewById(R.id.rbNameSearch);
        edSearch = findViewById(R.id.edSearch);

        imageButtonAdd.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnAdvanced.setOnClickListener(this);
        btnOrder.setOnClickListener(this);
        imageButtonSearch.setOnClickListener(this);

        listOfItems = new ArrayList<>();
        listOfSearchItems = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("Item");
        firebaseDatabase.addChildEventListener(this);
        lvItems = findViewById(R.id.lvItems);
        lvItems.setOnItemClickListener(this);

        itemArrayAdapter = new ItemArrayAdapter(this, R.layout.single_item_layout, listOfSearchItems);
        lvItems.setAdapter(itemArrayAdapter);

        String[] searchOptions = {"Item ID", "Item Name","All"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchOptions);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListSearch.setAdapter(adapterSpinner);
        spinnerListSearch.setOnItemSelectedListener(this);
        spinnerListSearch.setSelection(1);
        rgPositionSearch.setVisibility(View.INVISIBLE);
        edSearch.setVisibility(View.INVISIBLE);
        imageButtonSearch.setVisibility(View.INVISIBLE);
        registerActResL();
    }

    private void registerActResL() {
        actResL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK) {
                            Item receivedItem = (Item) o.getData().getSerializableExtra("new_item");
                            for (int i = 0; i < listOfItems.size(); i++) {
                                Item currentItem = listOfItems.get(i);
                                if (currentItem.getItemId() == receivedItem.getItemId()) {
                                    listOfItems.set(i, receivedItem);
                                    break;
                                }
                            }
                            for (int i = 0; i < listOfSearchItems.size(); i++) {
                                Item currentItem = listOfSearchItems.get(i);
                                if (currentItem.getItemId() == receivedItem.getItemId()) {
                                    listOfSearchItems.set(i, receivedItem);
                                    break;
                                }
                            }
                            itemArrayAdapter.notifyDataSetChanged();
                        }
                        else if (o.getResultCode() == RESULT_FIRST_USER) {
                            Item receivedItem = (Item) o.getData().getSerializableExtra("delete_item");

                            // Remove the item from listOfItems using Iterator
                            Iterator<Item> iterator = listOfItems.iterator();
                            while (iterator.hasNext()) {
                                Item currentItem = iterator.next();
                                if (currentItem.getItemId() == receivedItem.getItemId()) {
                                    iterator.remove();
                                    break;
                                }
                            }

                            // Remove the item from listOfSearchItems using Iterator
                            iterator = listOfSearchItems.iterator();
                            while (iterator.hasNext()) {
                                Item currentItem = iterator.next();
                                if (currentItem.getItemId() == receivedItem.getItemId()) {
                                    iterator.remove();
                                    break;
                                }
                            }

                            itemArrayAdapter.notifyDataSetChanged();
                        }

                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogOut) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.imageButtonSearch) {
            searchItem(String.valueOf(spinnerListSearch.getSelectedItem()));
        }
        else if (v.getId() == R.id.imageButtonAdd) {
            Intent intent = new Intent(this, ManagerActivity.class);
            intent.putExtra("listOfItems", listOfItems);
            actResL.launch(intent);
        }
        else if(v.getId() == R.id.btnOrder){
            Intent intent = new Intent(this, OrderItemListActivity.class);
            //intent.putExtra("listOfItems", listOfItems);
            actResL.launch(intent);
        }
        else if(v.getId() == R.id.btnAdvanced){
            Intent intent = new Intent(this, AdvancedThresholdActivity.class);
            //intent.putExtra("listOfItems", listOfItems);
            actResL.launch(intent);
        }
    }


    private void searchItem(String searchOption) {
        listOfSearchItems.clear();
        switch (searchOption) {
            case "Item ID":
                String searchItemIdStr = edSearch.getText().toString().trim();
                if (!searchItemIdStr.isEmpty()) {
                    try {
                        int searchItemId = Integer.parseInt(searchItemIdStr);
                        for (Item currentItem : listOfItems) {
                            if (currentItem.getItemId() == searchItemId) {
                                listOfSearchItems.add(currentItem);
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "Item Name":
                String searchItemName = edSearch.getText().toString().trim();
                for (Item currentItem : listOfItems) {
                    if (currentItem.getName().toLowerCase().contains(searchItemName.toLowerCase())) {
                        listOfSearchItems.add(currentItem);
                    }
                }
                break;

        }
        itemArrayAdapter.notifyDataSetChanged();
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Item item = snapshot.getValue(Item.class);
        if (item != null) {
            listOfItems.add(item);
            listOfSearchItems.add(item);
            Log.d("Firebase", "Item added: " + item.toString());
            itemArrayAdapter.notifyDataSetChanged();
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
        Intent intent = new Intent(this, ManagerActivity.class);
        Item item = listOfSearchItems.get(position);
        intent.putExtra("receivedItem", item);
        intent.putExtra("listOfItems", listOfItems);
        actResL.launch(intent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        edSearch.setText(null);
        rgPositionSearch.clearCheck();
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "Item ID":
                edSearch.setHint("Enter Item ID");
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
                listOfSearchItems.clear();
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.INVISIBLE);
                listOfSearchItems.addAll(listOfItems);
                imageButtonSearch.setVisibility(View.INVISIBLE);
                itemArrayAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onOrderAdded() {
        Toast.makeText(this, "Order added successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onQuantityBelowThreshold(Item item) {
        Toast.makeText(this, "Item "+item.getItemId()+" "+item.getName()+" quantity is below the threshold minimum quantity(current "+item.getQuantity()+")", Toast.LENGTH_LONG).show();

    }
}