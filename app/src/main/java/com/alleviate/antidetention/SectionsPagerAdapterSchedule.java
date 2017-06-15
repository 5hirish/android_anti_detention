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
                return DayFragment.newInstance(0);
            case 1:
                return DayFragment.newInstance(1);
            case 2:
                return DayFragment.newInstance(2);
            case 3:
                return DayFragment.newInstance(3);
            case 4:
                return DayFragment.newInstance(4);
            case 5:
                return DayFragment.newInstance(5);
            case 6:
                return DayFragment.newInstance(6);

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
