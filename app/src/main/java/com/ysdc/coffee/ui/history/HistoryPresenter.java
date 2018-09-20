package com.ysdc.coffee.ui.history;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.ui.base.BasePresenter;

/**
 * Created by david on 1/25/18.
 */

public class HistoryPresenter<V extends HistoryMvpView> extends BasePresenter<V> implements HistoryMvpPresenter<V> {


    public HistoryPresenter(ErrorHandler errorHandler) {
        super(errorHandler);
    }

}
