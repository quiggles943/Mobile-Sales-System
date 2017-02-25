package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Paul on 23/02/2017.
 */

public class ItemCheck extends Activity{
    TextView barcodeInfo;
    ImageButton discard, accept;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_layout);
        barcodeInfo = (TextView) findViewById(R.id.tv_detail_);
        final String barcode = getIntent().getStringExtra("barcode");
        final String [] barcodeSplit = barcode.split("/");                  //splitting the text read on the /
        final String productName;                                            //making a new barcode string
        if(barcodeSplit[barcodeSplit.length-2].contains("product"))         //if the second last element is products
        {
            productName = barcodeSplit[barcodeSplit.length-1];               //the new output is the product name
        } else {
            productName = "";                                                //otherwise it'll be blank
        }
        barcodeInfo.setText(productName);

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent returnIntent = new Intent();
                returnIntent.putExtra("title", productName);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }

}
