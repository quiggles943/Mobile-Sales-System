package com.example.mss.mobilesalessystem;

/**
 * Created by 40114815 on 31/01/2017.
 */
public class orderItem {

    private String itemID;                         //String to hold the item ID, used for database lookup
    private String itemDescription;             //String to hold the description of the item
    private String itemSize;                    //String to hold the size of the item, i.e. A4, A5, Keyring
    private Float itemPrice;                    //Float to hold the price of the item, due to decimal place acceptance

    public orderItem() {
        //standard constructor
    }

    public orderItem (String itemID, String itemDescription, String itemSize, Float itemPrice)
    {
        this.itemID = itemID;
        this.itemDescription = itemDescription;
        this.itemSize = itemSize;
        this.itemPrice = itemPrice;
    }

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

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }


}
