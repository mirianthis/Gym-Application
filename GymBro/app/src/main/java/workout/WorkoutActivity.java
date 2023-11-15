package workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.example.gymbro.BaseActivity;
import com.example.gymbro.R;
import com.google.android.material.tabs.TabLayout;

import workout.ViewPagerAdapter;

public class WorkoutActivity extends BaseActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    private String email,password;

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_workout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_workout);

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this, email, password);
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Check if the fragmentContainer is visible
        View fragmentContainer = findViewById(R.id.fragmentContainer);
        if (fragmentContainer != null && fragmentContainer.getVisibility() == View.VISIBLE) {
            // If visible, make it invisible and consume the back press event
            fragmentContainer.setVisibility(View.GONE);
            return;
        }

        // If not visible, let the default back button behavior take place
        super.onBackPressed();
    }
}