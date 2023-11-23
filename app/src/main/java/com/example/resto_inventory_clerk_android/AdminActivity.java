package com.example.resto_inventory_clerk_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.Employee;
import model.User;
import service.Service;
import service.Validator;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener, DialogInterface.OnClickListener {


    EditText edEmployeeId, edFirstName, edLastName, edEmail, edPassword;
    RadioGroup rgPosition;
    RadioButton rbManager, rbStaff;
    ImageButton imageButtonShowPassword;
    Button btnSave, btnClear, btnDelete, btnReturn;

    Employee receivedEmployee = null;
    User receivedUser = null;
    DatabaseReference firebaseDatabase;
    ArrayList<Employee> listOfEmployees;
    AlertDialog.Builder alertDialog;
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

        rgPosition = findViewById(R.id.rgPosition);
        rbManager = findViewById(R.id.rbManager);
        rbStaff = findViewById(R.id.rbStaff);
        imageButtonShowPassword = findViewById(R.id.imageButtonShowPassword);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnDelete = findViewById(R.id.btnDelete);
        btnReturn = findViewById(R.id.btnReturn);

        listOfEmployees = new ArrayList<>();

        btnSave.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnReturn.setOnClickListener(this);
        imageButtonShowPassword.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.addChildEventListener(this);

        if (getIntent().getSerializableExtra("receivedUser") != null && getIntent().getSerializableExtra("receivedEmployee") != null) {
            edEmployeeId.setEnabled(false);
            receivedUser = (User) getIntent().getSerializableExtra("receivedUser");
            receivedEmployee = (Employee) getIntent().getSerializableExtra("receivedEmployee");
            edEmployeeId.setText(String.valueOf(receivedEmployee.getEmployeeId()));
            edFirstName.setText(receivedEmployee.getFirstName());

            edLastName.setText(receivedEmployee.getLastName());
            edEmail.setText(receivedEmployee.getEmail());
            switch (receivedUser.getPosition()) {
                case "Manager":
                    rbManager.setChecked(true);
                    break;
                case "Staff":
                    rbStaff.setChecked(true);
                    break;
            }
        }
        else {
            btnDelete.setVisibility(View.INVISIBLE);
        }
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("COnfirmation");
        alertDialog.setMessage("Do you want to delete this user?");
        alertDialog.setPositiveButton("Yes", this);
        alertDialog.setNegativeButton("No", this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            saveUser();
        }
        else if (v.getId() == R.id.btnClear) {
            Service.clearAllWidgets((ViewGroup) findViewById(R.id.layoutAdminActivity));
        }
        else if (v.getId() == R.id.btnDelete) {
            deleteUser();
        }
        else if (v.getId() == R.id.btnReturn) {
            finish();
        }
        else if (v.getId() == R.id.imageButtonShowPassword) {
            if (edPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                // If it's a password field, change it to plain text
                edPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                imageButtonShowPassword.setImageResource(R.drawable.hidethepassword);

            } else if (edPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL)) {
                // If it's not a password field, change it to a password field
                edPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imageButtonShowPassword.setImageResource(R.drawable.showthepassword);
            }
        }
    }

    private void deleteUser() {
        alertDialog.create().show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            firebaseDatabase.child("Employee").child(edEmployeeId.getText().toString()).removeValue();
            firebaseDatabase.child("User").child(edEmployeeId.getText().toString()).removeValue();
            Toast.makeText(this, "Delete User Successfully", Toast.LENGTH_LONG);
            finish();
        }
        else if (which == Dialog.BUTTON_NEGATIVE) {
            Toast.makeText(this, "Cancel User Deletion", Toast.LENGTH_LONG);
        }
    }
    private void saveUser() {
        Employee newEmployee;
        User newUser;
        if (edEmployeeId.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Employee ID", Toast.LENGTH_SHORT).show();
            edEmployeeId.requestFocus();
            return;
        }

        String position = rgPosition.getCheckedRadioButtonId() == R.id.rbManager ? "Manager" :
                        rgPosition.getCheckedRadioButtonId() == R.id.rbStaff ? "Staff" : null;
        if (position == null) {
            Toast.makeText(this, "Please select employee position", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Validator.isValidUserId(edEmployeeId.getText().toString(), position)) {
            Toast.makeText(this, "The ID must be 6-digit number", Toast.LENGTH_SHORT).show();
            edEmployeeId.setText(null);
            return;
        }

        int id = Integer.valueOf(edEmployeeId.getText().toString());
        if (receivedEmployee == null && receivedUser == null) {
            newEmployee = new Employee();
            newUser = new User();
            if (listOfEmployees.stream().filter(e -> e.getEmployeeId() == Integer.valueOf(edEmployeeId.getText().toString())).findFirst().orElse(null) != null) {
                Toast.makeText(this, "This Employee Existing already exists", Toast.LENGTH_SHORT).show();
                edEmployeeId.setText(null);
                return;
            }
        }
        else {
            newUser = receivedUser;
            newEmployee = receivedEmployee;
        }
        newUser.setUserId(id);
        newEmployee.setEmployeeId(id);
        newUser.setPosition(position);
        if (edFirstName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Employee First Name", Toast.LENGTH_SHORT).show();
            edFirstName.requestFocus();
            return;
        }
        newEmployee.setFirstName(edFirstName.getText().toString());
        if (edLastName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Employee Last Name", Toast.LENGTH_SHORT).show();
            edLastName.requestFocus();
            return;
        }
        newEmployee.setLastName(edLastName.getText().toString());
        if (edEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, "Missing Employee Email", Toast.LENGTH_SHORT).show();
            edEmail.requestFocus();
            return;
        }
        if (receivedEmployee == null && receivedUser == null) {
            if (listOfEmployees.stream().filter(e -> e.getEmail().equals(edEmail.getText().toString())).findFirst().orElse(null) != null) {
                Toast.makeText(this, "This Employee Email already exists", Toast.LENGTH_SHORT).show();
                edEmail.setText(null);
                return;
            }
        }
        newEmployee.setEmail(edEmail.getText().toString());
        if (receivedUser == null) {
            if (edPassword.getText().toString().isEmpty()) {
                Toast.makeText(this, "Missing Employee Password", Toast.LENGTH_SHORT).show();
                edPassword.requestFocus();
                return;
            }
            newUser.setPassword(edPassword.getText().toString());
        }
        else {
            if (!edPassword.getText().toString().isEmpty()) {
                newUser.setPassword(edPassword.getText().toString());
            }
        }
        Toast.makeText(this, "Saving Account Successfully", Toast.LENGTH_SHORT).show();
        firebaseDatabase.child("Employee").child(String.valueOf(newEmployee.getEmployeeId())).setValue(newEmployee);
        firebaseDatabase.child("User").child(String.valueOf(newUser.getUserId())).setValue(newUser);
        if (receivedUser == null && receivedEmployee == null) {
            Intent intent = new Intent();
            intent.putExtra("new_user", newUser);
            intent.putExtra("new_employee", newEmployee);
            intent.putExtra("action", "create");
            setResult(RESULT_OK, intent);
        }
        else {
            Intent intent = new Intent();
            intent.putExtra("new_user", newUser);
            intent.putExtra("new_employee", newEmployee);
            intent.putExtra("action", "update");
            setResult(RESULT_OK, intent);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if (snapshot.getKey().equals("Employee")) {
            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                Employee employee = currentSnapshot.getValue(Employee.class);
                listOfEmployees.add(employee);
            }
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


}