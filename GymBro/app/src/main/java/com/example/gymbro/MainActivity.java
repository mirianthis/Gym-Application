package com.example.gymbro;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

import exercise_library.Exercises;
import home.HomeActivity;
import maps.MapsActivity;
import utilities.UtilitiesActivity;
import workout.WorkoutActivity;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set the listener for the bottom navigation menu
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");

            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent intent1 = new Intent(MainActivity.this, HomeActivity.class);
                    intent1.putExtra("email", email);
                    intent1.putExtra("password", password);
                    startActivity(intent1);
                    break;
                case R.id.action_map:
                    startActivity(new Intent(MainActivity.this, MapsActivity.class));
                    return true;
                case R.id.action_utilities:
                    startActivity(new Intent(MainActivity.this, UtilitiesActivity.class));
                    return true;
                case R.id.action_exercises:
                    Intent intent = new Intent(MainActivity.this, Exercises.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    return true;
                case R.id.action_add_workout:
                    intent = new Intent(MainActivity.this, WorkoutActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };



}



