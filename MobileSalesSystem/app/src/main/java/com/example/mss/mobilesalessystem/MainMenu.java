package com.example.mss.mobilesalessystem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.logging.*;

/**
 * Created by Kriss on 30/01/2017.
 */
public class MainMenu extends Activity {
    Context context;
    SQLiteDatabase pDB = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        setContentView(R.layout.mainmenu_layout);
        ImageButton transactionBtn = (ImageButton) findViewById(R.id.btn_cashpoint);
        ImageButton statsBtn = (ImageButton) findViewById(R.id.btn_statistics);
        /*if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }*/
        SharedPreferences sharedP = PreferenceManager.getDefaultSharedPreferences(this);      //oppening up shared preferences
        SharedPreferences.Editor editor = sharedP.edit();                           //openning up an editor to write to shared preferences

        String checkToken = sharedP.getString("token", "");                         //string to check if token is already there

        if(checkToken == "") {                                                      //if the token isn't there
            editor.putString("token", "bTe>(AQSs(Au9?9sS%&H6Pgke!LMm9,A?ZM9x");         //writing the token string to the shared preferences
            editor.commit();
        }

        initDatabase();
        transactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String check = "SELECT count(*) FROM image";
                Cursor cur = pDB.rawQuery(check, null);
                cur.moveToFirst();
                int checkInt = cur.getInt(0);
                if(checkInt>0) {
                    startActivity(new Intent(context, Cart.class));
                }
                else
                {
                    new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog_Alert))
                            .setTitle("No Images Found")
                            .setMessage("There are currently no images in the database")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setNegativeButton(android.R.string.ok, null).show();
                }
                pDB.close();
            }
        }
        );

        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,Statistics.class));
            }
        });
    }

    private void initDatabase(){
        try {
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
    }

}