package com.ysdc.coffee.data.network.model;

import com.google.gson.annotations.SerializedName;
import com.ysdc.coffee.data.model.OrderStatus;

public class UpdateOrder {
    @SerializedName("status")
    private int status;

    public UpdateOrder(OrderStatus orderStatus){
        this.status = orderStatus.getValue();
    }
}
