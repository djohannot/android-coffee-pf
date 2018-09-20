package com.ysdc.coffee.services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.injection.module.ServiceModule;

import java.util.Map;

import javax.inject.Inject;
import timber.log.Timber;


/**
 * Base class for receiving messages from Firebase Cloud Messaging.
 */

public class PushMessagingService extends FirebaseMessagingService {

    @Inject
    PushNotificationRepository notificationRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApplication) getApplication()).getAppComponent()
                .childServiceComponent(new ServiceModule(this)).inject(this);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //TODO
//
//        Timber.d("onMessageReceived: %s", remoteMessage);
//        Map<String, String> notificationData = remoteMessage.getData();
//
//        if (notificationData.isEmpty() || !notificationData.containsKey(PUSH_TYPE_KEY) || !notificationData.containsKey(PUSH_CONTENT_KEY)) {
//            return;
//        }
//
//        notificationRepository.addNotificationMessage(remoteMessage);
    }
}