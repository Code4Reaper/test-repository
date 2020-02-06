package com.sdwnmt.demo.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("plot")
    @Expose
    private String plot;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("plot_id")
    @Expose
    private String pid;

    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("lane_id")
    @Expose
    private String laneId;

    @SerializedName("token")
    @Expose
    private String token;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLaneId() {
        return laneId;
    }

    public void setLaneId(String laneId) {
        this.laneId = laneId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
