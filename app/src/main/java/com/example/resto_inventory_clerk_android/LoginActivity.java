package com.example.resto_inventory_clerk_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edUserId, edPassword;
    Button btnLogin;
    ImageButton imageButtonShowPassword;
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
        edPassword = findViewById(R.id.edPassword);

        btnLogin.setOnClickListener(this);
        imageButtonShowPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (R.id.btnLogin == v.getId()) {

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
}