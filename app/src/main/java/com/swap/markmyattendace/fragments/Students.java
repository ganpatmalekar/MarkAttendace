package com.swap.markmyattendace.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.activities.AddStudent;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.adapters.StudentAdapter;
import com.swap.markmyattendace.models.StudentModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Students extends Fragment {

    private FloatingActionButton fab_add_student;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.Adapter adapter;
    private List<StudentModel> studentModelList;
    private StudentAdapter studentAdapter;
    private ProgressDialog progressDialog;

    private static final String URL_DISPLAY_STUDENT = Constants.BASE_SCRIPT_URL + "/displayStudents.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_students, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Students");

        fab_add_student = view.findViewById(R.id.fab_add_student);
        fab_add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddStudent.class));
            }
        });

        recyclerView = view.findViewById(R.id.student_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        refreshLayout = view.findViewById(R.id.swiperefresh_student);
        refreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayStudents();
                refreshLayout.setRefreshing(false);
            }
        });

        studentModelList = new ArrayList<>();
        studentAdapter = new StudentAdapter(getContext(), studentModelList);

        displayStudents();

        return view;
    }

    private void displayStudents() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Students");
        progressDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_DISPLAY_STUDENT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                studentAdapter.clear();
                progressDialog.dismiss();
                try {
                    if (!response.getBoolean("error")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            StudentModel model = new StudentModel();

                            model.setFirst_name(jsonObject.getString("s_firstname"));
                            model.setLast_name(jsonObject.getString("s_lastname"));
                            model.setCourses(jsonObject.getString("s_course").replace(" ",", "));

                            studentModelList.add(model);
                        }
                        adapter = new StudentAdapter(getContext(), studentModelList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toast.makeText(getContext(), response.getString("data"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}
