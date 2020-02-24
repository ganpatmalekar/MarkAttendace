package com.swap.markmyattendace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.CourseListModel;

import java.util.ArrayList;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListHolder> {

    private Context context;
    private static ArrayList<CourseListModel> courseModelList;
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    public CourseListAdapter(Context context, final ArrayList<CourseListModel> courseModelList) {
        this.context = context;
        this.courseModelList = courseModelList;
        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CourseListModel model = (CourseListModel) buttonView.getTag();
                if(isChecked){
                    model.setStatus("Checked");
                }else {
                    model.setStatus("Unchecked");
                }
            }
        };
    }

    @NonNull
    @Override
    public CourseListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_list_layout, parent, false);
        return new CourseListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CourseListHolder holder, final int position) {
        final CourseListModel courseModel = courseModelList.get(position);

        holder.tv_code.setText(courseModel.getCourse_code());
        holder.tv_name.setText(courseModel.getCourse_name());

        holder.checkBox.setTag(courseModel);
        holder.checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    // get items on recyclerview
    public ArrayList<CourseListModel> getItems(){
        if(courseModelList == null){
            return new ArrayList<>();
        }
        return courseModelList;
    }

    public void clear() {
        courseModelList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseModelList.size();
    }

    public class CourseListHolder extends RecyclerView.ViewHolder {
        protected TextView tv_code, tv_name;
        protected CheckBox checkBox;

        public CourseListHolder(@NonNull View itemView) {
            super(itemView);

            tv_code = itemView.findViewById(R.id.course_code);
            tv_name = itemView.findViewById(R.id.course_name);
            checkBox = itemView.findViewById(R.id.course_check);
        }
    }
}