package com.sdwnmt.smartcollector.Modal.ACK;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Acknowledgement {
    @SerializedName("plot_id")
    @Expose
    String plotid;

    @SerializedName("worker_id")
    @Expose
    String workid;

    @SerializedName("date")
    @Expose
    String date;

    @SerializedName("response")
    @Expose
    String Response;

    @SerializedName("success")
    @Expose
    String Success;

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getPlotid() {
        return plotid;
    }

    public void setPlotid(String plotid) {
        this.plotid = plotid;
    }

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }



}
