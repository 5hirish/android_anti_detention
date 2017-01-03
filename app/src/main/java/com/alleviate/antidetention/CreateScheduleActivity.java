package com.alleviate.antidetention;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class CreateScheduleActivity extends AppCompatActivity {

    String db_day, db_start_time, db_end_time, db_lecture, db_lecture_staff, db_lecture_hall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] week_days = getResources().getStringArray(R.array.week_day);
        final ArrayAdapter<String> days_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.select_dialog_item, week_days);

        final AutoCompleteTextView auto_lect = (AutoCompleteTextView) findViewById(R.id.lecture);
        final AutoCompleteTextView auto_lect_staff = (AutoCompleteTextView) findViewById(R.id.lecturer);
        final AutoCompleteTextView auto_lect_hall = (AutoCompleteTextView) findViewById(R.id.lect_room);
        final TextView tv_day = (TextView) findViewById(R.id.day);
        final TextView tv_lect_time = (TextView) findViewById(R.id.time_diff);
        final TextView tv_start_time = (TextView) findViewById(R.id.start_time);
        final TextView tv_end_time = (TextView) findViewById(R.id.end_time);

        ArrayList<String> lecture_array = create_array_adapter(R.id.lecture);
        final ArrayAdapter<String> lecture_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.select_dialog_item, lecture_array);
        auto_lect.setAdapter(lecture_adapter);

        ArrayList<String> staff_array = create_array_adapter(R.id.lecturer);
        final ArrayAdapter<String> staff_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.select_dialog_item, staff_array);
        auto_lect_staff.setAdapter(staff_adapter);

        ArrayList<String> hall_array = create_array_adapter(R.id.lect_room);
        final ArrayAdapter<String> hall_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.select_dialog_item, hall_array);
        auto_lect_hall.setAdapter(hall_adapter);

        final AlertDialog.Builder vschedule_day = new AlertDialog.Builder(CreateScheduleActivity.this);

        LayoutInflater dialog_inflater = getLayoutInflater();
        final View dialog_view = dialog_inflater.inflate(R.layout.dialog_layout, null );

        TextView dialog_title = (TextView) dialog_view.findViewById(R.id.title);
        dialog_title.setText("Day");
        ListView dialog_list = (ListView) dialog_view.findViewById(R.id.dialog_list);
        dialog_list.setAdapter(days_adapter);

        vschedule_day.setView(dialog_view);

        final AlertDialog vselect_day = vschedule_day.create();

        tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vselect_day.show();
            }
        });

       dialog_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               db_day = parent.getItemAtPosition(position).toString();
               tv_day.setText(db_day);
               vselect_day.dismiss();
           }
       });


        Calendar cal = Calendar.getInstance();
        db_start_time = cal.get(Calendar.HOUR_OF_DAY) +":"+cal.get(Calendar.MINUTE);
        db_end_time = cal.get(Calendar.HOUR_OF_DAY) + 1+":"+cal.get(Calendar.MINUTE);
        SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");
        SimpleDateFormat time_std = new SimpleDateFormat("hh:mm a");

        try{

            Date picked_start_time = time_gen.parse(db_start_time);
            tv_start_time.setText(time_std.format(picked_start_time));
            Date picked_end_time = time_gen.parse(db_end_time);
            tv_end_time.setText(time_std.format(picked_end_time));

        }catch (java.text.ParseException exp){
            Log.d("Anti:Exception","Time Parsing exception - "+exp);
        }

        tv_lect_time.setText("01 hr, 00 min");

        final TimePickerDialog select_start_time_dialog = new TimePickerDialog(CreateScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int picked_hour, int picked_minute) {

                SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");
                SimpleDateFormat time_std = new SimpleDateFormat("hh:mm a");

                String str_start_time = picked_hour+":"+picked_minute;

                if (check_time(str_start_time, db_end_time)) {
                    String time_diff = get_timediff(db_start_time, db_end_time);
                    tv_lect_time.setText(time_diff);

                    db_start_time = picked_hour+":"+picked_minute;

                    try{

                        Date picked_start_time = time_gen.parse(str_start_time);
                        tv_start_time.setText(time_std.format(picked_start_time));

                    }catch (java.text.ParseException exp){
                        Log.d("Anti:Exception","Time Parsing exception - "+exp);
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Start Time is after End Time!",Toast.LENGTH_LONG).show();
                }
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false);

        final TimePickerDialog select_end_time_dialog = new TimePickerDialog(CreateScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int picked_hour, int picked_minute) {

                SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");
                SimpleDateFormat time_std = new SimpleDateFormat("hh:mm a");

                String str_end_time = picked_hour+":"+picked_minute;

                if (check_time(db_start_time, str_end_time)){
                    String time_diff = get_timediff(db_start_time, db_end_time);
                    tv_lect_time.setText(time_diff);

                    db_end_time = picked_hour+":"+picked_minute;

                    try{
                        Date picked_end_time = time_gen.parse(str_end_time);
                        tv_end_time.setText(time_std.format(picked_end_time));

                    }catch (java.text.ParseException exp){
                        Log.d("Anti:Exception","Time Parsing exception - "+exp);
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Start Time is after End Time!",Toast.LENGTH_LONG).show();
                }

            }
        }, cal.get(Calendar.HOUR_OF_DAY) + 1, cal.get(Calendar.MINUTE), false);

        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_start_time_dialog.show();

            }
        });

        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_end_time_dialog.show();
           }
        });

        auto_lect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db_lecture = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                db_lecture = auto_lect.getText().toString();
            }
        });

        auto_lect_staff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db_lecture_staff = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                db_lecture_staff = auto_lect_staff.getText().toString();
            }
        });

        auto_lect_hall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db_lecture_hall = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                db_lecture_hall = auto_lect_hall.getText().toString();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db_day = tv_day.getText().toString();

                db_lecture = auto_lect.getText().toString();

                db_lecture_staff = auto_lect_staff.getText().toString();

                db_lecture_hall = auto_lect_hall.getText().toString();

                if (!db_lecture.isEmpty()) {
                    if (!time_overlaps(db_day, db_start_time, db_end_time)){
                        new InsertSchedule().execute();

                        Toast.makeText(getApplicationContext(),"Schedule Added", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Snackbar.make(view,"Start and End Time Overlaps with other lecture on "+db_day,Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Lecture is empty", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    private boolean time_overlaps(String db_day, String db_start_time, String db_end_time) {

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr =db.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SQLiteHelper.db_schedule);

        String[] columns = {
                SQLiteHelper.db_schedule_day,
                SQLiteHelper.db_schedule_start,
                SQLiteHelper.db_schedule_end};

        Cursor cur = queryBuilder.query(dbr, columns, SQLiteHelper.db_schedule_day+" = ? ", new String[] {db_day}, null, null, null);
        if(cur != null) {
            if (cur.moveToFirst()) {
                do {
                    String start_time = cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_start));
                    String end_time = cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_start));

                    SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");

                    try {
                        Date date_start_time = time_gen.parse(start_time);
                        Calendar cal_start = Calendar.getInstance();
                        cal_start.setTime(date_start_time);

                        Date picked_start_time = time_gen.parse(db_start_time);
                        Calendar cal_start_selected = Calendar.getInstance();
                        cal_start_selected.setTime(picked_start_time);

                        Date date_end_time = time_gen.parse(end_time);
                        Calendar cal_end = Calendar.getInstance();
                        cal_end.setTime(date_end_time);

                        Date picked_end_time = time_gen.parse(db_end_time);
                        Calendar cal_end_selected = Calendar.getInstance();
                        cal_end_selected.setTime(picked_end_time);

                        if (cal_start_selected.after(cal_end) && cal_end_selected.after(cal_end)) {

                        } else if (cal_start_selected.before(cal_start) && cal_end_selected.before(cal_start)){

                        } else {
                            return true;
                        }
                        //Toast.makeText(getActivity(),cal.getTime().toString(),Toast.LENGTH_SHORT).show();

                    }catch (ParseException exp){
                        Log.d("Anti:Exception","Time Parsing exception - "+exp);

                    }

                } while (cur.moveToNext());
            }
            cur.close();
        }
        return false;
    }

    private ArrayList create_array_adapter(int res_type) {

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr =db.getReadableDatabase();

        ArrayList<String> array_adapter = new ArrayList<String>();

        String[] columns = {
                SQLiteHelper.db_schedule_lecture,
                SQLiteHelper.db_schedule_lecturer_staff,
                SQLiteHelper.db_schedule_lecturer_hall};

        //Cursor cur = dbr.rawQuery("SELECT * FROM "+SQLiteHelper.db_schedule, null);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(SQLiteHelper.db_schedule);

        Cursor cur = queryBuilder.query(dbr, columns, null, null, null, null, null);

        if (res_type == R.id.lecture){

            if (cur.getCount() != 0){
                if(cur != null) {
                    if (cur.moveToFirst()) {
                        do {
                            array_adapter.add(cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_lecture)));
                        } while (cur.moveToNext());
                    }
                    cur.close();
                }

            } else {
                array_adapter.add("Lecture Subject");
            }

        } else if (res_type == R.id.lecturer){

            if (cur.getCount() != 0){
                if(cur != null) {
                    if (cur.moveToFirst()) {
                        do {
                            array_adapter.add(cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_lecturer_staff)));
                        } while (cur.moveToNext());
                    }
                    cur.close();
                }

            } else {
                array_adapter.add("Lecturer");
            }

        } else if (res_type == R.id.lect_room){

            if (cur.getCount() != 0){
                if(cur != null) {
                    if (cur.moveToFirst()) {
                        do {
                            array_adapter.add(cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_lecturer_hall)));
                        } while (cur.moveToNext());
                    }
                    cur.close();
                }

            } else {
                array_adapter.add("Lecture Hall");
            }

        }

        dbr.close();
        db.close();

        Set<String> set = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        set.addAll(array_adapter);

        Toast.makeText(getApplicationContext(),""+array_adapter.size(),Toast.LENGTH_SHORT).show();

        return new ArrayList<String>(set);
    }

    private boolean check_time(String start_time, String end_time) {

        SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");

        try{

            Date start_date = time_gen.parse(start_time);
            Calendar start_date_cal = Calendar.getInstance();
            start_date_cal.setTime(start_date);


            Date end_date = time_gen.parse(end_time);
            Calendar end_date_cal = Calendar.getInstance();
            end_date_cal.setTime(end_date);

            if (start_date_cal.after(end_date_cal)){
                return false;
            }else {
                return true;
            }

        }catch (ParseException exp){
            Log.d("Anti:Exception","Date Parsing exception - "+exp);
        }

        return false;
    }

    private String get_timediff(String start_time, String end_time) {

        SimpleDateFormat time_gen = new SimpleDateFormat("HH:mm");

        String time_diff = "";

        try{

            Date start_date = time_gen.parse(start_time);
            Calendar start_date_cal = Calendar.getInstance();
            start_date_cal.setTime(start_date);


            Date end_date = time_gen.parse(end_time);
            Calendar end_date_cal = Calendar.getInstance();
            end_date_cal.setTime(end_date);

            long diff = end_date_cal.getTimeInMillis() - start_date_cal.getTimeInMillis();

            time_diff = String.format("%02d hr, %02d min",
                    TimeUnit.MILLISECONDS.toHours(diff),
                    TimeUnit.MILLISECONDS.toMinutes(diff) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)));

        }catch (java.text.ParseException exp){
            Log.d("Anti:Exception","Date Parsing exception - "+exp);
        }

        return time_diff;
    }

    private class InsertSchedule extends AsyncTask<Void, Void, Long> {

        @Override
        protected Long doInBackground(Void... params) {

            SQLiteHelper db = new SQLiteHelper(getApplicationContext());
            SQLiteDatabase dbw = db.getWritableDatabase();

            ContentValues schedule_val = new ContentValues();
            schedule_val.put(SQLiteHelper.db_schedule_day, db_day);
            schedule_val.put(SQLiteHelper.db_schedule_start, db_start_time);
            schedule_val.put(SQLiteHelper.db_schedule_end, db_end_time);
            schedule_val.put(SQLiteHelper.db_schedule_lecture, db_lecture);
            schedule_val.put(SQLiteHelper.db_schedule_lecturer_staff, db_lecture_staff);
            schedule_val.put(SQLiteHelper.db_schedule_lecturer_hall, db_lecture_hall);

            long resid = dbw.insert(SQLiteHelper.db_schedule, null, schedule_val);

            dbw.close();
            db.close();

            return resid;
        }

        @Override
        protected void onPostExecute(Long result) {

            Log.d("Anti:Database", "Schedule Inserted " + result);
        }
    }
}
