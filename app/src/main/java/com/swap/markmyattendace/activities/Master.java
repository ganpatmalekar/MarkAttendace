package com.swap.markmyattendace.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.fragments.AdminDashboard;
import com.swap.markmyattendace.fragments.Attendance;
import com.swap.markmyattendace.fragments.Course;
import com.swap.markmyattendace.fragments.Students;
import com.swap.markmyattendace.fragments.Teacher;

import java.util.HashMap;

public class Master extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private FragmentManager fragmentManager;
    private MySharedPrefrences prefrences;

    private TextView tvLoggedUser;
    public static String loggedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String getUserType = intent.getStringExtra("USER_TYPE");

        prefrences = new MySharedPrefrences(this);
        prefrences.checkLogin();
        // get logged user name
        HashMap<String, String> user = prefrences.getUserDetails();
        loggedUserName = user.get(prefrences.NAME);

//        if (getUserType.equals("Admin")) {
//            loggedUserName = user.get(prefrences.NAME);
//        } else {
//            loggedUserName = user.get(prefrences.FIRST_NAME);
//        }


        drawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        // set default fragment to the container
        fragmentManager = getSupportFragmentManager();
        AdminDashboard dashboard = new AdminDashboard();
        fragmentManager.beginTransaction().add(R.id.fragment_container, dashboard).commit();

        navigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        navigationView.setCheckedItem(R.id.dashboard);

        // set logged user name to the header of navigation drawer
        View header = navigationView.getHeaderView(0);
        tvLoggedUser = header.findViewById(R.id.logged_user);
        tvLoggedUser.setText("Welcome: " + loggedUserName);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.dashboard:
                        fragmentManager = getSupportFragmentManager();
                        AdminDashboard dashboard = new AdminDashboard();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, dashboard).commit();
                        break;
                    case R.id.course:
                        fragmentManager = getSupportFragmentManager();
                        Course course = new Course();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, course).commit();
                        break;
                    case R.id.teacher:
                        fragmentManager = getSupportFragmentManager();
                        Teacher teacher = new Teacher();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, teacher).commit();
                        break;
                    case R.id.student:
                        fragmentManager = getSupportFragmentManager();
                        Students students = new Students();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, students).commit();
                        break;
                    case R.id.attendance:
                        fragmentManager = getSupportFragmentManager();
                        Attendance attendance = new Attendance();
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, attendance).commit();
                        break;
                    case R.id.logout:
                        prefrences.logoutUser();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }
}
