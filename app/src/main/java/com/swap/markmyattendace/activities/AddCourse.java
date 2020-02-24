package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.PopulateTeacherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCourse extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edCourseCode, edCourseName;
    private Spinner spinner;
    private TextView error_textView;
    private List<PopulateTeacherModel> populateTeacherModelList;
    private List<String> teachers;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> adapter;

    private static final String URL_ADD_COURSE = Constants.BASE_SCRIPT_URL + "/addCourse.php";
    private static final String URL_POPULATE_TEACHER = Constants.BASE_SCRIPT_URL + "/populateTeacher.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Course");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edCourseCode = findViewById(R.id.c_code);
        edCourseName = findViewById(R.id.c_name);

        error_textView = findViewById(R.id.course_error_text);

        spinner = findViewById(R.id.spinner_teacher);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                error_textView.setText("");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        teachers = new ArrayList<>();
        populateTeacherModelList = new ArrayList<>();

        populateTeacher();

        findViewById(R.id.btn_add_course).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });
    }

    // add course
    private void addCourse() {
        final String course_code = edCourseCode.getText().toString().trim();
        final String course_name = edCourseName.getText().toString().trim();
        final String course_teacher = spinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(course_code)) {
            edCourseCode.setError("Please enter course code");
            edCourseCode.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(course_name)) {
            edCourseName.setError("Please enter course name");
            edCourseName.requestFocus();
            return;
        }
        if (course_teacher.equals("Select Teacher")) {
            error_textView.setText("Please, select teacher");
        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_COURSE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        if (!jsonObject.getBoolean("error")) {
                            Toast.makeText(AddCourse.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddCourse.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(AddCourse.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("course_code", course_code);
                    params.put("course_name", course_name);
                    params.put("course_teacher", course_teacher);

                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    // populate teacher list in spinner
    private void populateTeacher() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Teachers");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_POPULATE_TEACHER, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    if (!response.getBoolean("error")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            PopulateTeacherModel model = new PopulateTeacherModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            model.setFirst_name(jsonObject.getString("t_firstname"));
                            model.setLast_name(jsonObject.getString("t_lastname"));

                            populateTeacherModelList.add(model);
                        }
                        for (int i = 0; i < populateTeacherModelList.size(); i++) {
                            teachers.add(populateTeacherModelList.get(i).getFirst_name() + " " + populateTeacherModelList.get(i).getLast_name());
                        }
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, teachers);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        adapter.insert("Select Teacher", 0);
                        spinner.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AddCourse.this, response.getString("data"), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
