package com.example.mss.mobilesalessystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 13/02/2017.
 */
//returned type required
public class DatabaseConnector extends AsyncTask<String, Void, Void> {

    JSONArray jsonArr;
    Context context;


    @Override
    protected Void doInBackground(String... strings) {
        String token = strings[0];

        try {

            String link = "http://quigleyserver.ddns.net/Group/database_test.php";
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
                   StringBuilder sB = new StringBuilder();
                   String line = null;
                    while ((line = bR.readLine()) != null) {
                     sB.append(line);
                     break;
                  }

                    bR.close();

                    jsonArr = new JSONArray(line);

                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject row;
                        try {
                            row = jsonArr.getJSONObject(i);

                        } catch (JSONException e) {
                            Log.e("JSON error : ", e.getMessage().toString());
                        }
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

            }
        } catch (Exception e) {
            Log.e("ASYNC Error :", e.getMessage().toString());
        }

        return null;
    }
}
