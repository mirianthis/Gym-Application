package exercise_library;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbro.BaseActivity;
import com.example.gymbro.R;

import java.util.ArrayList;

import databases.ExerciseLibrary;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class Exercises extends BaseActivity {

    public String Appid = "gym-zovgl";

    private static final int REQUEST_LEVEL_SELECTION = 1;
    private Credentials credentials;
    private SyncConfiguration syncConfig;
    private App app;
    private Button chestButton;
    private Button legsButton;
    private Button backButton;
    private Button shouldersButton;
    private Button bicepsButton;
    private Button tricepsButton;
    private Button absButton;
    private String type,email,password;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_exercises;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_exercises);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        chestButton = findViewById(R.id.bChest);
        legsButton = findViewById(R.id.bLegs);
        backButton = findViewById(R.id.bBack);
        shouldersButton = findViewById(R.id.bShoulders);
        bicepsButton = findViewById(R.id.bBiceps);
        tricepsButton = findViewById(R.id.bTriceps);
        absButton = findViewById(R.id.bAbs);

        Realm.init(this);


        // Initialize the MongoDB Realm app with the partitionKey
        app = new App(new AppConfiguration.Builder(Appid).build());

        // Authenticate the user with email and password
        credentials = Credentials.emailPassword(email, password);


        chestButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Chest");
        });

        backButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Back");
        });

        legsButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Legs");
        });

        shouldersButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Shoulders");
        });

        bicepsButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Biceps");
        });

        tricepsButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Triceps");
        });

        absButton.setOnClickListener(view -> {
            openLevelSelectionActivity("Abdominals");
        });
    }

    private void performRealmOperations(String selectedLevel) {
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
                User user = app.currentUser();

                // Create a sync configuration for Realm
                syncConfig = new SyncConfiguration.Builder(app.currentUser(), type)
                        .waitForInitialRemoteData()
                        .build();

                // Delete the client file to resolve synchronization issues (if needed)
                Realm.deleteRealm(syncConfig);

                Log.v("value", "its:"+selectedLevel);

                AsyncTask.execute(() -> {
                    Realm backgroundRealm = Realm.getInstance(syncConfig);

                    RealmResults<ExerciseLibrary> data = backgroundRealm.where(ExerciseLibrary.class).equalTo("Level", selectedLevel)
                            .findAll();

                    Log.v("ds", "data:" + data.size());

                    ArrayList<ExerciseLibrary> dataList = new ArrayList<>(backgroundRealm.copyFromRealm(data));

                    Intent intent = new Intent(Exercises.this, ResultsActivity.class);
                    intent.putParcelableArrayListExtra("dataList", dataList);
                    startActivity(intent);
                    backgroundRealm.close();
                });
            } else {
                Log.e("QUICKSTART", "Failed to log in.", result.getError());
            }
        });
    }

    private void openLevelSelectionActivity(String partitionKey) {
        type = partitionKey;
        Intent intent = new Intent(Exercises.this, LevelSelectionActivity.class);
        intent.putExtra("partitionKey", partitionKey);
        startActivityForResult(intent, REQUEST_LEVEL_SELECTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LEVEL_SELECTION && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("selectedLevel")) {
                String selectedLevel = data.getStringExtra("selectedLevel");
                performRealmOperations(selectedLevel);
            }
        }
    }
}

