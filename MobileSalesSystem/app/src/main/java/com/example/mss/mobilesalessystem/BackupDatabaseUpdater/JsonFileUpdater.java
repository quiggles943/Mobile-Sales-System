package com.example.mss.mobilesalessystem.BackupDatabaseUpdater;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 28/03/2017.
 */

public class JsonFileUpdater extends AsyncTask<String, Void, Boolean> {

    Context context;
    String filePath;
    ProgressDialog ringDialog;
    SQLiteDatabase pDB;
    String urlString;
    AlertDialog.Builder alert;

    public JsonFileUpdater (Context c, String filePath)
    {
        this.context = c;
        this.filePath = filePath;

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
        //ringDialog.show();
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
        urlString = mSharedPreference.getString("server_url","");


    }

    @Override
    protected Boolean doInBackground(String... strings) {
        publishProgress();
        try {
            //set the URL and post data
            File file = new File(filePath);

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            ArrayList<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null)
            {
                lines.add(line);
            }

            HashMap<String,JSONArray> jsonData = new HashMap<>();
            for(String s: lines)
            {
                int found = s.indexOf('[');
                String key = s.substring(0,found-1);
                String newS = s.substring(found, s.length());

                JSONArray json = new JSONArray(newS);
                jsonData.put(key,json);
            }

            //interpretData(jsonData);
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
        //dropboxImageDownload();
        //pDB.close();
    }

    /*private void interpretData(HashMap<String, JSONArray> dataToIntepret)
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
    }*/

}