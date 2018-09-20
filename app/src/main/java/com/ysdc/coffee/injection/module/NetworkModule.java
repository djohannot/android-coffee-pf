package com.ysdc.coffee.injection.module;

import android.app.Application;

import com.google.gson.Gson;
import com.ysdc.coffee.app.GeneralConfig;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.config.AppNetworkConfig;
import com.ysdc.coffee.data.network.config.NetworkConfig;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.utils.NetworkUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by david on 2/8/18.
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    NetworkConfig provideNetworkConfig(MyPreferences appPrefs) {
        return new AppNetworkConfig(appPrefs);
    }

    @Provides
    @Singleton
    DefaultNetworkServiceCreator provideDefaultNetworkServiceCreator(Gson gson, NetworkConfig networkConfig, GeneralConfig generalConfig, Application
            application, NetworkUtils networkUtils, MyPreferences preferences) {
        return new DefaultNetworkServiceCreator(gson, networkConfig, generalConfig, application, networkUtils, preferences);
    }

}
