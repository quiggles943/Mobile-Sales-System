package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    private ArrayAdapter<orderItem> adapter;
    private ListView listview;
    ArrayList<orderItem>items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);
        context = this;
        addItemBtn = (ImageButton)findViewById(R.id.btn_add_item);
        checkoutBtn =(ImageButton)findViewById(R.id.btn_checkout);

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
        adapter = new CustomItemAdapter(this,R.layout.listitem, items);
        //set Adapter on listview
        listview.setAdapter(adapter);
        queryDatabase();
        registerForContextMenu(listview);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(items.size() > 0)
                {
                    Intent intent = new Intent(context, Checkout.class);
                    intent.putParcelableArrayListExtra("orderItems", items);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                boolean framed = extras.getBoolean("framed");
                orderItem result = extras.getParcelable("Item");
                items.add(result);

                if(framed)
                {
                    orderItem frame = extras.getParcelable("Frame");
                    items.add(frame);
                }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if(v.getId() == R.id.lv_itemList)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            int test = info.position;
            orderItem item = (orderItem) listview.getItemAtPosition(test);
            menu.setHeaderTitle("Options for "+item.getItemDescription());
            menu.add("Remove");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem selected)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)selected.getMenuInfo();
        if(selected.toString().equals("Remove"))
        {
            orderItem item = (orderItem) listview.getItemAtPosition(info.position);
            items.remove(item);
            adapter.notifyDataSetChanged();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Material_Dialog_Alert))
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit? This will remove your current order.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Cart.super.onBackPressed();
                        }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
}
