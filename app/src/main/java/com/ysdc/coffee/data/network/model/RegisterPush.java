package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

public class RegisterPush {
    @SerializedName("token")
    private String token;

    public RegisterPush(String token){
        this.token = token;
    }
}
