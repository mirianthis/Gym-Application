package workout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.gymbro.R;
import com.example.gymbro.RealmConfigHelper;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import databases.ExerciseLibrary;
import databases.UserWorkouts;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

public class AddWorkoutFragment extends Fragment {

    private final String[] muscleGroups = {" ", "Legs", "Chest", "Back", "Shoulders", "Biceps", "Triceps", "Abdominals"};
    private Button addExerciseButton;
    private Credentials credentials;
    private App app;
    public String Appid = "gym-zovgl";
    private String email, password;
    private SyncConfiguration syncConfig;
    private String selectedMuscleGroup;

    public AddWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_program, container, false);

        // Check if arguments are not null before accessing email and password
        if (getArguments() != null) {
            email = getArguments().getString("email");
            password = getArguments().getString("password");
        } else {
            // Handle the situation when email and password are not passed correctly
            // For example, you can show an error message or navigate back to the login screen.
            // In this example, I'll simply log an error message.
            Log.e("AddWorkoutFragment", "Email and/or password arguments are null.");
            return view; // Return the view without further processing to avoid crashes
        }

        app = new App(new AppConfiguration.Builder(Appid).build());
        credentials = Credentials.emailPassword(email, password);
        RealmList<String> exercises = new RealmList<>();

        EditText name = view.findViewById(R.id.editTextName);

        // Spinner initialization
        Spinner muscleGroupSpinner1 = view.findViewById(R.id.muscleGroupSpinner1);
        Spinner exerciseSpinner1 = view.findViewById(R.id.exerciseSpinner1);
        Spinner muscleGroupSpinner2 = view.findViewById(R.id.muscleGroupSpinner2);
        Spinner exerciseSpinner2 = view.findViewById(R.id.exerciseSpinner2);
        Spinner muscleGroupSpinner3 = view.findViewById(R.id.muscleGroupSpinner3);
        Spinner exerciseSpinner3 = view.findViewById(R.id.exerciseSpinner3);
        Spinner muscleGroupSpinner4 = view.findViewById(R.id.muscleGroupSpinner4);
        Spinner exerciseSpinner4 = view.findViewById(R.id.exerciseSpinner4);
        Spinner muscleGroupSpinner5 = view.findViewById(R.id.muscleGroupSpinner5);
        Spinner exerciseSpinner5 = view.findViewById(R.id.exerciseSpinner5);
        Spinner muscleGroupSpinner6 = view.findViewById(R.id.muscleGroupSpinner6);
        Spinner exerciseSpinner6 = view.findViewById(R.id.exerciseSpinner6);
        Spinner muscleGroupSpinner7 = view.findViewById(R.id.muscleGroupSpinner7);
        Spinner exerciseSpinner7 = view.findViewById(R.id.exerciseSpinner7);
        Spinner muscleGroupSpinner8 = view.findViewById(R.id.muscleGroupSpinner8);
        Spinner exerciseSpinner8 = view.findViewById(R.id.exerciseSpinner8);

        Spinner[] muscleGroupSpinners = {
                muscleGroupSpinner1, muscleGroupSpinner2, muscleGroupSpinner3, muscleGroupSpinner4,
                muscleGroupSpinner5, muscleGroupSpinner6, muscleGroupSpinner7, muscleGroupSpinner8
        };

        Spinner[] exerciseSpinners = {
                exerciseSpinner1, exerciseSpinner2, exerciseSpinner3, exerciseSpinner4,
                exerciseSpinner5, exerciseSpinner6, exerciseSpinner7, exerciseSpinner8
        };

        ArrayAdapter<String>[] muscleGroupAdapters = new ArrayAdapter[8];
        ArrayAdapter<String>[] exerciseAdapters = new ArrayAdapter[8];

        for (int i = 0; i < 8; i++) {
            muscleGroupAdapters[i] = new ArrayAdapter<>(requireContext(), R.layout.color_spinner_layout, muscleGroups);
            exerciseAdapters[i] = new ArrayAdapter<>(requireContext(), R.layout.color_spinner_layout, exercises);

            muscleGroupAdapters[i].setDropDownViewResource(R.layout.spinner_dropdown_layout);
            exerciseAdapters[i].setDropDownViewResource(R.layout.spinner_dropdown_layout);

            muscleGroupSpinners[i].setAdapter(muscleGroupAdapters[i]);
            exerciseSpinners[i].setAdapter(exerciseAdapters[i]);

            final int finalI = i;
            muscleGroupSpinners[i].setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    selectedMuscleGroup = muscleGroups[position];
                    List<String> muscleGroupExercises = retrieveExercisesFromRealm(selectedMuscleGroup, exerciseAdapters[finalI]);
                    exerciseAdapters[finalI].clear();
                    exerciseAdapters[finalI].addAll(muscleGroupExercises);
                    exerciseAdapters[finalI].notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // Do nothing
                }
            });
        }

        addExerciseButton = view.findViewById(R.id.addExerciseButton);
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RealmList<String> exercisesList = new RealmList<>();
                RealmList<String> muscleGroupList = new RealmList<>();
                String name1 = name.getText().toString();

                if (isExerciseSelected(exerciseSpinner1, muscleGroupSpinner1, exercisesList, muscleGroupList)) {
                    isExerciseSelected(exerciseSpinner2, muscleGroupSpinner2, exercisesList, muscleGroupList);
                    isExerciseSelected(exerciseSpinner3, muscleGroupSpinner3, exercisesList, muscleGroupList);
                    isExerciseSelected(exerciseSpinner4, muscleGroupSpinner4, exercisesList, muscleGroupList);
                    isExerciseSelected(exerciseSpinner5, muscleGroupSpinner5, exercisesList, muscleGroupList);
                    isExerciseSelected(exerciseSpinner6, muscleGroupSpinner6, exercisesList, muscleGroupList);
                    isExerciseSelected(exerciseSpinner7, muscleGroupSpinner7, exercisesList, muscleGroupList);
                    isExerciseSelected(exerciseSpinner8, muscleGroupSpinner8, exercisesList, muscleGroupList);

                    saveExerciseToRealm(email, exercisesList, muscleGroupList, "Chest", name1);
                }
            }
        });

        return view;
    }

    private boolean isExerciseSelected(Spinner exerciseSpinner, Spinner muscleGroupSpinner, RealmList<String> exercisesList, RealmList<String> muscleGroupList) {
        String selectedExercise = (String) exerciseSpinner.getSelectedItem();
        String selectedMuscleGroup = (String) muscleGroupSpinner.getSelectedItem();

        if (selectedExercise != null && !selectedExercise.isEmpty()) {
            exercisesList.add(selectedExercise);
            muscleGroupList.add(selectedMuscleGroup);
            return true;
        }

        return false;
    }

    private List<String> retrieveExercisesFromRealm(String muscleGroup, final ArrayAdapter<String> exerciseAdapter) {
        // Create a new ArrayList to store the retrieved exercises
        List<String> retrievedExercises = new ArrayList<>();


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
                    Realm backgroundRealm = RealmConfigHelper.getRealmInstance(email, password, muscleGroup);

                    // Query the ExerciseLibrary collection to retrieve exercises for the selected muscle group
                    RealmResults<ExerciseLibrary> results = backgroundRealm.where(ExerciseLibrary.class)
                            .equalTo("Muscle_Group", muscleGroup)
                            .findAll();
                    Log.v("adf", "df:" + results.size());

                    // Iterate over the query results and add the exercises and gifs to the lists
                    for (ExerciseLibrary exercise : results) {
                        retrievedExercises.add(exercise.getExercise());
                    }

                    // Update the UI on the main UI thread
                    requireActivity().runOnUiThread(() -> {
                        // Clear the exerciseAdapter before populating it with the retrieved exercises
                        exerciseAdapter.clear();

                        // Add the retrieved exercises to the adapter
                        exerciseAdapter.addAll(retrievedExercises);

                        // Notify the adapter that the data set has changed
                        exerciseAdapter.notifyDataSetChanged();
                    });

                    backgroundRealm.close();
                });
            }
        });

        return retrievedExercises;
    }

    private void saveExerciseToRealm(String email, RealmList<String> exercise, RealmList<String> muscleList, String muscleGroup, String name) {

        Log.d("AddWorkoutFragment", "saveExerciseToRealm called"); // Add this line for debugging
        Log.d("AddWorkoutFragment", "Email: " + email);
        Log.d("AddWorkoutFragment", "Exercise: " + exercise.toString());
        Log.d("AddWorkoutFragment", "MuscleList: " + muscleList.toString());
        Log.d("AddWorkoutFragment", "MuscleGroup: " + muscleGroup);
        Log.d("AddWorkoutFragment", "Name: " + name);

        app.loginAsync(credentials, result -> {
            if (result.isSuccess()) {
                Log.v("QUICKSTART", "Successfully authenticated.");
                User user = app.currentUser();

                AsyncTask.execute(() -> {
                    Realm backgroundRealm = RealmConfigHelper.getRealmInstance(email, password, muscleGroup);

                    backgroundRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Log.d("AddWorkoutFragment", "Executing transaction...");
                            UserWorkouts workout = new UserWorkouts();
                            workout.setId(new ObjectId());
                            workout.setEmail(email);
                            workout.setExercises(exercise);
                            workout.setMuscleList(muscleList);
                            workout.setName(name);
                            realm.insert(workout);
                        }
                    });

                    backgroundRealm.close();
                });
            }
        });
    }
}
