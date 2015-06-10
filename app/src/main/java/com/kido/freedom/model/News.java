package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class News {

    @SerializedName("AttachedImages")
    @Expose(serialize = true, deserialize = true)
    private ArrayList<NewsImage> nImages;

    @SerializedName("Date")
    @Expose(serialize = true, deserialize = true)
    private String nDate;

    @SerializedName("Description")
    @Expose(serialize = true, deserialize = true)
    private String nDesc;

    @SerializedName("Id")
    @Expose(serialize = true, deserialize = true)
    private long nId;

    @SerializedName("Name")
    @Expose(serialize = true, deserialize = true)
    private String nName;

    public ArrayList<NewsImage> getnImages() {
        return nImages;
    }

    public void setnImages(ArrayList<NewsImage> nImages) {
        this.nImages = nImages;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    public String getnDesc() {
        return nDesc;
    }

    public void setnDesc(String nDesc) {
        this.nDesc = nDesc;
    }

    public long getnId() {
        return nId;
    }

    public void setnId(long nId) {
        this.nId = nId;
    }

    public String getnName() {
        return nName;
    }

    public void setnName(String nName) {
        this.nName = nName;
    }
}
