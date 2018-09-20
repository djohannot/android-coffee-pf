package com.ysdc.coffee.ui.history;

import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.ui.base.MvpPresenter;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by david on 26/2/18.
 */

public interface HistoryMvpPresenter<V extends HistoryMvpView> extends MvpPresenter<V> {

    Single<List<OrderedProduct>> getOrderedProduct();

    void addOrder(OrderedProduct orderedProduct);
}