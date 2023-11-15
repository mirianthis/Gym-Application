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

public abstract class BaseActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // Set the listener for the bottom navigation menu
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }

    protected abstract int getContentViewLayout();

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");

            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent intent = new Intent(BaseActivity.this, HomeActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    break;
                case R.id.action_map:
                    Intent intent1 = new Intent(BaseActivity.this, MapsActivity.class);
                    intent1.putExtra("email", email);
                    intent1.putExtra("password", password);
                    startActivity(intent1);
                    return true;
                case R.id.action_utilities:
                    Intent intent2 = new Intent(BaseActivity.this, UtilitiesActivity.class);
                    intent2.putExtra("email", email);
                    intent2.putExtra("password", password);
                    startActivity(intent2);
                    return true;
                case R.id.action_exercises:
                    Intent intent3 = new Intent(BaseActivity.this, Exercises.class);
                    intent3.putExtra("email", email);
                    intent3.putExtra("password", password);
                    startActivity(intent3);
                    return true;
                case R.id.action_add_workout:
                    intent = new Intent(BaseActivity.this, WorkoutActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("password", password);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Set the selected item based on the current activity
        MenuItem menuItem = null;
        if (this instanceof HomeActivity) {
            menuItem = bottomNavigationView.getMenu().findItem(R.id.action_home);
        } else if (this instanceof MapsActivity) {
            menuItem = bottomNavigationView.getMenu().findItem(R.id.action_map);
        } else if (this instanceof UtilitiesActivity) {
            menuItem = bottomNavigationView.getMenu().findItem(R.id.action_utilities);
        } else if (this instanceof Exercises) {
            menuItem = bottomNavigationView.getMenu().findItem(R.id.action_exercises);
        } else if (this instanceof WorkoutActivity) {
            menuItem = bottomNavigationView.getMenu().findItem(R.id.action_add_workout);
        }

        if (menuItem != null) {
            menuItem.setChecked(true);
        }
    }
}
