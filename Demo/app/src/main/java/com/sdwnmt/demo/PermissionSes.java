package com.sdwnmt.demo;

import android.content.Context;
import android.content.SharedPreferences;

public class PermissionSes {
    private Context context;
    private String permission;
    private SharedPreferences sharedPreferences;
    public PermissionSes(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("perminfo",Context.MODE_PRIVATE);
    }

    public String getPermission() {
        permission = sharedPreferences.getString("permission","");
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
        sharedPreferences.edit().putString("permission",permission).apply();

    }
}
