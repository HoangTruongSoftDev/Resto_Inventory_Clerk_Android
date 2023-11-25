package com.example.resto_inventory_clerk_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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

import model.Employee;
import model.Item;
import model.User;
import model.UserArrayAdapter;

public class StaffItemListActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener, 
        AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {

    ImageButton imageButtonSearch;
    ListView lvItems;
    Button btnLogOut;
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
        setContentView(R.layout.activity_staff_itemlist);
        initialize();
    }

    private void initialize() {

        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        btnLogOut = findViewById(R.id.btnLogOut);
        spinnerListSearch = findViewById(R.id.spinnerListSearch);
        rgPositionSearch = findViewById(R.id.rgPositionSearch);
        rbIDSearch = findViewById(R.id.rbIDSearch);
        rbNameSearch = findViewById(R.id.rbNameSearch);
        edSearch = findViewById(R.id.edSearch);

        btnLogOut.setOnClickListener(this);
        imageButtonSearch.setOnClickListener(this);

        listOfItems = new ArrayList<>();
        listOfSearchItems = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Item");
        firebaseDatabase.addChildEventListener(this);

        lvItems = findViewById(R.id.lvItems);
        lvItems.setOnItemClickListener(this);

        itemArrayAdapter = new ArrayAdapter<Item>(this, R.layout.single_item_layout,listOfSearchItems);
        lvItems.setAdapter(itemArrayAdapter);

        String[] searchOptions = {"Item ID", "Itme Name"};
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
                        Item receivedItem = (Item) o.getData().getSerializableExtra("new_item");
                        for (int i = 0; i < listOfItems.size(); i++) {
                            Item currentItem = listOfItems.get(i);
                            if (currentItem.getItemId() == receivedItem.getItemId()) {
                                listOfItems.set(i, receivedItem);
                                break;
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogOut) {
            finish();
        }
        else if (v.getId() == R.id.imageButtonSearch) {
            searchItem(String.valueOf(spinnerListSearch.getSelectedItem()));
        }
    }

    private void searchItem(String searchOption) {
        listOfSearchItems.clear();
        switch (searchOption) {
            case "Item ID":
                for (Item currentItem : listOfItems) {
                    if (currentItem.getItemId() == Integer.valueOf(edSearch.getText().toString())) {
                        listOfSearchItems.add(currentItem);

                        Item item = (Item) listOfItems.stream().filter(i -> i.getItemId() == currentItem.getItemId()).findFirst().orElse(null);
                        listOfSearchItems.add(item);
                    }
                }
                break;
            case "Item Name":
                for (Item currentItem : listOfItems) {
                    if (currentItem.getName().equals(edSearch.getText().toString())) {
                        listOfSearchItems.add(currentItem);
                        listOfSearchItems.add((Item) listOfItems.stream().filter(i -> i.getItemId() == currentItem.getItemId()).findFirst().orElse(null));
                    }
                }
                break;
        }
        itemArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Item item = snapshot.getValue(Item.class);
        listOfItems.add(item);
        itemArrayAdapter.notifyDataSetChanged();

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
        Intent intent = new Intent(this, StaffActivity.class);
        Item item = listOfItems.stream().filter(i -> i.getItemId() == listOfSearchItems.get(position).getItemId()).findFirst().orElse(null);
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
        }
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}