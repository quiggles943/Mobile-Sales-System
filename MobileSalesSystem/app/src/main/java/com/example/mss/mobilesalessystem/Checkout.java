package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
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

        subtotalPriceDisplay.setText("Subtotal: £"+subTotal());

        discount = (EditText) findViewById(R.id.et_discount);

        total = subTotal();

        discount.setText(""+total);

        discount.setImeActionLabel("Apply",KeyEvent.KEYCODE_ENTER);
        discount.setOnEditorActionListener( new TextView.OnEditorActionListener(){

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                total = Float.parseFloat(discount.getText().toString());
                discount.setText(""+total);
                return true;
            }
        });

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
                startActivity(intent);
                finish();
            }
        });

        paypalPay = (ImageButton) findViewById(R.id.btn_paypal);
    }

    public float subTotal(){
        float value = 0;
        for(orderItem item : order)
        {
            value += item.getTotalPrice();
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

            String currentDate = DateFormat.format("yyyy-MM-dd", d.getTime()).toString();       //getting the date into a format for SQLite

            String sqlCreateInvoice = "INSERT INTO invoice (InvoiceID, Date, CustomerID, PaymentMethod, AmountPaid) VALUES ("+newInvoiceNumber+",'"+ currentDate +"',0,'"+paymentMethod+"', "+total+")";   //making the insertion statement with customer and event ID of 0, need to change eventID

            pDB.execSQL(sqlCreateInvoice);      //executing said SQL statement

            String start = "INSERT INTO invoiceitems VALUES (" + newInvoiceNumber + ",";      //sql starting statement

            for (orderItem o : order)       //for all the orders
            {
                String sqlInvoiceItems = start;         //resetting sql statement

                sqlInvoiceItems += o.getItemID();         //adding the item ID (ASSUMING THIS IS SAME AS PRODUCT ID IN SCHEMA??)

                sqlInvoiceItems += ",1);";         //adding on the 1 for quantity (ASSUMING WE ARENT GROUPING MULTIPLE OF SAME PRODUCT AS ONE ORDER ITEM??)

                pDB.execSQL(sqlInvoiceItems);       //running said SQL statement
            }

            pDB.close();

        } catch (SQLiteException e) {
            Log.e("Order Addition Error", e.getMessage().toString());
        }

    }
}
