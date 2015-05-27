package com.kido.freedom.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("ModelAndVersionPhone")
    @Expose(serialize = true, deserialize = false)
    private String pModelAndVersionDevice;

    @SerializedName("PhoneId")
    @Expose(serialize = true, deserialize = false)
    private String pDeviceId;

    @SerializedName("PushNotificationToken")
    @Expose(serialize = true, deserialize = false)
    private String pPushNotificationToken;

    @SerializedName("TypePhone")
    @Expose(serialize = true, deserialize = false)
    private String pTypeDevice = "1";

    @SerializedName("Value")
    @Expose(serialize = false, deserialize = true)
    private String profileId;

    @SerializedName("Message")
    @Expose(serialize = false, deserialize = true)
    private String responceMessage;

    @SerializedName("StatusResponse")
    @Expose(serialize = false, deserialize = true)
    private String statusResponse;


    public String getpModelAndVersionDevice() {
        return pModelAndVersionDevice;
    }

    public void setpModelAndVersionDevice(String pModelAndVersionDevice) {
        this.pModelAndVersionDevice = pModelAndVersionDevice;
    }

    public String getStatusResponse() {
        return statusResponse;
    }

    public void setStatusResponse(String statusResponse) {
        this.statusResponse = statusResponse;
    }

    public String getpDeviceId() {
        return pDeviceId;
    }

    public void setpDeviceId(String pDeviceId) {
        this.pDeviceId = pDeviceId;
    }

    public String getpPushNotificationToken() {
        return pPushNotificationToken;
    }

    public void setpPushNotificationToken(String pPushNotificationToken) {
        this.pPushNotificationToken = pPushNotificationToken;
    }

    public String getpTypeDevice() {
        return pTypeDevice;
    }

    public void setpTypeDevice(String pTypeDevice) {
        this.pTypeDevice = pTypeDevice;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getResponceMessage() {
        return responceMessage;
    }

    public void setResponceMessage(String responceMessage) {
        this.responceMessage = responceMessage;
    }
}
