package com.kido.freedom.model.ServerResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kido.freedom.model.News;

public class ServerNewsResponse {
    @SerializedName("Message")
    @Expose(serialize = false, deserialize = true)
    private String message;

    @SerializedName("StatusResponse")
    @Expose(serialize = false, deserialize = true)
    private int statusResponse;

    @SerializedName("Value")
    @Expose(serialize = false, deserialize = true)
    private News value;

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

    public News getValue() {
        return value;
    }

    public void setValue(News value) {
        this.value = value;
    }
}
