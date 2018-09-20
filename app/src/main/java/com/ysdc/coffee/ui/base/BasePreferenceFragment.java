package com.ysdc.coffee.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.XmlRes;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.util.TypedValue;
import android.view.View;

import com.ysdc.coffee.R;
import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.exception.NoConnectivityException;
import com.ysdc.coffee.injection.component.ActivityComponent;
import com.ysdc.coffee.injection.component.FragmentComponent;
import com.ysdc.coffee.injection.module.FragmentModule;

import butterknife.Unbinder;

/**
 * Created by david on 28/3/18.
 */

public abstract class BasePreferenceFragment extends PreferenceFragmentCompat implements MvpView {

    private BaseActivity activity;
    private Unbinder unBinder;
    private FragmentComponent fragmentComponent;
    private ContextThemeWrapper contextThemeWrapper;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(getPreferenceResource());
        resolveContextThemeWrapper();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUp(view);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.activity = activity;
            activity.onFragmentAttached();
        }
    }

    @XmlRes
    protected abstract int getPreferenceResource();

    protected abstract void setUp(View view);

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof NoConnectivityException) {
            onError(getString(R.string.exception_no_connectivity));
        } else {
            onError(getString(R.string.exception_undefined));
        }
    }

    @Override
    public void onError(String message) {
        if (activity != null) {
            activity.onError(message);
        }
    }

    @Override
    public void onError(@StringRes int resId) {
        if (activity != null) {
            activity.onError(resId);
        }
    }

    @Override
    public void showMessage(String message) {
        if (activity != null) {
            activity.showMessage(message);
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        if (activity != null) {
            activity.showMessage(resId);
        }
    }

    @Override
    public boolean isNetworkConnected() {
        if (activity != null) {
            return activity.isNetworkConnected();
        }
        return false;
    }

    @Override
    public void onDetach() {
        activity = null;
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (unBinder != null) {
            unBinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void hideKeyboard() {
        if (activity != null) {
            activity.hideKeyboard();
        }
    }

    @Override
    public void restartApp() {
        getBaseActivity().restartApp();
    }

    public ActivityComponent getActivityComponent() {
        if (activity != null) {
            return activity.getActivityComponent();
        }
        return null;
    }

    public FragmentComponent getFragmentComponent() {
        if ((fragmentComponent == null) && (activity != null)) {
            fragmentComponent = ((MyApplication) activity.getApplication())
                    .getAppComponent()
                    .childFragmentComponent(new FragmentModule(this));
        }
        return fragmentComponent;
    }

    public void setUnBinder(Unbinder unBinder) {
        this.unBinder = unBinder;
    }

    public ContextThemeWrapper getContextThemeWrapper() {
        return contextThemeWrapper;
    }

    public BaseActivity getBaseActivity() {
        return activity;
    }

    private ContextThemeWrapper resolveContextThemeWrapper() {
        Context activityContext = getActivity();
        TypedValue themeTypedValue = new TypedValue();
        activityContext.getTheme().resolveAttribute(R.attr.preferenceTheme, themeTypedValue, true);
        contextThemeWrapper = new ContextThemeWrapper(activityContext, themeTypedValue.resourceId);
        return contextThemeWrapper;
    }

    @Override
    public Resources provideResources() {
        return getResources();
    }
}