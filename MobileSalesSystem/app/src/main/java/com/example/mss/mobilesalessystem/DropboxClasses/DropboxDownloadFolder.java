package com.example.mss.mobilesalessystem.DropboxClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DownloadBuilder;
import com.dropbox.core.v2.files.Metadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


/**
 * Created by Paul on 25/03/2017.
 */

public class DropboxDownloadFolder extends AsyncTask<Void, String, Boolean> {
    ArrayList<String> downloadFilePaths;
    DbxClientV2 client;
    Context context;
    ProgressDialog mProgressDialog;
    int count;
    public DropboxDownloadFolder(DbxClientV2 dbxClient, ArrayList<String> downloadFilePaths, Context context){
        this.client = dbxClient;
        this.context = context;
        this.downloadFilePaths = downloadFilePaths;
    }

    @Override
    protected void onPreExecute()
    {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Downloading Images");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        publishProgress();
        File mydir = context.getDir("MedImg", Context.MODE_PRIVATE); //Creating an internal dir;
        if (!mydir.exists())
        {
            mydir.mkdirs();
        }
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());

        try {
            count = downloadFilePaths.size();
            mProgressDialog.setMax(count);
            int current = 0;
            for (String path : downloadFilePaths) {
                String[] paths = path.split("/");
                File directory = cw.getDir(paths[1], Context.MODE_PRIVATE);
                // Create imageDir
                /*Boolean requiresChanging = false;
                String newFormat = null;
                if (paths[2].contains(".jpg")) {
                    String[] formatChange = paths[2].split("\\.jpg");
                    newFormat = formatChange[0] + ".png";
                    requiresChanging = true;
                }
                File mypath = null;
                if (requiresChanging) {
                    mypath = new File(directory, newFormat);
                } else {
                    mypath = new File(directory, paths[2]);
                }*/
                File mypath = null;
                mypath = new File(directory, paths[2]);
                FileOutputStream fos = null;
                fos = new FileOutputStream(mypath);
                DownloadBuilder downloadBuilder = client.files().downloadBuilder(path);
                //long fileLength = downloadBuilder.download(fos).getSize();
                Metadata data = downloadBuilder.download(fos);
                publishProgress(client.files().getMetadata(path).getName());
                //data.getPathDisplay();
            }

        }
        catch(Exception e)
        {
            return false;
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        super.onProgressUpdate(progress);
        if(!mProgressDialog.isShowing())
        {
            mProgressDialog.show();
        }
        else {
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.incrementProgressBy(1);
            mProgressDialog.setMessage(progress[0]);
            //mProgressDialog.setProgress(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mProgressDialog.dismiss();
        if (result)
            Toast.makeText(context,"Images Downloaded", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
    }
}
