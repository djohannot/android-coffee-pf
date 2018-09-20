package com.ysdc.coffee.data.network;

import android.app.Application;

import com.google.gson.Gson;
import com.ysdc.coffee.app.GeneralConfig;
import com.ysdc.coffee.data.network.config.NetworkConfig;
import com.ysdc.coffee.data.network.service.NetworkBasicService;
import com.ysdc.coffee.utils.NetworkUtils;

/**
 * Created by david on 2/8/18.
 */

public class DefaultNetworkServiceCreator extends NetworkServiceCreator {
    private NetworkBasicService networkConfigurationService;

    public DefaultNetworkServiceCreator(Gson gson, NetworkConfig networkConfig, GeneralConfig generalConfig, Application application, NetworkUtils networkUtils) {
        super(gson, networkConfig, generalConfig, application, networkUtils);
    }

    public NetworkBasicService getNetworkBasicService() {
        if (networkConfigurationService == null) {
            networkConfigurationService = buildRetrofit().create(NetworkBasicService.class);
        }
        return networkConfigurationService;
    }
}
