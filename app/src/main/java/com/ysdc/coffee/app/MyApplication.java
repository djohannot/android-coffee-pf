package com.ysdc.coffee.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.ysdc.coffee.BuildConfig;
import com.ysdc.coffee.injection.component.AppComponent;
import com.ysdc.coffee.injection.component.DaggerAppComponent;
import com.ysdc.coffee.injection.module.AppModule;
import com.ysdc.coffee.utils.CrashlyticsUtils;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by david on 1/23/18.
 */

public class MyApplication extends MultiDexApplication implements Application
        .ActivityLifecycleCallbacks {

    private AppComponent appComponent;

    //Utility
    private int numStarted = 0;

    /**
     * Called when the application is starting, before any other application objects have been
     * created. We initialize the external libraries here also, as it is always called, at the real
     * starting of the app, and only once.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();

        registerActivityLifecycleCallbacks(this);

        if (BuildConfig.DEBUG) {
            //Init Timber
            Timber.plant(new Timber.DebugTree());
            //Init Stetho debug bridge
            initStetho();
            //Init LeakCanary
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
            //Traceur.enableLogging();
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashlyticsUtils.CrashlyticsTree());
            initFacebook();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(MyApplication.this);
    }

    /**
     * Called by the system when the device configuration changes while your component is running.
     *
     * @param newConfig the new configuration
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * This is called when the overall system is running low on memory, and would like actively
     * running processes to tighten their belts
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /**
     * Method used to set a test component
     *
     * @param appComponent the dagger test appComponent
     */
    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());

        //TODO: If you use realm, replace with this line
//                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
    }

    private void initDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        appComponent.inject(this);
    }

    private void initFacebook() {
//        TODO: Uncomment and set the facebook id to use the facebook SDK
//        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        FacebookSdk.setIsDebugEnabled(BuildConfig.DEBUG);
    }

    /**
     * APPLICATION ACTIVITIES LIFECYCLE
     */

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (numStarted == 0) {
            //TODO: If we want to do operations when the application enter its first activitz
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        numStarted--;
        if (numStarted == 0) {

        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
