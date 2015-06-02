package com.kido.freedom.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserProfile {

    @SerializedName("CountUnreviewEvents")
    @Expose(serialize = false, deserialize = true)
    private String countOfEvents;

    @SerializedName("Id")
    @Expose(serialize = false, deserialize = true)
    private String profileId;

    @SerializedName("Level")
    @Expose(serialize = false, deserialize = true)
    private String userLevel;

    @SerializedName("Name")
    @Expose(serialize = false, deserialize = true)
    private String userName="";

    @SerializedName("PointsByCurrentLevel")
    @Expose(serialize = false, deserialize = true)
    private String userPoints="";

    @SerializedName("PointsByEndLevel")
    @Expose(serialize = false, deserialize = true)
    private String userPointsNextLevel;

    @SerializedName("StatusUser")
    @Expose(serialize = false, deserialize = true)
    private String userStatus="";

    @SerializedName("UrlImage")
    @Expose(serialize = false, deserialize = true)
    private String userImage="";

    public String getCountOfEvents() {
        return countOfEvents;
    }

    public void setCountOfEvents(String countOfEvents) {
        this.countOfEvents = countOfEvents;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPoints() {
        return userPoints;
    }

    public void setUserPoints(String userPoints) {
        this.userPoints = userPoints;
    }

    public String getUserPointsNextLevel() {
        return userPointsNextLevel;
    }

    public void setUserPointsNextLevel(String userPointsNextLevel) {
        this.userPointsNextLevel = userPointsNextLevel;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}

