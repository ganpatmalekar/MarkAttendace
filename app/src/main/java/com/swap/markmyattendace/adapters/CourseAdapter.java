package com.swap.markmyattendace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.CourseModel;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private Context context;
    private List<CourseModel> courseModelList;

    public CourseAdapter(Context context, List<CourseModel> courseModelList) {
        this.context = context;
        this.courseModelList = courseModelList;
    }

    @NonNull
    @Override
    public CourseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_layout, parent, false);
        return new CourseHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseHolder holder, int position) {
        CourseModel courseModel = courseModelList.get(position);

        holder.tv_code.setText(courseModel.getCourse_code());
        holder.tv_name.setText(courseModel.getCourse_name());
    }

    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    public class CourseHolder extends RecyclerView.ViewHolder {
        protected TextView tv_code, tv_name;

        public CourseHolder(@NonNull View itemView) {
            super(itemView);

            this.tv_code = itemView.findViewById(R.id.course_code);
            this.tv_name = itemView.findViewById(R.id.course_name);
        }
    }
}
