package com.example.mss.mobilesalessystem;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by mrjbe on 21/03/2017.
 */

public class ImageFromPath {

    public ImageFromPath()
    {

    }

    public static Bitmap imageFromPath (String imageFilePath, Context c)
    {
        try {
            ContextWrapper cw = new ContextWrapper(c);
            // path to /data/data/yourapp/app_data/imageDir
            String[] filepath = imageFilePath.split("/");
            File directory = cw.getDir(filepath[0], Context.MODE_PRIVATE);
            File mypath;
            String fileToFind = filepath[1];
            if (fileToFind.contains(" ")) {
                String[] splitFile = fileToFind.split(" ");
                fileToFind = "";
                for (String s : splitFile) {
                    fileToFind += s.toLowerCase() + "_";
                }
                fileToFind = fileToFind.substring(0, fileToFind.length() - 1);
            }

            mypath = new File(directory, fileToFind);

            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap image;

            image = BitmapFactory.decodeStream(new FileInputStream(mypath), null, options);

            return image;

        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
            return null;
        }

    }

}
