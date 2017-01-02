package com.alleviate.antidetention;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateScheduleActivity extends AppCompatActivity {

    String db_day, db_start_time, db_end_time, db_lecture, db_lecture_staff, db_lecture_hall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] week_days = getResources().getStringArray(R.array.week_day);
        final ArrayAdapter<String> days_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.select_dialog_item, week_days);

        final AutoCompleteTextView auto_lect = (AutoCompleteTextView) findViewById(R.id.lecture);
        AutoCompleteTextView auto_lect_staff = (AutoCompleteTextView) findViewById(R.id.lecturer);
        AutoCompleteTextView auto_lect_hall = (AutoCompleteTextView) findViewById(R.id.lecturer);
        final TextView tv_day = (TextView) findViewById(R.id.day);
        final TextView tv_lect_time = (TextView) findViewById(R.id.time_diff);
        final TextView tv_start_time = (TextView) findViewById(R.id.start_time);
        final TextView tv_end_time = (TextView) findViewById(R.id.end_time);

        auto_lect.setAdapter(days_adapter);
        auto_lect_staff.setAdapter(days_adapter);
        auto_lect_hall.setAdapter(days_adapter);

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
                    Toast.makeText(getApplicationContext(),"Time already past",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(),"Time already past",Toast.LENGTH_LONG).show();
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
                db_lecture_staff = auto_lect.getText().toString();
            }
        });

        auto_lect_hall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                db_lecture_hall = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                db_lecture_hall = auto_lect.getText().toString();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

        }catch (java.text.ParseException exp){
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
}
