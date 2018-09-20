package com.ysdc.coffee.data.model;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {
    @SerializedName("0")
    PENDING(0),
    @SerializedName("1")
    IN_PROGRESS(1),
    @SerializedName("2")
    DONE(2),
    @SerializedName("3")
    CANCELED(3);

    private final int value;

    public int getValue() {
        return value;
    }

    private OrderStatus(int value) {
        this.value = value;
    }
}
