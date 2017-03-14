package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraDevice;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Paul on 01/02/2017.
 */

public class qrScanner extends Activity {
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    SurfaceView cameraView;
    Context context;
    CameraDevice camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrscanner_layout);
        context = this;

        cameraView = (SurfaceView)findViewById(R.id.camera_view);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();


        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("CAMERA SOURCE", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });



        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() == 1) {
                    processBarcode(barcodes);

                }
            }
        });

    }

    @Override
    protected void onResume(){      //onResume the barcode scanner restarts
        super.onResume();
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() == 1) {
                    processBarcode(barcodes);

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                ArrayList<orderItem> parcelItems = new ArrayList<>();
                //Bundle extras = data.getExtras();
                //boolean framed = extras.getBoolean("framed");
                //orderItem result = extras.getParcelable("Item");
                Intent returnIntent = new Intent();     //creates a return intent
                parcelItems = data.getParcelableArrayListExtra("items");
                returnIntent.putParcelableArrayListExtra("items",parcelItems);


                /*if(framed)
                {
                    orderItem frame = extras.getParcelable("Frame");
                    returnIntent.putBoolean("framed", true);      //adds the true boolean to denote that the item is framed
                    returnIntent.putExtra("Frame",frame);   //adds the orderItem to the intent
                }*/
                setResult(Activity.RESULT_OK,returnIntent);     //sends the intent back to the Cart
                finish();       //returns to the Cart
            }
            if(resultCode == Activity.RESULT_CANCELED){
                onResume();
            }
        }
    }

    public void processBarcode(SparseArray<Barcode> barcodes){
        Log.d("Barcode", barcodes.valueAt(0).displayValue);
        if(barcodes.valueAt(0).displayValue.contains(getString(R.string.website)))
        {
            barcodeDetector.release();      //stops the barcode scanner
            Intent intent = new Intent(context, ItemCheck.class);       //creates new intent
            intent.putExtra("barcode", barcodes.valueAt(0).displayValue);       //adds the scanned barcode to the intent
            startActivityForResult(intent,1);       //starts ItemCheck
        }

    }
}
