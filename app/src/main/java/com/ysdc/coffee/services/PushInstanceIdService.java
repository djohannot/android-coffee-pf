package com.ysdc.coffee.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.injection.module.ServiceModule;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Base class to handle Firebase Instance ID token refresh events.
 */

public class PushInstanceIdService extends FirebaseInstanceIdService {

    @Inject
    PushNotificationRepository notificationRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApplication) getApplication()).getAppComponent()
                .childServiceComponent(new ServiceModule(this)).inject(this);
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: %s", refreshedToken);

        sendRegistrationToServer(refreshedToken);
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Call the Network API to push the token to the backend
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        notificationRepository.savePushNotificationToken(token);
    }
}