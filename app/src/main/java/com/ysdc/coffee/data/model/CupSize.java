package com.ysdc.coffee.data.model;

import android.support.annotation.StringRes;

import com.google.gson.annotations.SerializedName;
import com.ysdc.coffee.R;

public enum CupSize {
    @SerializedName("1")
    SMALL(1, R.string.size_small),
    @SerializedName("2")
    MEDIUM(2, R.string.size_medium),
    @SerializedName("3")
    LARGE(3, R.string.size_large);

    private final int value;
    @StringRes
    private final int localizableKey;

    public int getValue() {
        return value;
    }


    private CupSize(int value, @StringRes int key) {
        this.value = value;
        this.localizableKey = key;
    }

    public static CupSize fromId(Integer size) {
        for (CupSize cupSize : values()) {
            if (cupSize.value == size) {
                return cupSize;
            }
        }
        return null;
    }

    public int getLocalizableKey() {
        return localizableKey;
    }
}
