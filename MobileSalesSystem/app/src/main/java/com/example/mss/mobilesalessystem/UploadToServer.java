package com.example.mss.mobilesalessystem;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
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

public class UploadToServer extends AsyncTask<String, Void, Void> {
    Context context;
    ProgressDialog ringDialog;
    SQLiteDatabase pDB;

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
    protected Void doInBackground(String... strings) {
        try {
            String token = strings[0];
            JSONArray finalSalesArray = getSalesTotals();
            finalSalesArray.toString();
            //set the URL and post data
            /*String link = "http://quigleyserver.ddns.net/Group/database_test_3.php";
            String data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
            data += "&" + URLEncoder.encode("tables", "UTF-8") + "=" + URLEncoder.encode(finalSalesArray.toString(), "UTF-8");

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
                    String line;
                    while ((line = bR.readLine()) != null) {
                        lines.add(line);
                    }

                    bR.close();
                    HashMap<String, JSONArray> jsonData = new HashMap<>();
                    for (String s : lines) {
                        int found = s.indexOf('[');
                        String key = s.substring(0, found - 1);
                        String newS = s.substring(found, s.length());

                        JSONArray json = new JSONArray(newS);
                        jsonData.put(key, json);
                    }

                    break;
                case 401:
                    Log.e("Authentication error", "The token on the device was not accepted by the server");
                    break;
                case 404:
                    Log.e("Server error", "The page or directory you have attempted to acces either does not exist, is unavailable, had its name changed or has been moved. ");
                    break;
                case 405:
                    Log.e("Request error", "The request to the server was not accepted because it was the wrong request format");

            }*/
        }
        catch(Exception e)
        {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        Toast.makeText(context,"Global database updated",Toast.LENGTH_SHORT).show();
        ringDialog.dismiss();
        pDB.close();
    }

    private JSONArray getSalesTotals()
    {
        JSONArray result = null;

        try {
            JSONObject objectResult;
            HashMap<Integer, Integer> salesTotals = new HashMap<>();

            String sql = "SELECT ProductID, COUNT(ProductID) FROM invoiceitems GROUP BY ProductID";

            Cursor c = pDB.rawQuery(sql, null);
            if (!(c.moveToFirst()) || c.getCount() == 0) {

            } else {
                while (!c.isAfterLast()) {
                    salesTotals.put(c.getInt(0), c.getInt(1));
                    c.moveToNext();
                }
            }

            objectResult = new JSONObject(salesTotals);
            result = new JSONArray(objectResult.toString());


        } catch (JSONException e) {
            Log.e("JSON Creation Error:", e.getMessage());
        } catch (SQLiteException ex) {
            Log.e("Sales Totals Error:", ex.getMessage());
        }

        return result;
    }
}
