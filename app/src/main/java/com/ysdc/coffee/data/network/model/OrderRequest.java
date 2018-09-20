package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRequest {
    @SerializedName("destination_id")
    private String destinationId;
    @SerializedName("items")
    private List<Item> items;

    public OrderRequest() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }
}
