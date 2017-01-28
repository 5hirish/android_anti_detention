package com.alleviate.antidetention;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MonthFragment extends Fragment {


    public MonthFragment() {
        // Required empty public constructor
    }

    String selected_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);

        CalendarView tv_month = (CalendarView) view.findViewById(R.id.calendarView);
        tv_month.setMaxDate(Calendar.getInstance().getTimeInMillis());



        tv_month.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String str_date = dayOfMonth+"/"+month+1+"/"+year;
                SimpleDateFormat date_std = new SimpleDateFormat("dd/MM/yyyy");

                Intent in = new Intent(getActivity(), DetailDayActivity.class);


                try {
                    Date picked_start_time = date_std.parse(str_date);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(picked_start_time);
                    Toast.makeText(getActivity(), date_std.format(cal.getTime()),Toast.LENGTH_SHORT).show();
                    in.putExtra("Date", date_std.format(cal.getTime()));


                }catch (ParseException exp) {
                    Log.d("Anti:Exception","Time Parsing exception - "+exp);
                }

                startActivity(in);

            }
        });



        // Inflate the layout for this fragment
        return view;
    }

}
