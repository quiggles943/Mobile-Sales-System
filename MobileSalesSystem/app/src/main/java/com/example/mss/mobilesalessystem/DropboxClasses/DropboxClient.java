package com.example.mss.mobilesalessystem.DropboxClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by mrjbe on 24/03/2017.
 */

public class DropboxClient {

    public static DbxClientV2 getClient(String ACCESS_TOKEN)
    {
        //(String client identifier, String user Locale)
        DbxRequestConfig config = new DbxRequestConfig("dropbox/mobilesalessystem", "en_EN");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        return client;
    }

    public static String retrieveAccessToken(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("com.example.mss.mobilesalessystem", Context.MODE_PRIVATE);
        String accessToken = prefs.getString("access-token",null);
        if(accessToken == null)
        {
            Log.e("Access Token","Access Token not found");
            return null;
        } else {
            return accessToken;
        }

    }

    public static Boolean logoutClient(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("com.example.mss.mobilesalessystem", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access-token",null);
        editor.putBoolean("dropbox_login", false);
        editor.commit();
        return true;
    }
}
