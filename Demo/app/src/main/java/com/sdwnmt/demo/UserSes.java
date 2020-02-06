package com.sdwnmt.demo;

import android.content.Context;
import android.content.SharedPreferences;

import com.sdwnmt.demo.Modal.Response;

public class UserSes {
    private String fullname,age,email,zoneid,jsonString;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String permission;
    private String resp;
    private String json;

    public UserSes(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);
    }
    public void removeUser(){
        sharedPreferences.edit().clear().apply();
    }

    public String getFullname() {
        fullname = sharedPreferences.getString("fullname","");
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
        sharedPreferences.edit().putString("fullname",fullname).apply();
    }

    public String getAge() {
        age = sharedPreferences.getString("age","");
        return age;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
        sharedPreferences.edit().putString("data",jsonString).apply();
    }

    public String getJsonString() {
        jsonString = sharedPreferences.getString("data","");
        return jsonString;
    }

    public void setResp(String resp){
        this.resp = resp;
        sharedPreferences.edit().putString("response",resp).apply();
    }

    public String getResp(){
        resp = sharedPreferences.getString("response","");
        return resp;
    }


}

