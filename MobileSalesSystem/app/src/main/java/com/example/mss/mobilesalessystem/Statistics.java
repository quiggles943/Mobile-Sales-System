package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Paul on 25/02/2017.
 */

public class Statistics extends Activity {
    Context context;
    SQLiteDatabase pDB = null;
    InvoiceAdapter adapter;
    ImageButton syncFromDb, syncToDb;

    ExpandableListView listView;
    ExpandableInvoiceAdapter expInvAdap;
    ArrayList<Invoice> expListTitles;
    HashMap<Invoice, ArrayList<InvoiceItems>> expListDetails;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.statistic_layout);

        listView = (ExpandableListView)findViewById(R.id.elv_itemList);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        expListTitles = new ArrayList<>();
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        String query = "SELECT * FROM invoice";
        Cursor cur = pDB.rawQuery(query,null);
            if(!(cur.moveToFirst()) || cur.getCount() ==0)
            {

            }
            else {
                while (!cur.isAfterLast()) {
                    expListTitles.add(new Invoice(cur.getInt(0), cur.getString(1), cur.getInt(2), cur.getString(4), cur.getFloat(5)));
                    cur.moveToNext();
                }
            }

        expListDetails = getHashedData();

        expInvAdap = new ExpandableInvoiceAdapter(this, expListTitles,expListDetails);
        listView.setAdapter(expInvAdap);
        //NEED TO SET ON GROUP CLICK LISTENER

        syncToDb = (ImageButton)findViewById(R.id.btn_sync_to_db);
        syncFromDb = (ImageButton)findViewById(R.id.btn_sync_from_db);

        syncFromDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);          //opening a connectivity manager
                NetworkInfo networkType = cManager.getActiveNetworkInfo();

                final DatabaseConnector connector = new DatabaseConnector(context, new String[]{"product","format","image"});      //creating a new database connector                 //openning up an editor to write to shared preferences

                final String token = mSharedPreference.getString("token","");                               //gaining the token

                if(networkType == null)
                {
                    Toast.makeText(context,"No internet available", Toast.LENGTH_SHORT).show();        //Toast to say there is no internet at all
                }
                else if(networkType.getType() == ConnectivityManager.TYPE_WIFI) {
                    connector.execute(token);                                                   //running the database connector with the token
                } else if (networkType.getType() == ConnectivityManager.TYPE_MOBILE) {
                    new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog_Alert))
                            .setTitle("Confirm Database Sync")
                            .setMessage("You are on mobile data, are you sure you would like to progress?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    connector.execute(token);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }

            }
        });
    }

    private HashMap<Invoice, ArrayList<InvoiceItems>> getHashedData()
    {
        HashMap<Invoice, ArrayList<InvoiceItems>> result = new HashMap<>();

        for(Invoice i: expListTitles)
        {
            String sqlStatement = "SELECT * FROM invoiceitems WHERE InvoiceID = " + i.getInvoiceId() + ";";

            ArrayList<InvoiceItems> items = new ArrayList<>();

            pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

            Cursor c = pDB.rawQuery(sqlStatement, null);

            if (!(c.moveToFirst()) || c.getCount() == 0)
            {

            } else {
                while (!c.isAfterLast()) {
                    String sql = "SELECT i.imageDesc FROM image i JOIN product p ON i.imageID=p.imageID WHERE ProductID = " + c.getString(1) + ";";
                    Cursor cu = pDB.rawQuery(sql, null);
                    if (!(cu.moveToFirst()) || cu.getCount() == 0) {

                    } else {
                        items.add(new InvoiceItems(c.getInt(0), c.getString(1), cu.getString(0)));
                        c.moveToNext();
                    }
                }
            }

            result.put(i, items);

        }

        return result;
    }

}
