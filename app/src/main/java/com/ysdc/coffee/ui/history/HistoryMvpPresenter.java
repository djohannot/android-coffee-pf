package com.ysdc.coffee.ui.history;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.ui.base.MvpPresenter;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by david on 26/2/18.
 */

public interface HistoryMvpPresenter<V extends HistoryMvpView> extends MvpPresenter<V> {

    Single<List<Order>> getOrderedProduct();

    void mergeWithCurrentOrder(Order orderedProduct);
}