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
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import model.User;

public class AdminListActivity extends AppCompatActivity implements View.OnClickListener, ChildEventListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ImageButton imageButtonAdd;
    ListView lvUsers;
    Button btnLogOut;

    ArrayAdapter<User> userAdapter;
    ArrayList<User> listOfUsers;

    DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        initialize();
    }

    private void initialize() {
        imageButtonAdd = findViewById(R.id.imageButtonAdd);
        btnLogOut = findViewById(R.id.btnLogOut);

        imageButtonAdd.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        listOfUsers = new ArrayList<>();
        userDatabase = FirebaseDatabase.getInstance().getReference("User");
        userDatabase.addChildEventListener(this);

        lvUsers = findViewById(R.id.lvUsers);
        lvUsers.setOnItemLongClickListener(this);
        lvUsers.setOnItemClickListener(this);
        userAdapter = new ArrayAdapter<>(this, R.layout.single_user_layout, listOfUsers);
        lvUsers.setAdapter(userAdapter);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButtonAdd) {

        }
        else if (view.getId() == R.id.btnLogOut) {

        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        User user = snapshot.getValue(User.class);
        listOfUsers.add(user);
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


}