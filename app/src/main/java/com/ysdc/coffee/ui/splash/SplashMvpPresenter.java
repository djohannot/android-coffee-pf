package com.ysdc.coffee.ui.splash;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.ysdc.coffee.ui.base.MvpPresenter;

import io.reactivex.Completable;

/**
 * Created by david on 26/2/18.
 */

public interface SplashMvpPresenter<V extends SplashMvpView> extends MvpPresenter<V> {

    Completable analyzeGoogleSignIn(Task<GoogleSignInAccount> completedTask);

    boolean isUserLoggedIn();
}
