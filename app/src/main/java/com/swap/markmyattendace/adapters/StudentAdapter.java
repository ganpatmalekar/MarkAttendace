package com.swap.markmyattendace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.StudentModel;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentHolder> {

    private Context context;
    private List<StudentModel> studentModelList;

    public StudentAdapter(Context context, List<StudentModel> studentModelList) {
        this.context = context;
        this.studentModelList = studentModelList;
    }

    @NonNull
    @Override
    public StudentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_layout, parent, false);
        return new StudentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentHolder holder, int position) {
        StudentModel model = studentModelList.get(position);

        holder.stud_name.setText(model.getFirst_name() + " " + model.getLast_name());
        holder.stud_course.setText(model.getCourses());
    }

    @Override
    public int getItemCount() {
        return studentModelList.size();
    }

    public void clear() {
        studentModelList.clear();
        notifyDataSetChanged();
    }

    public class StudentHolder extends RecyclerView.ViewHolder {
        protected TextView stud_name, stud_course;

        public StudentHolder(@NonNull View itemView) {
            super(itemView);

            stud_name = itemView.findViewById(R.id.student_name);
            stud_course = itemView.findViewById(R.id.student_courses);
        }
    }
}
