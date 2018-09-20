package com.ysdc.coffee.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.ysdc.coffee.R;
import com.ysdc.coffee.ui.bar.BarFragment;
import com.ysdc.coffee.ui.base.BaseActivity;
import com.ysdc.coffee.ui.base.BaseFragment;
import com.ysdc.coffee.ui.history.HistoryFragment;
import com.ysdc.coffee.ui.order.OrderFragment;
import com.ysdc.coffee.ui.utils.FragmentAdapter;
import com.ysdc.coffee.ui.utils.NoSwipePager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends BaseActivity implements HomeMvpView, AHBottomNavigation.OnTabSelectedListener {

    private static final int TAB_BAR = 0;
    private static final int TAB_ORDER = 1;
    private static final int TAB_HISTORY = 2;
    private static final int NO_ELEVATION = 0;
    private static final int DEFAULT_ELEVATION = 8;
    @BindView(R.id.bottom_navigation)
    protected AHBottomNavigation bottomNavigation;
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    @BindView(R.id.homeContainer)
    protected NoSwipePager viewPager;
    @BindView(R.id.fragmentContainer)
    protected FrameLayout fragmentContainer;
    //    @BindView(R.id.barLayout)
//    AppBarLayout barLayout;
    @Inject
    HomeMvpPresenter<HomeMvpView> presenter;
    private FragmentAdapter adapter;
    private BarFragment barFragment;
    private OrderFragment orderFragment;
    private HistoryFragment historyFragment;

    private int curTabId;
    private BaseFragment curFragment;

    public static Intent getInstance(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        presenter.onAttach(HomeActivity.this);

        initializeView();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Timber.d("Activity onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.d("Activity onStop");
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public boolean onTabSelected(int position, boolean wasSelected) {
        hideKeyboard();

        if (wasSelected) {
            if (needBackToRoot()) {
                Timber.d("current tab already selected: %d", bottomNavigation.getCurrentItem());
                backToRoot();
            }
        } else {
            if (curFragment != null) {
                pushFragmentToBackStack(curTabId, curFragment);
            }
            curTabId = position;
            switch (curTabId) {
                case TAB_BAR:
                    barFragment.updateContent();
                    setMenuDisplayer(barFragment);
                    break;
                case TAB_ORDER:
                    setMenuDisplayer(orderFragment);
                    break;
                case TAB_HISTORY:
                    historyFragment.updateContent();
                    setMenuDisplayer(historyFragment);
                    break;
                default:
                    setMenuDisplayer(null);
                    break;
            }
            BaseFragment fragment = (BaseFragment) popFragmentFromBackStack(curTabId);
            if (fragment != null) {
                replaceFragment(fragment, false);
            } else {
                backToRoot();
            }
            viewPager.setCurrentItem(position);
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void displayResultTab() {
        bottomNavigation.setCurrentItem(TAB_HISTORY);
    }

    private void setActionBarVisible(boolean actionBarVisible) {
        if (actionBarVisible != getSupportActionBar().isShowing()) {
            if (actionBarVisible) {
                getSupportActionBar().show();
            } else {
                getSupportActionBar().hide();
            }
        }
    }

    public void requireBarElevation(boolean required) {
//        barLayout.setElevation(required ? DEFAULT_ELEVATION : NO_ELEVATION);
    }

    private void initializeView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);

        viewPager.setPagingEnabled(false);
        adapter = new FragmentAdapter(getSupportFragmentManager());
        barFragment = BarFragment.newInstance();
        orderFragment = OrderFragment.newInstance();
        historyFragment = HistoryFragment.newInstance();

        adapter.addFragments(barFragment);
        adapter.addFragments(orderFragment);
        adapter.addFragments(historyFragment);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        bottomNavigation.setOnTabSelectedListener(this);
        bottomNavigation.addItem(new AHBottomNavigationItem(R.string.title_bar, R.drawable.ic_logo, R.color.white));
        bottomNavigation.addItem(new AHBottomNavigationItem(R.string.title_order, R.drawable.ic_coffee, R.color.white));
        bottomNavigation.addItem(new AHBottomNavigationItem(R.string.title_History, R.drawable.ic_history, R.color.white));
        bottomNavigation.setBehaviorTranslationEnabled(false);

        bottomNavigation.setColoredModeColors(ContextCompat.getColor(this, R.color.colorPrimaryDark), ContextCompat.getColor(this, R.color.dark));
        bottomNavigation.setColored(true);

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        bottomNavigation.setCurrentItem(TAB_BAR);
        setMenuDisplayer(barFragment);
    }

    private void showTopAndBottomBars() {
//        barLayout.setExpanded(true, true);
    }

    private void setBottomBarVisible(boolean bottomBarVisible) {
        if (bottomBarVisible) {
            if (bottomNavigation.isHidden()) {
                bottomNavigation.restoreBottomNavigation();
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
                params.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.bottom_navigation_height));
                fragmentContainer.setLayoutParams(params);
            }
        } else {
            bottomNavigation.hideBottomNavigation(true);
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) fragmentContainer.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            fragmentContainer.setLayoutParams(params);
        }
    }

    @Override
    public void onBackPressed() {

        removeCurrentFragment();

        Fragment fragment = popFragmentFromBackStack(curTabId);
        if (fragment != null) {
            backTo(curTabId, (BaseFragment) fragment);
        } else {
            if (viewPager.getVisibility() == View.GONE) {
                backToRoot();
            } else {
                super.onBackPressed();
            }
        }
    }

    private void backTo(int tabId, @NonNull BaseFragment fragment) {
        if (curTabId != tabId) {
            curTabId = tabId;
            bottomNavigation.setCurrentItem(curTabId);
        }
        replaceFragment(fragment, true);
        getSupportFragmentManager().executePendingTransactions();
    }

    private void backToRoot() {

        fragmentContainer.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        curFragment = null;

        clearBackStack(curTabId);
        BaseFragment fragment = adapter.getItemAtPosition(curTabId);
        if (fragment.isAdded()) {
            requireBarElevation(fragment.shouldToolbarBeElevated());
            getSupportActionBar().setTitle(fragment.getCustomTitle());
            setActionBarVisible(fragment.isActionBarVisible());
            setBottomBarVisible(fragment.isBottomBarVisible());

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            showMenu(null);
        }
    }

    /**
     * @return true if the viewpager is not visible (meaning we are looking at a detail fragment)
     */
    private boolean needBackToRoot() {
        return viewPager.getVisibility() == View.GONE;
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        showTopAndBottomBars();
        if (curFragment != null && addToBackStack) {
            pushFragmentToBackStack(curTabId, curFragment);
        }
        replaceFragment((BaseFragment) fragment, true);
    }

    private void replaceFragment(@NonNull BaseFragment fragment, boolean animate) {
        if (curFragment == null) {
            fragmentContainer.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
        }
        requireBarElevation(fragment.shouldToolbarBeElevated());
        getSupportActionBar().setTitle(fragment.getCustomTitle());
        setActionBarVisible(fragment.isActionBarVisible());
        setBottomBarVisible(fragment.isBottomBarVisible());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();

        tr.replace(R.id.fragmentContainer, fragment, fragment.getClass().getName());
        tr.commitAllowingStateLoss();

        curFragment = fragment;
    }

    private void removeCurrentFragment() {
        if (curFragment != null) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(curFragment.getClass().getName());
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }
}
