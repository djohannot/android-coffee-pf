package com.ysdc.coffee.ui.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.BottomSheetDialogFragment;
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

public abstract class BaseBottomSheetFragment extends BottomSheetDialogFragment implements MvpView {

    private BaseActivity activity;
    private Unbinder unBinder;
    private FragmentComponent fragmentComponent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
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

    public BaseActivity getBaseActivity() {
        return activity;
    }

    @Override
    public Resources provideResources() {
        return getResources();
    }
}