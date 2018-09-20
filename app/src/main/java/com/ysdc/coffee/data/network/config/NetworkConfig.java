package com.ysdc.coffee.data.network.config;

import android.support.annotation.NonNull;

/**
 * Created by david on 2/7/18.
 */

public interface NetworkConfig {

    @NonNull
    String getBaseUrl();

    void updateBaseUrl();

    String getAuthUsername();

    String getAuthPassword();
}
