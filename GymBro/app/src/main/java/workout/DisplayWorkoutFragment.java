package workout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbro.ExerciseListAdapter;
import com.example.gymbro.R;
import com.example.gymbro.RealmConfigHelper;

import java.util.List;

import databases.UserWorkouts;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class DisplayWorkoutFragment extends Fragment implements WorkoutAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private Credentials credentials;
    private App app;
    public String Appid = "gym-zovgl";
    private String email, password;
    private SyncConfiguration syncConfig;

    public DisplayWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_display_workout, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));


        email = getArguments().getString("email");
        password = getArguments().getString("password");

        app = new App(new AppConfiguration.Builder(Appid).build());
        credentials = Credentials.emailPassword(email, password);

        retrieveWorkoutsFromRealm();

        return view;
    }

    private void retrieveWorkoutsFromRealm() {
        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                User user = app.currentUser();

                // Create a sync configuration for Realm
                syncConfig = new SyncConfiguration.Builder(user, "Chest")
                        .waitForInitialRemoteData()
                        .build();

                // Delete the client file to resolve synchronization issues (if needed)
                //Realm.deleteRealm(syncConfig);

                AsyncTask.execute(() -> {
                    Realm backgroundRealm = RealmConfigHelper.getRealmInstance(email, password, "Chest");

                    // Query the UserWorkouts collection to retrieve all workouts for the user
                    RealmResults<UserWorkouts> workouts = backgroundRealm.where(UserWorkouts.class)
                            .equalTo("Email", email)
                            .findAll();

                    // Convert the RealmResults to a list to prevent access to a closed Realm instance
                    List<UserWorkouts> workoutsList = backgroundRealm.copyFromRealm(workouts);

                    // Create and set the custom adapter
                    WorkoutAdapter adapter = new WorkoutAdapter(workoutsList,this);

                    // Update the UI on the main UI thread
                    requireActivity().runOnUiThread(() -> {
                        recyclerView.setAdapter(adapter);
                    });

                    // Close the Realm instance after use
                    backgroundRealm.close();
                });
            }
        });
    }

    @Override
    public void onItemClick(UserWorkouts workout) {
        Log.d("WorkoutClick", "Workout item clicked: " + workout.getName());
        // Replace the current fragment with the ExerciseListFragment and pass workout details
        ExerciseListFragment fragment = new ExerciseListFragment();

        Bundle args = new Bundle();
        args.putString("workoutId", workout.getId().toString()); // Pass the workout ID or any other details
        args.putString("email", email); // Pass the email to ExerciseListFragment
        args.putString("password", password);
        fragment.setArguments(args);

        // Replace the current fragment with the new fragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        Log.d("FragmentTransaction", "Replacing fragment with ExerciseListFragment");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment); // Use the container ID you defined in XML
        transaction.addToBackStack(null); // Add to back stack
        transaction.commit();

        // Toggle the visibility of the fragmentContainer
        View fragmentContainer = requireActivity().findViewById(R.id.fragmentContainer);
        fragmentContainer.setVisibility(View.VISIBLE);

        Log.d("FragmentTransaction", "Fragment transaction executed");
    }

}
