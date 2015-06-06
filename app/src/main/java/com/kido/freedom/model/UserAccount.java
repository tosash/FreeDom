package com.kido.freedom.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class UserAccount {

    @SerializedName("BirthDate")
    @Expose(serialize = true, deserialize = true)
    private String birthday;

    @SerializedName("Cities")
    @Expose(serialize = true, deserialize = true)
    private ArrayList<Cities> cities;

    @SerializedName("CityId")
    @Expose(serialize = true, deserialize = true)
    private int idCity;

    @SerializedName("Email")
    @Expose(serialize = true, deserialize = true)
    private String email;

    @SerializedName("Gender")
    @Expose(serialize = true, deserialize = true)
    private int gender;

    @SerializedName("Id")
    @Expose(serialize = true, deserialize = true)
    private int profileId;

    @SerializedName("IsInvisible")
    @Expose(serialize = true, deserialize = true)
    private boolean sInvisible;

    @SerializedName("Name")
    @Expose(serialize = true, deserialize = true)
    private String name;

    @SerializedName("Phone")
    @Expose(serialize = true, deserialize = true)
    private String phone;

    @SerializedName("UrlImage")
    @Expose(serialize = true, deserialize = true)
    private String image;


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<Cities> getCities() {
        return cities;
    }

    public void setCities(ArrayList<Cities> cities) {
        this.cities = cities;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public boolean issInvisible() {
        return sInvisible;
    }

    public void setsInvisible(boolean sInvisible) {
        this.sInvisible = sInvisible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
