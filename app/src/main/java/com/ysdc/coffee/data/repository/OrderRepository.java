package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderEntry;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.mapper.OrderMapper;

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
            OrderMapper mapper = new OrderMapper(productRepository);

            return networkServiceCreator.getCoffeeService().placeOrder(mapper.convertOrder(currentOrder))
                    .doOnSuccess(networkOrder -> {
                        Timber.d("order pushed! %s", networkOrder);
                    }).ignoreElement();
        });
    }

    public void cleanCurrentOrder() {
        currentOrder = null;
    }

    public Single<List<OrderedProduct>> getUserOrders() {
        return networkServiceCreator.getCoffeeService().getOrders()
                .subscribeOn(Schedulers.io())
                .map(networkOrders -> {
                    OrderMapper mapper = new OrderMapper(productRepository);
                    return mapper.parseNetworkOrders(networkOrders);
                });
    }
}
