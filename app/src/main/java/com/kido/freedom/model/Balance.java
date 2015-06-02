package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Balance {
    @SerializedName("Money")
    @Expose(serialize = true, deserialize = true)
    private double money;

    @SerializedName("Points")
    @Expose(serialize = true, deserialize = true)
    private int points;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
