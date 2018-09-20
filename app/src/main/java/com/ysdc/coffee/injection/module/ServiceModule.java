package com.ysdc.coffee.injection.module;

import android.app.Service;

import dagger.Module;

/**
 * Created by david on 5/3/18.
 */

@Module
public class ServiceModule {
    private final Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }
}
