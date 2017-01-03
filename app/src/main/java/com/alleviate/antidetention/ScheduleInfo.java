package com.alleviate.antidetention;

/**
 * Created by felix on 3/1/17.
 */
public class ScheduleInfo {

    String sday, sstart_time, send_time, slect, slect_staff, slect_hall;
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
}
