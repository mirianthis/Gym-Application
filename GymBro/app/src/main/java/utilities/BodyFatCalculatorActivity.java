package utilities;

import static java.lang.Math.log10;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gymbro.R;

public class BodyFatCalculatorActivity extends AppCompatActivity {

    private TextView currentHeight, currentWeight, currentWaist, currentHip, currentNeck, resultTextView;
    private SeekBar seekBarForHeight, seekBarForWeight, seekBarForWaist, seekBarForHip, seekBarForNeck;
    private Button calculateButton;
    private RelativeLayout maleImageView, femaleImageView;

    private String typerofuser="0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_fat_calculator);

        // Initialize UI components
        resultTextView = findViewById(R.id.tvresults);
        currentHeight = findViewById(R.id.currentheight);
        currentWeight = findViewById(R.id.currentweight);
        currentWaist = findViewById(R.id.currentwaist);
        currentHip = findViewById(R.id.currenthip);
        currentNeck = findViewById(R.id.currentneck);
        seekBarForHeight = findViewById(R.id.seekbarforheight);
        seekBarForWeight = findViewById(R.id.seekbarforweight);
        seekBarForWaist = findViewById(R.id.seekbarforwaist);
        seekBarForHip = findViewById(R.id.seekbarforhip);
        seekBarForNeck = findViewById(R.id.seekbarforneck);
        calculateButton = findViewById(R.id.calculatebodyfat);
        maleImageView = findViewById(R.id.male);
        femaleImageView = findViewById(R.id.female);

        View backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Set up listeners
        setupSeekBarListeners();
        setupCalculateButtonListener();
        setupGenderSelection();
    }

    private void setupSeekBarListeners() {
        seekBarForHeight.setOnSeekBarChangeListener(createSeekBarListener(currentHeight,300));
        seekBarForWeight.setOnSeekBarChangeListener(createSeekBarListener(currentWeight,200));
        seekBarForWaist.setOnSeekBarChangeListener(createSeekBarListener(currentWaist,200));
        seekBarForHip.setOnSeekBarChangeListener(createSeekBarListener(currentHip,200));
        seekBarForNeck.setOnSeekBarChangeListener(createSeekBarListener(currentNeck,100));
    }

    private SeekBar.OnSeekBarChangeListener createSeekBarListener(TextView textView, int maxValue) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double scaledProgress = (double) progress / seekBar.getMax() * maxValue;
                textView.setText(String.format("%.0f", scaledProgress)); // Display as integer
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for now
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for now
            }
        };
    }


    private void setupCalculateButtonListener() {
        calculateButton.setOnClickListener(v -> calculateAndDisplayBodyFat());
    }

    private void setupGenderSelection() {
        maleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalefucus));
                femaleImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalenotfucus));
                typerofuser="Male";

            }
        });


        femaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                femaleImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalefucus));
                maleImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.malefemalenotfucus));
                typerofuser="Female";
            }
        });
    }

    private void calculateAndDisplayBodyFat() {
        // Get values from SeekBars and TextViews
        int height = seekBarForHeight.getProgress();
        int weight = seekBarForWeight.getProgress();
        int waist = seekBarForWaist.getProgress();
        int hip = seekBarForHip.getProgress();
        int neck = seekBarForNeck.getProgress();

        // Calculate body fat percentage based on the obtained values
        double bodyFatPercentage = calculateBodyFatPercentage(weight, waist, hip, neck, height);

        // Display the calculated body fat percentage in the resultTextView
        resultTextView.setText(String.format("%.2f%%", bodyFatPercentage));
    }

    private double calculateBodyFatPercentage(int weight, int waist, int hip, int neck, int height) {
        double bodyFatPercentage = 0; // Initialize the variable here

        if (typerofuser.equals("Male")) {
            // Calculate the constant factor based on desired body fat percentage
            bodyFatPercentage = 495/(1.0324-0.19077*log10(waist-neck)+0.15456*log10(height))-450;

        } else {
            bodyFatPercentage = 495/(1.29579-0.35004*log10(waist+hip-neck)+0.22100*log10(height))-450;

        }

        return bodyFatPercentage;
    }



}
