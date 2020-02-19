package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.ViewPages;
import com.swap.markmyattendace.adapters.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private List<ViewPages> viewPagesList;
    private ViewPagerAdapter viewPagerAdapter;
    private MySharedPrefrences prefrences;

    private MaterialButton button;
    private TextView textView;

    private String SCHOOL_IMAGE_URL = Constants.BASE_URL + "/images/school_orange_bg.png";
    private String ATTENDANCE_IMAGE_URL = Constants.BASE_URL + "/images/attendance_orange_bg.png";
    private String FEEDBACK_IMAGE_URL = Constants.BASE_URL + "/images/feedback_orange_bg.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager2 = findViewById(R.id.viewPager);
        button = findViewById(R.id.mbNext);
        textView = findViewById(R.id.txtPageCount);

        prefrences = new MySharedPrefrences(this);

        viewPagesList = new ArrayList<>();
        viewPagesList.add(new ViewPages(SCHOOL_IMAGE_URL, "School", "Pack Bags and Walk"));
        viewPagesList.add(new ViewPages(ATTENDANCE_IMAGE_URL, "Attendance", "Be Present and Mark Your Attendance"));
        viewPagesList.add(new ViewPages(FEEDBACK_IMAGE_URL, "Feedback", "Give Feedback to Improve Us"));

        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, viewPagesList);

        if (prefrences.getAppRunFirst().equals("FIRST")){
            // if app first time launches open viewpager
            viewPager2.setAdapter(viewPagerAdapter);
            prefrences.setAppRunFirst("NO");
        } else {
            // if app is being opened for second time it starts directly from Splash Screen
            startActivity(new Intent(MainActivity.this, SplashScreen.class));
            finish();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewPager2.getCurrentItem() + 1 < viewPagerAdapter.getItemCount()) {
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(MainActivity.this, SplashScreen.class));
                    finish();
                }
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == viewPagerAdapter.getItemCount() - 1) {
                    button.setText("Start");
                } else {
                    button.setText("Next");
                }

                textView.setText((position + 1) + " / " + viewPagerAdapter.getItemCount());
            }
        });
    }
}
