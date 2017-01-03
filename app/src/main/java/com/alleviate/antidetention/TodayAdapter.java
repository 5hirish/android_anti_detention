package com.alleviate.antidetention;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by felix on 9/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.ViewHolder> {

    ArrayList<ScheduleInfo> schedule;
    Context context;

    public TodayAdapter(Context context, ArrayList<ScheduleInfo> schedule) {
        this.schedule = schedule;
        this.context = context;

    }

    @Override
    public TodayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.today_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TodayAdapter.ViewHolder holder, int position) {

        holder.lecture.setText(schedule.get(position).slect);
        holder.lecturer.setText(schedule.get(position).slect_staff+" at "+schedule.get(position).slect_hall);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();
                Intent in = new Intent(context, NotesActivity.class);
                context.startActivity(in);

                //update(mcu_movies.get(position).toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    // Remove a RecyclerView item containing a specified Data object
    /*public void remove(String movie) {
        int position = schedule.indexOf(movie);
        schedule.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    /*public void update(String movie) {
        int position = mcu_movies.indexOf(movie);
        mcu_movies.set(position,"Movie Liked");
        notifyItemChanged(position);
    }

    public void insert(int position) {
        mcu_movies.add(position, "New Movie");
        notifyItemInserted(position);
        //getItemId(position);
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout relativeLayout;
        CheckBox present;
        TextView lecture, lecturer;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative_layout);
            present = (CheckBox)itemView.findViewById(R.id.attendance);
            lecture = (TextView)itemView.findViewById(R.id.lecture);
            lecturer = (TextView)itemView.findViewById(R.id.lecturer);

        }
    }
}
