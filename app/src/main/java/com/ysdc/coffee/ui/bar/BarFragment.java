package com.ysdc.coffee.ui.bar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysdc.coffee.R;
import com.ysdc.coffee.ui.base.BaseFragment;
import com.ysdc.coffee.ui.utils.MenuDisplayer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BarFragment extends BaseFragment implements BarMvpView, MenuDisplayer {

    private Integer menuId;

    @BindView(R.id.layout_close)
    protected RelativeLayout closeLayout;
    @BindView(R.id.layout_open)
    protected RelativeLayout openLayout;
    @BindView(R.id.status_queue)
    protected TextView statusQueue;

    @Inject
    BarMvpPresenter<BarMvpView> presenter;

    public static BarFragment newInstance() {
        return new BarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar, container, false);
        getFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(BarFragment.this);
        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    protected void setUp(View view) {
        updateView();
    }

    @Override
    public boolean shouldToolbarBeElevated() {
        return false;
    }

    @Override
    public String getCustomTitle() {
        return getString(R.string.title_bar);
    }

    @Override
    public boolean isActionBarVisible() {
        return false;
    }

    @Override
    public int getMenu() {
        if (menuId != null) {
            return menuId;
        } else {
            return R.menu.menu_empty;
        }
    }

    @Override
    public void showMenu(Integer menuId) {
        this.menuId = menuId;
        if (getBaseActivity() != null) {
            getBaseActivity().supportInvalidateOptionsMenu();
        }
    }

    public void updateView(){

    }
}
