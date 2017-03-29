package com.example.mss.mobilesalessystem.DropboxClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

/**
 * Created by mrjbe on 24/03/2017.
 */

public class GetDropboxAccount extends AsyncTask<Void, Void, FullAccount> {

    private DbxClientV2 dbxClient;
    private TaskDelegate  delegate;
    private Exception error;
    private Context context;
    ProgressDialog ringDialog;

    public interface TaskDelegate {
        void onAccountReceived(FullAccount account);
        void onError(Exception error);
    }

    public GetDropboxAccount(DbxClientV2 dbxClient, TaskDelegate delegate, Context context){
        this.dbxClient = dbxClient;
        this.delegate = delegate;
        this.context = context;
    }
    @Override
    protected void onPreExecute() {
        ringDialog = new ProgressDialog(context);
        ringDialog.setTitle("Retrieving Dropbox Account");
        ringDialog.setMessage("Currently retrieving Dropbox account details, please wait");
        ringDialog.setCancelable(false);
        ringDialog.show();
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
        ringDialog.dismiss();
        if (account != null && error == null){
            //User Account received successfully
            delegate.onAccountReceived(account);
        }
        else {
            // Something went wrong
            delegate.onError(error);
        }
    }
}
