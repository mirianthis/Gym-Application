package exercise_library;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gymbro.R;

import java.util.ArrayList;

import databases.ExerciseLibrary;

public class ResultsActivity extends AppCompatActivity {
    private ArrayList<ExerciseLibrary> dataList;
    private int currentItemIndex;
    private TextView textViewExercise, textViewMG, textViewULC, textViewLevel;
    private Button buttonNext;
    private ImageView ivGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Retrieve the Data list from intent extras
        Intent intent = getIntent();
        dataList = intent.getParcelableArrayListExtra("dataList");

        // Initialize the currentItemIndex
        currentItemIndex = 0;

        // Initialize the views
        textViewExercise = findViewById(R.id.textViewExercise);
        textViewMG = findViewById(R.id.textViewMuscleGroup);
        textViewULC = findViewById(R.id.textViewULC);
        textViewLevel = findViewById(R.id.textViewLevel);
        buttonNext = findViewById(R.id.buttonNext);
        ivGif = findViewById(R.id.ivGif);

        if (dataList != null && !dataList.isEmpty()) {
            // Set the details of the first item
            showDataDetails(currentItemIndex);

            // Set click listener for the Next button
            buttonNext.setOnClickListener(view -> {
                // Increment the currentItemIndex
                currentItemIndex++;

                // Check if there are more items in the list
                if (currentItemIndex < dataList.size()) {
                    // Show the details of the next item
                    showDataDetails(currentItemIndex);
                } else {
                    // All items have been shown, handle the end of the list
                    handleEndOfList();
                }
            });
        } else {
            // Empty or null dataList, handle accordingly
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            handleEndOfList();
        }
    }

    private void showDataDetails(int index) {
        // Get the data item at the specified index
        ExerciseLibrary data = dataList.get(index);

        // Display the details in the TextView
        String Exercise = "Exercise: " + data.getExercise();
        String MG = "Muscle Group: " + data.getMuscleGroup();
        String ULC =  "ULC: " + data.getULC();
        String Level = "Level: " + data.getLevel();
        Log.d("dei3e", "ds"+data.getGifs());

        textViewExercise.setText(Exercise);
        textViewMG.setText(MG);
        textViewULC.setText(ULC);
        textViewLevel.setText(Level);
        Glide.with(this)
                .asGif()
                .load(data.getGifs())
                .into(ivGif);
    }

    private void handleEndOfList() {
        // All items have been shown, handle the end of the list
        // For example, display a message or navigate back to the previous activity
        Toast.makeText(this, "End of the list reached", Toast.LENGTH_SHORT).show();
        finish(); // Finish the ResultsActivity and go back to the previous activity
    }
}
