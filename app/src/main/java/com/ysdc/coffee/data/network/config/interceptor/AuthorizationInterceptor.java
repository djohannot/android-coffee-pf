package com.ysdc.coffee.data.network.config.interceptor;

import com.ysdc.coffee.data.prefs.MyPreferences;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

/**
 * Created by david on 2/8/18.
 */

public class AuthorizationInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";
    private final MyPreferences preferences;

    public AuthorizationInterceptor(MyPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request.Builder requestBuilder = original.newBuilder()
                .header(AUTHORIZATION, preferences.getAsString(MyPreferences.USER_TOKEN, EMPTY_STRING))
                .method(original.method(), original.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
