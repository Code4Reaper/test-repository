package com.sdwnmt.smartcollector.Modal.ACK;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class endTripAck {
    @SerializedName("response")
    @Expose
    String stat;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
