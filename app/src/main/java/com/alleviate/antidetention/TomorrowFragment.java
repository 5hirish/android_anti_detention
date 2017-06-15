package com.alleviate.antidetention;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class TomorrowFragment extends Fragment {


    public TomorrowFragment() {
        // Required empty public constructor
    }

    RecyclerView.Adapter rvadpter;
    public static ArrayList <ScheduleInfo> schedule_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tomorrow, container, false);
        // Inflate the layout for this fragment

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycler_view_tomorrow);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK,1);

        String day = get_week_day(cal.get(Calendar.DAY_OF_WEEK));

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

        rvadpter = new TomorrowAdapter(getActivity(), schedule_list);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
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
