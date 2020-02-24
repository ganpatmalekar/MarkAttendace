package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.adapters.CourseListAdapter;
import com.swap.markmyattendace.models.CourseListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStudent extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edSFirstName, edSLastName, edSEmail, edSContact;
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;

    private ArrayList<CourseListModel> courseModelList;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;
    private CourseListAdapter courseListAdapter;

    private static final String URL_GET_COURSE = Constants.BASE_SCRIPT_URL + "/displayCourse.php";
    private static final String URL_REGISTER_STUDENT = Constants.BASE_SCRIPT_URL + "/register_student.php?apicall=signup";

    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Student");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edSFirstName = findViewById(R.id.s_first_name);
        edSLastName = findViewById(R.id.s_last_name);
        edSEmail = findViewById(R.id.s_email);
        edSContact = findViewById(R.id.s_contact);
        radioGroup = findViewById(R.id.radioGender);

        recyclerView = findViewById(R.id.course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        courseModelList = new ArrayList<>();
        courseListAdapter = new CourseListAdapter(this, courseModelList);

        getCourseList();

        findViewById(R.id.btn_add_student).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStudent();
            }
        });
    }

    // get course list
    private void getCourseList() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_GET_COURSE, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                courseListAdapter.clear();
                progressDialog.dismiss();
                try {
                    if (!response.getBoolean("error")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CourseListModel courseModel = new CourseListModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            courseModel.setCourse_code(jsonObject.getString("c_code"));
                            courseModel.setCourse_name(jsonObject.getString("c_name"));

                            courseModelList.add(courseModel);
                        }
                        adapter = new CourseListAdapter(getApplicationContext(), courseModelList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(AddStudent.this, response.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    // get checkbox selected items from recyclerView
    private String getCheckboxSelectedItems() {
        StringBuilder stringBuilder = new StringBuilder();
        courseListAdapter = new CourseListAdapter(this, courseModelList);

        courseModelList = courseListAdapter.getItems();
        for (CourseListModel model : courseModelList) {
            stringBuilder.append(model.getCourse_name() + " ");
        }
        return stringBuilder.toString();
    }

    // register student
    private void registerStudent() {
        final String first_name = edSFirstName.getText().toString().trim();
        final String last_name = edSLastName.getText().toString().trim();
        final String email = edSEmail.getText().toString().trim();
        final String contact = edSContact.getText().toString().trim();
        final String gender = ((RadioButton) findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        final String courses = getCheckboxSelectedItems();

        if (TextUtils.isEmpty(first_name)) {
            edSFirstName.setError("Please enter firstname");
            edSFirstName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(last_name)) {
            edSLastName.setError("Please enter lastname");
            edSLastName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            edSEmail.setError("Please enter email");
            edSEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edSEmail.setError("Enter a valid email");
            edSEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(contact)) {
            edSContact.setError("Please enter contact");
            edSContact.requestFocus();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER_STUDENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        Toast.makeText(AddStudent.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddStudent.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddStudent.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("firstname", first_name);
                map.put("lastname", last_name);
                map.put("email", email);
                map.put("gender", gender);
                map.put("contact", contact);
                map.put("course", courses);
                return map;
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
