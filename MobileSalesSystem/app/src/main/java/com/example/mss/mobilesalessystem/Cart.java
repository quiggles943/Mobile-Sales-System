package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by Paul on 25/02/2017.
 */

public class Cart extends Activity{
    ImageButton addItemBtn, checkoutBtn;
    Context context;
    String result;
    private BaseAdapter adapter;
    private ListView listview;
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
        adapter = new CustomItemAdapter(this);
        //set Adapter on listview
        listview.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                result = data.getStringExtra("title");
            }
            if(resultCode == Activity.RESULT_CANCELED){

            }
        }
    }
}
