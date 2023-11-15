package exercise_library;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbro.R;

public class LevelSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selection);

        Button beginnerButton = findViewById(R.id.beginnerButton);
        Button intermediateButton = findViewById(R.id.intermediateButton);
        Button advancedButton = findViewById(R.id.advancedButton);

        beginnerButton.setOnClickListener(view -> handleLevelSelection("Beginner"));

        intermediateButton.setOnClickListener(view -> handleLevelSelection("Intermediate"));

        advancedButton.setOnClickListener(view -> handleLevelSelection("Advanced"));

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void handleLevelSelection(String selectedLevel) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedLevel", selectedLevel);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
