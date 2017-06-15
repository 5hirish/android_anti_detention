package com.alleviate.antidetention;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

        SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");
        SimpleDateFormat time_std = new SimpleDateFormat("hh:mm a");
        String status = get_schedule_state(position);

        if (status.equals("True")){
            holder.present.setChecked(true);

        } else if (status.equals("False")) {
            holder.present.setChecked(false);

        } else {
            holder.present.setChecked(false);

        }

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
                Intent in = new Intent(context, NotesActivity.class);
                context.startActivity(in);

                //update(mcu_movies.get(position).toString());
            }
        });

        holder.present.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {

                    set_absence(holder.getAdapterPosition());

                } else {

                    set_present(holder.getAdapterPosition());

                }
            }
        });

    }

    private String get_schedule_state(int position) {

        int lecture_id = schedule.get(position).sid;

        SimpleDateFormat std_stat_fmt = new SimpleDateFormat("dd/MM/yy");

        Calendar cal = Calendar.getInstance();
        String today_date = std_stat_fmt.format(cal.getTime());
        String today_day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

        String status = "True";

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbr = db.getReadableDatabase();

        Cursor cursor = dbr.query(SQLiteHelper.db_stats, new String[] {SQLiteHelper.db_stats_lecture_id, SQLiteHelper.db_stats_date,SQLiteHelper.db_stats_status}, SQLiteHelper.db_stats_lecture_id +" = ? AND "+ SQLiteHelper.db_stats_date + " = ?", new String[] {""+lecture_id, today_date}, null, null, null);

        if(cursor != null){
            while (cursor.moveToNext()){

                int slid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.db_stats_lecture_id));
                String sstatus = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_stats_status));

                switch (sstatus) {
                    case "True":
                        status = "True";
                        break;
                    case "False":
                        status = "False";
                        break;
                    default:
                        status = "Neutral";
                        break;
                }

            }cursor.close();
        }
        dbr.close();
        db.close();

        return status;
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    public void set_absence(int position) {

        int lecture_id = schedule.get(position).sid;

        SimpleDateFormat std_stat_fmt = new SimpleDateFormat("dd/MM/yy");

        Calendar cal = Calendar.getInstance();
        String today_date = std_stat_fmt.format(cal.getTime());
        String today_day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw = db.getWritableDatabase();

        ContentValues update_stats = new ContentValues();
        update_stats.put(SQLiteHelper.db_stats_status, "False");

        long resid = dbw.update(SQLiteHelper.db_stats, update_stats, SQLiteHelper.db_stats_lecture_id +" = ? AND "+ SQLiteHelper.db_stats_date + " = ?", new String[] {""+lecture_id, today_date});

        dbw.close();
        db.close();

        Log.d("AntiDetention:Database","Attendance Status Updated "+resid);

    }

    public void set_present(int position) {

        int lecture_id = schedule.get(position).sid;

        SimpleDateFormat std_stat_fmt = new SimpleDateFormat("dd/MM/yy");

        Calendar cal = Calendar.getInstance();
        String today_date = std_stat_fmt.format(cal.getTime());
        String today_day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw = db.getWritableDatabase();

        ContentValues update_stats = new ContentValues();
        update_stats.put(SQLiteHelper.db_stats_status, "True");

        long resid = dbw.update(SQLiteHelper.db_stats, update_stats, SQLiteHelper.db_stats_lecture_id +" = ? AND "+ SQLiteHelper.db_stats_date + " = ?", new String[] {""+lecture_id, today_date});

        dbw.close();
        db.close();

        Log.d("AntiDetention:Database","Attendance Status Updated "+resid);

    }

    private String get_week_day(int day) {
        String str_day = "Sunday";
        switch (day){
            case 1:
                str_day = "Sunday";
                break;
            case 2:
                str_day = "Monday";
                break;
            case 3:
                str_day = "Tuesday";
                break;
            case 4:
                str_day = "Wednesday";
                break;
            case 5:
                str_day = "Thursday";
                break;
            case 6:
                str_day = "Friday";
                break;
            case 7:
                str_day = "Saturday";
                break;
        }
        return str_day;
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
        TextView lecture, lecturer, start_time, end_time;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative_layout);
            present = (CheckBox)itemView.findViewById(R.id.attendance);
            lecture = (TextView)itemView.findViewById(R.id.lecture);
            lecturer = (TextView)itemView.findViewById(R.id.lecturer);
            start_time = (TextView)itemView.findViewById(R.id.start_time);
            end_time = (TextView)itemView.findViewById(R.id.end_time);

        }
    }
}
