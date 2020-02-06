package com.sdwnmt.demo.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordinates {

    @SerializedName("lati")
    @Expose
    String Latitude;

    @SerializedName("long")
    @Expose
    String Longitude;

    @SerializedName("name")
    @Expose
    String Wname;

    @SerializedName("mobile")
    @Expose
    String Mobile;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getWname() {
        return Wname;
    }

    public void setWname(String wname) {
        Wname = wname;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }
}
