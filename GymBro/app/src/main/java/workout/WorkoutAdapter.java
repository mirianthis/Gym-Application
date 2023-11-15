package workout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbro.ExerciseListAdapter;
import com.example.gymbro.R;

import java.util.List;

import databases.UserWorkouts;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.ViewHolder> {

    private final List<UserWorkouts> workoutsList;
    private final OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onItemClick(UserWorkouts workout);
    }

    public WorkoutAdapter(List<UserWorkouts> workoutsList, OnItemClickListener listener) {
        this.workoutsList = workoutsList;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserWorkouts workout = workoutsList.get(position);

        holder.workoutNameTextView.setText(workout.getName());
        holder.numExercisesTextView.setText("Number of Exercises: " + workout.getExercises().size());
        holder.dayTextView.setText("DAY " + (position + 1));

        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(workout);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workoutsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView workoutNameTextView;
        TextView numExercisesTextView;
        TextView dayTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            workoutNameTextView = itemView.findViewById(R.id.workoutNameTextView);
            numExercisesTextView = itemView.findViewById(R.id.numExercisesTextView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
        }
    }
}

