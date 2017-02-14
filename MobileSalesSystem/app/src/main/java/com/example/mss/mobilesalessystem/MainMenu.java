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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import java.util.logging.*;

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
            String [] tableName = new String[5];
            String [] tableColumns = new String[5];

            tableName[2] = "product";
            tableColumns[2] = "Format text, ImageID text, ProdDesc text, Price REAL, PRIMARY KEY (Format, ImageID)";

            tableName[1] = "image";
            tableColumns[1] = "ImageID text, ImageDesc text, ImgFilePath text, PRIMARY KEY (ImageID)";

            tableName[0] = "format";
            tableColumns[0] = "Format text, FormatDesc text, PRIMARY KEY (Format)";

            tableName[3] = "invoice";
            tableColumns[3] = "InvoiceID INTEGER, Date DATE, Event INTEGER, CustomerID INTEGER";

            tableName[4] = "invoiceitems";
            tableColumns[4] = "InvoiceID INTEGER, ProductID INTEGER, Qty INTEGER";

            pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

            if(tableName.length == tableColumns.length) {
                for (int i = 0; i < tableName.length; i++) {
                    pDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName[i] + " (" + tableColumns[i] + ");");
                }
            }
            String username = "root";
            String password = "";
            DatabaseConnector connector = new DatabaseConnector(context, "192.168.0.26:80", username, password);
            connector.execute();
        }catch(Exception e){
            Log.e("Database Creation Error", e.getMessage().toString());
        }
    }

}