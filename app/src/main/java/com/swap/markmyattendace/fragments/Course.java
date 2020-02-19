package com.swap.markmyattendace.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swap.markmyattendace.Constants;
import com.swap.markmyattendace.activities.AddCourse;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.adapters.CourseAdapter;
import com.swap.markmyattendace.models.CourseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Course extends Fragment {
    private FloatingActionButton fab_add_course;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<CourseModel> courseModelList;
    private ProgressDialog progressDialog;

    private static final String URL_DISPLAY_COURSE = Constants.BASE_SCRIPT_URL + "/displayCourse.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Course");

        fab_add_course = view.findViewById(R.id.fab_add_course);
        fab_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddCourse.class));
            }
        });


        recyclerView = view.findViewById(R.id.course_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        courseModelList = new ArrayList<>();
        adapter = new CourseAdapter(getContext(), courseModelList);
        adapter.notifyDataSetChanged();

        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.scheduleLayoutAnimation();

        displayCourse();

        return view;
    }

    private void displayCourse() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DISPLAY_COURSE, new Response.Listener<String>() {
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
                    adapter = new CourseAdapter(getContext(), courseModelList);
                    recyclerView.setAdapter(adapter);

                    adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}
