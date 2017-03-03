package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.statistic_layout);
        listView = (ListView)findViewById(R.id.lv_itemList);

        invoices = new ArrayList<>();
        pDB = context.openOrCreateDatabase("Product_DB", MODE_PRIVATE, null);
        String query = "SELECT * FROM invoice";
        Cursor cur = pDB.rawQuery(query,null);

        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            invoices.add(new Invoice(cur.getInt(0),cur.getString(1), cur.getInt(2), cur.getInt(3)));
        }

        adapter = new InvoiceAdapter(this,R.layout.basic_invoice_item, invoices);
        //set Adapter on listview
        listView.setAdapter(adapter);
    }

}
