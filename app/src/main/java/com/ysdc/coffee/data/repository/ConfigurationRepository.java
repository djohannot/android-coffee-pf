package com.ysdc.coffee.data.repository;

import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.prefs.MyPreferences;

import java.util.UUID;

import timber.log.Timber;

import static com.ysdc.coffee.data.prefs.MyPreferences.DEVICE_ID;

public class ConfigurationRepository {

    private final DefaultNetworkServiceCreator networkServiceCreator;
    private final MyPreferences preferences;

    public ConfigurationRepository(MyPreferences preferences, DefaultNetworkServiceCreator networkServiceCreator){
        this.preferences = preferences;
        this.networkServiceCreator = networkServiceCreator;
    }

    /**
     * Build a new device ID is there is not one already existing in the preferences
     *
     * @return the device ID
     */
    private String getDeviceID() {
        if (preferences.getAsString(DEVICE_ID).isEmpty()) {
            preferences.set(DEVICE_ID, UUID.randomUUID().toString());
        }
        return preferences.getAsString(DEVICE_ID);
    }
}
