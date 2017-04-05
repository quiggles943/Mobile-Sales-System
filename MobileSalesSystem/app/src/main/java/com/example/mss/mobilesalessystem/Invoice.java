package com.example.mss.mobilesalessystem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Paul on 03/03/2017.
 */

public class Invoice implements Parcelable {
    private int invoiceId;
    private Date date;
    private int customer;
    private String paymentMethod;
    private float amountPaid;
    private long timeStamp;

    public Invoice()
    {
        //Standard converter
    }

    public Invoice(int id, String date, int customer, String paymentMethod, float amountPaid)
    {
        java.util.Date utilDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
             utilDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.invoiceId = id;
        this.date = new Date(utilDate.getTime());
        this.customer = customer;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.timeStamp = utilDate.getTime();
    }

    protected Invoice(Parcel in) {
        invoiceId = in.readInt();
        customer = in.readInt();
        paymentMethod = in.readString();
        amountPaid = in.readFloat();
        timeStamp = in.readLong();
        date = new Date(timeStamp);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(invoiceId);
        dest.writeInt(customer);
        dest.writeString(paymentMethod);
        dest.writeFloat(amountPaid);
        dest.writeLong(timeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };

    public int getInvoiceId(){ return this.invoiceId; }

    public Date getDate() { return this.date; }

    public String getpaymentMethod() { return this.paymentMethod; }

    public float getAmountPaid() { return this.amountPaid; }

    public long getTimeStamp(){ return this.timeStamp;}

    public String insertSQLCreator()
    {
        String sql;

        sql = "INSERT INTO invoice (InvoiceID, Date, CustomerID, PaymentMethod, AmountPaid) VALUES(";
        sql += this.invoiceId + ",";
        sql += "'" + this.date + "', ";
        sql += this.customer + ", ";
        sql += "'" + this.paymentMethod + "', ";
        sql += this.amountPaid + ");";

        return sql;
    }

    public int getNextID(SQLiteDatabase pDB)
    {
        int result = 0;

        Cursor c = pDB.rawQuery("SELECT InvoiceID FROM invoice ORDER BY InvoiceID DESC LIMIT 1", null);         //get all the invoice ID's by largest to smallest (largest will be most recent)

        if (c.getCount() > 0)       //if there are some results
        {
            c.moveToFirst();        //move to first (largest due to ORDER BY)
            result = c.getInt(c.getColumnIndex("InvoiceID"));        //the first should be the invoice just created
        }

        result ++;

        return result;
    }

}
