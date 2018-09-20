package com.ysdc.coffee.data.network.config.interceptor;

import android.content.Context;

import com.ysdc.coffee.exception.NoConnectivityException;
import com.ysdc.coffee.utils.NetworkUtils;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by david on 14/3/18.
 */

public class ConnectivityInterceptor implements Interceptor {

    private final Context context;
    private final NetworkUtils networkUtils;
    public ConnectivityInterceptor(Context context, NetworkUtils networkUtils) {
        this.context = context;
        this.networkUtils = networkUtils;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!networkUtils.isNetworkConnected(context)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

}