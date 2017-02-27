package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Paul on 25/02/2017.
 */

public class Cart extends Activity{
    ImageButton addItemBtn, checkoutBtn;
    Context context;
    String result;
    private BaseAdapter adapter;
    private ListView listview;
    ArrayList<orderItem>items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);
        context = this;
        addItemBtn = (ImageButton)findViewById(R.id.btn_add_item);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context,qrScanner.class);
                startActivityForResult(intent,1);
            }
        });

        // Mockup listview layout ---> cart_layout
        listview = (ListView) findViewById(R.id.lv_itemList);
        //create new Adapter
        items = new ArrayList<>();         //placeholder list
        adapter = new CustomItemAdapter(this, items);
        //set Adapter on listview
        listview.setAdapter(adapter);
        queryDatabase();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                orderItem result = data.getParcelableExtra("Item");
                result.toString();
                items.add(result);
                adapter.notifyDataSetChanged();
            }
            if(resultCode == Activity.RESULT_CANCELED){

            }
        }
    }

    private void queryDatabase(){
        SQLiteDatabase pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        Cursor cur = pDB.query("product",null,null,null,null,null,null);
        cur.moveToFirst();
        String[] testData = new String[40];
        int i=0;
        while(!cur.isAfterLast())
        {
            testData[i] = cur.getString(0)+" "+cur.getString(1)+" "+cur.getString(2)+" "+cur.getString(3);
            cur.moveToNext();
            i++;
        }
    }
}
