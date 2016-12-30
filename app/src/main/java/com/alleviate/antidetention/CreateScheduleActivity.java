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

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String item[]={
                "January", "February", "March", "April",
                "May", "June", "July", "August",
                "September", "October", "November", "December"
        };

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);

        AutoCompleteTextView auto_lect = (AutoCompleteTextView) findViewById(R.id.lecture);
        AutoCompleteTextView auto_lect_staff = (AutoCompleteTextView) findViewById(R.id.lecturer);
        TextView tv_day = (TextView) findViewById(R.id.day);
        TextView tv_start_time = (TextView) findViewById(R.id.start_time);
        TextView tv_end_time = (TextView) findViewById(R.id.end_time);

        auto_lect.setAdapter(adapter);

        final AlertDialog.Builder vtask_date_dialog = new AlertDialog.Builder(CreateScheduleActivity.this);

        LayoutInflater dialog_inflater = getLayoutInflater();
        final View dialog_view = dialog_inflater.inflate(R.layout.dialog_layout, null );

        TextView dialog_title = (TextView) dialog_view.findViewById(R.id.title);
        dialog_title.setText("Day");
        ListView dialog_list = (ListView) dialog_view.findViewById(R.id.dialog_list);
        dialog_list.setAdapter(adapter);

        vtask_date_dialog.setView(dialog_view);

        final AlertDialog vselect_task_date = vtask_date_dialog.create();

        tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vselect_task_date.show();
            }
        });

        final TimePickerDialog select_time_dialog = new TimePickerDialog(CreateScheduleActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int picked_hour, int picked_minute) {

            }
        }, 0, 0, false);

        tv_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_time_dialog.show();

            }
        });

        tv_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_time_dialog.show();




























































































            }
        });

        auto_lect_staff.setAdapter(adapter);

        auto_lect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
}
