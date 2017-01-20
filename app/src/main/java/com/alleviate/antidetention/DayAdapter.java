package com.alleviate.antidetention;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by felix on 9/6/16.
 * Created at Alleviate.
 * shirishkadam.com
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    ArrayList<ScheduleInfo> schedule;
    Context context;

    public DayAdapter(Context context, ArrayList<ScheduleInfo> schedule) {
        this.schedule = schedule;
        this.context = context;

    }

    @Override
    public DayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tomorrow_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DayAdapter.ViewHolder holder, int position) {

        holder.lecture.setText(schedule.get(position).slect);
        holder.lecturer.setText(schedule.get(position).slect_staff+" at "+schedule.get(position).slect_hall);

        SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");
        SimpleDateFormat time_std = new SimpleDateFormat("hh:mm a");

        try {
            Date start_time = time_gen.parse(schedule.get(position).sstart_time);
            Date end_time = time_gen.parse(schedule.get(position).send_time);

            holder.start_time.setText(time_std.format(start_time));
            holder.end_time.setText(time_std.format(end_time));

        } catch (java.text.ParseException exp) {
            Log.d("Anti:Exception","Time Parsing exception - "+exp);
        }


        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();

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
        TextView lecture, lecturer, start_time, end_time;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative_layout);
            lecture = (TextView)itemView.findViewById(R.id.lecture);
            lecturer = (TextView)itemView.findViewById(R.id.lecturer);
            start_time = (TextView)itemView.findViewById(R.id.start_time);
            end_time = (TextView)itemView.findViewById(R.id.end_time);


        }
    }
}
