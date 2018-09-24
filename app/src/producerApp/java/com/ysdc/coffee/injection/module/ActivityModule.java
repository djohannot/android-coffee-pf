package com.ysdc.coffee.injection.module;

import android.content.Context;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.data.repository.UserRepository;
import com.ysdc.coffee.injection.annotations.ActivityContext;
import com.ysdc.coffee.injection.annotations.ActivityScope;
import com.ysdc.coffee.ui.base.BaseActivity;
import com.ysdc.coffee.ui.home.HomeMvpPresenter;
import com.ysdc.coffee.ui.home.HomeMvpView;
import com.ysdc.coffee.ui.home.HomePresenter;
import com.ysdc.coffee.ui.splash.SplashMvpPresenter;
import com.ysdc.coffee.ui.splash.SplashMvpView;
import com.ysdc.coffee.ui.splash.SplashPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class ActivityModule {
    private BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return activity;
    }

    @Provides
    @ActivityScope
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(ErrorHandler errorHandler, UserRepository userRepository) {
        return new SplashPresenter<>(errorHandler, userRepository);
    }
    @Provides
    @ActivityScope
    HomeMvpPresenter<HomeMvpView> provideHomePresenter(ErrorHandler errorHandler, PushNotificationRepository pushNotificationRepository,
                                                       ConfigurationRepository configurationRepository, MyPreferences preferences, ProductRepository productRepository) {
        return new HomePresenter<>(errorHandler, pushNotificationRepository, configurationRepository, preferences, productRepository);
    }
}