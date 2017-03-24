package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dropbox.core.android.Auth;

/**
 * Created by mrjbe on 24/03/2017.
 */
public class DropboxLogin extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox_login);
        Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.APP_KEY));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
    }

    public void getAccessToken() {
        String accessToken = Auth.getOAuth2Token(); //generate Access Token
        if (accessToken != null) {
            //Store accessToken in SharedPreferences
            //SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences prefs = getSharedPreferences("com.example.mss.mobilesalessystem", Context.MODE_PRIVATE);
            prefs.edit().putString("access-token", accessToken).apply();

        }
        super.onBackPressed();
    }
}
