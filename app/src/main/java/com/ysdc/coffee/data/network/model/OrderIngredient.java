package com.ysdc.coffee.data.network.model;


import com.google.gson.annotations.SerializedName;

public class OrderIngredient {
    @SerializedName("id")
    private String productId;
    @SerializedName("quantity")
    private Integer quantity;

    public OrderIngredient(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
