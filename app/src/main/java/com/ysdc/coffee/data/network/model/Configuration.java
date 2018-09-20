package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

public class Configuration {
    @SerializedName("status")
    private Integer status;

    public boolean isBarOpen() {
        return status==1;
    }
}
