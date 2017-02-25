package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

/**
 * Created by Paul on 25/02/2017.
 */

public class Statistics extends Activity {
    Context context;
    SQLiteDatabase pDB = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.statistic_layout);
    }

}
