package com.sdwnmt.demo.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Acknowledge {

    @SerializedName("result")
    @Expose
    String stat;

    public String getRStat() {
        return stat;
    }

    public void setRStat(String stat) {
        this.stat = stat;
    }
}
