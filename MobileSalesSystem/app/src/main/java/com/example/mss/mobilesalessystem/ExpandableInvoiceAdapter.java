package com.example.mss.mobilesalessystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            convertView = layoutInflater.inflate(R.layout.invoice_listitem,null);
        }
        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.tv_itemTitle);
        TextView price = (TextView) convertView.findViewById(R.id.tv_total_price);
        TextView format = (TextView) convertView.findViewById(R.id.tv_format);
        TextView qty = (TextView) convertView.findViewById(R.id.tv_qty);
        TextView indvPrice = (TextView) convertView.findViewById(R.id.tv_detail_price);
        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.iv_thumbnail);
        int quantity = expandedListItem.getQty();
        float totalprice = (expandedListItem.getPrice()*quantity);
        float indvprice = expandedListItem.getPrice();
        indvPrice.setText(quantity+" x "+ String.format("£ %.2f",indvprice));
        price.setText(String.format("£ %.2f",totalprice));
        format.setText(expandedListItem.getFormat());

        expandedListTextView.setText(expandedListItem.getItemDescription());
        qty.setText(expandedListItem.getQty()+"");

        thumbnail.setImageBitmap(ImageFromPath.imageFromPath(expandedListItem.getImgFilePath(),context));
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
            convertView = layoutInflater.inflate(R.layout.groupview_listitem, null);
        }
        //TEXT VIEW WITHIN PARENT LISTVIEW
        TextView listTitleTextView = (TextView)convertView.findViewById(R.id.tv_groupView_title);
        TextView listDateTextView = (TextView) convertView.findViewById(R.id.tv_groupView_date);
        TextView listPriceTextView = (TextView) convertView.findViewById(R.id.tv_groupView_price);
        ImageView paymentType = (ImageView)convertView.findViewById(R.id.iv_paymenttype);
        if(invoice.getpaymentMethod().equals("Cash"))
        {
            Drawable drawable = context.getResources().getDrawable(R.drawable.sym_cash);
            paymentType.setImageDrawable(drawable);
        }
        else if(invoice.getpaymentMethod().equals("Paypal"))
        {
            Drawable drawable = context.getResources().getDrawable(R.drawable.sym_paypal);
            paymentType.setImageDrawable(drawable);
        }
        String currentDate = (String) DateFormat.format("dd MMMM  HH:mm:ss", invoice.getDate());
        listTitleTextView.setText("Order : "+invoice.getInvoiceId());
        listDateTextView.setText(currentDate);
        listPriceTextView.setText(String.format("£ %.2f",invoice.getAmountPaid()));
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
