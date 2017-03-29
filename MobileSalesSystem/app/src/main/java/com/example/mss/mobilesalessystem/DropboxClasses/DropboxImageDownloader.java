package com.example.mss.mobilesalessystem.DropboxClasses;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.users.FullAccount;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;

/**
 * Created by mrjbe on 24/03/2017.
 */

public class DropboxImageDownloader extends AsyncTask<Void, Void, FullAccount> {

    private DbxClientV2 dbxClient;
    private GetDropboxAccount.TaskDelegate delegate;
    private Exception error;

    public interface TaskDelegate {
        void onAccountReceived(FullAccount account);
        void onError(Exception error);
    }

    DropboxImageDownloader(DbxClientV2 dbxClient, GetDropboxAccount.TaskDelegate delegate){
        this.dbxClient = dbxClient;
        this.delegate = delegate;
    }

    @Override
    protected FullAccount doInBackground(Void... params) {
        try {
            //get the users FullAccount
            return dbxClient.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
            error = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(FullAccount account) {
        super.onPostExecute(account);

        if (account != null && error == null){
            //User Account received successfully
            delegate.onAccountReceived(account);
        }
        else {
            // Something went wrong
            delegate.onError(error);
        }
    }

    private boolean downloadDropboxFile (String dbPath, File localFile)
    {
        BufferedOutputStream bw = null;
        BufferedInputStream br = null;
        /*String path = DbxPathV2.getParent(path)+"/"+
        try
        {
            Metadata metadata =dbxClient.files().getMetadata(path);

        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }*/
        return false;
    }
}
