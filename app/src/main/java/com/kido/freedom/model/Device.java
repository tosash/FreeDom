package com.kido.freedom.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Device {

    @JsonField(name = "ModelAndVersionPhone")
    public String pModelAndVersionDevice;

    @JsonField(name = "PhoneId")
    public String pDeviceId;

    @JsonField(name = "PushNotificationToken")
    public String pPushNotificationToken;

    @JsonField(name = "TypePhone")
    public int pTypeDevice = 1;

    @JsonField (name = "Value")
    public double idFromServer;


    public String getpModelAndVersionDevice() {
        return pModelAndVersionDevice;
    }

    public void setpModelAndVersionDevice(String pModelAndVersionDevice) {
        this.pModelAndVersionDevice = pModelAndVersionDevice;
    }

    public int getpTypeDevice() {
        return pTypeDevice;
    }

    public void setpTypeDevice(int pTypeDevice) {
        this.pTypeDevice = pTypeDevice;
    }

    public double getIdFromServer() {
        return idFromServer;
    }

    public void setIdFromServer(double idFromServer) {
        this.idFromServer = idFromServer;
    }

    public String getpPushNotificationToken() {
        return pPushNotificationToken;
    }

    public void setpPushNotificationToken(String pPushNotificationToken) {
        this.pPushNotificationToken = pPushNotificationToken;
    }

    public String getpDeviceId() {
        return pDeviceId;
    }

    public void setpDeviceId(String pDeviceId) {
        this.pDeviceId = pDeviceId;
    }


}
