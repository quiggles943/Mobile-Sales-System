package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.android.Auth;
import com.dropbox.core.util.StringUtil;
import com.dropbox.core.v1.DbxEntry;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by mrjbe on 24/03/2017.
 */
public class DropboxLogin extends Activity {
    TextView email,username,accountType;
    ImageButton connect;
    Context context;
    ArrayList<String> test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.dropbox_login);
        email = (TextView) findViewById(R.id.tv_user_email);
        username = (TextView) findViewById(R.id.tv_user_name);
        accountType= (TextView) findViewById(R.id.tv_user_account);

        connect = (ImageButton)findViewById(R.id.btn_sync);

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(), R.drawable.sym_sync);
        connect.setImageBitmap(icon);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ACCESS_TOKEN = DropboxClient.retrieveAccessToken(context);
                if (ACCESS_TOKEN != null) {
                    Toast.makeText(context,"Already logged in",LENGTH_SHORT);
                    return;
                }
                Auth.startOAuth2Authentication(getApplicationContext(), getString(R.string.APP_KEY));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAccessToken();
        getAccountDetails();
    }

    public void getAccessToken() {
        String accessToken = Auth.getOAuth2Token(); //generate Access Token
        if (accessToken != null) {
            //Store accessToken in SharedPreferences
            //SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences prefs = getSharedPreferences("com.example.mss.mobilesalessystem", Context.MODE_PRIVATE);
            prefs.edit().putString("access-token", accessToken).apply();

        }
        //super.onBackPressed();
    }

    public void getAccountDetails(){
        final String ACCESS_TOKEN = DropboxClient.retrieveAccessToken(context);
        if (ACCESS_TOKEN == null)return;
        new GetDropboxAccount(DropboxClient.getClient(ACCESS_TOKEN), new GetDropboxAccount.TaskDelegate() {
            @Override
            public void onAccountReceived(FullAccount account) {
                //Print account's info
                DbxClientV2 client = DropboxClient.getClient(ACCESS_TOKEN);
                email.setText(account.getEmail());
                username.setText(account.getName().getDisplayName());
                accountType.setText(account.getAccountType().name());
                /*DropboxGetFolder folder = new DropboxGetFolder(client, context);
                try {
                    test = folder.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                DropboxDownloadFolder downloadFolder = new DropboxDownloadFolder(client,test,context);
                downloadFolder.execute();*/
            }
            @Override
            public void onError(Exception error) {
                Log.d("User", "Error receiving account details.");
            }
        },context).execute();
        //com.dropbox.core.v2.files.FolderMetadata meta = DropboxImageDownloader.
    }
}
