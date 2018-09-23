package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("status")
    private Integer status;

    public Configuration(boolean isBarOpen){
        this.status = isBarOpen ? 1 : 0;
    }

    public boolean isBarOpen() {
        return status==1;
    }
}
