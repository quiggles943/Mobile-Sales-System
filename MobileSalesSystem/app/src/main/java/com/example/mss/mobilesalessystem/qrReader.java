package com.example.mss.mobilesalessystem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.io.IOException;

/**
 * Created by 40163180 on 27/01/2017.
 */
public class qrReader extends ActionBarActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bitmap myQRCode = BitmapFactory.decodeStream(
                    getAssets().open("HW_QR.png")
            );
            BarcodeDetector barcodeDetector =
                    new BarcodeDetector.Builder(this)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
