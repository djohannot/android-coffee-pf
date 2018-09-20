package com.ysdc.coffee.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jetradar.multibackstack.BackStackEntry;
import com.jetradar.multibackstack.BackStackManager;
import com.ysdc.coffee.R;
import com.ysdc.coffee.app.MyApplication;
import com.ysdc.coffee.exception.GoogleException;
import com.ysdc.coffee.exception.NoConnectivityException;
import com.ysdc.coffee.exception.NotLoggedException;
import com.ysdc.coffee.injection.component.ActivityComponent;
import com.ysdc.coffee.injection.module.ActivityModule;
import com.ysdc.coffee.ui.utils.MenuDisplayer;
import com.ysdc.coffee.utils.NetworkUtils;

import javax.inject.Inject;

import butterknife.Unbinder;
import timber.log.Timber;


public abstract class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback {

    private static final String STATE_CURRENT_TAB_ID = "STATE_CURRENT_TAB_ID";
    private static final String STATE_BACK_STACK_MANAGER = "STATE_BACK_STACK_MANAGER";
    protected BackStackManager backStackManager;
    @Inject
    NetworkUtils networkUtils;
    private int curTabId;
    private ActivityComponent activityComponent;
    private Unbinder unBinder;
    private MenuDisplayer menuDisplayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent = ((MyApplication) getApplication()).getAppComponent().childActivityComponent(new ActivityModule(this));
        backStackManager = new BackStackManager();
    }

    public ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_TAB_ID, curTabId);
        outState.putParcelable(STATE_BACK_STACK_MANAGER, backStackManager.saveState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        curTabId = savedInstanceState.getInt(STATE_CURRENT_TAB_ID);
        backStackManager.restoreState(savedInstanceState.getParcelable(STATE_BACK_STACK_MANAGER));
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

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

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
        backStackManager = null;
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

    /**
     * @return false if failed to put fragment in back stack. Relates to issue:
     * java.lang.IllegalStateException: Fragment is not currently in the FragmentManager at
     * android.support.v4.app.FragmentManagerImpl.saveFragmentInstanceState(FragmentManager.java:702)
     */
    protected boolean pushFragmentToBackStack(int hostId, @NonNull Fragment fragment) {
        try {
            BackStackEntry entry = BackStackEntry.create(getSupportFragmentManager(), fragment);
            backStackManager.push(hostId, entry);
            return true;
        } catch (Exception e) {
            Log.e("MultiBackStack", "Failed to add fragment to back stack", e);
            return false;
        }
    }

    @Nullable
    protected Fragment popFragmentFromBackStack(int hostId) {
        BackStackEntry entry = backStackManager.pop(hostId);
        return entry != null ? entry.toFragment(this) : null;
    }

    @Nullable
    protected Pair<Integer, Fragment> popFragmentFromBackStack() {
        Pair<Integer, BackStackEntry> pair = backStackManager.pop();
        return pair != null ? Pair.create(pair.first, pair.second.toFragment(this)) : null;
    }

    /**
     * @return false if back stack is missing.
     */
    protected boolean resetBackStackToRoot(int hostId) {
        return backStackManager.resetToRoot(hostId);
    }

    /**
     * @return false if back stack is missing.
     */
    protected boolean clearBackStack(int hostId) {
        return backStackManager.clear(hostId);
    }

    /**
     * @return the number of fragments in back stack.
     */
    protected int backStackSize(int hostId) {
        return backStackManager.backStackSize(hostId);
    }

    public void showFragment(@NonNull Fragment fragment) {
        showFragment(fragment, true);
    }

    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        //Implemented by class that inherit, if they need it
    }

    protected void setMenuDisplayer(MenuDisplayer menuDisplayer) {
        this.menuDisplayer = menuDisplayer;
    }

    public void showMenu(Integer menuId) {
        if (menuDisplayer != null) {
            menuDisplayer.showMenu(menuId);
        }
    }

    public void displayResultTab(){

    }
}