package com.example.mss.mobilesalessystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.pm.ActivityInfoCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Kriss on 30/01/2017.
 */
public class MainMenu extends Activity {
    Context context;
    SQLiteDatabase pDB = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.mainmenu_layout);
        ImageButton transactionBtn = (ImageButton) findViewById(R.id.imageButton);
        /*if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }*/
        initDatabase();
        transactionBtn.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  startActivity(new Intent(context,qrScanner.class));
                                              }
                                          }
        );
    }

    private void initDatabase(){
        try {
            String tableName = "";
            String tableColumns = "";
            pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
            pDB.execSQL("CREATE TABLE IF NOT EXISTS "+ tableName + " " + tableColumns);

        }catch(Exception e){

        }
    }

}