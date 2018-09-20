package com.ysdc.coffee.ui.order;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.ui.base.BasePresenter;

/**
 * Created by david on 1/25/18.
 */

public class OrderPresenter<V extends OrderMvpView> extends BasePresenter<V> implements OrderMvpPresenter<V> {


    public OrderPresenter(ErrorHandler errorHandler) {
        super(errorHandler);
    }

}
