package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServerAnswer {

    @SerializedName("Message")
    @Expose(serialize = false, deserialize = true)
    private String message;

    @SerializedName("StatusResponse")
    @Expose(serialize = false, deserialize = true)
    private int statusResponse;

    @SerializedName("Value")
    @Expose(serialize = false, deserialize = true)
    private String value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        message = message;
    }

    public int getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(int statusResponse) {
        statusResponse = statusResponse;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        value = value;
    }


}
