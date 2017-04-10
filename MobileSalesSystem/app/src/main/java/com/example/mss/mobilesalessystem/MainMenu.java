package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Kriss on 30/01/2017.
 */
public class MainMenu extends Activity {
    Context context;
    SQLiteDatabase pDB = null;
    TextView version;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.mainmenu_layout);
        version = (TextView) findViewById(R.id.tv_version);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;
        version.setText("Version: "+ versionName);


        ImageButton transactionBtn = (ImageButton) findViewById(R.id.btn_cashpoint);
        ImageButton statsBtn = (ImageButton) findViewById(R.id.btn_statistics);

        SharedPreferences sharedP = PreferenceManager.getDefaultSharedPreferences(this);      //oppening up shared preferences
        SharedPreferences.Editor editor = sharedP.edit();                           //openning up an editor to write to shared preferences

        String checkToken = sharedP.getString("token", "");                         //string to check if token is already there

        if(checkToken == "") {                                                      //if the token isn't there
            editor.putString("token", "bTe>(AQSs(Au9?9sS%&H6Pgke!LMm9,A?ZM9x");         //writing the token string to the shared preferences
            editor.apply();
        }


        InitDatabase initDatabase = new InitDatabase(context);
        initDatabase.execute();
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

        transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = "SELECT count(*) FROM image";
                Cursor cur = pDB.rawQuery(check, null);
                cur.moveToFirst();
                int checkInt = cur.getInt(0);
                if(checkInt>0) {
                    startActivity(new Intent(context, Cart.class));
                }
                else
                {
                    new AlertDialog.Builder(context)
                            .setTitle("No Images Found")
                            .setMessage("There are currently no images in the database")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton(android.R.string.ok, null).show();
                }
            }
        }
        );

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,Statistics.class));
            }
        });
    }


}