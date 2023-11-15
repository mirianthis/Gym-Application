package workout;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.example.gymbro.R;
import com.example.gymbro.RealmConfigHelper;

import java.util.ArrayList;
import java.util.List;

import databases.UserWorkouts;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;


public class SuggestedWorkoutFragment extends Fragment implements WorkoutAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private Credentials credentials;
    private App app;
    public String Appid = "gym-zovgl";
    private String email, password,email1,password1;
    private SyncConfiguration syncConfig;

    public SuggestedWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggested_workout, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        email = getArguments().getString("email");
        password = getArguments().getString("password");

        // Find the Spinner in the onCreateView method
        Spinner dietSpinner = view.findViewById(R.id.GoalSpinner);

        // Create an ArrayAdapter to populate the Spinner with diet choices
        ArrayAdapter<String> dietAdapter = new ArrayAdapter<>(requireContext(), R.layout.color_spinner_layout, new String[]{"Cutting", "Maintaining", "Bulking"});
        dietAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        dietSpinner.setAdapter(dietAdapter);

        // Set a listener to handle the selected goal choice
        dietSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedDiet = parentView.getItemAtPosition(position).toString();
                if(selectedDiet == "Maintaining"){
                    email1 = "maintaining@gmail.com";
                    password1 = "maintaining";
                }else if(selectedDiet == "Cutting"){
                    email1 = "cutting@gmail.com";
                    password1 = "cutting";
                }else{
                    email1 = "bulking@gmail.com";
                    password1 = "bulking";
                }

                app = new App(new AppConfiguration.Builder(Appid).build());
                credentials = Credentials.emailPassword(email, password);

                retrieveWorkoutsFromRealm();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

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
                            .equalTo("Email", email1)
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
