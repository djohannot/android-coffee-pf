package com.ysdc.coffee.data.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ysdc.coffee.data.model.Destination;
import com.ysdc.coffee.data.model.Ingredient;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.model.Configuration;
import com.ysdc.coffee.data.prefs.MyPreferences;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static com.ysdc.coffee.data.prefs.MyPreferences.BAR_OPEN;
import static com.ysdc.coffee.data.prefs.MyPreferences.DESTINATIONS;
import static com.ysdc.coffee.data.prefs.MyPreferences.INGREDIENTS;

public class ConfigurationRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private final MyPreferences preferences;
    private final Gson gson;

    private final Map<String, Destination> destinations;


    public ConfigurationRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator, Gson gson) {
        this.networkServiceCreator = networkServiceCreator;
        this.preferences = preferences;
        this.gson = gson;

        this.destinations = new HashMap<>();
        loadFromStorage();
    }

    public Completable refreshConfiguration() {
        return getSettings();
    }

    public Completable retrieveConfiguration(Boolean isBarOpen) {
        return getSettings();
    }

    public boolean isBarOpen() {
        return preferences.getAsBoolean(BAR_OPEN, false);
    }

    public Destination getDestinationForId(String id){
        return destinations.get(id);
    }

    private Completable getSettings() {
        return networkServiceCreator.getCoffeeService().getSettings()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(configuration -> preferences.set(BAR_OPEN, configuration.isBarOpen()))
                .ignoreElement();
    }

    public Completable retrieveDestinations() {
        return networkServiceCreator.getCoffeeService().getDestinations()
                .subscribeOn(Schedulers.io())
                .doOnSuccess(newDestinations -> {
                    destinations.clear();
                    for (Destination destination : newDestinations) {
                        destinations.put(destination.getId(), destination);
                    }
                    preferences.set(DESTINATIONS, destinations);
                })
                .ignoreElement();
    }

    private void loadFromStorage() {
        Type destinationType = new TypeToken<Map<String, Destination>>() {
        }.getType();

        String destinationsJsonFile = preferences.getAsString(DESTINATIONS);
        if (!destinationsJsonFile.isEmpty()) {
            destinations.putAll(gson.fromJson(destinationsJsonFile, destinationType));
        }
    }
}
