package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderStatus;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.UserOrder;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.mapper.OrderMapper;
import com.ysdc.coffee.data.network.model.NetworkOrder;
import com.ysdc.coffee.data.network.model.UpdateOrder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OrderRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private final ProductRepository productRepository;
    private Order currentOrder;

    public OrderRepository(DefaultNetworkServiceCreator networkServiceCreator, ProductRepository productRepository) {
        this.networkServiceCreator = networkServiceCreator;
        this.productRepository = productRepository;
    }

    public void addProductToOrder(OrderEntry entry) {
        if (currentOrder == null) {
            currentOrder = new Order();
        }
        currentOrder.addEntry(entry);
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public Completable sendOrder() {
        return Completable.defer(() -> {
            OrderMapper mapper = new OrderMapper();

            return networkServiceCreator.getCoffeeService().placeOrder(mapper.convertOrder(currentOrder))
                    .doOnSuccess(networkOrder -> {
                        Timber.d("order pushed! %s", networkOrder);
                    }).ignoreElement();
        });
    }

    public void cleanCurrentOrder() {
        currentOrder = new Order();
    }

    public Single<List<OrderedProduct>> getUserOrders() {
        return networkServiceCreator.getCoffeeService().getOrders()
                .subscribeOn(Schedulers.io())
                .map(networkOrders -> {
                    OrderMapper mapper = new OrderMapper();
                    return mapper.parseNetworkOrders(networkOrders);
                });
    }

    public Single<List<UserOrder>> getActiveOrders(){
        return networkServiceCreator.getCoffeeService().getOrders()
                .subscribeOn(Schedulers.io())
                .map(networkOrders -> {
                    OrderMapper mapper = new OrderMapper();
                    return mapper.parseAllUsersOrders(networkOrders);
                });
    }

    public Completable updateOrder(String orderId, OrderStatus orderStatus) {
        return networkServiceCreator.getCoffeeService().updateOrder(orderId, new UpdateOrder(orderStatus))
                .subscribeOn(Schedulers.io())
                .ignoreElement();
    }
}
