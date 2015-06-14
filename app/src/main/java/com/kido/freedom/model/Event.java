package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {
    @SerializedName("Cost")
    @Expose(serialize = true, deserialize = true)
    private double eCost;

    @SerializedName("Date")
    @Expose(serialize = true, deserialize = true)
    private String eDate;

    @SerializedName("Description")
    @Expose(serialize = true, deserialize = true)
    private String eDesc;

    @SerializedName("Id")
    @Expose(serialize = true, deserialize = true)
    private long eId;

    @SerializedName("Name")
    @Expose(serialize = true, deserialize = true)
    private String eName;

    @SerializedName("ReservationDiscontPercent")
    @Expose(serialize = true, deserialize = true)
    private long eDiscount;

    @SerializedName("ShortDescription")
    @Expose(serialize = true, deserialize = true)
    private String eSmallDesc;

    @SerializedName("UrlImage")
    @Expose(serialize = true, deserialize = true)
    private String eImage;

    public double geteCost() {
        return eCost;
    }

    public void seteCost(double eCost) {
        this.eCost = eCost;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public long geteId() {
        return eId;
    }

    public void seteId(long eId) {
        this.eId = eId;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public long geteDiscount() {
        return eDiscount;
    }

    public void seteDiscount(long eDiscount) {
        this.eDiscount = eDiscount;
    }

    public String geteSmallDesc() {
        return eSmallDesc;
    }

    public void seteSmallDesc(String eSmallDesc) {
        this.eSmallDesc = eSmallDesc;
    }

    public String geteImage() {
        return eImage;
    }

    public void seteImage(String eImage) {
        this.eImage = eImage;
    }
}
