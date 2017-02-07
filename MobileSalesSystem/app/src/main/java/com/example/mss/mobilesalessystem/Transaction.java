package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Created by Paul on 02/02/2017.
 */

public class Transaction extends Activity {
    TextView barcodeInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basictransaction_layout);
        barcodeInfo = (TextView) findViewById(R.id.textView2);
        final String barcode = getIntent().getStringExtra("barcode");
        final String [] barcodeSplit = barcode.split("/");                  //splitting the text read on the /
        final String productName;                                            //making a new barcode string
        if(barcodeSplit[barcodeSplit.length-2].contains("product"))         //if the second last element is products
        {
            productName = barcodeSplit[barcodeSplit.length-1];               //the new output is the product name
        } else {
            productName = "";                                                //otherwise it'll be blank
        }
        barcodeInfo.setText(productName);                                    //outputs the new product name
    }
}
