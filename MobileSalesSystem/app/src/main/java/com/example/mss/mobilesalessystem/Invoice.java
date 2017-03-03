package com.example.mss.mobilesalessystem;

import java.sql.Date;

/**
 * Created by Paul on 03/03/2017.
 */

public class Invoice {
    private int invoiceId;
    private Date date;
    private int event;
    private int customer;

    public Invoice(int id, String date, int event, int customer)
    {
        this.invoiceId = id;
        this.date = Date.valueOf(date);
        this.event = event;
        this.customer = customer;
    }

    public int getInvoiceId(){ return this.invoiceId; }

    public Date getDate() { return this.date; }

    public int getEvent(){ return this.event;}

    public  int getCustomer() { return this.customer; }
}
