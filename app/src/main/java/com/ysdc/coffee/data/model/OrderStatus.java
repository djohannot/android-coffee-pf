package com.ysdc.coffee.data.model;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.ysdc.coffee.R;

public enum OrderStatus {
    @SerializedName("0")
    PENDING(0, R.string.status_pending),
    @SerializedName("1")
    IN_PROGRESS(1, R.string.status_progress),
    @SerializedName("2")
    READY(2, R.string.status_ready),
    @SerializedName("3")
    DONE(3, R.string.status_done),
    @SerializedName("4")
    CANCELED(4, R.string.status_canceled);

    private final int value;
    @StringRes
    private final int localizableKey;

    public int getValue() {
        return value;
    }
    public int getLocalizableKey(){
        return localizableKey;
    }

    private OrderStatus(int value,  @StringRes int key) {
        this.value = value;
        this.localizableKey = key;
    }
}
