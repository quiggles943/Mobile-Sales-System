package com.example.mss.mobilesalessystem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Paul on 03/03/2017.
 */

public class InvoiceAdapter extends ArrayAdapter<Invoice> {

    private Context context;
    int layoutResourceId;
    private ArrayList<Invoice> invoiceList;
    public InvoiceAdapter(Context context, int layoutResourceId, ArrayList<Invoice> list) {
        super(context, layoutResourceId,list);
        this.context = context;
        this.layoutResourceId =layoutResourceId;
        invoiceList = list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;

        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.basic_invoice_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Invoice currentInvoice = invoiceList.get(position);
        convertView.setLongClickable(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "ProductID: " + currentInvoice.getInvoiceId() , Toast.LENGTH_SHORT).show();
            }
        });

        // set on layout
        mViewHolder.tv_invoiceTitle.setText(" "+currentInvoice.getInvoiceId());
        mViewHolder.tv_invoiceDate.setText(" "+currentInvoice.getDate());

        return convertView;

    }
    private class MyViewHolder {
        TextView tv_invoiceTitle, tv_invoiceDate, tv_frame;

        // refer on layout
        public MyViewHolder(View item) {

            tv_invoiceTitle = (TextView) item.findViewById(R.id.tv_invoiceTitle);
            tv_invoiceDate = (TextView) item.findViewById(R.id.tv_invoiceDate);
            tv_frame = (TextView) item.findViewById(R.id.tv_frame);
        }
    }
}
