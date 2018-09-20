package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.mapper.OrderMapper;

import io.reactivex.Completable;
import timber.log.Timber;

public class OrderRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private Order currentOrder;

    public OrderRepository(DefaultNetworkServiceCreator networkServiceCreator) {
        this.networkServiceCreator = networkServiceCreator;
    }

    public void addProductToOrder(OrderedProduct orderedProduct){
        if(currentOrder == null){
            currentOrder = new Order();
        }
        currentOrder.addOrderedProduct(orderedProduct);
    }

    public Order getCurrentOrder(){
        return currentOrder;
    }

    public Completable sendOrder(){
        return Completable.defer(() -> {
            OrderMapper mapper = new OrderMapper();

            return networkServiceCreator.getCoffeeService().placeOrder(mapper.convertOrder(currentOrder))
                    .doOnSuccess(networkOrder -> {
                        Timber.d("order pushed! %s", networkOrder);
                    }).ignoreElement();
        });
    }
}
