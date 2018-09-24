package com.ysdc.coffee.ui.history;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by david on 1/25/18.
 */

public class HistoryPresenter<V extends HistoryMvpView> extends BasePresenter<V> implements HistoryMvpPresenter<V> {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public HistoryPresenter(ErrorHandler errorHandler, ProductRepository productRepository, OrderRepository orderRepository) {
        super(errorHandler);
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Single<List<Order>> getOrderedProduct() {
        return Single.defer(() -> {
            if (productRepository.getProductsMap().isEmpty()) {
                return Single.just(new ArrayList<>());
            }
            return orderRepository.getUserOrders().subscribeOn(Schedulers.io());
        });
    }

    @Override
    public void mergeWithCurrentOrder(Order order) {
        orderRepository.mergeWithCurrentOrder(order);
    }

}
