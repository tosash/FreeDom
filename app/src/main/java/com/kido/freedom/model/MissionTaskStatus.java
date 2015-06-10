package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MissionTaskStatus {

    @SerializedName("IsComplete")
    @Expose(serialize = true, deserialize = true)
    private Boolean mComplete;

    @SerializedName("Name")
    @Expose(serialize = true, deserialize = true)
    private String mTask;

    public Boolean getmComplete() {
        return mComplete;
    }

    public void setmComplete(Boolean mComplete) {
        this.mComplete = mComplete;
    }

    public String getmTask() {
        return mTask;
    }

    public void setmTask(String mTask) {
        this.mTask = mTask;
    }
}
