package com.ysdc.coffee.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.ysdc.coffee.ui.home.HomeActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class SplashActivity extends BaseActivity implements SplashMvpView {

    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;
    private CompositeDisposable subscriptions;

    @Inject
    SplashMvpPresenter<SplashMvpView> presenter;
    @BindView(R.id.main_container)
    protected RelativeLayout mainContainer;
    @BindView(R.id.sign_in_google)
    protected SignInButton signInButton;
    @BindView(R.id.sign_in_desc)
    protected TextView signInDesc;
    @BindView(R.id.progress)
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(SplashActivity.this);
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

        setUp();
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
                            hideLogin();
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

    @OnClick(R.id.sign_in_google)
    public void googleSignInClicked() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
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
        subscriptions.add(
                presenter.loadContent()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> showProgress())
                        .doFinally(() -> hideProgress())
                        .subscribe(() -> {
                            startActivity(HomeActivity.getInstance(this));
                            finish();
                        }, throwable -> onError(throwable))
        );
    }

    private void showLogin() {
        signInButton.setVisibility(View.VISIBLE);
        signInDesc.setVisibility(View.VISIBLE);
    }

    private void hideLogin() {
        signInButton.setVisibility(View.GONE);
        signInDesc.setVisibility(View.GONE);
    }

    private void showError() {
        signInDesc.setText(getString(R.string.login_error));
        signInDesc.setTextColor(getResources().getColor(R.color.selection));
    }

    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
