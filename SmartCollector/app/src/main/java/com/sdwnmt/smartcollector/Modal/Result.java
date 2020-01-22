
package com.sdwnmt.smartcollector.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("workerid")
    @Expose
    private String workerid;
    @SerializedName("ward_id")
    @Expose
    private String ward;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("zoneid")
    @Expose
    private String zoneid;
    @SerializedName("role_id")
    @Expose
    private String roleId;
    @SerializedName("vehicle")
    @Expose
    private String vehicle;
    @SerializedName("route")
    @Expose
    private String route;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("plotList")
    @Expose
    private List<PlotList> plotList = null;

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }


    public String getWorkerid() {
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneid() {
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PlotList> getPlotList() {
        return plotList;
    }

    public void setPlotList(List<PlotList> plotList) {
        this.plotList = plotList;
    }

}
