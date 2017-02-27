package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kriss on 16/02/2017.
 */

public class CustomItemAdapter extends ArrayAdapter<orderItem> {

    private Context context;
    int layoutResourceId;
    private ArrayList<orderItem> itemList;
    public CustomItemAdapter(Context context, int layoutResourceId, ArrayList<orderItem> list) {
        super(context, layoutResourceId,list);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.layoutResourceId =layoutResourceId;
        itemList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            //convertView = inflater.inflate(layoutResourceId, parent, false);
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final orderItem currentItem = itemList.get(position);
        convertView.setLongClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ProductID: " + currentItem.getItemID() , Toast.LENGTH_SHORT).show();
            }
        });
        /*convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public void onLongClick(View v) {

            }
        });*/

        // set on layout
        mViewHolder.tv_title.setText(currentItem.getItemDescription());
        //mViewHolder.tv_title.setTypeface(Global.typeFace); // Global Typeface
        mViewHolder.tv_detailPrice.setText("1 x " + currentItem.getFormat().getPrice());
        mViewHolder.tv_frame.setText(currentItem.getIsFramed());
        mViewHolder.tv_size.setText(currentItem.getFormat().getFormatDescription());
        mViewHolder.tv_totalPrice.setText("£" + currentItem.getFormat().getPrice());

        return convertView;

    }
    private class MyViewHolder {
        TextView tv_title, tv_size, tv_frame, tv_detailPrice, tv_totalPrice;

        // refer on layout
        public MyViewHolder(View item) {

            tv_title = (TextView) item.findViewById(R.id.tv_itemTitle);
            tv_size = (TextView) item.findViewById(R.id.tv_format);
            tv_frame = (TextView) item.findViewById(R.id.tv_frame);
            tv_detailPrice = (TextView) item.findViewById(R.id.tv_detail_price);
            tv_totalPrice = (TextView) item.findViewById(R.id.tv_total_price);
        }
    }
    /////// *** MOCKUP DATA *** ///////
    // just fot test purpose

    // add items to mockup list
    private void fillMockupList(){
//        itemList = new ArrayList<>();

//        itemList.add(new orderItem(""+0,"Custom title" + 0,  new Format("A4", "A4", true,(float) 5 ), true));
//        itemList.add(new orderItem(""+1,"Custom title" + 1,  new Format("A5", "A5", false,(float) 5 ), false));
//        itemList.add(new orderItem(""+2,"Custom title" + 2,  new Format("A3", "A3", true,(float) 12 ), true));
//        itemList.add(new orderItem(""+3,"Custom title" + 3,  new Format("A6", "A6", false,(float) 3 ), false));
//        itemList.add(new orderItem(""+4,"Custom title" + 4,  new Format("Keyring", "Keyring", false,(float) 3 ), false));
    }

    // inner Mockup Class
    /*public class MockupItem {

        public String name;
        public boolean frame;
        public String size;
        public double singlePrice;
        public int ammount;

        public MockupItem (String name, boolean frame, String size, double singlePrice, int ammount) {
            this.name = name;
            this.frame = frame;
            this.singlePrice = singlePrice;
            this.ammount = ammount;
        }
    }*/
}

