package com.sdwnmt.demo.Modal;

import com.google.gson.annotations.SerializedName;

public class FeedbackRate {
    @SerializedName("result")
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
