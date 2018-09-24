package com.ysdc.coffee.data.repository;

import com.google.firebase.messaging.RemoteMessage;
import com.ysdc.coffee.data.network.DefaultNetworkServiceCreator;
import com.ysdc.coffee.data.network.model.RegisterPush;
import com.ysdc.coffee.data.prefs.MyPreferences;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

import static com.ysdc.coffee.data.prefs.MyPreferences.PUSH_TOKEN;
import static com.ysdc.coffee.data.prefs.MyPreferences.PUSH_TOKEN_STORED;
import static com.ysdc.coffee.utils.AppConstants.EMPTY_STRING;

/**
 * Created by david on 25/2/18.
 */

public class PushNotificationRepository {

    private final MyPreferences appPrefs;
    private final DefaultNetworkServiceCreator networkService;
    private PublishSubject<RemoteMessage> notificationSubject = PublishSubject.create();

    public PushNotificationRepository(MyPreferences appPrefs, DefaultNetworkServiceCreator networkService) {
        this.appPrefs = appPrefs;
        this.networkService = networkService;
    }

    /**
     * Method called from Push service when a new notification is received. The notification is stored in a subject inside this repository. Any class interested
     * in notification can subscribe to the subject and get notified when there is a new push notification.
     *
     * @param message the new push notification
     */
    public void addNotificationMessage(RemoteMessage message) {
        notificationSubject.onNext(message);
    }

    public Observable<RemoteMessage> getNotifications() {
        return notificationSubject;
    }

    /**
     * Method called when the push token has changed. This method just store the new value and update the state of it to inform all future service that the
     * token needs to be updated to the backend when we have a connection.
     *
     * @param token
     */
    public Completable savePushNotificationToken(String token) {
        Timber.d("savePushNotificationToken called");
        appPrefs.set(PUSH_TOKEN, token);
        return registerPushTokenOnBackend();
    }

    /**
     * Store the push token on the backend side. Required by the server to send push notification to this specific device.
     * We use the token stored in the preferences and push it to the backend.
     *
     * @return A completable event when the operation is done.
     */
    public Completable registerPushTokenOnBackend() {
        Timber.d("registerPushTokenOnBackend called");
        return Completable.defer(() -> {
            if(appPrefs.getAsString(PUSH_TOKEN, EMPTY_STRING).isEmpty()){
                return Completable.complete();
            }
            RegisterPush registerPush = new RegisterPush(appPrefs.getAsString(PUSH_TOKEN, EMPTY_STRING));
            return networkService.getCoffeeService().registerPushToken(registerPush)
                    .subscribeOn(Schedulers.io()).doOnError(throwable -> Timber.e(throwable, "ERROR")).ignoreElement();
        });
    }
}
