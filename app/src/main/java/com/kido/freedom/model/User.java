package com.kido.freedom.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;


@JsonObject
public class User {

    @JsonField (name="BirthDate")
    public Date uBirthDate;

    @JsonField (name="CityId")
    public double uCityId;

    @JsonField (name="Email")
    public String uEmail;

    @JsonField (name="Gender")
    public int uGender;

    @JsonField (name="Id")
    public double  uId;

    @JsonField (name = "IsInvisible")
    public boolean uIsInvisible;

    @JsonField (name = "Name")
    public String uName;

    @JsonField (name = "Phome")
    public String uPhone;
}
