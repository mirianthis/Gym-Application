package maps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.gymbro.BaseActivity;
import com.example.gymbro.R;

import io.realm.Realm;

public class MapsActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_maps);

        // Initialize Realm
        Realm.init(this);

        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

    }
    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_maps; // Replace with your layout ID
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                startActivity(new Intent(MapsActivity.this, GymMapsActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(MapsActivity.this, SupplementMapsActivity.class));
                break;
            case R.id.button3:
                startActivity(new Intent(MapsActivity.this, ClothingMapsActivity.class));
                break;
        }
    }
}