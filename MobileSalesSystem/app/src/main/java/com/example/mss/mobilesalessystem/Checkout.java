package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mrjbe on 27/02/2017.
 */

public class Checkout extends Activity {
    ArrayList<orderItem> order;
    private ArrayAdapter<orderItem> adapter;
    private TextView subtotalPriceDisplay, totalPriceDisplay;
    ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);
        order = getIntent().getParcelableArrayListExtra("orderItems");
        order.get(0);
        listview = (ListView)findViewById(R.id.lv_itemList);
        subtotalPriceDisplay = (TextView)findViewById(R.id.tv_total_price_checkout);
        adapter = new CustomItemAdapter(this,R.layout.listitem, order);
        //set Adapter on listview
        listview.setAdapter(adapter);

        subtotalPriceDisplay.setText("Subtotal: £"+subTotal());
    }

    public float subTotal(){
        float value = 0;
        for(orderItem item : order)
        {
            value += item.getPrice();
        }
        return value;
    }
}
