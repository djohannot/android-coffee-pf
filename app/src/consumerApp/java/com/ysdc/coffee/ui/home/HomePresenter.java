package com.ysdc.coffee.ui.home;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.network.model.Configuration;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

/**
 * Created by david on 1/25/18.
 */

public class HomePresenter<V extends HomeMvpView> extends BasePresenter<V> implements HomeMvpPresenter<V> {


    public HomePresenter(ErrorHandler errorHandler) {
        super(errorHandler);
    }
}