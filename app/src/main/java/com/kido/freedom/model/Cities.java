package com.kido.freedom.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cities {

    @SerializedName("Id")
    @Expose(serialize = true, deserialize = true)
    private int idCity;

    @SerializedName("Name")
    @Expose(serialize = true, deserialize = true)
    private String nameCity;

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    @Override
    public String toString() {
        return nameCity;
    }
}
