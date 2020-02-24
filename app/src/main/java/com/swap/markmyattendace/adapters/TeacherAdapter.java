package com.swap.markmyattendace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.TeacherModel;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherHolder> {

    private Context context;
    private List<TeacherModel> teacherModelList;

    public TeacherAdapter(Context context, List<TeacherModel> teacherModelList) {
        this.context = context;
        this.teacherModelList = teacherModelList;
    }

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_layout, parent, false);
        return new TeacherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {
        TeacherModel teacherModel = teacherModelList.get(position);

        holder.tv_name.setText(teacherModel.getFirst_name() + " " + teacherModel.getLast_name());
        holder.tv_contact.setText(teacherModel.getContact());
    }

    @Override
    public int getItemCount() {
        return teacherModelList.size();
    }

    public void clear() {
        teacherModelList.clear();
        notifyDataSetChanged();
    }

    public class TeacherHolder extends RecyclerView.ViewHolder {
        protected TextView tv_name, tv_contact;

        public TeacherHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.teacher_name);
            tv_contact = itemView.findViewById(R.id.teacher_contact);
        }
    }
}
