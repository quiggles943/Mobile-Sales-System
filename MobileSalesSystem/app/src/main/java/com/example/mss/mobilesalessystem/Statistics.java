package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;
import com.example.mss.mobilesalessystem.DropboxClasses.*;

import java.util.ArrayList;
import java.util.HashMap;

import static android.os.AsyncTask.SERIAL_EXECUTOR;

/**
 * Created by Paul on 25/02/2017.
 */

public class Statistics extends Activity {
    Context context;
    SQLiteDatabase pDB = null;
    ImageButton syncFromDb, syncToDb, settings;

    ExpandableListView listView;
    ExpandableInvoiceAdapter expInvAdap;
    ArrayList<Invoice> expListTitles;
    HashMap<Invoice, ArrayList<InvoiceItems>> expListDetails;
    Invoice selectedInvoice;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.statistic_layout);
        listView = (ExpandableListView)findViewById(R.id.elv_itemList);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        expListTitles = new ArrayList<>();
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        String query = "SELECT * FROM invoice";
        Cursor cur = pDB.rawQuery(query,null);
            if(!(cur.moveToFirst()) || cur.getCount() ==0)
            {

            }
            else {
                while (!cur.isAfterLast()) {
                    expListTitles.add(new Invoice(cur.getInt(0), cur.getString(1), cur.getInt(2), cur.getString(4), cur.getFloat(5)));
                    cur.moveToNext();
                }
            }

        expListDetails = getHashedData();

        expInvAdap = new ExpandableInvoiceAdapter(this, expListTitles,expListDetails);
        listView.setAdapter(expInvAdap);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = listView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
                Invoice invoice = expListTitles.get(groupPosition);


        /*  if group item clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    Toast.makeText(context,"Invoice Order "+invoice.getInvoiceId(),Toast.LENGTH_SHORT).show();
                }

        /*  if child item clicked */
                else if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    InvoiceItems item = expListDetails.get(invoice).get(childPosition);
                    Toast.makeText(context,"Invoice item Id "+item.getItemID(),Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

        syncToDb = (ImageButton)findViewById(R.id.btn_sync_to_db);
        syncFromDb = (ImageButton)findViewById(R.id.btn_sync_from_db);

        syncFromDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);          //opening a connectivity manager
                NetworkInfo networkType = cManager.getActiveNetworkInfo();
                final String token = mSharedPreference.getString("token","");                               //gaining the token
                final DatabaseConnector connector = new DatabaseConnector(context, new String[]{"product","format","image"}, token);      //creating a new database connector                 //openning up an editor to write to shared preferences

                    if (networkType == null) {
                        Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();        //Toast to say there is no internet at all
                    } else if (networkType.getType() == ConnectivityManager.TYPE_WIFI) {
                        try {
                            //boolean dataDownloaded = connector.execute(token).get();//running the database connector with the token
                            connector.executeOnExecutor(SERIAL_EXECUTOR);
                            dropboxImageDownload();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (networkType.getType() == ConnectivityManager.TYPE_MOBILE) {
                        new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog_Alert))
                                .setTitle("Confirm Database Sync")
                                .setMessage("You are on mobile data, are you sure you would like to progress?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        try {
                                            //boolean dataDownloaded = connector.execute(token).get();
                                            connector.executeOnExecutor(SERIAL_EXECUTOR);
                                        }catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    }
            }
        });
        syncToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);          //opening a connectivity manager
                NetworkInfo networkType = cManager.getActiveNetworkInfo();
                final String token = mSharedPreference.getString("token","");                               //gaining the token
                final UploadToServer serverUpdate = new UploadToServer(context);      //creating a new database connector      //creating a new database connector                 //openning up an editor to write to shared preferences

                if (networkType == null) {
                    Toast.makeText(context, "No internet available", Toast.LENGTH_SHORT).show();        //Toast to say there is no internet at all
                } else if (networkType.getType() == ConnectivityManager.TYPE_WIFI) {
                    try {
                        serverUpdate.execute(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (networkType.getType() == ConnectivityManager.TYPE_MOBILE) {
                    new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog_Alert))
                            .setTitle("Confirm Database Sync")
                            .setMessage("You are on mobile data, are you sure you would like to progress?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    try {
                                        serverUpdate.execute(token);
                                    }catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }
            }
        });

        settings = (ImageButton)findViewById(R.id.btn_settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Settings.class);
                startActivity(intent);
            }
        });

        final String url = mSharedPreference.getString("server_url","");
        registerForContextMenu(listView);


    }

    public void dropboxImageDownload()
    {
        final String ACCESS_TOKEN = DropboxClient.retrieveAccessToken(context);
        if (ACCESS_TOKEN == null)
        {
            new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog_Alert))
                    .setTitle("Connection Error")
                    .setMessage("There is no Dropbox account connected")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok,null).show();
            return;
        }
        new GetDropboxAccount(DropboxClient.getClient(ACCESS_TOKEN), new GetDropboxAccount.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Print account's info
                ArrayList<String> test = null;
                final DbxClientV2 client = DropboxClient.getClient(ACCESS_TOKEN);
                new DropboxGetFolder(client, new DropboxGetFolder.TaskDelegate() {
                    @Override
                    public void onDataReceived(ArrayList<String> downloadPaths)
                    {
                        DropboxDownloadFolder downloadFolder = new DropboxDownloadFolder(client,downloadPaths,context);
                        downloadFolder.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    }
                    @Override
                    public void onError(Exception error) {
                        Log.d("User", "Error receiving filePath details.");
                    }

                }, context).execute();
                /*try {
                    folder.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    test = folder.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/


            }
            @Override
            public void onError(Exception error) {
                Log.d("User", "Error receiving account details.");
            }
        },context).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    private HashMap<Invoice, ArrayList<InvoiceItems>> getHashedData()
    {
        HashMap<Invoice, ArrayList<InvoiceItems>> result = new HashMap<>();

        for(Invoice i: expListTitles)
        {
            String sqlStatement = "SELECT * FROM invoiceitems WHERE InvoiceID = " + i.getInvoiceId() + ";";

            ArrayList<InvoiceItems> items = new ArrayList<>();

            pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

            Cursor c = pDB.rawQuery(sqlStatement, null);

            if (!(c.moveToFirst()) || c.getCount() == 0)
            {

            } else {
                while (!c.isAfterLast()) {
                    String sql = "SELECT i.imageDesc, i.MedImgFilePath FROM image i JOIN product p ON i.imageID=p.imageID WHERE ProductID = " + c.getString(1) + ";";
                    Cursor cu = pDB.rawQuery(sql, null);
                    if (!(cu.moveToFirst()) || cu.getCount() == 0) {

                    } else {
                        items.add(new InvoiceItems(c.getInt(0), c.getString(1), cu.getString(0), c.getInt(2), cu.getString(1)));
                        c.moveToNext();
                    }
                }
            }
            for(InvoiceItems item : items)
            {
                String sql = "SELECT p.format, p.price FROM  product p WHERE ProductID = " + item.getItemID() + ";";
                Cursor cu = pDB.rawQuery(sql,null);
                if (!(cu.moveToFirst()) || cu.getCount() == 0)
                {

                } else {
                    while (!cu.isAfterLast()) {
                        item.setFormat(cu.getString(0));
                        item.setPrice(cu.getFloat(1));
                        cu.moveToNext();
                    }
                }
            }
            result.put(i, items);

        }



        return result;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v , menuInfo);
        Log.i("", "Click");
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        Invoice invoice = expListTitles.get(groupPosition);

        // Show context menu for groups
        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            menu.setHeaderTitle("Invoice "+invoice.getInvoiceId());
            menu.add("Refund");

            // Show context menu for children
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            //InvoiceItems item = expListDetails.get(invoice).get(childPosition);
            //menu.setHeaderTitle(item.getItemDescription());
            //menu.add(0, 0, 1, "View");
        }
    }

    public boolean onContextItemSelected(MenuItem selected)
    {
        ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) selected.getMenuInfo();

        int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        Invoice invoice = expListTitles.get(groupPosition);

        if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            // do something with parent
            if(selected.toString().equals("Refund"))
            {
                //Invoice invoice = (Invoice) listview.getItemAtPosition(info.position);
                Intent intent = new Intent(context, Refund.class);
                intent.putExtra("invoice", invoice);
                startActivityForResult(intent, 0);
            }

        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            // do someting with child
            InvoiceItems item = expListDetails.get(invoice).get(childPosition);
            //Toast.makeText(context,"Invoice item Id "+item.getItemID(),Toast.LENGTH_SHORT).show();
        }

        return super.onContextItemSelected(selected);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 0){
            if(resultCode == 1){
                selectedInvoice = data.getParcelableExtra("invoice");
                Invoice removedInvoice = null;
                for(Invoice invoice: expListTitles){
                    if(invoice.getInvoiceId() == selectedInvoice.getInvoiceId())
                    {
                        removedInvoice = invoice;
                    }
                }
                boolean invoiceRemoved = expListTitles.remove(removedInvoice);

                String sql = "DELETE FROM invoice WHERE InvoiceID ="+removedInvoice.getInvoiceId()+";";
                pDB.execSQL(sql);
                sql = "DELETE FROM invoiceitems WHERE InvoiceID ="+removedInvoice.getInvoiceId()+";";
                pDB.execSQL(sql);

                expInvAdap.notifyDataSetChanged();
                String currentDate = (String) DateFormat.format("ddMMM  HH:mm:ss", removedInvoice.getDate());
                AlertDialog.Builder invoiceRemovedDialog = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog));
                if(invoiceRemoved)
                {           invoiceRemovedDialog.setTitle("Invoice Removed")
                            .setMessage("Invoice: "+removedInvoice.getInvoiceId()+"\nDate: "+currentDate+"\nPrice: "+String.format("£ %.2f",removedInvoice.getAmountPaid())+"\nhas been removed")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
                else if(!invoiceRemoved)
                {
                    invoiceRemovedDialog.setTitle("Invoice Removal Error")
                            .setMessage("Invoice: "+removedInvoice.getInvoiceId()+" has not been able to be refunded through the app")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }

            }
            else{
                selectedInvoice = null;
            }
        }
    }



}
