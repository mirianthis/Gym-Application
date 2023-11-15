package com.example.gymbro;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import home.InfoActivity;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;


public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    private App realmApp;
    private final String APP_ID = "gym-zovgl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Realm.init(this);
        //Toast.makeText(RegisterActivity.this, "Test toast", Toast.LENGTH_SHORT).show();

        usernameEditText = findViewById(R.id.editTextUsername);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        registerButton  = findViewById(R.id.buttonRegister);

        usernameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    usernameEditText.setText(""); // Clear the text when EditText gains focus
                }
            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    emailEditText.setText(""); // Clear the text when EditText gains focus
                }
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    passwordEditText.setText(""); // Clear the text when EditText gains focus
                    passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        // Initialize the MongoDB Realm app
        realmApp = new App(new AppConfiguration.Builder(APP_ID)
                .appName("my-app")
                .build());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test","The button is clicked");
                // Perform registration
                performRegistration();
            }
        });
    }

    private void performRegistration() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            //Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(RegisterActivity.this, "Invalid email format. Please enter a valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6 || password.length() > 128) {
            Toast.makeText(RegisterActivity.this, "Password must be between 6 and 128 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform registration using MongoDB Realm
        realmApp.getEmailPassword().registerUserAsync(email, password, result -> {
            if (result.isSuccess()) {
                // Registration successful
                Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, InfoActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("username", username);
                startActivity(intent);
            } else {
                // Registration failed
                Toast.makeText(RegisterActivity.this, "Error registering user. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}


