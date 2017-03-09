package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Paul on 23/02/2017.
 */

public class ItemCheck extends Activity{
    TextView barcodeInfo, framed;
    ImageButton discard, accept;
    String barcode = null;
    String ImageDesc, ImageId, ImageFilePath;

    Spinner formatList;
    ArrayList<Format> formats;
    ArrayList<String> FormatsDesc;
    SQLiteDatabase pDB;
    ToggleButton toggle;
    ImageView preview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_layout);
        pDB = this.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        toggle = (ToggleButton) findViewById(R.id.btn_toggle_framed);
        discard = (ImageButton)findViewById(R.id.btn_discard);
        accept = (ImageButton)findViewById(R.id.btn_accept);
        preview = (ImageView)findViewById(R.id.iv_preview);
        toggle.setEnabled(false);
        barcodeInfo = (TextView) findViewById(R.id.tv_detail_);
        formatList = (Spinner) findViewById(R.id.sp_format);
        framed = (TextView)findViewById(R.id.framed);
        barcode = getIntent().getStringExtra("barcode");
        final String [] barcodeSplit = barcode.split("/");                  //splitting the text read on the /
        final String productName;                                            //making a new barcode string

        if(getItem()) {
            getThumbnail();
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
                public void onClick(View view) {
                    Format finalFormat = null;
                    for (Format foundformat : formats) {
                        if (foundformat.getFormatDescription().equals(formatList.getSelectedItem().toString())) {       //Finds the format object that equals the selected spinner format
                            finalFormat = foundformat;
                        }
                    }
                    String prodId = "";
                    float itemPrice = 0;
                    String query = "SELECT ProductId, Price FROM product WHERE ImageId = '" + ImageId + "' AND Format = '" + finalFormat.getFormatId() + "';";     //finds the product id that has the image id and format id that is selected
                    Cursor cur = pDB.rawQuery(query, null);
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {
                        prodId = cur.getString(0);      //set product id
                        itemPrice = cur.getFloat(1);
                        cur.moveToNext();
                    }
                    cur.close();
                    orderItem finalItem = new orderItem(prodId, ImageDesc, finalFormat, itemPrice, toggle.isChecked());    //Create new orderItem with the given variables
                    Intent returnIntent = new Intent();     //create the return intent
                    Bundle extras = new Bundle();
                    extras.putParcelable("Item", finalItem);
                    //returnIntent.putExtra("Item", finalItem);       //adds the order item to the Extras
                    if(toggle.isChecked())
                    {
                        String frameId = "", frameDesc = "";
                        Format frameFormat;
                        float framePrice = 0;
                        String frameQuery = "SELECT ProductId, Price FROM product WHERE Format = '" + finalFormat.getFormatId() + "FRM';";
                        Cursor cur2 = pDB.rawQuery(frameQuery, null);
                        cur2.moveToFirst();
                        while (!cur2.isAfterLast()) {
                            frameId = cur2.getString(0);      //set product id
                            framePrice = cur2.getFloat(1);
                            cur2.moveToNext();
                        }
                        query = "SELECT FormatDesc FROM format WHERE Format = '" + finalFormat.getFormatId() + "FRM';";
                        Cursor cur3 = pDB.rawQuery(query, null);
                        cur3.moveToFirst();
                        while (!cur3.isAfterLast()){
                            frameDesc = cur3.getString(0);
                            cur3.moveToNext();
                        }
                        frameFormat = new Format(frameId,frameDesc,true);
                        orderItem frame = new orderItem(frameId, frameDesc, frameFormat,framePrice, true);
                        extras.putBoolean("framed", true);      //adds the true boolean to denote that the item is framed
                        extras.putParcelable("Frame", frame);   //adds the frame item
                        //returnIntent.putExtra("framed", 1);       //adds the order item to the Extras
                        //returnIntent.putExtra("Frame", frame);       //adds the order item to the Extras
                    }
                    returnIntent.putExtras(extras);
                    setResult(Activity.RESULT_OK, returnIntent);     //sets the result
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
    }


    private boolean getItem(){
        formats = new ArrayList<>();
        FormatsDesc = new ArrayList<>();
        String query = "SELECT ImageId, ImageDesc, MedImgFilePath FROM image WHERE QRCode = '"+barcode+"';";
        Cursor cur = pDB.rawQuery(query,null);      //Query to get the image details based on the barcode.
        if(!(cur.moveToFirst()) || cur.getCount() ==0)
        {
            new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_Material_Dialog_Alert))
                    .setTitle("Not Found")
                    .setMessage("No item matching this barcode has been found in the database.")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent returnIntent = new Intent();     //create the return intent
                            setResult(Activity.RESULT_CANCELED,returnIntent);     //sets the result
                            finish();
                        }
                    }).show();
            return false;
        }
        else {
            //cur.moveToFirst();
            while (!cur.isAfterLast()) {
                ImageId = cur.getString(0);         //sets the ImageId
                ImageDesc = cur.getString(1);       //sets the Image Description
                ImageFilePath = cur.getString(2);   //sets the image filepath

                cur.moveToNext();
            }
            cur.close();

            query = "SELECT Format, Framable FROM product WHERE ImageID = '" + ImageId + "';";
            cur = pDB.rawQuery(query, null);     //Query to find the formats that exist for the image
            cur.moveToFirst();

            while (!cur.isAfterLast()) {
                boolean framed;
                int framable = cur.getInt(1);
                if (framable == 1)        //checks if format is frameable
                {
                    framed = true;

                } else {
                    framed = false;
                }
                formats.add(new Format(cur.getString(0), framed));       //creates a new format
                cur.moveToNext();
            }
            for (Format format : formats) {

                query = "SELECT Format, FormatDesc FROM format WHERE Format = '" + format.getFormatId() + "';";
                cur = pDB.rawQuery(query, null);        //Query to find each formats details
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    int formatIndex;
                    for (Format foundformat : formats) {
                        if (foundformat.getFormatId().equals(cur.getString(0)))      //finds each format object and adds the extra details to them
                        {
                            formatIndex = formats.indexOf(foundformat);
                            formats.get(formatIndex).setFormatDescription(cur.getString(1));
                            FormatsDesc.add(foundformat.getFormatDescription());

                        }
                    }
                    cur.moveToNext();

                }
            }

            return true;
        }


    }

    private void getThumbnail()
    {
        try {
            ContextWrapper cw = new ContextWrapper(this.getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getDir("MedImg", Context.MODE_PRIVATE);
            File mypath = null;
            boolean thumbnail;
            // Create imageDir
            if(ImageId.equals("HLYQU")) {
                mypath = new File(directory, "harley.png");
                thumbnail = true;
            }
            else if(ImageId.equals("FRKGR"))
            {
                mypath = new File(directory, "freddy_krueger.png");
                thumbnail = true;
            }
            else
            {
                thumbnail = false;
            }

            if(thumbnail) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap image;

                image = BitmapFactory.decodeStream(new FileInputStream(mypath), null, options);
                preview.setImageBitmap(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
