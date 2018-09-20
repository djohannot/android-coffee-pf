package com.ysdc.coffee.injection.module;

import android.content.Context;

import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.injection.annotations.ActivityContext;
import com.ysdc.coffee.injection.annotations.ActivityScope;
import com.ysdc.coffee.ui.base.BaseActivity;
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
    SplashMvpPresenter<SplashMvpView> provideSplashPresenter(ErrorHandler errorHandler) {
        return new SplashPresenter<>(errorHandler);
    }
}