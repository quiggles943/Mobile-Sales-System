package com.example.mss.mobilesalessystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 20/03/2017.
 */

public class UploadToServer extends AsyncTask<String, Void, Boolean> {
    Context context;
    ProgressDialog ringDialog;
    SQLiteDatabase pDB;
    String message;

    public UploadToServer (Context c)
    {
        this.context = c;
        //Open Database
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
    }

    @Override
    protected void onPreExecute()
    {
        //Toast.makeText(context,"Local database update started",Toast.LENGTH_SHORT).show();
        //Set up the ring dialog
        ringDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_DARK);
        ringDialog.setTitle("Uploading Data");
        ringDialog.setMessage("Currently uploading database information, please wait");
        ringDialog.setCancelable(false);
        ringDialog.show();


    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            boolean success = true;
            String token = strings[0];
            JSONArray finalSalesArray = getSalesTotals();
            //set the URL and post data
            String link = "http://quigleyserver.ddns.net/Group/database_test_3.php";
            String data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
            data += "&" + URLEncoder.encode("items_sold", "UTF-8") + "=" + URLEncoder.encode(finalSalesArray.toString(), "UTF-8");

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();


            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //write the post data
            wr.write(data);
            wr.flush();
            //gets the response code from the web server
            int code = conn.getResponseCode();
            switch (code) {
                case 200:
                    //reads the data from the web server
                    BufferedReader bR = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    ArrayList<String> lines = new ArrayList<>();
                    ArrayList<String> failedUploads = new ArrayList<>();
                    String line;
                    int i = 0;
                    while ((line = bR.readLine()) != null) {
                        lines.add(line);
                        i++;
                    }
                    if(i == 0){
                        success = false;
                        message = "There were no transactions to upload";
                    }
                    for(String result: lines)
                    {
                        String[]resultSplit = result.split(":");
                        if(resultSplit[1].equals("0"))
                        {
                            failedUploads.add(resultSplit[0]);
                            success = false;
                        }
                    }
                    if(!success && i>0)
                    {
                        message = failedUploads.size()+" invoice(s) were unable to be uploaded";
                    }
                    break;
                case 401:
                    Log.e("Authentication error", "The token on the device was not accepted by the server");
                    break;
                case 404:
                    Log.e("Page not found", "The page or directory you have attempted to access either does not exist, is unavailable, had its name changed or has been moved. ");
                    break;
                case 405:
                    Log.e("Request error", "The request to the server was not accepted because it was the wrong request format");
                    break;
                case 500:
                    Log.e("Server error", "The server has thrown an error on it's side.");
                    break;
            }
            if(success) {
                message = "Global database updated";
                return true;
            }
            else
            {
                return false;
            }
        }
        catch(Exception e)
        {
            Log.e("Exception",e.getMessage());
            return false;
        }


    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        ringDialog.dismiss();
        AlertDialog.Builder alert = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog))
                                 .setTitle("Global Database Upload")
                                 .setMessage(message);
        if(result) {
            Toast.makeText(context, "Global database updated", Toast.LENGTH_SHORT).show();
            alert.setIcon(android.R.drawable.ic_dialog_info)
                 .setPositiveButton(android.R.string.ok, null).show();
        }
        else
        {
            Toast.makeText(context, "Global database update was not successful", Toast.LENGTH_SHORT).show();
            alert.setIcon(android.R.drawable.ic_dialog_alert)
                    .setNegativeButton(android.R.string.ok, null).show();
        }
        pDB.close();
    }

    private JSONArray getSalesTotals()
    {
        JSONArray result = null;

        try {
            JSONObject objectResult;
            HashMap<String, Integer> salesTotals = new HashMap<>();

            String sql = "SELECT ProductID, Qty FROM invoiceitems GROUP BY ProductID";

            Cursor c = pDB.rawQuery(sql, null);
            if (!(c.moveToFirst()) || c.getCount() == 0) {

            } else {
                while (!c.isAfterLast()) {
                    salesTotals.put(c.getString(0), c.getInt(1));
                    c.moveToNext();
                }
            }

            objectResult = new JSONObject(salesTotals);
            result = new JSONArray();
            result.put(objectResult);

        } catch (SQLiteException ex) {
            Log.e("Sales Totals Error:", ex.getMessage());
        }

        return result;
    }
}
