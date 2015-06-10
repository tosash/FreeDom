package com.kido.freedom.model.ServerResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ServerResponseMessage {

    @SerializedName("Message")
    @Expose(serialize = true, deserialize = true)
    private String message;

    @SerializedName("StatusResponse")
    @Expose(serialize = true, deserialize = true)
    private int statusresponse;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusresponse() {
        return statusresponse;
    }

    public void setStatusresponse(int statusresponse) {
        this.statusresponse = statusresponse;
    }
}
