package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;

public class SplashScreen extends AppCompatActivity {

    private MySharedPrefrences prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefrences = new MySharedPrefrences(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefrences.isLoggedIn()){
                    startActivity(new Intent(SplashScreen.this, Master.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            }
        }, Constants.SPLASH_TIME_OUT);
    }
}
