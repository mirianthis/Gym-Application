package utilities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbro.BaseActivity;
import com.example.gymbro.R;

import io.realm.Realm;

public class UtilitiesActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_utilities; // Replace with your layout ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_utilities);

        // Initialize Realm
        Realm.init(this);

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        //Button button5 = findViewById(R.id.button5);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        //button5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startActivity(new Intent(UtilitiesActivity.this, BMICalculatorActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(UtilitiesActivity.this, OneRepMaxCalculatorActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(UtilitiesActivity.this, BodyFatCalculatorActivity.class));
                break;
            case R.id.button4:
                startActivity(new Intent(UtilitiesActivity.this, TimerActivity.class));
                break;
        }
    }
}
