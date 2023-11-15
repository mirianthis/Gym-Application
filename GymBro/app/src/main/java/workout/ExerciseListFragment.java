package workout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbro.ExerciseListAdapter;
import com.example.gymbro.R;
import com.example.gymbro.RealmConfigHelper;

import org.bson.types.ObjectId;

import java.util.List;

import databases.UserWorkouts;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class ExerciseListFragment extends Fragment {

    private RecyclerView recyclerView;
    private String workoutId,email,password;
    private Credentials credentials;
    private App app;
    public String Appid = "gym-zovgl";
    private SyncConfiguration syncConfig;

    public ExerciseListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_list, container, false);

        recyclerView = view.findViewById(R.id.exerciseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext())); // Set the LayoutManager




        Bundle args = getArguments();
        if (args != null) {
            workoutId = args.getString("workoutId");
            email = args.getString("email"); // Retrieve the email from arguments
            password = args.getString("password");


            app = new App(new AppConfiguration.Builder(Appid).build());
            credentials = Credentials.emailPassword(email, password);

            loadExercises();
        }

        return view;
    }


    private void loadExercises() {

        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                User user = app.currentUser();
                ObjectId workoutObjectId = new ObjectId(workoutId);

                // Create a sync configuration for Realm
                syncConfig = new SyncConfiguration.Builder(user, "Chest")
                        .waitForInitialRemoteData()
                        .build();

                AsyncTask.execute(() -> {
                    Realm backgroundRealm = RealmConfigHelper.getRealmInstance(email, password, "Chest");

                    // Query the UserWorkouts collection to retrieve all workouts for the user
                    RealmResults<UserWorkouts> exercises = backgroundRealm.where(UserWorkouts.class)
                            .equalTo("_id", workoutObjectId)
                            .findAll();


                    // Convert the RealmResults to a list to prevent access to a closed Realm instance
                    List<UserWorkouts> workoutsList = backgroundRealm.copyFromRealm(exercises);

                    ExerciseListAdapter adapter = new ExerciseListAdapter(workoutsList,email,password);

                    requireActivity().runOnUiThread(() -> {
                        recyclerView.setAdapter(adapter);
                    });

                    // Close the Realm instance after use
                    backgroundRealm.close();
                });
            }
        });
    }


}
