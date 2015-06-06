package com.kido.freedom.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Strip {

    @SerializedName("Date")
    @Expose(serialize = true, deserialize = true)
    private String date;

    @SerializedName("Description")
    @Expose(serialize = true, deserialize = true)
    private String desc;

    @SerializedName("Id")
    @Expose(serialize = true, deserialize = true)
    private long id;

    @SerializedName("IsNew")
    @Expose(serialize = true, deserialize = true)
    private boolean stripNew;

    @SerializedName("Name")
    @Expose(serialize = true, deserialize = true)
    private String name;

    @SerializedName("TapeItemType")
    @Expose(serialize = true, deserialize = true)
    private int stripItemType;

    @SerializedName("UrlImage")
    @Expose(serialize = true, deserialize = true)
    private String  url;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isStripNew() {
        return stripNew;
    }

    public void setStripNew(boolean stripNew) {
        this.stripNew = stripNew;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStripItemType() {
        return stripItemType;
    }

    public void setStripItemType(int stripItemType) {
        this.stripItemType = stripItemType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
