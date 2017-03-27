package com.example.mss.mobilesalessystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 22/03/2017.
 */

public class InitDatabase extends AsyncTask<String, Void, Void> {
    Context context;
    ProgressDialog ringDialog;
    SQLiteDatabase pDB;

    public InitDatabase (Context c)
    {
        this.context = c;
    }
    @Override
    protected void onPreExecute()
    {
        //Toast.makeText(context,"Local database update started",Toast.LENGTH_SHORT).show();
        //Set up the ring dialog
        ringDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
        ringDialog.setTitle("Initialising Database");
        ringDialog.setMessage("Currently initialising the database");
        ringDialog.setCancelable(false);
        ringDialog.show();

    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
            String [] tableName = new String[5];
            String [] tableColumns = new String[5];

            tableName[2] = "product";
            tableColumns[2] = "ProductID INTEGER, ProductDescription text, Format text, ImageID text, Price REAL, Framable INTEGER, PRIMARY KEY (ProductID), FOREIGN KEY (Format) REFERENCES format(FormatID), FOREIGN KEY (ImageID) references image(ImageID)";

            tableName[1] = "image";
            tableColumns[1] = "ImageID text, ImageDesc text, MedImgFilePath text, QRCode text, PRIMARY KEY (ImageID)";

            tableName[0] = "format";
            tableColumns[0] = "Format text, FormatDesc text, PRIMARY KEY (Format)";

            tableName[3] = "invoice";
            tableColumns[3] = "InvoiceID INTEGER, Date DATE, CustomerID INTEGER, ShippingCost REAL, PaymentMethod text, AmountPaid REAL, PRIMARY KEY (InvoiceID)";

            tableName[4] = "invoiceitems";
            tableColumns[4] = "InvoiceID INTEGER, ProductID INTEGER, Qty INTEGER, PRIMARY KEY (InvoiceID, ProductID), FOREIGN KEY (InvoiceID) REFERENCES invoice(InvoiceID), FOREIGN KEY (ProductID) REFERENCES product(ProductID)";


            if(tableName.length == tableColumns.length) {
                for (int i = 0; i < tableName.length; i++) {
                    pDB.execSQL("CREATE TABLE IF NOT EXISTS " + tableName[i] + " (" + tableColumns[i] + ");");
                }
            }



        }catch(Exception e){
            Log.e("Database Creation Error", e.getMessage().toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        ringDialog.dismiss();
        pDB.close();
    }
}
