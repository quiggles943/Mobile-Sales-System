package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Paul on 25/02/2017.
 */

public class Cart extends Activity{
    ImageButton addItemBtn, checkoutBtn;
    Context context;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.cart_layout);
        context = this;


        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context,qrScanner.class);
                startActivityForResult(intent,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                result = data.getStringExtra("title");
            }
            if(resultCode == Activity.RESULT_CANCELED){

            }
        }
    }
}
