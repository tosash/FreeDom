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
    private int pTypeDevice = 1;

    @SerializedName("Value")
    @Expose(serialize = true, deserialize = true)
    private String profileId;

        public String getpModelAndVersionDevice() {
        return pModelAndVersionDevice;
    }

    public void setpModelAndVersionDevice(String pModelAndVersionDevice) {
        this.pModelAndVersionDevice = pModelAndVersionDevice;
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

    public int getpTypeDevice() {
        return pTypeDevice;
    }

    public void setpTypeDevice(int pTypeDevice) {
        this.pTypeDevice = pTypeDevice;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

}
