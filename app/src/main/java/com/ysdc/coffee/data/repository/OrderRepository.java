package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;

public class OrderRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;

    public OrderRepository(DefaultNetworkServiceCreator networkServiceCreator) {
        this.networkServiceCreator = networkServiceCreator;
    }
}
