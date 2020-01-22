package com.sdwnmt.smartcollector;

import android.content.Context;
import android.content.SharedPreferences;


public class UserNotify {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String note;

    public UserNotify (Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("UserNotification",Context.MODE_PRIVATE);
    }

    public String getNote() {
        note = sharedPreferences.getString("note","");
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        sharedPreferences.edit().putString("note",note).apply();
    }
}
