package com.sdwnmt.smartcollector.Modal.ACK;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class syncACK {

    @SerializedName("status")
    @Expose
    String Status;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
