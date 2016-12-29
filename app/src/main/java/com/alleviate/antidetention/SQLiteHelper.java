package com.alleviate.antidetention;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by felix on 27/2/16.
 * Created at Alleviate.
 * shirishkadam.com
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String db_database = "Anti";
    private static final int db_version = 1;

    /* Attendance */

    public static final String db_schedule = "Schedule";
    public static final String db_schedule_id = "Id";
    public static final String db_schedule_day = "Day";
    public static final String db_schedule_start = "Start_Time";
    public static final String db_schedule_end = "End_Time";
    public static final String db_schedule_lecture = "Lecture";
    public static final String db_schedule_lecturer_staff = "Lecturer";

    /* Notes */

    public static final String db_notes = "Notes";
    public static final String db_notes_id = "Id";
    public static final String db_notes_date = "Date";
    public static final String db_notes_input_type = "Input_Type";
    public static final String db_notes_title = "Title";
    public static final String db_notes_data = "Data";
    public static final String db_notes_lecture = "Lecture";
    public static final String db_notes_start_time = "Start_Time";
    public static final String db_notes_remind_date = "Remind_Date";
    public static final String db_notes_remind_time = "Remind_Time";

    /* Logs Dev */

    public static final String db_logs= "Anti_Logs";
    public static final String db_logs_id = "Id";
    public static final String db_logs_logs = "Logs";

    public SQLiteHelper(Context context) {
        super(context, db_database, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_db_schedule = "CREATE TABLE "+db_schedule+" ( "+db_schedule_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+db_schedule_day+" VARCHAR NOT NULL, " +
                ""+db_schedule_start+" VARCHAR NOT NULL, "+db_schedule_end+" VARCHAR NOT NULL, "+db_schedule_lecture+" VARCHAR NOT NULL, "+db_schedule_lecturer_staff+" VARCHAR NOT NULL )";

        String create_db_notes="CREATE TABLE "+db_notes+" ( "+db_notes_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+db_notes_date+" VARCHAR NOT NULL ," +
                ""+db_notes_input_type+" VARCHAR NOT NULL, " + db_notes_title+" VARCHAR NOT NULL, "+db_notes_data+" TEXT NOT NULL, "+db_notes_lecture+" VARCHAR NOT NULL , " +
                ""+db_notes_start_time+" VARCHAR NOT NULL, "+db_notes_remind_date+" VARCHAR NOT NULL, "+db_notes_remind_time+" VARCHAR NOT NULL )";

        sqLiteDatabase.execSQL(create_db_schedule);
        sqLiteDatabase.execSQL(create_db_notes);

        String create_db_logs="CREATE TABLE "+db_logs+" ( "+db_logs_id+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+db_logs_logs+" VARCHAR NOT NULL )";
        sqLiteDatabase.execSQL(create_db_logs);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {


        // Change DayMan Free...!!

        //Todo:: DropTables if exist - DMTask, DMActivity, DMAlarms from previous versions of Kronos Manager

    }

    // Method to insert developer's logs (Debug Version only !!!).

    public static void insert_log(String log, Context context) {
        SQLiteHelper db = new SQLiteHelper(context);
        SQLiteDatabase dbw = db.getWritableDatabase();

        ContentValues log_values = new ContentValues();
        log_values.put(db_logs_logs,log);

        dbw.insert(db_logs, null, log_values);

        dbw.close();db.close();
    }
}
