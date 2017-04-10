package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mss.mobilesalessystem.BackupDatabaseUpdater.JsonFileUpdater;

import java.net.URISyntaxException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Paul on 23/03/2017.
 */

public class Settings extends Activity {
    static Context context;
    ListView listView;
    ImageButton checkout,newItem;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.cart_layout);
        listView = (ListView)findViewById(R.id.lv_itemList);
        checkout = (ImageButton)findViewById(R.id.btn_checkout);
        newItem = (ImageButton)findViewById(R.id.btn_add_item);
        ViewManager parentView = (ViewManager) checkout.getParent();
        parentView.removeView(checkout);
        parentView.removeView(newItem);
        parentView = (ViewManager) listView.getParent();

        getFragmentManager().beginTransaction()
                            .replace(android.R.id.content, new SettingsFragment())
                            .commit();

    }

    public static class SettingsFragment extends PreferenceFragment implements AdapterView.OnItemLongClickListener {
        private ArrayList<Preference> mPreferences = new ArrayList<>();
        private String[] mPreferenceKeys = new String[] {"server_url", "dropbox_details","dropbox_login", "version_Info"};
        private SharedPreferences.OnSharedPreferenceChangeListener mListener;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            mListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    for (Preference pref : mPreferences) {
                        if (pref.getKey().equals(key)) {
                            if (pref instanceof EditTextPreference) {
                                pref.setSummary(sharedPreferences.getString(key, "The URL used to connect to the global database"));
                            } else if (pref.getKey().equals("dropbox_details"))
                            {
                                pref.setSummary("Name: "+sharedPreferences.getString("dropbox_name","n/a")+"\nEmail: "+sharedPreferences.getString("dropbox_email","n/a"));
                            }
                            else if (pref.getKey().equals("dropbox_login"))
                            {
                                if(sharedPreferences.getBoolean("dropbox_login",false)) {
                                    pref.setTitle("Log Out");
                                }
                                else
                                {
                                    pref.setTitle("Login");
                                }
                            }

                            //break;
                        }

                    }
                }
            };
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
            prefs.registerOnSharedPreferenceChangeListener(mListener);
            for (String prefKey : mPreferenceKeys) {
                Preference pref = (Preference) getPreferenceManager().findPreference(prefKey);
                mPreferences.add(pref);
                mListener.onSharedPreferenceChanged(prefs, prefKey);
            }
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            if(view != null)
            {
                View lv = view.findViewById (android.R.id.list);
                if (lv instanceof ListView)
                {
                    ((ListView)lv).setOnItemLongClickListener(this);
                }
                else
                {
                    //The view created is not a list view!
                }
            }
            view.setBackgroundResource(R.drawable.background);
            return view;
        }

        @Override
        public void onResume()
        {
            super.onResume();
            SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
            prefs.registerOnSharedPreferenceChangeListener(mListener);
            for (String prefKey : mPreferenceKeys) {
                Preference pref = (Preference) getPreferenceManager().findPreference(prefKey);
                mPreferences.add(pref);
                mListener.onSharedPreferenceChanged(prefs, prefKey);
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
            Preference pref = (Preference) getPreferenceManager().findPreference("version_Info");
            if (pref.getKey().equals("version_Info"))
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(context, "Please install a File Manager.",Toast.LENGTH_SHORT).show();
                }
            }

            return false;
        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case FILE_SELECT_CODE:
                    if (resultCode == RESULT_OK) {
                        // Get the Uri of the selected file
                        String uri = data.getData().getPath();
                        Log.d(TAG, "File Uri: " + uri);
                        JsonFileUpdater updater = new JsonFileUpdater(context, uri);
                        updater.execute();

                    }
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);

        }
    }



    private static final int FILE_SELECT_CODE = 0;

}
