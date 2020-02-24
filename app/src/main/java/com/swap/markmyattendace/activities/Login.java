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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private EditText edUsername, edPassword;
    private MaterialButton mbLogin;
    private TextView tvError;
    private Spinner userSpinner;
    private String[] user_list;
    private ArrayAdapter adapter;

    private MySharedPrefrences prefrences;
    private static final String URL_VALIDATE_LOGIN = Constants.BASE_SCRIPT_URL + "/login.php?apicall=login";

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });
    }

    // validate login form
    private void validateUser() {
        final String user = edUsername.getText().toString().trim();
        final String pass = edPassword.getText().toString().trim();
        final String user_type = userSpinner.getSelectedItem().toString();

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
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_VALIDATE_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean("error")) {
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            prefrences.createSession(user);
                            startActivity(new Intent(Login.this, Master.class));
                            finish();
                        } else {
                            Toast.makeText(Login.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", user);
                    map.put("password", pass);
                    map.put("type", user_type);

                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }
}
