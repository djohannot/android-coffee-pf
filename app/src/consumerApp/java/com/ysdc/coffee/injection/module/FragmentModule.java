package com.ysdc.coffee.injection.module;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.data.repository.OrderRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.injection.annotations.ActivityScope;
import com.ysdc.coffee.injection.annotations.FragmentScope;
import com.ysdc.coffee.ui.bar.BarMvpPresenter;
import com.ysdc.coffee.ui.bar.BarMvpView;
import com.ysdc.coffee.ui.bar.BarPresenter;
import com.ysdc.coffee.ui.create.CreateOrderMvpPresenter;
import com.ysdc.coffee.ui.create.CreateOrderMvpView;
import com.ysdc.coffee.ui.create.CreateOrderPresenter;
import com.ysdc.coffee.ui.history.HistoryMvpPresenter;
import com.ysdc.coffee.ui.history.HistoryMvpView;
import com.ysdc.coffee.ui.history.HistoryPresenter;
import com.ysdc.coffee.ui.order.OrderMvpPresenter;
import com.ysdc.coffee.ui.order.OrderMvpView;
import com.ysdc.coffee.ui.order.OrderPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment providesFragment() {
        return fragment;
    }

    @Provides
    Activity provideActivity() {
        return fragment.getActivity();
    }

    @Provides
    @ActivityScope
    Context providesContext() {
        return fragment.getActivity();
    }

    @Provides
    @FragmentScope
    BarMvpPresenter<BarMvpView> provideBarPresenter(ErrorHandler errorHandler, ConfigurationRepository configurationRepository, MyPreferences preferences) {
        return new BarPresenter<>(errorHandler, configurationRepository, preferences);
    }

    @Provides
    @FragmentScope
    OrderMvpPresenter<OrderMvpView> provideOrderPresenter(ErrorHandler errorHandler, ProductRepository productRepository, OrderRepository orderRepository) {
        return new OrderPresenter<>(errorHandler, productRepository, orderRepository);
    }

    @Provides
    @FragmentScope
    HistoryMvpPresenter<HistoryMvpView> provideHistoryPresenter(ErrorHandler errorHandler, ProductRepository productRepository, OrderRepository orderRepository) {
        return new HistoryPresenter<>(errorHandler, productRepository, orderRepository);
    }

    @Provides
    @FragmentScope
    CreateOrderMvpPresenter<CreateOrderMvpView> provideCreateOrderPresenter(ErrorHandler errorHandler, OrderRepository orderRepository) {
        return new CreateOrderPresenter<>(errorHandler, orderRepository);
    }
}
