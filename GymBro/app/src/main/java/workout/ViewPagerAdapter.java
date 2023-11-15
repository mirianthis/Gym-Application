package workout;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import workout.AddWorkoutFragment;
import workout.DisplayWorkoutFragment;
import workout.SuggestedWorkoutFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final String email;
    private final String password;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, String email, String password) {
        super(fragmentActivity);
        this.email = email;
        this.password = password;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                AddWorkoutFragment addWorkoutFragment = new AddWorkoutFragment();
                Bundle bundle = new Bundle();
                bundle.putString("email", email); // Replace with the actual email value
                bundle.putString("password", password); // Replace with the actual password value
                addWorkoutFragment.setArguments(bundle);
                return addWorkoutFragment;
            case 1:
                DisplayWorkoutFragment displayWorkoutFragment = new DisplayWorkoutFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("email", email); // Replace with the actual email value
                bundle1.putString("password", password); // Replace with the actual password value
                displayWorkoutFragment.setArguments(bundle1);
                return displayWorkoutFragment;
            case 2:
                SuggestedWorkoutFragment suggestedWorkoutFragment = new SuggestedWorkoutFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("email", email); // Replace with the actual email value
                bundle2.putString("password", password); // Replace with the actual password value
                suggestedWorkoutFragment.setArguments(bundle2);
                return suggestedWorkoutFragment;

            default:
                return new AddWorkoutFragment();
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }
}
