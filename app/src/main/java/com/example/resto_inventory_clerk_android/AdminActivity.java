package com.example.resto_inventory_clerk_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    EditText edEmployeeId, edFirstName, edLastName, edEmail, edPassword, edSearch;
    RadioGroup rgPosition, rgPositionSearch;
    RadioButton rbManager, rbStaff, rbManagerSearch, rbStaffSearch;
    ImageButton imageButtonShowPassword, imageButtonSearch;
    Spinner spinnerListSearch;
    Button btnSave, btnClear, btnUpdate, btnDelete, btnListAll, btnLogOut;

    TextView tvLabelMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initialize();
    }

    private void initialize() {
        edEmployeeId = findViewById(R.id.edEmployeeId);
        edFirstName = findViewById(R.id.edFirstName);
        edLastName = findViewById(R.id.edLastName);
        edEmail = findViewById(R.id.edEmail);
        edPassword = findViewById(R.id.edPassword);
        edSearch = findViewById(R.id.edSearch);
        rgPosition = findViewById(R.id.rgPosition);
        rbManager = findViewById(R.id.rbManager);
        rbStaff = findViewById(R.id.rbStaff);
        imageButtonShowPassword = findViewById(R.id.imageButtonShowPassword);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        spinnerListSearch = findViewById(R.id.spinnerListSearch);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnListAll = findViewById(R.id.btnListAll);
        btnLogOut = findViewById(R.id.btnLogOut);
        tvLabelMessage = findViewById(R.id.tvLabelMessage);
        rbManagerSearch = findViewById(R.id.rbManagerSearch);
        rbStaffSearch = findViewById(R.id.rbStaffSearch);
        rgPositionSearch = findViewById(R.id.rgPositionSearch);

        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnListAll.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        // 1. Define an array of search options
        String[] searchOptions = {"User ID", "First Name", "Last Name", "Email", "Position"};
        // 2. Create an ArrayAdapter and initialize it with the array of search options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchOptions);

        // 3. Set the layout resource for the drop-down view
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // 4. Set the adapter for the Spinner widget to display the search options
        spinnerListSearch.setAdapter(adapter);

        // 5. Set an OnItemSelectedListener to handle item selection events, this will trigger function onItemSelected() when the item clicked or function onNothingSelected
        spinnerListSearch.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "User ID":
                tvLabelMessage.setText("Enter Employee ID");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                break;
            case "First Name":
                tvLabelMessage.setText("Enter Employee First Name");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                break;
            case "Last Name":
                tvLabelMessage.setText("Enter Employee Last Name");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                break;
            case "Email":
                tvLabelMessage.setText("Enter Employee Email");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                break;
            case "Position":
                tvLabelMessage.setText("Enter Employee Position");
                rgPositionSearch.setVisibility(View.VISIBLE);
                edSearch.setVisibility(View.INVISIBLE);
                break;
            default:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}