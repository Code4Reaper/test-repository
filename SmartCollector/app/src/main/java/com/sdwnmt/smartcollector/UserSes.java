package com.sdwnmt.smartcollector;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSes {
    private String fullname,roleid,email,zoneid,token,workerid,ward,vehicle,route;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String json;
    private String trip;

    public UserSes (Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
    }

    public void removeUser(){
        sharedPreferences.edit().clear().apply();
    }


    public String getToken() {
        token = sharedPreferences.getString("toke","");
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        sharedPreferences.edit().putString("toke",token).apply();
    }

    public String getWorkerid() {
        workerid = sharedPreferences.getString("Wid","");
        return workerid;
    }

    public void setWorkerid(String workerid) {
        this.workerid = workerid;
        sharedPreferences.edit().putString("Wid",workerid).apply();
    }


    public String getFullname() {
        fullname = sharedPreferences.getString("fullname","");
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
        sharedPreferences.edit().putString("fullname",fullname).apply();
    }

    public String getRoleid() {
        roleid = sharedPreferences.getString("roleid","");
        return roleid;
    }

    public String getWard() {
        ward = sharedPreferences.getString("wardId","");
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
        sharedPreferences.edit().putString("wardId",ward).apply();
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
        sharedPreferences.edit().putString("roleid",roleid).apply();
    }

    public String getEmail() {
        email = sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferences.edit().putString("email",email).apply();
    }

    public String getZoneid() {
        zoneid = sharedPreferences.getString("zoneid","");
        return zoneid;
    }

    public void setZoneid(String zoneid) {
        this.zoneid = zoneid;
        sharedPreferences.edit().putString("zoneid",zoneid).apply();
    }

    public String getJson() {
        json = sharedPreferences.getString("plotList","");
        return json;
    }

    public void setJson(String json) {
        this.json = json;
        sharedPreferences.edit().putString("plotList",json).apply();
    }

    public String getVehicle() {
        vehicle = sharedPreferences.getString("vehicle","");
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
        sharedPreferences.edit().putString("vehicle",vehicle).apply();
    }

    public String getRoute() {
        route = sharedPreferences.getString("Route","");
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
        sharedPreferences.edit().putString("Route",route).apply();
    }

    public void setTripSes(String trip){
        this.trip = trip;
        sharedPreferences.edit().putString("Trip",trip).apply();
    }

    public String getTripSes(){
        trip = sharedPreferences.getString("Trip","");
        return trip;
    }
}
