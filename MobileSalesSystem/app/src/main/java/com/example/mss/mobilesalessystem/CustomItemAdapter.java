package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class CustomItemAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<orderItem> itemList;
    private static LayoutInflater inflater=null;
    public CustomItemAdapter(Activity mainActivity, ArrayList<orderItem> list) {
        // TODO Auto-generated constructor stub
        context = mainActivity;
        itemList = list;
        fillMockupList();
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        orderItem currentItem = itemList.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click " + position , Toast.LENGTH_SHORT).show();
            }
        });

        // set on layout
        mViewHolder.tv_title.setText(currentItem.getItemDescription());
        //mViewHolder.tv_title.setTypeface(Global.typeFace); // Global Typeface
        mViewHolder.tv_detailPrice.setText("1 x " + currentItem.getItemPrice());
        mViewHolder.tv_frame.setText(currentItem.getIsFramed());
        mViewHolder.tv_size.setText(currentItem.getItemSize());
        mViewHolder.tv_totalPrice.setText("£" + currentItem.getItemPrice());

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
        itemList = new ArrayList<>();

        itemList.add(new orderItem(""+0,"Custom title" + 0, "A4",true, (float) 7));
        itemList.add(new orderItem(""+1,"Custom title" + 1, "A5",false, (float) 5));
        itemList.add(new orderItem(""+2,"Custom title" + 2, "A3",true, (float) 12));
        itemList.add(new orderItem(""+3,"Custom title" + 3, "A6",false, (float) 3));
        itemList.add(new orderItem(""+4,"Custom title" + 4, "Keyring",false, (float) 3));
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

