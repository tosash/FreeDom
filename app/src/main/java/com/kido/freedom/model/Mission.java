package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mission {

    @SerializedName("Items")
    @Expose(serialize = true, deserialize = true)
    private List<MissionTaskStatus> mTasks;

    @SerializedName("NextMissionPoints")
    @Expose(serialize = true, deserialize = true)
    private long mPointMax;

    @SerializedName("Number")
    @Expose(serialize = true, deserialize = true)
    private long mNumber;

    @SerializedName("Points")
    @Expose(serialize = true, deserialize = true)
    private long mPoints;

    public List<MissionTaskStatus> getmTasks() {
        return mTasks;
    }

    public void setmTasks(List<MissionTaskStatus> mTasks) {
        this.mTasks = mTasks;
    }

    public long getmPointMax() {
        return mPointMax;
    }

    public void setmPointMax(long mPointMax) {
        this.mPointMax = mPointMax;
    }

    public long getmNumber() {
        return mNumber;
    }

    public void setmNumber(long mNumber) {
        this.mNumber = mNumber;
    }

    public long getmPoints() {
        return mPoints;
    }

    public void setmPoints(long mPoints) {
        this.mPoints = mPoints;
    }
}
