package com.example.mss.mobilesalessystem;

/**
 * Created by Paul on 26/02/2017.
 */

public class Format {
    private String productId;
    private String formatId;
    private String formatDescription;
    private boolean frameable;
    private float price;

    public Format(String FormatId, boolean Frameable)
    {
        this.formatId = FormatId;
        this.frameable = Frameable;
    }


    public Format(String FormatId, String FormatDescription, boolean Frameable)
    {
        this.formatId = FormatId;
        this.formatDescription = FormatDescription;
        this.frameable = Frameable;
    }
    public Format(String FormatId, String FormatDescription, boolean Frameable, float Price)
    {
        this.formatId = FormatId;
        this.formatDescription = FormatDescription;
        this.frameable = Frameable;
        this.price = Price;
    }

    public String getProductId(){ return  productId; }

    public void setProductId(String productId) { this.productId = productId; }

    public String getFormatId(){ return formatId; }


    public String getFormatDescription(){ return formatDescription ; }

    public void setFormatDescription(String description){ formatDescription = description; }

    public void setFrameable(boolean Frameable) { frameable = Frameable; }

    public boolean getFrameable(){ return frameable; }

    public void setPrice(float Price) { price = Price; }

    public float getPrice(){ return price; }


}
