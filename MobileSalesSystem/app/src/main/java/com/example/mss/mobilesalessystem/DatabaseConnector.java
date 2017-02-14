package com.example.mss.mobilesalessystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

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
public class DatabaseConnector extends AsyncTask<String, Boolean, Void> {
    private static String url;
    private static String user;
    private static String password;

    private Context thisContext;



    public DatabaseConnector (Context context, String url, String username, String password)
    {
        thisContext = context;
        String[] array = {url,username,password};
        doInBackground(array);
    }

    //returned type required
    protected Void doInBackground(String... strings){
        try{
            url = strings[0];
            user = strings[1];
            password = strings[2];
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            String result = "Database Connection Success \n";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM product");
            ResultSetMetaData rsmd = rs.getMetaData();
            // returns all of the data in the columns
            while(rs.next()){
                result += rsmd.getColumnName(1) + ": " + rs.getString(1) + "\n";
                result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
                result += rsmd.getColumnName(4) + ": " + rs.getString(4) + "\n";
            }
            Log.d("Data", result);

            SQLiteDatabase pDB = null;

            pDB = thisContext.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);

            String SQLInsertStatement = "INSERT ON CONFLICT IGNORE product (Format, ImageID, ProdDesc, Price) VALUES();";

        }
        catch (Exception ex)
        {
            Log.e("Database ASYNC Error:", ex.getMessage());
        }

        return null;

    }

//    protected void onPostExecute( result) {
//        //Data to be returned to the program
//    }
}
