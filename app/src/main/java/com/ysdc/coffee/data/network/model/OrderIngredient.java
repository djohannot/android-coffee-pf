package com.ysdc.coffee.data.network.model;


import com.google.gson.annotations.SerializedName;

public class OrderIngredient {
    @SerializedName("id")
    private String id;
    @SerializedName("quantity")
    private Integer quantity;

    public OrderIngredient(String id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
