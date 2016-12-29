package com.alleviate.antidetention;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day, container, false);
        TextView tv = (TextView)view.findViewById(R.id.textView);

        FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String day = "";
        switch (day_pos) {
            case 0:
                day =  "Monday";
                break;
            case 1:
                day =  "Tuesday";
                break;
            case 2:
                day =  "Wednesday";
                break;
            case 3:
                day =  "Thursday";                break;
            case 4:
                day =  "Friday";
                break;
            case 5:
                day =  "Saturday";
                break;
            case 6:
                day =  "Sunday";
        }

        tv.setText(day);
        return view;
    }

}
