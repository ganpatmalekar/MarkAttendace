package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;

public class Login extends AppCompatActivity {

    private EditText edUsername, edPassword;
    private MaterialButton mbLogin;
    private TextView tvError;
    private Spinner userSpinner;
    private String[] user_list;
    private ArrayAdapter adapter;
    private String user, pass;
    private String user_type;

    private MySharedPrefrences prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.username);
        edPassword = findViewById(R.id.password);
        mbLogin = findViewById(R.id.btn_login);
        tvError = findViewById(R.id.error_text);

        prefrences = new MySharedPrefrences(this);

        userSpinner = findViewById(R.id.spinner_usertype);
        user_list = getResources().getStringArray(R.array.user_types);

        adapter = new ArrayAdapter(Login.this, android.R.layout.simple_spinner_item, user_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvError.setText("");
                user_type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
    }

    // validate login form
    private void validateForm() {
        user = edUsername.getText().toString().trim();
        pass = edPassword.getText().toString().trim();

        if (user.isEmpty()) {
            edUsername.setError("Username required");
            edUsername.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            edPassword.setError("Password required");
            edPassword.requestFocus();
            return;
        }
        if (userSpinner.getSelectedItem().equals("Select User Type")) {
            tvError.setText("Please, select user!");
        }
        if (!user.isEmpty() && !pass.isEmpty()) {
            if (userSpinner.getSelectedItem().equals("Admin")) {
                validateAdmin();
            } else if (userSpinner.getSelectedItem().equals("Teacher")) {
                validateTeacher();
            } else if (userSpinner.getSelectedItem().equals("Student")) {
                validateStudent();
            }
        }
    }


    // validate student login credentials
    private void validateStudent() {
    }

    // validate teacher login credentials
    private void validateTeacher() {
    }

    // validate admin login credentials
    private void validateAdmin() {
        if (user.equals("admin") && pass.equals("admin@123")) {
            prefrences.createSession(user, "");
            startActivity(new Intent(Login.this, Master.class).putExtra("USER_TYPE", user_type));
            finish();
        } else {
            tvError.setText("Wrong credentials! Please try again.");
        }
    }
}
