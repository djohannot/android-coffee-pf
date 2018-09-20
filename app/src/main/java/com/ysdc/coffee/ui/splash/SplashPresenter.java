package com.ysdc.coffee.ui.splash;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ysdc.coffee.data.ErrorHandler;
import com.ysdc.coffee.data.repository.UserRepository;
import com.ysdc.coffee.exception.GoogleException;
import com.ysdc.coffee.exception.WrongEmailException;
import com.ysdc.coffee.ui.base.BasePresenter;

import io.reactivex.Completable;
import timber.log.Timber;

import static com.ysdc.coffee.utils.AppConstants.PROPERTY_FINDER_EMAIL;

/**
 * Created by david on 1/25/18.
 */

public class SplashPresenter<V extends SplashMvpView> extends BasePresenter<V> implements SplashMvpPresenter<V> {

    private final UserRepository userRepository;

    public SplashPresenter(ErrorHandler errorHandler, UserRepository userRepository) {
        super(errorHandler);
        this.userRepository = userRepository;
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
    public boolean isUserLoggedIn() {
        return userRepository.isLoggedIn();
    }
}
