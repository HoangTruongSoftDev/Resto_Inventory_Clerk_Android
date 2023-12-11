package com.example.resto_inventory_clerk_android;

import static model.ThresholdWarning.checkThresholdsAndShowWarnings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Optional;

import model.Item;
import model.ThresholdWarning;
import model.User;
import service.Validator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener {

    EditText edUserId, edPassword;
    Button btnLogin;
    ImageButton imageButtonShowPassword;

    RadioGroup rgPosition;
    RadioButton rbAdmin, rbManager, rbStaff;
    DatabaseReference userDatabase;
    ArrayList<User> listAllUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }
    private void initialize() {
        edUserId = findViewById(R.id.edUserId);
        btnLogin = findViewById(R.id.btnLogin);
        imageButtonShowPassword = findViewById(R.id.imageButtonShowPassword);
        rgPosition = findViewById(R.id.rgPosition);
        edPassword = findViewById(R.id.edPassword);
        rbAdmin = findViewById(R.id.rbAdmin);
        rbManager = findViewById(R.id.rbManager);
        rbStaff = findViewById(R.id.rbStaff);
        listAllUsers = new ArrayList<>();

        btnLogin.setOnClickListener(this);
        imageButtonShowPassword.setOnClickListener(this);
        userDatabase = FirebaseDatabase.getInstance().getReference("User");
        userDatabase.addChildEventListener(this);
    }
    @Override
    public void onClick(View v) {
        if (R.id.btnLogin == v.getId()) {
            login(v);
        }
        else if (R.id.imageButtonShowPassword == v.getId()) {

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

    private void login(View view) {
        if (edUserId.getText().toString().isEmpty()) {
            Snackbar.make(view, "Missing User ID", Snackbar.LENGTH_LONG).show();
            edUserId.requestFocus();
            return;
        }
        String position = rgPosition.getCheckedRadioButtonId() == R.id.rbAdmin ? "Admin" :
                                rgPosition.getCheckedRadioButtonId() == R.id.rbManager ? "Manager" :
                                    rgPosition.getCheckedRadioButtonId() == R.id.rbStaff ? "Staff" : null;
        if (position == null) {
            Snackbar.make(view, "Please select position", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (!Validator.isValidUserId(edUserId.getText().toString(), position)) {
           Snackbar.make(view, "Invalid User ID", Snackbar.LENGTH_LONG).show();
            edUserId.setText(null);
            edUserId.requestFocus();
            return;
        }
        if (edPassword.getText().toString().isEmpty()) {
            Snackbar.make(view, "Missing Password", Toast.LENGTH_LONG).show();
            edPassword.requestFocus();
            return;
        }
        User user = new User(Integer.valueOf(edUserId.getText().toString()), edPassword.getText().toString(), position);

        if (!listAllUsers.contains(user)) {
            Snackbar.make(view, "This account is invalid", Toast.LENGTH_LONG).show();
            edUserId.setText(null);
            edPassword.setText(null);
            return;
        }

        User existingUser = listAllUsers.stream().filter(u -> u.equals(user)).findFirst().orElse(null);
        if (!user.getPosition().equals(existingUser.getPosition())) {
            Snackbar.make(view, "This account position is invalid", Snackbar.LENGTH_LONG).show();
            edUserId.setText(null);
            edPassword.setText(null);
            return;
        }
        if (!user.getPassword().equals(existingUser.getPassword())) {
            Snackbar.make(view, "Your Password is incorrect", Toast.LENGTH_LONG).show();
            edPassword.setText(null);
            return;
        }
        Intent intent = null;
        switch (user.getPosition()) {
            case "Admin":
                intent = new Intent(this, AdminListActivity.class);
                break;
            case "Manager":
                intent = new Intent(this, ManagerItemListActivity.class);
                break;
            case "Staff":
                intent = new Intent(this, StaffItemListActivity.class);
                break;
        }
        startActivity(intent);
    }
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        User currentUser = snapshot.getValue(User.class);

        listAllUsers.add(currentUser);
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