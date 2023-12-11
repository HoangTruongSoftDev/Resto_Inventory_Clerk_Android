package com.example.resto_inventory_clerk_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.Employee;
import model.User;
import model.UserArrayAdapter;

public class AdminListActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageButton imageButtonAdd, imageButtonSearch;
    ListView lvUsers;
    Button btnLogOut;
    UserArrayAdapter userAdapter;
    ArrayList<User> listOfUsers, listOfSearchUsers;
    ArrayList<Employee> listOfEmployees, listOfSearchEmployees;
    DatabaseReference firebaseDatabase;
    AlertDialog.Builder alertDialog;
    Spinner spinnerListSearch;
    int selectedEmployeeId;
    EditText edSearch;
    RadioGroup rgPositionSearch;
    RadioButton rbManagerSearch, rbStaffSearch;
    ActivityResultLauncher<Intent> actResL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        initialize();
    }

    private void initialize() {

        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        btnLogOut = findViewById(R.id.btnLogOut);
        spinnerListSearch = findViewById(R.id.spinnerListSearch);
        rgPositionSearch = findViewById(R.id.rgPositionSearch);
        rbManagerSearch = findViewById(R.id.rbManagerSearch);
        rbStaffSearch = findViewById(R.id.rbStaffSearch);
        edSearch = findViewById(R.id.edSearch);

        imageButtonAdd.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        imageButtonSearch.setOnClickListener(this);

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmation");
        alertDialog.setMessage("Do you want to delete this user permanently?");
        alertDialog.setPositiveButton("Yes", this);
        alertDialog.setNegativeButton("No", this);

        listOfUsers = new ArrayList<>();
        listOfEmployees = new ArrayList<>();
        listOfSearchUsers = new ArrayList<>();
        listOfSearchEmployees = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseDatabase.addChildEventListener(this);


        lvUsers = findViewById(R.id.lvUsers);
        lvUsers.setOnItemClickListener(this);
        lvUsers.setOnItemLongClickListener(this);
        userAdapter = new UserArrayAdapter(this, R.layout.single_user_layout, listOfSearchUsers, listOfSearchEmployees);
        lvUsers.setAdapter(userAdapter);

        String[] searchOptions = {"User ID", "First Name", "Last Name", "Email", "Position", "All"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, searchOptions);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListSearch.setAdapter(adapterSpinner);
        spinnerListSearch.setOnItemSelectedListener(this);
        spinnerListSearch.setSelection(5);
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
                            String action = o.getData().getStringExtra("action");
                            Employee receivedEmployee = (Employee) o.getData().getSerializableExtra("new_employee");
                            User receivedUser = (User) o.getData().getSerializableExtra("new_user");
                            if (action.equals("create")) {
                                listOfEmployees.add(receivedEmployee);
                                listOfUsers.add(receivedUser);
                                listOfSearchUsers.add(receivedUser);
                                listOfSearchEmployees.add(receivedEmployee);
                            }
                            else if (action.equals("update")) {
                                for (int i = 0; i < listOfEmployees.size(); i++) {
                                    Employee currentEmployee = listOfEmployees.get(i);
                                    if (currentEmployee.getEmployeeId() == receivedEmployee.getEmployeeId()) {
                                        listOfEmployees.set(i, receivedEmployee);
                                        break;
                                    }
                                }
                                for (int i = 0; i < listOfUsers.size(); i++) {
                                    User currentUser = listOfUsers.get(i);
                                    if (currentUser.getUserId() == receivedUser.getUserId()) {
                                        listOfUsers.set(i, receivedUser);
                                        break;
                                    }
                                }
                                for (int i = 0; i < listOfSearchEmployees.size(); i++) {
                                    Employee currentEmployee = listOfSearchEmployees.get(i);
                                    if (currentEmployee.getEmployeeId() == receivedEmployee.getEmployeeId()) {
                                        listOfSearchEmployees.set(i, receivedEmployee);
                                        break;
                                    }
                                }
                                for (int i = 0; i < listOfSearchUsers.size(); i++) {
                                    User currentUser = listOfSearchUsers.get(i);
                                    if (currentUser.getUserId() == receivedUser.getUserId()) {
                                        listOfSearchUsers.set(i, receivedUser);
                                        break;
                                    }
                                }
                            }
                            userAdapter.notifyDataSetChanged();
                        }
                    }
                }
        );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        edSearch.setText(null);
        rgPositionSearch.clearCheck();
        switch (String.valueOf(parent.getItemAtPosition(position))) {
            case "User ID":
                edSearch.setHint("Enter Employee ID");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "First Name":
                edSearch.setHint("Enter First Name");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "Last Name":
                edSearch.setHint("Enter Last Name");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "Email":
                edSearch.setHint("Enter Employee Email");
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.VISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "Position":
                rgPositionSearch.setVisibility(View.VISIBLE);
                edSearch.setVisibility(View.INVISIBLE);
                imageButtonSearch.setVisibility(View.VISIBLE);
                break;
            case "All":
                listOfSearchEmployees.clear();
                listOfSearchUsers.clear();
                rgPositionSearch.setVisibility(View.INVISIBLE);
                edSearch.setVisibility(View.INVISIBLE);
                listOfSearchEmployees.addAll(listOfEmployees);
                listOfSearchUsers.addAll(listOfUsers);
                imageButtonSearch.setVisibility(View.INVISIBLE);
                userAdapter.notifyDataSetChanged();
                break;
        }
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE) {
            DatabaseReference employeeDatabase = firebaseDatabase.child("Employee");
            employeeDatabase.child(String.valueOf(selectedEmployeeId)).removeValue();
            DatabaseReference userDatabase = firebaseDatabase.child("User");
            userDatabase.child(String.valueOf(selectedEmployeeId)).removeValue();
            listOfEmployees.removeIf(e -> e.getEmployeeId() == selectedEmployeeId);
            listOfUsers.removeIf(u -> u.getUserId() == selectedEmployeeId);
            listOfSearchEmployees.removeIf(e -> e.getEmployeeId() == selectedEmployeeId);
            listOfSearchUsers.removeIf(u -> u.getUserId() == selectedEmployeeId);
            Toast.makeText(this, "Deleting User Successfully !!!", Toast.LENGTH_LONG).show();
            userAdapter.notifyDataSetChanged();
        }
        else if (which == Dialog.BUTTON_NEGATIVE) {
            Toast.makeText(this, "Cancelled Deletion !!!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(this, AdminActivity.class);
            Employee emp = listOfEmployees.stream().filter(e -> e.getEmployeeId() == listOfSearchEmployees.get(position).getEmployeeId()).findFirst().orElse(null);
            intent.putExtra("receivedEmployee", emp);
            User user = listOfUsers.stream().filter(u -> u.getUserId() == emp.getEmployeeId()).findFirst().orElse(null);
            intent.putExtra("receivedUser", user);
            intent.putExtra("listOfEmployees", listOfEmployees);
            intent.putExtra("listOfUsers", listOfUsers);
            actResL.launch(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        selectedEmployeeId = listOfSearchEmployees.get(position).getEmployeeId();
        alertDialog.create().show();
        return true;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButtonAdd) {
            Intent intent = new Intent(this, AdminActivity.class);
            actResL.launch(intent);
        }
        else if (view.getId() == R.id.btnLogOut) {
            finish();
        }
        else if (view.getId() == R.id.imageButtonSearch) {
            searchUsers(String.valueOf(spinnerListSearch.getSelectedItem()));
        }
    }

    private void searchUsers(String searchOption) {
        listOfSearchUsers.clear();
        listOfSearchEmployees.clear();
        switch (searchOption) {
            case "User ID":
                for (Employee currentEmployee : listOfEmployees) {
                    if (currentEmployee.getEmployeeId() == Integer.valueOf(edSearch.getText().toString())) {
                        listOfSearchEmployees.add(currentEmployee);

                        User user = (User)listOfUsers.stream().filter(u -> u.getUserId() == currentEmployee.getEmployeeId()).findFirst().orElse(null);
                        listOfSearchUsers.add(user);
                    }
                }
                break;
            case "First Name":
                for (Employee currentEmployee : listOfEmployees) {
                    if (currentEmployee.getFirstName().toLowerCase().contains(edSearch.getText().toString().toLowerCase())) {
                        listOfSearchEmployees.add(currentEmployee);
                        listOfSearchUsers.add((User)listOfUsers.stream().filter(u -> u.getUserId() == currentEmployee.getEmployeeId()).findFirst().orElse(null));
                    }
                }
                break;
            case "Last Name":
                for (Employee currentEmployee : listOfEmployees) {
                    if (currentEmployee.getLastName().toLowerCase().contains((edSearch.getText().toString().toLowerCase()))) {
                        listOfSearchEmployees.add(currentEmployee);
                        listOfSearchUsers.add((User)listOfUsers.stream().filter(u -> u.getUserId() == currentEmployee.getEmployeeId()).findFirst().orElse(null));
                    }
                }
                break;
            case "Email":
                for (Employee currentEmployee : listOfEmployees) {
                    if (currentEmployee.getEmail().toLowerCase().contains(edSearch.getText().toString().toLowerCase())) {
                        listOfSearchEmployees.add(currentEmployee);
                        listOfSearchUsers.add((User)listOfUsers.stream().filter(u -> u.getUserId() == currentEmployee.getEmployeeId()).findFirst().orElse(null));
                    }
                }
                break;
            case "Position":
                if (rgPositionSearch.getCheckedRadioButtonId() == R.id.rbManagerSearch) {
                    for (User currentUser : listOfUsers) {
                        if (currentUser.getPosition().equals("Manager")) {
                            listOfSearchUsers.add(currentUser);
                            listOfSearchEmployees.add((Employee)listOfEmployees.stream().filter(e -> e.getEmployeeId() == currentUser.getUserId()).findFirst().orElse(null));
                        }
                    }
                }
                else if (rgPositionSearch.getCheckedRadioButtonId() == R.id.rbStaffSearch) {
                    for (User currentUser : listOfUsers) {
                        if (currentUser.getPosition().equals("Staff")) {
                            listOfSearchUsers.add(currentUser);
                            listOfSearchEmployees.add((Employee)listOfEmployees.stream().filter(e -> e.getEmployeeId() == currentUser.getUserId()).findFirst().orElse(null));
                        }
                    }
                }
                break;
        }
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if (snapshot.getKey().equals("Employee")) {
            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                Employee employee = currentSnapshot.getValue(Employee.class);
                listOfEmployees.add(employee);
                listOfSearchEmployees.add(employee);
            }
        }
        else if (snapshot.getKey().equals("User")) {
            for (DataSnapshot currentSnapshot : snapshot.getChildren()) {
                User user = currentSnapshot.getValue(User.class);
                listOfUsers.add(user);
                listOfSearchUsers.add(user);
            }
        }
        userAdapter.notifyDataSetChanged();
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
    public void onNothingSelected(AdapterView<?> parent) {

    }
}