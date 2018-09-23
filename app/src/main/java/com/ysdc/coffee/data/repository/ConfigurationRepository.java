package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.model.Configuration;
import com.ysdc.coffee.data.prefs.MyPreferences;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ysdc.coffee.data.prefs.MyPreferences.BAR_OPEN;

public class ConfigurationRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private final MyPreferences preferences;

    public ConfigurationRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator) {
        this.preferences = preferences;
        this.networkServiceCreator = networkServiceCreator;
    }

    public Completable refreshConfiguration() {
        return networkServiceCreator.getCoffeeService().getSettings()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(configuration -> preferences.set(BAR_OPEN, configuration.isBarOpen()))
                .ignoreElement();
    }

    public Single<Boolean> updateConfiguration(Boolean isBarOpen){
        return networkServiceCreator.getCoffeeService().updateSettings(new Configuration(isBarOpen))
                .subscribeOn(Schedulers.io())
                .doOnSuccess(configuration -> preferences.set(BAR_OPEN, configuration.isBarOpen()))
                .map(configuration -> configuration.isBarOpen());
    }

    public boolean isBarOpen(){
        return preferences.getAsBoolean(BAR_OPEN,false);
    }

}
