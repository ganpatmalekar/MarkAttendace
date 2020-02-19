package com.swap.markmyattendace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.swap.markmyattendace.activities.Login;
import com.swap.markmyattendace.activities.Master;

import java.util.HashMap;

public class MySharedPrefrences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    private String PREF_NAME = Constants.PREF_NAME;
    private String KEY_SET_APP_RUN_FIRST_TIME = Constants.APP_RUN_FIRST_TIME;
    private static final String IS_LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String FIRST_NAME = "FIRST_NAME";

    public MySharedPrefrences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = preferences.edit();
    }

    public void setAppRunFirst(String appRunFirst) {
        editor.remove(KEY_SET_APP_RUN_FIRST_TIME);
        editor.putString(KEY_SET_APP_RUN_FIRST_TIME, appRunFirst);
        editor.apply();
    }

    public String getAppRunFirst() {
        String appRunFirst = preferences.getString(KEY_SET_APP_RUN_FIRST_TIME, "FIRST");
        return appRunFirst;
    }

    //create login session
    public void createSession(String name, String first_name) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(FIRST_NAME, first_name);
        editor.commit();
    }

    //get login state
    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(context, Login.class);
            context.startActivity(i);
            ((Master) context).finish();
        }
    }

    //get stored session data
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, preferences.getString(NAME, null));
        user.put(FIRST_NAME, preferences.getString(FIRST_NAME, null));

        return user;
    }

    //clear session details
    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, Login.class);
        context.startActivity(i);
        ((Master) context).finish();
    }
}
