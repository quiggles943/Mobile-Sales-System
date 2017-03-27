package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Paul on 25/02/2017.
 */

public class Cart extends Activity{
    ImageButton addItemBtn, checkoutBtn;
    Context context;
    private ArrayAdapter<orderItem> adapter;
    private ListView listview;
    ArrayList<orderItem>items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);
        context = this;

        //set the image buttons for adding item and moving to checkout
        addItemBtn = (ImageButton)findViewById(R.id.btn_add_item);
        checkoutBtn =(ImageButton)findViewById(R.id.btn_checkout);
        //sets the listView
        listview = (ListView) findViewById(R.id.lv_itemList);

        //initialise the array list and the item adapter
        items = new ArrayList<>();
        adapter = new CustomItemAdapter(this,R.layout.listitem, items);
        //set Adapter on listview
        listview.setAdapter(adapter);

        //initialises the context menu
        registerForContextMenu(listview);

        //sets onclick methods for the buttons
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context,qrScanner.class);
                startActivityForResult(intent,1);
            }
        });
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
                ArrayList<orderItem> parcelItems;
                parcelItems = data.getParcelableArrayListExtra("items");
                for(orderItem item : parcelItems) {
                    items.add(item);
                }
                adapter.notifyDataSetChanged();
            }
            if(resultCode == Activity.RESULT_CANCELED){

            }
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
        final AlertDialog alert = new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Material_Dialog_Alert))
                .setTitle("Confirm Exit")
                .setMessage("Are you sure you want to exit? This will remove your current order.")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Cart.super.onBackPressed();
                        }
                })
                .setNegativeButton(android.R.string.no, null).show();
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 2000);
    }
}
