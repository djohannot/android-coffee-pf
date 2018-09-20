package com.ysdc.coffee.app;

import com.ysdc.coffee.data.model.Version;

/**
 * Created by david on 2/11/18.
 */

public interface GeneralConfig {

    /**
     * Get a formatted version of the application, used by the tracking
     *
     * @return
     */
    String getFormattedVersion();

    /**
     * @return the version name of the application
     */
    String getVersionName();

    /**
     * @return the version code of the application
     */
    long getVersionCode();

    /**
     * @return an Version Object representing the Current version of the App
     */
    Version getCurrentAppVersion();

    /**
     *
     * @return true if the gradle build is in debug
     */
    boolean isDebug();
}