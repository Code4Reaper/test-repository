package com.sdwnmt.demo;

import android.content.Context;
import android.content.SharedPreferences;

public class DateSes {

    private SharedPreferences sharedPreferences;
    private Context context;
    private String date;

    public DateSes(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("dateinfo",Context.MODE_PRIVATE);
    }


    public void setDate(String date){
        this.date = date;
        sharedPreferences.edit().putString("Date",date).apply();
    }

    public String getDate(){
        date = sharedPreferences.getString("Date","");
        return date;
    }
}
