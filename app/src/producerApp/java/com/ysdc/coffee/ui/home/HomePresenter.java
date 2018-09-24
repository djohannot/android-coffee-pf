package com.ysdc.coffee.ui.home;

import com.ysdc.coffee.BuildConfig;
import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.network.model.Configuration;
import com.ysdc.coffee.data.prefs.MyPreferences;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.ui.base.BasePresenter;

import static com.ysdc.coffee.data.prefs.MyPreferences.USER_TOKEN;

/**
 * Created by david on 1/25/18.
 */

public class HomePresenter<V extends HomeMvpView> extends BasePresenter<V> implements HomeMvpPresenter<V> {

    private final PushNotificationRepository pushNotificationRepository;
    private final ConfigurationRepository configurationRepository;
    private final ProductRepository productRepository;
    private final MyPreferences preferences;

    public HomePresenter(ErrorHandler errorHandler, PushNotificationRepository pushNotificationRepository, ConfigurationRepository configurationRepository,
                         MyPreferences preferences, ProductRepository productRepository) {
        super(errorHandler);
        this.preferences = preferences;
        this.configurationRepository = configurationRepository;
        this.pushNotificationRepository = pushNotificationRepository;
        this.productRepository = productRepository;
        updateContent();
    }

    private void updateContent() {
        preferences.set(USER_TOKEN, BuildConfig.DEFAULT_TOKEN);
        getCompositeDisposable().add(pushNotificationRepository.registerPushTokenOnBackend()
                .andThen(configurationRepository.refreshConfiguration())
                .andThen(configurationRepository.retrieveDestinations())
                .andThen(productRepository.retrieveIngredients())
                .onErrorComplete()
                .doFinally(() -> getMvpView().hideProgress())
                .subscribe());
    }
}