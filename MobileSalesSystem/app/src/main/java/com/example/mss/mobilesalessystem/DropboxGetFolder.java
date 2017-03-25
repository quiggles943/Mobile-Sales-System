package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
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
    DropboxGetFolder(DbxClientV2 dbxClient, Context context){
        this.client = dbxClient;
        this.context = context;
    }
    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
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
            ListFolderResult result = client.files().listFolder("/MedImg/");
            downloadPaths = new ArrayList<>();
            for(Metadata metadata : result.getEntries())
            {
                entries.add(metadata.getPathDisplay());
                result = client.files().listFolderContinue(result.getCursor());
            }
            for(String fPath : databaseImgFilepaths)
            {
                for(String filePath : entries)
                {
                    if(fPath.equals(filePath.substring(1)))
                    {
                        downloadPaths.add(filePath);
                    }
                }
            }

        } catch (DbxException e) {
            e.printStackTrace();
        }
        return downloadPaths;
    }

    protected void onPostExecute(ArrayList<String> result)
    {
        super.onPostExecute(result);
    }
}
