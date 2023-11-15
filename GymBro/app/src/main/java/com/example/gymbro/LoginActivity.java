package com.example.gymbro;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import home.HomeActivity;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    // MongoDB Realm app credentials
    private final String APP_ID = "gym-zovgl";

    private App realmApp;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Realm.init(this);

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonRegister);

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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform login authentication
                performLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the register activity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();


        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        realmApp = new App(new AppConfiguration.Builder(APP_ID)
                .build());


        // Perform authentication using MongoDB Realm
        Credentials credentials = Credentials.emailPassword(email, password);
        realmApp.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                // User authenticated successfully, navigate to the next activity
                User user = realmApp.currentUser();
                if (user != null) {
                    // TODO: Perform necessary actions after successful login
                    Intent intent1 = new Intent(LoginActivity.this, HomeActivity.class);
                    intent1.putExtra("email", email);
                    intent1.putExtra("password", password);
                    startActivity(intent1);
                    finish(); // Finish the current activity to prevent going back to the login screen
                }
            } else {
                // Authentication failed, display error message
                Toast.makeText(LoginActivity.this, "Invalid email or password. Please try again or register.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



