package com.sdwnmt.demo;

import com.google.gson.annotations.SerializedName;

public class UserLog {

    @SerializedName("response")
    String Response;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}
