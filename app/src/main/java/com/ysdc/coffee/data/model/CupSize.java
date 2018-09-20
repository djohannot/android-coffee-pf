package com.ysdc.coffee.data.model;

import com.google.gson.annotations.SerializedName;

public enum CupSize {
    @SerializedName("1")
    SMALL(1),
    @SerializedName("2")
    MEDIUM(2),
    @SerializedName("3")
    LARGE(3);

    private final int value;

    public int getValue() {
        return value;
    }


    private CupSize(int value) {
        this.value = value;
    }
}
