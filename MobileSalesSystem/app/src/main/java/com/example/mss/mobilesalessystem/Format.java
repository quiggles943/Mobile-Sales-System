package com.example.mss.mobilesalessystem;

/**
 * Created by Paul on 26/02/2017.
 */

public class Format {
    private String formatId;
    private String formatDescription;
    private boolean frameable;
    private float priceExtra;

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
        this.priceExtra = 0;
    }
    public Format(String FormatId, String FormatDescription, boolean Frameable, float extraPrice)
    {
        this.formatId = FormatId;
        this.formatDescription = FormatDescription;
        this.frameable = Frameable;
        this.priceExtra = extraPrice;
    }


    public String getFormatId(){ return formatId; }


    public String getFormatDescription(){ return formatDescription ; }

    public void setFormatDescription(String description){ formatDescription = description; }

    public void setFrameable(boolean Frameable) { frameable = Frameable; }

    public boolean getFrameable(){ return frameable; }

    public void setExtraPrice(float extraPrice) { priceExtra = extraPrice; }

    public float getExtraPrice(){ return priceExtra; }


}
