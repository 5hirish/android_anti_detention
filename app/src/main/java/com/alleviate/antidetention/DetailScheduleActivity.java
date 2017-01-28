package com.alleviate.antidetention;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
