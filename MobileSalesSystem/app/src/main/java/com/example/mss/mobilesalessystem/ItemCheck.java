package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Paul on 23/02/2017.
 */

public class ItemCheck extends Activity{
    TextView barcodeInfo;
    ImageButton discard, accept;
    String barcode = null;
    orderItem product;
    String ProductId,ImageDesc, ImageId;

    private String itemSize;                    //String to hold the size of the item, i.e. A4, A5, Keyring
    public boolean frame;                       //Boolean to hold whether the item is framed
    private String imageId;                     //String to hold the image ID
    private Float itemPrice;
    Spinner formatList;
    ArrayList<Format> formats;
    ArrayList<String> FormatsDesc;
    SQLiteDatabase pDB;
    ToggleButton toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_layout);
        pDB = this.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        toggle = (ToggleButton) findViewById(R.id.btn_toggle_framed);
        discard = (ImageButton)findViewById(R.id.btn_discard);
        accept = (ImageButton)findViewById(R.id.btn_accept);

        barcodeInfo = (TextView) findViewById(R.id.tv_detail_);
        formatList = (Spinner) findViewById(R.id.sp_format);

        barcode = getIntent().getStringExtra("barcode");
        final String [] barcodeSplit = barcode.split("/");                  //splitting the text read on the /
        final String productName;                                            //making a new barcode string

        getItem();

        if(barcodeSplit[barcodeSplit.length-2].contains("product"))         //if the second last element is products
        {
            productName = ImageDesc;               //the new output is the product name
        } else {
            productName = "";                                                //otherwise it'll be blank
        }
        barcodeInfo.setText(productName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, FormatsDesc);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formatList.setAdapter(adapter);


        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Format finalFormat = null;
                for(Format foundformat:formats) {
                    if (foundformat.getFormatDescription().equals(formatList.getSelectedItem().toString())) {
                        finalFormat =  foundformat;
                    }
                }
                String prodId = "";
                String query = "SELECT ProductId FROM product WHERE ImageId = '"+ImageId+"' AND Format = '"+finalFormat.getFormatId()+"';";
                Cursor cur = pDB.rawQuery(query,null);
                cur.moveToFirst();
                while(!cur.isAfterLast())
                {
                    prodId = cur.getString(0);
                    cur.moveToNext();
                }
                orderItem finalItem = new orderItem(prodId, ImageDesc, finalFormat, toggle.isChecked());
                Intent returnIntent = new Intent();
                returnIntent.putExtra("Item", finalItem);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });


    }


    private void getItem(){
        formats = new ArrayList<>();
        FormatsDesc = new ArrayList<>();

        String query = "SELECT ImageId, ImageDesc FROM image WHERE QRCode = '"+barcode+"';";
        Cursor cur = pDB.rawQuery(query,null);
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            ImageId = cur.getString(0);
            ImageDesc = cur.getString(1);
            cur.moveToNext();
        }
        cur.close();

        query = "SELECT ProductID, Format FROM product WHERE ImageID = '"+ImageId+"';";
        cur = pDB.rawQuery(query,null);
        cur.moveToFirst();
        int i = 0;

        while(!cur.isAfterLast())
        {
            boolean framed;
            if(cur.getString(1).contains("FRM"))
            {
                framed = true;
            }
            else{
                framed = false;
            }
            formats.add(new Format(cur.getString(1),framed));
            i++;
            cur.moveToNext();
        }
        for(Format format:formats) {
            query = "SELECT Format, FormatDesc, Price FROM format WHERE Format = '" + format.getFormatId() + "';";
            cur = pDB.rawQuery(query, null);
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                int formatIndex = 0;
                for(Format foundformat:formats)
                {
                    if(foundformat.getFormatId().equals(cur.getString(0)))
                    {
                        formatIndex = formats.indexOf(foundformat);
                        formats.get(formatIndex).setFormatDescription(cur.getString(1));
                        FormatsDesc.add(cur.getString(1));
                        formats.get(formatIndex).setPrice(cur.getFloat(2));
                    }
                }
                cur.moveToNext();

            }
        }


    }

}
