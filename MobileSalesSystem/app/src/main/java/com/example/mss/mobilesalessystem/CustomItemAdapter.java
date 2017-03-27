package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

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
            //create the list item view
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.listitem, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }
        //get item at position
        final orderItem currentItem = itemList.get(position);
        convertView.setLongClickable(true);
        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ProductID: " + currentItem.getItemID() , Toast.LENGTH_SHORT).show();
            }
        });*/

        // set on layout
        mViewHolder.tv_title.setText(currentItem.getItemDescription());
        //mViewHolder.tv_title.setTypeface(Global.typeFace); // Global Typeface
        mViewHolder.tv_detailPrice.setText("");
        mViewHolder.tv_frame.setText(currentItem.getIsFramed());
        mViewHolder.tv_size.setText(currentItem.getFormat().getFormatDescription());
        mViewHolder.tv_totalPrice.setText("£" + currentItem.getPrice());
        mViewHolder.thumbnail.setImageBitmap(ImageFromPath.imageFromPath(currentItem.getImgFilePath(), context));
        return convertView;

    }
    private class MyViewHolder {
        TextView tv_title, tv_size, tv_frame, tv_detailPrice, tv_totalPrice;
        ImageView thumbnail;

        // refer on layout
        public MyViewHolder(View item) {
            //sets the View Holder Text Views
            tv_title = (TextView) item.findViewById(R.id.tv_itemTitle);
            tv_size = (TextView) item.findViewById(R.id.tv_format);
            tv_frame = (TextView) item.findViewById(R.id.tv_frame);
            tv_detailPrice = (TextView) item.findViewById(R.id.tv_detail_price);
            tv_totalPrice = (TextView) item.findViewById(R.id.tv_total_price);
            thumbnail = (ImageView)item.findViewById(R.id.iv_thumbnail);
        }
    }
}

