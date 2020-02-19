package com.swap.markmyattendace.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.activities.AddCourse;
import com.swap.markmyattendace.activities.AddTeacher;
import com.swap.markmyattendace.adapters.TeacherAdapter;
import com.swap.markmyattendace.models.TeacherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends Fragment {
    private FloatingActionButton fab_add_teacher;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private List<TeacherModel> teacherModelList;
    private ProgressDialog progressDialog;
    private static final String URL_DISPLAY_TEACHER = Constants.BASE_SCRIPT_URL + "/displayTeacher.php";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Teacher");

        fab_add_teacher = view.findViewById(R.id.fab_add_teacher);
        fab_add_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddTeacher.class));
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        teacherModelList = new ArrayList<>();

        displayTeacher();

        return view;
    }

    private void displayTeacher() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DISPLAY_TEACHER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TeacherModel teacherModel = new TeacherModel();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        teacherModel.setFirst_name(jsonObject.getString("t_firstname"));
                        teacherModel.setLast_name(jsonObject.getString("t_lastname"));
                        teacherModel.setContact(jsonObject.getString("t_contact"));

                        teacherModelList.add(teacherModel);
                    }
                    adapter = new TeacherAdapter(getContext(), teacherModelList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
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
