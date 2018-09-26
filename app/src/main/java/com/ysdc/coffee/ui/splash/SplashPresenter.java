package com.ysdc.coffee.ui.splash;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.repository.ConfigurationRepository;
import com.ysdc.coffee.data.repository.ProductRepository;
import com.ysdc.coffee.data.repository.PushNotificationRepository;
import com.ysdc.coffee.data.repository.UserRepository;
import com.ysdc.coffee.exception.GoogleException;
import com.ysdc.coffee.exception.WrongEmailException;
import com.ysdc.coffee.ui.base.BasePresenter;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static com.ysdc.coffee.utils.AppConstants.PROPERTY_FINDER_EMAIL;

/**
 * Created by david on 1/25/18.
 */

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    private final UserRepository userRepository;
    private final PushNotificationRepository pushNotificationRepository;
    private final ConfigurationRepository configurationRepository;
    private final ProductRepository productRepository;

    public SplashPresenter(ErrorHandler errorHandler, UserRepository userRepository, PushNotificationRepository pushNotificationRepository,
                           ConfigurationRepository configurationRepository, ProductRepository productRepository) {
        super(errorHandler);
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
        this.pushNotificationRepository = pushNotificationRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Completable analyzeGoogleSignIn(Task<GoogleSignInAccount> completedTask) {
        return Completable.defer(() -> {
            try {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                Timber.d("auth code: %s, \nname: %s\nemail: %s\ntoken: %s\n photo url: %s",
                        account.getServerAuthCode(),
                        account.getDisplayName(),
                        account.getEmail(),
                        account.getIdToken(),
                        account.getPhotoUrl().toString());
                if (account.getEmail().endsWith(PROPERTY_FINDER_EMAIL)) {
                    return userRepository.registerUser(account.getIdToken(), account.getDisplayName(), account.getEmail(), account.getPhotoUrl().toString());
                } else {
                    return Completable.error(new WrongEmailException());
                }
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Timber.e(e, "signInResult:failed code=%d\n%s", e.getStatusCode(), e.getLocalizedMessage());
                userRepository.logout();
                return Completable.error(new GoogleException());
            }
        });
    }

    @Override
    public Completable loadContent() {
        return pushNotificationRepository.registerPushTokenOnBackend()
                .andThen(configurationRepository.refreshConfiguration())
                .andThen(configurationRepository.retrieveDestinations())
                .andThen(productRepository.retrieveIngredients())
                .subscribeOn(Schedulers.io());
    }

    @Override
    public boolean isUserLoggedIn() {
        return userRepository.isLoggedIn();
    }
}
