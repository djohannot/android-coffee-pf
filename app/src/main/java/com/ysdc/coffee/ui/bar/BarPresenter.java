package com.ysdc.coffee.ui.bar;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.ui.base.BasePresenter;

/**
 * Created by david on 1/25/18.
 */

public class BarPresenter<V extends BarMvpView> extends BasePresenter<V> implements BarMvpPresenter<V> {


    public BarPresenter(ErrorHandler errorHandler) {
        super(errorHandler);
    }

}
