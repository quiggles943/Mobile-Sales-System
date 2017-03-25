package com.example.mss.mobilesalessystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 13/02/2017.
 */
//returned type required
public class DatabaseConnector extends AsyncTask<String, Void, Boolean> {

    //JSONArray jsonArr;
    Context context;
    String[] tables;
    String tablesJSON, token;
    ProgressDialog ringDialog;
    SQLiteDatabase pDB;
    String urlString;
    AlertDialog.Builder alert;

    public DatabaseConnector (Context c, String[] tables, String token)
    {
        this.context = c;
        this.tables = tables;
        try {
            JSONArray tablesArray = new JSONArray(tables);
            tablesJSON = tablesArray.toString();
        }
        catch (Exception ex) {

        }
        this.token = token;

    }

    @Override
    protected void onPreExecute()
    {
        //Toast.makeText(context,"Local database update started",Toast.LENGTH_SHORT).show();
        //Set up the ring dialog
        ringDialog = new ProgressDialog(context);
        ringDialog.setTitle("Downloading Data");
        ringDialog.setMessage("Currently Downloading Database, please wait");
        ringDialog.setCancelable(false);
        ringDialog.show();
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        urlString = mSharedPreference.getString("server_url","");


    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            //set the URL and post data
            if(urlString.equals("") ||urlString == null)
            {
                return false;
            }
            String link = "http://"+urlString+"/database_test_2.php";
            //String link = "http://quigleyserver.ddns.net/Group/database_test_2.php";
            String data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
            data += "&" + URLEncoder.encode("tables", "UTF-8") + "=" + URLEncoder.encode(tablesJSON, "UTF-8");

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();


            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //write the post data
            wr.write(data);
            wr.flush();
            //gets the response code from the web server
            int code = conn.getResponseCode();
            switch(code) {
                case 200:
                    //reads the data from the web server
                    BufferedReader bR = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    ArrayList<String> lines = new ArrayList<>();
                    String line;
                    while ((line = bR.readLine()) != null) {
                        lines.add(line);
                  }

                    bR.close();
                    HashMap<String,JSONArray> jsonData = new HashMap<>();
                    for(String s:lines)
                    {
                        int found = s.indexOf('[');
                        String key = s.substring(0,found-1);
                        String newS = s.substring(found, s.length());

                        JSONArray json = new JSONArray(newS);
                        jsonData.put(key,json);
                    }

                    interpretData(jsonData);
                    return true;
                case 401:
                    Log.e("Authentication error", "The token on the device was not accepted by the server");
                    return false;
                case 404:
                    Log.e("Server error", "The page or directory you have attempted to acces either does not exist, is unavailable, had its name changed or has been moved. ");
                    return false;
                case 405:
                    Log.e("Request error", "The request to the server was not accepted because it was the wrong request format");
                    return false;
            }
        } catch (Exception e) {
            Log.e("ASYNC Error :", e.getMessage().toString());
            return false;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        ringDialog.dismiss();
        if(result) {
            Toast.makeText(context, "Local database updated", Toast.LENGTH_SHORT).show();
        }else{
            alert = new AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_Material_Dialog_Alert))
                    .setTitle("Connection Error")
                    .setMessage("There is no database URL specified")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.ok,null);
            alert.show();
        }

        //pDB.close();
    }

    private void interpretData(HashMap<String, JSONArray> dataToIntepret)
    {

        for (Map.Entry<String, JSONArray> data : dataToIntepret.entrySet())
        {
            String sqlStatement = "INSERT OR REPLACE INTO ";
            sqlStatement += data.getKey();
            sqlStatement += " (";

            String sqlStart = sqlStatement;

            JSONArray jA = data.getValue();
            for (int i = 0; i < jA.length(); i  ++) {
                try {
                    JSONObject row;

                    row = jA.getJSONObject(i);

                    Iterator<String> iterColumns = row.keys();

                    while(iterColumns.hasNext()) {
                        String key = iterColumns.next();

                        sqlStatement += key;

                        sqlStatement += ",";
                    }

                    sqlStatement = sqlStatement.substring(0, sqlStatement.length()-1);

                    sqlStatement += ") VALUES (";

                    Iterator<String> iterValues = row.keys();

                    while (iterValues.hasNext()) {
                        String key = iterValues.next();

                        sqlStatement += "'";

                        sqlStatement += row.get(key);

                        sqlStatement += "',";
                    }

                    sqlStatement = sqlStatement.substring(0, sqlStatement.length()-1);
                    sqlStatement += ");";

                    //run SQL
                    pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

                    pDB.execSQL(sqlStatement);

                    sqlStatement = sqlStart;

                } catch (JSONException e) {
                    Log.e("JSON PARCING ERROR : ", e.getMessage().toString());
                } catch (SQLiteException sqlE) {
                    Log.e("SQL INSERTION ERROR : ", sqlE.getMessage().toString());
                    sqlStatement = sqlStart;
                }
            }
        }
    }

    protected void onProgressUpdate(Void... values)
    {

    }
}
