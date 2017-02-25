package com.example.mss.mobilesalessystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 13/02/2017.
 */
//returned type required
public class DatabaseConnector extends AsyncTask<String, Void, Void> {

    JSONArray jsonArr;
    Context context;

    public DatabaseConnector (Context c)
    {
        this.context = c;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String token = strings[0];

        try {

            String link = "http://quigleyserver.ddns.net/Group/database_test_2.php";
            String data = URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();


            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            int code = conn.getResponseCode();
            switch(code) {
                case 200:
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
                    break;
                case 401:
                    Log.e("Authentication error", "The token on the device was not accepted by the server");
                    break;
                case 404:
                    Log.e("Server error", "The page or directory you have attempted to acces either does not exist, is unavailable, had its name changed or has been moved. ");
                    break;
                case 405:
                    Log.e("Request error", "The request to the server was not accepted because it was the wrong request format");

            }
        } catch (Exception e) {
            Log.e("ASYNC Error :", e.getMessage().toString());
        }

        return null;
    }

    private void interpretData(HashMap<String, JSONArray> dataToIntepret)
    {
        for (Map.Entry<String, JSONArray> data : dataToIntepret.entrySet())
        {
            String sqlStatement = "INSERT INTO ";
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
                    sqlStatement += ") ON CONFLICT IGNORE;";

                    //run SQL
                    SQLiteDatabase pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

                    pDB.execSQL(sqlStatement);

                    sqlStatement = sqlStart;

                } catch (JSONException e) {
                    Log.e("JSON PARCING ERROR : ", e.getMessage().toString());
                } catch (SQLiteException sqlE) {
                    Log.e("SQL INSERTION ERROR : ", sqlE.getMessage().toString());
                }
            }
        }
    }
}
