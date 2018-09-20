package com.ysdc.coffee.ui.order;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.Product;
import com.ysdc.coffee.ui.base.MvpPresenter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by david on 26/2/18.
 */

public interface OrderMvpPresenter<V extends OrderMvpView> extends MvpPresenter<V> {

    Single<List<Product>> getProducts();

    List<OrderedProduct> getOrderedProducts();

    OrderedProduct getOrderedProductForProduct(Product product);

    Completable sendOrder();

    void cleanCurrentOrder();
}