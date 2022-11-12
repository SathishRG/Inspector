package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class CountData {
    @SerializedName("totalreopen")
    private int totalreopen;

    @SerializedName("totalcompleted")
    private int totalcompleted;

    @SerializedName("totalpending")
    private int totalpending;


    @SerializedName("thismonthcompleted")
    private int thismonthcompleted;

    public int getTotalreopen() {
        return totalreopen;
    }

    public void setTotalreopen(int totalreopen) {
        this.totalreopen = totalreopen;
    }

    public int getTotalcompleted() {
        return totalcompleted;
    }

    public void setTotalcompleted(int totalcompleted) {
        this.totalcompleted = totalcompleted;
    }

    public int getTotalpending() {
        return totalpending;
    }

    public void setTotalpending(int totalpending) {
        this.totalpending = totalpending;
    }

    public int getThismonthcompleted() {
        return thismonthcompleted;
    }

    public void setThismonthcompleted(int thismonthcompleted) {
        this.thismonthcompleted = thismonthcompleted;
    }

}
