package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderStatus;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.mapper.OrderMapper;
import com.ysdc.coffee.data.network.model.UpdateOrder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OrderRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private final OrderMapper orderMapper;
    private Order currentOrder;

    public OrderRepository(DefaultNetworkServiceCreator networkServiceCreator, ProductRepository productRepository) {
        this.networkServiceCreator = networkServiceCreator;
        this.orderMapper = new OrderMapper(productRepository);
    }

    public Order getCurrentOrder() {
        if (currentOrder == null) {
            currentOrder = new Order();
        }
        return currentOrder;
    }

    public void addProductToOrder(OrderEntry entry) {
        getCurrentOrder().addEntry(entry);
    }

    public Completable sendOrder() {
        return networkServiceCreator.getCoffeeService().placeOrder(orderMapper.convertOrder(currentOrder))
                .subscribeOn(Schedulers.io())
                .doOnSuccess(networkOrder -> Timber.d("order pushed! %s", networkOrder)).ignoreElement();
    }

    public void cleanCurrentOrder() {
        currentOrder = new Order();
    }

    public Single<List<Order>> getUserOrders() {
        return networkServiceCreator.getCoffeeService().getOrders()
                .subscribeOn(Schedulers.io())
                .map(networkOrders -> orderMapper.convertNetworkOrder(networkOrders));
    }

    public void mergeWithCurrentOrder(Order order) {
        for (OrderEntry orderEntry : order.getEntries()) {
            currentOrder.addEntry(orderEntry);
        }
    }

    public Completable updateOrder(String orderId, OrderStatus orderStatus) {
        return networkServiceCreator.getCoffeeService().updateOrder(orderId, new UpdateOrder(orderStatus))
                .subscribeOn(Schedulers.io())
                .ignoreElement();
    }
}
