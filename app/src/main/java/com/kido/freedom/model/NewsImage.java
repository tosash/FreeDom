package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsImage {
    @SerializedName("FullImageUrl")
    @Expose(serialize = true, deserialize = true)
    private String fullUrl;

    @SerializedName("MiniImageUrl")
    @Expose(serialize = true, deserialize = true)
    private String miniUrl;

    public String getFullUrl() {
        return fullUrl;
    }

    public void setFullUrl(String fullUrl) {
        this.fullUrl = fullUrl;
    }

    public String getMiniUrl() {
        return miniUrl;
    }

    public void setMiniUrl(String miniUrl) {
        this.miniUrl = miniUrl;
    }
}
