package com.alleviate.antidetention;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_schedule, container, false);

        SectionsPagerAdapterSchedule mSectionsPagerAdapter = new SectionsPagerAdapterSchedule(getFragmentManager());

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) fragment.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(day_of_week);

        TabLayout tabLayout = (TabLayout) fragment.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return fragment;
    }

}
