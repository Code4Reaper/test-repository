
package com.sdwnmt.smartcollector.Modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlotList {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("plot_no")
    @Expose
    private String plotNo;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("name")
    @Expose
    private String plotOwner;

    private boolean cardFlag;

    public boolean isCardFlag() {
        return cardFlag;
    }

    public void setCardFlag(boolean cardFlag) {
        this.cardFlag = cardFlag;
    }

    public String getPlotOwner() {
        return plotOwner;
    }

    public void setPlotOwner(String plotOwner) {
        this.plotOwner = plotOwner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlotNo() {
        return plotNo;
    }

    public void setPlotNo(String plotNo) {
        this.plotNo = plotNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
