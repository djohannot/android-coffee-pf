package com.ysdc.coffee.ui.orders;

import com.ysdc.coffee.data.model.Order;
import com.ysdc.coffee.data.model.OrderedProduct;
import com.ysdc.coffee.data.model.UserOrder;
import com.ysdc.coffee.ui.base.MvpPresenter;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by david on 26/2/18.
 */

public interface OrdersMvpPresenter<V extends OrdersMvpView> extends MvpPresenter<V> {

    Single<List<UserOrder>> getOrders();

    String[] getStatusString();

    Completable changeOrderStatus(UserOrder order, int statusIndex);
}