package com.sdwnmt.demo;

import android.content.Context;
import android.content.SharedPreferences;


public class NoteSes {
    private String Note;
    private SharedPreferences sharedPreferences;
    private Context context;

    public NoteSes(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("NoteInfo",Context.MODE_PRIVATE);
    }
    public void remove(){
        sharedPreferences.edit().clear().apply();
    }


    public String getNote() {
        Note = sharedPreferences.getString("Note","");
        return Note;
    }

    public void setNote(String note) {
        Note = note;
        sharedPreferences.edit().putString("Note",Note).apply();
    }
}
