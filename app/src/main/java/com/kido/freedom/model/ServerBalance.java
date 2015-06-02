package com.kido.freedom.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerBalance {
    @SerializedName("Message")
    @Expose(serialize = false, deserialize = true)
    private String message;

    @SerializedName("StatusResponse")
    @Expose(serialize = false, deserialize = true)
    private int statusResponse;

    @SerializedName("Value")
    @Expose(serialize = false, deserialize = true)
    private Balance value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(int statusResponse) {
        this.statusResponse = statusResponse;
    }

    public Balance getValue() {
        return value;
    }

    public void setValue(Balance value) {
        this.value = value;
    }
}
