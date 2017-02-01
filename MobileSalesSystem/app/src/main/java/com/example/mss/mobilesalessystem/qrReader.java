package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

/**
 * Created by 40163180 on 27/01/2017.
 */
public class qrReader extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bitmap myQRCode = BitmapFactory.decodeResource(this.getResources(),R.drawable.hw_qr);
            BarcodeDetector barcodeDetector =
                    new BarcodeDetector.Builder(this)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();
            Frame myFrame = new Frame.Builder()
                    .setBitmap(myQRCode)
                    .build();
            SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);
            if(barcodes.size() != 0)
            {
                Log.d("QR Code data",barcodes.valueAt(0).displayValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
