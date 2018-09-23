package com.ysdc.coffee.ui.orders;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.OrderStatus;
import com.ysdc.coffee.data.model.UserOrder;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by david on 1/25/18.
 */

public class OrdersPresenter<V extends OrdersMvpView> extends BasePresenter<V> implements OrdersMvpPresenter<V> {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrdersPresenter(ErrorHandler errorHandler, ProductRepository productRepository, OrderRepository orderRepository) {
        super(errorHandler);
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Single<List<UserOrder>> getOrders() {
        return orderRepository.getActiveOrders().subscribeOn(Schedulers.io());
    }

    @Override
    public String[] getStatusString() {
        String[] array = new String[OrderStatus.values().length];
        for (int i = 0; i < array.length; i++) {
            array[i] = getMvpView().provideResources().getString(OrderStatus.values()[i].getLocalizableKey());
        }
        return array;
    }

    @Override
    public Completable changeOrderStatus(UserOrder order, int statusIndex) {
        return orderRepository.updateOrder(order.getId(), OrderStatus.values()[statusIndex]);
    }
}
