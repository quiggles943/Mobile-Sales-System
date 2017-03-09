package com.example.mss.mobilesalessystem;

import java.sql.Date;

/**
 * Created by Paul on 03/03/2017.
 */

public class Invoice {
    private int invoiceId;
    private Date date;
    private int customer;
    private String paymentMethod;
    private float amountPaid;

    public Invoice(int id, String date, int customer, String paymentMethod, float amountPaid)
    {
        this.invoiceId = id;
        this.date = Date.valueOf(date);
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
    }

    public int getInvoiceId(){ return this.invoiceId; }

    public Date getDate() { return this.date; }

    public String getpaymentMethod() { return this.paymentMethod; }

    public float getAmountPaid() { return this.amountPaid; }
}
