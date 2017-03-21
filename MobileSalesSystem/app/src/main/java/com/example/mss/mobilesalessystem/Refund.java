package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Paul on 18/03/2017.
 */

public class Refund extends Activity {
    Context context;
    private EditText discount;
    private TextView discountText;
    private ImageButton cashPay, refund;
    Invoice invoice;
    ArrayList<orderItem> invoiceItems;
    SQLiteDatabase pDB = null;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.checkout_layout);
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        invoice = getIntent().getParcelableExtra("invoice");
        invoiceItems = getItems();

        discount = (EditText) findViewById(R.id.et_discount);
        discountText = (TextView) findViewById(R.id.discount);
        //Set up the cash payment ImageButton
        cashPay = (ImageButton) findViewById(R.id.btn_cash);




        //Set up the paypal payment ImageButton
        refund = (ImageButton) findViewById(R.id.btn_paypal);
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.sym_return);
        refund.setImageBitmap(icon);
        discountText.setText("Total Cost £"+invoice.getAmountPaid());

        ViewManager parentView = (ViewManager) discount.getParent();
        parentView.removeView(discount);
        parentView = (ViewManager) cashPay.getParent();
        parentView.removeView(cashPay);

        listView = (ListView)findViewById(R.id.lv_itemList);
        CustomItemAdapter adapter = new CustomItemAdapter(this,R.layout.listitem, invoiceItems);
        //set Adapter on listview
        listView.setAdapter(adapter);

        refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("invoice", invoice);
                setResult(1, returnIntent);
                finish();
            }
        });

    }

    public ArrayList<orderItem> getItems()
    {
        String sqlStatement = "SELECT * FROM invoiceitems WHERE InvoiceID = " + invoice.getInvoiceId() + ";";

        ArrayList<InvoiceItems> items = new ArrayList<>();
        ArrayList<orderItem> orderitems = new ArrayList<>();

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
                    items.add(new InvoiceItems(c.getInt(0), c.getString(1), cu.getString(0), c.getInt(2)));
                    c.moveToNext();
                }
            }
        }
        for(InvoiceItems item : items)
        {
            String sql = "SELECT p.format, p.price FROM  product p WHERE ProductID = " + item.getItemID() + ";";
            Cursor cu = pDB.rawQuery(sql,null);
            if (!(cu.moveToFirst()) || cu.getCount() == 0)
            {

            } else {
                while (!cu.isAfterLast()) {
                    orderitems.add(new orderItem(item.getItemID(),item.getItemDescription(),new Format(cu.getString(0), false),cu.getFloat(1), false));
                    //item.setFormat(cu.getString(0));
                    //item.setPrice(cu.getFloat(1));
                    cu.moveToNext();
                }
            }
        }
        return orderitems;
    }
}
