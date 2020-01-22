package com.sdwnmt.smartcollector.Modal;

import com.google.gson.annotations.SerializedName;

public class Worker {
    @SerializedName("response")
    private String Response;

    @SerializedName("name")
    private String Name;


    public void setResponse(String response) {
        Response = response;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getResponse() {
        return Response;
    }
}
