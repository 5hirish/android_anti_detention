package com.alleviate.antidetention;


import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;


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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        // Inflate the layout for this fragment

        String movies[] = getResources().getStringArray(R.array.planets_array);
        final ArrayList mcu_movies = new ArrayList<String>(Arrays.asList(movies));

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycler_view_day);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        rvadpter = new DayAdapter(getActivity(), mcu_movies);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), CreateScheduleActivity.class);
                in.putExtra("Day",day_pos);
                getActivity().startActivity(in);
            }
        });

        return view;
    }

}
