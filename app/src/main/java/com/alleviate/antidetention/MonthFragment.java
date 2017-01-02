package com.alleviate.antidetention;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;


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

        SimpleDateFormat date_std = new SimpleDateFormat("dd/MM/yy");

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(tv_month.getDate());

        Toast.makeText(getActivity(),""+date_std.format(cal.getTime()),Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        return view;
    }

}
