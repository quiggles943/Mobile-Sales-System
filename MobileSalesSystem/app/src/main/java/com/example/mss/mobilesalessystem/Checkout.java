package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Josh on 27/02/2017.
 */

public class Checkout extends Activity {
    ArrayList<orderItem> order;
    private ArrayAdapter<orderItem> adapter;
    private TextView subtotalPriceDisplay, totalPriceDisplay;
    private EditText discount;
    private ImageButton cashPay, paypalPay;
    private String paymentMethod;
    private float total;
    ListView listview;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout_layout);
        order = getIntent().getParcelableArrayListExtra("orderItems");
        order.get(0);

        context = this;

        listview = (ListView)findViewById(R.id.lv_itemList);
        subtotalPriceDisplay = (TextView)findViewById(R.id.tv_total_price_checkout);
        adapter = new CustomItemAdapter(this,R.layout.listitem, order);
        //set Adapter on listview
        listview.setAdapter(adapter);
        total = subTotal();
        //sets the subtotal price
        subtotalPriceDisplay.setText(String.format("Subtotal £ %.2f",total));

        //set up the discount EditText
        discount = (EditText) findViewById(R.id.et_discount);
        discount.setText(String.format("£ %.2f",total));
        discount.setSelectAllOnFocus(true);
        //discount.setTextColor(R.color.);
        discount.setImeActionLabel("Apply",KeyEvent.KEYCODE_ENTER);
        discount.setOnEditorActionListener( new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                total = Float.parseFloat(discount.getText().toString());
                discount.setText(String.format("£ %.2f",total));
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                subtotalPriceDisplay.setText("Was £" + String.format("%.2f", subTotal()) +"    Now £" + String.format("%.2f",total));
                return true;
            }
        });

        //Set up the cash payment ImageButton
        cashPay = (ImageButton) findViewById(R.id.btn_cash);
        cashPay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                paymentMethod = "Cash";
                addOrderToDatabase();                                                               //runs the subroutine to add the order to the database
                //WE HAVE NO WHERE TO STORE THE TOTAL PRICE AND SO CAN'T SHOW TOTAL MONEY MADE....
                Toast.makeText(context, "Order Added To Database", Toast.LENGTH_SHORT).show();      //temp toast to show it is working

                Intent intent = new Intent(context, MainMenu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                playSound();
                startActivity(intent);
                finish();
            }
        });

        //Set up the paypal payment ImageButton
        paypalPay = (ImageButton) findViewById(R.id.btn_paypal);
    }

    //Calculate subtotal
    public float subTotal(){
        float value = 0;
        for(orderItem item : order)
        {
            value += item.getPrice();
        }
        return value;
    }

    private void addOrderToDatabase() {

        try {

            SQLiteDatabase pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);        //oppening database

            String invoiceJustCreated;         //int to hold the invoice that was reviously just created
            String[] tables = new String[1];
            tables[0] = "InvoiceID";
            //Cursor c = pDB.query("invoice",tables,null,null,null,null,"InvoiceID DESC","1");
            Cursor c = pDB.rawQuery("SELECT InvoiceID FROM invoice ORDER BY InvoiceID DESC LIMIT 1", null);         //get all the invoice ID's by largest to smallest (largest will be most recent)

            if (c.getCount() > 0)       //if there are some results
            {
                c.moveToFirst();        //move to first (largest due to ORDER BY)
                invoiceJustCreated = c.getString(c.getColumnIndex("InvoiceID"));        //the first should be the invoice just created
            } else {
                invoiceJustCreated = "1";
            }

            int invoiceNumber = Integer.parseInt(invoiceJustCreated) + 1;

            String newInvoiceNumber = ""+invoiceNumber;

            Date d = new Date();        //getting the date

            String currentDate = DateFormat.format("yyyy-MM-dd HH:mm:ss", d.getTime()).toString();       //getting the date into a format for SQLite

            String sqlCreateInvoice = "INSERT INTO invoice (InvoiceID, Date, CustomerID, PaymentMethod, AmountPaid) VALUES ("+newInvoiceNumber+",'"+ currentDate +"',0,'"+paymentMethod+"', "+total+")";   //making the insertion statement with customer and event ID of 0, need to change eventID

            pDB.execSQL(sqlCreateInvoice);      //executing said SQL statement

            String start = "INSERT INTO invoiceitems VALUES (" + newInvoiceNumber + ",";      //sql starting statement

            ArrayList<orderItem> finalList = new ArrayList<>();

            for(orderItem item : order)
            {
                boolean found = false;
                for(orderItem finalItem : finalList)
                {
                    if(finalItem.getItemID().equals(item.getItemID()))
                    {
                        finalList.get(finalList.indexOf(finalItem)).setQty(finalList.get(finalList.indexOf(finalItem)).getQty()+1);
                        found =true;
                    }
                }
                if(!found)
                {
                    finalList.add(item);
                }
            }

            for (orderItem o : finalList)       //for all the orders
            {
                String sqlInvoiceItems = start;         //resetting sql statement

                sqlInvoiceItems += o.getItemID();         //adding the item ID (ASSUMING THIS IS SAME AS PRODUCT ID IN SCHEMA??)

                sqlInvoiceItems += ","+o.getQty()+");";         //adding on the 1 for quantity (ASSUMING WE ARENT GROUPING MULTIPLE OF SAME PRODUCT AS ONE ORDER ITEM??)

                pDB.execSQL(sqlInvoiceItems);       //running said SQL statement
            }

            pDB.close();

        } catch (SQLiteException e) {
            Log.e("Order Addition Error", e.getMessage().toString());
        }

    }

    public void playSound(){
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final boolean sound = mSharedPreference.getBoolean("payment_sound",true);
        if(sound) {
            final MediaPlayer soundPlayer = MediaPlayer.create(this, R.raw.complete_transaction);
            soundPlayer.start();
        }
    }
}
