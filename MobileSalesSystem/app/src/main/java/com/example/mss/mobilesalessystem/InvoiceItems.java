package com.example.mss.mobilesalessystem;

/**
 * Created by mrjbe on 14/03/2017.
 */

public class InvoiceItems  {

    private int invoiceID;
    private String itemID;
    private String itemDescription;

    public InvoiceItems ()
    {
        //standard constructor
    }

    public InvoiceItems(int invoiceID, String itemID, String itemDescription)
    {
        this.invoiceID = invoiceID;
        this.itemID = itemID;
        this.itemDescription = itemDescription;

    }

    public void setInvoiceID (int invoiceID)
    {
        this.invoiceID = invoiceID;
    }

    public int getInvoiceID ()
    {
        return this.invoiceID;
    }

    public void setItemID (String itemID)
    {
        this.itemID = itemID;
    }

    public String getItemID ()
    {
        return this.itemID;
    }

    public void setItemDescription (String itemDescription)
    {
        this.itemDescription = itemDescription;
    }

    public String getItemDescription ()
    {
        return this.itemDescription;
    }

}
