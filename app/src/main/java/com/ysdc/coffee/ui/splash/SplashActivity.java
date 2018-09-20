package com.ysdc.coffee.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.ysdc.coffee.R;
import com.ysdc.coffee.exception.WrongEmailException;
import com.ysdc.coffee.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private CompositeDisposable subscriptions;

    @Inject
    SplashMvpPresenter<SplashMvpView> presenter;
    @BindView(R.id.main_container)
    protected ConstraintLayout mainContainer;
    @BindView(R.id.sign_in_google)
    protected SignInButton signInButton;
    @BindView(R.id.sign_in_desc)
    protected TextView signInDesc;
    @BindView(R.id.sign_in_error)
    protected TextView errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(SplashActivity.this);

        setUp();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        subscriptions.dispose();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.subscriptions = new CompositeDisposable();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_GOOGLE_SIGN_IN:
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                subscriptions.add(
                        presenter.analyzeGoogleSignIn(task).subscribe(() -> {
                            Timber.d("analyzeGoogleSignIn done");
                            showNextActivity();
                        }, throwable -> {
                            if (throwable instanceof WrongEmailException) {
                                showError();
                                googleSignInClient.signOut().addOnCompleteListener(this, task2 -> Timber.d("Login error because of wrong email"));
                            } else {
                                onError(throwable);
                            }
                        })
                );
                break;
            default:
                break;
        }
    }

    private void setUp() {
        if (presenter.isUserLoggedIn()) {
            showNextActivity();
        } else {
            //Initialize google sign in
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail().build();
            googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
            showLogin();
        }
    }

    private void showNextActivity() {
        //startActivity(HomeActivity.getInstance(this));
        finish();
    }

    private void showLogin() {
        TransitionManager.beginDelayedTransition(mainContainer);
        signInButton.setVisibility(View.VISIBLE);
        signInDesc.setVisibility(View.VISIBLE);
    }

    private void hideLogin() {
        signInButton.setVisibility(View.GONE);
        signInDesc.setVisibility(View.GONE);
    }

    private void showError() {
        TransitionManager.beginDelayedTransition(mainContainer);
        errorView.setVisibility(View.VISIBLE);
    }

    private void hideError() {
        TransitionManager.beginDelayedTransition(mainContainer);
        errorView.setVisibility(View.GONE);
    }
}
