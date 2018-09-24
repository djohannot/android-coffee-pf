package com.ysdc.coffee.ui.bar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

    @BindView(R.id.bar_status_img)
    protected ImageView barImg;
    @BindView(R.id.layout_close)
    protected RelativeLayout closeLayout;
    @BindView(R.id.layout_open)
    protected RelativeLayout openLayout;
    @BindView(R.id.status_queue)
    protected TextView statusQueue;
    @BindView(R.id.progress)
    protected ProgressBar progressBar;

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
        updateContent();
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

    public void updateContent() {
        showProgress();
        presenter.updateContent();
    }

    @Override
    public void updateBar(boolean barOpen) {
        hideProgress();
        if (barOpen) {
            barImg.setImageDrawable(getResources().getDrawable(R.drawable.illustration_opened));
            openLayout.setVisibility(View.VISIBLE);
            closeLayout.setVisibility(View.GONE);
        } else {
            barImg.setImageDrawable(getResources().getDrawable(R.drawable.illustration_closed));
            openLayout.setVisibility(View.GONE);
            closeLayout.setVisibility(View.VISIBLE);
        }
        barImg.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    private void hideProgress() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
