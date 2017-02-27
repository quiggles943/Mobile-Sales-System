package com.example.mss.mobilesalessystem;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.Normalizer;

/**
 * Created by 40114815 on 31/01/2017.
 */
public class orderItem implements Parcelable{

    private String itemID;                         //String to hold the item ID, used for database lookup
    private String itemDescription;             //String to hold the description of the item
    public boolean frame;                       //Boolean to hold whether the item is framed
    private Format format;
    private String imageId;                     //String to hold the image ID
//    private Float itemPrice;                    //Float to hold the price of the item, due to decimal place acceptance

    public orderItem() {
        //standard constructor
    }


    public orderItem (String itemID, String itemDescription, Format format, Boolean framed)
    {
        this.itemID = itemID;
        this.itemDescription = itemDescription;
        this.frame = framed;
        this.format = format;
    }

    public static final Parcelable.Creator<orderItem> CREATOR = new Creator<orderItem>() {
        @Override
        public orderItem createFromParcel(Parcel in) {
            return new orderItem(in);
        }

        @Override
        public orderItem[] newArray(int size) {
            return new orderItem[size];
        }
    };

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }


    public Boolean getFramed() { return frame; }

    public String getIsFramed() {
        if(frame){
            return "Yes";
        }
        else{
            return "No";
        }
    }

    public void setFramed(Boolean framed) { this.frame = framed; }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public float getPrice(){ return format.getPrice(); }

//    public Float getItemPrice() {
//        return itemPrice;
//    }

//    public void setItemPrice(Float itemPrice) {
//        this.itemPrice = itemPrice;
//    }

    public void setFormat(Format format){ this.format = format; }

    public Format getFormat(){ return format; }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemID);
        parcel.writeString(itemDescription);
        parcel.writeByte((byte) (frame ? 1: 0));
        parcel.writeString(format.getFormatId());
        parcel.writeString(format.getFormatDescription());
        parcel.writeByte((byte) (format.getFrameable() ? 1:0));
        parcel.writeFloat(format.getPrice());
    }

    public orderItem(Parcel parcel)
    {
        this.frame = false;
        this.itemID = parcel.readString();
        this.itemDescription = parcel.readString();
        this.frame = parcel.readByte() != 0;
        String formatid = parcel.readString();
        String formatDesc = parcel.readString();
        boolean formatFrameable = false;
        formatFrameable = parcel.readByte() != 0;
        float formatPrice = parcel.readFloat();
        this.format = new Format(formatid,formatDesc,formatFrameable,formatPrice);
    }
}
