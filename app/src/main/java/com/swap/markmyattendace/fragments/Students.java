package com.swap.markmyattendace.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swap.markmyattendace.activities.AddStudent;
import com.swap.markmyattendace.R;

public class Students extends Fragment {

    private FloatingActionButton fab_add_student;
    private RecyclerView recyclerView;

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

        return view;
    }
}
