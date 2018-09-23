package com.ysdc.coffee.ui.status;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ysdc.coffee.R;
import com.ysdc.coffee.ui.base.BaseFragment;
import com.ysdc.coffee.ui.utils.MenuDisplayer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class StatusFragment extends BaseFragment implements StatusMvpView, MenuDisplayer {

    private Integer menuId;

    @BindView(R.id.bar_status_img)
    protected ImageView barImg;
    @BindView(R.id.button_change_status)
    protected Button changeStatusButton;
    @BindView(R.id.close_desc)
    protected TextView description;
    @BindView(R.id.progress)
    protected ProgressBar progressBar;

    private CompositeDisposable compositeDisposable;

    @Inject
    StatusMvpPresenter<StatusMvpView> presenter;

    public static StatusFragment newInstance() {
        return new StatusFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        getFragmentComponent().inject(this);
        setUnBinder(ButterKnife.bind(this, view));
        presenter.onAttach(StatusFragment.this);
        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.onDetach();
        compositeDisposable.dispose();
        super.onDestroyView();
    }

    @Override
    protected void setUp(View view) {
        compositeDisposable = new CompositeDisposable();
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
        presenter.updateContent();
    }

    @Override
    public void updateBar(boolean barOpen) {
        if (barOpen) {
            barImg.setImageDrawable(getResources().getDrawable(R.drawable.illustration_opened));
            changeStatusButton.setText(getString(R.string.bar_open_button));
            description.setText(getString(R.string.bar_open_desc));
        } else {
            barImg.setImageDrawable(getResources().getDrawable(R.drawable.illustration_closed));
            changeStatusButton.setText(getString(R.string.bar_close_button));
            description.setText(getString(R.string.bar_close_desc));
        }
        hideProgress();
    }

    @OnClick(R.id.button_change_status)
    public void switchStatus() {
        showProgress();
        compositeDisposable.add(
                presenter.switchStatus()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(status -> updateBar(status), throwable -> onError(throwable)));
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
