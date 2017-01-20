package com.alleviate.antidetention;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayFragment extends Fragment {

    private static final String day_id = "Day_Id";
    private int day_pos;

    public DayFragment() {
        // Required empty public constructor
    }

    public static DayFragment newInstance(int param1) {
        DayFragment fragment = new DayFragment();
        Bundle args = new Bundle();
        args.putInt(day_id, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day_pos = getArguments().getInt(day_id);
        }
    }

    RecyclerView.Adapter rvadpter;
    public static ArrayList <ScheduleInfo> schedule_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        // Inflate the layout for this fragment

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycler_view_day);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        //Toast.makeText(getActivity(),""+day_pos + 2,Toast.LENGTH_SHORT).show();

        String day = get_week_day(day_pos + 2);

        schedule_list = new ArrayList<ScheduleInfo>();

        SQLiteHelper db = new SQLiteHelper(getActivity());
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

                schedule_list.add(new ScheduleInfo(sid, sday,sstart_time, send_time, slect, slect_staff, slect_hall));

            }cursor.close();
        }
        dbr.close();
        db.close();

        Collections.sort(schedule_list, new Comparator<ScheduleInfo>() {
            @Override
            public int compare(ScheduleInfo scheduleInfo1, ScheduleInfo scheduleInfo2) {
                return scheduleInfo1.getTime().compareTo(scheduleInfo2.getTime());
            }
        });

        rvadpter = new DayAdapter(getActivity(), schedule_list);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), CreateScheduleActivity.class);
                in.putExtra("Day", day_pos + 2);
                getActivity().startActivity(in);
            }
        });

        return view;
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
