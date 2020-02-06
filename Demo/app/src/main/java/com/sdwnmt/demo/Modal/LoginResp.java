package com.sdwnmt.demo.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResp {
    @SerializedName("response")
    @Expose
    private com.sdwnmt.demo.Modal.Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
