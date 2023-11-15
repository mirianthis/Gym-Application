package utilities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbro.R;

public class OneRepMaxCalculatorActivity extends AppCompatActivity {

    private EditText currentWeightEditText, currentRepsEditText;
    private int currentWeight = 80;
    private int currentReps = 5;
    private TextView result1RmTextView, result5RmTextView, result2RmTextView, result3RmTextView,
            result4RmTextView, result6RmTextView, result7RmTextView, result8RmTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_rep_max_calculator);

        currentWeightEditText = findViewById(R.id.currentweight);
        currentRepsEditText = findViewById(R.id.currentreps);

        result1RmTextView = findViewById(R.id.result1rm);
        result5RmTextView = findViewById(R.id.result5rm);
        result2RmTextView = findViewById(R.id.result2rm);
        result3RmTextView = findViewById(R.id.result3rm);
        result4RmTextView = findViewById(R.id.result4rm);
        result6RmTextView = findViewById(R.id.result6rm);
        result7RmTextView = findViewById(R.id.result7rm);
        result8RmTextView = findViewById(R.id.result8rm);

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button calculateButton = findViewById(R.id.calculatebmi);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndDisplayRepMaxes();
            }
        });

        // Set listeners for weight increment and decrement buttons
        ImageView incrementWeightButton = findViewById(R.id.incrementweight);
        ImageView decrementWeightButton = findViewById(R.id.decrementweight);

        incrementWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment weight logic
                currentWeight++;
                currentWeightEditText.setText(String.valueOf(currentWeight));
            }
        });

        decrementWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement weight logic
                if (currentWeight > 0) {
                    currentWeight--;
                    currentWeightEditText.setText(String.valueOf(currentWeight));
                }
            }
        });

        // Set listeners for reps increment and decrement buttons
        ImageView incrementRepsButton = findViewById(R.id.incrementreps);
        ImageView decrementRepsButton = findViewById(R.id.decrementreps);

        incrementRepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increment reps logic
                currentReps++;
                currentRepsEditText.setText(String.valueOf(currentReps));
            }
        });

        decrementRepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrement reps logic
                if (currentReps > 1) {
                    currentReps--;
                    currentRepsEditText.setText(String.valueOf(currentReps));
                }
            }
        });
    }

    private void calculateAndDisplayRepMaxes() {
        double weightKg = Double.parseDouble(currentWeightEditText.getText().toString());
        int reps = Integer.parseInt(currentRepsEditText.getText().toString());

        // Example calculation: 1RM = Weight * (1 + (0.0333 * Reps))
        double oneRepMaxKg = weightKg * (1 + (0.0333 * reps));
        double fiveRepMaxKg = oneRepMaxKg * 0.85;
        double twoRepMaxKg = oneRepMaxKg * 0.95;
        double threeRepMaxKg = oneRepMaxKg * 0.90;
        double fourRepMaxKg = oneRepMaxKg * 0.88;
        double sixRepMaxKg = oneRepMaxKg * 0.80;
        double sevenRepMaxKg = oneRepMaxKg * 0.77;
        double eightRepMaxKg = oneRepMaxKg * 0.75;

        result1RmTextView.setText(String.format("%.2f kg", oneRepMaxKg));
        result5RmTextView.setText(String.format("%.2f kg", fiveRepMaxKg));
        result2RmTextView.setText(String.format("2RM:%.2f kg", twoRepMaxKg));
        result3RmTextView.setText(String.format("3RM: %.2f kg", threeRepMaxKg));
        result4RmTextView.setText(String.format("4RM: %.2f kg", fourRepMaxKg));
        result6RmTextView.setText(String.format("6RM: %.2f kg", sixRepMaxKg));
        result7RmTextView.setText(String.format("7RM: %.2f kg", sevenRepMaxKg));
        result8RmTextView.setText(String.format("8RM: %.2f kg", eightRepMaxKg));
    }



}

