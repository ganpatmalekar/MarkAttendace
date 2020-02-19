package com.swap.markmyattendace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.swap.markmyattendace.R;
import com.swap.markmyattendace.models.ViewPages;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.UserViewHolder> {

    private Context context;
    private List<ViewPages> viewPagesList;

    public ViewPagerAdapter(Context context, List<ViewPages> viewPagesList) {
        this.context = context;
        this.viewPagesList = viewPagesList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ViewPages viewPages = viewPagesList.get(position);

        holder.name.setText(viewPages.getName());
        holder.description.setText(viewPages.getDesc());
        Glide.with(context)
                .load(viewPages.getIconPath())
                .thumbnail(0.1f)
                .into(holder.icon);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        holder.icon.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return viewPagesList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name, description;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.img_icon);
            name = itemView.findViewById(R.id.text_name);
            description = itemView.findViewById(R.id.text_desc);
        }
    }
}
