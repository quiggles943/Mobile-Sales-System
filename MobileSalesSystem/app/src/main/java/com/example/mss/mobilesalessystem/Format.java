package com.example.mss.mobilesalessystem;

/**
 * Created by Paul on 26/02/2017.
 */

public class Format {
    private String formatId;
    private String formatDescription;
    private boolean frameable;

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


    public String getFormatId(){ return formatId; }


    public String getFormatDescription(){ return formatDescription ; }

    public void setFormatDescription(String description){ formatDescription = description; }

    public void setFrameable(boolean Frameable) { frameable = Frameable; }

    public boolean getFrameable(){ return frameable; }


}
