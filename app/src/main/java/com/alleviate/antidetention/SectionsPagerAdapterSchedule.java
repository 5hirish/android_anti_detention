package com.alleviate.antidetention;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by felix on 29/12/16.
 */

public class SectionsPagerAdapterSchedule extends FragmentStatePagerAdapter {

    public SectionsPagerAdapterSchedule(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TodayFragment();
            case 1:
                return new TomorrowFragment();
            case 2:
                return new MonthFragment();
            case 3:
                return new MonthFragment();
            case 4:
                return new MonthFragment();
            case 5:
                return new MonthFragment();
            case 6:
                return new MonthFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 7;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Monday";
            case 1:
                return "Tuesday";
            case 2:
                return "Wednesday";
            case 3:
                return "Thursday";
            case 4:
                return "Friday";
            case 5:
                return "Saturday";
            case 6:
                return "Sunday";
        }
        return null;
    }
}
