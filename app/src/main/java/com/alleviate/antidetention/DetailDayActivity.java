package com.alleviate.antidetention;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DetailDayActivity extends AppCompatActivity {

    RecyclerView.Adapter rvadpter;
    public static ArrayList <ScheduleInfo> schedule_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_day);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent in  = getIntent();
        String selected_date = in.getStringExtra("Date");
        String day = "";

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat date_std = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Date picked_start_time = date_std.parse(selected_date);
            cal.setTime(picked_start_time);
            day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

        }catch (ParseException exp) {
            Log.d("Anti:Exception","Time Parsing exception - "+exp);
        }


        RecyclerView rv = (RecyclerView)findViewById(R.id.recycler_view_today);
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(rvlayoutmanager);

        schedule_list = new ArrayList<ScheduleInfo>();

        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr = db.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(SQLiteHelper.db_schedule);

        Cursor cursor = queryBuilder.query(dbr, null, SQLiteHelper.db_schedule_day+" = ? ", new String[] {day}, null, null, null);

        //Toast.makeText(getActivity(),"Count "+cursor.getCount(),Toast.LENGTH_SHORT).show();

        if(cursor != null){
            while (cursor.moveToNext()){

                int sid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.db_schedule_id));
                String sday = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_schedule_day));
                String sstart_time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_schedule_start));
                String send_time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_schedule_end));
                String slect = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_schedule_lecture));
                String slect_staff = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_schedule_lecturer_staff));
                String slect_hall = cursor.getString(cursor.getColumnIndex(SQLiteHelper.db_schedule_lecturer_hall));

                schedule_list.add(new ScheduleInfo(sid, sday, sstart_time, send_time, slect, slect_staff, slect_hall, selected_date));

            }cursor.close();
        }
        dbr.close();
        db.close();

        rvadpter = new TodayAdapter(getApplicationContext(), schedule_list);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
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
}
