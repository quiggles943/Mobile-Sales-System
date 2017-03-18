package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Paul on 18/03/2017.
 */

public class Refund extends Activity {
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.checkout_layout);

    }
}
