package com.alleviate.antidetention;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by felix on 3/1/17.
 */
public class ScheduleInfo {

    String sday, sstart_time, send_time, slect, slect_staff, slect_hall, selected_date;
    int sid;

    public ScheduleInfo(int sid, String sday, String sstart_time, String send_time, String slect, String slect_staff, String slect_hall) {
        this.sid = sid;
        this.sday = sday;
        this.sstart_time = sstart_time;
        this.send_time = send_time;
        this.slect = slect;
        this.slect_staff = slect_staff;
        this.slect_hall = slect_hall;
    }

    public ScheduleInfo(int sid, String sday, String sstart_time, String send_time, String slect, String slect_staff, String slect_hall, String selected_date) {
        this.sid = sid;
        this.sday = sday;
        this.sstart_time = sstart_time;
        this.send_time = send_time;
        this.slect = slect;
        this.slect_staff = slect_staff;
        this.slect_hall = slect_hall;
        this.selected_date = selected_date;
    }

    public Date getTime() {

        SimpleDateFormat std_time = new SimpleDateFormat("HH:mm");
        Date start_time = new Date();

        try {
            start_time = std_time.parse(this.sstart_time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return start_time;
    }

    /*@Override
    public boolean equals(Object obj) {

        if ( this.aid == ((TaskInfo) obj).aid && this.tid == ((TaskInfo) obj).tid) {
            return true;

        } else {
            return false;
        }
    }*/
}
