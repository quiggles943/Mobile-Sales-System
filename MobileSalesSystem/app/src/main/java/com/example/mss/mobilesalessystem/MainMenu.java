package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Kriss on 30/01/2017.
 */
public class MainMenu extends Activity {

    // Mockup Adapter
    private BaseAdapter adapter;
    private ListView listview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_layout);

        //// Mockup listview layout ---> cart_layout
        //listview = (ListView) findViewById(R.id.lv_itemList);
        // create new Adapter
        //adapter = new CustomItemAdapter(this);
        // set Adapter on listview
        //listview.setAdapter(adapter);
    }
}

