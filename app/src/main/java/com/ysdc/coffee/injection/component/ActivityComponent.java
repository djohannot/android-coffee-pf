package com.ysdc.coffee.injection.component;

import com.ysdc.coffee.injection.annotations.ActivityScope;
import com.ysdc.coffee.injection.module.ActivityModule;
import com.ysdc.coffee.ui.splash.SplashActivity;

import dagger.Subcomponent;

/**
 * Created by david on 1/25/18.
 */
@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);
}
