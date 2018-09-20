package com.ysdc.coffee.ui.order;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by david on 1/25/18.
 */

public class OrderPresenter<V extends OrderMvpView> extends BasePresenter<V> implements OrderMvpPresenter<V> {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderPresenter(ErrorHandler errorHandler, ProductRepository productRepository, OrderRepository orderRepository) {
        super(errorHandler);
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Single<List<Product>> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public List<OrderedProduct> getOrderedProducts() {
        Order order = orderRepository.getCurrentOrder();
        if (order == null) {
            return new ArrayList<>();
        }
        return order.getOrderedProductList();
    }

    @Override
    public OrderedProduct getOrderedProductForProduct(Product product){
        for(OrderedProduct orderedProduct : getOrderedProducts()){
            if(orderedProduct.getProduct().getId().equalsIgnoreCase(product.getId())){
                return orderedProduct;
            }
        }
        return new OrderedProduct(product);
    }

    @Override
    public Completable sendOrder() {
        return orderRepository.sendOrder();
    }
}