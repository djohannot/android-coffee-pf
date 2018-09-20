package com.ysdc.coffee.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ysdc.coffee.R;
import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.exception.GoogleException;
import com.ysdc.coffee.exception.NoConnectivityException;
import com.ysdc.coffee.exception.NotLoggedException;
import com.ysdc.coffee.injection.component.ActivityComponent;
import com.ysdc.coffee.injection.module.ActivityModule;
import com.ysdc.coffee.utils.NetworkUtils;

import javax.inject.Inject;

import butterknife.Unbinder;
import timber.log.Timber;


public abstract class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback {

    private int curTabId;
    private ActivityComponent activityComponent;
    private Unbinder unBinder;
    private AlertDialog versionDialog;

    @Inject
    NetworkUtils networkUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = ((MyApplication) getApplication()).getAppComponent().childActivityComponent(new ActivityModule(this));
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onStop() {
        if (versionDialog != null) {
            versionDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onError(Throwable throwable) {
        Timber.e(throwable, "An Error occurred");
        if (throwable instanceof NoConnectivityException) {
            onError(getString(R.string.exception_no_connectivity));
        } else if (throwable instanceof GoogleException) {
            onError(getString(R.string.error_google_connection));
        } else if (throwable instanceof NotLoggedException) {
            onError(getString(R.string.exception_not_logged));
        } else {
            onError(getString(R.string.exception_undefined));
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }


    @Override
    public void onError(String message) {
        Timber.e("ERROR: %s", message);
        showMessage(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return networkUtils.isNetworkConnected(getApplicationContext());
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

    @Override
    protected void onDestroy() {
        if (unBinder != null) {
            unBinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void restartApp() {
        Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
        Runtime.getRuntime().exit(0);
    }

    @Override
    public Resources provideResources() {
        return getResources();
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }
}
