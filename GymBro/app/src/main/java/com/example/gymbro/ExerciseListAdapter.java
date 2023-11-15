package com.example.gymbro;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbro.R;

import org.bson.types.ObjectId;

import java.util.List;

import databases.ExerciseLibrary;
import databases.UserWorkouts;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;
import workout.ExerciseItemAdapter;
import workout.GifRetrievalCallback;

public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ExerciseViewHolder> {
    private List<UserWorkouts> exercisesList;
    private String email,password;

    public ExerciseListAdapter(List<UserWorkouts> exercisesList,String email, String password) {
        this.exercisesList = exercisesList;
        this.email = email;
        this.password = password;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_list_item, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        UserWorkouts workout = exercisesList.get(position);

        holder.exerciseNameTextView.setText(workout.getName());

        ExerciseItemAdapter adapter = new ExerciseItemAdapter(workout.getMuscleList(),workout.getExercises(), email, password, new GifRetrievalCallback() {
            @Override
            public void onGifsRetrieved(String gifUrl) {
                // This callback will be triggered when GIFs are retrieved
                // Do any additional processing or UI updates here
            }
        });
        holder.exerciseRecyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return exercisesList.size();
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView exerciseNameTextView;
        RecyclerView exerciseRecyclerView;

        ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            exerciseNameTextView = itemView.findViewById(R.id.exerciseNameTextView);
            exerciseRecyclerView = itemView.findViewById(R.id.exerciseRecyclerView);

            // Set layout manager and adapter for exerciseRecyclerView
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            exerciseRecyclerView.setLayoutManager(layoutManager);
        }
    }


}


