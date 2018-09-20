package com.ysdc.coffee.ui.order;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by david on 1/25/18.
 */

public class OrderPresenter<V extends OrderMvpView> extends BasePresenter<V> implements OrderMvpPresenter<V> {

    private final ProductRepository productRepository;

    public OrderPresenter(ErrorHandler errorHandler, ProductRepository productRepository) {
        super(errorHandler);
        this.productRepository = productRepository;
    }

    @Override
    public Single<List<Product>> getProducts() {
        return productRepository.getProduct();
    }

    @Override
    public List<OrderedProduct> getOrderedProducts() {
        //TODO
        return new ArrayList<>();
    }
}
