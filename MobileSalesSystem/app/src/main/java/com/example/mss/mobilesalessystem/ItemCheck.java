package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    TextView barcodeInfo, framed;
    ImageButton discard, accept;
    String barcode = null;
    String ImageDesc, ImageId;

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
        toggle.setEnabled(false);
        barcodeInfo = (TextView) findViewById(R.id.tv_detail_);
        formatList = (Spinner) findViewById(R.id.sp_format);
        framed = (TextView)findViewById(R.id.framed);
        barcode = getIntent().getStringExtra("barcode");
        final String [] barcodeSplit = barcode.split("/");                  //splitting the text read on the /
        final String productName;                                            //making a new barcode string

        getItem();

        productName = ImageDesc;               //The product name is set to be the Image Description
        barcodeInfo.setText(productName);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, FormatsDesc);     //Creates ArrayAdapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formatList.setAdapter(adapter);     //sets the formatList Adapter to be the arrayAdapter


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
                    if (foundformat.getFormatDescription().equals(formatList.getSelectedItem().toString())) {       //Finds the format object that equals the selected spinner format
                        finalFormat =  foundformat;
                    }
                }
                String prodId = "";
                float itemPrice = 0;
                String query = "SELECT ProductId, Price FROM product WHERE ImageId = '"+ImageId+"' AND Format = '"+finalFormat.getFormatId()+"';";     //finds the product id that has the image id and format id that is selected
                Cursor cur = pDB.rawQuery(query,null);
                cur.moveToFirst();
                while(!cur.isAfterLast())
                {
                    prodId = cur.getString(0);      //set product id
                    itemPrice = cur.getFloat(1);
                    cur.moveToNext();
                }
                orderItem finalItem = new orderItem(prodId, ImageDesc, finalFormat,itemPrice, toggle.isChecked());    //Create new orderItem with the given variables
                Intent returnIntent = new Intent();     //create the return intent
                returnIntent.putExtra("Item", finalItem);       //adds the order item to the Extras
                setResult(Activity.RESULT_OK,returnIntent);     //sets the result
                finish();       //returns to the qrScanner activity to pass the orderItem to the Cart
            }
        });

        formatList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String test = formatList.getSelectedItem().toString();
                for (Format foundformat : formats) {
                    if (foundformat.getFormatDescription().equals(test)) {
                        if (foundformat.getFrameable()) {
                            toggle.setEnabled(true);
                            toggle.setVisibility(View.VISIBLE);
                            framed.setVisibility(View.VISIBLE);
                        } else {
                            toggle.setEnabled(false);
                            toggle.setVisibility(View.INVISIBLE);
                            framed.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void getItem(){
        formats = new ArrayList<>();


        String query = "SELECT ImageId, ImageDesc FROM image WHERE QRCode = '"+barcode+"';";
        Cursor cur = pDB.rawQuery(query,null);      //Query to get the image details based on the barcode.
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            ImageId = cur.getString(0);         //sets the ImageId
            ImageDesc = cur.getString(1);       //sets the Image Description
            cur.moveToNext();
        }
        cur.close();

        query = "SELECT Format FROM product WHERE ImageID = '"+ImageId+"';";
        cur = pDB.rawQuery(query,null);     //Query to find the formats that exist for the image
        cur.moveToFirst();

        while(!cur.isAfterLast())
        {
            boolean framed;
            if(cur.getString(0).contains("FRM"))        //checks if format is frameable
            {
                framed = true;

            }
            else{
                framed = false;
            }
            formats.add(new Format(cur.getString(0),framed));       //creates a new format
            cur.moveToNext();
        }
        for(Format format:formats) {
            query = "SELECT Format, FormatDesc, Price FROM format WHERE Format = '" + format.getFormatId() + "';";
            cur = pDB.rawQuery(query, null);        //Query to find each formats details
            cur.moveToFirst();
            while(!cur.isAfterLast())
            {
                int formatIndex;
                for(Format foundformat:formats)
                {
                    if(foundformat.getFormatId().equals(cur.getString(0)))      //finds each format object and adds the extra details to them
                    {
                        formatIndex = formats.indexOf(foundformat);
                        formats.get(formatIndex).setFormatDescription(cur.getString(1));
                        //FormatsDesc.add(cur.getString(1));
                        if(foundformat.getFrameable())
                        {
                            formats.get(formatIndex).setExtraPrice(cur.getFloat(2));
                        }

                    }
                }
                cur.moveToNext();

            }
            ArrayList<Format> removeableFormats = new ArrayList<>();
            FormatsDesc = new ArrayList<>();
            for(Format foundformat:formats)
            {
                if(foundformat.getFormatId().contains("FRM"))
                {
                    int index = foundformat.getFormatId().indexOf("FRM");
                    String formatCheck = foundformat.getFormatId().substring(0,index);
                    for(Format matching:formats)
                    {
                        if(matching.getFormatId().equals(formatCheck))
                        {
                            matching.setFrameable(true);
                            matching.setExtraPrice(foundformat.getExtraPrice());
                            removeableFormats.add(foundformat);
                        }
                    }
                }
                else
                {
                    FormatsDesc.add(foundformat.getFormatDescription());
                }
            }
            formats.remove(removeableFormats);
        }


    }

}
