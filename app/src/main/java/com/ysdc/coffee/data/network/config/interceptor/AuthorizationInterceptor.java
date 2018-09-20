package com.ysdc.coffee.data.network.config.interceptor;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by david on 2/8/18.
 */

public class AuthorizationInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                //TODO: if your services required basic authentication set in the flavors and set them here
                //.header(AUTHORIZATION, Credentials.basic(BuildConfig.BASE_AUTH_USERNAME, BuildConfig.BASE_AUTH_PASSWORD))
                .method(original.method(), original.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
