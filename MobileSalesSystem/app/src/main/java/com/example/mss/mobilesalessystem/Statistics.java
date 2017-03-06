package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Paul on 25/02/2017.
 */

public class Statistics extends Activity {
    Context context;
    SQLiteDatabase pDB = null;
    ArrayList<Invoice> invoices;
    InvoiceAdapter adapter;
    ListView listView;
    ImageButton syncFromDb, syncToDb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.statistic_layout);
        listView = (ListView)findViewById(R.id.lv_itemList);
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        invoices = new ArrayList<>();
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        String query = "SELECT * FROM invoice";
        Cursor cur = pDB.rawQuery(query,null);
            if(!(cur.moveToFirst()) || cur.getCount() ==0)
            {

            }
            else {
                while (!cur.isAfterLast()) {
                    invoices.add(new Invoice(cur.getInt(0), cur.getString(1), cur.getInt(2), cur.getInt(3)));
                    cur.moveToNext();
                }
                adapter = new InvoiceAdapter(this,R.layout.basic_invoice_item, invoices);
                //set Adapter on listview
                listView.setAdapter(adapter);
            }

        syncToDb = (ImageButton)findViewById(R.id.btn_sync_to_db);
        syncFromDb = (ImageButton)findViewById(R.id.btn_sync_from_db);

        syncFromDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseConnector connector = new DatabaseConnector(context, new String[]{"product","format","image"});      //creating a new database connector                 //openning up an editor to write to shared preferences

                String token = mSharedPreference.getString("token","");                               //gaining the token

                connector.execute(token);                                                   //running the database connector with the token

            }
        });
    }

}
