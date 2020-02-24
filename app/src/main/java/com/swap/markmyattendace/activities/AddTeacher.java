package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddTeacher extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edFirstName, edLastName, edEmail, edContact;
    private RadioGroup radioGroupGender;

    private static final String URL_REGISTER_TEACHER = Constants.BASE_SCRIPT_URL + "/register_teacher.php?apicall=signup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Teacher");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edFirstName = findViewById(R.id.t_first_name);
        edLastName = findViewById(R.id.t_last_name);
        edEmail = findViewById(R.id.t_email);
        edContact = findViewById(R.id.t_contact);
        radioGroupGender = findViewById(R.id.radioGender);

        findViewById(R.id.btn_add_teacher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerTeacher();
            }
        });
    }

    // add teacher
    private void registerTeacher() {
        final String first_name = edFirstName.getText().toString().trim();
        final String last_name = edLastName.getText().toString().trim();
        final String email = edEmail.getText().toString().trim();
        final String contact = edContact.getText().toString().trim();

        final String gender = ((RadioButton) findViewById(radioGroupGender.getCheckedRadioButtonId())).getText().toString();

        if (TextUtils.isEmpty(first_name)) {
            edFirstName.setError("Please enter firstname");
            edFirstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(last_name)) {
            edLastName.setError("Please enter lastname");
            edLastName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            edEmail.setError("Please enter email");
            edEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edEmail.setError("Enter a valid email");
            edEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            edContact.setError("Please enter contact");
            edContact.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_TEACHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        Toast.makeText(AddTeacher.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddTeacher.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddTeacher.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", first_name);
                params.put("lastname", last_name);
                params.put("email", email);
                params.put("gender", gender);
                params.put("contact", contact);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
