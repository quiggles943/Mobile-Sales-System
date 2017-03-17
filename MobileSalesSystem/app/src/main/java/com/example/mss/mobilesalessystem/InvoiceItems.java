package com.example.mss.mobilesalessystem;

/**
 * Created by mrjbe on 14/03/2017.
 */

public class InvoiceItems extends orderItem {

    private int invoiceID;

    public InvoiceItems ()
    {
        //standard constructor
    }

    public InvoiceItems(int invoiceID, String itemID, String itemDescription, Format format,float price , Boolean framed)
    {
        this.invoiceID = invoiceID;
        this.setItemID(itemID);
        this.setItemDescription(itemDescription);
        this.setFormat(format);
        this.setItemPrice(price);
        this.setFramed(framed);
    }

    public void setInvoiceID (int invoiceID)
    {
        this.invoiceID = invoiceID;
    }

    public int getInvoiceID ()
    {
        return this.invoiceID;
    }

}
