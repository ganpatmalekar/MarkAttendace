package com.swap.markmyattendace.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.MySharedPrefrences;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.adapters.CourseAdapter;
import com.swap.markmyattendace.adapters.CourseListAdapter;
import com.swap.markmyattendace.models.CourseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddStudent extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edSFirstName, edSLastName, edSEmail, edSContact;
    private RecyclerView recyclerView;
    private RadioGroup radioGroup;

    private List<CourseModel> courseModelList;
    private RecyclerView.Adapter adapter;
    private ProgressDialog progressDialog;
    private MySharedPrefrences prefrences;

    private static final String URL_GET_COURSE = Constants.BASE_SCRIPT_URL + "/displayCourse.php";
    private static final String URL_REGISTER_STUDENT = Constants.BASE_SCRIPT_URL + "";

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

        recyclerView = findViewById(R.id.course_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        courseModelList = new ArrayList<>();

        getCourseList();
    }

    // get course list
    private void getCourseList(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_COURSE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CourseModel courseModel = new CourseModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        courseModel.setCourse_code(jsonObject.getString("c_code"));
                        courseModel.setCourse_name(jsonObject.getString("c_name"));

                        courseModelList.add(courseModel);
                    }
                    adapter = new CourseListAdapter(getApplicationContext(), courseModelList);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                    adapter.notifyDataSetChanged();
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
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
