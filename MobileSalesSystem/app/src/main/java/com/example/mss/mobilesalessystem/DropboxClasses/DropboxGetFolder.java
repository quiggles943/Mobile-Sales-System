package com.example.mss.mobilesalessystem.DropboxClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.CreateFolderErrorException;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.GetMetadataErrorException;
import com.dropbox.core.v2.files.ListFolderErrorException;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.users.FullAccount;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Paul on 25/03/2017.
 */

public class DropboxGetFolder extends AsyncTask<Void, Void, ArrayList<String>> {
    DbxClientV2 client;
    Context context;
    SQLiteDatabase pDB;
    ProgressDialog ringDialog;
    private TaskDelegate delegate;
    public DropboxGetFolder(DbxClientV2 dbxClient, TaskDelegate taskDelegate, Context context){
        this.client = dbxClient;
        this.delegate = taskDelegate;
        this.context = context;
    }

    public interface TaskDelegate {
        void onDataReceived(ArrayList<String> downloadPaths);
        void onError(Exception error);
    }

    @Override
    protected void onPreExecute()
    {
        ringDialog = new ProgressDialog(context);
        ringDialog.setTitle("Retrieving Image Data");
        ringDialog.setMessage("Currently Retrieving Image Data, please wait");
        ringDialog.setCancelable(false);
        ringDialog.show();
    }

    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        publishProgress();
        pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        ArrayList<String> downloadPaths = null;
        String sql = "SELECT MedImgFilePath FROM image";
        ArrayList<String> databaseImgFilepaths = new ArrayList<>();
        Cursor c = pDB.rawQuery(sql, null);
        if (!(c.moveToFirst()) || c.getCount() == 0) {
            Log.e("Database Connection", "No file paths were found");
        } else {
            while (!c.isAfterLast()) {
                databaseImgFilepaths.add(c.getString(0));
                c.moveToNext();
            }
        }
        ArrayList<String> entries = new ArrayList<>();
        try {
            ListFolderResult result = null;
            try {
                result = client.files().listFolder("/MedImg/");
            }catch (ListFolderErrorException ex)
            {
                try {
                    FolderMetadata data = client.files().createFolder("/MedImg", false);
                    data.getPathDisplay();
                    result = client.files().listFolder("/MedImg/");
                }
                catch(CreateFolderErrorException e)
                {
                    Log.e("error", e.getMessage());
                }
            }
            downloadPaths = new ArrayList<>();
            for(Metadata metadata : result.getEntries())
            {
                entries.add(metadata.getPathDisplay());
                //result = client.files().listFolderContinue(result.getCursor());
            }
            for(String filePath : entries)
            {

                if(databaseImgFilepaths.contains(filePath.substring(1)))
                {
                    downloadPaths.add(filePath);
                }
            }

        } catch (DbxException e) {
            e.printStackTrace();
        }
        return downloadPaths;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        ringDialog.dismiss();
        super.onPostExecute(result);
        delegate.onDataReceived(result);
    }
    @Override
    protected void onProgressUpdate(Void... values)
    {
        super.onProgressUpdate();

    }

}
