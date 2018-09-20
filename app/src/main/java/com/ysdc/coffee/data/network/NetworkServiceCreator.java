package com.ysdc.coffee.data.network;

import android.app.Application;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.ysdc.coffee.app.GeneralConfig;
import com.ysdc.coffee.data.network.config.NetworkConfig;
import com.ysdc.coffee.data.network.config.interceptor.AuthorizationInterceptor;
import com.ysdc.coffee.data.network.config.interceptor.ConnectivityInterceptor;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.utils.NetworkUtils;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by david on 2/7/18.
 */

public abstract class NetworkServiceCreator {

    private static final long TIMEOUT_IN_SECONDS = 20;

    private final Gson gson;
    private Retrofit retrofit;
    private final NetworkConfig networkConfig;
    private final GeneralConfig generalConfig;
    private final OkHttpClient.Builder httpClient;
    private final NetworkUtils networkUtils;
    private final MyPreferences preferences;

    public NetworkServiceCreator(Gson gson, NetworkConfig networkConfig, GeneralConfig generalConfig, Application application, NetworkUtils networkUtils,
                                 MyPreferences preferences) {
        this.gson = gson;
        this.networkConfig = networkConfig;
        this.generalConfig = generalConfig;
        this.networkUtils = networkUtils;
        this.preferences = preferences;

        this.httpClient = new OkHttpClient.Builder();
        this.httpClient.connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
        this.httpClient.readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        httpClient.cookieJar(new JavaNetCookieJar(cookieManager));

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.d(message));
        if (generalConfig.isDebug()) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addNetworkInterceptor(new StethoInterceptor());
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        this.httpClient.addInterceptor(httpLoggingInterceptor);
        this.httpClient.addInterceptor(new AuthorizationInterceptor(preferences));
        this.httpClient.addInterceptor(new ConnectivityInterceptor(application.getApplicationContext(), networkUtils));
    }

    /**
     * Hook to add custom request/response interceptors
     */
    protected void addInterceptor(Interceptor interceptor) {
        httpClient.addInterceptor(interceptor);
    }

    protected Retrofit buildRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(networkConfig.getBaseUrl())
                    .client(httpClient.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    protected OkHttpClient.Builder getHttpClient() {
        return httpClient;
    }
}
