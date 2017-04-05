package com.example.mss.mobilesalessystem;

/**
 * Created by mrjbe on 14/03/2017.
 */

public class InvoiceItems  {

    private int invoiceID;
    private String itemID;
    private String itemDescription;
    private float price;
    private String format;
    private int qty;
    private String imgFilePath;

    public InvoiceItems ()
    {
        //standard constructor
    }

    public InvoiceItems(int invoiceID, String itemID, String itemDescription, int quantity, String imgFilePath)
    {
        this.invoiceID = invoiceID;
        this.itemID = itemID;
        this.itemDescription = itemDescription;
        this.qty = quantity;
        this.imgFilePath = imgFilePath;
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

    public void setPrice(float price){ this.price = price; }

    public float getPrice () { return this.price; }

    public void setFormat(String format){ this.format = format; }

    public String getFormat(){ return this.format; }

    public int getQty() { return this.qty; }

    public String getImgFilePath() { return this.imgFilePath; }
}
