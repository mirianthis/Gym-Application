    package workout;

    import android.os.AsyncTask;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.gymbro.R;
    import com.example.gymbro.RealmConfigHelper;

    import java.util.ArrayList;
    import java.util.List;

    import databases.ExerciseLibrary;
    import databases.UserWorkouts;
    import io.realm.Realm;
    import io.realm.RealmResults;
    import io.realm.mongodb.App;
    import io.realm.mongodb.AppConfiguration;
    import io.realm.mongodb.Credentials;
    import io.realm.mongodb.User;
    import io.realm.mongodb.sync.SyncConfiguration;

    public class ExerciseItemAdapter extends RecyclerView.Adapter<ExerciseItemAdapter.ExerciseViewHolder> {
        private List<String> exercises,muscleList;
        private List<String> gifs;
        private RecyclerView recyclerView;
        private Credentials credentials;
        private App app;
        public String Appid = "gym-zovgl";
        private String email, password;
        private SyncConfiguration syncConfig;
        private GifRetrievalCallback callback;


        public ExerciseItemAdapter(List<String> muscleList,List<String> exercises, String email, String password, GifRetrievalCallback callback) {
            this.muscleList = muscleList;
            this.exercises = exercises;
            this.email = email;
            this.password = password;
            this.callback = callback;
        }


        @NonNull
        @Override
        public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
            return new ExerciseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {

            app = new App(new AppConfiguration.Builder(Appid).build());
            credentials = Credentials.emailPassword(email, password);

            String exercise = exercises.get(position);
            String muscle = muscleList.get(position);
            holder.exerciseNameTextView.setText(exercise);
            holder.repsSetsTextView.setText("3 x 12 reps                rest 1 min");

            retrieveGifsFromRealm(muscle,exercise, new GifRetrievalCallback() {
                @Override
                public void onGifsRetrieved(String gifUrl) {
                    if (gifUrl != null) {
                        // Load GIF as static image using Glide on the main UI thread
                        holder.itemView.post(new Runnable() {
                            @Override
                            public void run() {
                                Glide.with(holder.itemView.getContext())
                                        .asGif()
                                        .load(gifUrl)
                                        .into(holder.exerciseImageView);
                            }
                        });
                    }
                }
            });

        }


        @Override
        public int getItemCount() {
            return exercises.size();
        }

        static class ExerciseViewHolder extends RecyclerView.ViewHolder {
            TextView exerciseNameTextView;
            TextView repsSetsTextView;
            ImageView exerciseImageView;

            ExerciseViewHolder(@NonNull View itemView) {
                super(itemView);
                exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
                repsSetsTextView = itemView.findViewById(R.id.repsSetsTextView);
                exerciseImageView = itemView.findViewById(R.id.exerciseImageView);
            }
        }

        private void retrieveGifsFromRealm(String muscle,String exercise, GifRetrievalCallback callback) {
            app.loginAsync(credentials, result -> {
                if (result.isSuccess()) {
                    User user = app.currentUser();

                    // Create a sync configuration for Realm
                    syncConfig = new SyncConfiguration.Builder(user, muscle)
                            .waitForInitialRemoteData()
                            .build();

                    // Delete the client file to resolve synchronization issues (if needed)
                    // Realm.deleteRealm(syncConfig);

                    AsyncTask.execute(() -> {
                        Realm backgroundRealm = Realm.getInstance(syncConfig);

                        // Query the ExerciseLibrary collection to retrieve the exercise's GIF
                        ExerciseLibrary workout = backgroundRealm.where(ExerciseLibrary.class)
                                .equalTo("Exercise", exercise)
                                .findFirst();

                        if (workout != null) {
                            String gifUrl = workout.getGifs();
                            // Perform Glide operation on the main thread
                            Log.d("gif","name="+gifUrl);
                            callback.onGifsRetrieved(gifUrl);
                        }

                        // Close the Realm instance after use
                        backgroundRealm.close();
                    });
                }
            });
        }


    }



