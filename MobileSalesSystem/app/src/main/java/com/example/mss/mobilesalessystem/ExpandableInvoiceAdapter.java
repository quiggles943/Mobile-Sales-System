package com.example.mss.mobilesalessystem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mrjbe on 14/03/2017.
 */

public class ExpandableInvoiceAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Invoice> invoices;
    private HashMap<Invoice,ArrayList<InvoiceItems>> invoicesItems;

    public ExpandableInvoiceAdapter(Context context, ArrayList<Invoice> invoices, HashMap<Invoice,ArrayList<InvoiceItems>> invoicesItems)
    {
        this.context = context;
        this.invoices = invoices;
        this.invoicesItems = invoicesItems;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition){
        return this.invoicesItems.get(this.invoices.get(listPosition)).get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition)
    {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        final InvoiceItems expandedListItem = (InvoiceItems) getChild(listPosition,expandedListPosition);
        SQLiteDatabase pDB = context.openOrCreateDatabase("ProductDB", MODE_PRIVATE, null);
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Layout for expanded listview needed
            convertView = layoutInflater.inflate(R.layout.invoice_listitem, null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.tv_itemTitle);
        TextView price = (TextView) convertView.findViewById(R.id.tv_total_price);
        TextView format = (TextView) convertView.findViewById(R.id.tv_format);

        price.setText("£"+expandedListItem.getPrice());
        format.setText(expandedListItem.getFormat());

        expandedListTextView.setText(expandedListItem.getItemDescription());
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition){
        return this.invoicesItems.get(this.invoices.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition){
        return this.invoices.get(listPosition);
    }

    @Override
    public int getGroupCount(){
        return this.invoices.size();
    }

    @Override
    public long getGroupId(int listPosition)
    {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        Invoice invoice = (Invoice) getGroup(listPosition);
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //PARENT LISTVIEW LAYOUT NEEDED
            convertView = layoutInflater.inflate(R.layout.basic_groupview_item, null);
        }
        //TEXT VIEW WITHIN PARENT LISTVIEW
        TextView listTitleTextView = (TextView)convertView.findViewById(R.id.tv_groupView_title);
        String currentDate = (String) DateFormat.format("dd MMM  HH:mm:ss", invoice.getDate());
        listTitleTextView.setText("Order : "+invoice.getInvoiceId()+"\n"+ currentDate);
        return convertView;
    }

    @Override
    public boolean hasStableIds(){
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition){
        return true;
    }
}
