package com.example.mss.mobilesalessystem;

import android.content.Context;

import com.google.android.gms.drive.Drive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

/**
 * Created by Paul on 13/02/2017.
 */
//returned type required
public class DatabaseConnector extends AsyncTask<String,Boolean,>{
    private static String url;
    private static String user;
    private static String password;
    //returned type required
    protected doInBackground(String... strings){
        try{
            url = strings[0];
            user = strings[1];
            password = strings[2];
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);

            String result = "Database Connection Success \n";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from product");
            ResultSetMetaData rsmd = rs.getMetaData();
            // returns all of the data in the columns
            while(rs.next()){
                result += rsmd.getColumnName(1) + ": " + rs.getString(1) + "\n";
                result += rsmd.getColumnName(2) + ": " + rs.getString(2) + "\n";
                result += rsmd.getColumnName(3) + ": " + rs.getString(3) + "\n";
            }
        }
        catch (Exception ex)
        {

        }
    }

//    protected void onPostExecute( result) {
//        //Data to be returned to the program
//    }
}
