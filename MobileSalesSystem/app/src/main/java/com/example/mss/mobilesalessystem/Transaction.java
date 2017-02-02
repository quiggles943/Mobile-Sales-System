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
        barcodeInfo.setText(barcode);
    }
}
