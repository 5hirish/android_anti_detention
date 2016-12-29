package com.alleviate.antidetention;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {


    public TodayFragment() {
        // Required empty public constructor
    }

    RecyclerView.Adapter rvadpter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_today, container, false);
        // Inflate the layout for this fragment
        String movies[] = getResources().getStringArray(R.array.planets_array);
        final ArrayList mcu_movies = new ArrayList<String>(Arrays.asList(movies));

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycler_view_today);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rv.setHasFixedSize(true);

        RecyclerView.LayoutManager rvlayoutmanager = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(rvlayoutmanager);

        rvadpter = new TodayAdapter(getActivity(), mcu_movies);
        rv.setAdapter(rvadpter);

        rv.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

}
