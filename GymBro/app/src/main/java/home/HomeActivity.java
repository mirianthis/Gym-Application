package home;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gymbro.BaseActivity;
import com.example.gymbro.R;
import com.example.gymbro.RealmConfigHelper;

import java.util.ArrayList;

import databases.UserInfo;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class HomeActivity extends BaseActivity {

    private TextView textViewHeight,textViewWeight,textViewActivityLevel,textViewGoal,textViewDateOfBirth,textViewGender,textViewUsername,textViewBMI,textViewBodyFat;
    private ImageView imageViewAvatar;
    private App app;
    private String email,password;
    public String Appid = "gym-zovgl";
    private SyncConfiguration syncConfig;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_home; // Replace with your layout ID
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);

        // Initialize Realm
        Realm.init(this);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        app = new App(new AppConfiguration.Builder(Appid).build());
        Credentials credentials = Credentials.emailPassword(email, password);


        textViewHeight = findViewById(R.id.textViewHeight);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewActivityLevel = findViewById(R.id.textViewActivityLevel);
        textViewGoal = findViewById(R.id.textViewGoal);
        textViewDateOfBirth = findViewById(R.id.textViewDateOfBirth);
        textViewGender = findViewById(R.id.textViewGender);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewBMI = findViewById(R.id.textViewBMI);
        textViewBodyFat = findViewById(R.id.textViewBodyFat);

        // Retrieve the user's information from Realm database

        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
                User user = app.currentUser();

                Log.v("value", "its:"+email);

                syncConfig = new SyncConfiguration.Builder(user, "Chest")
                        .waitForInitialRemoteData()
                        .build();

                // Delete the client file to resolve synchronization issues (if needed)
                //Realm.deleteRealm(syncConfig);

                AsyncTask.execute(() -> {
                    Realm backgroundRealm = RealmConfigHelper.getRealmInstance(email, password, "Chest");

                    RealmResults<UserInfo> info = backgroundRealm.where(UserInfo.class).equalTo("Email",email)
                            .findAll();

                    Log.v("ds", "data:" + info.size());
                    ArrayList<UserInfo> infoList = new ArrayList<>(backgroundRealm.copyFromRealm(info));
                    UserInfo userInfo = infoList.get(0);



                    String height = userInfo.getHeight();
                    String weight = userInfo.getWeight();
                    String activityLevel = userInfo.getActivityLevel();
                    String goal = userInfo.getGoal();
                    String dateOfBirth = userInfo.getDateOfBirth();
                    String gender = userInfo.getGender();
                    String username = userInfo.getUsername();
                    double bmiValue = Double.parseDouble(weight) / ((Double.parseDouble(height) / 100.0) * (Double.parseDouble(height) / 100.0));


                    imageViewAvatar = findViewById(R.id.imageViewAvatar); // Find the ImageView

                    int avatarResId = R.drawable.male_avatar; // Default to male avatar
                    if (gender.equalsIgnoreCase("Female")) {
                        avatarResId = R.drawable.female_avatar;
                        imageViewAvatar.setImageResource(avatarResId); // Set avatar image
                    }else{
                        imageViewAvatar.setImageResource(avatarResId); // Set avatar image
                    }


                    runOnUiThread(() -> {
                        // Populate the UI elements with the retrieved information
                        textViewUsername.setText("Username: " + username);
                        textViewHeight.setText("Height: " + height);
                        textViewWeight.setText("Weight: " + weight);
                        textViewActivityLevel.setText("Activity Level: " + activityLevel);
                        textViewGoal.setText("Goal: " + goal);
                        textViewDateOfBirth.setText("Date of Birth: " + dateOfBirth);
                        textViewGender.setText("Gender: " + gender);

                        textViewBMI.setText("BMI: " + String.format("%.2f", bmiValue));

                        if(bmiValue<16)
                        {
                            textViewBodyFat.setText("Result: Severe Thinness");
                        }
                        else if(bmiValue<16.9 && bmiValue>16)
                        {
                            textViewBodyFat.setText("Result: Moderate Thinness");
                        }
                        else if(bmiValue<18.4 && bmiValue>17)
                        {
                            textViewBodyFat.setText("Result: Mild Thinness");
                        }
                        else if(bmiValue<24.9 && bmiValue>18.5 )
                        {
                            textViewBodyFat.setText("Result: Normal");
                        }
                        else if(bmiValue <29.9 && bmiValue>25)
                        {
                            textViewBodyFat.setText("Result: Overweight");
                        }
                        else if(bmiValue<34.9 && bmiValue>30)
                        {
                            textViewBodyFat.setText("Result: Obese Class I");
                        }
                        else
                        {
                            textViewBodyFat.setText("Result: Obese Class II");
                        }
                    });
                    backgroundRealm.close();
                });
            } else {
                Log.e("QUICKSTART", "Failed to log in.", result.getError());
            }
        });
    }
}