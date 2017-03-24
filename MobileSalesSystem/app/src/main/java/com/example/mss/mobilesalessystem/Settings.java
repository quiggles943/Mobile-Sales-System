package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ListView;

/**
 * Created by Paul on 23/03/2017.
 */

public class Settings extends Activity {
    Context context;
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

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View view = super.onCreateView(inflater, container, savedInstanceState);
            view.setBackgroundResource(R.drawable.background);
            return view;
        }
    }
}
