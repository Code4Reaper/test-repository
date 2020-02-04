package com.sdwnmt.demo;

import android.content.Context;
import android.content.SharedPreferences;

public class RateSes {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String rate;


    public RateSes(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("ratingDetails",Context.MODE_PRIVATE);
    }

    public String getRate() {
        rate = sharedPreferences.getString("rate","");
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
        sharedPreferences.edit().putString("rate",rate).apply();
    }
}

