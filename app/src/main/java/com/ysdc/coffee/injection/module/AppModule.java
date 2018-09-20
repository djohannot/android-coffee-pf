package com.ysdc.coffee.injection.module;

import android.app.Application;
import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.ysdc.coffee.app.AppConfig;
import com.ysdc.coffee.app.GeneralConfig;
import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.network.config.GsonCustom;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.injection.annotations.ApplicationContext;
import com.ysdc.coffee.utils.NetworkUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private MyApplication application;

    public AppModule(MyApplication app) {
        application = app;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    public MyApplication providePropertyFinderApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    public Context provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    public MyPreferences provideAppPreferences(@ApplicationContext Context context, Gson gson) {
        return new MyPreferences(context, gson);
    }

    @Provides
    @Singleton
    public ErrorHandler provideErrorHandler(@ApplicationContext Context context) {
        return new ErrorHandler(context);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return GsonCustom.createGson();
    }

    @Provides
    @Singleton
    public GeneralConfig provideGeneralConfig() {
        return new AppConfig(application);
    }

    @Provides
    @Singleton
    public NetworkUtils provideNetworkUtils(){
        return new NetworkUtils();
    }
    @Provides
    @Singleton
    public FirebaseRemoteConfig provideFirebaseRemoteConfig() {
        return FirebaseRemoteConfig.getInstance();
    }
}
