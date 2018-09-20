package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String imageUrl;

    public ProductResponse() {
        //Nothing to do
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
