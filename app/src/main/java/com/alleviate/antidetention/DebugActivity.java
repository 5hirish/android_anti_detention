package com.alleviate.antidetention;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DebugActivity extends AppCompatActivity {

    TextView debug_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        debug_info = (TextView)findViewById(R.id.debug_info);
        show_schedule_data();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_debug, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){

            supportFinishAfterTransition();

        } else if (id == R.id.action_schedule){

            show_schedule_data();
        } else if (id == R.id.action_status){

            //show_primary_data();
        } else if (id == R.id.action_notes){

            //show_primary_data();
        }

        return true;
    }

    private void show_schedule_data() {
        SQLiteHelper db = new SQLiteHelper(getApplicationContext());
        SQLiteDatabase dbr =db.getReadableDatabase();

        debug_info.setText(SQLiteHelper.db_schedule+"\n\n");

        Cursor cur = dbr.rawQuery("SELECT * FROM "+SQLiteHelper.db_schedule, null);

        if(cur != null){
            if (cur.moveToFirst()){
                do{
                    debug_info.append(
                            cur.getInt(cur.getColumnIndex(SQLiteHelper.db_schedule_id))+") "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_day))+" - "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_start))+" - "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_end))+" - "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_lecture))+" - "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_lecturer_staff))+" - "+
                                    cur.getString(cur.getColumnIndex(SQLiteHelper.db_schedule_lecturer_hall))+" \n "
                    );

                }while (cur.moveToNext());
            }cur.close();
        }dbr.close();db.close();
    }
}
