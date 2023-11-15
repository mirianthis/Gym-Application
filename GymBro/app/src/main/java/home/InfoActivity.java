package home;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymbro.MainActivity;
import com.example.gymbro.R;
import com.example.gymbro.RealmConfigHelper;

import org.bson.types.ObjectId;

import java.util.Calendar;

import databases.UserInfo;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextHeight;
    private EditText editTextWeight;
    private Credentials credentials;
    private App app;
    public String Appid = "gym-zovgl";
    private String email,password,username;
    private Spinner spinnerActivityLevel;
    private Spinner spinnerGoal;
    private EditText editTextDateOfBirth;
    private Spinner spinnerGender;
    private Button buttonSave;

    private String Height,Weight,ActivityLevel,Goal,DateOfBirth,Gender,Muscle_Group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Realm.init(this);

        Muscle_Group = "Chest";
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        username = getIntent().getStringExtra("username");

        app = new App(new AppConfiguration.Builder(Appid).build());
        credentials = Credentials.emailPassword(email, password);

        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWeight = findViewById(R.id.editTextWeight);
        spinnerActivityLevel = findViewById(R.id.spinnerActivityLevel);
        spinnerGoal = findViewById(R.id.spinnerGoal);
        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);
        spinnerGender = findViewById(R.id.spinnerGender);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(this);
        editTextDateOfBirth.setOnClickListener(this);

        // Set up ArrayAdapter for spinnerActivityLevel
        ArrayAdapter activityLevelAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.activity_levels,
                R.layout.color_spinner_layout
        );
        activityLevelAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerActivityLevel.setAdapter(activityLevelAdapter);

        // Set up ArrayAdapter for spinnerGoal
        ArrayAdapter goalAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.goals,
                R.layout.color_spinner_layout
        );
        goalAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerGoal.setAdapter(goalAdapter);

        // Set up ArrayAdapter for spinnerGender
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.genders,
                R.layout.color_spinner_layout
        );
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinnerGender.setAdapter(genderAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateOfBirth) {
            showDatePicker();
        } else if (v.getId() == R.id.buttonSave) {
            Height = editTextHeight.getText().toString().trim();
            Weight = editTextWeight.getText().toString().trim();
            ActivityLevel = spinnerActivityLevel.getSelectedItem().toString();
            Goal = spinnerGoal.getSelectedItem().toString();
            DateOfBirth = editTextDateOfBirth.getText().toString().trim();
            Gender = spinnerGender.getSelectedItem().toString();
            saveInfo();
            saveExerciseToRealm(email,Height,Weight,ActivityLevel,Goal,DateOfBirth,Gender,username);
            Intent intent = new Intent(InfoActivity.this, HomeActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateOfBirth = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        editTextDateOfBirth.setText(dateOfBirth);
                    }
                },
                year,
                month,
                day
        );
        datePickerDialog.show();
    }

    private void saveInfo() {
        String height = editTextHeight.getText().toString().trim();
        String weight = editTextWeight.getText().toString().trim();
        String activityLevel = spinnerActivityLevel.getSelectedItem().toString();
        String goal = spinnerGoal.getSelectedItem().toString();
        String dateOfBirth = editTextDateOfBirth.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();

        // Perform validation or further processing here

        // Display a toast message to show the saved information
        String message = "Height: " + height +
                "\nWeight: " + weight +
                "\nActivity Level: " + activityLevel +
                "\nGoal: " + goal +
                "\nDate of Birth: " + dateOfBirth +
                "\nGender: " + gender +
                "\nUsername: " + username;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void saveExerciseToRealm(String email, String Height, String Weight, String ActivityLevel, String Goal,String DateOfBirth, String Gender, String username) {

        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
                User user = app.currentUser();

                AsyncTask.execute(() -> {
                    Realm backgroundRealm = RealmConfigHelper.getRealmInstance(email, password, "Chest");

                    backgroundRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            UserInfo info = new UserInfo();
                            info.set_id(new ObjectId());
                            info.setEmail(email);
                            info.setHeight(Height);
                            info.setWeight(Weight);
                            info.setActivityLevel(ActivityLevel);
                            info.setGoal(Goal);
                            info.setDateOfBirth(DateOfBirth);
                            info.setGender(Gender);
                            info.setMuscle_Group("Chest");
                            info.setUsername(username);
                            realm.insert(info);
                        }
                    });
                    backgroundRealm.close();
                });

            }
        });
    }

}
