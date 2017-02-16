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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kriss on 16/02/2017.
 */

public class CustomItemAdapter extends BaseAdapter {

    private Context context;

    private static LayoutInflater inflater=null;
    public CustomItemAdapter(Activity mainActivity) {
        // TODO Auto-generated constructor stub
        fillMockupList();
        context = mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mockupList.size();
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

        MockupItem currentItem = mockupList.get(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Click " + position , Toast.LENGTH_SHORT).show();
            }
        });

        // set on layout
        mViewHolder.tv_title.setText(currentItem.name);
        //mViewHolder.tv_title.setTypeface(Global.typeFace); // Global Typeface
        mViewHolder.tv_detailPrice.setText("" + currentItem.ammount + " x " + currentItem.singlePrice);
        //mViewHolder.tv_frame.setText("" + currentItem.frame);
        //mViewHolder.tv_size.setText(currentItem.size);
        mViewHolder.tv_totalPrice.setText("" + (currentItem.ammount * currentItem.singlePrice + "£"));

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

    private ArrayList<MockupItem> mockupList;

    // add items to mockup list
    private void fillMockupList(){
        int count = 20;
        mockupList = new ArrayList<MockupItem>();

        for (int i = 0; i < count; i++){
            mockupList.add(new MockupItem("Custom title" + i, true, "A4", i, i));
        }
    }

    // inner Mockup Class
    public class MockupItem {

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
    }
}

