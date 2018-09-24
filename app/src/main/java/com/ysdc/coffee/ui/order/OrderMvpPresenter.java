package com.ysdc.coffee.ui.order;

import com.ysdc.coffee.data.model.OrderEntry;
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

    List<OrderEntry> getOrderEntries();

    OrderEntry getOrderEntriesForProduct(Product product);

    Completable sendOrder();

    void cleanCurrentOrder();
}