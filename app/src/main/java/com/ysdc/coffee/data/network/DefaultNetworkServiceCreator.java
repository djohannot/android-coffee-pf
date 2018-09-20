package com.ysdc.coffee.data.network;

import android.app.Application;

import com.google.gson.Gson;
import com.ysdc.coffee.app.GeneralConfig;
import com.ysdc.coffee.data.network.config.NetworkConfig;
import com.ysdc.coffee.data.network.service.CoffeeService;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.utils.NetworkUtils;

/**
 * Created by david on 2/8/18.
 */

public class DefaultNetworkServiceCreator extends NetworkServiceCreator {
    private CoffeeService networkConfigurationService;

    public DefaultNetworkServiceCreator(Gson gson, NetworkConfig networkConfig, GeneralConfig generalConfig, Application application, NetworkUtils
            networkUtils, MyPreferences preferences) {
        super(gson, networkConfig, generalConfig, application, networkUtils, preferences);
    }

    public CoffeeService getCoffeeService() {
        if (networkConfigurationService == null) {
            networkConfigurationService = buildRetrofit().create(CoffeeService.class);
        }
        return networkConfigurationService;
    }
}
